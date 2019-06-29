package astrotibs.villagenames.item;

import astrotibs.villagenames.utility.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Reference.MOD_ID)
/* This ^ is an instance of this object from my mod, and I want this one
 * preserved so that if I ever want to reference it again, I don't need
 * to worry about another mod tinkering with it.
 * 
 * That's a very high-level understanding of what this is. If anyone tries
 * to tinker with my items, they have their version of my items, and I
 * have my own.
 */
public class ModItems {

	public static final ItemCodex CODEX = new ItemCodex();

	public static final ItemVillageBook VILLAGE_BOOK = new ItemVillageBook("villagebook");
	public static final ItemVillageBook MINESHAFT_BOOK = new ItemVillageBook("mineshaftbook");
	
	// V3 temple types
	public static final ItemVillageBook JUNGLE_TEMPLE_BOOK = new ItemVillageBook("jungletemplebook");
	public static final ItemVillageBook DESERT_PYRAMID_BOOK = new ItemVillageBook("desertpyramidbook");
	public static final ItemVillageBook SWAMP_HUT_BOOK = new ItemVillageBook("swamphutbook");
	public static final ItemVillageBook IGLOO_BOOK = new ItemVillageBook("igloobook");
	
	public static final ItemVillageBook TEMPLE_BOOK = new ItemVillageBook("templebook");
	public static final ItemVillageBook STRONGHOLD_BOOK = new ItemVillageBook("strongholdbook");
	
	public static final ItemVillageBook MONUMENT_BOOK = new ItemVillageBook("monumentbook");
	public static final ItemVillageBook MANSION_BOOK = new ItemVillageBook("mansionbook");
	
	public static final ItemVillageBook FORTRESS_BOOK = new ItemVillageBook("fortressbook");
	public static final ItemVillageBook END_CITY_BOOK = new ItemVillageBook("endcitybook");
	
	public static final ItemVillageBook MOON_VILLAGE_BOOK = new ItemVillageBook("moonvillagebook");
	public static final ItemVillageBook KOENTUS_VILLAGE_BOOK = new ItemVillageBook("koentusvillagebook");
	public static final ItemVillageBook FRONOS_VILLAGE_BOOK = new ItemVillageBook("fronosvillagebook");
	public static final ItemVillageBook NIBIRU_VILLAGE_BOOK = new ItemVillageBook("nibiruvillagebook");
	public static final ItemVillageBook ABANDONED_BASE_BOOK = new ItemVillageBook("abandonedbasebook");

	public static void init() {
		GameRegistry.register(CODEX, new ResourceLocation(Reference.MOD_ID, "codex") );
		
		GameRegistry.register(VILLAGE_BOOK, new ResourceLocation(Reference.MOD_ID, "villagebook") );
		GameRegistry.register(MINESHAFT_BOOK, new ResourceLocation(Reference.MOD_ID, "mineshaftbook") );
		
		GameRegistry.register(JUNGLE_TEMPLE_BOOK, new ResourceLocation(Reference.MOD_ID, "jungletemplebook") );
		GameRegistry.register(DESERT_PYRAMID_BOOK, new ResourceLocation(Reference.MOD_ID, "desertpyramidbook") );
		GameRegistry.register(SWAMP_HUT_BOOK, new ResourceLocation(Reference.MOD_ID, "swamphutbook") );
		GameRegistry.register(IGLOO_BOOK, new ResourceLocation(Reference.MOD_ID, "igloobook") );
		
		GameRegistry.register(TEMPLE_BOOK, new ResourceLocation(Reference.MOD_ID, "templebook") );
		GameRegistry.register(STRONGHOLD_BOOK, new ResourceLocation(Reference.MOD_ID, "strongholdbook") );
		
		GameRegistry.register(MONUMENT_BOOK, new ResourceLocation(Reference.MOD_ID, "monumentbook") );
		GameRegistry.register(MANSION_BOOK, new ResourceLocation(Reference.MOD_ID, "mansionbook") );
		
		GameRegistry.register(FORTRESS_BOOK, new ResourceLocation(Reference.MOD_ID, "fortressbook") );
		GameRegistry.register(END_CITY_BOOK, new ResourceLocation(Reference.MOD_ID, "endcitybook") );
		
		GameRegistry.register(MOON_VILLAGE_BOOK, new ResourceLocation(Reference.MOD_ID, "moonvillagebook") );
		GameRegistry.register(KOENTUS_VILLAGE_BOOK, new ResourceLocation(Reference.MOD_ID, "koentusvillagebook") );
		GameRegistry.register(FRONOS_VILLAGE_BOOK, new ResourceLocation(Reference.MOD_ID, "fronosvillagebook") );
		GameRegistry.register(NIBIRU_VILLAGE_BOOK, new ResourceLocation(Reference.MOD_ID, "nibiruvillagebook") );
		GameRegistry.register(ABANDONED_BASE_BOOK, new ResourceLocation(Reference.MOD_ID, "abandonedbasebook") );
	}
}
