package astrotibs.villagenames.item;

import astrotibs.villagenames.reference.Reference;
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
	
	public static void init() {
		GameRegistry.registerItem(villagebook, "villagebook");
		GameRegistry.registerItem(fortressbook, "fortressbook");
		GameRegistry.registerItem(mineshaftbook, "mineshaftbook");
		GameRegistry.registerItem(mansionbook, "mansionbook");
		GameRegistry.registerItem(monumentbook, "monumentbook");
		GameRegistry.registerItem(strongholdbook, "strongholdbook");
		GameRegistry.registerItem(templebook, "templebook");
		GameRegistry.registerItem(endcitybook, "endcitybook");
		GameRegistry.registerItem(codex, "codex");
		GameRegistry.registerItem(moonvillagebook, "moonvillagebook");
		GameRegistry.registerItem(koentusvillagebook, "koentusvillagebook");
		GameRegistry.registerItem(fronosvillagebook, "fronosvillagebook");
		GameRegistry.registerItem(nibiruvillagebook, "nibiruvillagebook");
		GameRegistry.registerItem(abandonedbasebook, "abandonedbasebook");
	}
}
