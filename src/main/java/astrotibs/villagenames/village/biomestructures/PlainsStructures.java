package astrotibs.villagenames.village.biomestructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.config.village.VillageGeneratorConfigHandler;
import astrotibs.villagenames.handler.ChestLootHandler;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.FunctionsVN.MaterialType;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.village.StructureVillageVN;
import astrotibs.villagenames.village.StructureVillageVN.StartVN;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityHorse;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.ChestGenHooks;

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
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	public PlainsFountain01() {}
    	
    	public PlainsFountain01(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, float terrainType)
    	{
    		super(chunkManager, componentType, random, posX, posZ, components, terrainType);
    		
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
            	if (this.field_143015_k < 0)
                {
            		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
            						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
            				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
            		
                    if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
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

        	WorldChunkManager chunkManager= world.getWorldChunkManager();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(posX, 64, posZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, posX, posZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, posX, posZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, posX, posZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, posX, posZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
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
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            if (GeneralConfig.useVillageColors)
            {
            	IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 2, 1, 3, 6, 1, 5, concreteBlockstate, concreteBlockstate, false);
                this.fillWithBlocks(world, structureBB, 3, 1, 2, 5, 1, 6, concreteBlockstate, concreteBlockstate, false);
                
                // Under-torch GT
            	BlockPos uvw = new BlockPos(2, 0, 2); // Starting position of the block cluster. Use lowest X, Z.
            	
            	if (GeneralConfig.addConcrete)
            	{
                	int metaBase = ((int)world.getSeed()%4+this.coordBaseMode.getHorizontalIndex())%4; // Procedural based on world seed and base mode
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
                    this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), 2, 0, 2, structureBB);
                    this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), 2, 0, 6, structureBB);
                    this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), 6, 0, 2, structureBB);
                    this.setBlockState(world, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), 6, 0, 6, structureBB);
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
            	IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor2);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor2);
            	}
            	
            	this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 2, 4, concreteBlockstate, concreteBlockstate, false);
            }
            else
            {
            	this.fillWithBlocks(world, structureBB, 4, 1, 4, 4, 2, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            this.setBlockState(world, Blocks.flowing_water.getDefaultState(), 4, 3, 4, structureBB);
            
            
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
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
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
                this.replaceAirAndLiquidDownwards(world, biomeFillerState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.coordBaseMode.getHorizontalIndex(), false)));
				
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
	        			
	        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.11
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
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
		public PlainsMeetingPoint1() {}
		
		public PlainsMeetingPoint1(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, float terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
    		
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
        	StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()<=1? 3 : 4), this.boundingBox.maxY - 5, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
        	// Eastward
        	StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.maxY - 5, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()<=1? 3 : 4), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()<=1? 4 : 3), this.boundingBox.maxY - 5, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.maxY - 5, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()<=1? 4 : 3), EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.field_143015_k < 0)
            {
            	if (this.field_143015_k < 0)
                {
            		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
            						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
            				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
            		
                    if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeCobblestoneSlabState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
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

        	WorldChunkManager chunkManager= world.getWorldChunkManager();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(posX, 64, posZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, posX, posZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, posX, posZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, posX, posZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, posX, posZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
            // The well gets filled completely with water first
            //this.fillWithBlocks(world, structureBoundingBox, 3, -3, 3, 6, 12, 6, this.biomeCobblestoneBlock, Blocks.flowing_water, false);
            this.fillWithBlocks(world, structureBB, 3, -3, 3, 6, 2, 6, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 4, -2, 4, 5, 1, 5, Blocks.flowing_water.getDefaultState(), Blocks.flowing_water.getDefaultState(), false); // Water
            this.fillWithAir(world, structureBB, 4, 2, 4, 5, 2, 5);
            
            // Well rim
            if (GeneralConfig.wellSlabs && GeneralConfig.wellDecorations)
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
            		int metaBase = ((int)world.getSeed()%4+this.coordBaseMode.getHorizontalIndex())%4; // Procedural based on world seed and base mode
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
            		this.fillWithBlocks(world, structureBB, 4, 5, 4, 5, 5, 5, Blocks.stained_hardened_clay.getStateFromMeta(townColor2), Blocks.stained_hardened_clay.getStateFromMeta(townColor2), false);
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
                    		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);
                        	
                        	// Basin rim
                        	if (GeneralConfig.addConcrete)
                        	{
                        		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
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
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
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
                this.replaceAirAndLiquidDownwards(world, biomeFillerState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)));
				
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
	        			
	        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.11
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
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
	    public PlainsMeetingPoint2() {}
		
		public PlainsMeetingPoint2(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, float terrainType)
		{
		    super(chunkManager, componentType, random, posX, posZ, components, terrainType);
    		
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
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()==0 ? 1 : this.coordBaseMode.getHorizontalIndex()==1 ? 10 : this.coordBaseMode.getHorizontalIndex()==2 ? 1 : 2), this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
			// Eastward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()==0 ? 6 : this.coordBaseMode.getHorizontalIndex()==1 ? 1 : this.coordBaseMode.getHorizontalIndex()==2 ? 6 : 1), EnumFacing.EAST, this.getComponentType());
			// Southward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + (this.coordBaseMode.getHorizontalIndex()==0 ? 1 : this.coordBaseMode.getHorizontalIndex()==1 ? 6 : this.coordBaseMode.getHorizontalIndex()==2 ? 1 : 6), this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
			// Westward
			StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + (this.coordBaseMode.getHorizontalIndex()==0 ? 2 : this.coordBaseMode.getHorizontalIndex()==1 ? 1 : this.coordBaseMode.getHorizontalIndex()==2 ? 10 : 1), EnumFacing.WEST, this.getComponentType());
		}
		
		/*
		 * Construct the structure
		 */
		@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.field_143015_k < 0)
            {
            	if (this.field_143015_k < 0)
                {
            		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
            						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
            				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
            		
                    if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodenSlabState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
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

        	WorldChunkManager chunkManager= world.getWorldChunkManager();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(posX, 64, posZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, posX, posZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, posX, posZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, posX, posZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, posX, posZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
	            	else {this.disallowModSubs = false;}
	            	}
				catch (Exception e) {this.disallowModSubs = false;}
			}
        	
        	// Unkempt grass
        	this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), 0, 1, 8, structureBB);
        	this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), 1, 1, 7, structureBB);
        	this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), 1, 1, 12, structureBB);
        	this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), 4, 1, 10, structureBB);
        	this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), 4, 1, 11, structureBB);
        	
        	// Stalls
        	this.fillWithBlocks(world, structureBB, 4, 1, 1, 7, 1, 1, biomePlankState, biomePlankState, false);
        	this.fillWithBlocks(world, structureBB, 4, 0, 1, 7, 0, 1, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 0, 4, 3, 0, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 2, 4, 3, 2, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 2, 7, 3, 2, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 0, 7, 3, 0, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 4, 0, 7, 4, 2, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 5, 4, 0, 6, 4, 2, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), false);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 5, 4, 0, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 6, 4, 1, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 5, 4, 2, structureBB);
            
        	// Torches
            for (int[] uvwo : new int[][]{
            	{4, 2, 1, -1},
            	{7, 2, 1, -1},
            })
            {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	this.fillWithBlocks(world, structureBB, 2, 1, 5, 2, 1, 8, biomePlankState, biomePlankState, false);
        	this.fillWithBlocks(world, structureBB, 2, 0, 5, 2, 0, 8, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 1, 1, 5, 1, 3, 5, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 1, 1, 8, 1, 3, 8, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 8, 3, 3, 8, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 3, 1, 5, 3, 3, 5, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 1, 4, 5, 3, 4, 8, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 1, 4, 6, 3, 4, 7, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), false);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 1, 4, 7, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 2, 4, 6, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 3, 4, 7, structureBB);

            // Torches
            for (int[] uvwo : new int[][]{
            	{2, 2, 5, -1},
            	{2, 2, 8, -1},
            })
            {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	this.fillWithBlocks(world, structureBB, 4, 1, 13, 7, 1, 13, biomePlankState, biomePlankState, false);
        	this.fillWithBlocks(world, structureBB, 4, 0, 13, 7, 0, 13, biomeDirtState, biomeDirtState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 12, 4, 3, 12, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 1, 14, 4, 3, 14, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 14, 7, 3, 14, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 7, 1, 12, 7, 3, 12, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 4, 4, 12, 7, 4, 14, biomeWoodenSlabState, biomeWoodenSlabState, false);
        	this.fillWithBlocks(world, structureBB, 5, 4, 12, 6, 4, 14, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor : 4), false);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 5, 4, 12, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 6, 4, 13, structureBB);
        	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? townColor2 : 0), 5, 4, 14, structureBB);
        	
            // Torches
            for (int[] uvwo : new int[][]{
            	{4, 2, 13, -1},
            	{7, 2, 13, -1},
            })
            {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
            	
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(12, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
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
                this.replaceAirAndLiquidDownwards(world, biomeFillerState, bannerXBB, bannerYBB-2, bannerZBB, structureBB);

                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)));
				
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
	        			
	        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.11
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
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	public PlainsMeetingPoint3() {}
    	
    	public PlainsMeetingPoint3(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, float terrainType)
    	{
    		super(chunkManager, componentType, random, posX, posZ, components, terrainType);
    		
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
			if (this.coordBaseMode.getHorizontalIndex()!=0) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());}
			// Eastward
			if (this.coordBaseMode.getHorizontalIndex()!=1) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 4, EnumFacing.EAST, this.getComponentType());}
			// Southward
			if (this.coordBaseMode.getHorizontalIndex()!=2) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX + 4, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());}
			// Westward
			if (this.coordBaseMode.getHorizontalIndex()!=3) {StructureVillageVN.generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 4, EnumFacing.WEST, this.getComponentType());}
		}
    	
		/*
		 * Construct the structure
		 */
    	@Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.field_143015_k < 0)
            {
            	if (this.field_143015_k < 0)
                {
            		this.field_143015_k = StructureVillageVN.getMedianGroundLevel(world,
            				// Set the bounding box version as this bounding box but with Y going from 0 to 512
            				new StructureBoundingBox(
            						this.boundingBox.minX+(new int[]{INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U,INCREASE_MIN_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.minZ+(new int[]{INCREASE_MIN_W,INCREASE_MIN_U,DECREASE_MAX_W,INCREASE_MIN_U}[this.coordBaseMode.getHorizontalIndex()]),
            						this.boundingBox.maxX-(new int[]{DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U,DECREASE_MAX_W}[this.coordBaseMode.getHorizontalIndex()]), this.boundingBox.maxZ-(new int[]{DECREASE_MAX_W,DECREASE_MAX_U,INCREASE_MIN_W,DECREASE_MAX_U}[this.coordBaseMode.getHorizontalIndex()])),
            				true, (byte)14, this.coordBaseMode.getHorizontalIndex());
            		
                    if (this.field_143015_k < 0) {return true;} // Do not construct in a void

                    this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.minY - GROUND_LEVEL, 0);
                }
            }
        	
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWallSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wall_sign.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeCobblestoneStoneStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
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

        	WorldChunkManager chunkManager= world.getWorldChunkManager();
        	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            BiomeGenBase biome = chunkManager.getBiomeGenerator(new BlockPos(posX, 64, posZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
			if (this.villageType==null || this.materialType==null)
			{
				try {
	            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, posX, posZ);}
	            	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
	            	}
				catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, posX, posZ);}
				
				try {
	            	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
	            	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, posX, posZ);}
	            	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
	            	}
				catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, posX, posZ);}
				
				try {
	            	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf(biome.biomeName));
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
        		this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), offset_xy[0], 1, offset_xy[1], structureBB);
        	}
        	this.setBlockState(world, Blocks.yellow_flower.getDefaultState(), 3, 1, 1, structureBB);
        	
        	
        	// Tree
        	for (int uvwm[] : new int[][]{
        		{4, 1, 4, 3}, // Corner
        		{5, 1, 4, 3},
        		{6, 1, 4, 1}, // Corner
        		{6, 1, 5, 1},
        		{6, 1, 6, 2}, // Corner
        		{5, 1, 6, 2},
        		{4, 1, 6, 0}, // Corner
        		{4, 1, 5, 0},
        	})
        	{
        		this.setBlockState(world, biomeCobblestoneStoneStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(biomeCobblestoneStoneStairsState.getBlock(), uvwm[3])), uvwm[0], uvwm[1], uvwm[2], structureBB);
        	}
        	

        	// Dirt block
        	world.setBlockState(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(0), this.getZWithOffset(5, 5)), Blocks.dirt.getDefaultState(), 2);
        	
        	// Leaves placed into world
        	for (int u=3; u<=7; u++) {for (int v=5; v<=6; v++) {for (int w=4; w<=6; w++) {world.setBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(v), this.getZWithOffset(u, w)), Blocks.leaves.getStateFromMeta(0), 2);}}}
        	for (int u=4; u<=6; u++) {for (int v=5; v<=6; v++) {for (int w=3; w<=7; w++) {world.setBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(v), this.getZWithOffset(u, w)), Blocks.leaves.getStateFromMeta(0), 2);}}}
        	for (int u=4; u<=6; u++) {for (int v=7; v<=8; v++) {for (int w=4; w<=6; w++) {world.setBlockState(new BlockPos(this.getXWithOffset(u, w), this.getYWithOffset(v), this.getZWithOffset(u, w)), Blocks.leaves.getStateFromMeta(0), 2);}}}

        	// Logs need to be set in world so as not to be replaced with sandstone
        	for (int v=1; v<=7; v++) {world.setBlockState(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(v), this.getZWithOffset(5, 5)), Blocks.log.getStateFromMeta(0), 2);}
        	
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
            	world.setBlockState(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2])), Blocks.air.getDefaultState(), 2);
            }
            
            for (int[] uvwo : new int[][]{
            	{5, 3, 4, 2},
            	{4, 3, 5, 3},
            	{5, 3, 6, 0},
            	{6, 3, 5, 1},
            })
            {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
                    	        	
            // Posts
        	this.fillWithBlocks(world, structureBB, 4, 1, 1, 4, 4, 1, biomeFenceState, biomeFenceState, false);
        	this.fillWithBlocks(world, structureBB, 6, 1, 1, 6, 4, 1, biomeFenceState, biomeFenceState, false);
        	if (GeneralConfig.useVillageColors)
        	{
        		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor);
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
        		IBlockState concreteBlockstate = Blocks.stained_hardened_clay.getStateFromMeta(townColor2);
            	
            	// Basin rim
            	if (GeneralConfig.addConcrete)
            	{
            		concreteBlockstate = ModBlocksVN.blockConcrete.getStateFromMeta(townColor2);
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
        		
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(2, this.coordBaseMode.getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
    			world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
        		
                // I need to make a duplicate TileEntity because the first one gets consumed when applied to the first sign
        		TileEntitySign signContents2 = new TileEntitySign();
        		for (int i=0; i<4; i++) {signContents2.signText[i] = signContents.signText[i];}
        		
    			world.setBlockState(new BlockPos(signX2, signY, signZ2), biomeWallSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(0, this.coordBaseMode.getHorizontalIndex(), true)), 2); // 2 is "send change to clients without block update notification"
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
                this.replaceAirAndLiquidDownwards(world, biomeFillerState, bannerXBB, bannerYBB-3, bannerZBB, structureBB);
                
                BlockPos bannerPos = new BlockPos(bannerX, bannerY, bannerZ);
                
            	// Set the banner and its orientation
				world.setBlockState(bannerPos, Blocks.standing_banner.getStateFromMeta(StructureVillageVN.getSignRotationMeta(4, this.coordBaseMode.getHorizontalIndex(), false)));
				
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
	        			
	        			// Nitwits more often than not // TODO - Re-introduce Nitwits in 1.11
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
    
    
    // --- Flower planter ---//
    
    public static class PlainsAccessory1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
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
    	private static final int STRUCTURE_WIDTH = 5;
    	public static final int STRUCTURE_HEIGHT = 2;
    	private static final int STRUCTURE_DEPTH = 3;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsAccessory1() {}
        
        public PlainsAccessory1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static PlainsAccessory1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsAccessory1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        				true, (byte)15, this.coordBaseMode.getHorizontalIndex());
        		
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
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// make foundation and clear space above
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            		this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
            		this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
                    this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
            }}
        	
            // Sod filling
            this.fillWithBlocks(world, structureBB, 1, 1, 1, 3, 1, 1, Blocks.grass.getDefaultState(), Blocks.grass.getDefaultState(), false);
            // Trapdoor rim
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0 ? 6 : 4), 0, 1, 1, structureBB); // Left
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0 ? 7 : 5), 4, 1, 1, structureBB); // Right
        	this.fillWithBlocks(world, structureBB, 1, 1, 0, 3, 1, 0, biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{4, 7, 5, 6})[this.coordBaseMode.getHorizontalIndex()]), biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{4, 7, 5, 6})[this.coordBaseMode.getHorizontalIndex()]), false); // Front
        	this.fillWithBlocks(world, structureBB, 1, 1, 2, 3, 1, 2, biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{5, 6, 4, 7})[this.coordBaseMode.getHorizontalIndex()]), biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{5, 6, 4, 7})[this.coordBaseMode.getHorizontalIndex()]), false); // Back
        	// Flowers on top
        	for (int f=0; f<3; f++)
        	{
        		int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : Blocks.red_flower;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), 1+f, 2, 1, structureBB);
        		// Upper half of double flower block
        		//if (flowerindex>9) {this.setBlockState(world, flowerblock, 11, 1+f, 3, 1, structureBB);} // Meta is always 11?
        	}
        	
            return true;
        }
        
        /**
         * Returns the villager type to spawn in this component, based on the number
         * of villagers already spawned.
         */
        @Override
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    

    // --- Small Animal Pen --- //
    
    public static class PlainsAnimalPen1 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
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
    	private static final int STRUCTURE_WIDTH = 6;
    	public static final int STRUCTURE_HEIGHT = 8;
    	private static final int STRUCTURE_DEPTH = 5;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsAnimalPen1() {}

        public PlainsAnimalPen1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsAnimalPen1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsAnimalPen1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Make foundation and clear space above
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            		this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
            		this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
                    this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
                    
                    // Add a fence
                    if (u==0 || u==STRUCTURE_WIDTH-1 || w==0 || w==STRUCTURE_DEPTH-1)
                    {
                    	this.setBlockState(world, biomeFenceState, u, 1, w, structureBB);
                    }
            }}
        	
            // Fence gate
        	IBlockState biomeFenceGateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence_gate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            this.setBlockState(world, biomeFenceGateState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeFenceGateState.getBlock(), biomeFenceGateState.getBlock().getMetaFromState(biomeFenceGateState), this.coordBaseMode)), 2, 1, 0, structureBB);
            
            // Grass and flower in random places
            ArrayList<Integer> weedpositions = new ArrayList<Integer>();
            
            while (weedpositions.size()<3)
            {
            	while (true)
            	{
            		int candidatevalue = random.nextInt((STRUCTURE_WIDTH-2)*(STRUCTURE_DEPTH-2));
            		if (!weedpositions.contains(candidatevalue))
            		{
            			weedpositions.add(candidatevalue); break;
            		}
            	}
            }
            for (int i=0; i<weedpositions.size(); i++)
            {
            	if (i==0) // Random flower
            	{
            		int flowerindex = random.nextInt(10);
            		// 0-8 is "red" flower
            		// 9 is a basic yellow flower
            		// 10-11 are the flowers from UpToDateMod
            		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : Blocks.red_flower;
            		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
            		
            		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), weedpositions.get(i)%(STRUCTURE_WIDTH-2)+1, 1, weedpositions.get(i)/(STRUCTURE_WIDTH-2)+1, structureBB);
            	}
            	else // Tall grass
            	{
            		this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), weedpositions.get(i)%(STRUCTURE_WIDTH-2)+1, 1, weedpositions.get(i)/(STRUCTURE_WIDTH-2)+1, structureBB);
            	}
            }
            
            
        	// Tree
        	IBlockState biomeSaplingState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.sapling.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwss : new int[][]{
        		// u,v,w, ushift,wshift: Which adjacent spaces to use if this is a Dark Oak sapling
        		{4,1,3, -1,-1},
        		})
            {
        		Block dirtblock = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0], uvwss[2]))).getBlock();
        		Block saplingblock = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2]))).getBlock();

        		// Don't place if there's no dirt beneath to grow
        		if (dirtblock==null || (dirtblock != Blocks.dirt && dirtblock != Blocks.grass)) {continue;}
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
        					(dirtblock1 != Blocks.dirt && dirtblock1 != Blocks.grass)
        					|| (dirtblock2 != Blocks.dirt && dirtblock2 != Blocks.grass)
        					|| (dirtblock3 != Blocks.dirt && dirtblock3 != Blocks.grass)
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
        	
            
            // Animal in the pen
            if (!this.entitiesGenerated)
            {
            	entitiesGenerated = true;
            	
            	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(1, 2) + 0.5D, (double)this.getYWithOffset(1) + 0.5D, (double)this.getZWithOffset(1, 2) + 0.5D);
            	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, true, this.materialType==MaterialType.MUSHROOM);
            	if (VillageGeneratorConfigHandler.nameVillageHorses && GeneralConfig.nameEntities && animal instanceof EntityHorse)
            	{
            		String[] petname_a = NameGenerator.newRandomName("pet", random);
            		animal.setCustomNameTag((petname_a[1]+" "+petname_a[2]+" "+petname_a[3]).trim());
            	}
                animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(animal);
                
                // Dirt block underneath
                //this.setBlockState(world, biomeDirtState, 1, 0, 2, structureBB);
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
    
    
    
    
    // --- Large Animal Pen --- //
    
    public static class PlainsAnimalPen2 extends StructureVillagePieces.Village
    {
    	// Stuff to be used in the construction
    	public boolean entitiesGenerated = false;
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
    	private static final int STRUCTURE_WIDTH = 11;
    	public static final int STRUCTURE_HEIGHT = 7;
    	private static final int STRUCTURE_DEPTH = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsAnimalPen2() {}

        public PlainsAnimalPen2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsAnimalPen2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsAnimalPen2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
        }
        
        
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.averageGroundLevel < 0)
            {
        		this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				// ONLY USING PART OF THE FRONT for the butcher's shop
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
        	
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.grass.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.dirt.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Make foundation and clear space above
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            		this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB);
            		this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, w, structureBB);
                    this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL, w, structureBB);
                    
                    // Add a fence
                    if (u==0 || u==STRUCTURE_WIDTH-1 || w==0 || w==STRUCTURE_DEPTH-1)
                    {
                    	this.setBlockState(world, biomeFenceState, u, 1, w, structureBB);
                    }
            }}
        	
            // Fence gate
        	IBlockState biomeFenceGateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence_gate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            this.setBlockState(world, biomeFenceGateState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeFenceGateState.getBlock(), biomeFenceGateState.getBlock().getMetaFromState(biomeFenceGateState), this.coordBaseMode)), 5, 1, 0, structureBB);
            
            // Hay block
            this.setBlockState(world, Blocks.hay_block.getStateFromMeta(0), 7, 1, 3, structureBB);
            
            // Grass and flower in random places
            ArrayList<Integer> weedpositions = new ArrayList<Integer>();
            
            while (weedpositions.size()<14)
            {
            	while (true)
            	{
            		int candidatevalue = random.nextInt((STRUCTURE_WIDTH-2)*(STRUCTURE_DEPTH-2));
            		if (!weedpositions.contains(candidatevalue))
            		{
            			weedpositions.add(candidatevalue); break;
            		}
            	}
            }
            for (int i=0; i<weedpositions.size(); i++)
            {
            	if (i==0) // Random flower
            	{
            		int flowerindex = random.nextInt(10);
            		// 0-8 is "red" flower
            		// 9 is a basic yellow flower
            		// 10-11 are the flowers from UpToDateMod
            		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : Blocks.red_flower;
            		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
            		
            		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), weedpositions.get(i)%(STRUCTURE_WIDTH-2)+1, 1, weedpositions.get(i)/(STRUCTURE_WIDTH-2)+1, structureBB);
            	}
            	else // Tall grass if < 11 or double-tall grass otherwise 
            	{
            		this.setBlockState(world, i<11 ? Blocks.tallgrass.getStateFromMeta(1) : Blocks.double_plant.getStateFromMeta(2), weedpositions.get(i)%(STRUCTURE_WIDTH-2)+1, 1, weedpositions.get(i)/(STRUCTURE_WIDTH-2)+1, structureBB);
            		// Double-tall topper
            		if (i >=11) {this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), weedpositions.get(i)%(STRUCTURE_WIDTH-2)+1, 2, weedpositions.get(i)/(STRUCTURE_WIDTH-2)+1, structureBB);}
            	}
            }
            
            
        	// Tree
        	IBlockState biomeSaplingState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.sapling.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwss : new int[][]{
        		// u,v,w, ushift,wshift: Which adjacent spaces to use if this is a Dark Oak sapling
        		{2,1,4, -1,1},
        		})
            {
        		Block dirtblock = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0], uvwss[2]))).getBlock();
        		Block saplingblock = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2]))).getBlock();

        		// Don't place if there's no dirt beneath to grow
        		if (dirtblock==null || (dirtblock != Blocks.dirt && dirtblock != Blocks.grass)) {continue;}
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
        					(dirtblock1 != Blocks.dirt && dirtblock1 != Blocks.grass)
        					|| (dirtblock2 != Blocks.dirt && dirtblock2 != Blocks.grass)
        					|| (dirtblock3 != Blocks.dirt && dirtblock3 != Blocks.grass)
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
            
            // Animal in the pen
            if (!this.entitiesGenerated)
            {
            	entitiesGenerated = true;
            	
            	for (int[] uvw : new int[][]{
        			{3, 1, 2},
        			{7, 1, 4},
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
    
    
    
    
    // --- Decorated Animal Pen --- //
    
    public static class PlainsAnimalPen3 extends StructureVillagePieces.Village
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
            	"   FFFFFFFF",
            	"FFFFFFFFFFF",
            	"FFFFFFFFFFF",
            	"FFFFFFFFFFF",
            	"FFFFFFFFFFF",
            	"FFFFFFFFF  ",
            	"  FFFFFFF F",
            	"F FFFFFFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	public static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsAnimalPen3() {}

        public PlainsAnimalPen3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsAnimalPen3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsAnimalPen3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
        	
        	// Add fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int w = 0; w < STRUCTURE_DEPTH; ++w) {for (int u = new int[]{2,2,0,0,0,0,0,3}[w]; u <= new int[]{8,8,8,10,10,10,10,10}[w]; ++u) {
                    // Add a fence
                    if (u==new int[]{2,2,0,0,0,0,0,3}[w] || u==new int[]{8,8,8,10,10,10,10,10}[w] || w==0 || w==STRUCTURE_DEPTH-1)
                    {
                    	this.setBlockState(world, biomeFenceState, u, 1, w, structureBB);
                    }
            }}
            // Add fences missed by above algorithm
            for (int uw[] : new int[][]{
            	{1,2}, 
            	{2,2},
            	{9,3},
            	{8,3},
            	{1,6},
            	{2,6},
            	{3,6},
            	})
            {
            	this.setBlockState(world, biomeFenceState, uw[0], 1, uw[1], structureBB);
            }
            // Add decor foundations
            for (int uw[] : new int[][]{
            	{0,0}, 
            	{10,1},
            	})
            {
            	this.setBlockState(world, biomeDirtState, uw[0], 0, uw[1], structureBB);
        		this.replaceAirAndLiquidDownwards(world, biomeFillerState, uw[0], GROUND_LEVEL-2, uw[1], structureBB);
            }
            
            // Fence gate
        	IBlockState biomeFenceGateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence_gate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            this.setBlockState(world, biomeFenceGateState.getBlock().getStateFromMeta(StructureVillageVN.getMetadataWithOffset(biomeFenceGateState.getBlock(), biomeFenceGateState.getBlock().getMetaFromState(biomeFenceGateState), this.coordBaseMode)), 5, 1, 0, structureBB);
            
            // Torches
            for (int i : new int[]{0,2})
            {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(0), 4+i, 2, 0, structureBB);
            }
        	

        	// Grass
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 2:forward, 3:backward
        		// Front accent
        		{2,0,0, 8,0,1}, {0,0,2, 8,0,2}, {0,0,3, 10,0,6}, {3,0,7, 10,0,7}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeGrassState, biomeGrassState, false);
            }
            
            // Cross-shaped watering hole
            for (int u=0; u<3; u++) {for (int w=0; w<3; w++) {
            	if (u==1 || w==1) {this.setBlockState(world, Blocks.flowing_water.getStateFromMeta(0), 6+u, 0, 4+w, structureBB);}
            }}
            
            
            // Animal in the pen
            if (!this.entitiesGenerated)
            {
            	entitiesGenerated = true;
            	
            	for (int[] uvw : new int[][]{
        			{1, 1, 4},
        			{5, 1, 2},
        			{5, 1, 5},
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
                    //this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            }
            
            
            // Decor
            int[][] decorUVW = new int[][]{
            	{0, 1, 0},
            	{3, 1, 4},
            	{7, 1, 3},
            	{10, 1, 1},
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
    
    public static class PlainsArmorerHouse1 extends StructureVillagePieces.Village
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
            	" FFFFFF ",
            	" FFFFFF ",
            	" FFFFFF ",
            	" FFFFFF ",
            	" FFFFFF ",
            	" FFFFFF ",
            	" FFFFFF ",
            	"   FF   ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsArmorerHouse1() {}

        public PlainsArmorerHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsArmorerHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsArmorerHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        		// Floor
        		{1,0,1, 6,0,7}, 
        		// Walls
        		{2,0,1, 2,4,1}, {5,0,1, 5,4,1}, {3,3,1, 4,5,1}, 
        		{2,1,7, 5,4,7}, {3,5,7, 4,5,7}, 
        		{1,1,2, 1,3,6}, {6,1,2, 6,3,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            // Log corners (vertical)
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uw : new int[][]{{1,1},{1,7},{6,1},{6,7}})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 0, uw[1], uw[0], 3, uw[1], biomeLogVertState, biomeLogVertState, false);
            }            
            
            // Left windows
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            this.fillWithBlocks(world, structureBB, 1, 2, 2, 1, 2, 6, biomeLogHorAlongState, biomeLogHorAlongState, false);
            for (int i : new int[]{3,5}) {
            	this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), 1, 2, i, structureBB);
            }
            
            // Back windows
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            this.fillWithBlocks(world, structureBB, 2, 2, 7, 5, 2, 7, biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            this.fillWithBlocks(world, structureBB, 3, 2, 7, 4, 2, 7, Blocks.glass_pane.getStateFromMeta(0), Blocks.glass_pane.getStateFromMeta(0), false);
            
            // Roof
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int i=0; i<4; i++) {
            	// Left side meta 0
            	this.fillWithBlocks(world, structureBB, i, 3+i, 0, i, 3+i, 8, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), false);
            	// Right side meta 1
            	this.fillWithBlocks(world, structureBB, 7-i, 3+i, 0, 7-i, 3+i, 8, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), false);
            }
            // Roof trim
            for (int i=0; i<3; i++) {for (int j : new int[]{0,8}){
            	this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)+4), 1+i, 3+i, j, structureBB);
            	this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)+4), 6-i, 3+i, j, structureBB);
            }}
            // Trim on the side of the house where the furnace is
            for (int i=0; i<2; i++) {
            	this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, new int[]{2,3}[i])+4), 7, 3, new int[]{2,6}[i], structureBB);
            }
            
            // Wood slab (top)
            IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            this.fillWithBlocks(world, structureBB, 7, 3, 3, 7, 3, 5, biomeWoodSlabTopState, biomeWoodSlabTopState, false);
            
            // Torches
            for (int i : new int[]{2,5}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(2, this.coordBaseMode.getHorizontalIndex())), i, 2, 0, structureBB);
            }
            this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(1, this.coordBaseMode.getHorizontalIndex())), 2, 2, 4, structureBB);
            for (int i : new int[]{2,6}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(3, this.coordBaseMode.getHorizontalIndex())), 5, 4, i, structureBB);
            }
            
            // Counter
        	IBlockState smoothStoneState = ModObjects.chooseModSmoothStoneBlockState();
            for (int[] uuww : new int[][]{
            	{2,2,2,5},
            	{2,5,6,6}
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuww[0], 1, uuww[2], uuww[1], 1, uuww[3], smoothStoneState, smoothStoneState, false);
            }
            
            // Fireplace
            for (int i : new int[]{3,5}) {
            	this.fillWithBlocks(world, structureBB, 5, 1, i, 5, 2, i, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            for (int i=3; i<=5; i++) {
            	this.fillWithBlocks(world, structureBB, 7, 0, i, 7, 1, i, biomeCobblestoneState, biomeCobblestoneState, false);
            	this.replaceAirAndLiquidDownwards(world, biomeCobblestoneState, 7, GROUND_LEVEL-1, i, structureBB);
            }
            
            this.setBlockState(world, biomeCobblestoneState, 5, 3, 4, structureBB);
            
            // Stone Stairs - Fireplace and stairs out front
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwm : new int[][]{
            	// Front Stairs
            	{3,0,0,3},
            	{4,0,0,3},
            	// Furnace exterior
            	{7,2,3,3},
            	{7,2,4,1},
            	{7,2,5,2},
            	// Furnace interior
            	{5,3,3,3},
            	{5,3,5,2},
            	{5,4,4,0},
            	})
            {
            	this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwm[3])), uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
            // Chimney
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.fillWithBlocks(world, structureBB, 6, 3, 4, 6, 6, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            this.setBlockState(world, biomeCobblestoneWallState, 6, 7, 4, structureBB);
            
            // Brick
        	IBlockState biomeBrickState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.brick_block.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.setBlockState(world, biomeBrickState, 5, 0, 4, structureBB);
            this.setBlockState(world, biomeBrickState, 6, 2, 4, structureBB);
            
            // Blast Furnace - this is a TileEntity and needs to have its meta assigned manually
        	IBlockState blastFurnaceState = ModObjects.chooseModBlastFurnaceState(3, this.coordBaseMode);
        	for (int[] uvw : new int[][]{{6,1,4}})
            {
                this.setBlockState(world, blastFurnaceState, uvw[0], uvw[1], uvw[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvw[0], uvw[2]), this.getYWithOffset(uvw[1]), this.getZWithOffset(uvw[0], uvw[2])), blastFurnaceState, 2);
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
        	
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int height=0; height<2; height++) {for (int leftright=0; leftright<2; leftright++) {
            	this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(2, this.coordBaseMode, true, leftright==0)[height]), 3+leftright, 1+height, 1, structureBB);
            }}
            
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	int u = random.nextInt(2)+3;
            	int v = 1;
            	int w = random.nextInt(4)+2;
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    
    // --- Large House --- //
    
    public static class PlainsBigHouse1 extends StructureVillagePieces.Village
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
            	"           ",
            	" FFFFFFFFF ",
            	" FFFFFFFFF ",
            	" FFFFFFFFF ",
            	" FFFFFFFFF ",
            	" FFFFFFFFF ",
            	"     P     ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 11;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsBigHouse1() {}

        public PlainsBigHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsBigHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsBigHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            
            // Log frame
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uw : new int[][]{{1,1},{1,5},{9,1},{9,5}})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 1, uw[1], uw[0], 7, uw[1], biomeLogVertState, biomeLogVertState, false);
            }            
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            this.fillWithBlocks(world, structureBB, 2, 4, 1, 8, 4, 1, biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            this.fillWithBlocks(world, structureBB, 2, 4, 5, 8, 4, 5, biomeLogVertState, biomeLogVertState, false);
            this.fillWithBlocks(world, structureBB, 5, 1, 5, 5, 3, 5, biomeLogVertState, biomeLogVertState, false);
            for (int i : new int[]{1,5}) {
            	this.fillWithBlocks(world, structureBB, 5, 5, i, 5, 7, i, biomeLogVertState, biomeLogVertState, false);
            }
            for (int i : new int[]{1,9}) {
            	this.fillWithBlocks(world, structureBB, i, 4, 2, i, 4, 4, biomeLogVertState, biomeLogVertState, false);
            }
            
            // Front and back walls
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.fillWithBlocks(world, structureBB, 2, 1, 1, 8, 3, 1, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 2, 1, 5, 4, 3, 5, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 6, 1, 5, 8, 3, 5, biomeCobblestoneState, biomeCobblestoneState, false);
            
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int i : new int[]{1,5}) {
            	this.fillWithBlocks(world, structureBB, 2, 5, i, 4, 7, i, biomePlankState, biomePlankState, false);
                this.fillWithBlocks(world, structureBB, 6, 5, i, 8, 7, i, biomePlankState, biomePlankState, false);
            }
                        
            // Left and right walls
            for (int i : new int[]{1,9}) {
            	this.fillWithBlocks(world, structureBB, i, 1, 2, i, 3, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            	this.fillWithBlocks(world, structureBB, i, 5, 2, i, 8, 4, biomePlankState, biomePlankState, false);
            	this.setBlockState(world, biomePlankState, i, 9, 3, structureBB);
            }
            
            // Windows
            for (int v : new int[]{2, 6})
            {
            	for (int[] uw : new int[][]{
            		{1, 3}, {9, 3},
            		{3, 1}, {7, 1}, {3, 5}, {7, 5},
            		})
                {
            		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uw[0], v, uw[1], structureBB);
                }
            }
            
            // Roof
            for (int i=0; i<=3; i++) {
            	for (int j : i==0 ? new int[]{i} : new int[]{-i, i})
            	{
            		this.fillWithBlocks(world, structureBB, 0, 10-i, 3+j, 10, 10-i, 3+j, biomePlankState, biomePlankState, false);
            	}
            }
            
            
            // Front door
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int height=0; height<=1; height++) {
            	this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(2, this.coordBaseMode, true, true)[height]), 5, 1+height, 1, structureBB);
            }
            this.setBlockState(world, biomeCobblestoneState, 5, 0, 1, structureBB);
            
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
        	
        	
            // --- Interior --- //
            
            // Stone floor
            this.fillWithBlocks(world, structureBB, 2, 0, 2, 8, 0, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            
            // Stairway
            this.fillWithBlocks(world, structureBB, 4, 1, 4, 5, 2, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 2, 3, 4, 3, 3, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            
            // Stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int i=0; i<4; i++) {
            	this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, 1)), 3+i, 4-i, 4, structureBB);
            }
            
            // Wood floor
            this.fillWithBlocks(world, structureBB, 2, 4, 2, 8, 4, 3, biomePlankState, biomePlankState, false);
            this.fillWithBlocks(world, structureBB, 7, 4, 4, 8, 4, 4, biomePlankState, biomePlankState, false);
            this.setBlockState(world, biomePlankState, 2, 4, 4, structureBB);
            
            // Torches
            for (int[] uvwm : new int[][]{
            	{2,3,3,1},
            	{8,3,3,3},
            	{2,7,3,1},
            	{8,7,3,3},
            	{5,6,4,2},
            })
            {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwm[3], this.coordBaseMode.getHorizontalIndex())), uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
            
            
            // Beds
            for (int[] uvwm : new int[][]{
            	{2,1,3,2},
            	{7,1,2,3},
            	{3,5,2,1},
            	{7,5,2,3},
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwm[3];
            		int u = uvwm[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwm[1];
            		int w = uvwm[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor2 : 0); // Goes by wool meta
            	}
            }
            
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 8;
        	int chestV = 5;
        	int chestW = 4;
        	int chestO = 3;
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	            	int[][] villagerPositions = new int[][]{
	        			{3,1,3, -1, 0},
	        			{8,1,3, -1, 0},
	        			{2,5,3, -1, 0},
	        			{8,5,3, -1, 0},
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
    
    
    
    // --- Small Butcher Shop --- //
    
    public static class PlainsButcherShop1 extends StructureVillagePieces.Village
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
            	" FFFFFFFFFFF",
            	" FFFFFFFFFFF",
            	" FFFFFFFFFFF",
            	" FFFFFFFFFFF",
            	" FFFFFFFFFFF",
            	" FFFFFFFFFFF",
            	" FFFFFFFFFFF",
            	" FFFFFF     ",
            	" FFFFFF     ",
            	"   FF       ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 4;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsButcherShop1() {}

        public PlainsButcherShop1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsButcherShop1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsButcherShop1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
        	
        	
            // Log frame
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int u : new int[]{1,6}) {for (int w : new int[]{1,9}) {
            	this.fillWithBlocks(world, structureBB, u, 0, w, u, 3, w, biomeLogVertState, biomeLogVertState, false);
            }}
            this.fillWithBlocks(world, structureBB, 1, 0, 5, 1, 2, 5, biomeLogVertState, biomeLogVertState, false);
            for (int[] uw : new int[][]{
            	{2,1}, {5,1}, // Front wall
            	{2,9}, {5,9}, // Back wall
            	{1,2}, {1,4}, {1,6}, {1,8}, // Left wall
            })
            {
            	this.setBlockState(world, biomeLogVertState, uw[0], 2, uw[1], structureBB);
            }
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for (int w : new int[]{2,5,6}) {this.setBlockState(world, biomeLogHorAlongState, 6, 2, w, structureBB);}
            
            // Stone front and back walls
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int w : new int[]{1,9})
            {
            	this.fillWithBlocks(world, structureBB, 2, 0, w, 5, 1, w, biomeCobblestoneState, biomeCobblestoneState, false);
                this.fillWithBlocks(world, structureBB, 2, 3, w, 5, 4, w, biomeCobblestoneState, biomeCobblestoneState, false);
                this.fillWithBlocks(world, structureBB, 3, 5, w, 4, 5, w, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            // Stone left wall
            this.fillWithBlocks(world, structureBB, 1, 0, 2, 1, 1, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 1, 0, 6, 1, 1, 8, biomeCobblestoneState, biomeCobblestoneState, false);
            // Stone right wall
            this.fillWithBlocks(world, structureBB, 6, 0, 2, 6, 1, 6, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 6, 0, 8, 6, 2, 8, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 6, 0, 7, 7, 0, 7, biomeCobblestoneState, biomeCobblestoneState, false);
            
            // Windows
        	for (int[] uw : new int[][]{
        		{1, 3}, {1, 7}, // Left wall
        		{6, 3}, {6, 4}, // Right wall, facing yard
        		{3, 9}, {4, 9}, // Back wall
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uw[0], 2, uw[1], structureBB);
            }
            
            // Roof
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int i=0; i<4; i++) {
            	// Left side meta 0
            	this.fillWithBlocks(world, structureBB, i, 3+i, 0, i, 3+i, STRUCTURE_DEPTH-1, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), false);
            	// Right side meta 1
            	this.fillWithBlocks(world, structureBB, 7-i, 3+i, 0, 7-i, 3+i, STRUCTURE_DEPTH-1, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), false);
            }
            // Interior roof
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int i=0; i<3; i++) {
            	// Left side meta 0
            	this.fillWithBlocks(world, structureBB, 1+i, 3+i, 2, 1+i, 3+i, STRUCTURE_DEPTH-3, biomePlankState, biomePlankState, false);
            	// Right side meta 1
            	this.fillWithBlocks(world, structureBB, 6-i, 3+i, 2, 6-i, 3+i, STRUCTURE_DEPTH-3, biomePlankState, biomePlankState, false);
            }
            // Roof trim
            for (int i=0; i<3; i++) {for (int j : new int[]{0,STRUCTURE_DEPTH-1}){
            	this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)+4), 1+i, 3+i, j, structureBB);
            	this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)+4), 6-i, 3+i, j, structureBB);
            }}
            
            
            // Planks
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,2, 5,0,5}, 
            	{5,0,6, 5,0,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Yard sard
        	
        	// Grass
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 2:forward, 3:backward
        		// Front accent
        		{7,0,4, 10,0,6}, {8,0,7, 10,0,7}, {7,0,8, 10,0,8}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeGrassState, biomeGrassState, false);
            }
        	
            // Pen rim is vertical logs with fences above
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            IBlockState[] penrimState = new IBlockState[]{biomeLogVertState, biomeFenceState};
            for (int i=0; i<=1; i++)
            {
            	for (int w : new int[]{3,9}) {
                	this.fillWithBlocks(world, structureBB, 7, i, w, 10, i, w, penrimState[i], penrimState[i], false);
                }
                this.fillWithBlocks(world, structureBB, 11, i, 3, 11, i, 9, penrimState[i], penrimState[i], false);
            }
            
            
            // Hay bales
            this.fillWithBlocks(world, structureBB, 7, 1, 5, 7, 1, 6, Blocks.hay_block.getStateFromMeta(0), Blocks.hay_block.getStateFromMeta(0), false);
            
            
            // Torches
            for (int[] uvwm : new int[][]{
            	{2,2,0,2}, {5,2,0,2}, {0,2,5,3}, // House exterior
            	{2,3,3,1}, {5,3,7,3}, // House interior
            	{11,2,3,-1}, {11,2,9,-1}, // Animal pen
            })
            {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwm[3], this.coordBaseMode.getHorizontalIndex())), uvwm[0], uvwm[1], uvwm[2], structureBB);
            }
            
            
            // Counter
            this.fillWithBlocks(world, structureBB, 2, 0, 6, 4, 0, 8, Blocks.double_stone_slab.getStateFromMeta(0), Blocks.double_stone_slab.getStateFromMeta(0), false);
            this.fillWithBlocks(world, structureBB, 2, 1, 7, 3, 1, 7, Blocks.double_stone_slab.getStateFromMeta(0), Blocks.double_stone_slab.getStateFromMeta(0), false);
            
            
            // Fireplace
            // Furnace - this is a TileEntity and needs to have its meta assigned manually
        	IBlockState smokerState = ModObjects.chooseModSmokerState(3, this.coordBaseMode);
            for (int[] uvwo : new int[][]{{5,1,8,2}})
            {
                //this.setBlockState(world, smokerBlock, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), smokerState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode)), 2);
            }
            // Chimney
            this.setBlockState(world, Blocks.cobblestone.getStateFromMeta(0), 5, 5, 8, structureBB);
            this.fillWithBlocks(world, structureBB, 5, 2, 8, 5, 4, 8, Blocks.cobblestone_wall.getStateFromMeta(0), Blocks.cobblestone_wall.getStateFromMeta(0), false);
            this.fillWithBlocks(world, structureBB, 5, 6, 8, 5, 7, 8, Blocks.cobblestone_wall.getStateFromMeta(0), Blocks.cobblestone_wall.getStateFromMeta(0), false);
            
            // Table
            this.setBlockState(world, biomeFenceState, 2, 1, 3, structureBB);
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_pressure_plate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            this.setBlockState(world, biomeWoodPressurePlateState, 2, 2, 3, structureBB);
            
            
            // 0: nuffin
            // 1: poppy
            // 2: dandelion
            int flowernumber = random.nextInt(2)+1;
            
            // Flower pot on table
            this.setBlockState(world, biomePlankState, 5, 1, 2, structureBB);
        	
        	int potU = 5;
        	int potV = 2;
        	int potW = 2;
        	int potX = this.getXWithOffset(potU, potW);
        	int potY = this.getYWithOffset(potV);
        	int potZ = this.getZWithOffset(potU, potW);
        	
        	TileEntity flowerPot = (new BlockFlowerPot()).createNewTileEntity(world, flowernumber);
    		BlockPos flowerPotPos = new BlockPos(potX, potY, potZ);
    		world.setBlockState(flowerPotPos, Blocks.flower_pot.getDefaultState());
    		world.setTileEntity(flowerPotPos, flowerPot);
        	
        	
            // Chairs
            for (int wo[]: new int[][]{
            		{2,2},
            		{4,3},
            		}) {
            	this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, wo[1])), 2, 1, wo[0], structureBB);
            }
            
            
            // Front stairs
            this.fillWithBlocks(world, structureBB, 3, 0, 0, 4, 0, 0, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 3)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 3)), false);
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 1, 1, 2, 1, 1},
            	{4, 1, 1, 2, 1, 0},
            	{6, 1, 7, 1, 1, 1},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
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
        			{9, 1, 6},
        			})
        		{
                	BlockPos animalPos = new BlockPos((double)this.getXWithOffset(uvw[0], uvw[2]) + 0.5D, (double)this.getYWithOffset(uvw[1]) + 0.5D, (double)this.getZWithOffset(uvw[0], uvw[2]) + 0.5D);
                	EntityLiving animal = StructureVillageVN.getVillageAnimal(world, animalPos, random, false, this.materialType==MaterialType.MUSHROOM);
                    animal.setLocationAndAngles(animalPos.getX(), animalPos.getY(), animalPos.getZ(), random.nextFloat()*360F, 0.0F);
                    world.spawnEntityInWorld(animal);
                    
                    // Dirt block underneath
                    //this.setBlockState(world, biomeDirtState, uvw[0], uvw[1]-1, uvw[2], structureBB);
        		}
            	
            	// Villager
            	int u = random.nextInt(3)+3;
            	int v = 1;
            	int w = random.nextInt(4)+3;
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 4;}
    }
    
    
    
    // --- Large Butcher Shop --- //
    
    public static class PlainsButcherShop2 extends StructureVillagePieces.Village
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
            	"FFFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFFF",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	"   P   ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 12;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsButcherShop2() {}

        public PlainsButcherShop2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsButcherShop2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsButcherShop2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            
            // Decor
            for (int decorUVW[] : new int[][]{
            	{6, 0, 5, 0},
            	{0, 0, 14, 1},
            	})
            {
                // Add foundations
            	this.setBlockState(world, decorUVW[3]==1 ? biomeGrassState : biomeDirtState, decorUVW[0], decorUVW[1], decorUVW[2], structureBB);
        		this.replaceAirAndLiquidDownwards(world, biomeFillerState, decorUVW[0], decorUVW[1]-1, decorUVW[2], structureBB);
        		
                // The actual decor
        		for (int j=0; j<decorUVW.length; j++)
                {
        			int decorHeightY = decorUVW[1]+1;
        			
        			// Set random seed
                	Random randomFromXYZ = new Random();
                	randomFromXYZ.setSeed(
        					world.getSeed() +
        					FunctionsVN.getUniqueLongForXYZ(
        							this.getXWithOffset(decorUVW[0], decorUVW[2]),
        							this.getYWithOffset(decorHeightY),
        							this.getZWithOffset(decorUVW[0], decorUVW[2])
        							)
                			);
                	
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
                	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
                	
                	for (BlueprintData b : decorBlueprint)
                	{
                		// Place block indicated by blueprint
                		this.setBlockState(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos(), decorUVW[2]+b.getWPos(), structureBB);
                		
                		// Fill below if flagged
                		if ((b.getfillFlag()&1)!=0)
                		{
                			this.replaceAirAndLiquidDownwards(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()-1, decorUVW[2]+b.getWPos(), structureBB);
                		}
                		
                		// Clear above if flagged
                		if ((b.getfillFlag()&2)!=0)
                		{
                			this.clearCurrentPositionBlocksUpwards(world, decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()+1, decorUVW[2]+b.getWPos(), structureBB);
                		}            		
                	}
                }
            }
            
            // Log corners
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int u : new int[]{1,5})
            {
            	this.fillWithBlocks(world, structureBB, u, 1, 1, u, 4, 1, biomeLogVertState, biomeLogVertState, false);
            	this.fillWithBlocks(world, structureBB, u, 1, 8, u, 8, 8, biomeLogVertState, biomeLogVertState, false);
            	this.fillWithBlocks(world, structureBB, u, 5, 4, u, 8, 4, biomeLogVertState, biomeLogVertState, false);
            }
            
            // Log along
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for (int u : new int[]{1,5}) {for (int v : new int[]{0,2}) {
                	this.fillWithBlocks(world, structureBB, u, v+2, 2, u, v+2, 7, biomeLogHorAlongState, biomeLogHorAlongState, false);
                	this.fillWithBlocks(world, structureBB, u, v+6, 5, u, v+6, 7, biomeLogHorAlongState, biomeLogHorAlongState, false);
            }}
            
            // Log across
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for (int[] vw : new int[][]{
            	{4,1},
            	{4,8},
            	{6,4},
            	{8,4},
            	{6,8},
            	{8,8},
            	})
            {
            	this.fillWithBlocks(world, structureBB, 2, vw[0], vw[1], 4, vw[0], vw[1], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            // Floors
            // Stone first story
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.fillWithBlocks(world, structureBB, 2, 0, 2, 4, 0, 8, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 2, 0, 4, 2, 0, 5, biomeDirtState, biomeDirtState, false);
            // Wood second story
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	this.fillWithBlocks(world, structureBB, 3, 4, 5, 4, 4, 7, biomePlankState, biomePlankState, false);
            
            
            // Stone front wall
            this.fillWithBlocks(world, structureBB, 2, 1, 1, 4, 3, 1, biomeCobblestoneState, biomeCobblestoneState, false);
            
            // Stone back wall
            this.fillWithBlocks(world, structureBB, 2, 1, 8, 4, 3, 8, biomeCobblestoneState, biomeCobblestoneState, false);
            
            // Left and right walls
            for (int u : new int[]{1,5}) {for (int v : new int[]{1,3}) {
            	this.fillWithBlocks(world, structureBB, u, v, 2, u, v, 7, biomeCobblestoneState, biomeCobblestoneState, false);
            }}
            
            // Second floor
            for (int u : new int[]{1,5}) {for (int v : new int[]{5,7}) {
            	this.fillWithBlocks(world, structureBB, u, v, 5, u, v, 7, biomeCobblestoneState, biomeCobblestoneState, false);
            }}
            for (int w : new int[]{4,8}) {for (int v : new int[]{5,7,9}) {
            	this.fillWithBlocks(world, structureBB, 2, v, w, 4, v, w, biomeCobblestoneState, biomeCobblestoneState, false);
            }}
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{1, 2, 3}, {1, 2, 6}, {5, 2, 3}, {5, 2, 6}, // First floor
        		{1, 6, 6}, {5, 6, 6}, {3, 6, 4}, {3, 6, 8}, // Second floor
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            // Roof
        	// First floor
        	this.fillWithBlocks(world, structureBB, 2, 5, 0, 4, 5, 3, biomePlankState, biomePlankState, false);
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	this.fillWithBlocks(world, structureBB, 1, 5, 0, 1, 5, 3, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), false);
        	this.fillWithBlocks(world, structureBB, 5, 5, 0, 5, 5, 3, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), false);
        	this.fillWithBlocks(world, structureBB, 0, 4, 0, 0, 4, 9, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), false);
        	this.fillWithBlocks(world, structureBB, 6, 4, 0, 6, 4, 9, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), false);
        	this.fillWithBlocks(world, structureBB, 1, 4, 9, 5, 4, 9, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 2)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 2)), false);
        	// Second floor
        	this.fillWithBlocks(world, structureBB, 3, 10, 3, 3, 10, 9, biomePlankState, biomePlankState, false);
        	for (int i=0; i<=2; i++) {
        		this.fillWithBlocks(world, structureBB, 2-i, 10-i, 3, 2-i, 10-i, 9, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), false);
        		this.fillWithBlocks(world, structureBB, 4+i, 10-i, 3, 4+i, 10-i, 9, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), false);
        	}
        	
        	// Various wooden stairs
        	for (int[] uvwo : new int[][]{
        		{1, 4, 0, 5}, {5, 4, 0, 4}, 
        		{1, 8, 3, 5}, {5, 8, 3, 4}, {2, 9, 3, 5}, {4, 9, 3, 4},
        		{1, 8, 9, 5}, {5, 8, 9, 4}, {2, 9, 9, 5}, {4, 9, 9, 4},
        		{3, 4, 4, 7}, {4, 4, 4, 7}, // Ceiling trim
        		{2, 1, 6, 1}, {2, 1, 7, 1}, // Bench
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	

        	// Grass
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 2:forward, 3:backward
        		// Front accent
        		{1,0,9, 5,0,14}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeGrassState, biomeGrassState, false);
            }
        	
        	
        	// Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int u : new int[]{1,5})
        	{
        		this.fillWithBlocks(world, structureBB, u, 1, 9, u, 1, 13, biomeFenceState, biomeFenceState, false);
        	}
        	this.fillWithBlocks(world, structureBB, 1, 1, 14, 5, 1, 14, biomeFenceState, biomeFenceState, false);
        	
            // Torches
        	for (int u : new int[]{1,5})
        	{
        		this.setBlockState(world, Blocks.torch.getStateFromMeta(0), u, 2, 14, structureBB);
        	}
            for (int[] uvwo : new int[][]{
            	{3,3,0,2},
            	{3,3,9,0},
            	{3,8,3,2},
            	{3,3,2,0},
            	{3,3,7,2},
            	{3,7,7,2},
            	{3,7,5,0},
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            // Counter
            this.fillWithBlocks(world, structureBB, 3, 0, 3, 4, 0, 3, Blocks.double_stone_slab.getStateFromMeta(0), Blocks.double_stone_slab.getStateFromMeta(0), false);
            this.setBlockState(world, Blocks.double_stone_slab.getStateFromMeta(0), 3, 0, 4, structureBB);
            this.fillWithBlocks(world, structureBB, 3, 0, 5, 4, 0, 5, Blocks.double_stone_slab.getStateFromMeta(0), Blocks.double_stone_slab.getStateFromMeta(0), false);
            this.setBlockState(world, Blocks.double_stone_slab.getStateFromMeta(0), 4, 1, 4, structureBB);
            
            // Chimney
            this.fillWithBlocks(world, structureBB, 4, 6, 5, 4, 11, 5, Blocks.cobblestone_wall.getStateFromMeta(0), Blocks.cobblestone_wall.getStateFromMeta(0), false);
            // Smoker - this is a TileEntity and needs to have its meta assigned manually
        	IBlockState smokerState = ModObjects.chooseModSmokerState(3, this.coordBaseMode);
            for (int[] uvwo : new int[][]{{4,5,5,0}})
            {
                //this.setBlockState(world, smokerBlock, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), smokerState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode)), 2);
            }
            // Roof plug
            this.setBlockState(world, Blocks.cobblestone.getStateFromMeta(0), 4, 10, 5, structureBB);
            
            // Wooden stairway
            for (int[] vw : new int[][]{
            	{1,5},
            	{3,7},
            	}) {
            	this.setBlockState(world, biomePlankState, 2, vw[0], vw[1], structureBB);
            }
            for (int i=0; i<=3; i++) {
            	this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 3)), 2, 1+i, 4+i, structureBB);
            }
            
            
            this.setBlockState(world, biomeCobblestoneState, 3, 0, 1, structureBB);
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 1, 1, 2, 1, 0},
            	{3, 1, 8, 0, 1, 0},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
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
        	
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{3, 1, 12},
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
            	boolean levelflag = random.nextBoolean();
            	int u = levelflag ? random.nextInt(2)+3 : 3;
            	int v = levelflag ? 5 : 1;
            	int w = levelflag ? random.nextInt(2)+6 : random.nextInt(6)+2;
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 4;}
    }
    
    
    
    // --- Cartographer House --- //
    
    public static class PlainsCartographer1 extends StructureVillagePieces.Village
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
            	"       ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFFFF ",
            	" FFPFF ",
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
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsCartographer1() {}

        public PlainsCartographer1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsCartographer1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsCartographer1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            
            // Log Frame
            // Corners
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uw : new int[][]{
            	{1,2}, {5,2},
            	{1,8}, {5,8},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 1, uw[1], uw[0], 4, uw[1], biomeLogVertState, biomeLogVertState, false);
            }
            for (int u : new int[]{1, 5})
            {
            	this.fillWithBlocks(world, structureBB, u, 4, 3, u, 4, 7, biomeLogVertState, biomeLogVertState, false);
            }
            for (int w : new int[]{2, 8})
            {
            	this.fillWithBlocks(world, structureBB, 2, 4, w, 4, 4, w, biomeLogVertState, biomeLogVertState, false);
            }
            
            // Floor
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.fillWithBlocks(world, structureBB, 2, 0, 3, 4, 0, 7, biomePlankState, biomePlankState, false);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.setBlockState(world, biomeCobblestoneState, 3, 0, 2, structureBB);
            
            // Left and right walls
            for (int u : new int[]{1,5}) {
            	this.fillWithBlocks(world, structureBB, u, 1, 3, u, 3, 7, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            // Front and back walls
            for (int w : new int[]{2,8}) {
            	this.fillWithBlocks(world, structureBB, 2, 1, w, 4, 3, w, biomeCobblestoneState, biomeCobblestoneState, false);
            	this.fillWithBlocks(world, structureBB, 2, 5, w, 4, 5, w, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{1, 2, 4}, {1, 2, 6}, // Left wall
        		{5, 2, 4}, {5, 2, 6}, // Right wall
        		{3, 2, 8}, // Back wall
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            // Roof
        	// First floor
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int i=0; i<=2; i++) {
        		this.fillWithBlocks(world, structureBB, 2-i, 6-i, 1, 2-i, 6-i, 9, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), false);
        		this.fillWithBlocks(world, structureBB, 4+i, 6-i, 1, 4+i, 6-i, 9, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), false);
        	}
        	this.fillWithBlocks(world, structureBB, 3, 6, 1, 3, 6, 9, biomePlankState, biomePlankState, false);
        	// Wooden slab (bottom)
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	this.fillWithBlocks(world, structureBB, 3, 7, 1, 3, 7, 9, biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
        	
        	// Various wooden stairs
        	for (int[] uvwo : new int[][]{
        		{1, 4, 1, 5}, {5, 4, 1, 4}, {2, 5, 1, 5}, {4, 5, 1, 4}, // Front trim
        		{1, 4, 9, 5}, {5, 4, 9, 4}, {2, 5, 9, 5}, {4, 5, 9, 4}, // Rear trim
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
            // Torches
            for (int[] uvwo : new int[][]{
            	{3,4,1,2}, {3,4,3,0}, // Front wall
            	{3,4,7,2}, // Back wall
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 1, 2, 2, 1, 1},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // --- Interior --- //
            
            // Table
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.setBlockState(world, biomeFenceState, 2, 1, 3, structureBB);
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_pressure_plate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            this.setBlockState(world, biomeWoodPressurePlateState, 2, 2, 3, structureBB);
            // Cartography Table
        	IBlockState cartographyTableState = ModObjects.chooseModCartographyTableState();
            this.setBlockState(world, cartographyTableState, 3, 1, 6, structureBB);
            // Carpet
            int[] carpetU = new int[]{2,2,2,3,4,4,4,3};
            int[] carpetW = new int[]{5,6,7,7,7,6,5,5};
            for (int i=0; i<=7; i++)
            {
            	this.setBlockState(world, Blocks.carpet.getStateFromMeta(i%2==0?
            			(GeneralConfig.useVillageColors ? this.townColor : 4):(GeneralConfig.useVillageColors ? this.townColor2 : 0)),
            			carpetU[i], 1, carpetW[i], structureBB);
            }
            
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
        	int chestV = 1;
        	int chestW = 3;
        	int chestO = 3;
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_cartographer");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
            // Trapdoor rim
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0 ? 6 : 4), 0, 1, 1, structureBB); // Left
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0 ? 7 : 5), 6, 1, 1, structureBB); // Right
        	for (int u : new int[]{1,2,4,5})
        	{
        		this.setBlockState(world, biomeGrassState, u, 1, 1, structureBB); // Sod
        		this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{4, 7, 5, 6})[this.coordBaseMode.getHorizontalIndex()]), u, 1, 0, structureBB); // Front face
        	}
        	
        	// Flowers on top
        	for (int f : new int[]{1,2,4,5})
        	{
        		int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : Blocks.red_flower;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), f, 2, 1, structureBB);
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
            	int u = random.nextInt(3)+2;
            	int v =  1;
            	int w = random.nextInt(2)+4;
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 1;}
    }
    
    
    
    // --- Fisher Cottage --- //
    
    public static class PlainsFisherCottage1 extends StructureVillagePieces.Village
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
            	"    FFFF  ",
            	"    FFFF  ",
            	"  FFFFFFFF",
            	" FFFFFFFFF",
            	"FFFFFFFFFF",
            	"FFFFFFFFFF",
            	"FFFFFFFFFF",
            	" FFFFFFFF ",
            	"  FFFFFF  ",
            	"   FFFFF  ",
            	"    FFFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 2; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 2;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsFisherCottage1() {}

        public PlainsFisherCottage1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsFisherCottage1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsFisherCottage1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
        	
            
            // Log Frame
            // Corners
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uw : new int[][]{
            	{3,1}, {7,1},
            	{3,5}, {7,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 2, uw[1], uw[0], 6, uw[1], biomeLogVertState, biomeLogVertState, false);
            }
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for (int u : new int[]{3, 7})
            {
            	this.fillWithBlocks(world, structureBB, u, 6, 2, u, 6, 4, biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for (int w : new int[]{1, 5})
            {
            	this.fillWithBlocks(world, structureBB, 4, 6, w, 6, 6, w, biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            // Floor
            this.fillWithBlocks(world, structureBB, 3, 1, 1, 7, 1, 5, biomeDirtState, biomeDirtState, false);
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.setBlockState(world, biomeCobblestoneState, 3, 0, 2, structureBB);
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.fillWithBlocks(world, structureBB, 5, 2, 2, 6, 2, 4, biomePlankState, biomePlankState, false);
            this.fillWithBlocks(world, structureBB, 4, 2, 2, 4, 2, 4, Blocks.flowing_water.getStateFromMeta(0), Blocks.flowing_water.getStateFromMeta(0), false);
            this.fillWithBlocks(world, structureBB, 4, 1, 2, 4, 1, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            
            // Left and right walls
            for (int u : new int[]{3,7}) {
            	this.fillWithBlocks(world, structureBB, u, 2, 2, u, 5, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            // Front and back walls
            for (int w : new int[]{1,5}) {
            	this.fillWithBlocks(world, structureBB, 4, 2, w, 6, 5, w, biomeCobblestoneState, biomeCobblestoneState, false);
            	this.fillWithBlocks(world, structureBB, 4, 7, w, 6, 7, w, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{3, 4, 3}, // Left wall
        		{7, 4, 3}, // Right wall
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            // Roof
        	// First floor
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int i=0; i<=2; i++) {
        		this.fillWithBlocks(world, structureBB, 4-i, 8-i, 0, 4-i, 8-i, 6, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 0)), false);
        		this.fillWithBlocks(world, structureBB, 6+i, 8-i, 0, 6+i, 8-i, 6, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, 1)), false);
        	}
        	this.fillWithBlocks(world, structureBB, 5, 8, 0, 5, 8, 6, biomePlankState, biomePlankState, false);
        	
        	// Various wooden stairs
        	for (int[] uvwo : new int[][]{
        		{3, 6, 0, 5}, {4, 7, 0, 5}, {6, 7, 0, 4}, {7, 6, 0, 4}, // Front trim
        		{3, 6, 6, 5}, {4, 7, 6, 5}, {6, 7, 6, 4}, {7, 6, 6, 4}, // Rear trim
        		{5, 2, 6, 2}, // Back platform
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
            // Torches
            for (int[] uvwo : new int[][]{
            	{5,5,0,2},
            	{5,5,6,0},
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{5, 3, 1, 2, 1, 1},
            	{5, 3, 5, 0, 1, 0},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // --- Interior --- //
            
            // Trapdoor gate
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    		this.fillWithBlocks(world, structureBB, 4, 3, 2, 4, 3, 4, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0?6:4), biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0?6:4), false);
    		
    		//this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0 ? 6 : 4), 0, 1, 1, structureBB); // Left
        	//this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0 ? 7 : 5), 6, 1, 1, structureBB); // Right
    		
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 6;
        	int chestV = 3;
        	int chestW = 4;
        	int chestO = 2;
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_fisher");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
        	
        	// --- Backyard --- //
            
        	
            // Grass rim
        	for (int[] uvwm : new int[][]{
        		{1,3}, 
        		{0,4}, {0,5}, {0,6}, 
        		{1,7}, 
        		{2,8}, {3,8}, 
        		{4,9}, {4,10}, {5,10}, {6,10}, {7,10}, 
        		{8,8}, 
        		{9,8}, {9,7}, {9,6}, {9,5}, {9,4}, 
        		{8,3}, 
        		{7,0}, 
           		})
        	{
            	this.setBlockState(world, biomeGrassState, uvwm[0], 1, uvwm[1], structureBB);
            }
        	
        	
        	// Pond
        	int[][] waterDepths = new int[][]{
    			{4,6},
    			{3,7},
    			{6,7},
    			{6,8},
    			{6,9},
    			{6,9},
    			{6,8},
    			{4,7},
        	};
        	for (int u=0; u<=7; u++)
            {
        		this.fillWithBlocks(world, structureBB, u+1, 1, waterDepths[u][0], u+1, 1, waterDepths[u][1], Blocks.flowing_water.getStateFromMeta(0), Blocks.flowing_water.getStateFromMeta(0), false);
            }
        	
        	// Balcony
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	this.setBlockState(world, biomeWoodSlabBottomState, 5, 2, 7, structureBB);
    		this.fillWithBlocks(world, structureBB, 5, 1, 6, 5, 1, 7, biomePlankState, biomePlankState, false); // Instead of fences b/c waterlogging
    		
    		// Barrels
    		IBlockState barrelState = ModObjects.chooseModBarrelBlockState();
    		boolean isChestType=(barrelState==null);
    		for (int[] uvwoo : new int[][]{
    			// u, v, w, orientationIfChest, orientationIfUTDBarrel
    			// orientationIfChest:  0=foreward (away from you),  1=rightward,  2=backward (toward you),  3=leftward
    			// orientationIfUTDBarrel:  1=vertical,  0=forward,  1=rightward,  2=backward (toward you),  3=leftward
    			// TODO - use different barrel meta for different mods
            	{2,2,2, 3,-1},
            	{7,2,9, 1,-1},
            })
            {
    			// Set the barrel, or a chest if it's not supported
    			if (isChestType) {barrelState = Blocks.chest.getDefaultState();}
    			this.setBlockState(world, barrelState, uvwoo[0], uvwoo[1], uvwoo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwoo[0], uvwoo[2]), this.getYWithOffset(uvwoo[1]), this.getZWithOffset(uvwoo[0], uvwoo[2])), barrelState.getBlock().getStateFromMeta(isChestType?StructureVillageVN.chooseFurnaceMeta(uvwoo[3], this.coordBaseMode):StructureVillageVN.chooseFurnaceMeta(uvwoo[4], this.coordBaseMode)), 2);
                // Dirt beneath
                this.setBlockState(world, biomeDirtState, uvwoo[0], uvwoo[1]-1, uvwoo[2], structureBB);
            }
    		
    		
    		// Front steps
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int uvwm[] : new int[][]{
        		{4, 2, 0, 0}, // Corner
        		{5, 2, 0, 3},
        		{6, 2, 0, 1}, // Corner
        	})
        	{
        		this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwm[3])), uvwm[0], uvwm[1], uvwm[2], structureBB);
        	}
        	this.setBlockState(world, biomeDirtState, 7, 1, 0, structureBB);
        	
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
        	
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = random.nextInt(2)+5;
            	int v =  3;
            	int w = random.nextInt(2)+2;
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Fletcher House --- //
    
    public static class PlainsFletcherHouse1 extends StructureVillagePieces.Village
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
            	"           ",
            	" FFFFFFFFF ",
            	" FFFFFFFFF ",
            	" FFFFFFFFF ",
            	" FFFFFFFFF ",
            	"   FFFFF   ",
            	"    FFF    ",
            	" F   P  F F",
            	"    FPF FFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsFletcherHouse1() {}

        public PlainsFletcherHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsFletcherHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsFletcherHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            // The grass patch in front
            for (int[] uwg : new int[][]{ // g is grass type
            	{8,0,0},
            	{9,0,1},
            	{10,0,1},
            	{8,1,0},
            	{10,1,0},
            })
            {
    			if (uwg[2]==0) // Short grass
    			{
    				this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), uwg[0], GROUND_LEVEL, uwg[1], structureBB);
    			}
    			else // Tall grass
    			{
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(2), uwg[0], GROUND_LEVEL, uwg[1], structureBB);
    				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uwg[0], GROUND_LEVEL+1, uwg[1], structureBB);
    			}
            }
            
            // Log Frame
            // Corners
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uw : new int[][]{
            	{1,4}, {9,4},
            	{1,7}, {9,7},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 1, uw[1], uw[0], 4, uw[1], biomeLogVertState, biomeLogVertState, false);
            }
            // Crossbeams
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for (int u : new int[]{1,9})
            {
            	this.fillWithBlocks(world, structureBB, u, 4, 5, u, 4, 6, biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            this.fillWithBlocks(world, structureBB, 2, 4, 7, 8, 4, 7, biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            
            // Floor
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.fillWithBlocks(world, structureBB, 4, 0, 3, 6, 0, 3, biomePlankState, biomePlankState, false);
            this.fillWithBlocks(world, structureBB, 3, 0, 4, 7, 0, 4, biomePlankState, biomePlankState, false);
            this.fillWithBlocks(world, structureBB, 2, 0, 5, 8, 0, 6, biomePlankState, biomePlankState, false);
            this.setBlockState(world, biomePlankState, 5, 0, 2, structureBB);
            
            // Walls
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int u : new int[]{2,8}) {
            	this.fillWithBlocks(world, structureBB, u, 1, 4, u, 4, 4, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            for (int u : new int[]{1,9}) {
            	this.fillWithBlocks(world, structureBB, u, 1, 5, u, 3, 6, biomeCobblestoneState, biomeCobblestoneState, false);
            	this.fillWithBlocks(world, structureBB, u, 5, 5, u, 5, 6, biomeCobblestoneState, biomeCobblestoneState, false);
            }
            this.fillWithBlocks(world, structureBB, 2, 1, 7, 8, 3, 7, biomeCobblestoneState, biomeCobblestoneState, false);
            this.fillWithBlocks(world, structureBB, 4, 5, 7, 6, 5, 7, biomeCobblestoneState, biomeCobblestoneState, false);
            // Front wall
            this.fillWithBlocks(world, structureBB, 4, 1, 2, 6, 4, 2, biomePlankState, biomePlankState, false);
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{1, 2, 5}, {1, 2, 6}, // Left wall
        		{9, 2, 5}, {9, 2, 6}, // Right wall
        		{3, 2, 7}, {5, 2, 7}, {7, 2, 7}, // Back wall
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	// Various wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Front awning
        		{3, 4, 2, 0}, {4, 5, 2, 0}, {6, 5, 2, 1}, {7, 4, 2, 1},
        		// Front roof
        		{2, 4, 3, 0}, {3, 5, 3, 0}, {4, 6, 3, 0}, {6, 6, 3, 1}, {7, 5, 3, 1}, {8, 4, 3, 1},
        		{3, 5, 4, 3}, {4, 6, 4, 0}, {6, 6, 4, 1}, {7, 5, 4, 3},
        		{4, 6, 5, 3}, {6, 6, 5, 3},
        		// Back roof
        		{4, 6, 6, 2}, {6, 6, 6, 2},
        		{3, 5, 7, 2}, {4, 6, 7, 0}, {6, 6, 7, 1}, {7, 5, 7, 2},
        		{3, 5, 8, 0}, {4, 6, 8, 0}, {6, 6, 8, 1}, {7, 5, 8, 1},
        		// Left roof
        		{0, 4, 3, 3}, {0, 5, 4, 3}, {0, 6, 5, 3}, {0, 6, 6, 2}, {0, 5, 7, 2}, {0, 4, 8, 2},
        		{1, 4, 3, 3}, {1, 5, 4, 3}, {1, 6, 5, 3}, {1, 6, 6, 2}, {1, 5, 7, 2}, {1, 4, 8, 2},
        		{2, 5, 4, 3}, {2, 6, 5, 3}, {2, 6, 6, 2}, {2, 5, 7, 2}, {2, 4, 8, 2},
        		{3, 6, 5, 3}, {3, 6, 6, 2},
        		// Right roof
        		{7, 6, 5, 3}, {7, 6, 6, 2},
        		{8, 5, 4, 3}, {8, 6, 5, 3}, {8, 6, 6, 2}, {8, 5, 7, 2}, {8, 4, 8, 2},
        		{9, 4, 3, 3}, {9, 5, 4, 3}, {9, 6, 5, 3}, {9, 6, 6, 2}, {9, 5, 7, 2}, {9, 4, 8, 2},
        		{10, 4, 3, 3}, {10, 5, 4, 3}, {10, 6, 5, 3}, {10, 6, 6, 2}, {10, 5, 7, 2}, {10, 4, 8, 2},
        		
        		
        		// Left roof trim
        		{0, 4, 4, 6}, {0, 5, 5, 6}, {0, 5, 6, 7}, {0, 4, 7, 7},
        		// Right roof trim
        		{10, 4, 4, 6}, {10, 5, 5, 6}, {10, 5, 6, 7}, {10, 4, 7, 7},
        		// Back roof trim
        		{3, 4, 8, 5}, {4, 5, 8, 5}, {6, 5, 8, 4}, {7, 4, 8, 4},
        		
        		
        		// Interior supports
        		{3, 5, 5, 0}, {3, 5, 6, 0},
        		{7, 5, 5, 1}, {7, 5, 6, 1},
        		
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	this.fillWithBlocks(world, structureBB, 5, 6, 3, 5, 6, 8, biomePlankState, biomePlankState, false);
        	
        	// Torches
            for (int[] uvwo : new int[][]{
            	{5,3,1,2}, {5,3,3,0}, // Front door
            	{4,4,6,2}, {6,4,6,2}, {4,2,8,0}, {6,2,8,0}, // Back wall
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{5, 1, 2, 2, 1, 1},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // --- Interior --- //
            
            // Plank framework
            for (int u : new int[]{3,7})
            {
            	this.fillWithBlocks(world, structureBB, u, 1, 3, u, 4, 3, biomePlankState, biomePlankState, false);
            }
            for (int u : new int[]{4,6})
            {
            	this.setBlockState(world, biomePlankState, u, 5, 3, structureBB);
            }
            this.setBlockState(world, biomePlankState, 5, 5, 2, structureBB);
            
            // Table
            this.fillWithBlocks(world, structureBB, 8, 1, 5, 8, 1, 6, biomePlankState, biomePlankState, false);
            
            // 0: nuffin
            // 1: poppy
            // 2: dandelion
            int flowernumber = random.nextInt(2)+1;
            
            // Flower pot
        	int potU = 8;
        	int potV = 2;
        	int potW = 6;
        	int potX = this.getXWithOffset(potU, potW);
        	int potY = this.getYWithOffset(potV);
        	int potZ = this.getZWithOffset(potU, potW);
        	
        	TileEntity flowerPot = (new BlockFlowerPot()).createNewTileEntity(world, flowernumber);
    		BlockPos flowerPotPos = new BlockPos(potX, potY, potZ);
    		world.setBlockState(flowerPotPos, Blocks.flower_pot.getDefaultState());
    		world.setTileEntity(flowerPotPos, flowerPot);
        	
            // Fletching Table
        	IBlockState fletchingTableState = ModObjects.chooseModFletchingTableState();
        	this.setBlockState(world, fletchingTableState, 2, 1, 6, structureBB);
            
            // Carpet
            this.fillWithBlocks(world, structureBB, 4, 1, 5, 6, 1, 5, Blocks.carpet.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor : 4)), Blocks.carpet.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor : 4)), false);
            
            
            // --- Front awning --- //
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int u : new int[]{4,6})
            {
            	this.setBlockState(world, biomeGrassState, u, 0, 0, structureBB);
                this.fillWithBlocks(world, structureBB, u, 1, 0, u, 3, 0, biomeFenceState, biomeFenceState, false);
                this.fillWithBlocks(world, structureBB, u, 4, 0, u, 4, 1, biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);
            }
            // Wool
            this.setBlockState(world, Blocks.wool.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor : 4)), 5, 4, 0, structureBB);
            this.setBlockState(world, Blocks.wool.getStateFromMeta((GeneralConfig.useVillageColors ? this.townColor2 : 0)), 5, 4, 1, structureBB);
            
            
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
            for (int decorUVW[] : new int[][]{
            	{1, 1, 1},
            	})
            {
                // Add foundations
            	this.setBlockState(world, biomeDirtState, decorUVW[0], decorUVW[1]-1, decorUVW[2], structureBB);
        		this.replaceAirAndLiquidDownwards(world, biomeFillerState, decorUVW[0], decorUVW[1]-2, decorUVW[2], structureBB);
        		
                // The actual decor
        		for (int j=0; j<decorUVW.length; j++)
                {
        			int decorHeightY = decorUVW[1];
        			
        			// Set random seed
                	Random randomFromXYZ = new Random();
                	randomFromXYZ.setSeed(
        					world.getSeed() +
        					FunctionsVN.getUniqueLongForXYZ(
        							this.getXWithOffset(decorUVW[0], decorUVW[2]),
        							this.getYWithOffset(decorHeightY),
        							this.getZWithOffset(decorUVW[0], decorUVW[2])
        							)
                			);
                	
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
                	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
                	
                	for (BlueprintData b : decorBlueprint)
                	{
                		// Place block indicated by blueprint
                		this.setBlockState(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos(), decorUVW[2]+b.getWPos(), structureBB);
                		
                		// Fill below if flagged
                		if ((b.getfillFlag()&1)!=0)
                		{
                			this.replaceAirAndLiquidDownwards(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()-1, decorUVW[2]+b.getWPos(), structureBB);
                		}
                		
                		// Clear above if flagged
                		if ((b.getfillFlag()&2)!=0)
                		{
                			this.clearCurrentPositionBlocksUpwards(world, decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()+1, decorUVW[2]+b.getWPos(), structureBB);
                		}            		
                	}
                }
            }
            
			//LogHelper.info("Fletcher house spawned at " + this.getXWithOffset(4, 0) + " " + this.getYWithOffset(1) + " " + this.getZWithOffset(4, 0) + " with horizIndex " + this.coordBaseMode);
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = random.nextInt(5)+3;
            	int v =  1;
            	int w = random.nextInt(3)+4;
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Large Farm --- //
    
    public static class PlainsLargeFarm1 extends StructureVillagePieces.Village
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
    	private static final int STRUCTURE_WIDTH = 9;
    	public static final int STRUCTURE_HEIGHT = 6;
    	private static final int STRUCTURE_DEPTH = 13;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsLargeFarm1() {}

        public PlainsLargeFarm1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsLargeFarm1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsLargeFarm1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear above and make foundation
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            		
        		this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL+1, w, structureBB); // Clear above
    			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB); // Foundation
    			// Ground-level blocks
    			if(u==0 || u==(STRUCTURE_WIDTH-1) || w==0 || w==(STRUCTURE_DEPTH-1) )
    			{
    				this.setBlockState(world, biomeLogVertState, u, GROUND_LEVEL, w, structureBB);
    			}
    			else
    			{
    				this.setBlockState(world, Blocks.farmland.getStateFromMeta(7), u, GROUND_LEVEL, w, structureBB);
    			}
            }}
            // Place one non-moistened tilled block
            this.setBlockState(world, Blocks.farmland.getStateFromMeta(0), 7, GROUND_LEVEL, 2, structureBB);
            
            
            // Log Frame interior
            this.fillWithBlocks(world, structureBB, 1, 0, 6, STRUCTURE_WIDTH-2, 0, 6, biomeLogVertState, biomeLogVertState, false);
            // Water
            for (int w : new int[]{3,9}) {
            	this.fillWithBlocks(world, structureBB, 1, 0, w, STRUCTURE_WIDTH-2, 0, w, Blocks.flowing_water.getStateFromMeta(0), Blocks.flowing_water.getStateFromMeta(0), false);
            }
            
            
        	// Pairs of crops are generated together. These are pairs of ages arrays.
        	int[][][] uPairArray = {
    			{{1,2,3,4,5,6,7},
    			 {1,2,3,4,5,6,7}},
    			
    			{{1,2,3,4,5,6,7},
        		 {1,2,3,4,5,6,7}},
    			
    			{{1,2,3,4,5,6,7},
            	 {1,2,3,4,5,6,7}},
    			
    			{{1,2,3,4,5,6,7},
                 {1,2,3,4,5,6,7}},
        	};
        	int[][][] vPairArray = {
    			{{1,1,1,1,1,1,1},
       			 {1,1,1,1,1,1,1}},
       			
       			{{1,1,1,1,1,1,1},
           		 {1,1,1,1,1,1,1}},
       			
       			{{1,1,1,1,1,1,1},
               	 {1,1,1,1,1,1,1}},
       			
       			{{1,1,1,1,1,1,1},
                 {1,1,1,1,1,1,1}},
           	};
        	int[][][] wPairArray = {
    			{{11,11,11,11,11,11,11},
       			 {10,10,10,10,10,10,10}},
       			
       			{{8,8,8,8,8,8,8},
           		 {7,7,7,7,7,7,7}},
       			
       			{{5,5,5,5,5,5,5},
               	 {4,4,4,4,4,4,4}},
       			
       			{{2,2,2,2,2,2,2},
                 {1,1,1,1,1,1,1}},
           	};
        	int[][][] cropPairAgeArray = {
    			{{0,7,7,7,7,7,0},
       			 {7,7,3,1,7,1,0}},
       			
       			{{1,6,2,0,0,7,7},
           		 {0,0,1,0,0,0,0}},
       			
       			{{0,0,1,1,0,1,0},
               	 {0,1,0,0,1,0,0}},
       			
       			{{0,0,0,0,1,0,0},
                 {0,0,0,0,0,0,0}},
           	};
        	// Iterate through the pairs of crops
            for (int crop_pair=0; crop_pair<cropPairAgeArray.length; crop_pair++)
            {
            	Block[] cropBlocks = StructureVillageVN.chooseCropPair(random);
            	
            	// Iterate through the members of a crop pair
            	for (int crop_member=0; crop_member<2; crop_member++)
            	{
            		// Iterate through u positions
            		for (int i=0; i<cropPairAgeArray[crop_pair][crop_member].length; i++)
                	{
            			int cropProgressMeta = cropPairAgeArray[crop_pair][crop_member][i]; // Isolate the crop's age meta value
            			IBlockState cropState;
            			
            			while(true)
            			{
            				try {cropState = cropBlocks[0].getStateFromMeta(cropProgressMeta);}
            				catch (IllegalArgumentException e)
            				{
            					// The assignment failed with a meta of 0. IDK what's happening so just have wheat
            					if (cropProgressMeta==0) {cropState = Blocks.wheat.getStateFromMeta(cropPairAgeArray[crop_pair][crop_member][i]);}
            					// The crop is not allowed to have this value. Cut it in half and try again.
            					else {cropProgressMeta /= 2; continue;}
            				}
            				
            				// Finally, assign the working crop
        					this.setBlockState(world, cropState,
        							uPairArray[crop_pair][crop_member][i],
                					vPairArray[crop_pair][crop_member][i],
                					wPairArray[crop_pair][crop_member][i],
                					structureBB);
            				break;
            			}
                	}
            	}
            }
            
            
            
            /*
            // Crops here
            Block[] cropBlocks;
            
            for (int w=0; w<4; w++)
            {
            	cropBlocks = StructureVillageVN.chooseCropPair(random);
            	
            	for (int i=0; i<2; i++)
            	{
            		this.fillWithBlocks(world, structureBB, 1, 1, 3*w +i +1, STRUCTURE_WIDTH-2, 1, 3*w +i +1, cropBlocks[i], 0, cropBlocks[i], 0, false);
            	}
            }
            */
            // Attempt to add GardenCore Compost Bins. If this fails, do nothing
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            if (compostBinState != null)
            {
            	this.setBlockState(world, compostBinState.getBlock().getDefaultState(), 1, 1, 1, structureBB); this.setBlockState(world, biomeDirtState, 1, 0, 1, structureBB);
            	this.setBlockState(world, compostBinState.getBlock().getDefaultState(), 1, 1, 11, structureBB); this.setBlockState(world, biomeDirtState, 1, 0, 11, structureBB);
            }
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u=0;
            	int w=3;
            	int v= 1;
            	
            	while (w==3 || w==9)
            	{
            		u = random.nextInt(STRUCTURE_WIDTH);
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Plains Large Library --- //
    
    public static class PlainsLibrary1 extends StructureVillagePieces.Village
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
            	"                 ",
            	" FFFFFFFFFFFFFFF ",
            	" FFFFFFFFFFFFFFF ",
            	" FFFFFFFFFFFFFFF ",
            	" FFFFFFFFFFFFFFF ",
            	" FFFFFFFFFFFFFFF ",
            	" FFFFFFFFFFFFFFF ",
            	" FFFFFFFFFFFFFFF ",
            	"      FFFFF      ",
            	"       FFF       ",
            	"        F        ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsLibrary1() {}
        
        public PlainsLibrary1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static PlainsLibrary1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsLibrary1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            
            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,0,3, 5,0,9},
            	{11,0,3, 15,0,9},
            	// Front door
            	{6,0,2, 10,0,9},
            	{7,0,1, 9,0,1},
            	{6,1,2, 6,3,2}, {10,1,2, 10,3,2},
            	{7,1,1, 7,3,1}, {9,1,1, 9,3,1},
            	// Interior stairs
            	{5,1,5, 5,2,5}, {11,1,5, 11,2,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            this.setBlockState(world, biomeCobblestoneState, 8, 3, 1, structureBB);
            this.setBlockState(world, biomeCobblestoneState, 4, 1, 5, structureBB);
            this.setBlockState(world, biomeCobblestoneState, 12, 1, 5, structureBB);

            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left and right walls
            	{1,1,4, 1,6,8},
            	{15,1,4, 15,6,8},
            	// Front walls
            	{2,1,3, 4,5,3}, {12,1,3, 14,5,3}, {6,4,3, 10,6,3}, {7,8,3, 9,8,3},
            	// Back wall
            	{2,1,9, 14,5,9}, 
            	// Platform
            	{7,3,2, 9,3,2}, {6,3,3, 10,3,5},
            	// Interior plank beams
            	{2,6,4, 5,6,4}, {11,6,4, 14,6,4}, {2,6,8, 14,6,8},
            	{2,7,4, 6,7,5}, {10,7,4, 14,7,5}, {2,7,7, 14,7,7}, {0,8,6, 16,8,6},
            	{8,9,2, 8,9,5}, {7,8,4, 7,8,5}, {9,8,4, 9,8,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            // Log frame
            // Exterior corners
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uw : new int[][]{{1,3},{1,9},{15,3},{15,9}})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 0, uw[1], uw[0], 5, uw[1], biomeLogVertState, biomeLogVertState, false);
            }
            // Interior corners
            for(int[] uw : new int[][]{{5,3},{11,3}})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 0, uw[1], uw[0], 6, uw[1], biomeLogVertState, biomeLogVertState, false);
            }
            // Across beams
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	{2,4,3, 4,4,3},
            	{12,4,3, 14,4,3},
            	{6,7,3, 10,7,3},
            	{2,4,9, 14,4,9},
            })
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            for(int u : new int[]{3,5,7,9,11,13}){
            	this.setBlockState(world, biomeLogHorAcrossState, u, 2, 9, structureBB);
            }
            // Along beams
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for(int[] uuvvww : new int[][]{
            	{1,4,4, 1,4,8},
            	{1,7,5, 1,7,7},
            	{15,4,4, 15,4,8},
            	{15,7,5, 15,7,7},
            })
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            for (int u : new int[]{1,15}) {for (int w : new int[]{5,7}) {
            	this.setBlockState(world, biomeLogHorAlongState, u, 2, w, structureBB);
            }}
            
            // Windows
        	for (int[] uw : new int[][]{
        		{3, 3}, {13, 3}, // Front wall
        		{1, 6}, {15, 6}, // Left and right walls
        		{4, 9}, {8, 9}, {12, 9}, // Back wall
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uw[0], 2, uw[1], structureBB);
            }
            
        	// Various wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward; +4:inverted
        		
        		// Left roof
        		{0,5,2, 4,5,2, 3}, {0,6,3, 4,6,3, 3}, {0,7,4, 4,7,4, 3}, {0,8,5, 5,8,5, 3},
        		// Right roof
        		{12,5,2, 16,5,2, 3}, {12,6,3, 16,6,3, 3}, {12,7,4, 16,7,4, 3}, {11,8,5, 16,8,5, 3},
        		// Back roof
        		{0,5,10, 16,5,10, 2}, {0,6,9, 16,6,9, 2}, {0,7,8, 16,7,8, 2}, {0,8,7, 16,8,7, 2}, {7,9,6, 9,9,6, 2},
        		// Front roof
        		{5,7,2, 5,7,4, 0}, {11,7,2, 11,7,4, 1},
        		{6,8,2, 6,8,5, 0}, {10,8,2, 10,8,5, 1},
        		{7,9,2, 7,9,5, 0}, {9,9,2, 9,9,5, 1},
        		// Trim
        		{6,7,2, 6,7,2, 1+4}, {10,7,2, 10,7,2, 0+4}, {7,8,2, 7,8,2, 1+4}, {9,8,2, 9,8,2, 0+4},
        		{0,5,3, 0,5,3, 2+4}, {0,5,9, 0,5,9, 3+4}, {0,6,4, 0,6,4, 2+4}, {0,6,8, 0,6,8, 3+4}, {0,7,5, 0,7,5, 2+4}, {0,7,7, 0,7,7, 3+4},
        		{16,5,3, 16,5,3, 2+4}, {16,5,9, 16,5,9, 3+4}, {16,6,4, 16,6,4, 2+4}, {16,6,8, 16,6,8, 3+4}, {16,7,5, 16,7,5, 2+4}, {16,7,7, 16,7,7, 3+4},
        		// Interior between doors
        		{8,4,4, 8,4,4, 2},
        		// Benches
        		{2,1,7, 2,1,8, 1}, {3,1,8, 4,1,8, 3},
        		{14,1,7, 14,1,8, 0}, {12,1,8, 13,1,8, 3},
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);
            }
        	
        	// Torches
            for (int[] uvwo : new int[][]{
            	{1,2,2,2}, {15,2,2,2}, {7,2,0,2}, {9,2,0,2}, // Front
            	{6,2,10,0}, {10,2,10,0}, // Back
            	{6,3,6,0}, {10,3,6,0}, // Floor separation
            	{2,5,6,1}, {14,5,6,3}, // Upper interior walls
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	// Interior
        	// Various stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward; +4:inverted
        		// Left
        		{3,1,5, 0}, {4,2,5, 0}, {5,3,5, 0},
        		// Right
        		{13,1,5, 1}, {12,2,5, 1}, {11,3,5, 1},
        		// Front door
        		{8,0,0, 3},
        		})
            {
        		this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[3]%4)+(uuvvwwo[3]/4)*4), uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], structureBB);
            }
            
            // Bookshelves
            for(int[] uuvvww : new int[][]{
            	{5,1,8, 7,1,8}, {6,2,8, 6,2,8},
            	{9,1,8, 11,1,8}, {10,2,8, 10,2,8},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);	
            }
        	
            // Lecterns
        	/*
            blockObject = ModObjects.chooseModLectern(); Block lecternBlock = (Block) blockObject[0]; int lecternMeta = (Integer) blockObject[1];
            for (int u : new int[]{6, 10})
            {
            	this.setBlockState(world, lecternBlock, lecternMeta, u, 1, 5, structureBB);
            }
            */
            
            for (int[] uvwo : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{6,1,5, 1},
            	{10,1,5, 3},
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
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{8, 1, 1, 2, 1, 0},
            	{7, 4, 3, 2, 1, 0},
            	{9, 4, 3, 2, 1, 1},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.fillWithBlocks(world, structureBB, 7, 4, 1, 9, 4, 1, biomeFenceState, biomeFenceState, false);
            for (int u : new int[]{6,10}) {
            	this.setBlockState(world, biomeFenceState, u, 4, 2, structureBB);
            }
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
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
            	int u;
            	int w;
            	int v;
            	
            	if (random.nextBoolean())
            	{
            		// Upstairs
                	u=6+random.nextInt(5);
                	v=4;
                	w=5;
            	}
            	else
            	{
            		// Downstairs
                	u=3+random.nextInt(11);
                	v=1;
                	w=6+random.nextInt(2);
            	}
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 1;}
    }
    
    
    
    // --- Plains Small Library --- //
    
    public static class PlainsLibrary2 extends StructureVillagePieces.Village
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
            	" FFFFFFF ",
            	" FFFFFFF ",
            	" FFFFFFF ",
            	" FFFFFFF ",
            	" FFFFFFF ",
            	"   P P   ",
            	"  FPFPF  ",
            };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 10;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsLibrary2() {}
        
        public PlainsLibrary2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static PlainsLibrary2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsLibrary2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            
            
            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,3, 6,0,4},
            	// Front
            	{2,1,2, 6,3,2},
            	// Back
            	{2,1,6, 6,3,6},
            	// Left
            	{1,1,3, 1,3,5},
            	// Right
            	{7,1,3, 7,3,5},
            	// Under bookshelves
            	{2,0,5, 3,0,5},
            	// Foot of stairs
            	{6,0,5, 6,0,5},
            	// Under doors
            	{3,0,2, 3,0,2},
            	{5,0,2, 5,0,2},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Second floor
            	
            	// Floor
            	{2,4,3, 6,4,4},
            	// Front
            	{2,5,2, 6,7,2},
            	// Back
            	{2,5,6, 6,7,6},
            	// Left
            	{1,5,3, 1,7,5},
            	// Right
            	{7,5,3, 7,7,5},
            	// Balcony
            	{2,4,1, 6,4,1},
            	// Corner of second floor
            	{6,4,5, 6,4,5},
            	// Stairs
            	{4,1,5, 4,1,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            // Logs
            // Vertical beams
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,1,2, 1,3,2}, {1,5,2, 1,6,2}, 
            	{1,1,6, 1,3,6}, {1,5,6, 1,6,6},
            	{7,1,2, 7,3,2}, {7,5,2, 7,6,2}, 
            	{7,1,6, 7,3,6}, {7,5,6, 7,6,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
            }
            // Across beams
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	{2,4,0, 6,4,0},
            	{1,4,2, 7,4,2},
            	{3,8,2, 5,8,2},
            	{1,4,6, 7,4,6},
            	{3,8,6, 5,8,6},
            })
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            // Along beams
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for(int[] uuvvww : new int[][]{
            	{1,4,3, 1,4,5},
            	{7,4,3, 7,4,5},
            	{4,9,1, 4,9,7},
            })
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            for (int u : new int[]{1,7}) {
            	this.setBlockState(world, biomeLogHorAlongState, u, 4, 1, structureBB);
            }
            
            // Windows
        	for (int[] uvw : new int[][]{
        			{1,2,4}, {1,6,4},
        			{7,2,4}, {7,6,4},
        			{3,2,6}, {5,2,6}, {3,6,6}, {5,6	,6},
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	// Various wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward; +4:inverted
        		
        		// Roof
        		{0,6,1, 0,6,2, 0}, {8,6,1, 8,6,2, 1},
        		{0,6,6, 0,6,7, 0}, {8,6,6, 8,6,7, 1}, 
        		{1,7,1, 1,7,2, 0}, {7,7,1, 7,7,2, 1}, 
        		{1,7,6, 1,7,7, 0}, {7,7,6, 7,7,7, 1}, 
        		{2,8,1, 2,8,7, 0}, {6,8,1, 6,8,7, 1}, 
        		{3,9,1, 3,9,7, 0}, {5,9,1, 5,9,7, 1}, 
        		// Side awning
        		{0,7,3, 0,7,5, 0}, {8,7,3, 8,7,5, 1}, 
        		// Roof trim
        		{1,6,1, 1,6,1, 5}, {7,6,1, 7,6,1, 4}, 
        		{2,7,1, 2,7,1, 5}, {6,7,1, 6,7,1, 4}, 
        		{3,8,1, 3,8,1, 5}, {5,8,1, 5,8,1, 4}, 
        		{1,6,7, 1,6,7, 5}, {7,6,7, 7,6,7, 4}, 
        		{2,7,7, 2,7,7, 5}, {6,7,7, 6,7,7, 4}, 
        		{3,8,7, 3,8,7, 5}, {5,8,7, 5,8,7, 4}, 
        		// Roof awning trims
        		{0,6,3, 0,6,3, 6}, {0,6,5, 0,6,5, 7},
        		{8,6,3, 8,6,3, 6}, {8,6,5, 8,6,5, 7}, 
        		// Inside stairs
        		{5,1,5, 5,1,5, 1}, 
        		{4,2,5, 4,2,5, 1},
        		{3,3,5, 3,3,5, 1},
        		{2,4,5, 2,4,5, 1}, 
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);
            }
        	
        	// Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright; 
            	// Front
            	{3,3,1,2}, {5,3,1,2}, {3,7,1,2}, {5,7,1,2},
            	// Back
            	{4,2,7,0}, {4,6,7,0}, 
            	// Left
            	{0,4,4,3}, 
            	// Right
            	{8,4,4,1},
            	
            	// Interior
            	{4,2,3,0}, {4,4,5,2}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	// Interior
        	
            // Bookshelves
            for(int[] uuvvww : new int[][]{
            	{3,1,5, 3,1,5}, 
            	{2,1,5, 2,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.bookshelf.getDefaultState(), Blocks.bookshelf.getDefaultState(), false);	
            }
        	
            // Lecterns
        	/*
            blockObject = ModObjects.chooseModLectern(); Block lecternBlock = (Block) blockObject[0]; int lecternMeta = (Integer) blockObject[1];
            this.setBlockState(world, lecternBlock, lecternMeta, 2, 1, 3, structureBB);
            */
            for (int[] uvwo : new int[][]{ // u, v, w, orientation, color meta
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3, 1},
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
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 1, 2, 2, 1, 1},
            	{5, 1, 2, 2, 1, 0},
            	{3, 5, 2, 2, 1, 1},
            	{5, 5, 2, 2, 1, 0},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{2,1,0, 2,2,0}, {4,1,0, 4,2,0}, {6,1,0, 6,2,0}, 
            	{2,3,0, 6,3,0}, {2,5,0, 6,5,0},
            	{1,5,1, 1,5,1}, {7,5,1, 7,5,1}, 
            })
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
            // Front path
            for (int u : new int[]{2,4,6})
            {
            	this.setBlockState(world, biomeGrassState, u, GROUND_LEVEL-1, 0, structureBB);
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
        	
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3+random.nextInt(4);
            	int v = random.nextBoolean() ? 5 : 1;
            	int w = 3+random.nextInt(2);
            	            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 1;}
    }
    
    
    
    // --- Plains Mason House --- //
    
    public static class PlainsMasonsHouse1 extends StructureVillagePieces.Village
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
            	" FFFFFFF ",
            	" FFFFFFF ",
            	" FFFFFFF ",
            	" FFFFFFF ",
            	" FFFFFFF ",
            	"  FFFFFF ",
            	"   FFFFF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 2;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsMasonsHouse1() {}
        
        public PlainsMasonsHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static PlainsMasonsHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsMasonsHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            
            
            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{1,0,2, 7,0,6},
            	{3,0,1, 7,0,1},
            	{4,0,0, 7,0,0},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Embedded into the floor
            	{3,0,4, 5,0,4},
            	// Roof
            	{4,6,1, 4,6,7},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }

            
            // Stained terracotta
            for(int[] uuvvww : new int[][]{
            	// Front
            	{2,1,2, 6,3,2}, {3,5,2, 5,5,2},
            	// Back
            	{2,1,6, 6,3,6}, {3,5,6, 5,5,6},
            	// Left
            	{1,1,3, 1,3,5},
            	// Right
            	{7,1,3, 7,3,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors? this.townColor2:0), Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors? this.townColor2:0), false);	
            }
            
            
            // Logs
            // Vertical beams
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,1,2, 1,3,2}, 
            	{1,1,6, 1,3,6}, 
            	{7,1,2, 7,3,2}, 
            	{7,1,6, 7,3,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
            }
            // Across beams
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	{2,4,2, 6,4,2},
            	{2,4,6, 6,4,6},
            })
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            // Windows
        	for (int[] uvw : new int[][]{
    			{5,2,2},
    			{5,2,6},
    			{3,2,6},
    			{1,2,4},
    			{7,2,4},
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	// Various wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0:leftward, 1:rightward, 3:backward, 2:forward; +4:inverted
        		
        		// Roof
        		{0,3,1, 0,3,7, 0}, {8,3,1, 8,3,7, 1},
        		{1,4,1, 1,4,7, 0}, {7,4,1, 7,4,7, 1}, 
        		{2,5,1, 2,5,7, 0}, {6,5,1, 6,5,7, 1}, 
        		{3,6,1, 3,6,7, 0}, {5,6,1, 5,6,7, 1},
        		// Trim
        		{1,3,1, 1,3,1, 5}, {7,3,1, 7,3,1, 4},
        		{2,4,1, 2,4,1, 5}, {6,4,1, 6,4,1, 4},
        		{3,5,1, 3,5,1, 5}, {5,5,1, 5,5,1, 4},
        		{1,3,7, 1,3,7, 5}, {7,3,7, 7,3,7, 4},
        		{2,4,7, 2,4,7, 5}, {6,4,7, 6,4,7, 4},
        		{3,5,7, 3,5,7, 5}, {5,5,7, 5,5,7, 4},
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);
            }

            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{4,1,0, 7,1,0}, {7,1,1, 7,1,1}, 
            })
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);
            }
            
        	// Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright; 
            	// Porch
            	{4,2,0,-1}, {7,2,0,-1},
            	{4,4,3,0}, {4,4,5,2}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	// Interior
        	
            // Clay
            for(int[] uvw : new int[][]{
            	{2,1,5}, 
            	{3,1,5}, 
            	{2,2,5}, 
            	{2,1,4}, 
            	})
            {
            	this.setBlockState(world, Blocks.clay.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);	
            }
            // Terracotta
            for(int[] uvw : new int[][]{
            	{4,1,5}, 
            	{2,3,5}, 
            	})
            {
            	this.setBlockState(world, Blocks.hardened_clay.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);	
            }
        	
            // Stone Cutter
            // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        	IBlockState stonecutterState = ModObjects.chooseModStonecutterState(3);
            this.setBlockState(world, stonecutterState, 6, 1, 4, structureBB);
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 1, 2, 2, 1, 1},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            // Front stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward; +4:inverted
        		{3,0,0, 3}, 
        		})
            {
        		this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[3]%4)+(uuvvwwo[3]/4)*4), uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], structureBB);
            }
            
            
        	// Flower planter out front
        	this.setBlockState(world, biomeGrassState, 2, 0, 1, structureBB); // Sod
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0 ? 6 : 4), 1, 0, 1, structureBB); // Left
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{4, 7, 5, 6})[this.coordBaseMode.getHorizontalIndex()]), 2, 0, 0, structureBB); // Front
        	
        	// Flowers on top
    		int flowerindex = random.nextInt(10);
    		// 0-8 is "red" flower
    		// 9 is a basic yellow flower
    		// 10-11 are the flowers from UpToDateMod
    		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : Blocks.red_flower;
    		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
    		
    		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), 2, 1, 1, structureBB);
        	
    		
        	
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
            	
            	// Villager
            	int u = 3+random.nextInt(3);
            	int v = 1;
            	int w = 3+random.nextInt(2);
            	            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    
    // --- Plains Medium House 1 --- //
    
    public static class PlainsMediumHouse1 extends StructureVillagePieces.Village
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
        		"           ",
        		"   FFFFFFF ",
        		"   FFFFFFF ",
        		"   FFFFFFF ",
        		"   FFFFFFF ",
        		"   FFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		" FFFFFFFFF ",
        		"   FF      ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 4;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsMediumHouse1() {}
        
        public PlainsMediumHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static PlainsMediumHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsMediumHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
        	
        	// TODO - stripped wood
        	// For stripped logs specifically
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
        	IBlockState biomeStrippedLogVertState = biomeLogVertState;
        	IBlockState biomeStrippedLogHorAlongState = biomeLogHorAlongState;
        	IBlockState biomeStrippedLogHorAcrossState = biomeLogHorAcrossState;
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Try to see if stripped logs exist
        	if (biomeStrippedLogVertState.getBlock()==Blocks.log || biomeStrippedLogVertState.getBlock()==Blocks.log2)
        	{
            	if (biomeLogVertState.getBlock() == Blocks.log)
            	{
            		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 0);
            		biomeStrippedLogHorAlongState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1+(this.coordBaseMode.getHorizontalIndex()%2==0? 0:1));
            		biomeStrippedLogHorAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState), 1+(this.coordBaseMode.getHorizontalIndex()%2==0? 1:0));
            	}
            	else if (biomeLogVertState.getBlock() == Blocks.log2)
            	{
            		biomeStrippedLogVertState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 0);
            		biomeStrippedLogHorAlongState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 1+(this.coordBaseMode.getHorizontalIndex()%2==0? 0:1));
            		biomeStrippedLogHorAcrossState = ModObjects.chooseModStrippedLogState(biomeLogVertState.getBlock().getMetaFromState(biomeLogVertState)+4, 1+(this.coordBaseMode.getHorizontalIndex()%2==0? 1:0));
            	}
        	}
        	
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
            
            
            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,1, 8,2,1},
            	// Left wall
            	{1,1,2, 1,2,5},
            	{1,4,2, 1,4,5},
            	{1,5,3, 1,5,4},
            	{1,1,6, 3,2,6},
            	{3,1,7, 3,2,10},
            	{3,4,7, 3,4,10},
            	// Back wall
            	{4,1,11, 8,4,11},
            	{6,6,11, 6,6,11},
            	// Right wall
            	{9,1,2, 9,2,10},
            	{9,4,2, 9,4,10},
            	{9,5,3, 9,5,4},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Embedded into the floor
            	{2,1,2, 8,1,5},
            	{4,1,6, 8,1,10},
            	// Wall
            	{1,3,6, 3,4,6},
            	// Roof
            	{0,4,1, 10,4,1},
            	{0,5,2, 10,5,2},
            	{0,6,3, 10,6,3},
            	{0,6,4, 10,6,4},
            	{0,5,5, 4,5,5},
            	{4,5,5, 4,5,12},
            	{5,6,5, 5,6,12},
            	{6,7,5, 6,7,12},
            	{7,6,5, 7,6,12},
            	{8,5,3, 8,5,12},
            	
            	// Roof touchup
            	// Back
            	{3,4,12, 3,4,12},
            	{9,4,12, 9,4,12},
            	// Top
            	{5,7,4, 7,7,4},
            	{4,6,5, 4,6,5},
            	{3,5,6, 3,5,6},
            	{2,4,7, 2,4,7},
            	{8,6,5, 8,6,5},
            	// Left
            	{0,4,6, 0,4,6},
            	{10,4,6, 10,4,6},
            	// Right
            	{9,5,5, 10,5,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Stripped logs
            // Vertical
            for(int[] uuvvww : new int[][]{
            	{1,1,1, 1,4,1},
            	{9,1,1, 9,4,1},
            	{1,1,6, 1,4,6},
            	{3,1,11, 3,4,11},
            	{9,1,11, 9,4,11},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeStrippedLogVertState, biomeStrippedLogVertState, false);	
            }
            // Stripped log across
        	for (int[] uvw : new int[][]{
    			{9,3,6},
        		})
            {
        		this.setBlockState(world, biomeStrippedLogHorAcrossState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            // Stripped log along
        	for (int[] uvw : new int[][]{
    			{2,3,1},
    			{4,3,1},
    			{8,3,1},
        		})
            {
        		this.setBlockState(world, biomeStrippedLogHorAlongState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Logs
            // Window frames
        	for (int[] uvw : new int[][]{
    			{1,3,2}, {1,3,5},
    			{9,3,2}, {9,3,5},
    			{9,3,7}, {9,3,10},
    			{3,3,7}, {3,3,10},
    			{5,3,1}, {7,3,1},
    			{5,5,11}, {7,5,11},
    			})
	        {
	    		this.setBlockState(world, biomeLogVertState, uvw[0], uvw[1], uvw[2], structureBB);
	        }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
    			{1,3,3}, {1,3,4},
    			{9,3,3}, {9,3,4},
    			{9,3,8}, {9,3,9},
    			{3,3,8}, {3,3,9},
    			{6,3,1},
    			{6,5,11},
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
        	// Various wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0:leftward, 1:rightward, 3:backward, 2:forward; +4:inverted
        		
        		// Roof
        		// Front
        		{0,4,0, 10,4,0, 3},
        		{0,5,1, 10,5,1, 3},
        		{0,6,2, 10,6,2, 3},
        		{0,7,3, 10,7,3, 3},
        		//Back
        		{0,4,7, 1,4,7, 2},
        		{0,5,6, 2,5,6, 2},
        		{0,6,5, 3,6,5, 2},
        		{0,7,4, 4,7,4, 2},
        		{10,4,7, 10,4,7, 2},
        		{9,5,6, 10,5,6, 2},
        		{9,6,5, 10,6,5, 2},
        		{8,7,4, 10,7,4, 2},
        		// Left
        		{2,4,8, 2,4,12, 0},
        		{3,5,7, 3,5,12, 0},
        		{4,6,6, 4,6,12, 0},
        		{5,7,5, 5,7,12, 0},
        		{10,4,8, 10,4,12, 1},
        		{9,5,7, 9,5,12, 1},
        		{8,6,6, 8,6,12, 1},
        		{7,7,5, 7,7,12, 1},
            	// Benches
            	{5,2,2, 5,2,2, 1},
            	{7,2,2, 7,2,2, 0},
            	// Front steps
            	{3,1,0, 3,1,0, 3},
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);
            }
        	
        	
            // Tables: fence with pressure plate
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_pressure_plate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
    			{6,2,2},
    			{8,2,6},
    			{8,2,10},
    			{4,2,10},
        		})
            {
        		this.setBlockState(world, biomeFenceState, uvw[0], uvw[1], uvw[2], structureBB);
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1]+1, uvw[2], structureBB);
            }
            
            
        	// Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright; 
            	// Front
            	{2,3,0,2}, {4,3,0,2},
            	// Back
            	{6,3,12,0},
            	// Interior
            	{3,4,2,0}, {8,4,6,3}, {6,3,10,2},
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	// Interior
            
            // Beds
            for (int[] uvwm : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{5,2,9,2},
            	{7,2,9,2},
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwm[3];
            		int u = uvwm[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwm[1];
            		int w = uvwm[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor2 : 0); // Goes by wool meta	
            	}
            }
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 2, 1, 2, 1, 1},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            // Grass block is supposed to be under front stairs, but that's underneath stairs so I'm moving it to the side
            for (int[] uvw : new int[][]{
    			{4,0,0},
        		})
            {
        		this.setBlockState(world, biomeGrassState, uvw[0], uvw[1], uvw[2], structureBB);
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
        	
        	

            // Decor
            for (int decorUVW[] : new int[][]{
            	{0, GROUND_LEVEL, 10},
            	})
            {
                // Add foundations
            	this.setBlockState(world, biomeDirtState, decorUVW[0], decorUVW[1]-1, decorUVW[2], structureBB);
        		this.replaceAirAndLiquidDownwards(world, biomeFillerState, decorUVW[0], decorUVW[1]-2, decorUVW[2], structureBB);
        		
                // The actual decor
        		for (int j=0; j<decorUVW.length; j++)
                {
        			int decorHeightY = decorUVW[1];
        			
        			// Set random seed
                	Random randomFromXYZ = new Random();
                	randomFromXYZ.setSeed(
        					world.getSeed() +
        					FunctionsVN.getUniqueLongForXYZ(
        							this.getXWithOffset(decorUVW[0], decorUVW[2]),
        							this.getYWithOffset(decorHeightY),
        							this.getZWithOffset(decorUVW[0], decorUVW[2])
        							)
                			);
                	
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
                	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
                	
                	for (BlueprintData b : decorBlueprint)
                	{
                		// Place block indicated by blueprint
                		this.setBlockState(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos(), decorUVW[2]+b.getWPos(), structureBB);
                		
                		// Fill below if flagged
                		if ((b.getfillFlag()&1)!=0)
                		{
                			this.replaceAirAndLiquidDownwards(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()-1, decorUVW[2]+b.getWPos(), structureBB);
                		}
                		
                		// Clear above if flagged
                		if ((b.getfillFlag()&2)!=0)
                		{
                			this.clearCurrentPositionBlocksUpwards(world, decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()+1, decorUVW[2]+b.getWPos(), structureBB);
                		}            		
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
	        			{7,2,4, -1, 0},
	        			{6,2,6, -1, 0},
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
    
    
    
    // --- Plains Medium House 2 --- //
    
    public static class PlainsMediumHouse2 extends StructureVillagePieces.Village
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
        		"             ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsMediumHouse2() {}
        
        public PlainsMediumHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static PlainsMediumHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsMediumHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            
            
            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,0,1, 4,3,1}, {8,0,1, 10,3,1}, {6,3,1, 6,3,1},
            	// Back wall
            	{2,0,5, 4,3,5}, {6,0,5, 6,3,5}, {8,0,5, 10,3,5},
            	// Left
            	{1,0,2, 1,3,4}, 
            	// Right
            	{11,0,2, 11,3,4},
            	// Floor
            	{6,0,1, 6,0,4}, {3,0,3, 4,0,3}, {8,0,3, 9,0,3},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Embedded into the floor
            	{2,0,2, 2,0,4}, {3,0,2, 4,0,2}, {3,0,4, 4,0,4}, {5,0,2, 5,0,4},
            	{7,0,2, 7,0,4}, {8,0,2, 9,0,2}, {8,0,4, 9,0,4}, {10,0,2, 10,0,4},
            	// Double roof
            	{3,5,0, 3,5,6}, {9,5,0, 9,5,6},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            // Wooden slabs and double slabs
        	IBlockState biomeWoodDoubleSlabState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.double_wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.fillWithBlocks(world, structureBB, 6, 4, 0, 6, 4, 6, biomeWoodDoubleSlabState, biomeWoodDoubleSlabState, false);
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
    			{6,3,0},
    			{6,3,6},
        		})
            {
        		this.setBlockState(world, biomeWoodSlabTopState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Logs
        	// Vertical
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,0,1, 1,3,1}, {5,0,1, 5,3,1}, {7,0,1, 7,3,1}, {11,0,1, 11,3,1},
            	{1,0,5, 1,3,5}, {5,0,5, 5,3,5}, {7,0,5, 7,3,5}, {11,0,5, 11,3,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
        	// Across
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	{2,4,1, 4,4,1}, {8,4,1, 10,4,1},
            	{2,4,5, 4,4,5}, {8,4,5, 10,4,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
        	
        	// Various wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0:leftward, 1:rightward, 3:backward, 2:forward; +4:inverted
        		
        		// Roof
        		{0,3,0, 0,3,6, 0},
        		{1,4,0, 1,4,6, 0},
        		{2,5,0, 2,5,6, 0},
        		
        		{5,4,0, 5,4,6, 1},
        		{4,5,0, 4,5,6, 1},
        		
        		{7,4,0, 7,4,6, 0},
        		{8,5,0, 8,5,6, 0},
        		
        		{12,3,0, 12,3,6, 1},
        		{11,4,0, 11,4,6, 1},
        		{10,5,0, 10,5,6, 1},
        		
        		// Trim
        		{1,3,0, 1,3,0, 5},
        		{2,4,0, 2,4,0, 5},
        		{4,4,0, 4,4,0, 4},
        		{8,4,0, 8,4,0, 5},
        		{10,4,0, 10,4,0, 4},
        		{11,3,0, 11,3,0, 4},
        		{1,3,6, 1,3,6, 5},
        		{2,4,6, 2,4,6, 5},
        		{4,4,6, 4,4,6, 4},
        		{8,4,6, 8,4,6, 5},
        		{10,4,6, 10,4,6, 4},
        		{11,3,6, 11,3,6, 4},
        		
        		// A single bench
        		{2,1,4, 2,1,4, 1},
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);
            }
        	
            
            // Windows
        	for (int[] uvw : new int[][]{
    			{3,2,1}, {9,2,1},
    			{3,2,5}, {9,2,5},
    			{1,2,3}, {11,2,3},
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Tables: fence with pressure plate
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_pressure_plate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
    			{3,1,4},
        		})
            {
        		this.setBlockState(world, biomeFenceState, uvw[0], uvw[1], uvw[2], structureBB);
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1]+1, uvw[2], structureBB);
            }
            
            
        	// Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright; 
            	// Front
            	{5,2,0,2}, {7,2,0,2},
            	// Back
            	{6,2,6,0},
            	// Interior
            	{5,2,4,2}, {7,2,4,2},
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	// Interior
            
            // Beds
            for (int[] uvwm : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,1,2,1},
            	{9,1,4,3},
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwm[3];
            		int u = uvwm[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwm[1];
            		int w = uvwm[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor : 4); // Goes by wool meta	
            	}
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{6, 1, 1, 2, 1, 0},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
        	
            
            // Front stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward; +4:inverted
        		// Left
        		{6,0,0, 3}, 
        		})
            {
        		this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[3]%4)+(uuvvwwo[3]/4)*4), uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], structureBB);
            }
        	
            
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 10;
        	int chestV = 1;
        	int chestW = 2;
        	int chestO = 3; // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
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
	        			{4,1,3, -1, 0},
	        			{8,1,3, -1, 0},
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
    
    
    
    // --- Plains Large Market (building) --- //
    
    public static class PlainsMeetingPoint4 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFFFFFFFF",
        		"FFFFFPFPFFPFFPFF",
        		"FFPPPFFFFFFFPFFP",
        		"FFFFFPFFPFPFFPPF",
        		"FFFFPPPPFPFPFFFF",
        		"FFFPFFPFFFFFFFFF",
        		"FFFFPFPPPFFFPFPF",
        		"FFFFPPPFFPFPFFFF",
        		"FFPFFFPFPFPFPFFF",
        		"FPFFFPFPFFPPFFFF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsMeetingPoint4() {}
        
        public PlainsMeetingPoint4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static PlainsMeetingPoint4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsMeetingPoint4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            
            
            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uw : new int[][]{
            	{3, 3}, {3, 6}, 
            	{4, 1}, {4, 4}, 
            	{5, 4},
            	{6, 6},
            	{7, 2},
            	{8, 0}, {8, 4}, {8, 5},
            	{10, 2}, {10, 4}, {10, 7},
            	{11, 3},
            	{12, 4}, {12, 6},
            	{13, 6},
            	})
            {
            	this.setBlockState(world, biomeCobblestoneState, uw[0], GROUND_LEVEL-1, uw[1], structureBB);	
            }
            // Mossy cobblestone
        	IBlockState biomeMossyCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.mossy_cobblestone.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            this.setBlockState(world, biomeMossyCobblestoneState, 13, GROUND_LEVEL-1, 1, structureBB);
            
            // Dirt
            for(int[] uuww : new int[][]{
            	{2,0,2, 2,0,5}, 
            	{13,0,2, 13,0,5}, 
            	})
            {
                this.fillWithBlocks(world, structureBB, uuww[0], uuww[1], uuww[2], uuww[3], uuww[4], uuww[5], biomeDirtState, biomeDirtState, false);
            }
            
            
            // Tall grass
            for (int[] uw : new int[][]{
            	{0,0}, {0,2}, {0,5}, {0,7}, 
            	{1,3}, {1,4}, {1,9}, 
            	{3,0}, {3,9}, 
            	{4,6}, {4,8}, {4,9}, 
            	{5,7}, 
            	{6,8}, {6,9}, 
            	{7,9}, 
            	{8,7}, {8,8}, 
            	{11,8}, 
            	{12,0}, {12,9}, 
            	{13,0}, 
            	{14,1}, {14,4}, {14,9}, 
            	{15,2}, {15,3}, {15,9}, 
            })
            {
            	this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), uw[0], GROUND_LEVEL, uw[1], structureBB);
            }
            
            // Double-tall grass
            for (int[] uw : new int[][]{
            	{0,8}, 
            	{2,6}, 
            	{5,9}, 
            	{15,0}, {15,1}, 
            })
            {
				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(2), uw[0], GROUND_LEVEL, uw[1], structureBB);
				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uw[0], GROUND_LEVEL+1, uw[1], structureBB);
            }
            
            
            // Yellow flowers
            for (int[] uw : new int[][]{
            	{7,6}, {14,0}, 
            })
            {
            	this.setBlockState(world, Blocks.yellow_flower.getStateFromMeta(0), uw[0], GROUND_LEVEL, uw[1], structureBB);
            }
            
            
        	// Tree
        	IBlockState biomeSaplingState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.sapling.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwss : new int[][]{
        		// u,v,w, ushift,wshift: Which adjacent spaces to use if this is a Dark Oak sapling
        		{9,1,7, -1,-1},
        		})
            {
        		Block dirtblock = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]-1), this.getZWithOffset(uvwss[0], uvwss[2]))).getBlock();
        		Block saplingblock = world.getBlockState(new BlockPos(this.getXWithOffset(uvwss[0], uvwss[2]), this.getYWithOffset(uvwss[1]), this.getZWithOffset(uvwss[0], uvwss[2]))).getBlock();

        		// Don't place if there's no dirt beneath to grow
        		if (dirtblock==null || (dirtblock != Blocks.dirt && dirtblock != Blocks.grass)) {continue;}
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
        					(dirtblock1 != Blocks.dirt && dirtblock1 != Blocks.grass)
        					|| (dirtblock2 != Blocks.dirt && dirtblock2 != Blocks.grass)
        					|| (dirtblock3 != Blocks.dirt && dirtblock3 != Blocks.grass)
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
        	
            
            // Decor
            for (int decorUVW[] : new int[][]{
            	{1, GROUND_LEVEL, 1},
            	{2, GROUND_LEVEL, 8},
            	{13, GROUND_LEVEL, 7},
            	})
            {
                // Add foundations
            	this.setBlockState(world, biomeDirtState, decorUVW[0], decorUVW[1]-1, decorUVW[2], structureBB);
        		this.replaceAirAndLiquidDownwards(world, biomeFillerState, decorUVW[0], decorUVW[1]-2, decorUVW[2], structureBB);
        		
                // The actual decor
        		for (int j=0; j<decorUVW.length; j++)
                {
        			int decorHeightY = decorUVW[1];
        			
        			// Set random seed
                	Random randomFromXYZ = new Random();
                	randomFromXYZ.setSeed(
        					world.getSeed() +
        					FunctionsVN.getUniqueLongForXYZ(
        							this.getXWithOffset(decorUVW[0], decorUVW[2]),
        							this.getYWithOffset(decorHeightY),
        							this.getZWithOffset(decorUVW[0], decorUVW[2])
        							)
                			);
                	
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
                	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
                	
                	for (BlueprintData b : decorBlueprint)
                	{
                		// Place block indicated by blueprint
                		this.setBlockState(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos(), decorUVW[2]+b.getWPos(), structureBB);
                		
                		// Fill below if flagged
                		if ((b.getfillFlag()&1)!=0)
                		{
                			this.replaceAirAndLiquidDownwards(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()-1, decorUVW[2]+b.getWPos(), structureBB);
                		}
                		
                		// Clear above if flagged
                		if ((b.getfillFlag()&2)!=0)
                		{
                			this.clearCurrentPositionBlocksUpwards(world, decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()+1, decorUVW[2]+b.getWPos(), structureBB);
                		}            		
                	}
                }
            }

            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Stalls
            	{1,1,2, 1,3,2}, {3,1,2, 3,3,2}, {1,1,5, 1,3,5}, {3,1,5, 3,3,5}, 
            	{12,1,2, 12,3,2}, {14,1,2, 14,3,2}, {12,1,5, 12,3,5}, {14,1,5, 14,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Stalls
            	{2,1,2, 2,1,5}, 
            	{13,1,2, 13,1,5},
            	// Bell post
            	{6,1,7, 6,1,7},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            // Wooden slabs
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left stall
            	{1,4,2, 3,4,2}, {1,4,5, 3,4,5},
            	// Right stall
            	{12,4,2, 14,4,2}, {12,4,5, 14,4,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
            }
            
            
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signU = 6;
    			int signV = 2;
    			int signW = 7;
                int signX = this.getXWithOffset(signU, signW);
                int signY = this.getYWithOffset(signV);
                int signZ = this.getZWithOffset(signU, signW);
        		
                TileEntitySign signContents;
                
                if (this.namePrefix.equals("") && this.nameRoot.equals("") && this.nameSuffix.equals(""))
                {
                	// Something has gone wrong and we can't obtain the name. Substitute nonsense instead.
                	signContents = new TileEntitySign();
                	signContents.signText[1] = new ChatComponentText("Market");
                }
                else
                {
                	signContents = StructureVillageVN.generateSignContents(this.namePrefix, this.nameRoot, this.nameSuffix);
                }
                
            	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
    			world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
        		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
            }
            
            
            
        	// Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright; 
            	// Left stall
            	{2,2,2,-1}, {2,2,5,-1}, 
            	// Right stall
            	{13,2,2,-1}, {13,2,5,-1}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	

            // Wool Awnings
            // Yellow
            for (int[] uvw : new int[][]{
            	{1,4,4}, {2,4,3}, {3,4,4}, 
            	{12,4,3}, {13,4,4}, {14,4,3}, 
            })
            {
            	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 4), uvw[0], uvw[1], uvw[2], structureBB);
            }
            // White
            for (int[] uvw : new int[][]{
            	{1,4,3}, {2,4,4}, {3,4,3}, 
            	{12,4,4}, {13,4,3}, {14,4,4}, 
            })
            {
            	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor2 : 0), uvw[0], uvw[1], uvw[2], structureBB);
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
    
    
    
    // --- Plains Small Market --- //
    
    public static class PlainsMeetingPoint5 extends StructureVillagePieces.Village
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
        		"FFFFFFFFFFF",
        		"FFFFFFFFFFF",
        		"FFFFFFFFPFP",
        		"FFPFFPFPPPF",
        		"FFFPPPFPFPF",
        		"FFFFPPPPFFF",
        		"FFPPPFPPPFF",
        		"FFFPPPPPFFF",
        		"FFFFPPFPPFF",
        		"FPFPFPPPFPF",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsMeetingPoint5() {}
        
        public PlainsMeetingPoint5(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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
        
        public static PlainsMeetingPoint5 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsMeetingPoint5(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            for(int[] uuww : new int[][]{
            	{1,0,2, 1,0,4}, 
            	{4,0,7, 6,0,7}, 
            	{9,0,2, 9,0,4}, 
            	})
            {
                this.fillWithBlocks(world, structureBB, uuww[0], uuww[1], uuww[2], uuww[3], uuww[4], uuww[5], biomeDirtState, biomeDirtState, false);
            }
            
            
            // Tall grass
            for (int[] uw : new int[][]{
            	{0,0}, {0,6}, {0,9}, 
            	{1,1}, {1,5}, {1,6}, {1,7}, {1,8}, 
            	{2,0}, {2,5}, 
            	{3,8}, {3,9}, 
            	{6,9},
            	{7,0}, {7,5}, 
            	{8,1}, {8,7}, {8,9}, 
            	{9,1}, 
            })
            {
            	this.setBlockState(world, Blocks.tallgrass.getStateFromMeta(1), uw[0], GROUND_LEVEL, uw[1], structureBB);
            }
            
            // Double-tall grass
            for (int[] uw : new int[][]{
            	{5,8},
            	{7,8}, 
            })
            {
				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(2), uw[0], GROUND_LEVEL, uw[1], structureBB);
				this.setBlockState(world, Blocks.double_plant.getStateFromMeta(11), uw[0], GROUND_LEVEL+1, uw[1], structureBB);
            }
            
        	
            
            // Decor
            for (int decorUVW[] : new int[][]{
            	{2, GROUND_LEVEL, 7},
            	{8, GROUND_LEVEL, 8},
            	})
            {
                // Add foundations
            	this.setBlockState(world, biomeDirtState, decorUVW[0], decorUVW[1]-1, decorUVW[2], structureBB);
        		this.replaceAirAndLiquidDownwards(world, biomeFillerState, decorUVW[0], decorUVW[1]-2, decorUVW[2], structureBB);
        		
                // The actual decor
        		for (int j=0; j<decorUVW.length; j++)
                {
        			int decorHeightY = decorUVW[1];
        			
        			// Set random seed
                	Random randomFromXYZ = new Random();
                	randomFromXYZ.setSeed(
        					world.getSeed() +
        					FunctionsVN.getUniqueLongForXYZ(
        							this.getXWithOffset(decorUVW[0], decorUVW[2]),
        							this.getYWithOffset(decorHeightY),
        							this.getZWithOffset(decorUVW[0], decorUVW[2])
        							)
                			);
                	
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
                	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
                	
                	for (BlueprintData b : decorBlueprint)
                	{
                		// Place block indicated by blueprint
                		this.setBlockState(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos(), decorUVW[2]+b.getWPos(), structureBB);
                		
                		// Fill below if flagged
                		if ((b.getfillFlag()&1)!=0)
                		{
                			this.replaceAirAndLiquidDownwards(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()-1, decorUVW[2]+b.getWPos(), structureBB);
                		}
                		
                		// Clear above if flagged
                		if ((b.getfillFlag()&2)!=0)
                		{
                			this.clearCurrentPositionBlocksUpwards(world, decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()+1, decorUVW[2]+b.getWPos(), structureBB);
                		}            		
                	}
                }
            }

            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Stalls
            	{0,1,2, 0,3,2}, {2,1,2, 2,3,2}, {0,1,4, 0,3,4}, {2,1,4, 2,3,4}, 
            	{8,1,2, 8,3,2}, {10,1,2, 10,3,2}, {8,1,4, 8,3,4}, {10,1,4, 10,3,4}, 
            	{4,1,6, 4,3,6}, {6,1,6, 6,3,6}, {4,1,8, 4,3,8}, {6,1,8, 6,3,8},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Stalls
            	{1,1,2, 1,1,4}, 
            	{9,1,2, 9,1,4},
            	{4,1,7, 6,1,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            // Wooden slabs
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left stall
            	{0,4,2, 2,4,2}, {0,4,4, 2,4,4},
            	// Right stall
            	{8,4,2, 10,4,2}, {8,4,4, 10,4,4}, 
            	// Back stall
            	{4,4,6, 4,4,8}, {6,4,6, 6,4,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
            }
            
            
            // Sign
            if (GeneralConfig.nameSign)
            {
            	int signU = 5;
    			int signV = 2;
    			int signW = 7;
                int signX = this.getXWithOffset(signU, signW);
                int signY = this.getYWithOffset(signV);
                int signZ = this.getZWithOffset(signU, signW);
        		
            	IBlockState biomeStandingSignState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.standing_sign.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
                TileEntitySign signContents;
                if (this.namePrefix.equals("") && this.nameRoot.equals("") && this.nameSuffix.equals(""))
                {
                	// Something has gone wrong and we can't obtain the name. Substitute nonsense instead.
                	//signContents = new TileEntitySign();
                	//signContents.signText[1] = "Market";
                }
                else
                {
                	signContents = StructureVillageVN.generateSignContents(this.namePrefix, this.nameRoot, this.nameSuffix);
                	
                	world.setBlockState(new BlockPos(signX, signY, signZ), biomeStandingSignState.getBlock().getStateFromMeta(StructureVillageVN.getSignRotationMeta(8, this.coordBaseMode.getHorizontalIndex(), false)), 2); // 2 is "send change to clients without block update notification"
            		world.setTileEntity(new BlockPos(signX, signY, signZ), signContents);
                }
            }
            
        	

            // Wool Awnings
            // Yellow
            for (int[] uvw : new int[][]{
            	{0,4,3}, {2,4,3},
            	{8,4,3}, {10,4,3},
            	{5,4,7},
            })
            {
            	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 4), uvw[0], uvw[1], uvw[2], structureBB);
            }
            // White
            for (int[] uvw : new int[][]{
            	{1,4,3}, 
            	{9,4,3}, 
            	{5,4,6}, {5,4,8},
            })
            {
            	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor2 : 0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
        	// Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright; 
            	// Left stall
            	{3,4,3,1},  
            	// Right stall
            	{7,4,3,3}, 
            	// Back stall
            	{5,4,5,2}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
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
    
    
    
    // --- Shepherd's House --- //
    
    public static class PlainsShepherdsHouse1 extends StructureVillagePieces.Village
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
        		" F FFFFFFF   ",
        		"   FFFFFFF   ",
        		"   FFFFFFF   ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		" FFFFFFFFFFF ",
        		"      P      ",
        		"F   FFPF     ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsShepherdsHouse1() {}

        public PlainsShepherdsHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsShepherdsHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsShepherdsHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	
            
            // Log Frame
            // Corners
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uw : new int[][]{
            	{1, 2}, {11, 2}, 
            	{1, 5}, {11, 5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 1, uw[1], uw[0], 3, uw[1], biomeLogVertState, biomeLogVertState, false);
            }
            
            
            // Wood planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,3, 2,0,4}, {6,0,2, 6,0,5}, {10,0,3, 10,0,4}, 
            	// Front wall
            	{2,1,2, 10,3,2}, {5,4,2, 7,4,2}, 
            	// Back wall
            	{2,1,5, 10,3,5}, {5,4,5, 7,4,5}, 
            	// Left wall
            	{1,1,3, 1,3,4},
            	// Right wall
            	{11,1,3, 11,3,4}, 
            	// Roof
            	{0,4,3, 4,4,4}, {8,4,3, 12,4,4}, {6,5,2, 6,5,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Crossbeams
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,2,2, 4,2,2}, {8,2,2, 10,2,2},
            	// Back wall
            	{2,2,5, 4,2,5}, {8,2,5, 10,2,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{3, 2, 2}, {9, 2, 2}, // Front wall
        		{3, 2, 5}, {9, 2, 5}, // Back wall
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	// Various wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Left roof
        		{0,3,1, 3}, {1,3,1, 3}, {2,3,1, 3}, {3,3,1, 3}, 
        		{0,4,2, 3}, {1,4,2, 3}, {2,4,2, 3}, {3,4,2, 3}, 
        		{0,4,5, 2}, {1,4,5, 2}, {2,4,5, 2}, {3,4,5, 2}, 
        		{0,3,6, 2}, {1,3,6, 2}, {2,3,6, 2}, {3,3,6, 2}, 
        		// Right roof
        		{9,3,1, 3}, {10,3,1, 3}, {11,3,1, 3}, {12,3,1, 3}, 
        		{9,4,2, 3}, {10,4,2, 3}, {11,4,2, 3}, {12,4,2, 3}, 
        		{9,4,5, 2}, {10,4,5, 2}, {11,4,5, 2}, {12,4,5, 2}, 
        		{9,3,6, 2}, {10,3,6, 2}, {11,3,6, 2}, {12,3,6, 2}, 
        		// Top roof
        		{5,5,2, 0}, {5,5,3, 0}, {5,5,4, 0}, {5,5,5, 0}, {5,5,6, 0}, 
        		{7,5,2, 1}, {7,5,3, 1}, {7,5,4, 1}, {7,5,5, 1}, {7,5,6, 1}, 
        		{4,4,6, 0}, {4,4,5, 0}, 
        		{8,4,6, 1}, {8,4,5, 1}, 
        		// Trims and touchups
        		{4,3,1, 1}, {4,4,2, 0}, {8,3,1, 0}, {8,4,2, 1}, 
        		{4,3,6, 5}, {5,4,6, 5}, {7,4,6, 4}, {8,3,6, 4}, 
        		// Left trim
        		{0,3,2, 6}, {0,3,5, 7}, 
        		// Right trim
        		{12,3,2, 6}, {12,3,5, 7},
        		// Interior bench
        		{10,1,3, 0}, {10,1,4, 0}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	// Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{5,2,1, 2}, {7,2,1, 2}, // Front door
            	{6,3,6, 0}, // Back porch
            	// Interior
            	{2,3,3, 1}, {2,3,4, 1}, {10,3,3, 3}, {10,3,4, 3}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	// Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{6, 1, 2, 2, 1, 0},
            	{6, 1, 5, 0, 1, 1},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            // Grass out back
            this.fillWithBlocks(world, structureBB, 3, 0, 6, 9, 0, 8, biomeGrassState, biomeGrassState, false);
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front entrance
            	{5,1,0, 5,3,0}, {7,1,0, 7,3,0}, 
            	// Backyard
            	{3,1,6, 3,1,8}, {9,1,6, 9,1,8}, {4,1,8, 8,1,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);	
            }
            
            // Wooden slabs
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{5,4,0, 5,4,1}, {7,4,0, 7,4,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
            }
            
            
            // Wool
            // Yellow
            for (int[] uvw : new int[][]{
            	{6,4,1},
            	{3,0,3}, {4,0,4}, {5,0,3}, 
            	{7,0,4}, {8,0,3}, {9,0,4}, 
            })
            {
            	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor : 4), uvw[0], uvw[1], uvw[2], structureBB);
            }
            // White
            for (int[] uvw : new int[][]{
            	{6,4,0},
            	{3,0,4}, {4,0,3}, {5,0,4}, 
            	{7,0,3}, {8,0,4}, {9,0,3}, 
            })
            {
            	this.setBlockState(world, Blocks.wool.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor2 : 0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // --- Interior --- //
            
            // Loom
        	IBlockState loomState = ModObjects.chooseModLoom();
            for (int i=0; i<2; i++)
            {
            	this.setBlockState(world, loomState, 2, 1, 3+i, structureBB);
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
        	
        	
            // Decor
            for (int decorUVW[] : new int[][]{
            	{0, GROUND_LEVEL, 0},
            	{1, GROUND_LEVEL, 8},
            	})
            {
                // Add foundations
            	this.setBlockState(world, biomeDirtState, decorUVW[0], decorUVW[1]-1, decorUVW[2], structureBB);
        		this.replaceAirAndLiquidDownwards(world, biomeFillerState, decorUVW[0], decorUVW[1]-2, decorUVW[2], structureBB);
        		
                // The actual decor
        		for (int j=0; j<decorUVW.length; j++)
                {
        			int decorHeightY = decorUVW[1];
        			
        			// Set random seed
                	Random randomFromXYZ = new Random();
                	randomFromXYZ.setSeed(
        					world.getSeed() +
        					FunctionsVN.getUniqueLongForXYZ(
        							this.getXWithOffset(decorUVW[0], decorUVW[2]),
        							this.getYWithOffset(decorHeightY),
        							this.getZWithOffset(decorUVW[0], decorUVW[2])
        							)
                			);
                	
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
                	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
                	
                	for (BlueprintData b : decorBlueprint)
                	{
                		// Place block indicated by blueprint
                		this.setBlockState(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos(), decorUVW[2]+b.getWPos(), structureBB);
                		
                		// Fill below if flagged
                		if ((b.getfillFlag()&1)!=0)
                		{
                			this.replaceAirAndLiquidDownwards(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()-1, decorUVW[2]+b.getWPos(), structureBB);
                		}
                		
                		// Clear above if flagged
                		if ((b.getfillFlag()&2)!=0)
                		{
                			this.clearCurrentPositionBlocksUpwards(world, decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()+1, decorUVW[2]+b.getWPos(), structureBB);
                		}            		
                	}
                }
            }
            
			//LogHelper.info("Fletcher house spawned at " + this.getXWithOffset(4, 0) + " " + this.getYWithOffset(1) + " " + this.getZWithOffset(4, 0) + " with horizIndex " + this.coordBaseMode);
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u = 3+random.nextInt(7);
            	int v =  1;
            	int w = 3+random.nextInt(2);
            	
            	EntityVillager entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, 0, 3, 0);
    			
    			entityvillager.setLocationAndAngles((double)this.getXWithOffset(u, w) + 0.5D, (double)this.getYWithOffset(v) + 0.5D, (double)this.getZWithOffset(u, w) + 0.5D, random.nextFloat()*360F, 0.0F);
                world.spawnEntityInWorld(entityvillager);
                
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
    
    
    
    // --- Small Farm --- //
    
    public static class PlainsSmallFarm1 extends StructureVillagePieces.Village
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
    	private static final int STRUCTURE_WIDTH = 9;
    	public static final int STRUCTURE_HEIGHT = 6;
    	private static final int STRUCTURE_DEPTH = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsSmallFarm1() {}

        public PlainsSmallFarm1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsSmallFarm1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsSmallFarm1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Clear above and make foundation
            for (int u = 0; u < STRUCTURE_WIDTH; ++u) {for (int w = 0; w < STRUCTURE_DEPTH; ++w) {
            		
        		this.clearCurrentPositionBlocksUpwards(world, u, GROUND_LEVEL+1, w, structureBB); // Clear above
    			this.replaceAirAndLiquidDownwards(world, biomeFillerState, u, GROUND_LEVEL-1, w, structureBB); // Foundation
    			// Ground-level blocks
            	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
    			if(u==0 || u==(STRUCTURE_WIDTH-1) || w==0 || w==(STRUCTURE_DEPTH-1) )
    			{
    				this.setBlockState(world, biomeLogVertState, u, GROUND_LEVEL, w, structureBB);
    			}
    			else
    			{
    				this.setBlockState(world, Blocks.farmland.getStateFromMeta(7), u, GROUND_LEVEL, w, structureBB);
    			}
            }}
            
            // Water
            this.fillWithBlocks(world, structureBB, 1, 0, 3, STRUCTURE_WIDTH-2, 0, 3, Blocks.flowing_water.getStateFromMeta(0), Blocks.flowing_water.getStateFromMeta(0), false);
            
            
            
            
            

            
            
        	// Pairs of crops are generated together. These are pairs of ages arrays.
        	int[][][] uPairArray = {
    			{{1,2,3,4,5,6,7},
            	 {1,2,3,4,5,6,7}},
    			
    			{{1,2,3,4,5,6,7},
                 {1,2,3,4,5,6,7}},
        	};
        	int[][][] vPairArray = {
       			{{1,1,1,1,1,1,1},
               	 {1,1,1,1,1,1,1}},
       			
       			{{1,1,1,1,1,1,1},
                 {1,1,1,1,1,1,1}},
           	};
        	int[][][] wPairArray = {
       			{{5,5,5,5,5,5,5},
               	 {4,4,4,4,4,4,4}},
       			
       			{{2,2,2,2,2,2,2},
                 {1,1,1,1,1,1,1}},
           	};
        	int[][][] cropPairAgeArray = {
       			{{0,1,0,0,0,2,0},
               	 {1,0,1,0,0,1,1}},
       			
       			{{1,0,0,0,1,0,0},
                 {0,0,0,0,0,0,0}},
           	};
        	// Iterate through the pairs of crops
            for (int crop_pair=0; crop_pair<cropPairAgeArray.length; crop_pair++)
            {
            	Block[] cropBlocks = StructureVillageVN.chooseCropPair(random);
            	
            	// Iterate through the members of a crop pair
            	for (int crop_member=0; crop_member<2; crop_member++)
            	{
            		// Iterate through u positions
            		for (int i=0; i<cropPairAgeArray[crop_pair][crop_member].length; i++)
                	{
            			int cropProgressMeta = cropPairAgeArray[crop_pair][crop_member][i]; // Isolate the crop's age meta value
            			IBlockState cropState;
            			
            			while(true)
            			{
            				try {cropState = cropBlocks[0].getStateFromMeta(cropProgressMeta);}
            				catch (IllegalArgumentException e)
            				{
            					// The assignment failed with a meta of 0. IDK what's happening so just have wheat
            					if (cropProgressMeta==0) {cropState = Blocks.wheat.getStateFromMeta(cropPairAgeArray[crop_pair][crop_member][i]);}
            					// The crop is not allowed to have this value. Cut it in half and try again.
            					else {cropProgressMeta /= 2; continue;}
            				}
            				
            				// Finally, assign the working crop
        					this.setBlockState(world, cropState,
        							uPairArray[crop_pair][crop_member][i],
                					vPairArray[crop_pair][crop_member][i],
                					wPairArray[crop_pair][crop_member][i],
                					structureBB);
            				break;
            			}
                	}
            	}
            }
            
            /*
            // Crops here
            Block[] cropBlocks;
            
            for (int w=0; w<2; w++)
            {
            	cropBlocks = StructureVillageVN.chooseCropPair(random);
            	
            	for (int i=0; i<2; i++)
            	{
            		this.fillWithBlocks(world, structureBB, 1, 1, 3*w +i +1, STRUCTURE_WIDTH-2, 1, 3*w +i +1, cropBlocks[i], 0, cropBlocks[i], 0, false);
            	}
            }
            */
            // Attempt to add GardenCore Compost Bins. If this fails, do nothing
            IBlockState compostBinState = ModObjects.chooseModComposterState();
            if (compostBinState != null)
            {
            	this.setBlockState(world, compostBinState.getBlock().getDefaultState(), STRUCTURE_WIDTH-2, 1, STRUCTURE_DEPTH-2, structureBB);
            	this.setBlockState(world, biomeDirtState, STRUCTURE_WIDTH-2, 0, STRUCTURE_DEPTH-2, structureBB);
            }
            
            
    		// Entities
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u=0;
            	int w=3;
            	int v= 1;
            	
            	while (w==3)
            	{
            		u = random.nextInt(STRUCTURE_WIDTH);
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    
    // --- Small House 1 --- //
    
    public static class PlainsSmallHouse1 extends StructureVillagePieces.Village
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
        		"       ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"       ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsSmallHouse1() {}

        public PlainsSmallHouse1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsSmallHouse1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsSmallHouse1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
        	
        	// TODO - stripped wood
        	// For stripped logs specifically
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
        	
            
            // Log Frame
            // Corners
            for (int[] uw : new int[][]{
            	{1,1}, {5,1},
            	{1,5}, {5,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 0, uw[1], uw[0], 3, uw[1], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
            }
            
            
            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,0,1, 4,3,1}, 
            	// Back wall
            	{2,0,5, 4,3,5}, 
            	// Left
            	{1,0,2, 1,3,4},
            	// Right
            	{5,0,2, 5,3,4},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Embedded into the floor
            	{2,0,2, 4,0,4}, 
            	// Ceiling
            	{1,4,1, 5,4,1}, 
            	{1,4,5, 5,4,5}, 
            	{1,4,2, 1,4,4}, 
            	{5,4,2, 5,4,4},
            	
            	{2,5,2, 4,5,2}, 
            	{2,5,4, 4,5,4}, 
            	{2,5,3, 2,5,3}, 
            	{4,5,3, 4,5,3}, 
            	{3,6,3, 3,6,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{1, 2, 3}, 
        		{5, 2, 3}, 
        		{3, 2, 5}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Roof
        		// Front
        		{1,4,0, 3}, {2,4,0, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, 
        		{2,5,1, 3}, {3,5,1, 3}, {4,5,1, 3}, 
        		{3,6,2, 3},
        		// Back
        		{3,6,4, 2},
        		{2,5,5, 2}, {3,5,5, 2}, {4,5,5, 2}, 
        		{1,4,6, 2}, {2,4,6, 2}, {3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, 
        		// Left
        		{0,4,0, 0}, {0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, {0,4,6, 0}, 
        		{1,5,1, 0}, {1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, {1,5,5, 0}, 
        		{2,6,2, 0}, {2,6,3, 0}, {2,6,4, 0}, 
        		// Right
        		{6,4,0, 1}, {6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, {6,4,6, 1}, 
        		{5,5,1, 1}, {5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, {5,5,5, 1}, 
        		{4,6,2, 1}, {4,6,3, 1}, {4,6,4, 1}, 
        		// Chair
        		{4,1,4, 3}, 
        		
        		// Front entrance
        		{3,0,0, 3},
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{2,2,0,2}, {4,2,0,2}, // Front wall
            	{3,3,4,2}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 1, 1, 2, 1, 0},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // --- Interior --- //
            
            
            // Beds
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3,2},
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwo[3];
            		int u = uvwo[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwo[1];
            		int w = uvwo[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor2 : 0); // Goes by wool meta
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
	        		for (int[] ia : new int[][]{
	        			{3, 1, 3, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], 0); // Adult
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
    
    public static class PlainsSmallHouse2 extends StructureVillagePieces.Village
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
        		"       ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"       ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsSmallHouse2() {}

        public PlainsSmallHouse2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsSmallHouse2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsSmallHouse2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
        	
        	// TODO - stripped wood
        	// For stripped logs specifically
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
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
        	
            
            // Log Frame
            // Corners
            for (int[] uw : new int[][]{
            	{1,1}, {5,1},
            	{1,5}, {5,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 0, uw[1], uw[0], 3, uw[1], biomeStrippedLogVertState, biomeStrippedLogVertState, false);
            }
            
            
            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,0,1, 4,0,1}, 
            	// Back wall
            	{2,0,5, 4,0,5}, 
            	// Left
            	{1,0,2, 1,0,4},
            	// Right
            	{5,0,2, 5,0,4},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Embedded into the floor
            	{2,0,2, 4,0,4}, 
            	// Table
            	{2,1,3, 2,1,3}, 
            	// Ceiling
            	{0,4,0, 6,4,1}, {0,4,5, 6,4,6}, {0,4,2, 1,4,4}, {5,4,2, 6,4,4}, 
            	{1,5,1, 5,5,2}, {1,5,4, 5,5,5}, {1,5,3, 2,5,3}, {4,5,3, 5,5,3}, 
            	{2,6,2, 4,6,4},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Stained terracotta
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,1, 4,3,1}, 
            	// Back wall
            	{2,1,5, 4,3,5}, 
            	// Left
            	{1,1,2, 1,3,4},
            	// Right
            	{5,1,2, 5,3,4},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors? this.townColor2:0), Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors? this.townColor2:0), false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{1, 2, 3}, 
        		{5, 2, 3}, 
        		{3, 2, 5}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Chairs
        		{2,1,2, 2}, {2,1,4, 3}, 
        		
        		// Front entrance
        		{3,0,0, 3},
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,3,0,2}, // Front wall
            	{3,3,4,2}, // Interior
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 1, 1, 2, 1, 0},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // --- Interior --- //
            
            
            // Beds
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{4,1,3,2},
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwo[3];
            		int u = uvwo[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwo[1];
            		int w = uvwo[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor : 4); // Goes by wool meta
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
	        		for (int[] ia : new int[][]{
	        			{3, 1, 3, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], 0); // Adult
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
    
    public static class PlainsSmallHouse3 extends StructureVillagePieces.Village
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
        		"       ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"       ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsSmallHouse3() {}

        public PlainsSmallHouse3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsSmallHouse3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsSmallHouse3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
        	
            
            // Log Frame
            // Corners
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uw : new int[][]{
            	{1,1}, {5,1},
            	{1,5}, {5,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 0, uw[1], uw[0], 3, uw[1], biomeLogVertState, biomeLogVertState, false);
            }

            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,0,1, 4,3,1}, 
            	// Back wall
            	{2,0,5, 4,3,5}, 
            	// Left
            	{1,0,2, 1,3,4},
            	// Right
            	{5,0,2, 5,3,4},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Window frames
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	{1,2,2, 1,2,4}, {5,2,2, 5,2,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            // Window frames
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	{2,2,5, 4,2,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Embedded into the floor
            	{2,0,2, 4,0,4}, 
            	// Ceiling
            	{1,4,1, 5,4,1}, 
            	{1,4,5, 5,4,5}, 
            	{1,4,2, 1,4,4}, 
            	{5,4,2, 5,4,4},
            	
            	{2,5,2, 4,5,2}, 
            	{2,5,4, 4,5,4}, 
            	{2,5,3, 2,5,3}, 
            	{4,5,3, 4,5,3}, 
            	{3,6,3, 3,6,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{1, 2, 3}, 
        		{5, 2, 3}, 
        		{3, 2, 5}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Roof
        		// Front
        		{1,4,0, 3}, {2,4,0, 3}, {3,4,0, 3}, {4,4,0, 3}, {5,4,0, 3}, 
        		{2,5,1, 3}, {3,5,1, 3}, {4,5,1, 3}, 
        		{3,6,2, 3},
        		// Back
        		{3,6,4, 2},
        		{2,5,5, 2}, {3,5,5, 2}, {4,5,5, 2}, 
        		{1,4,6, 2}, {2,4,6, 2}, {3,4,6, 2}, {4,4,6, 2}, {5,4,6, 2}, 
        		// Left
        		{0,4,0, 0}, {0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, {0,4,6, 0}, 
        		{1,5,1, 0}, {1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, {1,5,5, 0}, 
        		{2,6,2, 0}, {2,6,3, 0}, {2,6,4, 0}, 
        		// Right
        		{6,4,0, 1}, {6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, {6,4,6, 1}, 
        		{5,5,1, 1}, {5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, {5,5,5, 1}, 
        		{4,6,2, 1}, {4,6,3, 1}, {4,6,4, 1}, 
        		// Chair
        		{4,1,4, 3}, 
        		
        		// Front entrance
        		{3,0,0, 3},
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,3,0,2}, // Front wall
            	{3,3,4,2}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 1, 1, 2, 1, 0},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // --- Interior --- //
            
            
            // Beds
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3,2},
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwo[3];
            		int u = uvwo[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwo[1];
            		int w = uvwo[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor : 4); // Goes by wool meta
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
	        		for (int[] ia : new int[][]{
	        			{3, 1, 3, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], 0); // Adult
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
    
    public static class PlainsSmallHouse4 extends StructureVillagePieces.Village
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
        		"       ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"       ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsSmallHouse4() {}

        public PlainsSmallHouse4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsSmallHouse4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsSmallHouse4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
        	
            
            // Log Frame
            // Corners
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uw : new int[][]{
            	{1,1}, {5,1},
            	{1,5}, {5,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 0, uw[1], uw[0], 4, uw[1], biomeLogVertState, biomeLogVertState, false);
            }

            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,0,1, 4,3,1}, {2,5,1, 4,5,1}, 
            	// Back wall
            	{2,0,5, 4,3,5}, {2,5,5, 4,5,5}, 
            	// Left
            	{1,0,2, 1,3,4},
            	// Right
            	{5,0,2, 5,3,4},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Window frames
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	{1,2,2, 1,2,4}, {5,2,2, 5,2,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            // Window frames
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	{2,4,1, 4,4,1}, {2,4,5, 4,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Embedded into the floor
            	{2,0,2, 4,0,4}, 
            	// Ceiling
            	{1,4,2, 1,4,4}, {5,4,2, 5,4,4}, 
            	{2,5,2, 2,5,4}, {4,5,2, 4,5,4}, {3,5,2, 3,5,2}, {3,5,4, 3,5,4}, 
            	{3,6,0, 3,6,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{1, 2, 3}, 
        		{5, 2, 3}, 
        		{3, 2, 5}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Roof
        		{0,4,0, 0}, {0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, {0,4,6, 0}, 
        		{1,5,0, 0}, {1,5,1, 0}, {1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, {1,5,5, 0}, {1,5,6, 0}, 
        		{2,6,0, 0}, {2,6,1, 0}, {2,6,2, 0}, {2,6,3, 0}, {2,6,4, 0}, {2,6,5, 0}, {2,6,6, 0}, 
        		{4,6,0, 1}, {4,6,1, 1}, {4,6,2, 1}, {4,6,3, 1}, {4,6,4, 1}, {4,6,5, 1}, {4,6,6, 1}, 
        		{5,5,0, 1}, {5,5,1, 1}, {5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, {5,5,5, 1}, {5,5,6, 1}, 
        		{6,4,0, 1}, {6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, {6,4,6, 1}, 
        		// Roof trim
        		{1,4,0, 5}, {2,5,0, 5}, {4,5,0, 4}, {5,4,0, 4}, 
        		{1,4,6, 5}, {2,5,6, 5}, {4,5,6, 4}, {5,4,6, 4}, 
        		
        		// Front entrance
        		{3,0,0, 3},
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Bench
        		{4,1,2, 0}, {4,1,3, 0}, {4,1,4, 0}, 
        		})
            {
        		this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,3,0,2}, // Front wall
            	{3,3,4,2}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 1, 1, 2, 1, 0},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // --- Interior --- //
            
            
            // Beds
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3,2},
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwo[3];
            		int u = uvwo[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwo[1];
            		int w = uvwo[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor2 : 0); // Goes by wool meta
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
	        		for (int[] ia : new int[][]{
	        			{3, 1, 3, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], 0); // Adult
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
    
    public static class PlainsSmallHouse5 extends StructureVillagePieces.Village
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
        		"   F     ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		" FFFFFFF ",
        		"  FFFFF  ",
        		"   FFF F ",
        		" F  P    ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 11;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsSmallHouse5() {}

        public PlainsSmallHouse5(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsSmallHouse5 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsSmallHouse5(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
        	
            
        	// Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uw : new int[][]{
            	// Corner posts
            	{1,1,3, 1,7,3}, {7,1,3, 7,7,3}, 
            	{1,1,7, 1,7,7}, {7,1,7, 7,7,7}, 
            	// Window frames
            	{3,3,7, 3,3,7}, {5,3,7, 5,3,7}, 
            	{3,6,7, 3,6,7}, {5,6,7, 5,6,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogVertState, biomeLogVertState, false);
            }
            

            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Left
            	{1,1,4, 1,1,6}, 
            	// Right
            	{7,1,4, 7,1,6}, {2,1,7, 6,1,7}, 
            	// Front
            	{2,1,2, 2,4,2}, {6,1,2, 6,4,2}, {3,1,1, 5,4,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
        	
            // Decor
            for (int decorUVW[] : new int[][]{
            	{1, 1, 0},
            	{3, 1, 8},
            	{7, 1, 1},
            	})
            {
                // Add foundations
            	this.setBlockState(world, biomeDirtState, decorUVW[0], decorUVW[1]-1, decorUVW[2], structureBB);
        		this.replaceAirAndLiquidDownwards(world, biomeFillerState, decorUVW[0], decorUVW[1]-2, decorUVW[2], structureBB);
        		
                // The actual decor
        		for (int j=0; j<decorUVW.length; j++)
                {
        			int decorHeightY = decorUVW[1];
        			
        			// Set random seed
                	Random randomFromXYZ = new Random();
                	randomFromXYZ.setSeed(
        					world.getSeed() +
        					FunctionsVN.getUniqueLongForXYZ(
        							this.getXWithOffset(decorUVW[0], decorUVW[2]),
        							this.getYWithOffset(decorHeightY),
        							this.getZWithOffset(decorUVW[0], decorUVW[2])
        							)
                			);
                	
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
                	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
                	
                	for (BlueprintData b : decorBlueprint)
                	{
                		// Place block indicated by blueprint
                		this.setBlockState(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos(), decorUVW[2]+b.getWPos(), structureBB);
                		
                		// Fill below if flagged
                		if ((b.getfillFlag()&1)!=0)
                		{
                			this.replaceAirAndLiquidDownwards(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()-1, decorUVW[2]+b.getWPos(), structureBB);
                		}
                		
                		// Clear above if flagged
                		if ((b.getfillFlag()&2)!=0)
                		{
                			this.clearCurrentPositionBlocksUpwards(world, decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()+1, decorUVW[2]+b.getWPos(), structureBB);
                		}            		
                	}
                }
            }
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Embedded into the floor
            	{3,1,2, 5,1,2}, {2,1,3, 6,1,6}, 
            	// Left wall
            	{1,2,4, 1,4,6}, {1,6,4, 1,7,6}, 
            	// Right wall
            	{7,2,4, 7,4,6}, {7,6,4, 7,7,6}, 
            	// Front wall
            	{2,4,3, 6,7,3}, {3,9,3, 5,9,3}, 
            	// Back wall
            	{2,2,7, 6,7,7}, {3,9,7, 5,9,7}, 
            	// Second floor
            	{3,4,2, 5,4,2}, {3,4,4, 6,4,4}, 
            	// Ceiling
            	{2,8,4, 2,8,6}, {3,9,4, 3,9,6}, {5,9,4, 5,9,6}, {6,8,4, 6,8,6}, 
            	{4,10,2, 4,10,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            // Along logs
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	{1,5,4, 1,5,6}, {7,5,4, 7,5,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            // Across logs
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	{2,8,3, 6,8,3}, 
            	{2,8,7, 6,8,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{1, 3, 5}, 
        		{7, 3, 5}, 
        		{4, 3, 7}, {4, 6, 7}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Roof
        		{0,7,2, 0}, {0,7,3, 0}, {0,7,4, 0}, {0,7,5, 0}, {0,7,6, 0}, {0,7,7, 0}, {0,7,8, 0}, 
        		{1,8,2, 0}, {1,8,3, 0}, {1,8,4, 0}, {1,8,5, 0}, {1,8,6, 0}, {1,8,7, 0}, {1,8,8, 0}, 
        		{2,9,2, 0}, {2,9,3, 0}, {2,9,4, 0}, {2,9,5, 0}, {2,9,6, 0}, {2,9,7, 0}, {2,9,8, 0}, 
        		{3,10,2, 0}, {3,10,3, 0}, {3,10,4, 0}, {3,10,5, 0}, {3,10,6, 0}, {3,10,7, 0}, {3,10,8, 0}, 
        		{5,10,2, 1}, {5,10,3, 1}, {5,10,4, 1}, {5,10,5, 1}, {5,10,6, 1}, {5,10,7, 1}, {5,10,8, 1}, 
        		{6,9,2, 1}, {6,9,3, 1}, {6,9,4, 1}, {6,9,5, 1}, {6,9,6, 1}, {6,9,7, 1}, {6,9,8, 1}, 
        		{7,8,2, 1}, {7,8,3, 1}, {7,8,4, 1}, {7,8,5, 1}, {7,8,6, 1}, {7,8,7, 1}, {7,8,8, 1}, 
        		{8,7,2, 1}, {8,7,3, 1}, {8,7,4, 1}, {8,7,5, 1}, {8,7,6, 1}, {8,7,7, 1}, {8,7,8, 1}, 
        		// Roof trim
        		{1,7,2, 5}, {2,8,2, 5}, {3,9,2, 5}, {5,9,2, 4}, {6,8,2, 4}, {7,7,2, 4}, 
        		{1,7,8, 5}, {2,8,8, 5}, {3,9,8, 5}, {5,9,8, 4}, {6,8,8, 4}, {7,7,8, 4}, 
        		// Bench
        		{5,2,6, 3}, {6,2,6, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front entrance
        		{4,1,0, 3}, 
        		})
            {
        		this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front
            	{4,4,0,2}, 
            	// Back
            	{5,3,8,0}, {3,6,8,0}, {5,6,8,0}, //{3,3,8,0}, // Last one interferes with decor post 
            	// Interior
            	{3,3,2,0}, {5,3,2,0}, 
            	{4,7,6,2}, {4,7,4,0}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{4, 2, 1, 2, 1, 1}, 
            	{3, 5, 3, 2, 1, 1}, {5, 5, 3, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Stalls
            	{2,5,2, 2,5,2}, {3,5,1, 5,5,1}, {6,5,2, 6,5,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);	
            }
            
            
            // --- Interior --- //
            
            
            // Carpet
            this.fillWithBlocks(world, structureBB, 4, 2, 4, 5, 2, 4, Blocks.carpet.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor3 : 13), Blocks.carpet.getStateFromMeta(GeneralConfig.useVillageColors ? this.townColor3 : 13), false); // 13 is green
            
            // Ladder
        	IBlockState biomeLadderState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.ladder.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            this.fillWithBlocks(world, structureBB, 2, 2, 4, 2, 4, 4, biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(1, this.coordBaseMode)), biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(1, this.coordBaseMode)), false);
            
            // Beds
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,2,5,2},
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwo[3];
            		int u = uvwo[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwo[1];
            		int w = uvwo[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor2 : 0); // Goes by wool meta
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
	        		for (int[] ia : new int[][]{
	        			{2, 2, 3, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], 0); // Adult
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
    
    
    
    // --- Small House 6 --- //
    
    public static class PlainsSmallHouse6 extends StructureVillagePieces.Village
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
        		"       ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"  FFF  ",
        		"   F   ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsSmallHouse6() {}

        public PlainsSmallHouse6(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsSmallHouse6 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsSmallHouse6(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
        	
            
            // Log Frame
            // Corners
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uw : new int[][]{
            	{1,2}, {5,2},
            	{1,5}, {5,5},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], 0, uw[1], uw[0], 4, uw[1], biomeLogVertState, biomeLogVertState, false);
            }

            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Ground
            	{2,0,2, 4,0,5}, 
            	{1,0,3, 1,0,4}, {5,0,3, 5,0,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Along logs
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	{1,4,3, 1,4,4}, 
            	{5,4,3, 5,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            // Across logs
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	{2,4,2, 4,4,2}, {2,4,5, 4,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            // Bed table with torch
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            this.setBlockState(world, biomePlankState, 2, 1, 4, structureBB);
            this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(-1, this.coordBaseMode.getHorizontalIndex())), 2,2,4, structureBB);
            
            // Planks
            for(int[] uuvvww : new int[][]{
            	// Front wall
            	{2,1,2, 4,3,2}, {2,5,2, 4,5,2},
            	// Back wall
            	{2,1,5, 4,3,5}, {2,5,5, 4,5,5}, 
            	// Left wall
            	{1,0,3, 1,3,4}, 
            	// Right wall
            	{5,0,3, 5,3,4}, 
            	
            	// Ceiling
            	{3,6,1, 3,6,6},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{3, 2, 5}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Roof
        		{0,4,1, 0}, {0,4,2, 0}, {0,4,3, 0}, {0,4,4, 0}, {0,4,5, 0}, {0,4,6, 0}, 
        		{1,5,1, 0}, {1,5,2, 0}, {1,5,3, 0}, {1,5,4, 0}, {1,5,5, 0}, {1,5,6, 0}, 
        		{2,6,1, 0}, {2,6,2, 0}, {2,6,3, 0}, {2,6,4, 0}, {2,6,5, 0}, {2,6,6, 0}, 
        		{4,6,1, 1}, {4,6,2, 1}, {4,6,3, 1}, {4,6,4, 1}, {4,6,5, 1}, {4,6,6, 1}, 
        		{5,5,1, 1}, {5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, {5,5,5, 1}, {5,5,6, 1}, 
        		{6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, {6,4,6, 1}, 
        		// Roof trim
        		{1,4,1, 5}, {2,5,1, 5}, {4,5,1, 4}, {5,4,1, 4}, 
        		{1,4,6, 5}, {2,5,6, 5}, {4,5,6, 4}, {5,4,6, 4}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{3,0,1, 3}, 
        		})
            {
        		this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{3,3,1,2}, // Front wall
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 1, 2, 2, 1, 1},
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // --- Interior --- //
            
            
            // Beds
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,1,4,3},
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwo[3];
            		int u = uvwo[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwo[1];
            		int w = uvwo[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor2 : 0); // Goes by wool meta
            	}
            }
            
            
                        
        	// Flower planters out front
            
        	IBlockState biomeTrapdoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.trapdoor.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
            for (int u : new int[]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	2,4
            })
            {
            	this.setBlockState(world, biomeGrassState, u, 0, 1, structureBB); // Sod
            	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta((new int[]{4, 7, 5, 6})[this.coordBaseMode.getHorizontalIndex()]), u, 0, 0, structureBB); // Front trapdoor
            	
            	// Flowers on top
        		int flowerindex = random.nextInt(10);
        		// 0-8 is "red" flower
        		// 9 is a basic yellow flower
        		// 10-11 are the flowers from UpToDateMod
        		Block flowerblock = flowerindex == 9 ? Blocks.yellow_flower : Blocks.red_flower;
        		int flowermeta = new int[]{0,1,2,3,4,5,6,7,8,0,0,1}[flowerindex];
        		
        		this.setBlockState(world, flowerblock.getStateFromMeta(flowermeta), u, 1, 1, structureBB);
            }
            
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0 ? 6 : 4), 1, 0, 1, structureBB); // Left trapdoor
        	this.setBlockState(world, biomeTrapdoorState.getBlock().getStateFromMeta(this.coordBaseMode.getHorizontalIndex()%2==0 ? 7 : 5), 5, 0, 1, structureBB); // Right trapdoor
        	
            
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
	        		for (int[] ia : new int[][]{
	        			{4, 1, 3, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], 0); // Adult
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
    
    
    
    // --- Small House 7 --- //
    
    public static class PlainsSmallHouse7 extends StructureVillagePieces.Village
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
        		" FFFFFF ",
        		" FFFFFF ",
        		" FFFFFF ",
        		" FFFFFF ",
        		" FFFFFF ",
        		"  FFFF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsSmallHouse7() {}

        public PlainsSmallHouse7(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsSmallHouse7 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsSmallHouse7(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
        	
            
            // Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Corner posts
            	{1,0,1, 1,3,1}, {6,0,1, 6,3,1},
            	{1,0,5, 1,3,5}, {6,0,5, 6,3,5},
            	// Floor inset
            	{2,0,2, 2,0,2}, {5,0,2, 5,0,2}, 
            	{2,0,4, 2,0,4}, {5,0,4, 5,0,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
            }

            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front
            	{2,0,1, 5,1,1}, {2,3,1, 5,4,1}, {3,5,1, 4,5,1}, 
            	// Back
            	{2,0,5, 5,1,5}, {2,3,5, 5,4,5}, {3,5,5, 4,5,5}, 
            	// Left and right
            	{1,0,2, 1,3,4}, {6,0,2, 6,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Along logs
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	{1,2,2, 1,2,4}, 
            	{6,2,2, 6,2,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            // Across logs
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	{2,2,1, 5,2,1}, {2,2,5, 5,2,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{3,0,2, 4,0,2}, {2,0,3, 5,0,3}, {3,0,4, 4,0,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{1, 2, 3}, {6, 2, 3}, 
        		{3, 2, 5}, {4, 2, 5}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Roof
        		{0,3,0, 0}, {0,3,1, 0}, {0,3,2, 0}, {0,3,3, 0}, {0,3,4, 0}, {0,3,5, 0}, {0,3,6, 0}, 
        		{1,4,0, 0}, {1,4,1, 0}, {1,4,2, 0}, {1,4,3, 0}, {1,4,4, 0}, {1,4,5, 0}, {1,4,6, 0}, 
        		{2,5,0, 0}, {2,5,1, 0}, {2,5,2, 0}, {2,5,3, 0}, {2,5,4, 0}, {2,5,5, 0}, {2,5,6, 0}, 
        		{3,6,0, 0}, {3,6,1, 0}, {3,6,2, 0}, {3,6,3, 0}, {3,6,4, 0}, {3,6,5, 0}, {3,6,6, 0}, 
        		{4,6,0, 1}, {4,6,1, 1}, {4,6,2, 1}, {4,6,3, 1}, {4,6,4, 1}, {4,6,5, 1}, {4,6,6, 1}, 
        		{5,5,0, 1}, {5,5,1, 1}, {5,5,2, 1}, {5,5,3, 1}, {5,5,4, 1}, {5,5,5, 1}, {5,5,6, 1}, 
        		{6,4,0, 1}, {6,4,1, 1}, {6,4,2, 1}, {6,4,3, 1}, {6,4,4, 1}, {6,4,5, 1}, {6,4,6, 1}, 
        		{7,3,0, 1}, {7,3,1, 1}, {7,3,2, 1}, {7,3,3, 1}, {7,3,4, 1}, {7,3,5, 1}, {7,3,6, 1}, 
        		// Roof trim
        		{1,3,0, 5}, {2,4,0, 5}, {3,5,0, 5}, {4,5,0, 4}, {5,4,0, 4}, {6,3,0, 4}, 
        		{1,3,6, 5}, {2,4,6, 5}, {3,5,6, 5}, {4,5,6, 4}, {5,4,6, 4}, {6,3,6, 4}, 
        		// Chair
        		{2,1,4, 1}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,0,0, 0}, 
        		{3,0,0, 3}, 
        		{4,0,0, 3}, 
        		{5,0,0, 1}, 
        		})
            {
        		this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front wall
            	{2,2,0, 2}, {5,2,0, 2}, 
            	{3,3,2, 0}, {4,3,2, 0}, // Interior
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3, 1, 1, 2, 1, 1}, 
            	{4, 1, 1, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // --- Interior --- //
            
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 5;
        	int chestV = 1;
        	int chestW = 4;
        	int chestO = 3;
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo(ChestLootHandler.getGenericLootForVillageType(this.villageType));
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
            
            
            // Beds
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{2,1,3,0},
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwo[3];
            		int u = uvwo[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwo[1];
            		int w = uvwo[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor : 4); // Goes by wool meta
            	}
            }
            
            
         // Clear path for easier entry
            for (int[] uvw : new int[][]{
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
        	
            
            // Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	if (VillageGeneratorConfigHandler.spawnVillagersInResidences)
            	{
	        		for (int[] ia : new int[][]{
	        			{4, 0, 3, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], 0); // Adult
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
    
    
    
    // --- Small House 8 --- //
    
    public static class PlainsSmallHouse8 extends StructureVillagePieces.Village
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
        		"    F    ",
        		"   FFF   ",
        		"  FFFFF  ",
        		" FFFFFFF ",
        		"FFFFFFFFF",
        		" FFFFFFF ",
        		"  FFFFF  ",
        		"   FPF   ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 9;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 2; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 1;
    	private static final int DECREASE_MAX_U = 1;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsSmallHouse8() {}

        public PlainsSmallHouse8(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsSmallHouse8 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsSmallHouse8(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
        	
            
            // Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uuvvww : new int[][]{
            	// Corner posts
            	{2,2,1, 2,6,1}, {6,2,1, 6,6,1}, 
            	{2,2,5, 2,6,5}, {6,2,5, 6,6,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);
            }

            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front
            	{3,2,1, 5,7,1}, 
            	// Back
            	{3,2,5, 5,7,5}, 
            	// Left and right
            	{2,2,2, 2,6,4}, {6,2,2, 6,6,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            // Across logs
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	{3,6,1, 5,6,1}, {3,6,5, 5,6,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{3,2,2, 5,2,4}, 
            	// High walls
            	{2,7,2, 2,7,4}, {6,7,2, 6,7,4}, 
            	// Ceiling
            	{3,8,2, 5,8,4}, {4,8,0, 4,8,1}, {4,8,5, 4,8,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{2, 4, 3}, {2, 7, 3}, {6, 4, 3}, {6, 7, 3}, {4, 4, 5}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Roof
        		{1,6,0, 0}, {1,6,1, 0}, {2,7,0, 0}, {2,7,1, 0}, {3,8,0, 0}, {3,8,1, 0}, 
        		{1,6,2, 0}, {1,6,3, 0}, {1,6,4, 0}, 
        		{1,6,5, 0}, {1,6,6, 0}, {2,7,5, 0}, {2,7,6, 0}, {3,8,5, 0}, {3,8,6, 0},
        		{2,8,2, 3}, {2,8,3, 0}, {2,8,4, 2}, 
        		
        		{7,6,0, 1}, {7,6,1, 1}, {6,7,0, 1}, {6,7,1, 1}, {5,8,0, 1}, {5,8,1, 1}, 
        		{7,6,2, 1}, {7,6,3, 1}, {7,6,4, 1}, 
        		{7,6,5, 1}, {7,6,6, 1}, {6,7,5, 1}, {6,7,6, 1}, {5,8,5, 1}, {5,8,6, 1},
        		{6,8,2, 3}, {6,8,3, 1}, {6,8,4, 2}, 
        		
        		// Roof trim
        		{2,6,0, 5}, {3,7,0, 5}, {6,6,0, 4}, {5,7,0, 4}, 
        		{2,6,6, 5}, {3,7,6, 5}, {6,6,6, 4}, {5,7,6, 4}, 
        		
        		// Chair
        		{5,3,4, 3}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{3,2,0, 0}, 
        		{4,2,0, 3}, 
        		{5,2,0, 1}, 
        		})
            {
        		this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Front wall
            	{4,5,0, 2}, 
            	// Interior
            	{4,6,4, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{4, 3, 1, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            // Farmland with wheat
            for(int[] uvw : new int[][]{
            	// Left
            	{1,1,2}, {1,1,4}, 
            	// Right
            	{7,1,2}, {7,1,4}, 
            	// Back
            	{3,1,6}, {5,1,6}, 
            	})
            {
            	this.setBlockState(world, Blocks.farmland.getStateFromMeta(7), uvw[0], uvw[1], uvw[2], structureBB);
            	this.setBlockState(world, Blocks.wheat.getStateFromMeta(7), uvw[0], uvw[1]+1, uvw[2], structureBB);
            }
            
            // Water for the farmland
            for(int[] uvw : new int[][]{
            	// Left
            	{1,1,3},  
            	// Right
            	{7,1,3},  
            	// Back
            	{4,1,6},  
            	})
            {
            	this.setBlockState(world, Blocks.flowing_water.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // --- Interior --- //
            
            
            // Beds
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward
            	{3,3,3,2},
            })
            {
            	for (boolean isHead : new boolean[]{false, true})
            	{
            		int orientation = uvwo[3];
            		int u = uvwo[0] + (isHead?(new int[]{0,-1,0,1}[orientation]):0);
            		int v = uvwo[1];
            		int w = uvwo[2] + (isHead?(new int[]{-1,0,1,0}[orientation]):0);
                	ModObjects.setModBedBlock(world,
                			this.getXWithOffset(u, w),
                			this.getYWithOffset(v),
                			this.getZWithOffset(u, w),
                			StructureVillageVN.getBedOrientationMeta(orientation, this.coordBaseMode, isHead),
                			GeneralConfig.useVillageColors ? this.townColor2 : 0); // Goes by wool meta
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
	        		for (int[] ia : new int[][]{
	        			{4, 3, 3, -1, 0},
	        			})
	        		{
	        			EntityVillager entityvillager = new EntityVillager(world);
	        			entityvillager = StructureVillageVN.makeVillagerWithProfession(world, random, ia[3], ia[4], 0); // Adult
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
    
    
    
    // --- Cobblestone Stable --- //
    
    public static class PlainsStable1 extends StructureVillagePieces.Village
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
        		"FFFFFF          ",
        		"FFFFFF F      F ",
        		"FFFFFFFFFFFFFFF ",
        		"FFFFFFFFFFFFFFF ",
        		"FFFFFFFFFFFFFFF ",
        		"FFFFFFFFFFFFFFF ",
        		"FFFFFFFFFFFFFFF ",
        		"FFFFFF P        ",
        		"FFFFFF P   F   F",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 4;
    	private static final int DECREASE_MAX_U = 4;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsStable1() {}

        public PlainsStable1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsStable1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsStable1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            
        	
            // Decor
            for (int decorUVW[] : new int[][]{
            	{1, 1, 6}, 
            	{11, 1, 0}, {15, 1, 0}, 
            	})
            {
                // Add foundations
            	this.setBlockState(world, biomeDirtState, decorUVW[0], decorUVW[1]-1, decorUVW[2], structureBB);
        		this.replaceAirAndLiquidDownwards(world, biomeFillerState, decorUVW[0], decorUVW[1]-2, decorUVW[2], structureBB);
        		
                // The actual decor
        		for (int j=0; j<decorUVW.length; j++)
                {
        			int decorHeightY = decorUVW[1];
        			
        			// Set random seed
                	Random randomFromXYZ = new Random();
                	randomFromXYZ.setSeed(
        					world.getSeed() +
        					FunctionsVN.getUniqueLongForXYZ(
        							this.getXWithOffset(decorUVW[0], decorUVW[2]),
        							this.getYWithOffset(decorHeightY),
        							this.getZWithOffset(decorUVW[0], decorUVW[2])
        							)
                			);
                	
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
                	ArrayList<BlueprintData> decorBlueprint = StructureVillageVN.getRandomDecorBlueprint(this.villageType, this.materialType, this.disallowModSubs, this.biome, this.coordBaseMode, randomFromXYZ, VillageGeneratorConfigHandler.allowTaigaTroughs && !VillageGeneratorConfigHandler.restrictTaigaTroughs);
                	
                	for (BlueprintData b : decorBlueprint)
                	{
                		// Place block indicated by blueprint
                		this.setBlockState(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos(), decorUVW[2]+b.getWPos(), structureBB);
                		
                		// Fill below if flagged
                		if ((b.getfillFlag()&1)!=0)
                		{
                			this.replaceAirAndLiquidDownwards(world, b.getBlockState(), decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()-1, decorUVW[2]+b.getWPos(), structureBB);
                		}
                		
                		// Clear above if flagged
                		if ((b.getfillFlag()&2)!=0)
                		{
                			this.clearCurrentPositionBlocksUpwards(world, decorUVW[0]+b.getUPos(), decorHeightY+b.getVPos()+1, decorUVW[2]+b.getWPos(), structureBB);
                		}            		
                	}
                }
            }
            

            // Stone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front
            	{5,1,2, 14,4,2}, 
            	// Back
            	{5,1,6, 14,4,6}, 
            	// Right
            	{14,1,2, 14,4,6}, {14,5,3, 14,5,5}, 
            	// Left
            	{5,4,2, 5,4,6}, {5,5,3, 5,5,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            
            // Along logs
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	{12,1,3, 12,1,5}, 
            	{14,2,3, 14,2,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            // Across logs
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	{9,2,2, 13,2,2}, 
            	{8,2,6, 12,2,6},
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Ceiling
            	{4,6,4, 15,6,4},  
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{10, 2, 2}, {12, 2, 2}, 
        		{9, 2, 6}, {11, 2, 6},
        		{14, 2, 4}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Roof
        		{4,4,1, 3}, {5,4,1, 3}, {6,4,1, 3}, {7,4,1, 3}, {8,4,1, 3}, {9,4,1, 3}, {10,4,1, 3}, {11,4,1, 3}, {12,4,1, 3}, {13,4,1, 3}, {14,4,1, 3}, {15,4,1, 3}, 
        		{4,5,2, 3}, {5,5,2, 3}, {6,5,2, 3}, {7,5,2, 3}, {8,5,2, 3}, {9,5,2, 3}, {10,5,2, 3}, {11,5,2, 3}, {12,5,2, 3}, {13,5,2, 3}, {14,5,2, 3}, {15,5,2, 3}, 
        		{4,6,3, 3}, {5,6,3, 3}, {6,6,3, 3}, {7,6,3, 3}, {8,6,3, 3}, {9,6,3, 3}, {10,6,3, 3}, {11,6,3, 3}, {12,6,3, 3}, {13,6,3, 3}, {14,6,3, 3}, {15,6,3, 3}, 
        		{4,6,5, 2}, {5,6,5, 2}, {6,6,5, 2}, {7,6,5, 2}, {8,6,5, 2}, {9,6,5, 2}, {10,6,5, 2}, {11,6,5, 2}, {12,6,5, 2}, {13,6,5, 2}, {14,6,5, 2}, {15,6,5, 2}, 
        		{4,5,6, 2}, {5,5,6, 2}, {6,5,6, 2}, {7,5,6, 2}, {8,5,6, 2}, {9,5,6, 2}, {10,5,6, 2}, {11,5,6, 2}, {12,5,6, 2}, {13,5,6, 2}, {14,5,6, 2}, {15,5,6, 2}, 
        		{4,4,7, 2}, {5,4,7, 2}, {6,4,7, 2}, {7,4,7, 2}, {8,4,7, 2}, {9,4,7, 2}, {10,4,7, 2}, {11,4,7, 2}, {12,4,7, 2}, {13,4,7, 2}, {14,4,7, 2}, {15,4,7, 2}, 
        		// Trim
        		{4,4,2, 6}, {4,5,3, 6}, {4,5,5, 7}, {4,4,6, 7}, 
        		{15,4,2, 6}, {15,5,3, 6}, {15,5,5, 7}, {15,4,6, 7}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{5,3,3, 6}, {5,3,5, 7}, 
        		})
            {
        		this.setBlockState(world, biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{0,1,0, 0,1,8}, 
        		{5,1,0, 5,1,1}, {5,1,7, 5,1,8}, 
        		{1,1,0, 4,1,0}, 
        		{1,1,8, 4,1,8}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);	
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior
            	{4,4,4, 3}, {15,4,4, 1}, 
            	// Interior
            	{6,4,4, 1}, {13,4,4, 3}, 
            	{10,4,3, 0}, {10,4,5, 2}, 
            	// Stables
            	{0,2,0, -1}, {5,2,0, -1}, 
            	{0,2,8, -1}, {5,2,8, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{7,1,2, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Hay bales
        	for (int[] uvw : new int[][]{
        		// Outside of the barn
        		{3, 1, 3}, {5, 1, 5}, 
        		// Inside of the barn
        		{8, 1, 3}, {9, 1, 3}, {8, 2, 3}, 
        		})
            {
        		this.setBlockState(world, Blocks.hay_block.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
        	// Grass
        	for (int[] uuvvww : new int[][]{
        		{0,0,0, 5,0,8},
        		{5,0,3, 13,0,5}, 
        		{7,0,2, 7,0,2}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }
        	
        	
            // Water trough
        	this.fillWithBlocks(world, structureBB, 13,1,3, 13,1,5, Blocks.flowing_water.getStateFromMeta(0), Blocks.flowing_water.getStateFromMeta(0), false);
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
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
        	
            
            // Animals
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{2, 1, 2}, 
        			{10, 1, 4}, 
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Terracotta Stable --- //
    
    public static class PlainsStable2 extends StructureVillagePieces.Village
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
        		" F               ",
        		"FFFFFFFFFFFFFFFF ",
        		"FFFFFFFFFFFFFFFF ",
        		"FFFFFFFFFFFFFFFF ",
        		"FFFFFFFFFFFFFFFF ",
        		"FFFFFFFFFFFFFFFF ",
        		"   F     FP      ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 1; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 4;
    	private static final int DECREASE_MAX_U = 2;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsStable2() {}

        public PlainsStable2(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsStable2 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsStable2(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            

            // Stained terracotta
            for(int[] uuvvww : new int[][]{
            	// Front
            	{6,1,1, 14,3,1}, 
            	// Lip
            	{5,4,2, 5,4,4}, 
            	// Back
            	{6,1,5, 14,3,5}, 
            	// Back wall
            	{15,1,2, 15,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors? this.townColor2:0), Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors? this.townColor2:0), false);	
            }
            
            
            // Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uw : new int[][]{
            	{5,1,1, 5,3,1}, {8,1,1, 8,3,1}, {12,1,1, 12,3,1}, {15,1,1, 15,3,1}, 
            	{5,1,5, 5,3,5}, {8,1,5, 8,3,5}, {12,1,5, 12,3,5}, {15,1,5, 15,3,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogVertState, biomeLogVertState, false);
            }
            
            // Along logs
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for (int[] uw : new int[][]{
            	{5,3,2, 5,3,4}, 
            	// Trough
            	{13,1,2, 13,1,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAlongState, biomeLogHorAlongState, false);
            }
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Ceiling
            	{4,5,3, 16,5,3}, 
            	// Segments above the door
            	{9,4,1, 11,4,1}, {9,4,5, 11,4,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            // Double wood slab
        	IBlockState biomeWoodDoubleSlabState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.double_wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Awning
            	{2,4,2, 4,4,2}, {2,4,4, 4,4,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodDoubleSlabState, biomeWoodDoubleSlabState, false);	
            }
            
            // Bottom wood slab
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Awning
            	{2,5,3, 3,5,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
            }
            
            // Top wood slab
        	IBlockState biomeWoodSlabTopState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(8), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Awning
            	{2,4,3, 4,4,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabTopState, biomeWoodSlabTopState, false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{15, 2, 3}, {10, 2, 5}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		
        		// Roof
        		{4,3,0, 3}, {5,3,0, 3}, {6,3,0, 3}, {7,3,0, 3}, {8,3,0, 3}, {9,4,0, 3}, {10,4,0, 3}, {11,4,0, 3}, {12,3,0, 3}, {13,3,0, 3}, {14,3,0, 3}, {15,3,0, 3}, {16,3,0, 3}, 
        		{2,4,1, 3}, {3,4,1, 3}, {4,4,1, 3}, {5,4,1, 3}, {6,4,1, 3}, {7,4,1, 3}, {8,4,1, 3}, {12,4,1, 3}, {13,4,1, 3}, {14,4,1, 3}, {15,4,1, 3}, {16,4,1, 3}, 
        		{4,5,2, 3}, {5,5,2, 3}, {6,5,2, 3}, {7,5,2, 3}, {8,5,2, 3}, {9,5,2, 3}, {10,5,2, 3}, {11,5,2, 3}, {12,5,2, 3}, {13,5,2, 3}, {14,5,2, 3}, {15,5,2, 3}, {16,5,2, 3}, 
        		{4,5,4, 2}, {5,5,4, 2}, {6,5,4, 2}, {7,5,4, 2}, {8,5,4, 2}, {9,5,4, 2}, {10,5,4, 2}, {11,5,4, 2}, {12,5,4, 2}, {13,5,4, 2}, {14,5,4, 2}, {15,5,4, 2}, {16,5,4, 2}, 
        		{2,4,5, 2}, {3,4,5, 2}, {4,4,5, 2}, {5,4,5, 2}, {6,4,5, 2}, {7,4,5, 2}, {8,4,5, 2}, {12,4,5, 2}, {13,4,5, 2}, {14,4,5, 2}, {15,4,5, 2}, {16,4,5, 2}, 
        		{4,3,6, 2}, {5,3,6, 2}, {6,3,6, 2}, {7,3,6, 2}, {8,3,6, 2}, {9,4,6, 2}, {10,4,6, 2}, {11,4,6, 2}, {12,3,6, 2}, {13,3,6, 2}, {14,3,6, 2}, {15,3,6, 2}, {16,3,6, 2}, 
        		// Trim
        		{4,3,1, 6}, {4,3,5, 7}, 
        		{16,3,1, 6}, {16,3,5, 7}, {16,4,2, 6}, {16,4,4, 7}, 
        		{9,3,0, 5}, {11,3,0, 4}, {9,3,6, 5}, {11,3,6, 4}, 
        		})
            {
        		this.setBlockState(world, biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uvwo[3]%4)+(uvwo[3]/4)*4), uvwo[0], uvwo[1], uvwo[2], structureBB);	
            }
            
        	
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{0,1,1, 4,1,1}, {0,1,5, 4,1,5}, {0,1,2, 0,1,4}, 
        		{2,2,1, 2,3,1}, {2,2,5, 2,3,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);	
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Exterior
            	{4,3,3, 3}, {6,3,3, 1}, 
            	{10,3,2, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{10,1,1, 2, 1, 1}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
            				uvwoor[0], uvwoor[1]+height, uvwoor[2], structureBB);
            	}
            }
            
            
            // Hay bales, vertical
        	for (int[] uvw : new int[][]{
        		{6,1,2}, {7,1,4}, {8,1,4}, {8,2,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.hay_block.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	// Hay bales, along
        	for (int[] uvw : new int[][]{
        		{9,1,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.hay_block.getStateFromMeta(4+(this.coordBaseMode.getHorizontalIndex()%2==0? 0:4)), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
        	// Grass
        	for (int[] uuvvww : new int[][]{
        		{0,0,1, 4,0,5},
        		// Interior
        		{5,0,2, 14,0,4},
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeGrassState, biomeGrassState, false);	
            }
        	
        	
            // Water trough
        	this.fillWithBlocks(world, structureBB, 14,1,2, 14,1,4, Blocks.flowing_water.getStateFromMeta(0), Blocks.flowing_water.getStateFromMeta(0), false);
            
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
        		{10, GROUND_LEVEL, -1}, 
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
            	
            	// Animals
            	for (int[] uvw : new int[][]{
        			{2, 1, 3}, 
        			{4, 1, 3}, 
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 0;}
    }
    
    
    
    // --- Tannery --- //
    
    public static class PlainsTannery1 extends StructureVillagePieces.Village
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
        		" FFFFFFFF ",
        		" FFFFFFFF ",
        		" FFFFFFFF ",
        		" FFFFFFFF ",
        		"  FFF FFF ",
        		"      FFF ",
        		" F   F F  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 4;
    	private static final int DECREASE_MAX_U = 2;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsTannery1() {}

        public PlainsTannery1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsTannery1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsTannery1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            

            // Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Front
            	{2,0,3, 6,3,3}, 
            	// Back
            	{2,0,6, 7,3,6}, 
            	// Left
            	{1,0,4, 1,4,5}, 
            	// Right
            	{8,0,3, 8,4,5}, 
            	// Front door
            	{7,0,2, 7,0,2}, {7,3,2, 7,3,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Ceiling
            	{5,5,4, 9,5,4}, 
            	// Above door frame
            	{5,4,3, 8,4,3}, 
            	// Door awning
            	{7,4,1, 7,4,2}, 
            	// Floor
            	{2,0,4, 7,0,5}, {7,0,3, 7,0,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Bottom wood slab
        	IBlockState biomeWoodSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_slab.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Awning
            	{1,3,0, 5,3,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeWoodSlabBottomState, biomeWoodSlabBottomState, false);	
            }
            
            
            // Windows
        	for (int[] uvw : new int[][]{
        		{8, 2, 4}, {6, 2, 6}, {3, 2, 6}, 
        		})
            {
        		this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		// Front
        		{0,3,2, 4,3,2, 3},
        		{0,4,3, 4,4,3, 3},
        		{0,5,4, 4,5,4, 3},
        		{9,4,2, 9,4,2, 3},
        		{5,5,3, 9,5,3, 3},
        		{5,3,1, 5,3,2, 0},
        		{6,4,1, 6,4,2, 0},
        		{8,4,1, 8,4,2, 1},
        		{9,3,1, 9,3,1, 1},
        		// Back
        		{0,3,7, 9,3,7, 2},
        		{0,4,6, 9,4,6, 2},
        		{0,5,5, 9,5,5, 2},
        		// Trims
        		{6,3,1, 6,3,1, 5},
        		{8,3,1, 8,3,1, 4},
        		
        		{0,3,6, 0,3,6, 7},
        		{0,4,5, 0,4,5, 7},
        		{0,4,4, 0,4,4, 6},
        		{0,3,3, 0,3,3, 6},
        		{9,4,5, 9,4,5, 7},
        		{9,3,6, 9,3,6, 7},
        		{9,4,3, 9,4,3, 6},
        		{9,3,2, 9,3,2, 6},
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);	
            }
        	
        	
            // Stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{7,0,1, 7,0,1, 3},
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);	
            }
            
        	
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvww : new int[][]{
        		{1,0,0, 1,2,0}, {5,0,0, 5,2,0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);	
            }
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{6,0,1}, {8,0,1}, 
        		})
            {
        		this.setBlockState(world, biomeCobblestoneWallState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{6,1,1, -1}, {8,1,1, -1}, 
            	{7,3,3, 0}, 
            	{2,3,4, 1}, {2,3,5, 1}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uw : new int[][]{
            	{1,0,3, 1,3,3}, {1,0,6, 1,3,6}, 
            	{8,0,2, 8,3,2}, {8,0,6, 8,3,6}, 
            	{6,0,2, 6,3,2}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogVertState, biomeLogVertState, false);
            }
            
            
            // Smooth stone
        	IBlockState smoothStoneState = ModObjects.chooseModSmoothStoneBlockState();
            this.setBlockState(world, smoothStoneState, 2, 1, 4, structureBB);
            
        	
            // Cauldron
        	for (int[] uuvvww : new int[][]{
        		{2,0,2, 4,0,2}, 
        		{2,1,5, 2,1,5}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.cauldron.getStateFromMeta(3), Blocks.cauldron.getStateFromMeta(3), false);	
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{7,1,2, 2, 1, 1}, 
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
            int chestU = 7;
        	int chestV = 1;
        	int chestW = 5;
        	int chestO = 3;
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_tannery");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
            
            // Clear path for easier entry
            for (int[] uvw : new int[][]{
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
            	int u = 3+random.nextInt(4);
            	int v = 1;
            	int w = 4+random.nextInt(2);
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 4;}
    }
    
    
    
    // --- Terracotta Temple --- //
    
    public static class PlainsTemple3 extends StructureVillagePieces.Village
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
        		"       ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 7;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsTemple3() {}

        public PlainsTemple3(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsTemple3 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsTemple3(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            

            // Terracotta
            for(int[] uuvvww : new int[][]{
            	// Floor rim
            	{2,1,1, 4,4,1}, 
            	{2,1,9, 4,4,9}, 
            	{1,1,2, 1,4,4}, {1,1,6, 1,4,8}, 
            	{5,1,2, 5,4,4}, {5,1,6, 5,4,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors? this.townColor2:0), Blocks.stained_hardened_clay.getStateFromMeta(GeneralConfig.useVillageColors? this.townColor2:0), false);	
            }
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{1,0,0}, {5,0,0}, 
        		})
            {
        		this.setBlockState(world, biomeCobblestoneWallState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{1,1,0, -1}, {5,1,0, -1}, 
            	{3,4,2, 0}, {3,4,8, 2}, {3,4,10, 0}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Vertical logs
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uw : new int[][]{
            	{1,0,1, 1,4,1}, {5,0,1, 5,4,1}, 
            	{1,0,5, 1,4,5}, {5,0,5, 5,4,5}, 
            	{1,0,9, 1,4,9}, {5,0,9, 5,4,9}, 
            	{2,4,1, 2,4,1}, {4,4,1, 4,4,1}, 
            	{2,4,9, 2,4,9}, {4,4,9, 4,4,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogVertState, biomeLogVertState, false);
            }
            
            
            // Horizontal logs
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for (int[] uw : new int[][]{
            	{2,5,1, 4,5,1}, 
            	{2,5,8, 4,5,8}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uw[0], uw[1], uw[2], uw[3], uw[4], uw[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);
            }
            

            // Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor rim
            	{2,0,1, 4,0,1}, 
            	{1,0,2, 1,0,4}, {1,0,6, 1,0,8}, 
            	{5,0,2, 5,0,4}, {5,0,6, 5,0,8}, 
            	{2,0,9, 4,0,9}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{2,0,2, 4,0,8}, 
            	// Ceiling
            	{1,5,2, 1,5,4}, {1,5,6, 1,5,8}, {5,5,2, 5,5,4}, {5,5,6, 5,5,8}, 
            	{3,6,0, 3,6,10}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Windows
            // Primary color
        	for (int[] uvw : new int[][]{
        		{1, 2, 3}, {1, 2, 7}, {5, 2, 3}, {5, 2, 7}, {3, 2, 9}, 
        		})
            {
        		this.setBlockState(world, Blocks.stained_glass_pane.getStateFromMeta(GeneralConfig.useVillageColors? this.townColor:4), uvw[0], uvw[1], uvw[2], structureBB);
            }
            // Secondary color
        	for (int[] uvw : new int[][]{
        		{1, 3, 3}, {1, 3, 7}, {5, 3, 3}, {5, 3, 7}, 
        		})
            {
        		this.setBlockState(world, Blocks.stained_glass_pane.getStateFromMeta(GeneralConfig.useVillageColors? this.townColor2:0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
        	
            // Wooden stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		{0,4,0, 0,4,1, 0}, {0,4,5, 0,4,5, 0}, {0,4,9, 0,4,10, 0}, 
        		{1,5,0, 1,5,1, 0}, {1,5,5, 1,5,5, 0}, {1,5,9, 1,5,10, 0}, 
        		{2,6,0, 2,6,10, 0}, 
        		{0,5,2, 0,5,2, 3}, {0,5,3, 0,5,3, 0}, {0,5,4, 0,5,4, 2}, {0,5,6, 0,5,6, 3}, {0,5,7, 0,5,7, 0}, {0,5,8, 0,5,8, 2}, 
        		{6,5,2, 6,5,2, 3}, {6,5,3, 6,5,3, 1}, {6,5,4, 6,5,4, 2}, {6,5,6, 6,5,6, 3}, {6,5,7, 6,5,7, 1}, {6,5,8, 6,5,8, 2}, 
        		{4,6,0, 4,6,10, 1}, 
        		{5,5,0, 5,5,1, 1}, {5,5,5, 5,5,5, 1}, {5,5,9, 5,5,10, 1}, 
        		{6,4,0, 6,4,1, 1}, {6,4,5, 6,4,5, 1}, {6,4,9, 6,4,10, 1}, 
        		// Trim
        		{1,4,0, 1,4,0, 5}, {2,5,0, 2,5,0, 5}, {4,5,0, 4,5,0, 4}, {5,4,0, 5,4,0, 4}, 
        		{1,4,10, 1,4,10, 5}, {2,5,10, 2,5,10, 5}, {4,5,10, 4,5,10, 4}, {5,4,10, 5,4,10, 4}, 
        		{0,4,2, 0,4,2, 6}, {0,4,4, 0,4,4, 7}, {0,4,6, 0,4,6, 6}, {0,4,8, 0,4,8, 7}, 
        		{6,4,2, 6,4,2, 6}, {6,4,4, 6,4,4, 7}, {6,4,6, 6,4,6, 6}, {6,4,8, 6,4,8, 7}, 
        		// Benches
        		{2,1,2, 2,1,5, 1}, {4,1,2, 4,1,5, 0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);	
            }
        	
        	
            // Stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		{2,0,0, 2,0,0, 0}, 
        		{3,0,0, 3,0,0, 3}, 
        		{4,0,0, 4,0,0, 1}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);	
            }
            
            
        	
            // Brewing stand
        	for (int[] uvw : new int[][]{
        		{3,1,7}, 
        		})
            {
        		this.setBlockState(world, Blocks.brewing_stand.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3,1,1, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
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
            	
            	// Villager
            	int u = 3;
            	int v = 1;
            	int w = 7;
            	
            	while (u==3 && w==7)
            	{
                	u = 2 + random.nextInt(3);
                	v = 1;
                	w = 6 + random.nextInt(3);
            	}
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 2;}
    }
    
    
    
    // --- Cobblestone Temple --- //
    
    public static class PlainsTemple4 extends StructureVillagePieces.Village
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
        		"  FFF  ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		" FFFFF ",
        		"FFFFFFF",
        		"FFFFFFF",
        		"FFFFFFF",
        		" FFFFF ",
        		" FFFFF ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 12;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsTemple4() {}

        public PlainsTemple4(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsTemple4 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsTemple4(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            

            // Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Posts out front
            	{1,0,0, 1,0,0}, {5,0,0, 5,0,0}, 
            	// Front wall
            	{2,0,1, 4,4,1}, 
            	// Left wall
            	{0,0,2, 0,4,4}, {1,4,2, 1,10,4}, {1,0,5, 1,4,8}, 
            	// Right wall
            	{6,0,2, 6,4,4}, {5,4,2, 5,10,4}, {5,0,5, 5,4,8}, 
            	// Back wall
            	{2,0,9, 4,4,9}, {2,6,5, 4,10,5}, 
            	// Floor
            	{1,0,2, 2,0,4}, {4,0,2, 5,0,4}, {3,0,4, 3,0,4}, 
            	{2,0,5, 4,0,8}, {2,1,7, 2,1,8}, {3,1,8, 3,1,8}, {4,1,7, 4,1,8}, 
            	// Lower ceiling
            	{2,5,1, 4,5,8}, 
            	// Upper ceiling
            	{2,9,2, 4,9,4}, 
            	// parapet
            	{1,9,1, 1,9,1}, {5,9,1, 5,9,1},
            	{1,9,5, 1,9,5}, {5,9,5, 5,9,5},
            	{3,11,5, 3,11,5}, {1,11,3, 1,11,3}, {5,11,3, 5,11,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
        	
        	
        	// Cobblestone wall
        	IBlockState biomeCobblestoneWallState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone_wall.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{1,1,0}, {5,1,0}, 
        		})
            {
        		this.setBlockState(world, biomeCobblestoneWallState, uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Cobblestone slab
        	IBlockState biomeCobblestoneSlabBottomState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_slab.getStateFromMeta(3), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
        		{0,5,3}, {6,5,3}, 
        		})
            {
        		this.setBlockState(world, biomeCobblestoneSlabBottomState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{1,2,0, -1}, {5,2,0, -1}, 
            	{3,2,2, 0}, 
            	{2,4,7, 1}, {3,4,8, 2}, {4,4,7, 3}, 
            	// Second floor
            	{2,7,2, 1}, {4,7,2, 3}, 
            	// Rooftop torches
            	{3,6,7, -1}, {3,10,3, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Cobblestone part 2
            // Used to touch up the remainder after torches were put in place
            for(int[] uuvvww : new int[][]{
            	// Posts out front
            	{2,6,1, 4,10,1}, {3,11,1, 3,11,1}, 
            	{1,0,1, 1,3,2}, {5,0,1, 5,3,2}, 
            	{1,0,3, 1,3,2}, {5,0,3, 5,3,2}, 
            	{1,1,4, 1,3,4}, {5,1,4, 5,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            
            
            // Stained Glass Windows
            for (int[] uvw : new int[][]{
            	// Front
            	{3,3,1}, {3,7,1}, 
            	// Left
            	{0,2,3}, {1,2,7}, {1,7,3}, 
            	// Right
            	{6,2,3}, {5,2,7}, {5,7,3}, 
            	// Back
            	{3,7,5}, {3,2,9}, 
        		})
            {
        		for (int i=0; i<2; i++)
        		{
        			this.setBlockState(world, Blocks.stained_glass_pane.getStateFromMeta(
        					i==0 ? (GeneralConfig.useVillageColors? this.townColor:4) : (GeneralConfig.useVillageColors? this.townColor2:0)),
        					uvw[0], uvw[1]+i, uvw[2], structureBB);
        		}
            }
        	
        	
            // Stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof trim
        		{1,4,1, 1,4,1, 0}, {1,8,1, 1,8,1, 4}, {1,8,5, 1,8,5, 4}, 
        		{5,4,1, 5,4,1, 1}, {5,8,1, 5,8,1, 5}, {5,8,5, 5,8,5, 5}, 
        		{0,4,2, 0,4,2, 3}, {0,4,4, 0,4,4, 2}, 
        		{6,4,2, 6,4,2, 3}, {6,4,4, 6,4,4, 2}, 
        		// Interior
        		{3,0,3, 3,0,3, 3}, 
        		{1,1,3, 1,1,3, 1}, {5,1,3, 5,1,3, 0}, 
        		{2,2,8, 2,2,8, 1}, {4,2,8, 4,2,8, 0}, 
        		{2,1,6, 2,1,6, 3}, {3,1,7, 3,1,7, 3}, {4,1,6, 4,1,6, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);	
            }
            
            
            // Ladder
        	IBlockState biomeLadderState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.ladder.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 3:leftward, 1:rightward, 2:backward, 0:forward
        		// Roof trim
        		{4,1,4, 4,10,4, 3},  
        		})
            {
        		this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode)), biomeLadderState.getBlock().getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uuvvwwo[6], this.coordBaseMode)), false);
            }
            
            
            // Brewing stand
        	for (int[] uvw : new int[][]{
        		{2,1,4}, 
        		})
            {
        		this.setBlockState(world, Blocks.brewing_stand.getDefaultState(), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{3,0,1, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
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
            	
            	// Villager
            	int u = 2;
            	int v = 1;
            	int w = 4;
            	
            	while (u==2 && w==4)
            	{
                	u = 2 + random.nextInt(3);
                	w = 2 + random.nextInt(7);
                	
                	if (u==3 && w==2) // At the entrance
                	{
                		v=0;
                	}
                	else if ( // On the split level
                			(u==2 || u==4) && (w==6 || w==7)
                			||
                			(u==3  && (w==7 || w==8)) 
                			)
                	{
                		v=2;
                	}
                	else if ((u==2 || u==4) && w==8) // At the very back
                	{
                		v=3;
                	}
                	else {v=1;}
            	}
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 2;}
    }
    
    
    
    // --- Tool Smithy --- //
    
    public static class PlainsToolSmith1 extends StructureVillagePieces.Village
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
        		" FFFFFFFFFF ",
        		" FFFFFFFFFF ",
        		" FFFFFFFFFF ",
        		" FFFFFFFFFF ",
        		"       FFFF ",
        		"       FFFF ",
        		"        FF  ",
        		"        FF  ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 6;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 6;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsToolSmith1() {}

        public PlainsToolSmith1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsToolSmith1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsToolSmith1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            

            // Cobblestone
        	IBlockState biomeCobblestoneState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.cobblestone.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Posts out front
            	{1,0,4, 10,0,7}, 
            	{7,0,2, 10,0,3}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeCobblestoneState, biomeCobblestoneState, false);	
            }
            

            // Logs
            // Vertical
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{1,0,4, 1,3,4}, {1,0,7, 1,3,7}, 
            	{7,0,2, 7,3,2}, {10,0,2, 10,3,2}, 
            	{7,0,4, 7,3,4}, {10,0,7, 10,3,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	// Posts out front
            	{1,1,5, 1,4,6}, 
            	{2,1,4, 6,3,4}, 
            	{10,1,3, 10,4,6}, 
            	{2,1,7, 9,3,7}, 
            	{7,1,3, 7,3,3}, 
            	{8,3,2, 9,4,2}, 
            	// Table
            	{2,1,5, 2,1,5}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
        	
            // Logs Across
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	{3,2,4, 5,2,4}, 
            	{3,2,7, 8,2,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Torches
        	for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	// Side
            	{0,2,4, 3}, {0,2,7, 3}, 
            	// Entrance
            	{7,2,1, 2}, {10,2,1, 2}, 
            	// Interior
            	{4,3,6, 2}, {7,3,6, 2}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Windows
            for (int[] uvw : new int[][]{
            	{4,2,4}, {4,2,7}, {7,2,7}, 
        		})
            {
            	this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front roof
        		{0,3,3, 5,3,3, 3}, 
        		{0,4,4, 6,4,4, 3}, {11,4,4, 11,4,4, 3}, 
        		{0,5,5, 7,5,5, 3}, {10,5,5, 11,5,5, 3}, 
        		// Back roof
        		{0,5,6, 11,5,6, 2}, 
        		{0,4,7, 11,4,7, 2}, 
        		{0,3,8, 11,3,8, 2}, 
        		// Left roof
        		{6,3,1, 6,3,3, 0}, 
        		{7,4,1, 7,4,4, 0}, 
        		{8,5,1, 8,5,5, 0}, 
        		// Right roof
        		{9,5,1, 9,5,5, 1}, 
        		{10,4,1, 10,4,4, 1}, 
        		{11,3,1, 11,3,3, 1}, 
        		// Trim
        		{0,3,4, 0,3,4, 6}, {0,4,5, 0,4,5, 6}, {0,4,6, 0,4,6, 7}, {0,3,7, 0,3,7, 7}, 
        		{11,3,4, 11,3,4, 6}, {11,4,5, 11,4,5, 6}, {11,4,6, 11,4,6, 7}, {11,3,7, 11,3,7, 7}, 
        		{7,3,1, 7,3,1, 5}, {8,4,1, 8,4,1, 5}, {9,4,1, 9,4,1, 4}, {10,3,1, 10,3,1, 4}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);	
            }
        	
        	
            // Stone stairs
        	IBlockState biomeStoneStairsBlock = StructureVillageVN.getBiomeSpecificBlockState(Blocks.stone_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{8,0,1, 9,0,1, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeStoneStairsBlock.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);	
            }
            
            
            // Smithing table
        	IBlockState smithingTableBlockState = ModObjects.chooseModSmithingTable();
        	for (int[] uvw : new int[][]{
        		{2,1,6}, 
        		})
            {
        		this.setBlockState(world, smithingTableBlockState, uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{8,1,2, 2, 1, 1}, {9,1,2, 2, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
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
            	
            	// Villager
            	int u = 3 + random.nextInt(9);
            	int v = 1;
            	int w = 5 + random.nextInt(2);
            	
            	// Put in the section by the door
            	if (u > 9)
            	{
            		u-=2; w-=2;
            	}
            	
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    
    // --- Weapon Smithy --- //
    
    public static class PlainsWeaponsmith1 extends StructureVillagePieces.Village
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
        		"           ",
        		"FFFFFFFFFF ",
        		"FFFFFFFFFF ",
        		"FFFFFFFFFF ",
        		"FFFFFFFFFF ",
        		"FFFFFFFFFF ",
        		"FFFFFFFFFF ",
        		"FFFFFFFFFF ",
        		" FFF       ",
        };
    	// Here are values to assign to the bounding box
    	public static final int STRUCTURE_WIDTH = foundationPattern[0].length();
    	public static final int STRUCTURE_DEPTH = foundationPattern.length;
    	public static final int STRUCTURE_HEIGHT = 8;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 6;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsWeaponsmith1() {}

        public PlainsWeaponsmith1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsWeaponsmith1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsWeaponsmith1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            

            // Cobblestone
            for(int[] uuvvww : new int[][]{
            	// Floor
            	{0,0,1, 9,0,7}, 
            	// Basin
            	{0,1,5, 2,3,6}, 
            	// Furnace block
            	{3,1,4, 3,3,6}, {4,1,4, 4,3,4}, 
            	// Back wall
            	{0,0,7, 5,4,7}, 
            	// Ceiling
            	{0,4,1, 8,4,6},  
            	// Furnace
            	{1,5,6, 2,5,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.cobblestone.getStateFromMeta(0), Blocks.cobblestone.getStateFromMeta(0), false);	
            }
            
            
            // Chimney
            for (int[] uvw : new int[][]{
            	{1,6,6}, 
            	}) {
            	this.setBlockState(world, Blocks.cobblestone_wall.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
            
            
            // Smooth stone roof trim
            for(int[] uuvvww : new int[][]{
            	{0,5,1, 4,5,1}, 
            	{0,5,2, 0,5,6}, 
            	{0,5,7, 4,5,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], Blocks.stone_slab.getStateFromMeta(0), Blocks.stone_slab.getStateFromMeta(0), false);
            }
            
            
            // Lava basin
            this.fillWithBlocks(world, structureBB, 1, 2, 5, 2, 2, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(world, structureBB, 1, 1, 6, 2, 1, 6, Blocks.lava.getDefaultState(), Blocks.lava.getDefaultState(), false);
            this.fillWithBlocks(world, structureBB, 0, 2, 5, 0, 2, 6, Blocks.iron_bars.getDefaultState(), Blocks.iron_bars.getDefaultState(), false);
            
            
            // Planks
        	IBlockState biomePlankState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.planks.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{6,1,2, 6,3,3}, 
            	{7,1,1, 8,3,1}, {7,5,1, 8,6,1}, 
            	{7,1,7, 8,3,7}, {7,5,7, 8,6,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            
            
            // Torches
            for (int[] uvwo : new int[][]{ // Orientation - 0:forward, 1:rightward, 2:backward (toward you), 3:leftward, -1:upright;
            	{5,2,3, 3}, 
            	{1,2,8, 0}, 
            	{8,2,6, 2}, 
            	{3,5,4, -1}, 
            	}) {
            	this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(uvwo[3], this.coordBaseMode.getHorizontalIndex())), uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
            
            
            // Planks part 2 so that we can place torch
            for(int[] uuvvww : new int[][]{
            	{9,1,2, 9,3,6}, {9,5,2, 9,6,6}, 
            	// Table
            	{8,1,6, 8,1,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomePlankState, biomePlankState, false);	
            }
            this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(1, this.coordBaseMode.getHorizontalIndex())), 10, 2, 4, structureBB);
            // This torch is manually added by me because I don't want zombies spawning int the attic
            this.setBlockState(world, Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(3, this.coordBaseMode.getHorizontalIndex())), 8, 5, 4, structureBB);
            
            
            // Logs
            // Vertical
        	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for(int[] uuvvww : new int[][]{
            	{6,0,1, 6,5,1}, {9,0,1, 9,5,1}, 
            	{6,0,7, 6,5,7}, {9,0,7, 9,5,7}, 
            	{5,1,4, 5,3,4}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogVertState, biomeLogVertState, false);	
            }
            // Along
        	IBlockState biomeLogHorAlongState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), false); // Toward you
            for(int[] uuvvww : new int[][]{
            	{9,4,2, 9,4,6}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAlongState, biomeLogHorAlongState, false);	
            }
            // Across
        	IBlockState biomeLogHorAcrossState = StructureVillageVN.getHorizontalPillarState(biomeLogVertState, this.coordBaseMode.getHorizontalIndex(), true); // Perpendicular to you
            for(int[] uuvvww : new int[][]{
            	{3,2,7, 5,2,7}, 
            	{7,4,1, 8,4,1}, 
            	{7,4,7, 8,4,7}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeLogHorAcrossState, biomeLogHorAcrossState, false);	
            }
            
            
            // Fences
        	IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs); 
            for(int[] uuvvww : new int[][]{
            	{0,1,1, 0,3,1}, {4,1,1, 4,3,1}, 
            	})
            {
            	this.fillWithBlocks(world, structureBB, uuvvww[0], uuvvww[1], uuvvww[2], uuvvww[3], uuvvww[4], uuvvww[5], biomeFenceState, biomeFenceState, false);	
            }
            
            
            // Windows
            for (int[] uvw : new int[][]{
            	{4,2,7}, {9,2,3}, {9,2,5}, 
        		})
            {
            	this.setBlockState(world, Blocks.glass_pane.getStateFromMeta(0), uvw[0], uvw[1], uvw[2], structureBB);
            }
        	
        	
            // Wood stairs
        	IBlockState biomeWoodStairsState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_stairs.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Roof
        		{5,5,0, 5,5,8, 0}, 
        		{6,6,0, 6,6,8, 0}, 
        		{7,7,0, 7,7,8, 0}, 
        		{8,7,0, 8,7,8, 1}, 
        		{9,6,0, 9,6,8, 1}, 
        		{10,5,0, 10,5,8, 1}, 
        		// Roof trim
        		{6,5,0, 6,5,0, 5}, {9,5,0, 9,5,0, 4}, {7,6,0, 7,6,0, 5}, {8,6,0, 8,6,0, 4}, 
        		{6,5,8, 6,5,8, 5}, {9,5,8, 9,5,8, 4}, {7,6,8, 7,6,8, 5}, {8,6,8, 8,6,8, 4}, 
        		// Chairs
        		{7,1,6, 7,1,6, 3}, {8,1,5, 8,1,5, 0}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), biomeWoodStairsState.getBlock().getStateFromMeta(this.getMetadataWithOffset(Blocks.oak_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);	
            }
        	
        	
            // Tables: fence with pressure plate
        	IBlockState biomeWoodPressurePlateState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.wooden_pressure_plate.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	for (int[] uvw : new int[][]{
    			{7,1,5},
        		})
            {
        		this.setBlockState(world, biomeFenceState, uvw[0], uvw[1], uvw[2], structureBB);
        		this.setBlockState(world, biomeWoodPressurePlateState, uvw[0], uvw[1]+1, uvw[2], structureBB);
            }
        	
        	
            // Stone stairs
        	for (int[] uuvvwwo : new int[][]{ // Orientation - 0: leftward, 1: rightward, 3:backward, 2:forward
        		// Front steps
        		{1,0,0, 3,0,0, 3}, 
        		})
            {
            	this.fillWithBlocks(world, structureBB, uuvvwwo[0], uuvvwwo[1], uuvvwwo[2], uuvvwwo[3], uuvvwwo[4], uuvvwwo[5], Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), Blocks.stone_stairs.getStateFromMeta(this.getMetadataWithOffset(Blocks.stone_stairs, uuvvwwo[6]%4)+(uuvvwwo[6]/4)*4), false);	
            }
            
            
            // Grindstone
        	for (int[] uvwo : new int[][]{
        		{1,1,2, 2}, // 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
        		})
            {
        		// Generate the blockObject here so that we have the correct meta on hand
        		IBlockState biomeGrindstoneState = ModObjects.chooseModGrindstone(uvwo[3], this.coordBaseMode);
            	
        		this.setBlockState(world, biomeGrindstoneState, uvwo[0], uvwo[1], uvwo[2], structureBB);
            }
        	
        	
        	// Furnaces
            for (int[] uvwo : new int[][]{
            	{3,2,4, 2}, 
            	{3,3,4, 2}, 
            	})
            {
                //this.setBlockState(world, Blocks.furnace, 0, uvwo[0], uvwo[1], uvwo[2], structureBB);
                world.setBlockState(new BlockPos(this.getXWithOffset(uvwo[0], uvwo[2]), this.getYWithOffset(uvwo[1]), this.getZWithOffset(uvwo[0], uvwo[2])), Blocks.furnace.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(uvwo[3], this.coordBaseMode)), 2);
            }
            
            
            // Chest
        	// https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/modification-development/1431724-forge-custom-chest-loot-generation
            int chestU = 4;
        	int chestV = 1;
        	int chestW = 6;
        	int chestO = 1;
            BlockPos chestPos = new BlockPos(this.getXWithOffset(chestU, chestW), this.getYWithOffset(chestV), this.getZWithOffset(chestU, chestW));
        	world.setBlockState(chestPos, Blocks.chest.getStateFromMeta(StructureVillageVN.chooseFurnaceMeta(chestO, this.coordBaseMode)), 2);
        	TileEntity te = world.getTileEntity(chestPos);
        	if (te instanceof IInventory)
        	{
            	ChestGenHooks chestGenHook = ChestGenHooks.getInfo("vn_weaponsmith");
            	WeightedRandomChestContent.generateChestContents(random, chestGenHook.getItems(random), (TileEntityChest)te, chestGenHook.getCount(random));
        	}
        	
            
            // Doors
        	IBlockState biomeWoodDoorState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_door.getStateFromMeta(0), this.materialType, this.biome, this.disallowModSubs);
            for (int[] uvwoor : new int[][]{ // u, v, w, orientation, isShut (1/0 for true/false), isRightHanded (1/0 for true/false)
            	{6,1,2, 3, 1, 0}, 
            })
            {
            	for (int height=0; height<=1; height++)
            	{
            		this.setBlockState(world, biomeWoodDoorState.getBlock().getStateFromMeta(StructureVillageVN.getDoorMetas(uvwoor[3], this.coordBaseMode, uvwoor[4]==1, uvwoor[5]==1)[height]),
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
        	
            
    		// Villagers
            if (!this.entitiesGenerated)
            {
            	this.entitiesGenerated=true;
            	
            	// Villager
            	int u;
            	int v = 1;
            	int w;
            	
            	if (random.nextInt(14)<8)
            	{ // Outside
            		u = 2 + random.nextInt(4);
            		w = 2 + random.nextInt(2);
            	}
            	else
            	{ // Inside
            		u = 7 + random.nextInt(2);
            		w = 2 + random.nextInt(3);
            	}
            	
            	// Put in the section by the door
            	if (u > 9)
            	{
            		u-=2; w-=2;
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
        protected int func_180779_c(int villagersSpawnedIn, int currentVillagerProfession) {return 3;}
    }
    
    
    
    // ------------------ //
    // --- Road Decor --- //
    // ------------------ //
    
    
    // --- Road Decor --- //
    
    public static class PlainsStreetDecor1 extends StructureVillagePieces.Village
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
    	public static final int STRUCTURE_DEPTH = 6;
    	public static final int STRUCTURE_HEIGHT = 4;
    	// Values for lining things up
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private static final int INCREASE_MIN_U = 0;
    	private static final int DECREASE_MAX_U = 0;
    	private static final int INCREASE_MIN_W = 0;
    	private static final int DECREASE_MAX_W = 0;
    	
    	private int averageGroundLevel = -1;
    	
        public PlainsStreetDecor1() {}

        public PlainsStreetDecor1(StartVN start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing coordBaseMode)
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

        public static PlainsStreetDecor1 buildComponent(StartVN villagePiece, List pieces, Random random, int x, int y, int z, EnumFacing coordBaseMode, int componentType)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, STRUCTURE_WIDTH, STRUCTURE_HEIGHT, STRUCTURE_DEPTH, coordBaseMode);
            
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new PlainsStreetDecor1(villagePiece, componentType, random, structureboundingbox, coordBaseMode) : null;
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
            				true, (byte)1, this.coordBaseMode.getHorizontalIndex());
            		
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
            			new    int[]{-2,1,2,3,4}, // Values
            			new double[]{ 1,6,6,4,2}, // Weights
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
    
    
    // ------------------- //
    // --- Biome Decor --- //
    // ------------------- //
    
    
    
    
	/**
	 * Returns a list of blocks and coordinates used to construct a decor piece
	 */
    public static ArrayList<BlueprintData> getRandomPlainsDecorBlueprint(MaterialType materialType, boolean disallowModSubs, BiomeGenBase biome, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		int decorCount = 1;
		return getPlainsDecorBlueprint(random.nextInt(decorCount), materialType, disallowModSubs, biome, coordBaseMode, random);//, townColor);
	}
	public static ArrayList<BlueprintData> getPlainsDecorBlueprint(int decorType, MaterialType materialType, boolean disallowModSubs, BiomeGenBase biome, EnumFacing coordBaseMode, Random random)//, int townColor)
	{
		ArrayList<BlueprintData> blueprint = new ArrayList(); // The blueprint to export
		
		
		// Generate per-material blocks
		
		IBlockState biomeFenceState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.oak_fence.getDefaultState(), materialType, biome, disallowModSubs);
    	IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.log.getStateFromMeta(0), materialType, biome, disallowModSubs);
    	
    	// For stripped wood specifically
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
    			BlueprintData.addPlaceBlock(blueprint, lamp_uwm[0], 3, lamp_uwm[1], Blocks.torch.getStateFromMeta(StructureVillageVN.getTorchRotationMeta(lamp_uwm[2], coordBaseMode.getHorizontalIndex())));
    		}
    		
    		break;
        }
        
        // Return the decor blueprint
        return blueprint;
	}
}
