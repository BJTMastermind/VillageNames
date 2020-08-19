package astrotibs.villagenames.village;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.ieep.ExtendedVillager;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.nbt.VNWorldDataStructure;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.FunctionsVN.MaterialType;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.village.biomestructures.BlueprintData;
import astrotibs.villagenames.village.biomestructures.DesertStructures;
import astrotibs.villagenames.village.biomestructures.PlainsStructures;
import astrotibs.villagenames.village.biomestructures.SavannaStructures;
import astrotibs.villagenames.village.biomestructures.SnowyStructures;
import astrotibs.villagenames.village.biomestructures.TaigaStructures;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.Direction;
import net.minecraft.util.Facing;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.MapGenStructureData;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;

/**
 * This class gives me better control over deactivating legacy buildings
 * @author AstroTibs
 */
public class StructureVillageVN
{
	// Array of meta values for furnaces indexed by [orientation][horizIndex]
	public static final int[][] FURNACE_META_ARRAY = new int[][]{
		{3,4,2,5},
		{5,3,5,3},
		{2,5,3,4},
		{4,2,4,2},
	};
	
	// Array of meta values for buyttons indexed by [orientation][horizIndex]
	public static final int[][] BUTTON_META_ARRAY = new int[][]{
		{3,2,4,1},
		{1,3,1,3},
		{4,1,3,2},
		{2,4,2,4},
	};
	
	// Array of meta values for furnaces indexed by [orientation][horizIndex]
	public static final int[][] ANVIL_META_ARRAY = new int[][]{
		{1,2,3,0},
		{0,1,0,1},
		{3,0,1,2},
		{2,3,2,3},
	};
	
	// Array of meta values for furnaces indexed by [orientation][horizIndex]
	public static final int[][] GLAZED_TERRACOTTA_META_ARRAY = new int[][]{
		{1,2,2,3},
		{0,1,3,0},
		{3,0,0,1},
		{2,3,1,2},
	};
	
	// Array of meta values for door indexed by [isLower][isRightHanded][isShut][orientation][horizIndex]
	public static final int[][][][][] DOOR_META_ARRAY = new int[][][][][]
	{
		// --- UPPER HALF --- //
		// Left-hand
		{{{ // Open
		{8,8,9,9},
		{8,8,9,9},
		{8,8,9,9},
		{8,8,9,9}
		},
		{ // Shut
		{8,8,9,9},
		{8,8,9,9},
		{8,8,9,9},
		{8,8,9,9}
		}},
		
		// Right-hand
		{{ // Open
		{9,9,8,8},
		{9,9,8,8},
		{9,9,8,8},
		{9,9,8,8}
		},
		{ // Shut
		{9,9,8,8},
		{9,9,8,8},
		{9,9,8,8},
		{9,9,8,8}
		}}},
		
		// --- LOWER HALF --- //
		// Left-hand
		{{{ // Open
		{7,4,5,6},
		{6,7,6,7},
		{5,6,7,4},
		{4,5,4,5}
		},
		{ // Shut
		{3,0,1,2},
		{2,3,2,3},
		{1,2,3,0},
		{0,1,0,1}
		}},
		
		// Right-hand
		{{ // Open
		{7,4,5,6},
		{6,7,6,7},
		{5,6,7,4},
		{4,5,4,5}
		},
		{ // Shut
		{3,0,1,2},
		{2,3,2,3},
		{1,2,3,0},
		{0,1,0,1}
		}}}
	};
	
	
    public static List getStructureVillageWeightedPieceList(Random random, int villageSize, FunctionsVN.VillageType villageType)
    {
    	ArrayList arraylist = new ArrayList();
        
    	// Legacy structures
        if (GeneralConfig.componentLegacyHouse4Garden_vals.get(0)>0) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House4Garden.class, GeneralConfig.componentLegacyHouse4Garden_vals.get(0), MathHelper.getRandomIntegerInRange(random, villageSize * GeneralConfig.componentLegacyHouse4Garden_vals.get(1) + GeneralConfig.componentLegacyHouse4Garden_vals.get(2), villageSize * GeneralConfig.componentLegacyHouse4Garden_vals.get(3) + GeneralConfig.componentLegacyHouse4Garden_vals.get(4))));}
        if (GeneralConfig.componentLegacyChurch_vals.get(0)>0) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Church.class, GeneralConfig.componentLegacyChurch_vals.get(0), MathHelper.getRandomIntegerInRange(random, villageSize * GeneralConfig.componentLegacyChurch_vals.get(1) + GeneralConfig.componentLegacyChurch_vals.get(2), villageSize * GeneralConfig.componentLegacyChurch_vals.get(3) + GeneralConfig.componentLegacyChurch_vals.get(4))));}
        if (GeneralConfig.componentLegacyHouse1_vals.get(0)>0) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House1.class, GeneralConfig.componentLegacyHouse1_vals.get(0), MathHelper.getRandomIntegerInRange(random, villageSize * GeneralConfig.componentLegacyHouse1_vals.get(1) + GeneralConfig.componentLegacyHouse1_vals.get(2), villageSize * GeneralConfig.componentLegacyHouse1_vals.get(3) + GeneralConfig.componentLegacyHouse1_vals.get(4))));}
        if (GeneralConfig.componentLegacyWoodHut_vals.get(0)>0) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.WoodHut.class, GeneralConfig.componentLegacyWoodHut_vals.get(0), MathHelper.getRandomIntegerInRange(random, villageSize * GeneralConfig.componentLegacyWoodHut_vals.get(1) + GeneralConfig.componentLegacyWoodHut_vals.get(2), villageSize * GeneralConfig.componentLegacyWoodHut_vals.get(3) + GeneralConfig.componentLegacyWoodHut_vals.get(4))));}
        if (GeneralConfig.componentLegacyHall_vals.get(0)>0) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Hall.class, GeneralConfig.componentLegacyHall_vals.get(0), MathHelper.getRandomIntegerInRange(random, villageSize * GeneralConfig.componentLegacyHall_vals.get(1) + GeneralConfig.componentLegacyHall_vals.get(2), villageSize * GeneralConfig.componentLegacyHall_vals.get(3) + GeneralConfig.componentLegacyHall_vals.get(4))));}
        if (GeneralConfig.componentLegacyField1_vals.get(0)>0) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field1.class, GeneralConfig.componentLegacyField1_vals.get(0), MathHelper.getRandomIntegerInRange(random, villageSize * GeneralConfig.componentLegacyField1_vals.get(1) + GeneralConfig.componentLegacyField1_vals.get(2), villageSize * GeneralConfig.componentLegacyField1_vals.get(3) + GeneralConfig.componentLegacyField1_vals.get(4))));}
        if (GeneralConfig.componentLegacyField2_vals.get(0)>0) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field2.class, GeneralConfig.componentLegacyField2_vals.get(0), MathHelper.getRandomIntegerInRange(random, villageSize * GeneralConfig.componentLegacyField1_vals.get(1) + GeneralConfig.componentLegacyField1_vals.get(2), villageSize * GeneralConfig.componentLegacyField1_vals.get(3) + GeneralConfig.componentLegacyField1_vals.get(4))));}
        if (GeneralConfig.componentLegacyHouse2_vals.get(0)>0) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House2.class, GeneralConfig.componentLegacyHouse2_vals.get(0), MathHelper.getRandomIntegerInRange(random, villageSize * GeneralConfig.componentLegacyHouse2_vals.get(1) + GeneralConfig.componentLegacyHouse2_vals.get(2), villageSize * GeneralConfig.componentLegacyHouse2_vals.get(3) + GeneralConfig.componentLegacyHouse2_vals.get(4))));}
        if (GeneralConfig.componentLegacyHouse3_vals.get(0)>0) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House3.class, GeneralConfig.componentLegacyHouse3_vals.get(0), MathHelper.getRandomIntegerInRange(random, villageSize * GeneralConfig.componentLegacyHouse3_vals.get(1) + GeneralConfig.componentLegacyHouse3_vals.get(2), villageSize * GeneralConfig.componentLegacyHouse3_vals.get(3) + GeneralConfig.componentLegacyHouse3_vals.get(4))));}
        
        VillagerRegistry.addExtraVillageComponents(arraylist, random, villageSize);
        
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext())
        {
        	PieceWeight pw = (StructureVillagePieces.PieceWeight)iterator.next();
        	
        	// Remove all buildings that rolled 0 for number or which have a weight of 0
            if (pw.villagePiecesLimit == 0 || pw.villagePieceWeight <=0) {iterator.remove(); continue;}
            /*
            // Remove buildings that aren't appropriate for the current biome
            if (villageType!=FunctionsVN.VillageType.PLAINS)
            {
            	if (
            			   pw.villagePieceClass==PlainsStructures.PlainsAccessory1.class
               			|| pw.villagePieceClass==PlainsStructures.PlainsArmorerHouse1.class
            			)
            	{
            		iterator.remove(); continue;
            	}
            }
            if (villageType!=FunctionsVN.VillageType.DESERT)
            {
            	if (
         			   pw.villagePieceClass==DesertStructures.DesertWeaponsmith1.class
         			)
	         	{
	         		iterator.remove(); continue;
	         	}
            }
            if (villageType!=FunctionsVN.VillageType.TAIGA)
            {
            	
            }
            if (villageType!=FunctionsVN.VillageType.SAVANNA)
            {
            	
            }
            if (villageType!=FunctionsVN.VillageType.SNOWY)
            {
            	
            }
            */
        }
        
        return arraylist;
    }
    
    
    // Pasted in from StructureVillagePieces so that I can access it, particularly to expand the allowed village biomes
    // This prepares a new path component to build upon
    public static StructureComponent getNextVillageStructureComponent(StartVN start, List components, Random random, int x, int y, int z, int coordBaseMode, int componentType)
    {
        if (componentType > 50)
        {
            return null;
        }
        else if (Math.abs(x - start.getBoundingBox().minX) <= 112 && Math.abs(z - start.getBoundingBox().minZ) <= 112)
        {
			// Attach another structure
			Method getNextVillageComponent_reflected = ReflectionHelper.findMethod(
					StructureVillagePieces.class, null, new String[]{"getNextVillageComponent", "func_75081_c"},
					StructureVillagePieces.Start.class, List.class, Random.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE
					);
			
			//StructureVillagePieces.Village structurecomponent = StructureVillagePieces.getNextVillageComponent(start, components, random, x, y, z, coordBaseMode, componentType + 1);
			StructureVillagePieces.Village structurecomponent = null;
			try {structurecomponent = (StructureVillagePieces.Village)getNextVillageComponent_reflected.invoke(start, (StructureVillagePieces.Start)start, components, random, x, y, z, coordBaseMode, componentType + 1);}
    		catch (Exception e) {if (GeneralConfig.debugMessages) LogHelper.warn("Could not invoke StructureVillagePieces.getNextVillageComponent method");}
			
            if (structurecomponent != null)
            {
            	// Substitute old torch with the new one
            	if (structurecomponent instanceof StructureVillagePieces.Torch)
            	{
            		StructureBoundingBox decorTorchBB = StructureVillageVN.DecorTorch.findPieceBox(start, components, random, x, y, z, coordBaseMode);
            		if (decorTorchBB==null) {return null;}
            		structurecomponent = new StructureVillageVN.DecorTorch(start, componentType, random, decorTorchBB, coordBaseMode);
            	}
            	
                int medianX = (structurecomponent.getBoundingBox().minX + structurecomponent.getBoundingBox().maxX) / 2;
                int medianZ = (structurecomponent.getBoundingBox().minZ + structurecomponent.getBoundingBox().maxZ) / 2;
                int rangeX = structurecomponent.getBoundingBox().maxX - structurecomponent.getBoundingBox().minX;
                int rangeZ = structurecomponent.getBoundingBox().maxZ - structurecomponent.getBoundingBox().minZ;
                int bboxWidth = rangeX > rangeZ ? rangeX : rangeZ;
                
                // Replaces the ordinary "areBiomesViable" method with one that uses the VN biome config list
                BiomeGenBase biome = start.getWorldChunkManager().getBiomeGenAt(medianX, medianZ);
            	
            	if (GeneralConfig.spawnBiomesNames != null) // Biome list is not empty
        		{
        			for (int i = 0; i < GeneralConfig.spawnBiomesNames.length; i++)
        			{
        				if (GeneralConfig.spawnBiomesNames[i].equals(biome.biomeName))
        				{
        					BiomeManager.addVillageBiome(biome, true); // Set biome to be able to spawn villages
        					
        					components.add(structurecomponent);
                            start.field_74932_i.add(structurecomponent);
                            return structurecomponent;
        				}
        			}
        		}
            	/*
                if (start.getWorldChunkManager().areBiomesViable(medianX, medianZ, bboxWidth / 2 + 4, MapGenVillage.villageSpawnBiomes))
                {
                    components.add(village);
                    start.field_74932_i.add(village);
                    return village;
                }
                */
            }

            return null;
        }
        else
        {
            return null;
        }
    }
    
    
    // Pasted in from StructureVillagePieces so that I can access it, particularly to expand the allowed village biomes
    // This prepares a new path component to build upon
    public static StructureComponent getRandomVillageRoadComponent(StartVN start, List components, Random random, int x, int y, int z, int coordBaseMode, int componentType)
    {
        if (componentType > 50)
        {
            return null;
        }
        else if (Math.abs(x - start.getBoundingBox().minX) <= 112 && Math.abs(z - start.getBoundingBox().minZ) <= 112)
        {
        	// Initialize some variables
        	StructureBoundingBox structureboundingbox;
        	StructureVillagePieces.Village structurecomponent;
        	
        	// Attach a Church to the end
        	structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 5, 12, 9, coordBaseMode);
			structurecomponent = (structureboundingbox != null && structureboundingbox.minY > 10 && StructureComponent.findIntersecting(components, structureboundingbox) == null) ? new StructureVillagePieces.Church(start, componentType, random, structureboundingbox, coordBaseMode) : null;
            
            if (structurecomponent != null)
            {
            	int medianX = (structurecomponent.getBoundingBox().minX + structurecomponent.getBoundingBox().maxX) / 2;
                int medianZ = (structurecomponent.getBoundingBox().minZ + structurecomponent.getBoundingBox().maxZ) / 2;
                int rangeX = structurecomponent.getBoundingBox().maxX - structurecomponent.getBoundingBox().minX;
                int rangeZ = structurecomponent.getBoundingBox().maxZ - structurecomponent.getBoundingBox().minZ;
                int bboxWidth = rangeX > rangeZ ? rangeX : rangeZ;
                
                // Replaces the ordinary "areBiomesViable" method with one that uses the VN biome config list
                BiomeGenBase biome = start.getWorldChunkManager().getBiomeGenAt(medianX, medianZ);
            	
            	if (GeneralConfig.spawnBiomesNames != null) // Biome list is not empty
        		{
        			for (int i = 0; i < GeneralConfig.spawnBiomesNames.length; i++)
        			{
        				if (GeneralConfig.spawnBiomesNames[i].equals(biome.biomeName))
        				{
        					BiomeManager.addVillageBiome(biome, true); // Set biome to be able to spawn villages
        					
        					components.add(structurecomponent);
                            start.field_74932_i.add(structurecomponent);
                            return structurecomponent;
        				}
        			}
        		}
            }

            return null;
        }
        else
        {
            return null;
        }
    }
    
    
    /**
     * Pasted in from StructureVillagePieces.Path findPieceBox so that I can access it
     */
    public static StructureBoundingBox findPieceBox(StructureVillagePieces.Start start, List<StructureComponent> components, Random random, int x, int y, int z, int horizIndex)
    {
    	// Select a length for the road. Start with a random length of 21, 28, or 35,
    	// and then decrement 7 blocks at a time until the road doesn't intersect with any generated components
        for (int i = 7 * MathHelper.getRandomIntegerInRange(random, 3, 5); i >= 7; i -= 7)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
            		x, y, z, // Structure coordinates
            		0, 0, 0, // u, v, w offset. The u and w offsets are positive for horizIndex <=1 and negative for horizIndex >=2
            		3, 3, i, // width, height, depth
            		horizIndex);
            
            if (StructureComponent.findIntersecting(components, structureboundingbox) == null)
            {
                return structureboundingbox;
            }
        }
        
        return null;
    }
    
    
    // Pasted in from StructureVillagePieces so that I can access it
    // This prepares a new path component to build upon
    public static StructureComponent generateAndAddRoadPiece(StructureVillagePieces.Start start, List components, Random random, int x, int y, int z, int horizIndex, int componentType)
    {
        if (componentType > 3 + start.terrainType) {return null;} // Idk what this is
        else if (Math.abs(x - start.getBoundingBox().minX) <= 112 && Math.abs(z - start.getBoundingBox().minZ) <= 112) // Village bb can't be larger than 112x112
        {
        	StructureBoundingBox structureboundingbox = findPieceBox(start, components, random, x, y, z, horizIndex);
        	
            if (structureboundingbox != null && structureboundingbox.minY > 10)
            {
                StructureVillageVN.PathVN path = new StructureVillageVN.PathVN(start, componentType, random, structureboundingbox, horizIndex);
                int medianX = (path.getBoundingBox().minX + path.getBoundingBox().maxX) / 2;
                int medianZ = (path.getBoundingBox().minZ + path.getBoundingBox().maxZ) / 2;
                int rangeX = path.getBoundingBox().maxX - path.getBoundingBox().minX;
                int rangeZ = path.getBoundingBox().maxZ - path.getBoundingBox().minZ;
                int bboxWidth = rangeX > rangeZ ? rangeX : rangeZ;
                
                // Replaces the ordinary "areBiomesViable" method with one that uses the VN biome config list
                BiomeGenBase biome = start.getWorldChunkManager().getBiomeGenAt(medianX, medianZ);
            	
            	if (GeneralConfig.spawnBiomesNames != null) // Biome list is not empty
        		{
        			for (int i = 0; i < GeneralConfig.spawnBiomesNames.length; i++)
        			{
        				if (GeneralConfig.spawnBiomesNames[i].equals(biome.biomeName))
        				{
        					BiomeManager.addVillageBiome(biome, true); // Set biome to be able to spawn villages
        					
        					components.add(path);
                            start.field_74930_j.add(path);
                            return path;
        				}
        			}
        		}
            	
            }
            
            return null;
        }
        else
        {
            return null;
        }
    }
    
    
    /**
     * Returns the space above the topmost block that is solid or liquid. Does not count leaves or other foliage
     */
    public static int getAboveTopmostSolidOrLiquidBlockVN(World world, int posX, int posZ)
    {
        Chunk chunk = world.getChunkFromBlockCoords(posX, posZ);
        int x = posX;
        int z = posZ;
        int k = chunk.getTopFilledSegment() + 15;
        posX &= 15;
        
        // Search downward unti you hit the first block that meets the "solid/liquid" requirement
        for (posZ &= 15; k > 0; --k)
        {
            Block block = chunk.getBlock(posX, k, posZ);
            
            if (
            		// If it's a solid, full block that isn't one of these particular types
            		(block.getMaterial().blocksMovement()
            		&& block.getMaterial() != Material.leaves
    				&& block.getMaterial() != Material.plants
					&& block.getMaterial() != Material.vine
					&& block.getMaterial() != Material.air
            		&& !block.isFoliage(world, x, k, z)
            		&& block.isOpaqueCube())
            		// If the block is liquid, return the value above it
            		|| block.getMaterial().isLiquid()
            		)
            {
                return k + 1;
            }
        }
        
        return -1;
    }
    
    
    /**
     * Discover the y coordinate that will serve as the ground level of the supplied BoundingBox.
     * (An ACTUAL median of all the levels in the BB's horizontal rectangle).
     * 
     * Use outlineOnly if you'd like to tally only the boundary values.
     * 
     * If outlineOnly is true, use sideFlag to specify which boundaries:
     * +1: front
     * +2: left (wrt coordbase 0 or 1)
     * +4: back
     * +8: right (wrt coordbase 0 or 1)
     * 
     * horizIndex is the integer that represents the orientation of the structure.
     */
    public static int getMedianGroundLevel(World world, StructureBoundingBox boundingBox, boolean outlineOnly, byte sideFlag, int horizIndex)
    {
    	ArrayList<Integer> i = new ArrayList<Integer>();
    	
        for (int k = boundingBox.minZ; k <= boundingBox.maxZ; ++k)
        {
            for (int l = boundingBox.minX; l <= boundingBox.maxX; ++l)
            {
                if (boundingBox.isVecInside(l, 64, k))
                {
                	if (!outlineOnly || (outlineOnly &&
                			(
                					(k==boundingBox.minZ && (sideFlag&(new int[]{1,2,4,2}[horizIndex]))>0) ||
                					(k==boundingBox.maxZ && (sideFlag&(new int[]{4,8,1,8}[horizIndex]))>0) ||
                					(l==boundingBox.minX && (sideFlag&(new int[]{2,4,2,1}[horizIndex]))>0) ||
                					(l==boundingBox.maxX && (sideFlag&(new int[]{8,1,8,4}[horizIndex]))>0) ||
                					false
                					)
                			))
                	{
                		int aboveTopLevel = getAboveTopmostSolidOrLiquidBlockVN(world, l, k);
                		if (aboveTopLevel != -1) {i.add(aboveTopLevel);}
                		//if (GeneralConfig.debugMessages) {LogHelper.info("Position [" + l + ", " + k + "] sideFlag: " + sideFlag + ", horizIndex: " + horizIndex);}
                	}
                }
            }
        }
        //if (GeneralConfig.debugMessages) {LogHelper.info("Ground height array for [" + boundingBox.minX + ", " + boundingBox.minZ + "] to [" + boundingBox.maxX + ", " + boundingBox.maxZ + "]: " + i);}
        return FunctionsVN.medianIntArray(i, true);
    }
    
    
    /**
     * Biome-specific block replacement
     */
    //public static Object[] getBiomeSpecificBlock(Block block, int meta, StructureVillageVN.StartVN startPiece)
    public static Object[] getBiomeSpecificBlock(Block block, int meta, MaterialType materialType, BiomeGenBase biome)
    {
    	if (materialType==null || biome==null) {return new Object[]{block, meta};}
    	
    	// TODO - use vanilla fences and gates in 1.8
    	if (materialType == FunctionsVN.MaterialType.OAK)
        {
        	if (block == Blocks.sandstone && meta==2)          {return new Object[]{Blocks.stonebrick, 0};} // Cut sandstone into stone brick
        	if (block == Blocks.sandstone && meta==1)          {return new Object[]{Blocks.stonebrick, 3};} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.sandstone)                     {return new Object[]{Blocks.cobblestone, 0};}
        	if (block == Blocks.stone_slab)                    {return new Object[]{Blocks.stone_slab, meta==1? 3: meta==9? 11 : meta};}
        	if (block == Blocks.double_stone_slab)             {return new Object[]{Blocks.double_stone_slab, meta==1? 0 : meta};}
        	if (block == Blocks.sandstone_stairs)              {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneStairsUTD)) {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabUTD)) {return new Object[]{Blocks.stone_slab, meta==8 ? 11: meta==0? 3:meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneUTD)) {return new Object[]{Blocks.cobblestone, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.sandstoneWallUTD)) {return new Object[]{Blocks.cobblestone_wall, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.wallRC) && meta==11) {return new Object[]{Blocks.cobblestone_wall, 0};}
        }
    	if (materialType == FunctionsVN.MaterialType.SPRUCE)
        {
        	if (block == Blocks.log || block == Blocks.log2)   {return new Object[]{Blocks.log, (meta/4)*4 + 1};}
        	if (block == Blocks.planks)                        {return new Object[]{Blocks.planks, 1};}
        	if (block == Blocks.fence)                         {return new Object[]{ModObjects.chooseModFence(1), 0};}
        	if (block == Blocks.fence_gate)                    {return new Object[]{ModObjects.chooseModFenceGate(1), 0};}
        	if (block == Blocks.oak_stairs)                    {return new Object[]{Blocks.spruce_stairs, meta};}
        	if (block == Blocks.wooden_slab)                   {return new Object[]{Blocks.wooden_slab, meta==0? 0 +1: meta==8? 8 +1 : meta};}
        	if (block == Blocks.double_wooden_slab)            {return new Object[]{Blocks.double_wooden_slab, 1};}
        	if (block == Blocks.wooden_door)                   {return new Object[]{chooseWoodenDoor(1), meta};}
        	if (block == Blocks.wooden_button)                 {return new Object[]{chooseWoodenButton(1), meta};}
        	if (block == Blocks.wooden_pressure_plate)         {return new Object[]{ModObjects.chooseModPressurePlate(1), 0};}
        	if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(1), meta};}
        	if (block == Blocks.ladder)                        {return new Object[]{ModObjects.chooseModLadderBlock(1), meta};}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(1, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(1, false), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 1};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogSpruceUTD), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 1};}
        	if (block == Blocks.sandstone && meta==2)          {return new Object[]{Blocks.stonebrick, 0};} // Cut sandstone into stone brick
        	if (block == Blocks.sandstone && meta==1)          {return new Object[]{Blocks.stonebrick, 3};} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.sandstone)                     {return new Object[]{Blocks.cobblestone, 0};}
        	if (block == Blocks.stone_slab)                    {return new Object[]{Blocks.stone_slab, meta==1? 3: meta==9? 11 : meta};}
        	if (block == Blocks.double_stone_slab)             {return new Object[]{Blocks.double_stone_slab, meta==1? 0 : meta};}
        	if (block == Blocks.sandstone_stairs)              {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneStairsUTD)) {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabUTD)) {return new Object[]{Blocks.stone_slab, meta==8 ? 11: meta==0? 3:meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneUTD)) {return new Object[]{Blocks.cobblestone, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.sandstoneWallUTD)) {return new Object[]{Blocks.cobblestone_wall, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.wallRC) && meta==11) {return new Object[]{Blocks.cobblestone_wall, 0};}
        }
        if (materialType == FunctionsVN.MaterialType.BIRCH)
        {
        	if (block == Blocks.log || block == Blocks.log2)   {return new Object[]{Blocks.log, (meta/4)*4 + 2};}
        	if (block == Blocks.planks)                        {return new Object[]{Blocks.planks, 2};}
        	if (block == Blocks.fence)                         {return new Object[]{ModObjects.chooseModFence(2), 0};}
        	if (block == Blocks.fence_gate)                    {return new Object[]{ModObjects.chooseModFenceGate(2), 0};}
        	if (block == Blocks.oak_stairs)                    {return new Object[]{Blocks.birch_stairs, meta};}
        	if (block == Blocks.wooden_slab)                   {return new Object[]{Blocks.wooden_slab, meta==0? 0 +2: meta==8? 8 +2 : meta};}
        	if (block == Blocks.double_wooden_slab)            {return new Object[]{Blocks.double_wooden_slab, 2};}
        	if (block == Blocks.wooden_door)                   {return new Object[]{chooseWoodenDoor(2), meta};}
        	if (block == Blocks.wooden_button)                 {return new Object[]{chooseWoodenButton(2), meta};}
        	if (block == Blocks.wooden_pressure_plate)         {return new Object[]{ModObjects.chooseModPressurePlate(2), 0};}
        	if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(2), meta};}
        	if (block == Blocks.ladder)                        {return new Object[]{ModObjects.chooseModLadderBlock(2), meta};}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(2, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(2, false), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 2};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogBirchUTD), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 2};}
        	if (block == Blocks.sandstone && meta==2)          {return new Object[]{Blocks.stonebrick, 0};} // Cut sandstone into stone brick
        	if (block == Blocks.sandstone && meta==1)          {return new Object[]{Blocks.stonebrick, 3};} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.sandstone)                     {return new Object[]{Blocks.cobblestone, 0};}
        	if (block == Blocks.stone_slab)                    {return new Object[]{Blocks.stone_slab, meta==1? 3: meta==9? 11 : meta};}
        	if (block == Blocks.double_stone_slab)             {return new Object[]{Blocks.double_stone_slab, meta==1? 0 : meta};}
        	if (block == Blocks.sandstone_stairs)              {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneStairsUTD)) {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabUTD)) {return new Object[]{Blocks.stone_slab, meta==8 ? 11: meta==0? 3:meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneUTD)) {return new Object[]{Blocks.cobblestone, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.sandstoneWallUTD)) {return new Object[]{Blocks.cobblestone_wall, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.wallRC) && meta==11) {return new Object[]{Blocks.cobblestone_wall, 0};}
        }
        if (materialType == FunctionsVN.MaterialType.JUNGLE)
        {
        	if (block == Blocks.log || block == Blocks.log2)   {return new Object[]{Blocks.log, (meta/4)*4 + 3};}
        	if (block == Blocks.cobblestone)                   {return new Object[]{Blocks.mossy_cobblestone, 0};}
        	if (block == Blocks.stone_stairs)                  {
													        		block = Block.getBlockFromName(ModObjects.mossyCobblestoneStairsUTD);
													        		if (block==null) {block = Blocks.stone_stairs;}
													        		return new Object[]{block, meta};
        													   } // Mossy cobblestone stairs
        	if (block == Blocks.cobblestone_wall)              {return new Object[]{block, 1};} // Mossy cobblestone wall
        	if (block == Blocks.planks)                        {return new Object[]{Blocks.planks, 3};}
        	if (block == Blocks.fence)                         {return new Object[]{ModObjects.chooseModFence(3), 0};}
        	if (block == Blocks.fence_gate)                    {return new Object[]{ModObjects.chooseModFenceGate(3), 0};}
        	if (block == Blocks.oak_stairs)                    {return new Object[]{Blocks.jungle_stairs, meta};}
        	if (block == Blocks.wooden_slab)                   {return new Object[]{Blocks.wooden_slab, meta==0? 0 +3: meta==8? 8 +3 : meta};}
        	if (block == Blocks.double_wooden_slab)            {return new Object[]{Blocks.double_wooden_slab, 3};}
        	if (block == Blocks.wooden_door)                   {return new Object[]{chooseWoodenDoor(3), meta};}
        	if (block == Blocks.wooden_button)                 {return new Object[]{chooseWoodenButton(3), meta};}
        	if (block == Blocks.wooden_pressure_plate)         {return new Object[]{ModObjects.chooseModPressurePlate(3), 0};}
        	if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(3), meta};}
        	if (block == Blocks.ladder)                        {return new Object[]{ModObjects.chooseModLadderBlock(3), meta};}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(3, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(3, false), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 3};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogJungleUTD), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 3};}
        	if (block == Blocks.sandstone && meta==2)          {return new Object[]{Blocks.stonebrick, 0};} // Cut sandstone into stone brick
        	if (block == Blocks.sandstone && meta==1)          {return new Object[]{Blocks.stonebrick, 3};} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.sandstone)                     {return new Object[]{Blocks.cobblestone, 0};}
        	if (block == Blocks.stone_slab)                    {return new Object[]{Blocks.stone_slab, meta==1? 3: meta==9? 11 : meta};}
        	if (block == Blocks.double_stone_slab)             {return new Object[]{Blocks.double_stone_slab, meta==1? 0 : meta};}
        	if (block == Blocks.sandstone_stairs)              {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneStairsUTD)) {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabUTD)) {return new Object[]{Blocks.stone_slab, meta==8 ? 11: meta==0? 3:meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneUTD)) {return new Object[]{Blocks.mossy_cobblestone, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.sandstoneWallUTD)) {return new Object[]{Blocks.cobblestone_wall, 1};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.wallRC) && meta==11) {return new Object[]{Blocks.cobblestone_wall, 0};}
        }
        if (materialType == FunctionsVN.MaterialType.ACACIA)
        {
        	if (block == Blocks.log || block == Blocks.log2)   {return new Object[]{Blocks.log2, (meta/4)*4 + 0};}
        	if (block == Blocks.planks)                        {return new Object[]{Blocks.planks, 4};}
        	if (block == Blocks.fence)                         {return new Object[]{ModObjects.chooseModFence(4), 0};}
        	if (block == Blocks.fence_gate)                    {return new Object[]{ModObjects.chooseModFenceGate(4), 0};}
        	if (block == Blocks.oak_stairs)                    {return new Object[]{Blocks.acacia_stairs, meta};}
        	if (block == Blocks.wooden_slab)                   {return new Object[]{Blocks.wooden_slab, meta==0? 0 +4: meta==8? 8 +4 : meta};}
        	if (block == Blocks.double_wooden_slab)            {return new Object[]{Blocks.double_wooden_slab, 4};}
        	if (block == Blocks.wooden_door)                   {return new Object[]{chooseWoodenDoor(4), meta};}
        	if (block == Blocks.wooden_button)                 {return new Object[]{chooseWoodenButton(4), meta};}
        	if (block == Blocks.wooden_pressure_plate)         {return new Object[]{ModObjects.chooseModPressurePlate(4), 0};}
        	if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(4), meta};}
        	if (block == Blocks.ladder)                        {return new Object[]{ModObjects.chooseModLadderBlock(4), meta};}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(4, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(4, false), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.bark2EF)) {return new Object[]{block, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogAcaciaUTD), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog2EF), 4*meta + 0};}
        	if (block == Blocks.sandstone && meta==2)          {return new Object[]{Blocks.stonebrick, 0};} // Cut sandstone into stone brick
        	if (block == Blocks.sandstone && meta==1)          {return new Object[]{Blocks.stonebrick, 3};} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.sandstone)                     {return new Object[]{Blocks.cobblestone, 0};}
        	if (block == Blocks.stone_slab)                    {return new Object[]{Blocks.stone_slab, meta==1? 3: meta==9? 11 : meta};}
        	if (block == Blocks.double_stone_slab)             {return new Object[]{Blocks.double_stone_slab, meta==1? 0 : meta};}
        	if (block == Blocks.sandstone_stairs)              {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneStairsUTD)) {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabUTD)) {return new Object[]{Blocks.stone_slab, meta==8 ? 11: meta==0? 3:meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneUTD)) {return new Object[]{Blocks.cobblestone, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.sandstoneWallUTD)) {return new Object[]{Blocks.cobblestone_wall, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.wallRC) && meta==11) {return new Object[]{Blocks.cobblestone_wall, 0};}
        }
        if (materialType == FunctionsVN.MaterialType.DARK_OAK)
        {
        	if (block == Blocks.log || block == Blocks.log2)   {return new Object[]{Blocks.log2, (meta/4)*4 + 1};}
        	if (block == Blocks.planks)                        {return new Object[]{Blocks.planks, 5};}
        	if (block == Blocks.fence)                         {return new Object[]{ModObjects.chooseModFence(5), 0};}
        	if (block == Blocks.fence_gate)                    {return new Object[]{ModObjects.chooseModFenceGate(5), 0};}
        	if (block == Blocks.oak_stairs)                    {return new Object[]{Blocks.dark_oak_stairs, meta};}
        	if (block == Blocks.wooden_slab)                   {return new Object[]{Blocks.wooden_slab, meta==0? 0 +5: meta==8? 8 +5 : meta};}
        	if (block == Blocks.double_wooden_slab)            {return new Object[]{Blocks.double_wooden_slab, 5};}
        	if (block == Blocks.wooden_door)                   {return new Object[]{chooseWoodenDoor(5), meta};}
        	if (block == Blocks.wooden_button)                 {return new Object[]{chooseWoodenButton(5), meta};}
        	if (block == Blocks.wooden_pressure_plate)         {return new Object[]{ModObjects.chooseModPressurePlate(5), 0};}
        	if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(5), meta};}
        	if (block == Blocks.ladder)                        {return new Object[]{ModObjects.chooseModLadderBlock(5), meta};}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(5, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(5, false), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.bark2EF)) {return new Object[]{block, 1};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogDarkOakUTD), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog2EF), 4*meta + 1};}
        	if (block == Blocks.sandstone && meta==2)          {return new Object[]{Blocks.stonebrick, 0};} // Cut sandstone into stone brick
        	if (block == Blocks.sandstone && meta==1)          {return new Object[]{Blocks.stonebrick, 3};} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.sandstone)                     {return new Object[]{Blocks.cobblestone, 0};}
        	if (block == Blocks.stone_slab)                    {return new Object[]{Blocks.stone_slab, meta==1? 3: meta==9? 11 : meta};}
        	if (block == Blocks.double_stone_slab)             {return new Object[]{Blocks.double_stone_slab, meta==1? 0 : meta};}
        	if (block == Blocks.sandstone_stairs)              {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneStairsUTD)) {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabUTD)) {return new Object[]{Blocks.stone_slab, meta==8 ? 11: meta==0? 3:meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneUTD)) {return new Object[]{Blocks.cobblestone, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.sandstoneWallUTD)) {return new Object[]{Blocks.cobblestone_wall, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.wallRC) && meta==11) {return new Object[]{Blocks.cobblestone_wall, 0};}
        }
        if (materialType == FunctionsVN.MaterialType.SAND)
        {
        	if (block == Blocks.log || block == Blocks.log2)   {return new Object[]{Blocks.sandstone, 2};} // Cut sandstone
        	if (block == Blocks.stonebrick && meta==0)         {return new Object[]{Blocks.sandstone, 2};} // Stone brick into cut sandstone
        	if (block == Blocks.cobblestone && meta==3)        {return new Object[]{Blocks.sandstone, 1};} // Chiseled sandstone
        	if (block == Blocks.cobblestone)                   {return new Object[]{Blocks.sandstone, 0};} // Regular sandstone
        	if (block == Blocks.mossy_cobblestone)             {return new Object[]{Blocks.sandstone, 0};} // Regular sandstone
        	if (block == Blocks.planks)                        {return new Object[]{Blocks.planks, 3};} // Jungle planks
        	if (block == Blocks.fence)                         {return new Object[]{ModObjects.chooseModFence(3), 0};} // Jungle fence
        	if (block == Blocks.fence_gate)                    {return new Object[]{ModObjects.chooseModFenceGate(3), 0};} // Jungle fence gate
        	if (block == Blocks.oak_stairs)                    {return new Object[]{Blocks.jungle_stairs, meta};}
        	if (block == Blocks.stone_stairs)                  {return new Object[]{Blocks.sandstone_stairs, meta};}
        	if (block == Blocks.cobblestone_wall)              {
													        		if (ModObjects.chooseModSandstoneWall(false)==null) {block = Blocks.sandstone;}
													        		else {return ModObjects.chooseModSandstoneWall(false);}
															   } // Sandstone wall
        	if (block == Blocks.gravel)                        {return new Object[]{Blocks.sandstone, 0};}
        	if (block == Blocks.dirt)                          {return new Object[]{Blocks.sand, 0};}
        	if (block == Blocks.grass)                         {return new Object[]{Blocks.sand, 0};}
        	if (block == Blocks.wooden_slab)                   {return new Object[]{Blocks.wooden_slab, meta==0? 0 +3: meta==8? 8 +3 : meta};} // Jungle slab
        	if (block == Blocks.double_wooden_slab)            {return new Object[]{Blocks.double_wooden_slab, 3};} // Jungle double slab
        	if (block == Blocks.wooden_door)                   {return new Object[]{chooseWoodenDoor(3), meta};} // Jungle door
        	if (block == Blocks.wooden_button)                 {return new Object[]{chooseWoodenButton(3), meta};}
        	if (block == Blocks.wooden_pressure_plate)         {return new Object[]{ModObjects.chooseModPressurePlate(3), 0};}
        	if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(3), meta};} // Jungle trapdoor
        	if (block == Blocks.ladder)                        {return new Object[]{ModObjects.chooseModLadderBlock(3), meta};}
        	if (block == Blocks.stone_slab)                    {return new Object[]{Blocks.stone_slab, meta==3? 1: meta==11? 9 : meta};} // Sandstone slab
        	//if (block == Blocks.double_stone_slab)             {return new Object[]{Blocks.double_stone_slab, 4};} // Brick double slab
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(3, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(3, false), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 3};} // Jungle bark
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogJungleUTD), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 3};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{Blocks.sandstone, 2};} // Cut sandstone
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Blocks.sandstone, 2};} // Cut sandstone
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Blocks.sandstone, 2};} // Cut sandstone
        }
        if (materialType == FunctionsVN.MaterialType.MESA)
        {
        	if (block == Blocks.cobblestone)                   {return new Object[]{Blocks.hardened_clay, 0};} // TODO - change stain color with village colors?
        	if (block == Blocks.mossy_cobblestone)             {return new Object[]{Blocks.hardened_clay, 0};}
        	if (block == Blocks.stone_stairs)                  {return new Object[]{Blocks.brick_stairs, meta};}
        	if (block == Blocks.gravel)                        {return new Object[]{Blocks.hardened_clay, 0};}
        	if (block == Blocks.dirt)                          {return new Object[]{Blocks.clay, 0};}
        	if (block == Blocks.grass)                         {return new Object[]{Blocks.clay, 0};}
        	//if (block == Blocks.stone_slab)                    {return new Object[]{Blocks.stone_slab, meta==3? 4: meta==11? 12 : meta};} // Brick slab
        	if (block == Blocks.double_stone_slab)             {return new Object[]{Blocks.double_stone_slab, 1};} // Sandstone double slab
        	if (block == Blocks.cobblestone_wall)              {
        															Object[] modobject = ModObjects.chooseModSandstoneWall(true);
													        		if (modobject==null)
													        		{
													        			block = ModObjects.chooseModRedSandstone();
													        			if (block==null) {block = Blocks.cobblestone_wall; meta=0;} // Just return what you put in
													        			return new Object[]{block, meta};
													        		}
													        		else {return modobject;}
															   } // Brick wall
        	if (block == Blocks.sandstone)                     {
        															block = ModObjects.chooseModRedSandstone();
        															if (block == null) {return new Object[]{Blocks.sandstone, meta};}
        															else {return new Object[]{block, 0};}
												        	   }
        	if (block == Blocks.stone_slab)                    {
																	Object[] modobject = ModObjects.chooseModRedSandstoneSlab(meta>=8);
																	if (modobject == null) {return new Object[]{Blocks.stone_slab, meta<8? 1: 9};}
											        		   }
        	if (block == Blocks.double_stone_slab)             {return new Object[]{Blocks.double_stone_slab, meta==0? 1 : meta};}
        	if (block == Blocks.sandstone_stairs)              {
																	block = ModObjects.chooseModRedSandstoneStairs();
																	if (block == null) {return new Object[]{Blocks.sandstone_stairs, meta};}
																	else {return new Object[]{block, meta};}
													    	   }
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneStairsUTD))
													           {
																	block = ModObjects.chooseModSmoothSandstoneStairs(true);
																	if (block == null) {return new Object[]{Block.getBlockFromName(ModObjects.smoothSandstoneStairsUTD), meta};}
																	else {return new Object[]{block, meta};}
													    	   }
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabUTD))
													           {
																	Object[] modobject = ModObjects.chooseModSmoothSandstoneSlab(meta==8, true);
																	if (modobject == null) {return new Object[]{Block.getBlockFromName(ModObjects.smoothSandstoneSlabUTD), meta};}
																	else {return modobject;}
													    	   }
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneUTD)) {return ModObjects.chooseModSmoothSandstoneBlock(true);}
        }
        if (materialType == FunctionsVN.MaterialType.SNOW)
        {
        	if (block == Blocks.log || block == Blocks.log2)   {return new Object[]{Blocks.log, (meta/4)*4 + 1};}
        	if (block == Blocks.planks)                        {return new Object[]{Blocks.planks, 1};}
        	if (block == Blocks.fence)                         {return new Object[]{ModObjects.chooseModFence(1), 0};}
        	if (block == Blocks.fence_gate)                    {return new Object[]{ModObjects.chooseModFenceGate(1), 0};}
        	if (block == Blocks.oak_stairs)                    {return new Object[]{Blocks.spruce_stairs, meta};}
        	if (block == Blocks.wooden_slab)                   {return new Object[]{Blocks.wooden_slab, meta==0? 0 +1: meta==8? 8 +1 : meta};}
        	if (block == Blocks.double_wooden_slab)            {return new Object[]{Blocks.double_wooden_slab, 1};}
        	if (block == Blocks.wooden_door)                   {return new Object[]{chooseWoodenDoor(1), meta};}
        	if (block == Blocks.wooden_button)                 {return new Object[]{chooseWoodenButton(1), meta};}
        	if (block == Blocks.wooden_pressure_plate)         {return new Object[]{ModObjects.chooseModPressurePlate(1), 0};}
        	if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(1), meta};}
        	if (block == Blocks.ladder)                        {return new Object[]{ModObjects.chooseModLadderBlock(1), meta};}
        	if (block == Blocks.cobblestone)                   {return new Object[]{Blocks.packed_ice, 0};}
        	if (block == Blocks.mossy_cobblestone)             {return new Object[]{Blocks.packed_ice, 0};}
        	if (block == Blocks.gravel)                        {return new Object[]{Blocks.packed_ice, 0};}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(1, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(1, false), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 1};} // Spruce bark
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogSpruceUTD), meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 1};}
        	if (block == Blocks.stonebrick)                    {return new Object[]{Blocks.packed_ice, 0};}
        	if (block == Blocks.sandstone)                     {return new Object[]{Blocks.packed_ice, 0};}
        	if (block == Blocks.stone_slab)                    {return new Object[]{Blocks.stone_slab, meta==1? 3: meta==9? 11 : meta};}
        	if (block == Blocks.double_stone_slab)             {return new Object[]{Blocks.double_stone_slab, meta==1? 0 : meta};}
        	if (block == Blocks.sandstone_stairs)              {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneStairsUTD)) {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabUTD)) {return new Object[]{Blocks.stone_slab, meta==8 ? 11: meta==0? 3:meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneUTD)) {return new Object[]{Blocks.packed_ice, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.sandstoneWallUTD)) {return new Object[]{Blocks.cobblestone_wall, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.wallRC) && meta==11) {return new Object[]{Blocks.cobblestone_wall, 0};}
        }
    	if (materialType == FunctionsVN.MaterialType.MUSHROOM)
        {
        	if (block == Blocks.log || block == Blocks.log2)   {return new Object[]{Blocks.brown_mushroom_block, 15};} // Stem on all six sides
        	if (block == Blocks.cobblestone)                   {return new Object[]{Blocks.brown_mushroom_block, 14};} // Cap on all six sides
        	if (block == Blocks.mossy_cobblestone)             {return new Object[]{Blocks.brown_mushroom_block, 14};} // Cap on all six sides
        	if (block == Blocks.planks)                        {return new Object[]{Blocks.brown_mushroom_block, 0};} // Pores on all six sides
        	if (block == Blocks.cobblestone && meta==3)        {return new Object[]{Blocks.brown_mushroom_block, 14};} // Chiseled stone brick into cap on all sides
        	if (block == Blocks.stonebrick)                    {return new Object[]{Blocks.brown_mushroom_block, 14};} // Cap on all six sides
        	if (block == Blocks.sandstone && meta==2)          {return new Object[]{Blocks.brown_mushroom_block, 14};} // Cap on all six sides
        	if (block == Blocks.sandstone && meta==1)          {return new Object[]{Blocks.brown_mushroom_block, 14};} // Chiseled sandstone into cap on all sides
        	if (block == Blocks.sandstone)                     {return new Object[]{Blocks.brown_mushroom_block, 14};} // Cap on all six sides
        	if (block == Blocks.stone_slab)                    {return new Object[]{Blocks.stone_slab, meta==1? 3: meta==9? 11 : meta};}
        	if (block == Blocks.double_stone_slab)             {return new Object[]{Blocks.double_stone_slab, meta==1? 0 : meta};}
        	if (block == Blocks.sandstone_stairs)              {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneStairsUTD)) {return new Object[]{Blocks.stone_stairs, meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabUTD)) {return new Object[]{Blocks.stone_slab, meta==8 ? 11: meta==0? 3:meta};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneUTD)) {return new Object[]{Blocks.brown_mushroom_block, 14};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.sandstoneWallUTD)) {return new Object[]{Blocks.cobblestone_wall, 0};}
        	if (block != null && block == Block.getBlockFromName(ModObjects.wallRC) && meta==11) {return new Object[]{Blocks.cobblestone_wall, 0};}
        }
        
        // Post Forge event
        BiomeEvent.GetVillageBlockID event = new BiomeEvent.GetVillageBlockID(biome == null ? null : biome, block, meta);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        if (event.getResult() == Result.DENY) return new Object[]{event.replacement, meta};
        
        return new Object[]{block, meta};
    }
    
    /**
     * Give this method the orientation of a sign or banner and the base mode of the structure it's in,
     * and it'll give you back the required meta value for construction.
     * For relative orientations, use:
     * 
     * HANGING:
     * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
     *   
     * STANDING:
     * 0=fore-facing (away from you); 4=right-facing; 8=back-facing (toward you); 12=left-facing
     */
    public static int getSignRotationMeta(int relativeOrientation, int coordBaseMode, boolean isHangingOnWall)
    {
    	if(isHangingOnWall)
    	{
    		switch (relativeOrientation)
    		{
    		case 0: // Facing away
    			return new int[]{3,4,2,5}[coordBaseMode];
    		case 1: // Facing right
    			return new int[]{5,3,5,3}[coordBaseMode];
    		case 2: // Facing you
    			return new int[]{2,5,3,4}[coordBaseMode];
    		case 3: // Facing left
    			return new int[]{4,2,4,2}[coordBaseMode];
    		}
    	}
    	else
    	{
    		return coordBaseMode <=1 ? ((coordBaseMode==1 ? 24 : 16)-(relativeOrientation+coordBaseMode*4))%16 : (relativeOrientation+coordBaseMode*4)%16;
    	}
    	return 0;
    }
    
    /**
     * Give this method the orientation of a hanging torch and the base mode of the structure it's in,
     * and it'll give you back the required meta value for construction.
     * For relative orientations, use:
     * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
     */
    public static int getTorchRotationMeta(int relativeOrientation, int coordBaseMode)
    {
		switch (relativeOrientation)
		{
		case 0: // Facing away
			return new int[]{3,2,4,1}[coordBaseMode];
		case 1: // Facing right
			return new int[]{1,3,1,3}[coordBaseMode];
		case 2: // Facing you
			return new int[]{4,1,3,2}[coordBaseMode];
		case 3: // Facing left
			return new int[]{2,4,2,4}[coordBaseMode];
		default: // Torch will be standing upright, hopefully
			return 0;
		}
    }
    
    public static int seaLevel = 63; //TODO - actually call sea level in later versions
	
    /**
     * Sets the path-specific block into the world
     * The block will get set at the ground height or posY, whichever is higher.
     * Returns the height at which the block was placed
     */
    public static int setPathSpecificBlock(World world, StructureVillageVN.StartVN startPiece, int meta, int posX, int posY, int posZ)
    {
    	Object[] grassPath = getBiomeSpecificBlock(ModObjects.chooseModPathBlock(), 0, startPiece.materialType, startPiece.biome);
    	Object[] planks = getBiomeSpecificBlock(Blocks.planks, 0, startPiece.materialType, startPiece.biome);
    	Object[] gravel = getBiomeSpecificBlock(Blocks.gravel, 0, startPiece.materialType, startPiece.biome);
    	Object[] cobblestone = getBiomeSpecificBlock(Blocks.cobblestone, 0, startPiece.materialType, startPiece.biome);
    	
    	// Top block level
    	int surfaceY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, posX, posZ)-1;
    	
    	// Raise Y to be at least below sea level
    	if (surfaceY < seaLevel) {surfaceY = seaLevel-1;}
    	
    	while (surfaceY >= seaLevel-1)
    	{
    		Block surfaceBlock = world.getBlock(posX, surfaceY, posZ);
    		
    		// Replace grass with grass path
    		if (surfaceBlock instanceof BlockGrass && world.isAirBlock(posX, Math.max(surfaceY, posY)+1, posZ))
    		{
    			world.setBlock(posX, Math.max(surfaceY, posY), posZ, (Block)grassPath[0], (Integer)grassPath[1], 2);
    			return Math.max(surfaceY, posY);
    		}
    		
    		// Replace sand with gravel supported by cobblestone
    		if (surfaceBlock instanceof BlockSand)
    		{
    			world.setBlock(posX, Math.max(surfaceY, posY), posZ, (Block)gravel[0], (Integer)gravel[1], 2);
    			world.setBlock(posX, Math.max(surfaceY, posY)-1, posZ, (Block)cobblestone[0], (Integer)cobblestone[1], 2);
    			return Math.max(surfaceY, posY);
    		}
    		
    		// Replace lava with two-layer cobblestone
    		if (surfaceBlock==Blocks.lava || surfaceBlock==Blocks.flowing_lava)
    		{
    			world.setBlock(posX, Math.max(surfaceY, posY), posZ, (Block)cobblestone[0], (Integer)cobblestone[1], 2);
    			world.setBlock(posX, Math.max(surfaceY, posY)-1, posZ, (Block)cobblestone[0], (Integer)cobblestone[1], 2);
    			return Math.max(surfaceY, posY);
    		}
    		
    		// Replace other liquid with planks
    		if (surfaceBlock.getMaterial().isLiquid())
    		{
    			world.setBlock(posX, Math.max(surfaceY, posY), posZ, (Block)planks[0], (Integer)planks[1], 2);
    			return Math.max(surfaceY, posY);
    		}
    		
    		surfaceY -=1;
    	}
		return -1;
    }
    
    /**
     * Contracts bounding box by amount specified in X, Y, Z
     */
    public static StructureBoundingBox contractBB(StructureBoundingBox structureBoundingBox, int xAmount, int yAmount, int zAmount)
    {
    	return new StructureBoundingBox(
    			structureBoundingBox.minX+MathHelper.abs_int(xAmount),
    			structureBoundingBox.minY+MathHelper.abs_int(yAmount),
    			structureBoundingBox.minZ+MathHelper.abs_int(zAmount),
    			structureBoundingBox.maxX-MathHelper.abs_int(xAmount),
    			structureBoundingBox.maxY-MathHelper.abs_int(yAmount),
    			structureBoundingBox.maxZ-MathHelper.abs_int(zAmount)
    			);
    }
    
    /**
     * Contracts bounding box by specified X, Z amount
     */
    public static StructureBoundingBox contractBB(StructureBoundingBox structureBoundingBox, int xAmount, int zAmount)
    {
    	return contractBB(structureBoundingBox, xAmount, 0, zAmount);
    }
    
    /**
     * Contracts bounding box by specified amount in all dimensions
     */
    public static StructureBoundingBox contractBB(StructureBoundingBox structureBoundingBox, int amount)
    {
    	return contractBB(structureBoundingBox, amount, amount, amount);
    }
    
    /**
     * This method checks to see if VN info exists for this village (name, banner, color, etc.)
     * If that information does not exist, it instead makes it.
     */
    public static NBTTagCompound getOrMakeVNInfo(World world, int posX, int posY, int posZ)
    {
    	int townColorMeta; int townColorMeta2;
    	String townSignEntry;
    	String namePrefix; String nameRoot; String nameSuffix;
    	NBTTagCompound bannerNBT;
        int townX; int townY; int townZ;
		NBTTagCompound villagetagcompound = new NBTTagCompound();
		
		int villageRadiusBuffer = 16;
		
    	// Set random seed
    	Random randomFromXYZ = new Random();
    	randomFromXYZ.setSeed(
					world.getSeed() +
					FunctionsVN.getUniqueLongForXYZ(posX, posY, posZ)
    			);
		
		// First, search through all previously generated VN villages and see if this is inside
    	// the bounding box of one of them.
    	
		
		// --- TRY 1: See if the village already exists in the vanilla collection object and try to match it to this village --- //
		
		
		Village villageNearTarget = world.villageCollectionObj.findNearestVillage(posX, posY, posZ, villageRadiusBuffer);
		
		if (villageNearTarget != null) // There is a town.
		{
			int villageRadius = villageNearTarget.getVillageRadius();
			int popSize = villageNearTarget.getNumVillagers();
			int centerX = villageNearTarget.getCenter().posX; // Village X position
			int centerY = villageNearTarget.getCenter().posY; // Village Y position
			int centerZ = villageNearTarget.getCenter().posZ; // Village Z position
			
			// Let's see if we can find a sign near that located village center!
			VNWorldDataStructure data = VNWorldDataStructure.forWorld(world, "villagenames3_Village", "NamedStructures");
			// .getTagList() will return all the entries under the specific village name.
			NBTTagCompound tagCompound = data.getData();
			Set tagmapKeyset = tagCompound.func_150296_c(); // Gets the town key list: "coordinates"

			Iterator itr = tagmapKeyset.iterator();
			
			//Placeholders for villagenames.dat tags
			boolean signLocated = false; //Use this to record whether or not a sign was found
			boolean isColony = false; //Use this to record whether or not the village was naturally generated
			
			while(itr.hasNext()) // Going through the list of VN villages
			{
				Object element = itr.next();
				townSignEntry = element.toString(); //Text name of village header (e.g. "x535y80z39")
				//The only index that has data is 0:
				NBTTagList nbttaglist = tagCompound.getTagList(townSignEntry, tagCompound.getId());
	            villagetagcompound = nbttaglist.getCompoundTagAt(0);
				
				// Retrieve the "sign" coordinates
				townColorMeta = villagetagcompound.getInteger("townColor");
				townX = villagetagcompound.getInteger("signX");
				townY = villagetagcompound.getInteger("signY");
				townZ = villagetagcompound.getInteger("signZ");
				namePrefix = villagetagcompound.getString("namePrefix");
				nameRoot = villagetagcompound.getString("nameRoot");
				nameSuffix = villagetagcompound.getString("nameSuffix");
				
				// Check if it has a second color
        		townColorMeta2 = townColorMeta;
        		// Second color is already explicitly registered
        		if (villagetagcompound.hasKey("townColor2")) {townColorMeta2 = villagetagcompound.getInteger("townColor2");}
        		// Try to extract second color from banner colors
        		else
        		{
        			if (villagetagcompound.hasKey("BlockEntityTag", 10))
            		{
            			bannerNBT = villagetagcompound.getCompoundTag("BlockEntityTag");
            			if (bannerNBT.hasKey("Patterns", 9)) // 9 is Tag List
            	        {
            				NBTTagList nbttaglistPattern = bannerNBT.getTagList("Patterns", 9);
            				
            				for (int i=0; i < nbttaglistPattern.tagCount(); i++)
            				{
            					NBTTagCompound patterntag = nbttaglistPattern.getCompoundTagAt(i);
            					if (patterntag.hasKey("Color") && patterntag.getInteger("Color")!=townColorMeta)
            					{
            						townColorMeta2 = patterntag.getInteger("Color");
            					}
            				}
            	        }
            		}
            		// Village does not have banner info. Make some.
            		else
            		{
            			while (
            					townColorMeta2==townColorMeta // Colors are the same, requiring a new draw
            					&& !(townColorMeta==2 && townColorMeta2==10) && !(townColorMeta==10 && townColorMeta2==2) // Lime and Green aren't in the same banner
            					&& !(townColorMeta==5 && townColorMeta2==13) && !(townColorMeta==13 && townColorMeta2==5) // Magenta and Purple aren't in the same banner
            					&& !(townColorMeta==6 && townColorMeta2==12) && !(townColorMeta==12 && townColorMeta2==6) // Light Blue and Cyan aren't in the same banner
            					)
            			{
            				townColorMeta2 = (Integer) FunctionsVN.weightedRandom(BannerGenerator.colorMeta, BannerGenerator.colorWeights, randomFromXYZ);
            			}
            		}
            		
            		villagetagcompound.setInteger("townColor2", townColorMeta2);
            		
            		// Replace the old tag
            		nbttaglist.func_150304_a(0, villagetagcompound);
            		tagCompound.setTag(townSignEntry, nbttaglist);
            		data.markDirty();
        		}
				
				// Now find the nearest Village to that sign's coordinate, within villageRadiusBuffer blocks outside the radius.
				Village villageNearSign = world.villageCollectionObj.findNearestVillage(townX, townY, townZ, villageRadiusBuffer);
				
				isColony = villagetagcompound.getBoolean("isColony");
				
				if (villageNearSign == villageNearTarget) // There is a match between the nearest village to this villager and the nearest village to the sign
				{
					signLocated = true;
					
					return villagetagcompound;
				}
			}
		}
		
		
		// --- TRY 2: compare this sign position with stored VN sign positions and see if you're within range of one of them --- // 
		
		VNWorldDataStructure data = VNWorldDataStructure.forWorld(world, "villagenames3_Village", "NamedStructures");
		NBTTagCompound tagCompound = data.getData();
		Set tagmapKeyset = tagCompound.func_150296_c(); //Gets the town key list: "coordinates"
		Iterator itr = tagmapKeyset.iterator();

		while(itr.hasNext())
		{
			Object element = itr.next();
			townSignEntry = element.toString(); //Text name of village header (e.g. "Kupei, x191 y73 z187")
			//The only index that has data is 0:
			NBTTagList nbttaglist = tagCompound.getTagList(townSignEntry, tagCompound.getId());
			villagetagcompound = nbttaglist.getCompoundTagAt(0);

			townX = villagetagcompound.hasKey("signX") ? villagetagcompound.getInteger("signX") : Integer.MAX_VALUE;
			townY = villagetagcompound.hasKey("signY") ? villagetagcompound.getInteger("signY") : Integer.MAX_VALUE;
			townZ = villagetagcompound.hasKey("signZ") ? villagetagcompound.getInteger("signZ") : Integer.MAX_VALUE;
			
			int radiussearch = 32;
			if (
					villagetagcompound.hasKey("signX") && villagetagcompound.hasKey("signY") && villagetagcompound.hasKey("signZ")
					&& (posX-townX)*(posX-townX) + (posZ-townZ)*(posZ-townZ) <= radiussearch*radiussearch
					)
			{
				// This village already has a name.
				townColorMeta = villagetagcompound.getInteger("townColor");
				namePrefix = villagetagcompound.getString("namePrefix");
				nameRoot = villagetagcompound.getString("nameRoot");
				nameSuffix = villagetagcompound.getString("nameSuffix");
				/*
				LogHelper.info("Village detected at " + posX + " " + posY + " " + posZ + " - it's " + nameRoot + " listed under " + townSignEntry);
				LogHelper.info("It thinks its position is " + townX + " " + townY + " " + townZ + " - it's " + nameRoot + " listed under " + townSignEntry);
				LogHelper.info("Distance difference is " + MathHelper.sqrt_double((posX-townX)*(posX-townX) + (posY-townY)*(posY-townY) + (posZ-townZ)*(posZ-townZ)));
				*/
				// Check if it has a second color
				townColorMeta2 = townColorMeta;
				// Second color is already explicitly registered
				if (villagetagcompound.hasKey("townColor2")) {townColorMeta2 = villagetagcompound.getInteger("townColor2");}
				// Try to extract second color from banner colors
				else
				{
					if (villagetagcompound.hasKey("BlockEntityTag", 10))
					{
						bannerNBT = villagetagcompound.getCompoundTag("BlockEntityTag");
						if (bannerNBT.hasKey("Patterns", 9)) // 9 is Tag List
						{
							NBTTagList nbttaglistPattern = bannerNBT.getTagList("Patterns", 9);
							
							for (int i=0; i < nbttaglistPattern.tagCount(); i++)
							{
								NBTTagCompound patterntag = nbttaglistPattern.getCompoundTagAt(i);
								if (patterntag.hasKey("Color") && patterntag.getInteger("Color")!=townColorMeta)
								{
									townColorMeta2 = patterntag.getInteger("Color");
								}
							}
						}
					}
					// Village does not have banner info. Make some.
					else
					{
            			while (
            					townColorMeta2==townColorMeta // Colors are the same, requiring a new draw
            					&& !(townColorMeta==2 && townColorMeta2==10) && !(townColorMeta==10 && townColorMeta2==2) // Lime and Green aren't in the same banner
            					&& !(townColorMeta==5 && townColorMeta2==13) && !(townColorMeta==13 && townColorMeta2==5) // Magenta and Purple aren't in the same banner
            					&& !(townColorMeta==6 && townColorMeta2==12) && !(townColorMeta==12 && townColorMeta2==6) // Light Blue and Cyan aren't in the same banner
            					)
            			{
            				townColorMeta2 = (Integer) FunctionsVN.weightedRandom(BannerGenerator.colorMeta, BannerGenerator.colorWeights, randomFromXYZ);
            			}
					}
					
					villagetagcompound.setInteger("townColor2", townColorMeta2);
					
					// Replace the old tag
					nbttaglist.func_150304_a(0, villagetagcompound);
					tagCompound.setTag(townSignEntry, nbttaglist);
					data.markDirty();
				}
				
				return villagetagcompound;
			}
		}
		
		
		// --- TRY 3: just make a new VN entry --- //
		
    	
    	villagetagcompound = new NBTTagCompound();
    	
		MapGenStructureData structureData;
		NBTTagCompound nbttagcompound = null;
		int villageArea = -1; // If a village area value is not ascertained, this will remain as -1.
		
		try
		{
			structureData = (MapGenStructureData)world.perWorldStorage.loadData(MapGenStructureData.class, "Village");
			nbttagcompound = structureData.func_143041_a();
		}
		catch (Exception e) // Village.dat does not exist
		{
			try // Open Terrain Generation version
    		{
    			structureData = (MapGenStructureData)world.perWorldStorage.loadData(MapGenStructureData.class, "OTGVillage");
    			nbttagcompound = structureData.func_143041_a();
    		}
    		catch (Exception e1) {} // OTGVillage.dat does not exist
		}
		
		
		int topLineRand = randomFromXYZ.nextInt(4);
		
		// Call the name generator here
		String[] newVillageName = NameGenerator.newRandomName("Village", randomFromXYZ);
		String headerTags = newVillageName[0];
		namePrefix = newVillageName[1];
		nameRoot = newVillageName[2];
		nameSuffix = newVillageName[3];
		
		// Generate banner info, regardless of if we make a banner.
		Object[] newRandomBanner = BannerGenerator.randomBannerArrays(randomFromXYZ, -1, -1);
		ArrayList<String> patternArray = (ArrayList<String>) newRandomBanner[0];
		ArrayList<Integer> colorArray = (ArrayList<Integer>) newRandomBanner[1];
		ItemStack villageBanner = BannerGenerator.makeBanner(patternArray, colorArray);
		townColorMeta = 15-colorArray.get(0);
		townColorMeta2 = colorArray.size()==1 ? townColorMeta : 15-colorArray.get(1);
		
		// Make the data bundle to save to NBT
		NBTTagList nbttaglist = new NBTTagList();
		
        villagetagcompound.setInteger("signX", posX);
        villagetagcompound.setInteger("signY", posY);
        villagetagcompound.setInteger("signZ", posZ);
        villagetagcompound.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
        villagetagcompound.setInteger("townColor2", townColorMeta2);
        villagetagcompound.setString("namePrefix", namePrefix);
        villagetagcompound.setString("nameRoot", nameRoot);
        villagetagcompound.setString("nameSuffix", nameSuffix);
        
        // Form and append banner info
        // If you don't have a mod banner, this will not be added (bc crash). It will be generated once you do.
        if (villageBanner!=null) {villagetagcompound.setTag("BlockEntityTag", BannerGenerator.getNBTFromBanner(villageBanner));}

        nbttaglist.appendTag(villagetagcompound);
        
        // Save the data under a "Villages" entry with unique name based on sign coords
        data.getData().setTag((namePrefix + " " + nameRoot + " " + nameSuffix).trim() + ", x" + posX + " y" + posY + " z" + posZ, nbttaglist);
		data.markDirty();
		
		return villagetagcompound;
    }
        
    /**
     * Generates stuff for the sign that will be placed at the village meeting point
     * @return 
     */
    public static TileEntitySign generateSignContents(String namePrefix, String nameRoot, String nameSuffix)
    {
    	Random random = new Random();
    	
    	TileEntitySign signContents = new TileEntitySign();
		
    	String[] villageOrTown = new String[]{"Village"+(random.nextBoolean()?" of":":"), "Town"+(random.nextBoolean()?" of":":")};
    	if (namePrefix.toLowerCase().trim().contains("village") || nameSuffix.toLowerCase().trim().contains("village")) {villageOrTown[0]="Town"+(random.nextBoolean()?" of":":");}
    	if (namePrefix.toLowerCase().trim().contains("town") || nameSuffix.toLowerCase().trim().contains("town")) {villageOrTown[0]="Village"+(random.nextBoolean()?" of":":");}
    	if (villageOrTown[0].toLowerCase().trim().contains("town") && villageOrTown[1].toLowerCase().trim().contains("village")) {villageOrTown[0]=""; villageOrTown[1]="";}
    	String topLine = random.nextBoolean() ? "Welcome to" : villageOrTown[random.nextInt(2)];
		topLine = topLine.trim();
		
		if ( (namePrefix.length() + 1 + nameRoot.length()) > 15 )
		{
			// Prefix+Root is too long, so move prefix to line 1
			signContents.signText[0] = GeneralConfig.headerTags.trim() + topLine.trim();
			signContents.signText[1] = namePrefix.trim();
			if ( (nameRoot.length() + 1 + nameSuffix.length()) > 15 )
			{
				// Root+Suffix is too long, so move suffix to line 3
				signContents.signText[2] = nameRoot.trim();
				signContents.signText[3] = nameSuffix.trim();
			}
			else
			{
				// Fit Root+Suffix onto line 2
				signContents.signText[2] = (nameRoot+" "+nameSuffix).trim();
			}
		}
		else if ( (namePrefix.length() + 1 + nameRoot.length() + 1 + nameSuffix.length()) <= 15 )
		{
			// Whole name fits on one line! Put it all on line 2.
			signContents.signText[1] = GeneralConfig.headerTags.trim() + topLine;
			signContents.signText[2] = (namePrefix+" "+nameRoot+" "+nameSuffix).trim();
		}
		else
		{
			// Only Prefix and Root can fit together on line 2.
			signContents.signText[1] = GeneralConfig.headerTags.trim() + topLine.trim();
			signContents.signText[2] = (namePrefix+" "+nameRoot).trim();
			signContents.signText[3] = nameSuffix.trim();
		}
		// If top line is blank, roll everything up one line:
		if (topLine.equals(""))
		{
			for (int isign=0; isign <3; isign++)
			{
				signContents.signText[isign] = signContents.signText[isign+1];	
			}
			signContents.signText[3] = "";
		}
		
		return signContents;
    }
    
    /**
     * Creates a villager of the specified profession/career/age.
     * A profession of -1 means return a random one. Whether this is vanilla only or from all registered will be determined by the configs.
     * Any career greater than 0 requires the career system to be true.
     */
    public static EntityVillager makeVillagerWithProfession(World world, Random random, int profession, int career, int age)
    {
		EntityVillager entityvillager = new EntityVillager(world);
		
		// Set profession
		if (profession==-1)
		{
			if (GeneralConfig.spawnModdedVillagers)
			{
				VillagerRegistry.applyRandomTrade(entityvillager, random);
			}
			else
			{
				entityvillager.setProfession(GeneralConfig.enableNitwit ? random.nextInt(6) : random.nextInt(5));
			}
			
			// Equally weight career subdivisions
			if (entityvillager.getProfession()>=0 && entityvillager.getProfession()<=4 && GeneralConfig.villagerCareers)
			{
				ExtendedVillager ieep = ExtendedVillager.get(entityvillager);
				
				switch(random.nextInt(GeneralConfig.modernVillagerTrades ? 13 : 12))
				{
					default:
					case 0: // Farmer | Farmer
						profession = 0; career = 1; break;
					case 1: // Farmer | Fisherman
						profession = 0; career = 2; break;
					case 2: // Farmer | Shepherd
						profession = 0; career = 3; break;
					case 3: // Farmer | Fletcher
						profession = 0; career = 4; break;
					case 4: // Librarian | Librarian
						profession = 1; career = 1; break;
					case 5: // Librarian | Cartographer
						profession = 1; career = 2; break;
					case 6: // Priest | Cleric
						profession = 2; career = 1; break;
					case 7: // Blacksmith | Armorer
						profession = 3; career = 1; break;
					case 8: // Blacksmith | Weaponsmith
						profession = 3; career = 2; break;
					case 9: // Blacksmith | Toolsmith
						profession = 3; career = 3; break;
					case 10: // Butcher | Butcher
						profession = 4; career = 1; break;
					case 11: // Butcher | Leatherworker
						profession = 4; career = 2; break;
					case 12: // Blacksmith | Mason
						profession = 3; career = 4; break;
				}
				entityvillager.setProfession(profession);
				ieep.setCareer(career);
			}
		}
		else
		{
			entityvillager.setProfession(profession);
			
			// Set career
			if (career > 0 && GeneralConfig.villagerCareers)
			{
				ExtendedVillager ieep = ExtendedVillager.get(entityvillager);
				ieep.setCareer(career);
			}
		}
		
		// Set age
		entityvillager.setGrowingAge(age);
		
		return entityvillager;
    }
    
    
    
    // -------------------------- //    
    // --- General components --- //
    // -------------------------- //
    
    // --- Start --- //
    
    public static class StartVN extends StructureVillagePieces.Start
    {
    	// Set them to defaults here
    	public FunctionsVN.VillageType villageType = FunctionsVN.VillageType.PLAINS;
    	public FunctionsVN.MaterialType materialType = FunctionsVN.MaterialType.OAK;
    	public int townColor = 11; // Blue
    	public int townColor2 = 14; // Red
    	// These colors are used in case more are needed
    	public int townColorA = -1; // 0: White
    	public int townColorB = -1; // 13: Green
    	public int townColorC = -1; // 4: Yellow
    	
    	public String namePrefix = "";
    	public String nameRoot = "";
    	public String nameSuffix = "";
    	public boolean villagersGenerated = false;
    	public int bannerY = 0;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	
        public StartVN() {}

        public StartVN(WorldChunkManager chunkManager, int componentType, Random random, int posX, int posZ, List components, int terrainType)
        {
            super(chunkManager, componentType, random, posX, posZ, components, terrainType);
            this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, posX, posZ);
            this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, posX, posZ);
        }
        
        // Beth had this great idea to publicly call these protected methods lmao :'C
        //public int getXWithOffsetPublic(int xOffset, int zOffset) {return this.getXWithOffset(xOffset, zOffset);}
        //public int getYWithOffsetPublic(int yOffset) {return this.getYWithOffset(yOffset);}
        //public int getZWithOffsetPublic(int xOffset, int zOffset) {return this.getZWithOffset(xOffset, zOffset);}
    }
    
    
    /**
     * Gets the next village component, with the bounding box shifted -1 in the X and Z direction.
     */
    protected static StructureComponent getNextComponentNN(StartVN start, List components, Random random, int yOffset, int lateralOffset, int horizIndex, int componentType, StructureBoundingBox boundingBox)
    {
        switch (horizIndex)
        {
            case 0: // South
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX - 1, boundingBox.minY + yOffset, boundingBox.minZ + lateralOffset, 1, componentType);
            case 1: // West
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX + lateralOffset, boundingBox.minY + yOffset, boundingBox.minZ - 1, 2, componentType);
            case 2: // North
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX - 1, boundingBox.minY + yOffset, boundingBox.minZ + lateralOffset, 1, componentType);
            case 3: // East
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX + lateralOffset, boundingBox.minY + yOffset, boundingBox.minZ - 1, 2, componentType);
            default:
                return null;
        }
    }

    /**
     * Gets the next village component, with the bounding box shifted +1 in the X and Z direction.
     */
    protected static StructureComponent getNextComponentPP(StartVN start, List components, Random random, int yOffset, int lateralOffset, int horizIndex, int componentType, StructureBoundingBox boundingBox)
    {
        switch (horizIndex)
        {
            case 0: // South
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.maxX + 1, boundingBox.minY + yOffset, boundingBox.minZ + lateralOffset, 3, componentType);
            case 1: // West
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX + lateralOffset, boundingBox.minY + yOffset, boundingBox.maxZ + 1, 0, componentType);
            case 2: // North
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.maxX + 1, boundingBox.minY + yOffset, boundingBox.minZ + lateralOffset, 3, componentType);
            case 3: // East
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX + lateralOffset, boundingBox.minY + yOffset, boundingBox.maxZ + 1, 0, componentType);
            default:
                return null;
        }
    }
    
    
    public static class DecorTorch extends StructureVillagePieces.Village
    {
    	public FunctionsVN.VillageType villageType = FunctionsVN.VillageType.PLAINS;
    	public FunctionsVN.MaterialType materialType = FunctionsVN.MaterialType.OAK;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	public StartVN start;
    	
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private int averageGroundLevel = -1;
    	
        public DecorTorch() {}
        
        public DecorTorch(StartVN start, int componentType, Random random, StructureBoundingBox structureBB, int horizIndex)
        {
            super(start, componentType);
            this.coordBaseMode = horizIndex;
            this.boundingBox = structureBB;
            
            this.start = start;
            // Set biome and material type, to be used during construction
            int averageX = (structureBB.minX+structureBB.maxX)/2;
            int averageZ = (structureBB.minZ+structureBB.maxZ)/2;
            this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(start.getWorldChunkManager(), averageX, averageZ);
            this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(start.getWorldChunkManager(), averageX, averageZ);
        }
        
        public static StructureBoundingBox findPieceBox(StartVN start, List components, Random random, int x, int y, int z, int horizIndex)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
            		x, y, z, // Structure coordinates
            		0, 0, 0, // u, v, w offset. The u and w offsets are positive for horizIndex <=1 and negative for horizIndex >=2
            		3, 4, 3, // width, height, depth
            		horizIndex);
            
            return StructureComponent.findIntersecting(components, structureboundingbox) != null ? null : structureboundingbox;
        }
        
        /**
         * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
         * Mineshafts at the end, it adds Fences...
         */
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	if (this.averageGroundLevel < 0)
            {
        		this.averageGroundLevel = StructureVillageVN.getMedianGroundLevel(world,
        				// Set the bounding box version as this bounding box but with Y going from 0 to 512
        				new StructureBoundingBox(
        						this.boundingBox.minX, this.boundingBox.minZ,
        						this.boundingBox.maxX, this.boundingBox.maxZ),
        				true, (byte)1, this.coordBaseMode);
        		
                if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
            }
        	/*
            if (this.field_143015_k < 0)
            {
                this.field_143015_k = this.getAverageGroundLevel(world, structureBB);
                
                if (this.field_143015_k < 0)
                {
                    return true;
                }
                
                this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 4 - 1, 0);
            }
            
            this.fillWithBlocks(world, structureBB, 0, 0, 0, 2, 3, 1, Blocks.air, Blocks.air, false);
            this.placeBlockAtCurrentPosition(world, Blocks.fence, 0, 1, 0, 0, structureBB);
            this.placeBlockAtCurrentPosition(world, Blocks.fence, 0, 1, 1, 0, structureBB);
            this.placeBlockAtCurrentPosition(world, Blocks.fence, 0, 1, 2, 0, structureBB);
            this.placeBlockAtCurrentPosition(world, Blocks.log,   0, 1, 3, 0, structureBB);
            this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 0, 3, 0, structureBB);
            this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 1, 3, 1, structureBB);
            this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 2, 3, 0, structureBB);
            this.placeBlockAtCurrentPosition(world, Blocks.torch, 0, 1, 3, -1, structureBB);
            */
        	// Decor
            int[][] decorUVW = new int[][]{
            	{1, 0, 1},
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
            	
            	// Generate lantern-like decor
            	ArrayList<BlueprintData> decorBlueprint = null;
            	
            	if (this.villageType==FunctionsVN.VillageType.DESERT)
            	{
            		decorBlueprint = DesertStructures.getDesertDecorBlueprint(0, this.start, this.coordBaseMode, randomFromXYZ);//, 5); // Use lime
            	}
            	else if (this.villageType==FunctionsVN.VillageType.TAIGA)
            	{
            		decorBlueprint = TaigaStructures.getTaigaDecorBlueprint(6, this.start, this.coordBaseMode, randomFromXYZ);
            	}
            	else if (this.villageType==FunctionsVN.VillageType.SAVANNA)
            	{
            		decorBlueprint = SavannaStructures.getSavannaDecorBlueprint(0, this.start, this.coordBaseMode, randomFromXYZ);
            	}
            	else if (this.villageType==FunctionsVN.VillageType.SNOWY)
            	{
            		decorBlueprint = SnowyStructures.getSnowyDecorBlueprint(randomFromXYZ.nextInt(3), this.start, this.coordBaseMode, randomFromXYZ);
            	}
            	else // Plains
            	{
            		decorBlueprint = PlainsStructures.getPlainsDecorBlueprint(0, this.start, this.coordBaseMode, randomFromXYZ);
            	}
            	
            	for (BlueprintData b : decorBlueprint)
            	{
            		// Place block indicated by blueprint
            		this.placeBlockAtCurrentPosition(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos(), uvw[2]+b.getWPos(), structureBB);
            		
            		// Fill below if flagged
            		if ((b.getfillFlag()&1)!=0)
            		{
            			this.func_151554_b(world, b.getBlock(), b.getMeta(), uvw[0]+b.getUPos(), decorHeightY+b.getVPos()-1, uvw[2]+b.getWPos(), structureBB);
            		}
            		
            		// Clear above if flagged
            		if ((b.getfillFlag()&2)!=0)
            		{
            			this.clearCurrentPositionBlocksUpwards(world, uvw[0]+b.getUPos(), decorHeightY+b.getVPos()+1, uvw[2]+b.getWPos(), structureBB);
            		}            		
            	}
            }
        	
            return true;
        }
    }
    
    
    // --- Path --- //
    
    public static class PathVN extends StructureVillagePieces.Road
    {
        private int length;
        
        public PathVN() {}

        public PathVN(StructureVillagePieces.Start start, int componentType, Random random, StructureBoundingBox boundingBox, int coordBaseMode)
        {
            super(start, componentType);
            this.coordBaseMode = coordBaseMode;
            this.boundingBox = boundingBox;
            this.length = Math.max(boundingBox.getXSize(), boundingBox.getZSize());
        }
        
        @Override
        protected void func_143012_a(NBTTagCompound tagCompound)
        {
            super.func_143012_a(tagCompound);
            tagCompound.setInteger("Length", this.length);
        }
        
        @Override
        protected void func_143011_b(NBTTagCompound tagCompound)
        {
            super.func_143011_b(tagCompound);
            this.length = tagCompound.getInteger("Length");
        }

        /**
         * Initiates construction of the Structure Component picked, at the current Location of StructGen
         */
        @Override
        public void buildComponent(StructureComponent start, List components, Random random)
        {
            boolean structureWasGenerated = false;
            int i;
            StructureComponent structurecomponent1;
            
            // Construct buildings on the -x/-z sides of roads
            for (i = random.nextInt(5); i < this.length - 8; i += 2 + random.nextInt(5))
            {
                structurecomponent1 = StructureVillageVN.getNextComponentNN((StartVN)start, components, random, 0, i, this.coordBaseMode, this.getComponentType(), this.getBoundingBox());
                
                if (structurecomponent1 != null)
                {
                	i += Math.max(structurecomponent1.getBoundingBox().getXSize(), structurecomponent1.getBoundingBox().getZSize());
                    structureWasGenerated = true;
                }
            }
            
            // Construct buildings on the +x/+z sides of roads
            for (i = random.nextInt(5); i < this.length - 8; i += 2 + random.nextInt(5))
            {
                structurecomponent1 = StructureVillageVN.getNextComponentPP((StartVN)start, components, random, 0, i, this.coordBaseMode, this.getComponentType(), this.getBoundingBox());
                
                if (structurecomponent1 != null)
                {
                    i += Math.max(structurecomponent1.getBoundingBox().getXSize(), structurecomponent1.getBoundingBox().getZSize());
                    structureWasGenerated = true;
                }
            }
            
            // If a building was generated, add a left-hand-turn street (for horizIndex>=2)
            if (structureWasGenerated && random.nextInt(3) > 0)
            {
            	switch (this.coordBaseMode)
                {
                    case 0: // South
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, 1, this.getComponentType());
                        break;
                    case 1: // West
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.getComponentType());
                        break;
                    case 2: // North
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, 1, this.getComponentType());
                        break;
                    case 3: // East
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, 2, this.getComponentType());
                        break;
                }
            }
            
            // If a building was generated, add a right-hand-turn street (for horizIndex>=2)
            if (structureWasGenerated && random.nextInt(3) > 0)
            {
            	switch (this.coordBaseMode)
                {
                    case 0: // South
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, 3, this.getComponentType());
                        break;
                    case 1: // West
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.getComponentType());
                        break;
                    case 2: // North
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, 3, this.getComponentType());
                        break;
                    case 3: // East
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, 0, this.getComponentType());
                        break;
                }
            }
            
            //LogHelper.info("Spawning " + ((StartVN)start).villageType + " church at " + this.boundingBox.minX + " " + this.boundingBox.minZ);
            
            // If I wanted to place something on a road, I could do that here:
			// Place a church along the end
            /*
            switch (this.coordBaseMode)
            {
	            case 0: // South
	            	StructureVillageVN.getRandomVillageRoadComponent((StartVN)start, components, random, this.boundingBox.minX+(0), this.boundingBox.minY, this.boundingBox.maxZ+(1), 0, this.getComponentType());
	                break;
	            case 1: // West
	            	StructureVillageVN.getRandomVillageRoadComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(0), 1, this.getComponentType());
	                break;
	            case 2: // North
	            	StructureVillageVN.getRandomVillageRoadComponent((StartVN)start, components, random, this.boundingBox.minX+(-1), this.boundingBox.minY, this.boundingBox.minZ+(0), 2, this.getComponentType());
	                break;
	            case 3: // East
	            	StructureVillageVN.getRandomVillageRoadComponent((StartVN)start, components, random, this.boundingBox.maxX+(1), this.boundingBox.minY, this.boundingBox.minZ+(0), 3, this.getComponentType());
	                break;
            }
			*/
		
        }
        
        /**
         * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes
         * Mineshafts at the end, it adds Fences...
         */
        @Override
        public boolean addComponentParts(World world, Random random, StructureBoundingBox structureBB)
        {
        	StructureVillagePieces.Start startPiece_reflected = ReflectionHelper.getPrivateValue(StructureVillagePieces.Village.class, this, new String[]{"startPiece", "startPiece"});
        	
        	// Scans X, Z inside bounding box and finds the ground layer
        	for (int u = 0; u <= (this.boundingBox.maxX-this.boundingBox.minX); ++u)
            {
        		for (int w = 0; w <= (this.boundingBox.maxZ-this.boundingBox.minZ); ++w)
                {
        			int x = this.boundingBox.minX + u;
        			int z = this.boundingBox.minZ + w;
        			
                    if (structureBB.isVecInside(x, 64, z))
                    {
                    	// Gets ground level, so long as it's not leaves or other foliage
                        int y = getAboveTopmostSolidOrLiquidBlockVN(world, x, z) - 1;
                        
                        setPathSpecificBlock(world, (StartVN)startPiece_reflected, 0, x, y, z);
                    }
                }
            }
        	
            return true;
        }
    }
    
    
    /**
     * Returns the direction-shifted metadata for blocks that require orientation, e.g. doors, stairs, ladders.
     */
    public static int getMetadataWithOffset(Block blockIn, int metaIn, int coordBaseMode)
    {
    	// Rotate rails
        if (blockIn == Blocks.rail)
        {
            if (coordBaseMode == 1 || coordBaseMode == 3)
            {
                if (metaIn == 1) {return 0;}
                return 1;
            }
        }
        else if (blockIn != Blocks.wooden_door && blockIn != Blocks.iron_door)
        {
            if (blockIn != Blocks.stone_stairs && blockIn != Blocks.oak_stairs && blockIn != Blocks.nether_brick_stairs && blockIn != Blocks.stone_brick_stairs && blockIn != Blocks.sandstone_stairs)
            {
                if (blockIn == Blocks.ladder)
                {
                    if (coordBaseMode == 0)
                    {
                    	switch (metaIn)
                    	{
                    	case 2: return 3;
                    	case 3: return 2;
                    	default:
                    	}
                    }
                    else if (coordBaseMode == 1)
                    {
                    	switch (metaIn)
                    	{
                    	case 2: return 4;
                    	case 3: return 5;
                    	case 4: return 2;
                    	case 5: return 3;
                    	default:
                    	}
                    }
                    else if (coordBaseMode == 3)
                    {
                    	switch (metaIn)
                    	{
                    	case 2: return 5;
                    	case 3: return 4;
                    	case 4: return 2;
                    	case 5: return 3;
                    	default:
                    	}
                    }
                }
                else if (blockIn == Blocks.stone_button)
                {
                    if (coordBaseMode == 0)
                    {
                    	switch (metaIn)
                    	{
                    	case 3: return 4;
                    	case 4: return 3;
                    	default:
                    	}
                    }
                    else if (coordBaseMode == 1)
                    {
                    	switch (metaIn)
                    	{
                    	case 1: return 4;
                    	case 2: return 3;
                    	case 3: return 1;
                    	case 4: return 2;
                    	default:
                    	}
                    }
                    else if (coordBaseMode == 3)
                    {
                    	switch (metaIn)
                    	{
                    	case 1: return 4;
                    	case 2: return 3;
                    	case 3: return 2;
                    	case 4: return 1;
                    	default:
                    	}
                    }
                }
                else if (blockIn != Blocks.tripwire_hook && !(blockIn instanceof BlockDirectional))
                {
                    if (blockIn == Blocks.piston || blockIn == Blocks.sticky_piston || blockIn == Blocks.lever || blockIn == Blocks.dispenser)
                    {
                        if (coordBaseMode == 0)
                        {
                            if (metaIn == 2 || metaIn == 3)
                            {
                                return Facing.oppositeSide[metaIn];
                            }
                        }
                        else if (coordBaseMode == 1)
                        {
                        	switch (metaIn)
                        	{
                        	case 2: return 4;
                        	case 3: return 5;
                        	case 4: return 2;
                        	case 5: return 3;
                        	default:
                        	}
                        }
                        else if (coordBaseMode == 3)
                        {
                        	switch (metaIn)
                        	{
                        	case 2: return 5;
                        	case 3: return 4;
                        	case 4: return 2;
                        	case 5: return 3;
                        	default:
                        	}
                        }
                    }
                }
                else if (coordBaseMode == 0)
                {
                    if (metaIn == 0 || metaIn == 2)
                    {
                        return Direction.rotateOpposite[metaIn];
                    }
                }
                else if (coordBaseMode == 1)
                {
                	switch (metaIn)
                	{
                	case 0: return 3;
                	case 1: return 2;
                	case 2: return 1;
                	case 3: return 0;
                	default:
                	}
                }
                else if (coordBaseMode == 3)
                {
                	switch (metaIn)
                	{
                	case 0: return 1;
                	case 1: return 2;
                	case 2: return 3;
                	case 3: return 0;
                	default:
                	}
                }
            }
            else if (coordBaseMode == 0)
            {
            	switch (metaIn)
            	{
            	case 2: return 3;
            	case 3: return 2;
            	default:
            	}
            }
            else if (coordBaseMode == 1)
            {
            	switch (metaIn)
            	{
            	case 0: return 2;
            	case 1: return 3;
            	case 2: return 0;
            	case 3: return 1;
            	default:
            	}
            }
            else if (coordBaseMode == 3)
            {
            	switch (metaIn)
            	{
            	case 0: return 2;
            	case 1: return 3;
            	case 2: return 1;
            	case 3: return 0;
            	default:
            	}
            }
        }
        else if (coordBaseMode == 0)
        {
        	switch (metaIn)
        	{
        	case 0: return 2;
        	case 2: return 0;
        	default:
        	}
        }
        else
        {
        	switch (metaIn)
        	{
        	case 1: return metaIn + 1 & 3;
        	case 3: return metaIn + 3 & 3;
        	default:
        	}
        }

        return metaIn;
    }
    
    /**
	 * furnaceOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 * -X: returns the value X - used for things like upright barrels
	 */
	public static int chooseFurnaceMeta(int orientation, int horizIndex)
	{
		if (orientation<0) {return -orientation;}
		return FURNACE_META_ARRAY[orientation][horizIndex];
	}
    /**
	 * buttonOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static int chooseButtonMeta(int orientation, int horizIndex)
	{
		return BUTTON_META_ARRAY[orientation][horizIndex];
	}
    /**
	 * anvilOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static int chooseAnvilMeta(int orientation, int horizIndex)
	{
		return ANVIL_META_ARRAY[orientation][horizIndex];
	}
    /**
	 * anvilOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static int chooseGlazedTerracottaMeta(int orientation, int horizIndex)
	{
		return GLAZED_TERRACOTTA_META_ARRAY[orientation][horizIndex];
	}
	
    /**
     * Returns meta values for lower and upper halves of a door
     * 
	 * orientation - Direction the outside of the door faces when closed:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 * 
	 * isShut - doors are "shut" by default when placed by a player
	 * rightHandRule - whether the door opens counterclockwise when viewed from above. This is default state when placed by a player
	 */
	public static int[] getDoorMetas(int orientation, int horizIndex, boolean isShut, boolean isRightHanded)
	{
		return new int[] {
				DOOR_META_ARRAY[1][isRightHanded?1:0][isShut?1:0][orientation][horizIndex],
				DOOR_META_ARRAY[0][isRightHanded?1:0][isShut?1:0][orientation][horizIndex]
						};
	}

	/**
	 * Returns a random animal from the /structures/village/common/animals folder, not including cats
	 */
	public static EntityLiving getVillageAnimal(World world, Random random, boolean includeHorses)
	{
		EntityLiving animal;
		int animalIndex = random.nextInt(4 + (includeHorses ? 5 : 0));
		
		if (animalIndex==0)      {animal = new EntityCow(world);}
		else if (animalIndex==1) {animal = new EntityPig(world);}
		else if (animalIndex<=3) {animal = new EntitySheep(world);}
		else                     {animal = new EntityHorse(world);}
		
		IEntityLivingData ientitylivingdata = animal.onSpawnWithEgg(null); // To give the animal random spawning properties (horse pattern, sheep color, etc)
		
		return animal;
	}
	
	
	
	/**
	 * relativeOrientation
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static int getBedOrientationMeta(int relativeOrientation, int coordBaseMode, boolean isHead)
	{
		switch (relativeOrientation)
		{
		case 0: // Facing away
			return new int[]{2,3,0,1}[coordBaseMode] + (isHead ? 8:0);
		case 1: // Facing right
			return new int[]{1,2,1,2}[coordBaseMode] + (isHead ? 8:0);
		case 2: // Facing you
			return new int[]{0,1,2,3}[coordBaseMode] + (isHead ? 8:0);
		case 3: // Facing left
			return new int[]{3,0,3,0}[coordBaseMode] + (isHead ? 8:0);
		}
		return 0 + (isHead ? 8:0);
	}
	
	
	
	/**
	 * relativeOrientation
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static int getTrapdoorMeta(int relativeOrientation, int coordBaseMode, boolean isTop, boolean isVertical)
	{
		int meta = 0;
		
		switch (relativeOrientation)
		{
		case 0: // Facing away
			meta = (new int[]{1, 2, 0, 3})[coordBaseMode]; break;
		case 1: // Facing right
			meta = coordBaseMode%2==0 ? 3 : 1; break;
		default:
		case 2: // Facing you
			meta = (new int[]{0, 3, 1, 2})[coordBaseMode]; break;
		case 3: // Facing left
			meta = coordBaseMode%2==0 ? 2 : 0; break;
		}
		
		meta += (isVertical?4:0);
		meta += (isTop?8:0);
		
		return meta;
	}
	

	/*
	 * Used to select wooden door types.
	 */
	public static Block chooseWoodenDoor(int materialMeta)
	{
		if (!GeneralConfig.useModdedWoodenDoors) {return Blocks.wooden_door;} // Returns the default oak door always
		
		if (materialMeta==0) {return Blocks.wooden_door;}
		
		String[] modprioritylist = GeneralConfig.modDoor;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().equals("uptodate"))
			{
				switch (materialMeta)
				{
					case 1: modblock = Block.getBlockFromName(ModObjects.doorSpruceUTD); break;
					case 2: modblock = Block.getBlockFromName(ModObjects.doorBirchUTD); break;
					case 3: modblock = Block.getBlockFromName(ModObjects.doorJungleUTD); break;
					case 4: modblock = Block.getBlockFromName(ModObjects.doorAcaciaUTD); break;
					case 5: modblock = Block.getBlockFromName(ModObjects.doorDarkOakUTD); break;
				}
				if (modblock != null) {return modblock;}
			}
			if (mod.toLowerCase().equals("etfuturum"))
			{
				switch (materialMeta)
				{
					case 1: modblock = Block.getBlockFromName(ModObjects.doorSpruceEF); break;
					case 2: modblock = Block.getBlockFromName(ModObjects.doorBirchEF); break;
					case 3: modblock = Block.getBlockFromName(ModObjects.doorJungleEF); break;
					case 4: modblock = Block.getBlockFromName(ModObjects.doorAcaciaEF); break;
					case 5: modblock = Block.getBlockFromName(ModObjects.doorDarkOakEF); break;
				}
				if (modblock != null) {return modblock;}
			}
			if (mod.toLowerCase().equals("ganyssurface"))
			{
				switch (materialMeta)
				{
					case 1: modblock = Block.getBlockFromName(ModObjects.doorSpruceGS); break;
					case 2: modblock = Block.getBlockFromName(ModObjects.doorBirchGS); break;
					case 3: modblock = Block.getBlockFromName(ModObjects.doorJungleGS); break;
					case 4: modblock = Block.getBlockFromName(ModObjects.doorAcaciaGS); break;
					case 5: modblock = Block.getBlockFromName(ModObjects.doorDarkOakGS); break;
				}
				if (modblock != null) {return modblock;}
			}
		}
		// If all else fails, grab the vanilla version
		return Blocks.wooden_door;
	}
	

	/*
	 * Used to select wooden door types.
	 */
	public static Block chooseWoodenButton(int materialMeta)
	{
		if (!GeneralConfig.useModdedWoodenDoors) {return Blocks.wooden_button;} // Returns the default oak door always
		
		if (materialMeta==0) {return Blocks.wooden_button;}
		
		String[] modprioritylist = GeneralConfig.modButton;
		
		for (String mod : modprioritylist)
		{
			Block modblock=null;
			
			if (mod.toLowerCase().equals("uptodate"))
			{
				switch (materialMeta)
				{
					case 1: modblock = Block.getBlockFromName(ModObjects.buttonSpruceUTD); break;
					case 2: modblock = Block.getBlockFromName(ModObjects.buttonBirchUTD); break;
					case 3: modblock = Block.getBlockFromName(ModObjects.buttonJungleUTD); break;
					case 4: modblock = Block.getBlockFromName(ModObjects.buttonAcaciaUTD); break;
					case 5: modblock = Block.getBlockFromName(ModObjects.buttonDarkOakUTD); break;
				}
				if (modblock != null) {return modblock;}
			}
			if (mod.toLowerCase().equals("etfuturum"))
			{
				switch (materialMeta)
				{
					case 1: modblock = Block.getBlockFromName(ModObjects.buttonSpruceEF); break;
					case 2: modblock = Block.getBlockFromName(ModObjects.buttonBirchEF); break;
					case 3: modblock = Block.getBlockFromName(ModObjects.buttonJungleEF); break;
					case 4: modblock = Block.getBlockFromName(ModObjects.buttonAcaciaEF); break;
					case 5: modblock = Block.getBlockFromName(ModObjects.buttonDarkOakEF); break;
				}
				if (modblock != null) {return modblock;}
			}
			if (mod.toLowerCase().equals("ganyssurface"))
			{
				switch (materialMeta)
				{
					case 1: modblock = Block.getBlockFromName(ModObjects.buttonSpruceGS); break;
					case 2: modblock = Block.getBlockFromName(ModObjects.buttonBirchGS); break;
					case 3: modblock = Block.getBlockFromName(ModObjects.buttonJungleGS); break;
					case 4: modblock = Block.getBlockFromName(ModObjects.buttonAcaciaGS); break;
					case 5: modblock = Block.getBlockFromName(ModObjects.buttonDarkOakGS); break;
				}
				if (modblock != null) {return modblock;}
			}
		}
		// If all else fails, grab the vanilla version
		return Blocks.wooden_button;
	}
	
	
	/**
	 * Used to select crops. Returns a two-element array that are either two different harvestcraft/JAFFA crops, or the same vanilla/beetroot crop twice.
	 */
	public static Block[] chooseCropPair(Random random)
	{
		Block[] cropblocks = new Block[]{Blocks.wheat, Blocks.wheat}; // What ultimately to return
		ArrayList<Block> cropArray = new ArrayList(); // The array from which to choose crops
		
		// TODO - Substitute with other mod versions
		
		if (random.nextDouble() < GeneralConfig.harvestcraftCropFarmRate)
		{
			Block tryModCrop;
			
			tryModCrop = Block.getBlockFromName(ModObjects.cropBambooHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropSesameseedHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropBlackberryHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropOnionHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropTealeafHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropCucumberHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropScallionHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropCornHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropRhubarbHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropTomatoHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropCottonHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropPeanutHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropSweetpotatoHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropBeetHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropPineappleHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropStrawberryHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropArtichokeHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropOkraHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropRutabegaHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropParsnipHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropCandleberryHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropCantaloupeHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropWintersquashHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropRaspberryHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropCoffeebeanHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropSoybeanHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropSpiceleafHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropCeleryHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropBellpepperHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropBeanHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropCabbageHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropZucchiniHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropLeekHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropLettuceHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropBrusselsproutHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropCauliflowerHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropPeasHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropKiwiHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropRyeHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropChilipepperHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropRadishHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropCurryleafHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropGrapeHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropMustardseedHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropEggplantHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropGarlicHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropBarleyHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropBroccoliHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropSpinachHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropAsparagusHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropGingerHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropOatsHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropBlueberryHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropTurnipHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropKaleJAFFA); if (tryModCrop != null) {cropArray.add(tryModCrop);}
		}

		if (random.nextDouble() < GeneralConfig.dragonQuestCropFarmRate)
		{
			Block tryModCrop;
			
			tryModCrop = Block.getBlockFromName(ModObjects.cropHerbDRQ); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropAntidoteHerbDRQ); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropStrengthSeedDRQ); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropDefenceSeedDRQ); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropAgilitySeedDRQ); if (tryModCrop != null) {cropArray.add(tryModCrop);}
		}
		
		// If there are any modded crops added, return them.
		if (cropArray.size()>0)
		{
			for (int i=0; i<2; i++) {cropblocks[i] = cropArray.get(random.nextInt(cropArray.size()));}
			return cropblocks;
		}
		
		// If you've reached this stage, you will be returning vanilla crops
		cropArray.add(Blocks.wheat); cropArray.add(Blocks.wheat); cropArray.add(Blocks.wheat); cropArray.add(Blocks.wheat); cropArray.add(Blocks.wheat);
		cropArray.add(Blocks.carrots); cropArray.add(Blocks.carrots);
		cropArray.add(Blocks.potatoes); cropArray.add(Blocks.potatoes);
		
		// Add backported blocks
		Block tryBeetroot = ModObjects.chooseModBeetrootCrop();
		if (tryBeetroot != null) {cropArray.add(tryBeetroot);}
		
		// If the array is empty, either because harvestcraft crops were not used or were not selected, then choose a vanilla crop
		Block cropblock = cropArray.get(random.nextInt(cropArray.size()));
		cropblocks[0] = cropblock;
		cropblocks[1] = cropblock;
		
		return cropblocks;
	}
	
	/**
	 * Inputs an int array of colors being used in a village, and returns one that is not in use
	 * useWeighted uses the banner color weighting. Set to false for uniform. 
	 */
	public static int generateUnusedColor(int[] colorArray, Random random, boolean useWeighted)
	{
		int candidateColor = (useWeighted ? (Integer) FunctionsVN.weightedRandom(BannerGenerator.colorMeta, BannerGenerator.colorWeights, random) : random.nextInt(16));
		if (colorArray.length>=16) {return candidateColor;} // If it's not possible to return a unique color (pigeonhole principle), just return a random one
		boolean matchFound=false;
		
		// Then check the color against the provided array
		while (true)
		{
			candidateColor = (useWeighted ? (Integer) FunctionsVN.weightedRandom(BannerGenerator.colorMeta, BannerGenerator.colorWeights, random) : random.nextInt(16));
			
			for (int color : colorArray)
			{
				if (color==candidateColor) {matchFound=true;}
			}
			
			if (matchFound) {matchFound=false; continue;}
			else {return candidateColor;}
		}
	}
}
