package astrotibs.villagenames.structure;

import java.util.Collection;

import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.structure.MapGenStructure;

public interface IStructureGenProvider {
	//public boolean canUseOnProvider(IChunkProvider provider);
	public boolean canUseOnProvider(IChunkGenerator provider);

	//public Collection<MapGenStructure> listProviders(IChunkProvider provider);
	public Collection<MapGenStructure> listProviders(IChunkGenerator provider);
}