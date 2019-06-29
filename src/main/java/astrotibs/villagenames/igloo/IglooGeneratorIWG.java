package astrotibs.villagenames.igloo;

import java.util.Random;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.IWorldGenerator;

public class IglooGeneratorIWG implements IWorldGenerator {

	private static VNMapGenIgloo iglooGenerator = new VNMapGenIgloo();
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		
		//Block[] ablock = new Block[65536];
		ChunkPrimer chunkprimer = new ChunkPrimer();
		
		if (
				(world.provider.getDimensionId() == 0)
				&& (world.getWorldInfo().isMapFeaturesEnabled())
				)
		{
			// THIS BLOCK RUNS REGARDLESS OF WHETHER GENERATION TYPE IS VANILLA, ALTERNATE, REALISTIC
			//iglooGenerator = (VNMapGenIgloo) TerrainGen.getModdedMapGen(new VNMapGenIgloo(), EventType.SCATTERED_FEATURE);
			iglooGenerator = (VNMapGenIgloo) TerrainGen.getModdedMapGen(new VNMapGenIgloo(), EventType.CUSTOM); //Fixed for version 3.0.2
			
			// This block only seems to fire with a vanilla world generator :(
			if (VNMapGenIgloo.canSpawnStructureAtCoords(world, chunkX, chunkZ))//.canSpawnStructureAtCoords(world, chunkX, chunkZ))
			{
				int chunkOffset = 2;
				for (int OM_x = -chunkOffset; OM_x <= chunkOffset-1; OM_x++) {
					for (int OM_z = -chunkOffset; OM_z <= chunkOffset-1; OM_z++) {
						
						this.iglooGenerator.generate(world.getChunkProvider(), world, chunkX, chunkZ, chunkprimer); //THIS needed for chunk generation
						
						try {
							boolean generateInChunk = this.iglooGenerator.generateStructure(world, rand, new ChunkCoordIntPair(chunkX+OM_x, chunkZ+OM_z)); //THIS needed for chunk generation
		                }
		                catch (Exception e) {
		                }
						
					}
				}
				
			}
		}

	}
	
	public static VNMapGenIgloo getIglooGenerator()
	{
		return iglooGenerator;
	}
	
}