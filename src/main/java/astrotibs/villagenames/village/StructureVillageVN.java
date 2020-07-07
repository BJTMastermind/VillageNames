package astrotibs.villagenames.village;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.capabilities.IModularSkin;
import astrotibs.villagenames.capabilities.ModularSkinProvider;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.integration.ModObjects;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.nbt.VNWorldDataStructure;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.utility.FunctionsVN.MaterialType;
import astrotibs.villagenames.utility.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumFacing;
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
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class StructureVillageVN
{
    public static List getStructureVillageWeightedPieceList(Random random, int villageSize)
    {
        ArrayList arraylist = new ArrayList();
        
        if (GeneralConfig.structureLegacySmallHouse) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House4Garden.class, 4, MathHelper.getRandomIntegerInRange(random, 2 + villageSize, 4 + villageSize * 2)));}
        if (GeneralConfig.structureLegacyChurch) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Church.class, 20, MathHelper.getRandomIntegerInRange(random, 0 + villageSize, 1 + villageSize)));}
        if (GeneralConfig.structureLegacyLibrary) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House1.class, 20, MathHelper.getRandomIntegerInRange(random, 0 + villageSize, 2 + villageSize)));}
        if (GeneralConfig.structureLegacyHut) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.WoodHut.class, 3, MathHelper.getRandomIntegerInRange(random, 2 + villageSize, 5 + villageSize * 3)));}
        if (GeneralConfig.structureLegacyButcherShop) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Hall.class, 15, MathHelper.getRandomIntegerInRange(random, 0 + villageSize, 2 + villageSize)));}
        if (GeneralConfig.structureLegacyLargeFarm) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field1.class, 3, MathHelper.getRandomIntegerInRange(random, 1 + villageSize, 4 + villageSize)));}
        if (GeneralConfig.structureLegacySmallFarm) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.Field2.class, 3, MathHelper.getRandomIntegerInRange(random, 2 + villageSize, 4 + villageSize * 2)));}
        if (GeneralConfig.structureLegacySmithy) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House2.class, 15, MathHelper.getRandomIntegerInRange(random, 0, 1 + villageSize)));}
        if (GeneralConfig.structureLegacyLargeHouse) {arraylist.add(new StructureVillagePieces.PieceWeight(StructureVillagePieces.House3.class, 8, MathHelper.getRandomIntegerInRange(random, 0 + villageSize, 3 + villageSize * 2)));}
        
        VillagerRegistry.addExtraVillageComponents(arraylist, random, villageSize);

        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext())
        {
            if (((StructureVillagePieces.PieceWeight)iterator.next()).villagePiecesLimit == 0)
            {
                iterator.remove();
            }
        }

        return arraylist;
    }
    
    
    // Pasted in from StructureVillagePieces so that I can access it, particularly to expand the allowed village biomes
    // This prepares a new path component to build upon
    // Called func_176066_d in 1.8
    public static StructureComponent getNextVillageStructureComponent(StructureVillagePieces.Start start, List<StructureComponent> components, Random rand, int x, int y, int z, EnumFacing facing, int componentType)
    {
        if (componentType > 50)
        {
            return null;
        }
        else if (Math.abs(x - start.getBoundingBox().minX) <= 112 && Math.abs(z - start.getBoundingBox().minZ) <= 112)
        {
        	
			// Attach another structure
			Method generateComponent_reflected = ReflectionHelper.findMethod(
					StructureVillagePieces.class, null, new String[]{"generateComponent", "func_176067_c"},
					StructureVillagePieces.Start.class, List.class, Random.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, EnumFacing.class, Integer.TYPE
					);
			
			StructureVillagePieces.Village structurecomponent = null;

			try
			{
				structurecomponent = (StructureVillagePieces.Village)generateComponent_reflected.invoke(start, (StructureVillagePieces.Start)start, components, rand, x, y, z, facing, componentType + 1);
			}
    		catch (Exception e)
			{
    			if (GeneralConfig.debugMessages) LogHelper.warn("Could not invoke StructureVillagePieces.generateComponent method");
    		}
			
        	//StructureComponent structurecomponent = func_176067_c(start, components, rand, x, y, z, facing, componentType + 1);
            
            if (structurecomponent != null)
            {
                int medianX = (structurecomponent.getBoundingBox().minX + structurecomponent.getBoundingBox().maxX) / 2;
                int medianZ = (structurecomponent.getBoundingBox().minZ + structurecomponent.getBoundingBox().maxZ) / 2;
                int rangeX = structurecomponent.getBoundingBox().maxX - structurecomponent.getBoundingBox().minX;
                int rangeZ = structurecomponent.getBoundingBox().maxZ - structurecomponent.getBoundingBox().minZ;
                int bboxWidth = rangeX > rangeZ ? rangeX : rangeZ;
                
                // Replaces the ordinary "areBiomesViable" method with one that uses the VN biome config list
                Biome biome = start.getBiomeProvider().getBiomeGenerator(new BlockPos(medianX, 0, medianZ));

            	if (GeneralConfig.spawnBiomesNames != null) // Biome list is not empty
        		{
        			for (int i = 0; i < GeneralConfig.spawnBiomesNames.length; i++)
        			{
        				if (GeneralConfig.spawnBiomesNames[i].equals(biome.getBiomeName()))
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
    
    // Pasted in from StructureVillagePieces so that I can access it
    // This prepares a new path component to build upon
    // func_176069_e for 1.8
    public static StructureComponent getNextComponentVillagePath(StructureVillagePieces.Start start, List<StructureComponent> components, Random rand, int x, int y, int z, EnumFacing facing, int componentType)
    {
        if (componentType > 3 + start.terrainType)
        {
            return null;
        }
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
                Biome biome = start.getBiomeProvider().getBiomeGenerator(new BlockPos(medianX, 0, medianZ));

            	if (GeneralConfig.spawnBiomesNames != null) // Biome list is not empty
        		{
        			for (int i = 0; i < GeneralConfig.spawnBiomesNames.length; i++)
        			{
        				if (GeneralConfig.spawnBiomesNames[i].equals(biome.getBiomeName()))
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

            if ((blockstate.getMaterial().blocksMovement() && !block.isLeaves(world.getBlockState(blockpos1), world, blockpos1) && !block.isFoliage(world, blockpos1))
            		|| blockstate.getMaterial().isLiquid()
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
     * Use outlineOnly if you'd like to tally only the boundary values.
     */
    public static int getMedianGroundLevel(World world, StructureBoundingBox boundingBox, boolean outlineOnly)
    {
    	ArrayList<Integer> i = new ArrayList<Integer>();
    	
        for (int k = boundingBox.minZ; k <= boundingBox.maxZ; ++k)
        {
            for (int l = boundingBox.minX; l <= boundingBox.maxX; ++l)
            {
                if (boundingBox.isVecInside(new BlockPos(l, 64, k)))
                {
                	if (!outlineOnly || (outlineOnly && (k==boundingBox.minZ || k==boundingBox.maxZ || l==boundingBox.minX || l==boundingBox.maxX)))
                	{
                		//i.add(Math.max(world.getTopSolidOrLiquidBlock(l, k), world.provider.getAverageGroundLevel())); // getAverageGroundLevel returns 64
                		//LogHelper.info("height " + world.getTopSolidOrLiquidBlock(l, k) + " at " + l + " " + k);
                		i.add(getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(l, 64, k)).getY());
                		//i.add(Math.max(world.getTopSolidOrLiquidBlock(l, k), world.provider.getAverageGroundLevel()));
                	}
                }
            }
        }
        
        return FunctionsVN.medianIntArray(i, true);
    }
    
    /**
     * Biome-specific block replacement
     */
    public static IBlockState getBiomeSpecificBlock(IBlockState blockstate, MaterialType materialType, Biome biome)
    {
    	if (materialType==null || biome==null) {return blockstate;}
    	
    	Block block = blockstate.getBlock();
    	int meta = block.getMetaFromState(blockstate);
    	
    	if (materialType == FunctionsVN.MaterialType.SPRUCE)
        {
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {return Blocks.LOG.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()%4);}
        	if (block == Blocks.PLANKS)                        {return Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata());}
        	if (block == Blocks.OAK_FENCE)					   {return Blocks.SPRUCE_FENCE.getDefaultState();}
	        if (block == Blocks.OAK_FENCE_GATE)				   {return Blocks.SPRUCE_FENCE_GATE.getDefaultState();}
        	if (block == Blocks.OAK_STAIRS)                    {return Blocks.SPRUCE_STAIRS.getStateFromMeta(meta);}
        	if (block == Blocks.WOODEN_SLAB)                   {return Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +BlockPlanks.EnumType.SPRUCE.getMetadata(): meta==8? 8 +BlockPlanks.EnumType.SPRUCE.getMetadata() : meta);}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {return Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata());}
        	if (block == Blocks.ACACIA_DOOR
        		|| block == Blocks.BIRCH_DOOR
        		|| block == Blocks.DARK_OAK_DOOR
        		|| block == Blocks.JUNGLE_DOOR
        		|| block == Blocks.OAK_DOOR
        		|| block == Blocks.SPRUCE_DOOR)
        													   {return Blocks.SPRUCE_DOOR.getStateFromMeta(meta);}
        	//if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(1), meta};}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(1, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(1, false), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 1};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogSpruceUTD), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 1};}
        }
        if (materialType == FunctionsVN.MaterialType.BIRCH)
        {
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {return Blocks.LOG.getStateFromMeta(BlockPlanks.EnumType.BIRCH.getMetadata()%4);}
        	if (block == Blocks.PLANKS)                        {return Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.BIRCH.getMetadata());}
        	if (block == Blocks.OAK_FENCE)					   {return Blocks.BIRCH_FENCE.getDefaultState();}
        	if (block == Blocks.OAK_FENCE_GATE)				   {return Blocks.BIRCH_FENCE_GATE.getDefaultState();}
        	if (block == Blocks.OAK_STAIRS)                    {return Blocks.BIRCH_STAIRS.getStateFromMeta(meta);}
        	if (block == Blocks.WOODEN_SLAB)                   {return Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +BlockPlanks.EnumType.BIRCH.getMetadata(): meta==8? 8 +BlockPlanks.EnumType.BIRCH.getMetadata() : meta);}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {return Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(BlockPlanks.EnumType.BIRCH.getMetadata());}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
			   												   {return Blocks.BIRCH_DOOR.getStateFromMeta(meta);}
        	//if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(2), meta};}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(2, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(2, false), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 2};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogBirchUTD), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 2};}
        }
        if (materialType == FunctionsVN.MaterialType.JUNGLE)
        {
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {return Blocks.LOG.getStateFromMeta(BlockPlanks.EnumType.JUNGLE.getMetadata()%4);}
        	if (block == Blocks.COBBLESTONE)                   {return Blocks.MOSSY_COBBLESTONE.getDefaultState();}
        	//if (block == Blocks.STONE_STAIRS)                  {
			//										        		block = Block.getBlockFromName(ModObjects.mossyCOBBLESTONEStairsUTD);
			//										        		if (block==null) {block = Blocks.STONE_STAIRS;}
			//										        		return new Object[]{block, meta};
        	//												   } // Mossy COBBLESTONE stairs
        	if (block == Blocks.COBBLESTONE_WALL)              {return Blocks.COBBLESTONE_WALL.getStateFromMeta(1);} // Mossy COBBLESTONE wall
        	if (block == Blocks.PLANKS)                        {return Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.JUNGLE.getMetadata());}
        	if (block == Blocks.OAK_FENCE)					   {return Blocks.JUNGLE_FENCE.getDefaultState();}
        	if (block == Blocks.OAK_FENCE_GATE)				   {return Blocks.JUNGLE_FENCE_GATE.getDefaultState();}
        	if (block == Blocks.OAK_STAIRS)                    {return Blocks.JUNGLE_STAIRS.getStateFromMeta(meta);}
        	if (block == Blocks.WOODEN_SLAB)                   {return Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +BlockPlanks.EnumType.JUNGLE.getMetadata(): meta==8? 8 +BlockPlanks.EnumType.JUNGLE.getMetadata() : meta);}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {return Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(BlockPlanks.EnumType.JUNGLE.getMetadata());}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
        													   {return Blocks.JUNGLE_DOOR.getStateFromMeta(meta);}
        	//if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(3), meta};}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(3, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(3, false), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 3};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogJungleUTD), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 3};}
        }
        if (materialType == FunctionsVN.MaterialType.ACACIA)
        {
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {return Blocks.LOG2.getStateFromMeta(BlockPlanks.EnumType.ACACIA.getMetadata()%4);}
        	if (block == Blocks.PLANKS)                        {return Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.ACACIA.getMetadata());}
        	if (block == Blocks.OAK_FENCE)					   {return Blocks.ACACIA_FENCE.getDefaultState();}
        	if (block == Blocks.OAK_FENCE_GATE)				   {return Blocks.ACACIA_FENCE_GATE.getDefaultState();}
        	if (block == Blocks.OAK_STAIRS)                    {return Blocks.ACACIA_STAIRS.getStateFromMeta(meta);}
        	if (block == Blocks.WOODEN_SLAB)                   {return Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +BlockPlanks.EnumType.ACACIA.getMetadata(): meta==8? 8 +BlockPlanks.EnumType.ACACIA.getMetadata() : meta);}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {return Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(BlockPlanks.EnumType.ACACIA.getMetadata());}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
			   												   {return Blocks.ACACIA_DOOR.getStateFromMeta(meta);}
        	//if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(4), meta};}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(4, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(4, false), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 4};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogAcaciaUTD), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog2EF), 4*meta + 0};}
        }
        if (materialType == FunctionsVN.MaterialType.DARK_OAK)
        {
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {return Blocks.LOG2.getStateFromMeta(BlockPlanks.EnumType.DARK_OAK.getMetadata()%4);}
        	if (block == Blocks.PLANKS)                        {return Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.DARK_OAK.getMetadata());}
        	if (block == Blocks.OAK_FENCE)					   {return Blocks.DARK_OAK_FENCE.getDefaultState();}
        	if (block == Blocks.OAK_FENCE_GATE)				   {return Blocks.DARK_OAK_FENCE_GATE.getDefaultState();}
        	if (block == Blocks.OAK_STAIRS)                    {return Blocks.DARK_OAK_STAIRS.getStateFromMeta(meta);}
        	if (block == Blocks.WOODEN_SLAB)                   {return Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +BlockPlanks.EnumType.DARK_OAK.getMetadata(): meta==8? 8 +BlockPlanks.EnumType.DARK_OAK.getMetadata() : meta);}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {return Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(BlockPlanks.EnumType.DARK_OAK.getMetadata());}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
			   												   {return Blocks.DARK_OAK_DOOR.getStateFromMeta(meta);}
        	//if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(5), meta};}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(5, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(5, false), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 5};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogDarkOakUTD), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog2EF), 4*meta + 1};}
        }
        if (materialType == FunctionsVN.MaterialType.SAND)
        {
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {return Blocks.SANDSTONE.getStateFromMeta(2);} // Cut sandstone
        	if (block == Blocks.COBBLESTONE)                   {return Blocks.SANDSTONE.getStateFromMeta(0);} // Regular sandstone
        	if (block == Blocks.MOSSY_COBBLESTONE)             {return Blocks.SANDSTONE.getStateFromMeta(0);} // Regular sandstone
        	if (block == Blocks.PLANKS)                        {return Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.JUNGLE.getMetadata());}
        	if (block == Blocks.OAK_FENCE)					   {return Blocks.JUNGLE_FENCE.getDefaultState();}
			if (block == Blocks.ACACIA_FENCE_GATE || block == Blocks.BIRCH_FENCE_GATE || block == Blocks.DARK_OAK_FENCE_GATE || block == Blocks.JUNGLE_FENCE_GATE || block == Blocks.OAK_FENCE_GATE || block == Blocks.SPRUCE_FENCE_GATE)
															   {return Blocks.JUNGLE_FENCE_GATE.getDefaultState();}
        	if (block == Blocks.OAK_STAIRS)                    {return Blocks.JUNGLE_STAIRS.getStateFromMeta(meta);}
        	if (block == Blocks.STONE_STAIRS)                  {return Blocks.SANDSTONE_STAIRS.getStateFromMeta(meta);}
        	//if (block == Blocks.COBBLESTONE_WALL)              {
			//										        		block = Block.getBlockFromName(ModObjects.SANDSTONEWallUTD);
			//										        		if (block==null) {block = Blocks.SANDSTONE;}
			//										        		return new Object[]{block, 0};
			//												   } // Sandstone wall
        	if (block == Blocks.GRAVEL)                        {return Blocks.SANDSTONE.getStateFromMeta(0);}
        	if (block == Blocks.DIRT)                          {return Blocks.SAND.getStateFromMeta(0);}
        	if (block == Blocks.GRASS)                         {return Blocks.SAND.getStateFromMeta(0);}
        	if (block == Blocks.WOODEN_SLAB)                   {return Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +BlockPlanks.EnumType.JUNGLE.getMetadata(): meta==8? 8 +BlockPlanks.EnumType.JUNGLE.getMetadata() : meta);}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {return Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(BlockPlanks.EnumType.JUNGLE.getMetadata());}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
        													   {return Blocks.JUNGLE_DOOR.getStateFromMeta(meta);}
        	//if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(3), meta};} // Jungle trapdoor
        	if (block == Blocks.STONE_SLAB)                    {return Blocks.STONE_SLAB.getStateFromMeta(meta==3? 1: meta==11? 9 : meta);} // Sandstone slab
        	if (block == Blocks.DOUBLE_STONE_SLAB)             {return Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(4);} // Brick double slab
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(3, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(3, false), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 3};} // Jungle bark
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogJungleUTD), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 3};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{Blocks.SANDSTONE, 2};} // Cut sandstone
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Blocks.SANDSTONE, 2};} // Cut sandstone
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Blocks.SANDSTONE, 2};} // Cut sandstone
        }
        if (materialType == FunctionsVN.MaterialType.MESA)
        {
        	if (block == Blocks.COBBLESTONE)                   {return Blocks.HARDENED_CLAY.getDefaultState();} // TODO - change stain color with village colors?
        	if (block == Blocks.MOSSY_COBBLESTONE)             {return Blocks.HARDENED_CLAY.getDefaultState();}
        	if (block == Blocks.STONE_STAIRS)                  {return Blocks.BRICK_STAIRS.getStateFromMeta(meta);}
        	if (block == Blocks.GRAVEL)                        {return Blocks.HARDENED_CLAY.getDefaultState();}
        	if (block == Blocks.DIRT)                          {return Blocks.CLAY.getDefaultState();}
        	if (block == Blocks.GRASS)                         {return Blocks.CLAY.getDefaultState();}
        	if (block == Blocks.STONE_SLAB)                    {return Blocks.STONE_SLAB.getStateFromMeta(meta==3? 4: meta==11? 12 : meta);} // Brick slab
        	if (block == Blocks.DOUBLE_STONE_SLAB)             {return Blocks.DOUBLE_STONE_SLAB.getStateFromMeta(1);} // Sandstone double slab
        	//if (block == Blocks.COBBLESTONE_WALL)              {
			//										        		block = Block.getBlockFromName(ModObjects.brickWallUTD);
			//										        		if (block==null) {block = Blocks.brick_block;}
			//										        		return new Object[]{block, 0};
			//												   } // Brick wall
        }
        if (materialType == FunctionsVN.MaterialType.SNOW)
        {
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {return Blocks.LOG.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata()%4);}
        	if (block == Blocks.PLANKS)                        {return Blocks.PLANKS.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata());}
        	if (block == Blocks.OAK_FENCE)					   {return Blocks.SPRUCE_FENCE.getDefaultState();}
        	if (block == Blocks.OAK_FENCE_GATE)				   {return Blocks.SPRUCE_FENCE_GATE.getDefaultState();}
        	if (block == Blocks.OAK_STAIRS)                    {return Blocks.SPRUCE_STAIRS.getStateFromMeta(meta);}
        	if (block == Blocks.WOODEN_SLAB)                   {return Blocks.WOODEN_SLAB.getStateFromMeta(meta==0? 0 +BlockPlanks.EnumType.SPRUCE.getMetadata(): meta==8? 8 +BlockPlanks.EnumType.SPRUCE.getMetadata() : meta);}
        	if (block == Blocks.DOUBLE_WOODEN_SLAB)            {return Blocks.DOUBLE_WOODEN_SLAB.getStateFromMeta(BlockPlanks.EnumType.SPRUCE.getMetadata());}
        	if (block == Blocks.ACACIA_DOOR || block == Blocks.BIRCH_DOOR || block == Blocks.DARK_OAK_DOOR || block == Blocks.JUNGLE_DOOR || block == Blocks.OAK_DOOR || block == Blocks.SPRUCE_DOOR)
			   												   {return Blocks.SPRUCE_DOOR.getStateFromMeta(meta);}
        	//if (block == Blocks.trapdoor)                      {return new Object[]{ModObjects.chooseModWoodenTrapdoor(1), meta};}
        	if (block == Blocks.COBBLESTONE)                   {Blocks.PACKED_ICE.getDefaultState();}
        	if (block == Blocks.MOSSY_COBBLESTONE)             {Blocks.PACKED_ICE.getDefaultState();}
        	if (block == Blocks.GRAVEL)                        {Blocks.PACKED_ICE.getDefaultState();}
        	//if (block == Blocks.standing_sign)                 {return new Object[]{ModObjects.chooseModWoodenSign(1, true), meta/4};}
        	//if (block == Blocks.wall_sign)                     {return new Object[]{ModObjects.chooseModWoodenSign(1, false), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.barkEF)) {return new Object[]{block, 1};} // Spruce bark
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLogOakUTD)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLogSpruceUTD), meta};}
        	//if (block != null && block == Block.getBlockFromName(ModObjects.strippedLog1EF)) {return new Object[]{Block.getBlockFromName(ModObjects.strippedLog1EF), 4*meta + 1};}
        }
        if (materialType == FunctionsVN.MaterialType.MUSHROOM)
        {
        	if (block == Blocks.LOG || block == Blocks.LOG2)   {Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(15);} // Stem on all six sides
        	if (block == Blocks.COBBLESTONE)                   {Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14);} // Cap on all six sides
        	if (block == Blocks.MOSSY_COBBLESTONE)             {Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(14);} // Cap on all six sides
        	if (block == Blocks.PLANKS)                        {Blocks.BROWN_MUSHROOM_BLOCK.getStateFromMeta(0);} // Pores on all six sides
        }
        
        // Post Forge event
        BiomeEvent.GetVillageBlockID event = new BiomeEvent.GetVillageBlockID(biome == null ? null : biome, block.getStateFromMeta(meta));
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        if (event.getResult() == Result.DENY) return event.getReplacement();//new Object[]{event.replacement.getBlock(), meta};
        
        return blockstate;
    }
    
    
    /**
     * Returns the direction-shifted metadata for blocks that require orientation, e.g. doors, stairs, ladders.
     * Copied from 1.8 because 1.9 doesn't have it anymore
     */
    /*
    public static int getMetadataWithOffset(Block blockIn, int meta, EnumFacing coordBaseMode)
    {
        if (blockIn == Blocks.RAIL)
        {
            if (coordBaseMode == EnumFacing.WEST || coordBaseMode == EnumFacing.EAST)
            {
                return meta==1 ? 0 : 1;
            }
        }
        else if (blockIn instanceof BlockDoor)
        {
            if (coordBaseMode == EnumFacing.SOUTH)
            {
                if (meta == 0) {return 2;}
                if (meta == 2) {return 0;}
            }
            else
            {
                if (coordBaseMode == EnumFacing.WEST) {return meta + 1 & 3;}
                if (coordBaseMode == EnumFacing.EAST) {return meta + 3 & 3;}
            }
        }
        else if (blockIn != Blocks.STONE_STAIRS && blockIn != Blocks.OAK_STAIRS && blockIn != Blocks.NETHER_BRICK_STAIRS && blockIn != Blocks.STONE_BRICK_STAIRS && blockIn != Blocks.SANDSTONE_STAIRS)
        {
            if (blockIn == Blocks.LADDER)
            {
                if (coordBaseMode == EnumFacing.SOUTH)
                {
                    if (meta == EnumFacing.NORTH.getIndex()) {return EnumFacing.SOUTH.getIndex();}
                    if (meta == EnumFacing.SOUTH.getIndex()) {return EnumFacing.NORTH.getIndex();}
                }
                else if (coordBaseMode == EnumFacing.WEST)
                {
                    if (meta == EnumFacing.NORTH.getIndex()) {return EnumFacing.WEST.getIndex();}
                    if (meta == EnumFacing.SOUTH.getIndex()) {return EnumFacing.EAST.getIndex();}
                    if (meta == EnumFacing.WEST.getIndex()) {return EnumFacing.NORTH.getIndex();}
                    if (meta == EnumFacing.EAST.getIndex()) {return EnumFacing.SOUTH.getIndex();}
                }
                else if (coordBaseMode == EnumFacing.EAST)
                {
                    if (meta == EnumFacing.NORTH.getIndex()) {return EnumFacing.EAST.getIndex();}
                    if (meta == EnumFacing.SOUTH.getIndex()) {return EnumFacing.WEST.getIndex();}
                    if (meta == EnumFacing.WEST.getIndex()) {return EnumFacing.NORTH.getIndex();}
                    if (meta == EnumFacing.EAST.getIndex()) {return EnumFacing.SOUTH.getIndex();}
                }
            }
            else if (blockIn == Blocks.STONE_BUTTON)
            {
                if (coordBaseMode == EnumFacing.SOUTH)
                {
                    if (meta == 3) {return 4;}
                    if (meta == 4) {return 3;}
                }
                else if (coordBaseMode == EnumFacing.WEST)
                {
                	switch(meta)
                	{
        	        	case 1: return 4;
        	        	case 2: return 3;
        	        	case 3: return 1;
        	        	case 4: return 2;
                	}
                }
                else if (coordBaseMode == EnumFacing.EAST)
                {
                	switch(meta)
                	{
        	        	case 1: return 4;
        	        	case 2: return 3;
        	        	case 3: return 2;
        	        	case 4: return 1;
                	}
                }
            }
            else if (blockIn != Blocks.TRIPWIRE_HOOK && !(blockIn instanceof BlockDirectional))
            {
                if (blockIn == Blocks.PISTON || blockIn == Blocks.STICKY_PISTON || blockIn == Blocks.LEVER || blockIn == Blocks.DISPENSER)
                {
                    if (coordBaseMode == EnumFacing.SOUTH)
                    {
                        if (meta == EnumFacing.NORTH.getIndex() || meta == EnumFacing.SOUTH.getIndex())
                        {
                            return EnumFacing.getFront(meta).getOpposite().getIndex();
                        }
                    }
                    else if (coordBaseMode == EnumFacing.WEST)
                    {
                        if (meta == EnumFacing.NORTH.getIndex()) {return EnumFacing.WEST.getIndex();}
                        if (meta == EnumFacing.SOUTH.getIndex()) {return EnumFacing.EAST.getIndex();}
                        if (meta == EnumFacing.WEST.getIndex()) {return EnumFacing.NORTH.getIndex();}
                        if (meta == EnumFacing.EAST.getIndex()) {return EnumFacing.SOUTH.getIndex();}
                    }
                    else if (coordBaseMode == EnumFacing.EAST)
                    {
                        if (meta == EnumFacing.NORTH.getIndex()) {return EnumFacing.EAST.getIndex();}
                        if (meta == EnumFacing.SOUTH.getIndex()) {return EnumFacing.WEST.getIndex();}
                        if (meta == EnumFacing.WEST.getIndex()) {return EnumFacing.NORTH.getIndex();}
                        if (meta == EnumFacing.EAST.getIndex()) {return EnumFacing.SOUTH.getIndex();}
                    }
                }
            }
            else
            {
                EnumFacing enumfacing = EnumFacing.getHorizontal(meta);

                if (coordBaseMode == EnumFacing.SOUTH)
                {
                    if (enumfacing == EnumFacing.SOUTH || enumfacing == EnumFacing.NORTH)
                    {
                        return enumfacing.getOpposite().getHorizontalIndex();
                    }
                }
                else if (coordBaseMode == EnumFacing.WEST)
                {
                    if (enumfacing == EnumFacing.NORTH) {return EnumFacing.WEST.getHorizontalIndex();}
                    if (enumfacing == EnumFacing.SOUTH) {return EnumFacing.EAST.getHorizontalIndex();}
                    if (enumfacing == EnumFacing.WEST) {return EnumFacing.NORTH.getHorizontalIndex();}
                    if (enumfacing == EnumFacing.EAST) {return EnumFacing.SOUTH.getHorizontalIndex();}
                }
                else if (coordBaseMode == EnumFacing.EAST)
                {
                    if (enumfacing == EnumFacing.NORTH) {return EnumFacing.EAST.getHorizontalIndex();}
                    if (enumfacing == EnumFacing.SOUTH) {return EnumFacing.WEST.getHorizontalIndex();}
                    if (enumfacing == EnumFacing.WEST) {return EnumFacing.NORTH.getHorizontalIndex();}
                    if (enumfacing == EnumFacing.EAST) {return EnumFacing.SOUTH.getHorizontalIndex();}
                }
            }
        }
        else if (coordBaseMode == EnumFacing.SOUTH)
        {
            if (meta == 2) {return 3;}
            if (meta == 3) {return 2;}
        }
        else if (coordBaseMode == EnumFacing.WEST)
        {
        	switch(meta)
        	{
	        	case 0: return 2;
	        	case 1: return 3;
	        	case 2: return 0;
	        	case 3: return 1;
        	}
        }
        else if (coordBaseMode == EnumFacing.EAST)
        {
        	switch(meta)
        	{
	        	case 0: return 2;
	        	case 1: return 3;
	        	case 2: return 1;
	        	case 3: return 0;
        	}
        }

        return meta;
    }
    */
    
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
    public static int setPathSpecificBlock(World world, StructureVillageVN.StartVN startPiece, int meta, int posX, int posY, int posZ)
    {
    	IBlockState grassPath = getBiomeSpecificBlock(Blocks.GRASS_PATH.getDefaultState(), startPiece.materialType, startPiece.biome);
    	IBlockState planks = getBiomeSpecificBlock(Blocks.PLANKS.getStateFromMeta(0), startPiece.materialType, startPiece.biome);
    	IBlockState gravel = getBiomeSpecificBlock(Blocks.GRAVEL.getDefaultState(), startPiece.materialType, startPiece.biome);
    	IBlockState cobblestone = getBiomeSpecificBlock(Blocks.COBBLESTONE.getStateFromMeta(0), startPiece.materialType, startPiece.biome);
    	
    	// Top block level
    	int surfaceY = StructureVillageVN.getAboveTopmostSolidOrLiquidBlockVN(world, new BlockPos(posX, 0, posZ)).down().getY();
    	
    	// Raise Y to be at least below sea level
    	if (surfaceY < world.getSeaLevel()) {surfaceY = world.getSeaLevel()-1;}
    	
    	while (surfaceY >= world.getSeaLevel()-1)
    	{
    		Block surfaceBlock = world.getBlockState(new BlockPos(posX, surfaceY, posZ)).getBlock();
    		BlockPos pos = new BlockPos(posX, Math.max(surfaceY, posY), posZ);
    		
    		// Replace grass with grass path
    		
    		if (surfaceBlock instanceof BlockGrass && world.isAirBlock(new BlockPos(posX, Math.max(surfaceY, posY), posZ).up()))
    		{
    			world.setBlockState(pos, grassPath, 2);
    			return Math.max(surfaceY, posY);
    		}
    		
    		// Replace sand with GRAVEL supported by COBBLESTONE
    		if (surfaceBlock instanceof BlockSand)
    		{
    			world.setBlockState(pos, gravel, 2);
    			world.setBlockState(pos.down(), cobblestone, 2);
    			return Math.max(surfaceY, posY);
    		}
    		
    		// Replace lava with two-layer COBBLESTONE
    		if (surfaceBlock==Blocks.LAVA || surfaceBlock==Blocks.FLOWING_LAVA)
    		{
    			world.setBlockState(pos, cobblestone, 2);
    			world.setBlockState(pos.down(), cobblestone, 2);
    			return Math.max(surfaceY, posY);
    		}
    		
    		// Replace other liquid with planks
    		if (surfaceBlock.getDefaultState().getMaterial().isLiquid())
    		{
    			world.setBlockState(pos, planks, 2);
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
    public static NBTTagCompound getOrMakeVNInfo(World world, int posX, int posY, int posZ) {return getOrMakeVNInfo(world, new BlockPos(posX, posY, posZ));}
    public static NBTTagCompound getOrMakeVNInfo(World world, BlockPos pos)
    {
    	int posX = pos.getX(); int posY = pos.getY(); int posZ = pos.getZ(); 
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
					FunctionsVN.getUniqueLongForXYZ(pos.getX(), pos.getY(), pos.getZ())
    			);
		
		// First, search through all previously generated VN villages and see if this is inside
    	// the bounding box of one of them.
    	
		
		// --- TRY 1: See if the village already exists in the vanilla collection object and try to match it to this village --- //
		
		
		Village villageNearTarget = world.villageCollectionObj.getNearestVillage(pos, villageRadiusBuffer);
		
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
            			while (townColorMeta2==townColorMeta)
            			{
            				townColorMeta2 = (Integer) FunctionsVN.weightedRandom(BannerGenerator.colorMeta, BannerGenerator.colorWeights, randomFromXYZ);
            			}
            		}
            		
            		villagetagcompound.setInteger("townColor2", townColorMeta2);
            		
            		// Replace the old tag
            		nbttaglist.set(0, villagetagcompound);
            		tagCompound.setTag(townSignEntry, nbttaglist);
            		data.markDirty();
        		}
				
				// Now find the nearest Village to that sign's coordinate, within villageRadiusBuffer blocks outside the radius.
				Village villageNearSign = world.villageCollectionObj.getNearestVillage(new BlockPos(townX, townY, townZ), villageRadiusBuffer);
				
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
		Set tagmapKeyset = tagCompound.getKeySet(); //Gets the town key list: "coordinates"
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
						while (townColorMeta2==townColorMeta)
						{
							townColorMeta2 = (Integer) FunctionsVN.weightedRandom(BannerGenerator.colorMeta, BannerGenerator.colorWeights, randomFromXYZ);
						}
					}
					
					villagetagcompound.setInteger("townColor2", townColorMeta2);
					
					// Replace the old tag
					nbttaglist.set(0, villagetagcompound);
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
		
		// Set profession
		if (profession==-1)
		{
			if (GeneralConfig.spawnModdedVillagers)
			{
				net.minecraftforge.fml.common.registry.VillagerRegistry.setRandomProfession(entityvillager, random);
			}
			else
			{
				entityvillager.setProfession(random.nextInt(5)); //GeneralConfig.enableNitwit ? random.nextInt(6) : random.nextInt(5)); // TODO - Enable Nitwit setting in 1.10
			}
			
			VillagerRegistry.setRandomProfession(entityvillager, entityvillager.worldObj.rand);
			
			// Equally weight career subdivisions
			if (entityvillager.getProfession()>=0 && entityvillager.getProfession()<=4)
			{
				IModularSkin ims = entityvillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
				
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
				ims.setCareer(career);
			}
		}
		else
		{
			entityvillager.setProfession(profession);
			
			// Set career
			if (career > 0)
			{
				IModularSkin ims = entityvillager.getCapability(ModularSkinProvider.MODULAR_SKIN, null);
				ims.setCareer(career);
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
    	public boolean villagersGenerated = false;
    	public int bannerY = -1;
    	public ArrayList<Integer> decorHeightY = new ArrayList();
    	
        public StartVN() {}

        public StartVN(BiomeProvider biomeProvider, int componentType, Random random, int posX, int posZ, List components, int terrainType)
        {
            super(biomeProvider, componentType, random, posX, posZ, components, terrainType);
            this.villageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);
            this.materialType = FunctionsVN.MaterialType.getMaterialTemplateForBiome(biomeProvider, posX, posZ);
        }
        
        // Beth had this great idea to publicly call these protected methods lmao :'C
        //public int getXWithOffsetPublic(int xOffset, int zOffset) {return this.getXWithOffset(xOffset, zOffset);}
        //public int getYWithOffsetPublic(int yOffset) {return this.getYWithOffset(yOffset);}
        //public int getZWithOffsetPublic(int xOffset, int zOffset) {return this.getZWithOffset(xOffset, zOffset);}
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
        protected void readStructureFromNBT(NBTTagCompound tagCompound)
        {
            super.readStructureFromNBT(tagCompound);
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

            for (i = random.nextInt(5); i < this.length - 8; i += 2 + random.nextInt(5))
            {
                structurecomponent1 = this.getNextComponentNN((StructureVillagePieces.Start)start, components, random, 0, i);

                if (structurecomponent1 != null)
                {
                    i += Math.max(structurecomponent1.getBoundingBox().getXSize(), structurecomponent1.getBoundingBox().getZSize());
                    flag = true;
                }
            }

            for (i = random.nextInt(5); i < this.length - 8; i += 2 + random.nextInt(5))
            {
                structurecomponent1 = this.getNextComponentPP((StructureVillagePieces.Start)start, components, random, 0, i);

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
                        getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                        break;
                    case SOUTH:
                        getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                        break;
                    case WEST:
                        getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
                        break;
                    case EAST:
                        getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    default: // This shouldn't be reached because paths don't go vertically
                }
            }

            if (flag && random.nextInt(3) > 0 && this.getCoordBaseMode() != null)
            {
                switch (this.getCoordBaseMode())
                {
                    case NORTH:
                        getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                        break;
                    case SOUTH:
                        getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                        break;
                    case WEST:
                        getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
                        break;
                    case EAST:
                        getNextComponentVillagePath((StructureVillagePieces.Start)start, components, random, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
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
            for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i)
            {
                for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j)
                {
                	BlockPos blockpos = new BlockPos(i, 64, j);
                	
                	if (structureBB.isVecInside(blockpos))
                    {
                    	// Gets ground level, so long as it's not leaves or other foliage
                		blockpos = world.getTopSolidOrLiquidBlock(blockpos).down();
                        
                        setPathSpecificBlock(world, (StartVN)startPiece_reflected, 0, i, blockpos.getY(), j);
                    }
                }
            }

            return true;
        }
    }
}