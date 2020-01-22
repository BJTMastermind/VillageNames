package astrotibs.villagenames.item;

import astrotibs.villagenames.utility.Reference;
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
	
	public static final ItemCodex codex = new ItemCodex();
	
	public static final ItemVillageBook villagebook = new ItemVillageBook("villagebook");
	public static final ItemVillageBook mineshaftbook = new ItemVillageBook("mineshaftbook");

	// V3 temple types
	public static final ItemVillageBook jungletemplebook = new ItemVillageBook("jungletemplebook");
	public static final ItemVillageBook desertpyramidbook = new ItemVillageBook("desertpyramidbook");
	public static final ItemVillageBook swamphutbook = new ItemVillageBook("swamphutbook");
	public static final ItemVillageBook igloobook = new ItemVillageBook("igloobook");
	
	public static final ItemVillageBook templebook = new ItemVillageBook("templebook");
	public static final ItemVillageBook strongholdbook = new ItemVillageBook("strongholdbook");
	public static final ItemVillageBook fortressbook = new ItemVillageBook("fortressbook");
	public static final ItemVillageBook monumentbook = new ItemVillageBook("monumentbook");
	public static final ItemVillageBook endcitybook = new ItemVillageBook("endcitybook");
	public static final ItemVillageBook mansionbook = new ItemVillageBook("mansionbook");
	
	// Mod books
	public static final ItemVillageBook moonvillagebook = new ItemVillageBook("moonvillagebook");
	public static final ItemVillageBook koentusvillagebook = new ItemVillageBook("koentusvillagebook");
	public static final ItemVillageBook fronosvillagebook = new ItemVillageBook("fronosvillagebook");
	public static final ItemVillageBook nibiruvillagebook = new ItemVillageBook("nibiruvillagebook");
	public static final ItemVillageBook abandonedbasebook = new ItemVillageBook("abandonedbasebook");
	
	/*// Originally, I had registered these as individual items
	public static final ItemVillageBook villagebook = new ItemVillageBook();
	public static final ItemFortressBook fortressbook = new ItemFortressBook();
	public static final ItemMineshaftBook mineshaftbook = new ItemMineshaftBook();
	public static final ItemMansionBook mansionbook = new ItemMansionBook();
	public static final ItemMonumentBook monumentbook = new ItemMonumentBook();
	public static final ItemStrongholdBook strongholdbook = new ItemStrongholdBook();
	public static final ItemTempleBook templebook = new ItemTempleBook();
	public static final ItemEndCityBook endcitybook = new ItemEndCityBook();
	public static final ItemCodex codex = new ItemCodex();
	public static final ItemMoonVillageBook moonvillagebook = new ItemMoonVillageBook();
	public static final ItemKoentusVillageBook koentusvillagebook = new ItemKoentusVillageBook();
	public static final ItemFronosVillageBook fronosvillagebook = new ItemFronosVillageBook();
	public static final ItemNibiruVillageBook nibiruvillagebook = new ItemNibiruVillageBook();
	public static final ItemAbandonedBaseBook abandonedbasebook = new ItemAbandonedBaseBook();
	*/
	public static void init() {
		GameRegistry.registerItem(codex, "codex");
		
		GameRegistry.registerItem(villagebook, "villagebook");
		GameRegistry.registerItem(mineshaftbook, "mineshaftbook");
		
		GameRegistry.registerItem(jungletemplebook, "jungletemplebook");
		GameRegistry.registerItem(desertpyramidbook, "desertpyramidbook");
		GameRegistry.registerItem(swamphutbook, "swamphutbook");
		GameRegistry.registerItem(igloobook, "igloobook");
		
		GameRegistry.registerItem(templebook, "templebook");
		GameRegistry.registerItem(strongholdbook, "strongholdbook");
		
		GameRegistry.registerItem(monumentbook, "monumentbook");
		GameRegistry.registerItem(mansionbook, "mansionbook");
				
		GameRegistry.registerItem(fortressbook, "fortressbook");
		GameRegistry.registerItem(endcitybook, "endcitybook");
		
		// Mod books
		GameRegistry.registerItem(moonvillagebook, "moonvillagebook");
		GameRegistry.registerItem(koentusvillagebook, "koentusvillagebook");
		GameRegistry.registerItem(fronosvillagebook, "fronosvillagebook");
		GameRegistry.registerItem(nibiruvillagebook, "nibiruvillagebook");
		GameRegistry.registerItem(abandonedbasebook, "abandonedbasebook");
	}
}
