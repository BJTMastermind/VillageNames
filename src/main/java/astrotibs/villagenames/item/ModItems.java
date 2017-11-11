package astrotibs.villagenames.item;

import astrotibs.villagenames.VillageNames;
import astrotibs.villagenames.reference.Reference;
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
	
	//public static Item villageBook; //I had this in the 1.11.2 version for some reason
	
	public static void init() {
		//villagebook = new ItemVillageBook(); //This was like this when I got here, for 1.11
		
		registerItems();
	}
	
	
	public static void registerItems() {
		
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
