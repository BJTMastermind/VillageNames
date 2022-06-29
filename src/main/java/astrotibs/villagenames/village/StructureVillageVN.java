package astrotibs.villagenames.village;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.capabilities.ModularSkinProvider;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.config.village.VillageGeneratorConfigHandler;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.nbt.VNWorldDataStructure;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.FunctionsVN.MaterialType;
import astrotibs.villagenames.utility.FunctionsVN.VillageType;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.utility.Reference;
import astrotibs.villagenames.village.biomestructures.BlueprintData;
import astrotibs.villagenames.village.biomestructures.DesertStructures;
import astrotibs.villagenames.village.biomestructures.JungleStructures;
import astrotibs.villagenames.village.biomestructures.PlainsStructures;
import astrotibs.villagenames.village.biomestructures.SavannaStructures;
import astrotibs.villagenames.village.biomestructures.SnowyStructures;
import astrotibs.villagenames.village.biomestructures.SwampStructures;
import astrotibs.villagenames.village.biomestructures.TaigaStructures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.structure.MapGenStructureData;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class StructureVillageVN
{
	public static final int VILLAGE_RADIUS_BUFFER = 112;
	
	// Indexed by [orientation][horizIndex]
	// 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	public static final int[][] HANGING_META_ARRAY = new int[][]{
		{0,1,2,3}, // Fore-facing (away from you)
		{3,0,3,0}, // Right-facing
		{2,3,0,1}, // Back-facing (toward you)
		{1,2,1,2}, // Left-facing
	//   N E S W
	};
	
	// Indexed by [orientation]
	public static final int[] VINE_META_ARRAY = new int[]{1,2,4,8};
	
	// Indexed by [orientation]
	public static final int[] LADDER_META_ARRAY = new int[]{2,5,3,4};
	
	// Indexed by [orientation][horizIndex]
	public static final int[][] FURNACE_META_ARRAY = new int[][]{
		{3,4,2,5},
		{5,3,5,3},
		{2,5,3,4},
		{4,2,4,2},
	};
	
	// Indexed by [orientation][horizIndex]
	public static final int[][] BED_META_ARRAY = new int[][]{
		{2,3,0,1}, // Forward
		{1,2,1,2}, // Right
		{0,1,2,3}, // Back
		{3,0,3,0}, // Left
	   //N E S W
	};

	// Indexed by [orientation][horizIndex]
	public static final int[] BUTTON_META_ARRAY = new int[]{4,1,3,2};
	
	// Indexed by [orientation][horizIndex]
	public static final int[][] BLAST_FURNACE_META_ARRAY = new int[][]{
		{0,1,2,3},
		{3,0,3,0},
		{2,3,0,1},
		{1,2,1,2},
	};
	
	// Indexed by [orientation][horizIndex]
	public static final int[][] HANGING_GRINDSTONE_META_ARRAY = new int[][]{
		{6,7,4,5},
		{5,6,5,6},
		{4,5,6,7},
		{7,4,7,4},
	};
	
	// Indexed by [orientation][horizIndex]
	public static final int[][] CARTOGRAPHY_TABLE_META_ARRAY = new int[][]{
		{2,5,3,4},
		{4,2,4,2},
		{3,4,2,5},
		{5,3,5,3},
	};
	
	// Array of meta values for furnaces indexed by [orientation][horizIndex]
	public static final int[][] ANVIL_META_ARRAY = new int[][]{
		{3,3,1,1}, // Facing forward
		{2,2,2,2}, // Facing right
		{1,1,3,3}, // Facing you
		{0,0,0,0}, // Facing left
	};
	
	// Indexed by [orientation][horizIndex]
	public static final int[][] GLAZED_TERRACOTTA_META_ARRAY = new int[][]{
		{1,2,2,3},
		{0,1,3,0},
		{3,0,0,1},
		{2,3,1,2},
	};
	
	// Indexed by [orientation]
	public static final int[] DOOR_META_ARRAY = new int[]
	// --- LOWER HALF --- //
	// Shut
	{1,2,3,0};
	
	// Array of meta values for furnaces indexed by [orientation][horizIndex]
	public static final int[][] BIBLIOCRAFT_DESK_META_ARRAY = new int[][]{
		{3,0,1,2}, 
		{2,3,2,3}, 
		{1,2,3,0}, 
		{0,1,0,1}, 
	};

	// Indexed by [orientation]
	public static final int[] FENCE_GATE_META_ARRAY = new int[]
	{0,1,2,3,};
	
	
    public static List getStructureVillageWeightedPieceList(Random random, float villageSize, FunctionsVN.VillageType villageType)
    {
        ArrayList arraylist = new ArrayList();
        
    	// Legacy structures
        if (VillageGeneratorConfigHandler.componentLegacyHouse4Garden_vals.get(0)>0)
        {
        	ArrayList<Double> ald = VillageGeneratorConfigHandler.componentLegacyHouse4Garden_vals;
	    	double weightDouble = ald.get(0); int weightStochastic = MathHelper.floor(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ald.get(1) + ald.get(2); int lowerLimitStochastic = MathHelper.floor(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ald.get(3) + ald.get(4); int upperLimitStochastic = MathHelper.floor(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House4Garden.class, weightStochastic, MathHelper.getInt(random, lowerLimitStochastic, upperLimitStochastic)));
        }
        if (VillageGeneratorConfigHandler.componentLegacyChurch_vals.get(0)>0)
        {
        	ArrayList<Double> ald = VillageGeneratorConfigHandler.componentLegacyChurch_vals;
	    	double weightDouble = ald.get(0); int weightStochastic = MathHelper.floor(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ald.get(1) + ald.get(2); int lowerLimitStochastic = MathHelper.floor(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ald.get(3) + ald.get(4); int upperLimitStochastic = MathHelper.floor(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Church.class, weightStochastic, MathHelper.getInt(random, lowerLimitStochastic, upperLimitStochastic)));
        }
        if (VillageGeneratorConfigHandler.componentLegacyHouse1_vals.get(0)>0)
        {
        	ArrayList<Double> ald = VillageGeneratorConfigHandler.componentLegacyHouse1_vals;
	    	double weightDouble = ald.get(0); int weightStochastic = MathHelper.floor(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ald.get(1) + ald.get(2); int lowerLimitStochastic = MathHelper.floor(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ald.get(3) + ald.get(4); int upperLimitStochastic = MathHelper.floor(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House1.class, weightStochastic, MathHelper.getInt(random, lowerLimitStochastic, upperLimitStochastic)));
        }
        if (VillageGeneratorConfigHandler.componentLegacyWoodHut_vals.get(0)>0)
        {
        	ArrayList<Double> ald = VillageGeneratorConfigHandler.componentLegacyWoodHut_vals;
	    	double weightDouble = ald.get(0); int weightStochastic = MathHelper.floor(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ald.get(1) + ald.get(2); int lowerLimitStochastic = MathHelper.floor(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ald.get(3) + ald.get(4); int upperLimitStochastic = MathHelper.floor(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.WoodHut.class, weightStochastic, MathHelper.getInt(random, lowerLimitStochastic, upperLimitStochastic)));
        }
        if (VillageGeneratorConfigHandler.componentLegacyHall_vals.get(0)>0)
        {
        	ArrayList<Double> ald = VillageGeneratorConfigHandler.componentLegacyHall_vals;
	    	double weightDouble = ald.get(0); int weightStochastic = MathHelper.floor(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ald.get(1) + ald.get(2); int lowerLimitStochastic = MathHelper.floor(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ald.get(3) + ald.get(4); int upperLimitStochastic = MathHelper.floor(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Hall.class, weightStochastic, MathHelper.getInt(random, lowerLimitStochastic, upperLimitStochastic)));
        }
        if (VillageGeneratorConfigHandler.componentLegacyField1_vals.get(0)>0)
        {
        	ArrayList<Double> ald = VillageGeneratorConfigHandler.componentLegacyField1_vals;
	    	double weightDouble = ald.get(0); int weightStochastic = MathHelper.floor(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ald.get(1) + ald.get(2); int lowerLimitStochastic = MathHelper.floor(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ald.get(3) + ald.get(4); int upperLimitStochastic = MathHelper.floor(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field1.class, weightStochastic, MathHelper.getInt(random, lowerLimitStochastic, upperLimitStochastic)));
        }
        if (VillageGeneratorConfigHandler.componentLegacyField2_vals.get(0)>0)
        {
        	ArrayList<Double> ald = VillageGeneratorConfigHandler.componentLegacyField2_vals;
	    	double weightDouble = ald.get(0); int weightStochastic = MathHelper.floor(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ald.get(1) + ald.get(2); int lowerLimitStochastic = MathHelper.floor(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ald.get(3) + ald.get(4); int upperLimitStochastic = MathHelper.floor(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field2.class, weightStochastic, MathHelper.getInt(random, lowerLimitStochastic, upperLimitStochastic)));
        }
        if (VillageGeneratorConfigHandler.componentLegacyHouse2_vals.get(0)>0)
        {
        	ArrayList<Double> ald = VillageGeneratorConfigHandler.componentLegacyHouse2_vals;
	    	double weightDouble = ald.get(0); int weightStochastic = MathHelper.floor(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ald.get(1) + ald.get(2); int lowerLimitStochastic = MathHelper.floor(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ald.get(3) + ald.get(4); int upperLimitStochastic = MathHelper.floor(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House2.class, weightStochastic, MathHelper.getInt(random, lowerLimitStochastic, upperLimitStochastic)));
        }
        if (VillageGeneratorConfigHandler.componentLegacyHouse3_vals.get(0)>0)
        {
        	ArrayList<Double> ald = VillageGeneratorConfigHandler.componentLegacyHouse3_vals;
	    	double weightDouble = ald.get(0); int weightStochastic = MathHelper.floor(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ald.get(1) + ald.get(2); int lowerLimitStochastic = MathHelper.floor(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ald.get(3) + ald.get(4); int upperLimitStochastic = MathHelper.floor(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House3.class, weightStochastic, MathHelper.getInt(random, lowerLimitStochastic, upperLimitStochastic)));
        }
        
        VillagerRegistry.addExtraVillageComponents(arraylist, random, Math.floor(villageSize)+villageSize%1<random.nextFloat()?1:0); // Round to integer stochastically

        
        // keys: "ClassPaths", "VillageTypes"
		Map<String, ArrayList> mappedComponentVillageTypes = VillageGeneratorConfigHandler.unpackComponentVillageTypes(VillageGeneratorConfigHandler.componentVillageTypes);
		Map<String, ArrayList> mappedComponentVillageTypesNonModDefaults = VillageGeneratorConfigHandler.unpackComponentVillageTypes(VillageGeneratorConfigHandler.MODERN_VANILLA_COMPONENT_VILLAGE_TYPE_DEFAULTS);
        
        Iterator iterator = arraylist.iterator();
        
        String villageTypeToCompare = villageType.toString().toLowerCase();
        
        while (iterator.hasNext())
        {
        	PieceWeight pw = (StructureVillagePieces.PieceWeight)iterator.next();
        	
        	// Remove all buildings that rolled 0 for number or which have a weight of 0
            if (pw.villagePiecesLimit == 0 || pw.villagePieceWeight <=0) {iterator.remove(); continue;}
            
            // Remove vanilla buildings re-added by other mods (?)
            if (VillageGeneratorConfigHandler.componentLegacyHouse4Garden_vals.get(0)<=0 && pw.villagePieceClass.toString().substring(6).equals(Reference.House4Garden_CLASS)) {iterator.remove(); continue;}
            if (VillageGeneratorConfigHandler.componentLegacyChurch_vals.get(0)<=0 && pw.villagePieceClass.toString().substring(6).equals(Reference.Church_CLASS)) {iterator.remove(); continue;}
            if (VillageGeneratorConfigHandler.componentLegacyHouse1_vals.get(0)<=0 && pw.villagePieceClass.toString().substring(6).equals(Reference.House1_CLASS)) {iterator.remove(); continue;}
            if (VillageGeneratorConfigHandler.componentLegacyWoodHut_vals.get(0)<=0 && pw.villagePieceClass.toString().substring(6).equals(Reference.WoodHut_CLASS)) {iterator.remove(); continue;}
            if (VillageGeneratorConfigHandler.componentLegacyHall_vals.get(0)<=0 && pw.villagePieceClass.toString().substring(6).equals(Reference.Hall_CLASS)) {iterator.remove(); continue;}
            if (VillageGeneratorConfigHandler.componentLegacyField1_vals.get(0)<=0 && pw.villagePieceClass.toString().substring(6).equals(Reference.Field1_CLASS)) {iterator.remove(); continue;}
            if (VillageGeneratorConfigHandler.componentLegacyField2_vals.get(0)<=0 && pw.villagePieceClass.toString().substring(6).equals(Reference.Field2_CLASS)) {iterator.remove(); continue;}
            if (VillageGeneratorConfigHandler.componentLegacyHouse2_vals.get(0)<=0 && pw.villagePieceClass.toString().substring(6).equals(Reference.House2_CLASS)) {iterator.remove(); continue;}
            if (VillageGeneratorConfigHandler.componentLegacyHouse3_vals.get(0)<=0 && pw.villagePieceClass.toString().substring(6).equals(Reference.House3_CLASS)) {iterator.remove(); continue;}
            
            // Remove buildings that aren't appropriate for the current biome
			int classPathListIndex = mappedComponentVillageTypes.get("ClassPaths").indexOf(pw.villagePieceClass.toString().substring(6));
			
			if (classPathListIndex==-1) // It's not in the "Component Village Types" config entries
			{
				int classPathListIndexNonModDefaults = mappedComponentVillageTypesNonModDefaults.get("ClassPaths").indexOf(pw.villagePieceClass.toString().substring(6));
				
				if (classPathListIndexNonModDefaults!=-1) // It is in the "Component Village Types" default values
				{
					if (
							!((String) ((mappedComponentVillageTypesNonModDefaults.get("VillageTypes")).get(classPathListIndexNonModDefaults))).trim().toLowerCase().equals("any")
							&& !((String) ((mappedComponentVillageTypesNonModDefaults.get("VillageTypes")).get(classPathListIndexNonModDefaults))).trim().toLowerCase().equals("all")
							&& (!((String) ((mappedComponentVillageTypesNonModDefaults.get("VillageTypes")).get(classPathListIndexNonModDefaults))).trim().toLowerCase().contains(villageTypeToCompare)
							|| ((String) ((mappedComponentVillageTypesNonModDefaults.get("VillageTypes")).get(classPathListIndexNonModDefaults))).trim().toLowerCase().equals(""))
							)
					{
						iterator.remove(); continue;
					}
				}
			}
			else if (
						!((String) ((mappedComponentVillageTypes.get("VillageTypes")).get(classPathListIndex))).trim().toLowerCase().equals("any")
						&& !((String) ((mappedComponentVillageTypes.get("VillageTypes")).get(classPathListIndex))).trim().toLowerCase().equals("all")
						&& (!((String) ((mappedComponentVillageTypes.get("VillageTypes")).get(classPathListIndex))).trim().toLowerCase().contains(villageTypeToCompare)
						|| ((String) ((mappedComponentVillageTypes.get("VillageTypes")).get(classPathListIndex))).trim().toLowerCase().equals(""))
						)
            {
            	iterator.remove(); continue;
            }
        }
        
        return arraylist;
    }
    
    
    // Pasted in from StructureVillagePieces so that I can access it, particularly to expand the allowed village biomes
    // This prepares a new path component to build upon
    // Called func_176066_d in 1.8
    public static StructureComponent getNextVillageStructureComponent(StartVN start, List<StructureComponent> components, Random random, int x, int y, int z, EnumFacing facing, int componentType)
    {
        if (componentType > 50)
        {
            return null;
        }
        else if (Math.abs(x - start.getBoundingBox().minX) <= 112 && Math.abs(z - start.getBoundingBox().minZ) <= 112)
        {
        	
			// Attach another structure
			Method generateComponent_reflected = ReflectionHelper.findMethod(
					StructureVillagePieces.class, "generateComponent", "func_176067_c",
					StructureVillagePieces.Start.class, List.class, Random.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, EnumFacing.class, Integer.TYPE
					);
			
			StructureVillagePieces.Village structurecomponent = null;
			try {structurecomponent = (StructureVillagePieces.Village)generateComponent_reflected.invoke(start, (StructureVillagePieces.Start)start, components, random, x, y, z, facing, componentType + 1);}
    		catch (Exception e) {if (GeneralConfig.debugMessages) LogHelper.warn("Could not invoke StructureVillagePieces.generateComponent method");}
			
        	//StructureComponent structurecomponent = func_176067_c(start, components, rand, x, y, z, facing, componentType + 1);
            
            if (structurecomponent != null)
            {
            	// Substitute old torch with the new one
            	if (structurecomponent instanceof StructureVillagePieces.Torch)
            	{
            		StructureBoundingBox decorTorchBB = StructureVillageVN.DecorTorch.findPieceBox(start, components, random, x, y, z, facing);
            		if (decorTorchBB==null) {return null;}
            		structurecomponent = new StructureVillageVN.DecorTorch(start, componentType, random, decorTorchBB, facing);
            	}
            	
                int medianX = (structurecomponent.getBoundingBox().minX + structurecomponent.getBoundingBox().maxX) / 2;
                int medianZ = (structurecomponent.getBoundingBox().minZ + structurecomponent.getBoundingBox().maxZ) / 2;
                int rangeX = structurecomponent.getBoundingBox().maxX - structurecomponent.getBoundingBox().minX;
                int rangeZ = structurecomponent.getBoundingBox().maxZ - structurecomponent.getBoundingBox().minZ;
                int bboxWidth = rangeX > rangeZ ? rangeX : rangeZ;
                
                // Replaces the ordinary "areBiomesViable" method with one that uses the VN biome config list
                Biome biome = start.biomeProvider.getBiome(new BlockPos(medianX, 0, medianZ));

            	if (VillageGeneratorConfigHandler.spawnBiomesNames != null) // Biome list is not empty
        		{
            		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            		
        			for (int i = 0; i < mappedBiomes.get("BiomeNames").size(); i++)
        			{
        				if (mappedBiomes.get("BiomeNames").get(i).equals((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))))
        				{
        					BiomeManager.addVillageBiome(biome, true); // Set biome to be able to spawn villages
        					
        					components.add(structurecomponent);
                            start.pendingHouses.add(structurecomponent);
                            return structurecomponent;
        				}
        			}
        		}
            	/*
                if (start.getBiomeProvider().areBiomesViable(medianX, medianZ, bboxWidth / 2 + 4, MapGenVillage.villageSpawnBiomes))
                {
                    components.add(structurecomponent);
                    start.pendingHouses.add(structurecomponent);
                    return structurecomponent;
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
    
    
    /**
     * Pasted in from StructureVillagePieces.Path findPieceBox so that I can access it
     */
    public static StructureBoundingBox findPieceBox(StructureVillagePieces.Start start, List<StructureComponent> components, Random random, int x, int y, int z, EnumFacing coordBaseMode)
    {
    	// Select a length for the road. Start with a random length of 21, 28, or 35,
    	// and then decrement 7 blocks at a time until the road doesn't intersect with any generated components
        for (int i = 7 * MathHelper.getInt(random, 3, 5); i >= 7; i -= 7)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
            		x, y, z, // Structure coordinates
            		0, 0, 0, // u, v, w offset. The u and w offsets are positive for horizIndex <=1 and negative for horizIndex >=2
            		3, 3, i, // width, height, depth
            		coordBaseMode);
            
            if (StructureComponent.findIntersecting(components, structureboundingbox) == null)
            {
                return structureboundingbox;
            }
        }
        
        return null;
    }
    
    
    // Pasted in from StructureVillagePieces so that I can access it
    // This prepares a new path component to build upon
    // func_176069_e for 1.8
    public static StructureComponent generateAndAddRoadPiece(StructureVillagePieces.Start start, List<StructureComponent> components, Random rand, int x, int y, int z, EnumFacing facing, int componentType)
    {
    	if (componentType > 3 + start.terrainType) {return null;} // Idk what this is
        else if (Math.abs(x - start.getBoundingBox().minX) <= 112 && Math.abs(z - start.getBoundingBox().minZ) <= 112)
        {
            StructureBoundingBox structureboundingbox = StructureVillagePieces.Path.findPieceBox(start, components, rand, x, y, z, facing);

            if (structureboundingbox != null && structureboundingbox.minY > 10)
            {
            	StructureVillageVN.PathVN path = new StructureVillageVN.PathVN(start, componentType, rand, structureboundingbox, facing);
                int medianX = (path.getBoundingBox().minX + path.getBoundingBox().maxX) / 2;
                int medianZ = (path.getBoundingBox().minZ + path.getBoundingBox().maxZ) / 2;
                int rangeX = path.getBoundingBox().maxX - path.getBoundingBox().minX;
                int rangeZ = path.getBoundingBox().maxZ - path.getBoundingBox().minZ;
                int bboxWidth = rangeX > rangeZ ? rangeX : rangeZ;
                
                // Replaces the ordinary "areBiomesViable" method with one that uses the VN biome config list
                Biome biome = start.biomeProvider.getBiome(new BlockPos(medianX, 0, medianZ));

            	if (VillageGeneratorConfigHandler.spawnBiomesNames != null) // Biome list is not empty
        		{
            		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            		
        			for (int i = 0; i < mappedBiomes.get("BiomeNames").size(); i++)
        			{
        				if (mappedBiomes.get("BiomeNames").get(i).equals((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))))
        				{
        					BiomeManager.addVillageBiome(biome, true); // Set biome to be able to spawn villages
        					
        					components.add(path);
                            start.pendingRoads.add(path);
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
    public static BlockPos getAboveTopmostSolidOrLiquidBlockVN(World world, BlockPos pos)
    {
        
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        BlockPos blockpos;
        BlockPos blockpos1;

        for (blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos.getY() >= 0; blockpos = blockpos1)
        {
            blockpos1 = blockpos.down();
            IBlockState blockstate = chunk.getBlockState(blockpos1);
            Block block = blockstate.getBlock();
            Material material = blockstate.getMaterial();
            
            if (
            		// If it's a solid, full block that isn't one of these particular types
            		(material.blocksMovement()
    				&& !block.isLeaves(world.getBlockState(blockpos1), world, blockpos1)
    				&& material != Material.LEAVES
					&& material != Material.PLANTS
					&& material != Material.VINE
					&& material != Material.AIR
    				&& !block.isFoliage(world, blockpos1))
            		&& blockstate.isNormalCube()
            		// If the block is liquid, return the value above it
            		|| material.isLiquid()
            		)
            {
                break;
            }
        }
        
        return blockpos;
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
                if (boundingBox.isVecInside(new BlockPos(l, 64, k)))
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
                		int aboveTopLevel = getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(l, 64, k)).getY();
                		if (aboveTopLevel != -1) {i.add(aboveTopLevel);}
                	}
                }
            }
        }
        
        return FunctionsVN.medianIntArray(i, true);
    }
    
    /**
     * Biome-specific block replacement
     */
    public static IBlockState getBiomeSpecificBlockState(IBlockState blockstate, MaterialType materialType, Biome biome, boolean disallowModSubs)
    {
    	if (materialType==null || biome==null) {return blockstate;}
    	
    	Block block = blockstate.getBlock();
    	int meta = block.getMetaFromState(blockstate);
    	int woodMeta = 0;
    	
    	switch (materialType)
    	{
    	default:
    	case OAK:
    		woodMeta = BlockPlanks.EnumType.OAK.getMetadata();
    		
    		if (block == Blocks.LOG || block == Blocks.LOG2)   {blockstate=Blocks.LOG.getStateFromMeta(woodMeta%4); break;}
    		if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {blockstate=Blocks.LEAVES.getStateFromMeta(woodMeta%4); break;}
        	if (block == Blocks.SANDSTONE && meta==2)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(0); break;} // Cut sandstone into stone brick
        	if (block == Blocks.SANDSTONE && meta==1)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(3); break;} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.SANDSTONE)                     {blockstate=Blocks.COBBLESTONE.getStateFromMeta(0); break;}
        	if (
        			(block == Blocks.STONE_SLAB && (meta==1 || meta==9))
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)
        					|| block == Block.getBlockFromName(ModObjects.smoothRedSandstoneSlabQu)
        			))
        			) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==1? 3: meta==9? 11 : meta); break;}
        	if (
        			(block == Blocks.DOUBLE_STONE_SLAB && meta==9)
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstone_white_Qu)
        					|| block == Block.getBlockFromName(ModObjects.smoothSandstone_white_FMC)
        			))
        			) {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;} // Smooth sandstone into planks
        	if (block == Blocks.DOUBLE_STONE_SLAB)             {blockstate=Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(meta==1? 0 : meta); break;}
        	if (block == Blocks.SANDSTONE_STAIRS)              {blockstate=Blocks.STONE_STAIRS.getStateFromMeta(meta); break;}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==8 ? 11: meta==0? 3:meta); break;}
        	if (block != null && ( 
        			block == Block.getBlockFromName(ModObjects.sandstoneWall_white_Qu)
        			|| block == Block.getBlockFromName(ModObjects.sandstoneWall_white_FMC)
        			)) {blockstate=Blocks.OAK_FENCE.getDefaultState(); break;}
        	if (block == Blocks.SAPLING)                       {blockstate=Blocks.SAPLING.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.SNOW)                          {blockstate=Blocks.DIRT.getDefaultState(); break;}
        	if (block == Blocks.SNOW_LAYER)                    {blockstate=Blocks.AIR.getDefaultState(); break;}
        	if (block == Blocks.ICE)                           {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.PACKED_ICE)                    {blockstate=Blocks.COBBLESTONE.getDefaultState(); break;}
        	if (block != null && block==Block.getBlockFromName(ModObjects.blueIceFMC)) {blockstate=Blocks.STONEBRICK.getDefaultState(); break;}
        	
        	break;
        	
    	case SPRUCE:
    		woodMeta = BlockPlanks.EnumType.SPRUCE.getMetadata();
    		
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {blockstate=Blocks.LOG.getStateFromMeta(woodMeta%4); break;}
    		if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {blockstate=Blocks.LEAVES.getStateFromMeta(woodMeta+4); break;}
        	if (block == Blocks.PLANKS)                        {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.OAK_FENCE)					   {blockstate=Blocks.SPRUCE_FENCE.getDefaultState(); break;}
	        if (block == Blocks.OAK_FENCE_GATE)				   {blockstate=Blocks.SPRUCE_FENCE_GATE.getDefaultState(); break;}
        	if (block == Blocks.OAK_STAIRS)                    {blockstate=Blocks.SPRUCE_STAIRS.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_SLAB)                   {blockstate=Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +woodMeta: meta==8? 8 +woodMeta : meta); break;}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {blockstate=Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
        													   {blockstate=Blocks.SPRUCE_DOOR.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_BUTTON)                 {blockstate=ModObjects.chooseWoodenButton(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_PRESSURE_PLATE)         {blockstate=ModObjects.chooseModPressurePlate(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.TRAPDOOR)                      {blockstate=ModObjects.chooseModWoodenTrapdoor(woodMeta).getStateFromMeta(meta); break;}
        	//if (block == Blocks.standing_sign)                 {blockstate=new Object[]{ModObjects.chooseModWoodenSign(1, true), meta/4}; break;}
        	//if (block == Blocks.wall_sign)                     {blockstate=new Object[]{ModObjects.chooseModWoodenSign(1, false), meta}; break;}
        	if (block != null &&
        			(block == Block.getBlockFromName(ModObjects.woodQu)
        				|| block == Block.getBlockFromName(ModObjects.woodFV_spruce))) {blockstate=block.getStateFromMeta(woodMeta); break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLogSpruceUTD), meta}; break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 1}; break;}
        	if (block == Blocks.SANDSTONE && meta==2)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(0); break;} // Cut sandstone into stone brick
        	if (block == Blocks.SANDSTONE && meta==1)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(3); break;} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.SANDSTONE)                     {blockstate=Blocks.COBBLESTONE.getStateFromMeta(0); break;}
        	if (
        			(block == Blocks.STONE_SLAB && (meta==1 || meta==9))
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)
        					|| block == Block.getBlockFromName(ModObjects.smoothRedSandstoneSlabQu)
        			))
        			) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==1? 3: meta==9? 11 : meta); break;}
        	if (
        			(block == Blocks.DOUBLE_STONE_SLAB && meta==9)
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstone_white_Qu)
        					|| block == Block.getBlockFromName(ModObjects.smoothSandstone_white_FMC)
        			))
        			) {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;} // Smooth sandstone into planks
        	if (block == Blocks.DOUBLE_STONE_SLAB)             {blockstate=Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(meta==1? 0 : meta); break;}
        	if (block == Blocks.SANDSTONE_STAIRS)              {blockstate=Blocks.STONE_STAIRS.getStateFromMeta(meta); break;}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==8 ? 11: meta==0? 3:meta); break;}
        	if (block != null && ( 
        			block == Block.getBlockFromName(ModObjects.sandstoneWall_white_Qu)
        			|| block == Block.getBlockFromName(ModObjects.sandstoneWall_white_FMC)
        			)) {blockstate=Blocks.SPRUCE_FENCE.getDefaultState(); break;}
        	if (block == Blocks.SAPLING)                       {blockstate=Blocks.SAPLING.getStateFromMeta(woodMeta); break;}
        	// No snow conversion because snow is okay in spruce biomes
        	if (block == Blocks.ICE)                           {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.PACKED_ICE)                    {blockstate=Blocks.COBBLESTONE.getDefaultState(); break;}
        	if (block != null && block==Block.getBlockFromName(ModObjects.blueIceFMC)) {blockstate=Blocks.STONEBRICK.getDefaultState(); break;}
        	
        	break;
        	
        case BIRCH:
    		woodMeta = BlockPlanks.EnumType.BIRCH.getMetadata();
    		
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {blockstate=Blocks.LOG.getStateFromMeta(woodMeta%4); break;}
    		if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {blockstate=Blocks.LEAVES.getStateFromMeta(woodMeta+4); break;}
        	if (block == Blocks.PLANKS)                        {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.OAK_FENCE)					   {blockstate=Blocks.BIRCH_FENCE.getDefaultState(); break;}
        	if (block == Blocks.OAK_FENCE_GATE)				   {blockstate=Blocks.BIRCH_FENCE_GATE.getDefaultState(); break;}
        	if (block == Blocks.OAK_STAIRS)                    {blockstate=Blocks.BIRCH_STAIRS.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_SLAB)                   {blockstate=Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +woodMeta: meta==8? 8 +woodMeta : meta); break;}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {blockstate=Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
			   												   {blockstate=Blocks.BIRCH_DOOR.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_BUTTON)                 {blockstate=ModObjects.chooseWoodenButton(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_PRESSURE_PLATE)         {blockstate=ModObjects.chooseModPressurePlate(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.TRAPDOOR)                      {blockstate=ModObjects.chooseModWoodenTrapdoor(woodMeta).getStateFromMeta(meta); break;}
        	//if (block == Blocks.standing_sign)                 {blockstate=new Object[]{ModObjects.chooseModWoodenSign(2, true), meta/4}; break;}
        	//if (block == Blocks.wall_sign)                     {blockstate=new Object[]{ModObjects.chooseModWoodenSign(2, false), meta}; break;}
        	if (block != null &&
            		(block == Block.getBlockFromName(ModObjects.woodQu)
            			|| block == Block.getBlockFromName(ModObjects.woodFV_birch))) {blockstate=block.getStateFromMeta(woodMeta); break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLogBirchUTD), meta}; break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 2}; break;}
        	if (block == Blocks.SANDSTONE && meta==2)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(0); break;} // Cut sandstone into stone brick
        	if (block == Blocks.SANDSTONE && meta==1)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(3); break;} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.SANDSTONE)                     {blockstate=Blocks.COBBLESTONE.getStateFromMeta(0); break;}
        	if (
        			(block == Blocks.STONE_SLAB && (meta==1 || meta==9))
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)
        					|| block == Block.getBlockFromName(ModObjects.smoothRedSandstoneSlabQu)
        			))
        			) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==1? 3: meta==9? 11 : meta); break;}
        	if (
        			(block == Blocks.DOUBLE_STONE_SLAB && meta==9)
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstone_white_Qu)
        					|| block == Block.getBlockFromName(ModObjects.smoothSandstone_white_FMC)
        			))
        			) {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;} // Smooth sandstone into planks
        	if (block == Blocks.DOUBLE_STONE_SLAB)             {blockstate=Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(meta==1? 0 : meta); break;}
        	if (block == Blocks.SANDSTONE_STAIRS)              {blockstate=Blocks.STONE_STAIRS.getStateFromMeta(meta); break;}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==8 ? 11: meta==0? 3:meta); break;}
        	if (block != null && ( 
        			block == Block.getBlockFromName(ModObjects.sandstoneWall_white_Qu)
        			|| block == Block.getBlockFromName(ModObjects.sandstoneWall_white_FMC)
        			)) {blockstate=Blocks.BIRCH_FENCE.getDefaultState(); break;}
        	if (block == Blocks.SAPLING)                       {blockstate=Blocks.SAPLING.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.SNOW)                          {blockstate=Blocks.DIRT.getDefaultState(); break;}
        	if (block == Blocks.SNOW_LAYER)                    {blockstate=Blocks.AIR.getDefaultState(); break;}
        	if (block == Blocks.ICE)                           {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.PACKED_ICE)                    {blockstate=Blocks.COBBLESTONE.getDefaultState(); break;}
        	if (block != null && block==Block.getBlockFromName(ModObjects.blueIceFMC)) {blockstate=Blocks.STONEBRICK.getDefaultState(); break;}
        	
        	break;
        	
        case JUNGLE:
    		woodMeta = BlockPlanks.EnumType.JUNGLE.getMetadata();
    		
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {blockstate=Blocks.LOG.getStateFromMeta(woodMeta%4); break;}
    		if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {blockstate=Blocks.LEAVES.getStateFromMeta(woodMeta+4); break;}
        	if (block == Blocks.COBBLESTONE)                   {blockstate=Blocks.MOSSY_COBBLESTONE.getDefaultState(); break;}
        	if (block == Blocks.STONEBRICK && meta==0)         {blockstate=Blocks.STONEBRICK.getStateFromMeta(1); break;} // Stone brick into mossy stone brick
        	if (block == Blocks.STONE_STAIRS)
													     	   {
																	   block = ModObjects.chooseModMossyCobblestoneStairsBlock();
																	   if (block==null) {blockstate=Blocks.STONE_STAIRS.getDefaultState();} else {blockstate=block.getDefaultState();}
																	   break;
													     	   }
        	if (block == Blocks.STONE_BRICK_STAIRS)
													     	   {
																	   block = ModObjects.chooseModMossyStoneBrickStairsBlock();
																	   if (block==null) {blockstate=Blocks.STONE_BRICK_STAIRS.getDefaultState();} else {blockstate=block.getDefaultState();}
																	   break;
													     	   }
        	if (block == Blocks.COBBLESTONE_WALL)              {blockstate=Blocks.COBBLESTONE_WALL.getStateFromMeta(1); break;} // Mossy COBBLESTONE wall
        	if (block != null && ModObjects.chooseModStoneBrickWallState()!=null && block == ModObjects.chooseModStoneBrickWallState().getBlock())
													     	   {
																	   IBlockState modstate = ModObjects.chooseModMossyStoneBrickWallState();
																	   if (modstate!=null) {blockstate=ModObjects.chooseModMossyStoneBrickWallState();}
																	   break;
													     	   }
        	if (block == Blocks.PLANKS)                        {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.OAK_FENCE)					   {blockstate=Blocks.JUNGLE_FENCE.getDefaultState(); break;}
        	if (block == Blocks.OAK_FENCE_GATE)				   {blockstate=Blocks.JUNGLE_FENCE_GATE.getDefaultState(); break;}
        	if (block == Blocks.OAK_STAIRS)                    {blockstate=Blocks.JUNGLE_STAIRS.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_SLAB)                   {blockstate=Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +woodMeta: meta==8? 8 +woodMeta : meta); break;}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {blockstate=Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
        													   {blockstate=Blocks.JUNGLE_DOOR.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_BUTTON)                 {blockstate=ModObjects.chooseWoodenButton(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_PRESSURE_PLATE)         {blockstate=ModObjects.chooseModPressurePlate(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.TRAPDOOR)                      {blockstate=ModObjects.chooseModWoodenTrapdoor(woodMeta).getStateFromMeta(meta); break;}
        	//if (block == Blocks.standing_sign)                 {blockstate=new Object[]{ModObjects.chooseModWoodenSign(3, true), meta/4}; break;}
        	//if (block == Blocks.wall_sign)                     {blockstate=new Object[]{ModObjects.chooseModWoodenSign(3, false), meta}; break;}
        	if (block != null &&
            		(block == Block.getBlockFromName(ModObjects.woodQu)
            			|| block == Block.getBlockFromName(ModObjects.woodFV_jungle))) {blockstate=block.getStateFromMeta(woodMeta); break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLogJungleUTD), meta}; break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 3}; break;}
        	if (block == Blocks.SANDSTONE && meta==2)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(1); break;} // Cut sandstone into mossy stone brick
        	if (block == Blocks.SANDSTONE && meta==1)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(3); break;} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.SANDSTONE)                     {blockstate=Blocks.MOSSY_COBBLESTONE.getDefaultState(); break;}
        	if (
        			(block == Blocks.STONE_SLAB && (meta==1 || meta==9))
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)
        					|| block == Block.getBlockFromName(ModObjects.smoothRedSandstoneSlabQu)
        			))
        			) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==1? 3: meta==9? 11 : meta); break;}
        	if (block == Blocks.STONE_SLAB && (meta==5||meta==13)) // Stone brick slab into mossy stone brick slab
															   {blockstate = ModObjects.chooseModMossyStoneBrickSlabState(meta==13);
																if (blockstate==null) {blockstate = block.getStateFromMeta(meta);}
																break;}
        	if (
        			(block == Blocks.DOUBLE_STONE_SLAB && meta==9)
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstone_white_Qu)
        					|| block == Block.getBlockFromName(ModObjects.smoothSandstone_white_FMC)
        			))
        			) {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;} // Smooth sandstone into planks
        	if (block == Blocks.DOUBLE_STONE_SLAB)             {blockstate=Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(meta==1? 0 : meta); break;}
        	if (block == Blocks.SANDSTONE_STAIRS)              {blockstate=Blocks.STONE_STAIRS.getStateFromMeta(meta); break;}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==8 ? 11: meta==0? 3:meta); break;}
        	if (block != null && ( 
        			block == Block.getBlockFromName(ModObjects.sandstoneWall_white_Qu)
        			|| block == Block.getBlockFromName(ModObjects.sandstoneWall_white_FMC)
        			)) {blockstate=Blocks.JUNGLE_FENCE.getDefaultState(); break;}
        	if (block == Blocks.SAPLING)                       {blockstate=Blocks.SAPLING.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.SNOW)                          {blockstate=Blocks.DIRT.getDefaultState(); break;}
        	if (block == Blocks.SNOW_LAYER)                    {blockstate=Blocks.AIR.getDefaultState(); break;}
        	if (block == Blocks.ICE)                           {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.PACKED_ICE)                    {blockstate=Blocks.MOSSY_COBBLESTONE.getDefaultState(); break;}
        	if (block != null && block==Block.getBlockFromName(ModObjects.blueIceFMC)) {blockstate=Blocks.STONEBRICK.getDefaultState(); break;}
        	
        	break;
        	
        case ACACIA:
    		woodMeta = BlockPlanks.EnumType.ACACIA.getMetadata();
    		
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {blockstate=Blocks.LOG2.getStateFromMeta(woodMeta%4); break;}
    		if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {blockstate=Blocks.LEAVES2.getStateFromMeta(12); break;}
        	if (block == Blocks.PLANKS)                        {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.OAK_FENCE)					   {blockstate=Blocks.ACACIA_FENCE.getDefaultState(); break;}
        	if (block == Blocks.OAK_FENCE_GATE)				   {blockstate=Blocks.ACACIA_FENCE_GATE.getDefaultState(); break;}
        	if (block == Blocks.OAK_STAIRS)                    {blockstate=Blocks.ACACIA_STAIRS.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_SLAB)                   {blockstate=Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +woodMeta: meta==8? 8 +woodMeta : meta); break;}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {blockstate=Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
			   												   {blockstate=Blocks.ACACIA_DOOR.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_BUTTON)                 {blockstate=ModObjects.chooseWoodenButton(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_PRESSURE_PLATE)         {blockstate=ModObjects.chooseModPressurePlate(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.TRAPDOOR)                      {blockstate=ModObjects.chooseModWoodenTrapdoor(woodMeta).getStateFromMeta(meta); break;}
        	//if (block == Blocks.standing_sign)                 {blockstate=new Object[]{ModObjects.chooseModWoodenSign(4, true), meta/4}; break;}
        	//if (block == Blocks.wall_sign)                     {blockstate=new Object[]{ModObjects.chooseModWoodenSign(4, false), meta}; break;}
        	if (block != null &&
            		(block == Block.getBlockFromName(ModObjects.woodQu)
            			|| block == Block.getBlockFromName(ModObjects.woodFV_acacia))) {blockstate=block.getStateFromMeta(woodMeta); break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLogAcaciaUTD), meta}; break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLog2EF), 4*meta + 0}; break;}
        	if (block == Blocks.SANDSTONE && meta==2)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(0); break;} // Cut sandstone into stone brick
        	if (block == Blocks.SANDSTONE && meta==1)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(3); break;} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.SANDSTONE)                     {blockstate=Blocks.COBBLESTONE.getStateFromMeta(0); break;}
        	if (
        			(block == Blocks.STONE_SLAB && (meta==1 || meta==9))
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)
        					|| block == Block.getBlockFromName(ModObjects.smoothRedSandstoneSlabQu)
        			))
        			) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==1? 3: meta==9? 11 : meta); break;}
        	if (
        			(block == Blocks.DOUBLE_STONE_SLAB && meta==9)
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstone_white_Qu)
        					|| block == Block.getBlockFromName(ModObjects.smoothSandstone_white_FMC)
        			))
        			) {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;} // Smooth sandstone into planks
        	if (block == Blocks.DOUBLE_STONE_SLAB)             {blockstate=Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(meta==1? 0 : meta); break;}
        	if (block == Blocks.SANDSTONE_STAIRS)              {blockstate=Blocks.STONE_STAIRS.getStateFromMeta(meta); break;}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==8 ? 11: meta==0? 3:meta); break;}
        	if (block != null && ( 
        			block == Block.getBlockFromName(ModObjects.sandstoneWall_white_Qu)
        			|| block == Block.getBlockFromName(ModObjects.sandstoneWall_white_FMC)
        			)) {blockstate=Blocks.ACACIA_FENCE.getDefaultState(); break;}
        	if (block == Blocks.SAPLING)                       {blockstate=Blocks.SAPLING.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.SNOW)                          {blockstate=Blocks.DIRT.getDefaultState(); break;}
        	if (block == Blocks.SNOW_LAYER)                    {blockstate=Blocks.AIR.getDefaultState(); break;}
        	if (block == Blocks.ICE)                           {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.PACKED_ICE)                    {blockstate=Blocks.COBBLESTONE.getDefaultState(); break;}
        	if (block != null && block==Block.getBlockFromName(ModObjects.blueIceFMC)) {blockstate=Blocks.STONEBRICK.getDefaultState(); break;}
        	
        	break;
        	
        case DARK_OAK:
    		woodMeta = BlockPlanks.EnumType.DARK_OAK.getMetadata();
    		
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {blockstate=Blocks.LOG2.getStateFromMeta(woodMeta%4); break;}
    		if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {blockstate=Blocks.LEAVES2.getStateFromMeta(5); break;}
        	if (block == Blocks.PLANKS)                        {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.OAK_FENCE)					   {blockstate=Blocks.DARK_OAK_FENCE.getDefaultState(); break;}
        	if (block == Blocks.OAK_FENCE_GATE)				   {blockstate=Blocks.DARK_OAK_FENCE_GATE.getDefaultState(); break;}
        	if (block == Blocks.OAK_STAIRS)                    {blockstate=Blocks.DARK_OAK_STAIRS.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_SLAB)                   {blockstate=Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +woodMeta: meta==8? 8 +woodMeta : meta); break;}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {blockstate=Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
			   												   {blockstate=Blocks.DARK_OAK_DOOR.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_BUTTON)                 {blockstate=ModObjects.chooseWoodenButton(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_PRESSURE_PLATE)         {blockstate=ModObjects.chooseModPressurePlate(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.TRAPDOOR)                      {blockstate=ModObjects.chooseModWoodenTrapdoor(woodMeta).getStateFromMeta(meta); break;}
        	//if (block == Blocks.standing_sign)                 {blockstate=new Object[]{ModObjects.chooseModWoodenSign(5, true), meta/4}; break;}
        	//if (block == Blocks.wall_sign)                     {blockstate=new Object[]{ModObjects.chooseModWoodenSign(5, false), meta}; break;}
        	if (block != null &&
            		(block == Block.getBlockFromName(ModObjects.woodQu)
            			|| block == Block.getBlockFromName(ModObjects.woodFV_dark_oak))) {blockstate=block.getStateFromMeta(woodMeta); break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLogDarkOakUTD), meta}; break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLog2EF), 4*meta + 1}; break;}
        	if (block == Blocks.SANDSTONE && meta==2)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(0); break;} // Cut sandstone into stone brick
        	if (block == Blocks.SANDSTONE && meta==1)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(3); break;} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.SANDSTONE)                     {blockstate=Blocks.COBBLESTONE.getStateFromMeta(0); break;}
        	if (
        			(block == Blocks.STONE_SLAB && (meta==1 || meta==9))
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)
        					|| block == Block.getBlockFromName(ModObjects.smoothRedSandstoneSlabQu)
        			))
        			) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==1? 3: meta==9? 11 : meta); break;}
        	if (
        			(block == Blocks.DOUBLE_STONE_SLAB && meta==9)
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstone_white_Qu)
        					|| block == Block.getBlockFromName(ModObjects.smoothSandstone_white_FMC)
        			))
        			) {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;} // Smooth sandstone into planks
        	if (block == Blocks.DOUBLE_STONE_SLAB)             {blockstate=Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(meta==1? 0 : meta); break;}
        	if (block == Blocks.SANDSTONE_STAIRS)              {blockstate=Blocks.STONE_STAIRS.getStateFromMeta(meta); break;}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==8 ? 11: meta==0? 3:meta); break;}
        	if (block != null && ( 
        			block == Block.getBlockFromName(ModObjects.sandstoneWall_white_Qu)
        			|| block == Block.getBlockFromName(ModObjects.sandstoneWall_white_FMC)
        			)) {blockstate=Blocks.DARK_OAK_FENCE.getDefaultState(); break;}
        	if (block == Blocks.SAPLING)                       {blockstate=Blocks.SAPLING.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.SNOW)                          {blockstate=Blocks.DIRT.getDefaultState(); break;}
        	if (block == Blocks.SNOW_LAYER)                    {blockstate=Blocks.AIR.getDefaultState(); break;}
        	if (block == Blocks.ICE)                           {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.PACKED_ICE)                    {blockstate=Blocks.COBBLESTONE.getDefaultState(); break;}
        	if (block != null && block==Block.getBlockFromName(ModObjects.blueIceFMC)) {blockstate=Blocks.STONEBRICK.getDefaultState(); break;}
        	
        	break;
        	
        case SAND:
    		woodMeta = BlockPlanks.EnumType.JUNGLE.getMetadata();
    		
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {blockstate=Blocks.SANDSTONE.getStateFromMeta(2); break;} // Cut sandstone
    		if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {blockstate=Blocks.LEAVES.getStateFromMeta(woodMeta+4); break;}
        	if (block == Blocks.STONEBRICK && meta==0)         {blockstate=Blocks.SANDSTONE.getStateFromMeta(2); break;} // Stone brick into cut sandstone
        	if (block == Blocks.COBBLESTONE && meta==3)        {blockstate=Blocks.COBBLESTONE.getStateFromMeta(1); break;} // Chiseled sandstone
        	if (block == Blocks.COBBLESTONE)                   {blockstate=Blocks.SANDSTONE.getStateFromMeta(0); break;} // Regular sandstone
        	if (block == Blocks.STONE && meta==0)              {blockstate=Blocks.SANDSTONE.getStateFromMeta(0); break;} // Regular stone into regular sandstone
        	if (block == Blocks.MOSSY_COBBLESTONE)             {blockstate=Blocks.SANDSTONE.getStateFromMeta(0); break;} // Regular sandstone
        	if (block == Blocks.PLANKS)                        {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.OAK_FENCE)					   {blockstate=Blocks.JUNGLE_FENCE.getDefaultState(); break;}
			if (block == Blocks.ACACIA_FENCE_GATE || block == Blocks.BIRCH_FENCE_GATE || block == Blocks.DARK_OAK_FENCE_GATE || block == Blocks.JUNGLE_FENCE_GATE || block == Blocks.OAK_FENCE_GATE || block == Blocks.SPRUCE_FENCE_GATE)
															   {blockstate=Blocks.JUNGLE_FENCE_GATE.getDefaultState(); break;}
        	if (block == Blocks.OAK_STAIRS)                    {blockstate=Blocks.JUNGLE_STAIRS.getStateFromMeta(meta); break;}
        	if (block == Blocks.STONE_STAIRS)                  {blockstate=Blocks.SANDSTONE_STAIRS.getStateFromMeta(meta); break;}
        	if (block == Blocks.COBBLESTONE_WALL)              {
																	IBlockState tryBlockstate=ModObjects.chooseModSandstoneWall(false);
													        		if (tryBlockstate!=null) {blockstate=tryBlockstate;}
													        		break;
															   } // Sandstone wall
        	if (block == Blocks.GRAVEL)                        {blockstate=Blocks.SANDSTONE.getStateFromMeta(0); break;}
        	if (block == Blocks.DIRT)                          {blockstate=Blocks.SAND.getStateFromMeta(0); break;}
        	if (block == Blocks.GRASS)                         {blockstate=Blocks.SAND.getStateFromMeta(0); break;}
        	if (block == Blocks.WOODEN_SLAB)                   {blockstate=Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +woodMeta: meta==8? 8 +woodMeta : meta); break;}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {blockstate=Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
        													   {blockstate=Blocks.JUNGLE_DOOR.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_BUTTON)                 {blockstate=ModObjects.chooseWoodenButton(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_PRESSURE_PLATE)         {blockstate=ModObjects.chooseModPressurePlate(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.TRAPDOOR)                      {blockstate=ModObjects.chooseModWoodenTrapdoor(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.STONE_SLAB)                    {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==3? 1: meta==11? 9 : meta); break;} // Sandstone slab
        	//if (block == Blocks.DOUBLE_STONE_SLAB)             {blockstate=Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(4); break;} // Brick double slab
        	//if (block == Blocks.standing_sign)                 {blockstate=new Object[]{ModObjects.chooseModWoodenSign(3, true), meta/4}; break;}
        	//if (block == Blocks.wall_sign)                     {blockstate=new Object[]{ModObjects.chooseModWoodenSign(3, false), meta}; break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLogJungleUTD), meta}; break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 3}; break;}
        	if (block != null &&
            		(block == Block.getBlockFromName(ModObjects.woodQu)
            			|| block == Block.getBlockFromName(ModObjects.woodFV_jungle))) {blockstate=Blocks.SANDSTONE.getStateFromMeta(2); break;} // Cut sandstone
        	
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {blockstate=new Object[]{Blocks.SANDSTONE, 2}; break;} // Cut sandstone
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {blockstate=new Object[]{Blocks.SANDSTONE, 2}; break;} // Cut sandstone
        	if (block == Blocks.SAPLING)                       {blockstate=Blocks.DEADBUSH.getDefaultState(); break;}
        	if (block == Blocks.SNOW)                          {blockstate=Blocks.SAND.getStateFromMeta(0); break;}
        	if (block == Blocks.SNOW_LAYER)                    {blockstate=Blocks.AIR.getDefaultState(); break;}
        	if (block == Blocks.ICE)                           {blockstate=Blocks.SANDSTONE.getStateFromMeta(0); break;}
        	if (block == Blocks.PACKED_ICE)                    {blockstate=Blocks.SANDSTONE.getStateFromMeta(2); break;} // Cut sandstone
        	if (block != null && block==Block.getBlockFromName(ModObjects.blueIceFMC)) {blockstate=Blocks.SANDSTONE.getStateFromMeta(2); break;}
        	
        	break;
        	
        case MESA:
        	
        	if (block == Blocks.COBBLESTONE)                   {blockstate=Blocks.HARDENED_CLAY.getDefaultState(); break;}
        	if (block == Blocks.STONE && meta==0)              {blockstate=Blocks.HARDENED_CLAY.getDefaultState(); break;} // Regular stone into terracotta
        	if (block == Blocks.MOSSY_COBBLESTONE)             {blockstate=Blocks.HARDENED_CLAY.getDefaultState(); break;}
        	if (block == Blocks.STONE_STAIRS)                  {blockstate=Blocks.BRICK_STAIRS.getStateFromMeta(meta); break;}
        	if (block == Blocks.GRAVEL)                        {blockstate=Blocks.HARDENED_CLAY.getDefaultState(); break;}
        	//if (block == Blocks.STONE_SLAB)                    {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==3? 4: meta==11? 12 : meta); break;} // Brick slab
        	//if (block == Blocks.COBBLESTONE_WALL)              {
			//										        		block = Block.getBlockFromName(ModObjects.brickWallUTD);
			//										        		if (block==null) {block = Blocks.brick_block;}
			//										        		return new Object[]{block, 0};
			//												   } // Brick wall
        	if (block == Blocks.SAND)                          {blockstate=Blocks.SAND.getStateFromMeta(1); break;} // Red Sand
        	if (block == Blocks.COBBLESTONE_WALL)              {
																	IBlockState tryBlockstate=ModObjects.chooseModSandstoneWall(true);
													        		if (tryBlockstate!=null) {blockstate=tryBlockstate;}
													        		break;
															   } // Sandstone wall
			if (block == Blocks.SANDSTONE)                     {blockstate=Blocks.RED_SANDSTONE.getDefaultState(); break;}
			if (block == Blocks.STONE_SLAB)                    {blockstate=Blocks.STONE_SLAB2.getStateFromMeta(meta>=8? 8:0); break;}
			if (block == Blocks.SANDSTONE_STAIRS)              {blockstate=Blocks.RED_SANDSTONE_STAIRS.getStateFromMeta(meta); break;}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu))
        													   {
        													   		block=Block.getBlockFromName(ModObjects.smoothRedSandstoneSlabQu);
        													   		if (block==null) {blockstate=Blocks.STONE_SLAB2.getStateFromMeta(meta); break;}
        													   		return block.getStateFromMeta(meta);
        													   }
        	if (block == Blocks.SAPLING)                       {blockstate=Blocks.TALLGRASS.getStateFromMeta(0); break;} // Shrub
        	if (block == Blocks.SNOW)                          {blockstate=Blocks.SAND.getStateFromMeta(1); break;} // Red Sand
        	if (block == Blocks.SNOW_LAYER)                    {blockstate=Blocks.AIR.getDefaultState(); break;}
        	if (block == Blocks.ICE)                           {blockstate=Blocks.HARDENED_CLAY.getDefaultState(); break;}
        	if (block == Blocks.PACKED_ICE)                    {blockstate=Blocks.HARDENED_CLAY.getDefaultState(); break;}
        	if (block != null && block==Block.getBlockFromName(ModObjects.blueIceFMC)) {blockstate=Blocks.HARDENED_CLAY.getDefaultState(); break;}
        	
        	break;
        	
        case SNOW:
    		woodMeta = BlockPlanks.EnumType.SPRUCE.getMetadata();
    		
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {blockstate=Blocks.LOG.getStateFromMeta(woodMeta%4); break;}
    		if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {blockstate=Blocks.LEAVES.getStateFromMeta(woodMeta+4); break;}
        	if (block == Blocks.PLANKS)                        {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.OAK_FENCE)					   {blockstate=Blocks.SPRUCE_FENCE.getDefaultState(); break;}
        	if (block == Blocks.OAK_FENCE_GATE)				   {blockstate=Blocks.SPRUCE_FENCE_GATE.getDefaultState(); break;}
        	if (block == Blocks.OAK_STAIRS)                    {blockstate=Blocks.SPRUCE_STAIRS.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_SLAB)                   {blockstate=Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +woodMeta: meta==8? 8 +woodMeta : meta); break;}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {blockstate=Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(woodMeta); break;}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
			   												   {blockstate=Blocks.SPRUCE_DOOR.getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_BUTTON)                 {blockstate=ModObjects.chooseWoodenButton(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.WOODEN_PRESSURE_PLATE)         {blockstate=ModObjects.chooseModPressurePlate(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.TRAPDOOR)                      {blockstate=ModObjects.chooseModWoodenTrapdoor(woodMeta).getStateFromMeta(meta); break;}
        	if (block == Blocks.MOSSY_COBBLESTONE)             {blockstate=Blocks.COBBLESTONE.getDefaultState(); break;}
        	if (block == Blocks.STONEBRICK && meta==1)         {blockstate=Blocks.STONEBRICK.getStateFromMeta(0); break;} // Mossy stone brick into regular
			if (block != null && block == ModObjects.chooseModMossyCobblestoneStairsBlock()) {blockstate = Blocks.STONE_STAIRS.getDefaultState(); break;}
			if (block != null && block == ModObjects.chooseModMossyStoneBrickStairsBlock()) {blockstate = Blocks.STONE_BRICK_STAIRS.getDefaultState(); break;}
        	if (block == Blocks.COBBLESTONE_WALL && meta==1)   {blockstate=Blocks.COBBLESTONE_WALL.getStateFromMeta(0); break;} // Mossy cobblestone wall into regular
        	if (block != null && ModObjects.chooseModMossyStoneBrickWallState()!=null && block == ModObjects.chooseModMossyStoneBrickWallState().getBlock())
													     	   {
																	   IBlockState modstate = ModObjects.chooseModStoneBrickWallState();
																	   if (modstate!=null) {blockstate=ModObjects.chooseModStoneBrickWallState();}
																	   break;
													     	   }
        	//if (block == Blocks.standing_sign)                 {blockstate=new Object[]{ModObjects.chooseModWoodenSign(1, true), meta/4}; break;}
        	//if (block == Blocks.wall_sign)                     {blockstate=new Object[]{ModObjects.chooseModWoodenSign(1, false), meta}; break;}
        	if (block != null &&
        			(block == Block.getBlockFromName(ModObjects.woodQu)
        				|| block == Block.getBlockFromName(ModObjects.woodFV_spruce))) {blockstate=block.getStateFromMeta(woodMeta); break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLogSpruceUTD), meta}; break;}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {blockstate=new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 1}; break;}
        	if (block == Blocks.SANDSTONE && meta==2)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(0); break;} // Cut sandstone into stone brick
        	if (block == Blocks.SANDSTONE && meta==1)          {blockstate=Blocks.STONEBRICK.getStateFromMeta(3); break;} // Chiseled sandstone into chiseled stone
        	if (block == Blocks.SANDSTONE)                     {blockstate=Blocks.COBBLESTONE.getStateFromMeta(0); break;}
        	if (
        			(block == Blocks.STONE_SLAB && (meta==1 || meta==9))
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)
        					|| block == Block.getBlockFromName(ModObjects.smoothRedSandstoneSlabQu)
        			))
        			) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==1? 3: meta==9? 11 : meta); break;}
        	if (block!=null && ModObjects.chooseModMossyStoneBrickSlabState(true)!=null && block == ModObjects.chooseModMossyStoneBrickSlabState(true).getBlock()) // Mossy stone brick slab into stone brick slab
															   {block = Blocks.STONE_SLAB;
																meta = meta==block.getMetaFromState(ModObjects.chooseModMossyStoneBrickSlabState(true))?13:5;
																break;}
        	if (
        			(block == Blocks.DOUBLE_STONE_SLAB && meta==9)
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstone_white_Qu)
        					|| block == Block.getBlockFromName(ModObjects.smoothSandstone_white_FMC)
        			))
        			) {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;} // Smooth sandstone into planks
        	if (block == Blocks.DOUBLE_STONE_SLAB)             {blockstate=Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(meta==1? 0 : meta); break;}
        	if (block == Blocks.SANDSTONE_STAIRS)              {blockstate=Blocks.STONE_STAIRS.getStateFromMeta(meta); break;}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==8 ? 11: meta==0? 3:meta); break;}
        	if (block != null && ( 
        			block == Block.getBlockFromName(ModObjects.sandstoneWall_white_Qu)
        			|| block == Block.getBlockFromName(ModObjects.sandstoneWall_white_FMC)
        			)) {blockstate=Blocks.SPRUCE_FENCE.getDefaultState(); break;}
        	if (block == Blocks.SAPLING)                       {blockstate=Blocks.SAPLING.getStateFromMeta(woodMeta); break;}
        	
        	break;
        	
        case MUSHROOM:
        	
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(15); break;} // Stem on all six sides
    		if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14); break;} // Cap on all six sides
        	if (block == Blocks.COBBLESTONE)                   {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14); break;} // Cap on all six sides
        	if (block == Blocks.MOSSY_COBBLESTONE)             {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14); break;} // Cap on all six sides
        	if (block == Blocks.PLANKS)                        {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(0); break;} // Pores on all six sides
        	if (block == Blocks.COBBLESTONE && meta==3)        {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14); break;} // Cap on all six sides
        	if (block == Blocks.STONEBRICK)                    {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14); break;} // Cap on all six sides
        	if (block == Blocks.SANDSTONE && meta==2)          {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14); break;} // Cap on all six sides
        	if (block == Blocks.SANDSTONE && meta==1)          {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14); break;} // Cap on all six sides
        	if (block == Blocks.SANDSTONE)                     {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14); break;} // Cap on all six sides
        	if (
        			(block == Blocks.STONE_SLAB && (meta==1 || meta==9))
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)
        					|| block == Block.getBlockFromName(ModObjects.smoothRedSandstoneSlabQu)
        			))
        			) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==1? 3: meta==9? 11 : meta); break;}
        	if (
        			(block == Blocks.DOUBLE_STONE_SLAB && meta==9)
        			|| (block != null && (
        					block == Block.getBlockFromName(ModObjects.smoothSandstone_white_Qu)
        					|| block == Block.getBlockFromName(ModObjects.smoothSandstone_white_FMC)
        			))
        			) {blockstate=Blocks.PLANKS.getStateFromMeta(woodMeta); break;} // Smooth sandstone into planks
        	if (block == Blocks.DOUBLE_STONE_SLAB)             {blockstate=Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(meta==1? 0 : meta); break;}
        	if (block == Blocks.SANDSTONE_STAIRS)              {blockstate=Blocks.STONE_STAIRS.getStateFromMeta(meta); break;}
        	if (block != null && block == Block.getBlockFromName(ModObjects.smoothSandstoneSlabQu)) {blockstate=Blocks.STONE_SLAB.getStateFromMeta(meta==8 ? 11: meta==0? 3:meta); break;}
        	if (block != null && ( 
        			block == Block.getBlockFromName(ModObjects.sandstoneWall_white_Qu)
        			|| block == Block.getBlockFromName(ModObjects.sandstoneWall_white_FMC)
        			)) {blockstate=Blocks.COBBLESTONE_WALL.getDefaultState(); break;}
        	if (block == Blocks.SAPLING)                       {blockstate=Blocks.BROWN_MUSHROOM.getStateFromMeta(0); break;}
        	if (block == Blocks.GRASS)                         {blockstate=Blocks.MYCELIUM.getStateFromMeta(0); break;}
        	if (block == Blocks.SNOW)                          {blockstate=Blocks.DIRT.getDefaultState(); break;}
        	if (block == Blocks.SNOW_LAYER)                    {blockstate=Blocks.AIR.getDefaultState(); break;}
        	if (block == Blocks.ICE)                           {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(0); break;} // Pores on all six sides
        	if (block == Blocks.PACKED_ICE)                    {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14); break;} // Cap on all six sides
        	if (block != null && block==Block.getBlockFromName(ModObjects.blueIceFMC)) {blockstate=Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14); break;} // Cap on all six sides
        	
        	break;
        	
    	}
    	
        // Post Forge event
    	if (!disallowModSubs)
    	{
    		// Re-export blocks and meta for comparison
    		block = blockstate.getBlock(); meta = blockstate.getBlock().getMetaFromState(blockstate);
    		
	        // Post Forge event
	        BiomeEvent.GetVillageBlockID villageBlockEvent = new BiomeEvent.GetVillageBlockID(biome == null ? null : biome, block.getStateFromMeta(meta));
	        MinecraftForge.TERRAIN_GEN_BUS.post(villageBlockEvent);
	        
	        if (villageBlockEvent.getResult() == Result.DENY) return villageBlockEvent.getReplacement();
    	}
        
        return blockstate;
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
    public static int getTorchRotationMeta(int relativeOrientation)
    {
		switch (relativeOrientation)
		{
		case 0: // Facing away
			return 4;
		case 1: // Facing right
			return 1;
		case 2: // Facing you
			return 3;
		case 3: // Facing left
			return 2;
		}
    	return 0; // Torch will be standing upright, hopefully
    }
    
    /**
     * Give this method a pillar-like blockstate, the horizontal orientation index of a structure component,
     * and use isAlong to determine whether the log should be oriented "down the barrel" (false) or laterally (true)
     * from the perspective of a player entering the structure component; get back the new blockstate.
     */
    public static IBlockState getHorizontalPillarState(IBlockState blockstate, int coordBaseMode, boolean isAcross)
    {
    	return blockstate.getBlock().getStateFromMeta(
    			blockstate.getBlock().getMetaFromState(blockstate)%4 // Material meta value
    			+ 4 // Horizontal is either 4 or 8
    			//+ (coordBaseMode%2==(isAcross? 0 : 1)? 0 : 1) *4
    			+ (isAcross? 0 : 4)
   					);
    }
    
    /**
     * Sets the path-specific block into the world
     * The block will get set at the ground height or posY, whichever is higher.
     * Returns the height at which the block was placed
     */
    public static int setPathSpecificBlock(World world, MaterialType materialType, Biome biome, boolean disallowModSubs, int posX, int posY, int posZ, boolean searchDownward)
    {
    	// Regenerate these if null
    	if (materialType==null) {materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, posX, posZ);}
    	if (biome==null) {biome = world.getBiome(new BlockPos(posX, 64, posZ));}
    	
    	
    	// Top block level
    	int surfaceY = searchDownward ? StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(posX, 64, posZ)).down().getY() : posY;
    	
    	// Raise Y to be at least below sea level
    	if (surfaceY < world.getSeaLevel()) {surfaceY = world.getSeaLevel()-1;}
    	
    	do
    	{
    		BlockPos pos = new BlockPos(posX, surfaceY, posZ);
    		IBlockState surfaceBlockState = world.getBlockState(pos);
    		Block surfaceBlock = surfaceBlockState.getBlock();
    		int surfaceMeta = surfaceBlockState.getBlock().getMetaFromState(surfaceBlockState);
    		String targetBlockRegistryName = Block.REGISTRY.getNameForObject(surfaceBlock).toString();
    		
    		// Replace loamy grass with loamy grass path
    		if (Block.getBlockFromName(ModObjects.grass_BOP) != null
    				&& surfaceBlock==Block.getBlockFromName(ModObjects.grass_BOP)
    				&& surfaceMeta==2
    				&& world.isAirBlock(new BlockPos(posX, surfaceY, posZ).up()))
    		{
    			Block modblock = Block.getBlockFromName(ModObjects.loamyGrassPath_BoP);
				
    			if (modblock != null)
				{
					world.setBlockState(pos, modblock.getDefaultState(), 2);
					return surfaceY;
				}
    		}
    		
    		// Replace grass with grass path
    		if ((surfaceBlock instanceof BlockGrass || surfaceBlock instanceof BlockDirt
    				|| (surfaceBlock.getMaterial(surfaceBlockState) == Material.GRASS && surfaceBlockState.getMaterial().blocksMovement() && surfaceBlockState.isFullCube()) 
    				) && world.isAirBlock(new BlockPos(posX, surfaceY, posZ).up()))
    		{
    	    	IBlockState grassPath = getBiomeSpecificBlockState(Blocks.GRASS_PATH.getDefaultState(), materialType, biome, disallowModSubs);
    			world.setBlockState(pos, grassPath, 2);
    			return surfaceY;
    		}
    		
    		// Replace sand with GRAVEL supported by COBBLESTONE
    		if (surfaceBlock instanceof BlockSand)
    		{
    	    	IBlockState gravel = getBiomeSpecificBlockState(Blocks.GRAVEL.getDefaultState(), materialType, biome, disallowModSubs);
    	    	IBlockState cobblestone = getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), materialType, biome, disallowModSubs);
    			world.setBlockState(pos, gravel, 2);
    			
    			// Add cobblestone supports
    			int yDownScan = surfaceY;
    			if (MathHelper.abs(posX)%2==0 && MathHelper.abs(posZ)%2==0)
    			{
    				while(world.getBlockState(new BlockPos(posX, --yDownScan, posZ)).getMaterial().isLiquid() && yDownScan>0)
    				{
    					world.setBlockState(new BlockPos(posX, yDownScan, posZ), cobblestone, 2);
    				}
    			}
    			
    			return surfaceY;
    		}
    		
    		// Replace lava with two-layer COBBLESTONE
    		if (surfaceBlock==Blocks.LAVA || surfaceBlock==Blocks.FLOWING_LAVA
					|| targetBlockRegistryName.contains("streams:river/tile.lava")
					)
    		{
    	    	IBlockState cobblestone = getBiomeSpecificBlockState(Blocks.COBBLESTONE.getStateFromMeta(0), materialType, biome, disallowModSubs);
    			world.setBlockState(pos, cobblestone, 2);
    			world.setBlockState(pos.down(), cobblestone, 2);
    			return surfaceY;
    		}
    		
    		// Replace other liquid or ice with planks
    		if (surfaceBlock.getDefaultState().getMaterial().isLiquid() 
    				|| surfaceBlock instanceof BlockIce || surfaceBlock instanceof BlockPackedIce 
					|| targetBlockRegistryName.equals(ModObjects.mudBOP)
					|| targetBlockRegistryName.equals(ModObjects.quicksandBOP)
					|| targetBlockRegistryName.contains("streams:river/tile.water")
    				)
    		{
    	    	IBlockState planks = getBiomeSpecificBlockState(Blocks.PLANKS.getStateFromMeta(0), materialType, biome, disallowModSubs);
    			world.setBlockState(pos, planks, 2);

    			// Clear space above just in case it's a lilypad
    			world.setBlockToAir(pos.up());
    			
    			// Add log support
    			int yDownScan = surfaceY;
    			if (MathHelper.abs(posX)%2==0 && MathHelper.abs(posZ)%2==0)
    			{
    				IBlockState biomeLogVertState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.LOG.getStateFromMeta(0), materialType, biome, disallowModSubs);
    				while(world.getBlockState(new BlockPos(posX, --yDownScan, posZ)).getMaterial().isLiquid() && yDownScan>0)
    				{
    					world.setBlockState(new BlockPos(posX, yDownScan, posZ), biomeLogVertState, 2);
    				}
    			}
    			
    			return surfaceY;
    		}
    		
    		surfaceY -=1;
    	}
    	while (surfaceY >= world.getSeaLevel()-1);
    	
		return -1;
    }
    
    /**
     * Contracts bounding box by amount specified in X, Y, Z
     */
    public static StructureBoundingBox contractBB(StructureBoundingBox structureBoundingBox, int xAmount, int yAmount, int zAmount)
    {
    	return new StructureBoundingBox(
    			structureBoundingBox.minX+MathHelper.abs(xAmount),
    			structureBoundingBox.minY+MathHelper.abs(yAmount),
    			structureBoundingBox.minZ+MathHelper.abs(zAmount),
    			structureBoundingBox.maxX-MathHelper.abs(xAmount),
    			structureBoundingBox.maxY-MathHelper.abs(yAmount),
    			structureBoundingBox.maxZ-MathHelper.abs(zAmount)
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
    public static NBTTagCompound getOrMakeVNInfo(World world, int posX, int posY, int posZ) {return getOrMakeVNInfo(world, new BlockPos(posX, posY, posZ));}
    public static NBTTagCompound getOrMakeVNInfo(World world, BlockPos pos)
    {
    	int posX = pos.getX(); int posY = pos.getY(); int posZ = pos.getZ(); 

    	int townColorMeta;
    	
    	String townSignEntry="";
    	String namePrefix; String nameRoot; String nameSuffix;
    	NBTTagCompound bannerNBT;
        int townX; int townY; int townZ;
		NBTTagCompound villagetagcompound = new NBTTagCompound();
		
    	// Set random seed
    	Random randomFromXYZ = new Random();
    	randomFromXYZ.setSeed(
					world.getSeed() +
					FunctionsVN.getUniqueLongForXYZ(pos.getX(), pos.getY(), pos.getZ())
    			);
		
		// First, search through all previously generated VN villages and see if this is inside
    	// the bounding box of one of them.
    	
		
		// --- TRY 1: See if the village already exists in the vanilla collection object and try to match it to this village --- //
    	try
		{
    		Village villageNearTarget = world.villageCollection.getNearestVillage(pos, VILLAGE_RADIUS_BUFFER);
    		
    		if (villageNearTarget != null) // There is a town.
    		{
    			int villageRadius = villageNearTarget.getVillageRadius();
    			int popSize = villageNearTarget.getNumVillagers();
    			int centerX = villageNearTarget.getCenter().getX(); // Village X position
    			int centerY = villageNearTarget.getCenter().getY(); // Village Y position
    			int centerZ = villageNearTarget.getCenter().getZ(); // Village Z position
    			
    			// Let's see if we can find a sign near that located village center!
    			VNWorldDataStructure data = VNWorldDataStructure.forWorld(world, "villagenames3_Village", "NamedStructures");
    			// .getTagList() will return all the entries under the specific village name.
    			NBTTagCompound tagCompound = data.getData();
    			Set tagmapKeyset = tagCompound.getKeySet(); // Gets the town key list: "coordinates"

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


    				// Obtain the array of banner colors from the banner tag, if there is one
    				boolean updateTownNBT=false;
    				int[] townColorArray = new int[]{townColorMeta,-1,-1,-1,-1,-1,-1};
    				if (villagetagcompound.hasKey("BlockEntityTag", 10))
            		{
            			bannerNBT = villagetagcompound.getCompoundTag("BlockEntityTag");
            			if (bannerNBT.hasKey("Patterns", 9)) // 9 is Tag List
            	        {
            				NBTTagList nbttaglistPattern = bannerNBT.getTagList("Patterns", 9);
            				
            				// The banner color array has repeat values. Create a version that does not. 
            				ArrayList<Integer> colorSet = new ArrayList();
            				// Add the first element straight away
            				colorSet.add(townColorMeta);
            				// Go through the colors in the array and add them only if they're unique
            				for (int i=0; i < nbttaglistPattern.tagCount(); i++)
            				{
            					NBTTagCompound patterntag = nbttaglistPattern.getCompoundTagAt(i);
            					if (patterntag.hasKey("Color"))
            					{
            						int candidateColor = patterntag.getInteger("Color");
            						
            						boolean matchFound = false;
                					// Go through colors already in the set and seee if this matches any of them
                					for (int j=0; j<colorSet.size(); j++) {if (candidateColor==colorSet.get(j)) {matchFound=true; break;}}
                					// Color is unique!
                					if (!matchFound) {colorSet.add(candidateColor);}
            					}
            				}
            				// Assign the new unique colors to the town color array 
            				for (int i=1; i<colorSet.size(); i++)
            				{
            					townColorArray[i] = 15-colorSet.get(i);
            				}
            	        }
            		}
    				// Change each entry that's -1 to a unique color
    				for (int c=1; c<(townColorArray.length); c++)
    				{
    					// The color
    	        		if (villagetagcompound.hasKey("townColor"+(c+1))) {townColorArray[c] = villagetagcompound.getInteger("townColor"+(c+1));} // is already registered as its own NBT flag
    	        		else if (townColorArray[c]==-1) // must be generated
    	        		{
    	        			while(true)
    	        			{
    	        				townColorArray[c] = (Integer) FunctionsVN.weightedRandom(BannerGenerator.colorMeta, BannerGenerator.colorWeights, randomFromXYZ);
    	        				// Compare to all previous colors to ensure it's unique
    	        				boolean isRedundant=false;
    	        				for (int i=0; i<c; i++) {if (townColorArray[c]==townColorArray[i]) {isRedundant=true; break;}}
    	        				if (!isRedundant) {break;} // Keep this color and move on to the next one
    	        			}
    	        		}
    	        		
    	        		// Now that the townColorArray is populated, assign the colors to the town
    					if (!villagetagcompound.hasKey("townColor"+(c+1))) {updateTownNBT=true; villagetagcompound.setInteger("townColor"+(c+1), townColorArray[c]);}
    				}

    				// Add tags for village and biome types
    				if (!villagetagcompound.hasKey("villageType")) {villagetagcompound.setString("villageType", FunctionsVN.VillageType.getVillageTypeFromBiome(world, posX, posZ).toString());}
    				if (!villagetagcompound.hasKey("materialType")) {villagetagcompound.setString("materialType", FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, posX, posZ).toString());}
    				
    				// Replace the old tag
    				if (updateTownNBT)
    				{
    	        		nbttaglist.set(0, villagetagcompound);
    	        		tagCompound.setTag(townSignEntry, nbttaglist);
    	        		data.markDirty();
    				}
            		
    				// Now find the nearest Village to that sign's coordinate, within villageRadiusBuffer blocks outside the radius.
    				Village villageNearSign = world.villageCollection.getNearestVillage(new BlockPos(townX, townY, townZ), VILLAGE_RADIUS_BUFFER);
    				
    				isColony = villagetagcompound.getBoolean("isColony");
    				
    				if (villageNearSign == villageNearTarget) // There is a match between the nearest village to this villager and the nearest village to the sign
    				{
    					signLocated = true;
    					
    					return villagetagcompound;
    				}
    			}
    		}
		}
		catch (Exception e) {}
		
    	
    	
		// --- TRY 2: compare this sign position with stored VN sign positions and see if you're within range of one of them --- // 
		
		VNWorldDataStructure data = VNWorldDataStructure.forWorld(world, "villagenames3_Village", "NamedStructures");
		NBTTagCompound tagCompound = data.getData();
		Set tagmapKeyset = tagCompound.getKeySet(); //Gets the town key list: "coordinates"
		//Iterator itr = tagmapKeyset.iterator();
		NBTTagList nbttaglist = null;
		villagetagcompound = null;
		
        // Initialize village to be returned as null
		NBTTagCompound villageToCompare = null;
        // Initialize f at max value
        float bestSquareDistance = Float.MAX_VALUE;
        // Establish iterator
        Iterator itr = tagmapKeyset.iterator();
        
        // Go through iterated values
        while (itr.hasNext())
        {
        	// Obtain next village to check
			Object element = itr.next();
			townSignEntry = element.toString(); //Text name of village header (e.g. "Kupei, x191 y73 z187")
			//The only index that has data is 0:
			nbttaglist = tagCompound.getTagList(townSignEntry, tagCompound.getId());
			villageToCompare = nbttaglist.getCompoundTagAt(0);
            
			townX = villageToCompare.hasKey("signX") ? villageToCompare.getInteger("signX") : 0;
			townY = villageToCompare.hasKey("signY") ? villageToCompare.getInteger("signY") : 0;
			townZ = villageToCompare.hasKey("signZ") ? villageToCompare.getInteger("signZ") : 0;
			
            // Set f1 to distance squared between this position and the well sign position
            float squareDistanceToVNVillage = ((posX-townX)*(posX-townX)) + ((posY-townY)*(posY-townY)) + ((posZ-townZ)*(posZ-townZ));
            
            // In this case, we're closer than the closest recorded village so far
            if (squareDistanceToVNVillage < bestSquareDistance)
            {
                // And we're closer than the prescribed buffer from that village, so hold on to it
                if (squareDistanceToVNVillage <= VILLAGE_RADIUS_BUFFER * VILLAGE_RADIUS_BUFFER)
                {
                	villagetagcompound = villageToCompare;
                    bestSquareDistance = squareDistanceToVNVillage;
                }
            }
        }
        // At this point, villagetagcompound should be the best candidate of the lot.
		if (villagetagcompound!=null)
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
			
			// Obtain the array of banner colors from the banner tag, if there is one
			boolean updateTownNBT=false;
			int[] townColorArray = new int[]{townColorMeta,-1,-1,-1,-1,-1,-1};
			if (villagetagcompound.hasKey("BlockEntityTag", 10))
    		{
    			bannerNBT = villagetagcompound.getCompoundTag("BlockEntityTag");
    			if (bannerNBT.hasKey("Patterns", 9)) // 9 is Tag List
    	        {
    				NBTTagList nbttaglistPattern = bannerNBT.getTagList("Patterns", 9);
    				
    				// The banner color array has repeat values. Create a version that does not. 
    				ArrayList<Integer> colorSet = new ArrayList();
    				// Add the first element straight away
    				colorSet.add(townColorMeta);
    				// Go through the colors in the array and add them only if they're unique
    				for (int i=0; i < nbttaglistPattern.tagCount(); i++)
    				{
    					NBTTagCompound patterntag = nbttaglistPattern.getCompoundTagAt(i);
    					if (patterntag.hasKey("Color"))
    					{
    						int candidateColor = patterntag.getInteger("Color");
    						
    						boolean matchFound = false;
        					// Go through colors already in the set and seee if this matches any of them
        					for (int j=0; j<colorSet.size(); j++) {if (candidateColor==colorSet.get(j)) {matchFound=true; break;}}
        					// Color is unique!
        					if (!matchFound) {colorSet.add(candidateColor);}
    					}
    				}
    				// Assign the new unique colors to the town color array 
    				for (int i=1; i<colorSet.size(); i++)
    				{
    					townColorArray[i] = 15-colorSet.get(i);
    				}
    	        }
    		}
			// Change each entry that's -1 to a unique color
			for (int c=1; c<(townColorArray.length); c++)
			{
				// The color
        		if (villagetagcompound.hasKey("townColor"+(c+1))) {townColorArray[c] = villagetagcompound.getInteger("townColor"+(c+1));} // is already registered as its own NBT flag
        		else if (townColorArray[c]==-1) // must be generated
        		{
        			while(true)
        			{
        				townColorArray[c] = (Integer) FunctionsVN.weightedRandom(BannerGenerator.colorMeta, BannerGenerator.colorWeights, randomFromXYZ);
        				// Compare to all previous colors to ensure it's unique
        				boolean isRedundant=false;
        				for (int i=0; i<c; i++) {if (townColorArray[c]==townColorArray[i]) {isRedundant=true; break;}}
        				if (!isRedundant) {break;} // Keep this color and move on to the next one
        			}
        		}
        		
        		// Now that the townColorArray is populated, assign the colors to the town
				if (!villagetagcompound.hasKey("townColor"+(c+1))) {updateTownNBT=true; villagetagcompound.setInteger("townColor"+(c+1), townColorArray[c]);}
			}

			// Add tags for village and biome types
			if (!villagetagcompound.hasKey("villageType")) {villagetagcompound.setString("villageType", FunctionsVN.VillageType.getVillageTypeFromBiome(world, posX, posZ).toString());}
			if (!villagetagcompound.hasKey("materialType")) {villagetagcompound.setString("materialType", FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, posX, posZ).toString());}
			
			// Replace the old tag
			if (updateTownNBT)
			{
        		nbttaglist.set(0, villagetagcompound);
        		tagCompound.setTag(townSignEntry, nbttaglist);
        		data.markDirty();
			}
    		
			return villagetagcompound;
		}
		
		
		// --- TRY 3: just make a new VN entry --- //
		
    	
    	villagetagcompound = new NBTTagCompound();
    	
		MapGenStructureData structureData;
		NBTTagCompound nbttagcompound = null;
		int villageArea = -1; // If a village area value is not ascertained, this will remain as -1.
		
		try
		{
			structureData = (MapGenStructureData)world.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "Village");
			nbttagcompound = structureData.getTagCompound();
		}
		catch (Exception e) // Village.dat does not exist
		{
			try // Open Terrain Generation version
    		{
    			structureData = (MapGenStructureData)world.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "OTGVillage");
    			nbttagcompound = structureData.getTagCompound();
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
		
		villagetagcompound.setString("namePrefix", namePrefix);
        villagetagcompound.setString("nameRoot", nameRoot);
        villagetagcompound.setString("nameSuffix", nameSuffix);
        
		// Generate banner info, regardless of if we make a banner.
		Object[] newRandomBanner = BannerGenerator.randomBannerArrays(randomFromXYZ, -1, -1);
		ArrayList<String> patternArray = (ArrayList<String>) newRandomBanner[0];
		ArrayList<Integer> colorArray = (ArrayList<Integer>) newRandomBanner[1];
		ItemStack villageBanner = BannerGenerator.makeBanner(patternArray, colorArray);
		townColorMeta = 15-colorArray.get(0);

        villagetagcompound.setInteger("signX", posX);
        villagetagcompound.setInteger("signY", posY);
        villagetagcompound.setInteger("signZ", posZ);
        villagetagcompound.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
        
		boolean updateTownNBT=false;
		int[] townColorArray = new int[]{townColorMeta,-1,-1,-1,-1,-1,-1};
		// The banner color array has repeat values. Create a version that does not. 
		ArrayList<Integer> colorSet = new ArrayList();
		// Add the first element straight away
		colorSet.add(colorArray.get(0));
		// Go through the colors in the array and add them only if they're unique
		for (int i=1; i<colorArray.size(); i++)
		{
			int candidateColor = colorArray.get(i);
			boolean matchFound = false;
			// Go through colors already in the set and seee if this matches any of them
			for (int j=0; j<colorSet.size(); j++) {if (candidateColor==colorSet.get(j)) {matchFound=true; break;}}
			// Color is unique!
			if (!matchFound) {colorSet.add(candidateColor);}
		}
		// Assign the new unique colors to the town color array 
		for (int i=1; i<colorSet.size(); i++)
		{
			townColorArray[i] = 15-colorSet.get(i);
		}
		// Change each entry that's -1 to a unique color
		for (int c=1; c<(townColorArray.length); c++)
		{
			// The color
    		if (villagetagcompound.hasKey("townColor"+(c+1))) {townColorArray[c] = villagetagcompound.getInteger("townColor"+(c+1));} // is already registered as its own NBT flag
    		else if (townColorArray[c]==-1) // must be generated
    		{
    			while(true)
    			{
    				townColorArray[c] = (Integer) FunctionsVN.weightedRandom(BannerGenerator.colorMeta, BannerGenerator.colorWeights, randomFromXYZ);
    				// Compare to all previous colors to ensure it's unique
    				boolean isRedundant=false;
    				for (int i=0; i<c; i++) {if (townColorArray[c]==townColorArray[i]) {isRedundant=true; break;}}
    				if (!isRedundant) {break;} // Keep this color and move on to the next one
    			}
    		}
    		
    		// Now that the townColorArray is populated, assign the colors to the town
			if (!villagetagcompound.hasKey("townColor"+(c+1))) {updateTownNBT=true; villagetagcompound.setInteger("townColor"+(c+1), townColorArray[c]);}
		}

		// Add tags for village and biome types
		villagetagcompound.setString("villageType", FunctionsVN.VillageType.getVillageTypeFromBiome(world, posX, posZ).toString());
		villagetagcompound.setString("materialType", FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, posX, posZ).toString());
		
		// Make the data bundle to save to NBT
		nbttaglist = new NBTTagList();
		
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
		int tries = 100;
    	
    	while(true)
    	{
        	entityvillager = new EntityVillager(world);
        	Method populateBuyingList_m = ReflectionHelper.findMethod(EntityVillager.class, "populateBuyingList", "func_175554_cu"); // Updated 1.11 FindMethod version and changed to EntityVillagerZombie
    		IModularSkin ims = entityvillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
    				
    		// Set profession
    		if (profession==-1)
    		{
    			if (VillageGeneratorConfigHandler.spawnModdedVillagers)
    			{
    				VillagerRegistry.setRandomProfession(entityvillager, random);
    			}
    			else
    			{
    				entityvillager.setProfession(random.nextInt(6));
    			}
    			
    			VillagerRegistry.setRandomProfession(entityvillager, entityvillager.world.rand);
    			
    			// Equally weight career subdivisions
    			if (entityvillager.getProfession()>=0 && entityvillager.getProfession()<=4)
    			{
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
    			}
    			else {break;} // Non-vanilla profession value
    		}
    		else {entityvillager.setProfession(profession);}
    		
    		ReflectionHelper.setPrivateValue(EntityVillager.class, entityvillager, career, new String[]{"careerId", "field_175563_bv"});
    		ims.setCareer(career);
    		
			// Populate the villager's buying list
			try {populateBuyingList_m.invoke(entityvillager);} catch (Exception e) {if (GeneralConfig.debugMessages) {LogHelper.warn("Could not invoke EntityVillager.populateBuyingList method");} break;}
			
			// Make sure this villager has the stats you requested
			if (
					(entityvillager.getProfession() == profession
					&& (Integer)ReflectionHelper.getPrivateValue(EntityVillager.class, entityvillager, new String[]{"careerId", "field_175563_bv"}) == career
					&& ims.getCareer() == career)
					|| tries-- <= 0 // Just give up and accept what we've got
					)
			{
				/*
				LogHelper.info(
		    			"Villager Profession: " + entityvillager.getProfession()
		    			+ ", requested Profession: " + profession
		    			+ ", Villager Career: " + ReflectionHelper.getPrivateValue(EntityVillager.class, entityvillager, new String[]{"careerId", "field_175563_bv"})
		    			+ ", Villager CareerVN: " + ims.getCareer()
		    			+ ", requested Career: " + career
		    			);*/
				break;
			}
			else {entityvillager.setDead();}
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
    	// These values are shared by all components in a village.
    	// If a world is loaded and a village has to regenerate, the start piece will be null and so these will be inaccessible.
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
    	
    	// For use with the town center only. These should not be needed on a partial world load.
    	public boolean villagersGenerated = false;
    	public int bannerY = 0;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	
        public StartVN() {}

        public StartVN(BiomeProvider biomeProvider, int componentType, Random random, int posX, int posZ, List components, float villageSize)
        {
            super(biomeProvider, componentType, random, posX, posZ, components, villageSize%1<random.nextFloat()?1:0);
            
            this.biome = biomeProvider.getBiome(new BlockPos(posX, 64, posZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
            
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
            	String mappeddisallowModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
            	if (mappeddisallowModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
            	else {this.disallowModSubs = false;}
            	}
			catch (Exception e) {this.disallowModSubs = false;}
        }
        
        // Beth had this great idea to publicly call these protected methods lmao :'C
        //public int getXWithOffsetPublic(int xOffset, int zOffset) {return this.getXWithOffset(xOffset, zOffset);}
        //public int getYWithOffsetPublic(int yOffset) {return this.getYWithOffset(yOffset);}
        //public int getZWithOffsetPublic(int xOffset, int zOffset) {return this.getZWithOffset(xOffset, zOffset);}
    }
    
    
    /**
     * Gets the next village component, with the bounding box shifted -1 in the X and Z direction.
     */
    protected static StructureComponent getNextComponentNN(StartVN start, List components, Random random, int yOffset, int lateralOffset, EnumFacing coordBaseMode, int componentType, StructureBoundingBox boundingBox)
    {
        switch (coordBaseMode)
        {
            case SOUTH:
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX - 1, boundingBox.minY + yOffset, boundingBox.minZ + lateralOffset, EnumFacing.WEST, componentType);
            case WEST:
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX + lateralOffset, boundingBox.minY + yOffset, boundingBox.minZ - 1, EnumFacing.NORTH, componentType);
            case NORTH:
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX - 1, boundingBox.minY + yOffset, boundingBox.minZ + lateralOffset, EnumFacing.WEST, componentType);
            case EAST:
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX + lateralOffset, boundingBox.minY + yOffset, boundingBox.minZ - 1, EnumFacing.NORTH, componentType);
            default:
                return null;
        }
    }

    /**
     * Gets the next village component, with the bounding box shifted +1 in the X and Z direction.
     */
    protected static StructureComponent getNextComponentPP(StartVN start, List components, Random random, int yOffset, int lateralOffset, EnumFacing coordBaseMode, int componentType, StructureBoundingBox boundingBox)
    {
        switch (coordBaseMode)
        {
            case SOUTH:
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.maxX + 1, boundingBox.minY + yOffset, boundingBox.minZ + lateralOffset, EnumFacing.EAST, componentType);
            case WEST:
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX + lateralOffset, boundingBox.minY + yOffset, boundingBox.maxZ + 1, EnumFacing.SOUTH, componentType);
            case NORTH:
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.maxX + 1, boundingBox.minY + yOffset, boundingBox.minZ + lateralOffset, EnumFacing.EAST, componentType);
            case EAST:
                return StructureVillageVN.getNextVillageStructureComponent(start, components, random, boundingBox.minX + lateralOffset, boundingBox.minY + yOffset, boundingBox.maxZ + 1, EnumFacing.SOUTH, componentType);
            default:
                return null;
        }
    }
    
    
    public static class DecorTorch extends StructureVillagePieces.Village
    {
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
    	
    	public Biome biome=null;
    	
    	private static final int GROUND_LEVEL = 0; // Spaces above the bottom of the structure considered to be "ground level"
    	private int averageGroundLevel = -1;
    	
        public DecorTorch() {}
        
        public DecorTorch(StartVN start, int componentType, Random random, StructureBoundingBox structureBB, EnumFacing coordBaseMode)
        {
            super(start, componentType);
            this.setCoordBaseMode(coordBaseMode);
            this.boundingBox = structureBB;
            // Additional stuff to be used in the construction
            if (start!=null)
            {
            	this.villageType=start.villageType;
            	this.materialType=start.materialType;
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
        
        public static StructureBoundingBox findPieceBox(StartVN start, List components, Random random, int x, int y, int z, EnumFacing coordBaseMode)
        {
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(
            		x, y, z, // Structure coordinates
            		0, 0, 0, // u, v, w offset. The u and w offsets are positive for horizIndex <=1 and negative for horizIndex >=2
            		3, 4, 3, // width, height, depth
            		coordBaseMode);
            
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
        				true, (byte)1, this.getCoordBaseMode().getHorizontalIndex());
        		
                if (this.averageGroundLevel < 0) {return true;} // Do not construct in a void

                this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.minY - GROUND_LEVEL, 0);
            }
        	
        	// In the event that this village construction is resuming after being unloaded
        	// you may need to reestablish the village name/color/type info
            if (
                	this.villageType==null
                	|| this.materialType==null
                	|| this.townColor==-1
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
            	
            	BiomeProvider chunkManager= world.getBiomeProvider();
            	int posX = (this.boundingBox.minX+this.boundingBox.maxX)/2; int posZ = (this.boundingBox.minZ+this.boundingBox.maxZ)/2;
            	Biome biome = chunkManager.getBiome(new BlockPos(posX, 64, posZ));
    			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
                
    			try {
                	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedVillageType.equals("")) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, posX, posZ);}
                	else {this.villageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
                	}
    			catch (Exception e) {this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(chunkManager, posX, posZ);}
    			
    			try {
                	String mappedMaterialType = (String) (mappedBiomes.get("MaterialTypes")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedMaterialType.equals("")) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, posX, posZ);}
                	else {this.materialType = FunctionsVN.MaterialType.getMaterialTypeFromName(mappedMaterialType, FunctionsVN.MaterialType.OAK);}
                	}
    			catch (Exception e) {this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(chunkManager, posX, posZ);}
    			
    			try {
                	String mappedBlockModSubs = (String) (mappedBiomes.get("DisallowModSubs")).get(mappedBiomes.get("BiomeNames").indexOf((String)(ReflectionHelper.getPrivateValue(Biome.class, biome, new String[]{"biomeName","field_76791_y"}))));
                	if (mappedBlockModSubs.toLowerCase().trim().equals("nosub")) {this.disallowModSubs = true;}
                	else {this.disallowModSubs = false;}
                	}
    			catch (Exception e) {this.disallowModSubs = false;}
            	
            }
        	// Reestablish biome if start was null or something
            if (this.biome==null) {this.biome = world.getBiome(new BlockPos(
            		(this.boundingBox.minX+this.boundingBox.maxX)/2, 0, (this.boundingBox.minZ+this.boundingBox.maxZ)/2));}

        	IBlockState biomeDirtState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.DIRT.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	IBlockState biomeGrassState = StructureVillageVN.getBiomeSpecificBlockState(Blocks.GRASS.getDefaultState(), this.materialType, this.biome, this.disallowModSubs);
        	// Establish top and filler blocks, substituting Grass and Dirt if they're null
        	IBlockState biomeTopState=biomeGrassState; if (this.biome!=null && this.biome.topBlock!=null) {biomeTopState=this.biome.topBlock;}
        	IBlockState biomeFillerState=biomeDirtState; if (this.biome!=null && this.biome.fillerBlock!=null) {biomeFillerState=this.biome.fillerBlock;}
        	
        	// Make dirt foundation
			this.replaceAirAndLiquidDownwards(world, biomeFillerState, 1, -2, 1, structureBB);
			// Top with grass
        	this.setBlockState(world, biomeTopState, 1, -1, 1, structureBB);
            
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
            		decorBlueprint = DesertStructures.getDesertDecorBlueprint(0, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ);//, 5); // Use lime
            	}
            	else if (this.villageType==FunctionsVN.VillageType.TAIGA)
            	{
            		decorBlueprint = TaigaStructures.getTaigaDecorBlueprint(6, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ);
            	}
            	else if (this.villageType==FunctionsVN.VillageType.SAVANNA)
            	{
            		decorBlueprint = SavannaStructures.getSavannaDecorBlueprint(0, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ);
            	}
            	else if (this.villageType==FunctionsVN.VillageType.SNOWY)
            	{
            		decorBlueprint = SnowyStructures.getSnowyDecorBlueprint(randomFromXYZ.nextInt(3), this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ);
            	}
            	else if (this.villageType==FunctionsVN.VillageType.JUNGLE)
            	{
            		decorBlueprint = JungleStructures.getJungleDecorBlueprint(2+randomFromXYZ.nextInt(6), this.villageType, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ);
            	}
            	else if (this.villageType==FunctionsVN.VillageType.SWAMP)
            	{
            		decorBlueprint = SwampStructures.getSwampDecorBlueprint(randomFromXYZ.nextInt(7), this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ);
            	}
            	else // Plains
            	{
            		decorBlueprint = PlainsStructures.getPlainsDecorBlueprint(0, this.materialType, this.disallowModSubs, this.biome, this.getCoordBaseMode(), randomFromXYZ);
            	}
            	
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
    }
    
    
    // --- Path --- //
    
    public static class PathVN extends StructureVillagePieces.Road
    {
        private int length;
        
        public PathVN() {}

        public PathVN(StructureVillagePieces.Start start, int componentType, Random random, StructureBoundingBox boundingBox, EnumFacing facing)
        {
            super(start, componentType);
            this.setCoordBaseMode(facing);
            this.boundingBox = boundingBox;
            this.length = Math.max(boundingBox.getXSize(), boundingBox.getZSize());
        }
        
        @Override
        protected void writeStructureToNBT(NBTTagCompound tagCompound)
        {
            super.writeStructureToNBT(tagCompound);
            tagCompound.setInteger("Length", this.length);
        }
        
        @Override
        protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager templateManager)
        {
            super.readStructureFromNBT(tagCompound, templateManager);
            this.length = tagCompound.getInteger("Length");
        }

        /**
         * Initiates construction of the Structure Component picked, at the current Location of StructGen
         */
        @Override
        public void buildComponent(StructureComponent start, List components, Random random)
        {
            boolean flag = false;
            int i;
            StructureComponent structurecomponent1;

        	// Construct buildings on the -x/-z sides of roads
            for (i = random.nextInt(5); i < this.length - 8; i += 2 + random.nextInt(5))
            {
            	structurecomponent1 = StructureVillageVN.getNextComponentNN((StartVN)start, components, random, 0, i, this.getCoordBaseMode(), this.getComponentType(), this.getBoundingBox());
                
                if (structurecomponent1 != null)
                {
                    i += Math.max(structurecomponent1.getBoundingBox().getXSize(), structurecomponent1.getBoundingBox().getZSize());
                    flag = true;
                }
            }
            
            // Construct buildings on the +x/+z sides of roads
            for (i = random.nextInt(5); i < this.length - 8; i += 2 + random.nextInt(5))
            {
            	structurecomponent1 = StructureVillageVN.getNextComponentPP((StartVN)start, components, random, 0, i, this.getCoordBaseMode(), this.getComponentType(), this.getBoundingBox());
                
                if (structurecomponent1 != null)
                {
                    i += Math.max(structurecomponent1.getBoundingBox().getXSize(), structurecomponent1.getBoundingBox().getZSize());
                    flag = true;
                }
            }

            if (flag && random.nextInt(3) > 0 && this.getCoordBaseMode() != null)
            {
                switch (this.getCoordBaseMode())
                {
	                case NORTH:
	                    generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
	                    break;
                    case SOUTH:
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                        break;
                    case WEST:
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        break;
                    case EAST:
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    default: // This shouldn't be reached because paths don't go vertically
                }
            }

            if (flag && random.nextInt(3) > 0 && this.getCoordBaseMode() != null)
            {
                switch (this.getCoordBaseMode())
                {
	                case NORTH:
	                    generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
	                    break;
                    case SOUTH:
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                        break;
                    case WEST:
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        break;
                    case EAST:
                        generateAndAddRoadPiece((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    default: // This shouldn't be reached because paths don't go vertically
                }
            }
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
        			
                	BlockPos blockpos = new BlockPos(x, 64, z);
                	
                	if (structureBB.isVecInside(blockpos))
                    {
                    	// Gets ground level, so long as it's not leaves or other foliage
                        int y = getAboveTopmostSolidOrLiquidBlockVN(world, blockpos).getY() - 1;
                        
                        if (startPiece_reflected==null)
                        {
                        	setPathSpecificBlock(world, FunctionsVN.MaterialType.getMaterialTemplateForBiome(world, x, z), world.getBiome(new BlockPos(x, y, z)), false, x, y, z, true);
                        }
                        else
                        {
                        	setPathSpecificBlock(world, ((StartVN)startPiece_reflected).materialType, ((StartVN)startPiece_reflected).biome, ((StartVN)startPiece_reflected).disallowModSubs, x, y, z, true);
                        }
                    }
                }
            }

            return true;
        }
    }
    
    /**
	 * hangingOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 * -X: returns the value X - used for things like upright barrels
	 */
	public static int chooseHangingMeta(int orientation, EnumFacing coordBaseMode)
	{
		return HANGING_META_ARRAY[orientation][coordBaseMode.getHorizontalIndex()];
	}
    /**
	 * vineOrientation:
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static int chooseVineMeta(int orientation)
	{
		return VINE_META_ARRAY[orientation];
	}
	public static int chooseLadderMeta(int orientation)
	{
		if (orientation<0) {return -orientation;}
		return LADDER_META_ARRAY[orientation];
	}
	
	public static int chooseFurnaceMeta(int orientation, EnumFacing coordBaseMode)
	{
		if (orientation<0) {return -orientation;}
		return FURNACE_META_ARRAY[orientation][coordBaseMode.getHorizontalIndex()];
	}
	
	public static int chooseButtonMeta(int orientation)
	{
		return BUTTON_META_ARRAY[orientation];
	}
	
	public static int chooseGlazedTerracottaMeta(int orientation, EnumFacing coordBaseMode)
	{
		return GLAZED_TERRACOTTA_META_ARRAY[orientation][coordBaseMode.getHorizontalIndex()];
	}
	
	public static int chooseBlastFurnaceMeta(int orientation, EnumFacing coordBaseMode)
	{
		if (orientation<0) {return -orientation;}
		return BLAST_FURNACE_META_ARRAY[orientation][coordBaseMode.getHorizontalIndex()];
	}
	public static int chooseCartographyTableMeta(int orientation, EnumFacing coordBaseMode)
	{
		if (orientation<0) {return -orientation;}
		return CARTOGRAPHY_TABLE_META_ARRAY[orientation][coordBaseMode.getHorizontalIndex()];
	}
	
	public static int chooseAnvilMeta(int orientation, EnumFacing coordBaseMode)
	{
		return ANVIL_META_ARRAY[orientation][coordBaseMode.getHorizontalIndex()];
	}
	
	public static int chooseFMCGrindstoneHangingMeta(int orientation, EnumFacing coordBaseMode)
	{
		if (orientation<0) {return -orientation;}
		return HANGING_GRINDSTONE_META_ARRAY[orientation][coordBaseMode.getHorizontalIndex()];
	}

	public static int chooseFenceGateMeta(int orientation, boolean isOpen)
	{
		return FENCE_GATE_META_ARRAY[orientation] + (isOpen?4:0);
	}
	
	public static int chooseBibliocraftDeskMeta(int orientation, EnumFacing coordBaseMode)
	{
		return (StructureVillageVN.BIBLIOCRAFT_DESK_META_ARRAY[orientation][coordBaseMode.getHorizontalIndex()])%4;
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
	public static int[] getDoorMetas(int orientation, EnumFacing coordBaseMode, boolean isShut, boolean isRightHanded)
	{
		int horizIndex = coordBaseMode.getHorizontalIndex();
		
		return new int[] {
				DOOR_META_ARRAY[orientation] + (isShut?0:4),
				isRightHanded ? 8 : 9
						};
	}


	/**
	 * Returns a random animal from the /structures/village/common/animals folder, not including cats
	 */
	public static EntityLiving getVillageAnimal(World world, BlockPos pos, Random random, boolean includeHorses, boolean includeSheep, boolean includeOtherAnimals, boolean mooshroomsInsteadOfCows)
	{
		EntityLiving animal = null;
		
		int chickenWeight = 0; // How much to weight chickens vs other animals
		int sheepWeight = 2; // How much to weight sheep vs other animals
		int horseWeight = 5; // How much to weight horses vs other animals
		
		// Make blank arraylist
		ArrayList<EntityLiving> arraylist_animal = new ArrayList<>(); 
		
		// Check Animania animal list
		// For each entry that is not null, add to list
		ArrayList<EntityLiving> arraylist_temp_animal = new ArrayList<>();
		
		arraylist_temp_animal.clear();
		if (includeHorses)
		{
			if (VillageGeneratorConfigHandler.animaniaLivestock)
			{
				for (String animal_namespace : ModObjects.animania_horse)
				{
					EntityLiving testEntity = null;
					
					try {testEntity = (EntityLiving)EntityList.createEntityByIDFromName(new ResourceLocation(animal_namespace), world);}
					catch (Exception e) {}
					
					if (testEntity != null) {for (int i=0; i<horseWeight; i++) {arraylist_temp_animal.add((EntityLiving) testEntity);}}
				}
			}
			
			if (VillageGeneratorConfigHandler.villageAnimalRestrictionLevel<1)
        	{
				// Add vanilla horses regardless
				for (int i=0; i<horseWeight; i++) {arraylist_temp_animal.add(new EntityHorse(world));}
        	}
		}
		// Dump temp animals into main animal array
		for (EntityLiving transfer_animal : arraylist_temp_animal) {arraylist_animal.add(transfer_animal);}
		
		arraylist_temp_animal.clear();
		if (includeSheep)
		{
			if (VillageGeneratorConfigHandler.animaniaLivestock)
			{
				for (String animal_namespace : ModObjects.animania_sheep)
				{
					EntityLiving testEntity = null;
					
					try {testEntity = (EntityLiving)EntityList.createEntityByIDFromName(new ResourceLocation(animal_namespace), world);}
					catch (Exception e) {}
					
					if (testEntity != null) {for (int i=0; i<sheepWeight; i++) {arraylist_temp_animal.add((EntityLiving) testEntity);}}
				}
			}
			
			if (arraylist_temp_animal.isEmpty() && VillageGeneratorConfigHandler.villageAnimalRestrictionLevel<1) {for (int i=0; i<sheepWeight; i++) {arraylist_temp_animal.add(new EntitySheep(world));}} 
		}
		// Dump temp animals into main animal array
		for (EntityLiving transfer_animal : arraylist_temp_animal) {arraylist_animal.add(transfer_animal);}

		if (includeOtherAnimals)
		{
			arraylist_temp_animal.clear();
			// Chicken
			if (VillageGeneratorConfigHandler.animaniaLivestock)
			{
				for (String animal_namespace : ModObjects.animania_chicken)
				{
					EntityLiving testEntity = null;
					
					try {testEntity = (EntityLiving)EntityList.createEntityByIDFromName(new ResourceLocation(animal_namespace), world);}
					catch (Exception e) {}
					
					if (testEntity != null) {for (int i=0; i<chickenWeight; i++) {arraylist_temp_animal.add((EntityLiving) testEntity);}}
				}
			}
			// Dump temp animals into main animal array
			for (EntityLiving transfer_animal : arraylist_temp_animal) {arraylist_animal.add(transfer_animal);}
			
			arraylist_temp_animal.clear();
			// Goat
			if (VillageGeneratorConfigHandler.animaniaLivestock)
			{
				for (String animal_namespace : ModObjects.animania_goat)
				{
					EntityLiving testEntity = null;
					
					try {testEntity = (EntityLiving)EntityList.createEntityByIDFromName(new ResourceLocation(animal_namespace), world);}
					catch (Exception e) {}
					
					if (testEntity != null) {for (int i=0; i<1; i++) {arraylist_temp_animal.add((EntityLiving) testEntity);}}
				}
			}
			// Dump temp animals into main animal array
			for (EntityLiving transfer_animal : arraylist_temp_animal) {arraylist_animal.add(transfer_animal);}
			
			arraylist_temp_animal.clear();
			// Pig
			if (VillageGeneratorConfigHandler.animaniaLivestock)
			{
				for (String animal_namespace : ModObjects.animania_pig)
				{
					EntityLiving testEntity = null;
					
					try {testEntity = (EntityLiving)EntityList.createEntityByIDFromName(new ResourceLocation(animal_namespace), world);}
					catch (Exception e) {}
					
					if (testEntity != null) {for (int i=0; i<1; i++) {arraylist_temp_animal.add((EntityLiving) testEntity);}}
				}
			}
			if (arraylist_temp_animal.isEmpty() && VillageGeneratorConfigHandler.villageAnimalRestrictionLevel<1) {for (int i=0; i<1; i++) {arraylist_animal.add(new EntityPig(world));}} 
			// Dump temp animals into main animal array
			for (EntityLiving transfer_animal : arraylist_temp_animal) {arraylist_animal.add(transfer_animal);}
			
			arraylist_temp_animal.clear();
			// Cow or Mooshroom
			if (VillageGeneratorConfigHandler.animaniaLivestock)
			{
				for (String animal_namespace : mooshroomsInsteadOfCows ? ModObjects.animania_mooshroom:ModObjects.animania_cow)
				{
					EntityLiving testEntity = null;
					
					try {testEntity = (EntityLiving)EntityList.createEntityByIDFromName(new ResourceLocation(animal_namespace), world);}
					catch (Exception e) {}
					
					if (testEntity != null) {for (int i=0; i<1; i++) {arraylist_temp_animal.add((EntityLiving) testEntity);}}
				}
			}
			if (arraylist_temp_animal.isEmpty() && VillageGeneratorConfigHandler.villageAnimalRestrictionLevel<1) {for (int i=0; i<1; i++) {arraylist_temp_animal.add(mooshroomsInsteadOfCows ? new EntityMooshroom(world) : new EntityCow(world));}}
			
			// Dump temp animals into main animal array
			for (EntityLiving transfer_animal : arraylist_temp_animal) {arraylist_animal.add(transfer_animal);}
		}
		
		
		// Pick a random animal from the list
		animal = arraylist_animal.get(random.nextInt(arraylist_animal.size()));
		
		// To give the animal random spawning properties (horse pattern, sheep color, etc)
		IEntityLivingData ientitylivingdata = animal.onInitialSpawn(world.getDifficultyForLocation(pos), null);
		
		return animal;
	}
	
	
	/**
	 * relativeOrientation
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static int getBedOrientationMeta(int relativeOrientation, EnumFacing coordBaseMode, boolean isHead)
	{
		int horizIndex = coordBaseMode.getHorizontalIndex();
		
		return BED_META_ARRAY[relativeOrientation][horizIndex] + (isHead ? 8:0);
	}
	
	
	/**
	 * relativeOrientation
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 * stage is growth stage: 0-2. This results in an additional meta of +0/+4/+8
	 */
	public static int getCocoaPodOrientationMeta(int relativeOrientation, EnumFacing coordBaseMode, int stage)
	{
		return BED_META_ARRAY[relativeOrientation][coordBaseMode.getHorizontalIndex()] + (stage*4);
	}
	
	
	/**
	 * relativeOrientation
	 * Updated for 1.9+ because all objects are assumed to be at coordBaseMode=NORTH (horizIndex=2)
	 * 0=fore-facing (away from you); 1=right-facing; 2=back-facing (toward you); 3=left-facing
	 */
	public static int getTrapdoorMeta(int relativeOrientation, boolean isTop, boolean isVertical)
	{
		int meta = 0;
		
		switch (relativeOrientation)
		{
		case 0: // Facing away
			meta = 0; break;
		case 1: // Facing right
			meta = 3; break;
		default:
		case 2: // Facing you
			meta = 1; break;
		case 3: // Facing left
			meta = 2; break;
		}
		
		meta += (isVertical?4:0);
		meta += (isTop?8:0);
		
		return meta;
	}
	
	
	/**
	 * Used to select crops. Returns a two-element array that are either two different harvestcraft/JAFFA crops, or the same vanilla/beetroot crop twice.
	 */
	public static Block[] chooseCropPair(Random random)
	{
		Block[] cropblocks = new Block[]{Blocks.WHEAT, Blocks.WHEAT}; // What ultimately to return
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
			// Unique to 1.12
			tryModCrop = Block.getBlockFromName(ModObjects.cropAgaveHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropAmaranthHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropArrowrootHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropCassavaHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropChickpeaHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropElderberryHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropFlaxHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropGigapickleHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropGreengrapeHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropHuckleberryHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropJicamaHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropJuniperHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropJuteHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropKaleHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropKenafHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropKohlrabiHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropLentilHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropMilletHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropMulberryHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropQuinoaHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropSisalHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropTaroHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
			tryModCrop = Block.getBlockFromName(ModObjects.cropTomatilloHC); if (tryModCrop != null) {cropArray.add(tryModCrop);}
		}
		
		// If there are any modded crops added, return them.
		if (cropArray.size()>0)
		{
			for (int i=0; i<2; i++) {cropblocks[i] = cropArray.get(random.nextInt(cropArray.size()));}
			return cropblocks;
		}
		
		// If you've reached this stage, you will be returning vanilla crops
		cropArray.add(Blocks.WHEAT); cropArray.add(Blocks.WHEAT); cropArray.add(Blocks.WHEAT); cropArray.add(Blocks.WHEAT); cropArray.add(Blocks.WHEAT);
		cropArray.add(Blocks.CARROTS); cropArray.add(Blocks.CARROTS);
		cropArray.add(Blocks.POTATOES); cropArray.add(Blocks.POTATOES);
		cropArray.add(Blocks.BEETROOTS);
		
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
	
	/**
	 * Pick a random decor component based off of village type
	 * The decor will be randomly selected based on the village type, except you can specify whether to allow troughs for Taiga types
	 */
	public static ArrayList<BlueprintData> getRandomDecorBlueprint(VillageType villageType, MaterialType materialType, boolean disallowModSubs, Biome biome, EnumFacing coordBaseMode, Random random, boolean allowTaigaTroughs)
	{
		switch (villageType)
		{
		default:
		case PLAINS:
			return PlainsStructures.getRandomPlainsDecorBlueprint(materialType, disallowModSubs, biome, coordBaseMode, random);
		case DESERT:
			return DesertStructures.getRandomDesertDecorBlueprint(materialType, disallowModSubs, biome, coordBaseMode, random);
		case TAIGA:
			return allowTaigaTroughs ? 
					TaigaStructures.getRandomTaigaDecorBlueprint(materialType, disallowModSubs, biome, coordBaseMode, random)
					:TaigaStructures.getTaigaDecorBlueprint(1+random.nextInt(6), materialType, disallowModSubs, biome, coordBaseMode, random);
		case SAVANNA:
			return SavannaStructures.getRandomSavannaDecorBlueprint(materialType, disallowModSubs, biome, coordBaseMode, random);
		case SNOWY:
			return SnowyStructures.getRandomSnowyDecorBlueprint(materialType, disallowModSubs, biome, coordBaseMode, random);
		case JUNGLE:
			return JungleStructures.getRandomJungleDecorBlueprint(villageType, materialType, disallowModSubs, biome, coordBaseMode, random);
		case SWAMP:
			return SwampStructures.getRandomSwampDecorBlueprint(materialType, disallowModSubs, biome, coordBaseMode, random);
		}
	}
	
	
	/**
	 * Deletes EntityItems within a given structure bounding box
	 */
	public static void cleanEntityItems(World world, StructureBoundingBox boundingBox)
	{
		// selectEntitiesWithinAABB is an AABB method
		AxisAlignedBB aabb = (new AxisAlignedBB(
				// Modified to center onto front of house
				boundingBox.minX, boundingBox.minY, boundingBox.minZ,
				boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ)).expand(3, 8, 3);
        
        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, aabb, null);
        
		if (!list.isEmpty())
        {
			Iterator iterator = list.iterator();
					
			while (iterator.hasNext())
			{
				EntityItem entityitem = (EntityItem) iterator.next();
				entityitem.setDead();
			}
			
			if (GeneralConfig.debugMessages) {LogHelper.info("Cleaned "+list.size()+" Entity items within " + aabb.toString());}
        }
	}
	
	
    /**
     * Used to generate flower pots with contents that are tougher to access than just meta data alone
     */
    public static boolean generateStructureFlowerPot(World world, StructureBoundingBox box, Random random, BlockPos pos, Block block, int meta)
    {
        if (box.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.FLOWER_POT)
        {
        	IBlockState flowerPotState = Blocks.FLOWER_POT.getDefaultState();
        	
            world.setBlockState(pos, flowerPotState, 2);
            
            // This is here just in case a flower pot can't be placed at the given position
            TileEntityFlowerPot tileentityflowerpot = (TileEntityFlowerPot)world.getTileEntity(pos);
            
            if (tileentityflowerpot != null)
            {
                if (!isNotPlaceableIntoPot(block, meta))
                {
                    return false;
                }
                else
                {
                	// Sets the flower pot's item and meta
                	if (block==null) {return false;}
                	Item potContentItem = Item.getItemFromBlock(block);
                	if (potContentItem==null) {return false;}
                	ItemStack potContentItemStack = new ItemStack(potContentItem);
                	
                    tileentityflowerpot.setItemStack(potContentItemStack);
                    
                    tileentityflowerpot.markDirty();

                    if (!world.setBlockState(pos, flowerPotState, 2))
                    {
                    	world.notifyBlockUpdate(pos, flowerPotState, flowerPotState, 2);
                    }
                    
                    return true;
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }
    
	
    /**
     * Returns "true" if the block is not something that can be placed into a flower pot.
     */
    private static boolean isNotPlaceableIntoPot(Block block, int meta)
    {
    	return     block != Blocks.YELLOW_FLOWER
        		&& block != Blocks.RED_FLOWER
        		&& block != Blocks.CACTUS
        		&& block != Blocks.BROWN_MUSHROOM
        		&& block != Blocks.RED_MUSHROOM
        		&& block != Blocks.SAPLING
        		&& block != Blocks.DEADBUSH ? block == Blocks.TALLGRASS && meta == 2 : true;
    }
}