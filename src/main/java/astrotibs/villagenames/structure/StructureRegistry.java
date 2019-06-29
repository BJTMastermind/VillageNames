package astrotibs.villagenames.structure;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import astrotibs.villagenames.utility.LogHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.ChunkProviderOverworld;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;

public class StructureRegistry {

	private static <T> void addMapGen(Collection<MapGenStructure> output, Class<T> klazz, T provider, String... names) {
		try {
			MapGenStructure struct = ReflectionHelper.getPrivateValue(klazz, provider, names);
			if (struct != null) output.add(struct);
		} catch (UnableToAccessFieldException e) {
			LogHelper.warn("Can't access fields %s from provider %s. Some structures may not be detected");
		}
	}

	private StructureRegistry() {
		ImmutableList.Builder<IStructureGenProvider> builder = ImmutableList.builder();

		builder.add(new IStructureGenProvider() {
			@Override
			//public boolean canUseOnProvider(IChunkProvider provider) {
			public boolean canUseOnProvider(IChunkGenerator provider) {
				return provider instanceof ChunkProviderOverworld;
			}

			@Override
			//public Collection<MapGenStructure> listProviders(IChunkProvider provider) {
			public Collection<MapGenStructure> listProviders(IChunkGenerator provider) {
				ChunkProviderOverworld cp = (ChunkProviderOverworld)provider;
				List<MapGenStructure> result = Lists.newArrayList();
				addMapGen(result, ChunkProviderOverworld.class, cp, "strongholdGenerator", "field_186004_w");
				addMapGen(result, ChunkProviderOverworld.class, cp, "villageGenerator", "field_186005_x");
				addMapGen(result, ChunkProviderOverworld.class, cp, "mineshaftGenerator", "field_186006_y");
				addMapGen(result, ChunkProviderOverworld.class, cp, "scatteredFeatureGenerator", "field_186007_z");
				addMapGen(result, ChunkProviderOverworld.class, cp, "oceanMonumentGenerator", "field_185980_B");
				return result;
			}
		});

		builder.add(new IStructureGenProvider() {
			@Override
			//public boolean canUseOnProvider(IChunkProvider provider) {
			public boolean canUseOnProvider(IChunkGenerator provider) {
				return provider instanceof ChunkProviderFlat;
			}

			@Override
			//public Collection<MapGenStructure> listProviders(IChunkProvider provider) {
			public Collection<MapGenStructure> listProviders(IChunkGenerator provider) {
				ChunkProviderFlat cp = (ChunkProviderFlat)provider;
				List<MapGenStructure> result = Lists.newArrayList();
				try {
					List<MapGenStructure> gen = ReflectionHelper.getPrivateValue(ChunkProviderFlat.class, cp, "structureGenerators", "field_82696_f");
					if (gen != null) result.addAll(gen);
				} catch (UnableToAccessFieldException e) {
					LogHelper.warn("Can't access map gen list from provider %s. Some structures may not be detected");
				}
				return result;
			}
		});

		builder.add(new IStructureGenProvider() {
			@Override
			//public boolean canUseOnProvider(IChunkProvider provider) {
			public boolean canUseOnProvider(IChunkGenerator provider) {
				return provider instanceof ChunkProviderHell;
			}

			@Override
			//public Collection<MapGenStructure> listProviders(IChunkProvider provider) {
			public Collection<MapGenStructure> listProviders(IChunkGenerator provider) {
				ChunkProviderHell cp = (ChunkProviderHell)provider;
				List<MapGenStructure> result = Lists.newArrayList();
				addMapGen(result, ChunkProviderHell.class, cp, "genNetherBridge", "field_73172_c");
				return result;
			}
		});
		providers = builder.build();
	}

	public final static StructureRegistry instance = new StructureRegistry();

	private List<IStructureNamer> names = Lists.newArrayList();

	private List<IStructureGenProvider> providers;

	//private static IChunkProvider getWrappedChunkProvider(ChunkProviderServer provider) {
	private static IChunkGenerator getWrappedChunkProvider(ChunkProviderServer provider) {
		try {
			//return ObfuscationReflectionHelper.getPrivateValue(ChunkProviderServer.class, provider, "chunkGenerator", "field_186029_c");
			return provider.chunkGenerator; // This is public in 1.9.4
		} catch (UnableToAccessFieldException e) {
			LogHelper.error("Can't access chunk provider data. No structures will be detected");
			return null;
		}
	}

	private interface IStructureVisitor {
		public void visit(MapGenStructure structure);
	}

	private void visitStructures(WorldServer world, IStructureVisitor visitor) {
		ChunkProviderServer provider = world.getChunkProvider();
		//IChunkProvider inner = getWrappedChunkProvider(provider);
		IChunkGenerator inner = getWrappedChunkProvider(provider);

		if (inner != null) {
			for (IStructureGenProvider p : providers)
				if (p.canUseOnProvider(inner)) {
					for (MapGenStructure struct : p.listProviders(inner))
						visitor.visit(struct);
				}
		}
	}

	private String identifyStructure(MapGenStructure structure) {
		for (IStructureNamer n : names) {
			String name = n.identify(structure);
			if (!Strings.isNullOrEmpty(name)) return name;
		}
		return structure.getStructureName();
	}

	public Map<String, BlockPos> getNearestStructures(final WorldServer world, final int x, final int y, final int z) {
		final ImmutableMap.Builder<String, BlockPos> result = ImmutableMap.builder();
		visitStructures(world, new IStructureVisitor() {
			@Override
			public void visit(MapGenStructure structure) {
				try {
					BlockPos structPos = structure.getClosestStrongholdPos(world, new BlockPos(x, y, z));//.func_151545_a(world, x, y, z);

					if (structPos != null) {
						String structType = identifyStructure(structure);
						if (!Strings.isNullOrEmpty(structType)) result.put(structType, structPos);
					}
				} catch (IndexOutOfBoundsException e) {
					// bug in MC, just ignore
					// hopefully fixed by magic of ASM
				}
			}
		});

		return result.build();
	}

	public Set<BlockPos> getNearestInstance(final String name, final WorldServer world, final int x, final int y, final int z) {
		final ImmutableSet.Builder<BlockPos> result = ImmutableSet.builder();
		visitStructures(world, new IStructureVisitor() {
			@Override
			public void visit(MapGenStructure structure) {
				String structType = identifyStructure(structure);
				if (name.equals(structType)) {
					try {
						BlockPos structPos = structure.getClosestStrongholdPos(world, new BlockPos(x, y, z));
						if (structPos != null) result.add(structPos);
					} catch (IndexOutOfBoundsException e) {
						// bug in MC, just ignore
						// hopefully fixed by magic of ASM
					}
				}
			}
		});

		return result.build();
	}
}