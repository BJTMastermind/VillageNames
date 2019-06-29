package astrotibs.villagenames.item;

import astrotibs.villagenames.VillageNames;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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
	
	//public static Item villageBook; //I had this in the 1.11.2 version for some reason
	
	public static void init() {
		//villagebook = new ItemVillageBook(); //This was like this when I got here, for 1.11
		
		registerItems();
	}
	
	
	public static void registerItems() {
		
		Item codex = registerItem(CODEX, "codex", CreativeTabs.MISC);
		
		Item villagebook = registerItem(VILLAGE_BOOK, "villagebook", CreativeTabs.MISC);
		Item mineshaftbook = registerItem(MINESHAFT_BOOK, "mineshaftbook", CreativeTabs.MISC);
		
		Item jungletemplebook = registerItem(JUNGLE_TEMPLE_BOOK, "jungletemplebook", CreativeTabs.MISC);
		Item desertpyramidbook = registerItem(DESERT_PYRAMID_BOOK, "desertpyramidbook", CreativeTabs.MISC);
		Item swamphutbook = registerItem(SWAMP_HUT_BOOK, "swamphutbook", CreativeTabs.MISC);
		Item igloobook = registerItem(IGLOO_BOOK, "igloobook", CreativeTabs.MISC);
		
		Item templebook = registerItem(TEMPLE_BOOK, "templebook", CreativeTabs.MISC);
		Item strongholdbook = registerItem(STRONGHOLD_BOOK, "strongholdbook", CreativeTabs.MISC);
		
		Item monumentbook = registerItem(MONUMENT_BOOK, "monumentbook", CreativeTabs.MISC);
		Item mansionbook = registerItem(MANSION_BOOK, "mansionbook", CreativeTabs.MISC);
		
		Item fortressbook = registerItem(FORTRESS_BOOK, "fortressbook", CreativeTabs.MISC);
		Item endcitybook = registerItem(END_CITY_BOOK, "endcitybook", CreativeTabs.MISC);
		
		Item moonvillagebook = registerItem(MOON_VILLAGE_BOOK, "moonvillagebook", CreativeTabs.MISC);
		Item koentusvillagebook = registerItem(KOENTUS_VILLAGE_BOOK, "koentusvillagebook", CreativeTabs.MISC);
		Item fronosvillagebook = registerItem(FRONOS_VILLAGE_BOOK, "fronosvillagebook", CreativeTabs.MISC);
		Item nibiruvillagebook = registerItem(NIBIRU_VILLAGE_BOOK, "nibiruvillagebook", CreativeTabs.MISC);
		Item abandonedbasebook = registerItem(ABANDONED_BASE_BOOK, "abandonedbasebook", CreativeTabs.MISC);
		/*
		Item codex = registerItem(new ItemCodex(), "codex", CreativeTabs.MISC);
		
		Item villagebook = registerItem(new ItemVillageBook("villagebook"), "villagebook", CreativeTabs.MISC);
		Item mineshaftbook = registerItem(new ItemVillageBook("mineshaftbook"), "mineshaftbook", CreativeTabs.MISC);
		
		Item jungletemplebook = registerItem(new ItemVillageBook("jungletemplebook"), "jungletemplebook", CreativeTabs.MISC);
		Item desertpyramidbook = registerItem(new ItemVillageBook("desertpyramidbook"), "desertpyramidbook", CreativeTabs.MISC);
		Item swamphutbook = registerItem(new ItemVillageBook("swamphutbook"), "swamphutbook", CreativeTabs.MISC);
		Item igloobook = registerItem(new ItemVillageBook("igloobook"), "igloobook", CreativeTabs.MISC);
		
		Item templebook = registerItem(new ItemVillageBook("templebook"), "templebook", CreativeTabs.MISC);
		Item strongholdbook = registerItem(new ItemVillageBook("strongholdbook"), "strongholdbook", CreativeTabs.MISC);
		
		Item monumentbook = registerItem(new ItemVillageBook("monumentbook"), "monumentbook", CreativeTabs.MISC);
		Item mansionbook = registerItem(new ItemVillageBook("mansionbook"), "mansionbook", CreativeTabs.MISC);
		
		Item fortressbook = registerItem(new ItemVillageBook("fortressbook"), "fortressbook", CreativeTabs.MISC);
		Item endcitybook = registerItem(new ItemVillageBook("endcitybook"), "endcitybook", CreativeTabs.MISC);
		
		Item moonvillagebook = registerItem(new ItemVillageBook("moonvillagebook"), "moonvillagebook", CreativeTabs.MISC);
		Item koentusvillagebook = registerItem(new ItemVillageBook("koentusvillagebook"), "koentusvillagebook", CreativeTabs.MISC);
		Item fronosvillagebook = registerItem(new ItemVillageBook("fronosvillagebook"), "fronosvillagebook", CreativeTabs.MISC);
		Item nibiruvillagebook = registerItem(new ItemVillageBook("nibiruvillagebook"), "nibiruvillagebook", CreativeTabs.MISC);
		Item abandonedbasebook = registerItem(new ItemVillageBook("abandonedbasebook"), "abandonedbasebook", CreativeTabs.MISC);
		*/
		/*
		Item villagebook = registerItem(new ItemVillageBook(), "villagebook", CreativeTabs.MISC);
		Item fortressbook = registerItem(new ItemFortressBook(), "fortressbook", CreativeTabs.MISC);
		Item mineshaftbook = registerItem(new ItemMineshaftBook(), "mineshaftbook", CreativeTabs.MISC);
		Item mansionbook = registerItem(new ItemMansionBook(), "mansionbook", CreativeTabs.MISC);
		Item monumentbook = registerItem(new ItemMonumentBook(), "monumentbook", CreativeTabs.MISC);
		Item strongholdbook = registerItem(new ItemStrongholdBook(), "strongholdbook", CreativeTabs.MISC);
		Item templebook = registerItem(new ItemTempleBook(), "templebook", CreativeTabs.MISC);
		Item endcitybook = registerItem(new ItemEndCityBook(), "endcitybook", CreativeTabs.MISC);
		Item codex = registerItem(new ItemCodex(), "codex", CreativeTabs.MISC);
		Item moonvillagebook = registerItem(new ItemMoonVillageBook(), "moonvillagebook", CreativeTabs.MISC);
		Item koentusvillagebook = registerItem(new ItemKoentusVillageBook(), "koentusvillagebook", CreativeTabs.MISC);
		Item fronosvillagebook = registerItem(new ItemFronosVillageBook(), "fronosvillagebook", CreativeTabs.MISC);
		Item nibiruvillagebook = registerItem(new ItemNibiruVillageBook(), "nibiruvillagebook", CreativeTabs.MISC);
		Item abandonedbasebook = registerItem(new ItemAbandonedBaseBook(), "abandonedbasebook", CreativeTabs.MISC);
		*/
	}
	

	public static Item registerItem(Item item, String name, CreativeTabs tab) {
		// Commented out until I figure out how to register items again --AstroTibs
		item.setUnlocalizedName(Reference.MOD_ID.toLowerCase() + ":"+ name);
		if (tab != null) {
			item.setCreativeTab(tab);
		}
		
		item.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
		ForgeRegistries.ITEMS.register(item);
		VillageNames.PROXY.registerItemSided(item);
		
		return item;
	}
	
}
