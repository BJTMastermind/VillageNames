package astrotibs.villagenames.config.pieces;

import java.io.File;

import astrotibs.villagenames.name.NamePiecesEntities;
import astrotibs.villagenames.utility.Reference;
import net.minecraftforge.common.config.Configuration;

public class VillagerConfigHandler
{
	public static Configuration config;
	
	
	public static String[] villager_prefix;
	public static String[] villager_root_initial;
	public static String[] villager_root_syllables;
	public static String[] villager_root_terminal;
	public static String[] villager_suffix;

	public static float prefix_chance;
	public static float suffix_chance;
	public static int[] syllable_count_weighting; 
	
	
	public static void init(File configFile)
	{
		if (config == null)
		{
			config = new Configuration(configFile);
			loadConfiguration();
		}
	}
	
	public static void loadConfiguration()
	{
		villager_prefix = config.getString("Prefixes", Reference.CATEGORY_VILLAGER_SYLLABLE_POOL, NamePiecesEntities.villager_prefix_default,
				"Prefixes that can occur before the core name.").trim().split("\\s*,\\s*");
		
		villager_root_initial = config.getString("Root: Initial", Reference.CATEGORY_VILLAGER_SYLLABLE_POOL, NamePiecesEntities.villager_root_initial_default,
				"Core names begin with one of these half-syllables. Use _ to represent a space.").trim().split("\\s*,\\s*");
		
		villager_root_syllables = config.getString("Root: Syllables", Reference.CATEGORY_VILLAGER_SYLLABLE_POOL, NamePiecesEntities.villager_root_sylBegin_default,
				"Core names insert zero or more of these elements to build to their target lengths. Use _ to represent a space, and ^ for a blank entry.").trim().split("\\s*,\\s*");
		
		villager_root_terminal = config.getString("Root: Terminal", Reference.CATEGORY_VILLAGER_SYLLABLE_POOL, NamePiecesEntities.villager_root_terminal_default,
				"Core names end with one of these half-syllables. Use _ to represent a space, and ^ for a blank entry.").trim().split("\\s*,\\s*");
		
		villager_suffix = config.getString("Suffixes", Reference.CATEGORY_VILLAGER_SYLLABLE_POOL, NamePiecesEntities.villager_suffix_default,
				"Suffixes that can occur after the core name.").trim().split("\\s*,\\s*");

		
		
		syllable_count_weighting = config.get(Reference.CATEGORY_VILLAGER_SYLLABLE_POOL, "Syllable Count Weighting", NamePiecesEntities.villager_syllable_count_weights,
				"How often core names of various lengths are generated. The number in the Nth row is the weighting for N-syllable names.").getIntList();
		
		prefix_chance = config.getFloat("Prefix Chance", Reference.CATEGORY_VILLAGER_SYLLABLE_POOL,
				(villager_root_initial.length-1) <= 0 ? 0 : ((float)villager_prefix.length-1)/(villager_root_initial.length-1), 0.0F, 1.0F,
				"The fraction of names that include a prefix.");
		
		suffix_chance = config.getFloat("Suffix Chance", Reference.CATEGORY_VILLAGER_SYLLABLE_POOL,
				(villager_root_initial.length-1) <= 0 ? 0 : ((float)villager_suffix.length-1)/(villager_root_initial.length-1), 0.0F, 1.0F,
				"The fraction of names that include a suffix.");
		
		
		
		if (config.hasChanged()) config.save();
	}
	
}
