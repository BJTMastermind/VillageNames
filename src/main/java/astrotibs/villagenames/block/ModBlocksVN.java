package astrotibs.villagenames.block;

import astrotibs.villagenames.VillageNames;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModBlocksVN {
	//I think the below are just internal registry names
	public static final Block LUNARIN_GOLD_BRICK = new BlockLunarinGold("lunaringoldbrick");
	public static final Block LUNARIN_IRON_BRICK = new BlockLunarinIron("lunarinironbrick");
	
	public static void init() {
		
		registerBlock(LUNARIN_GOLD_BRICK, "lunaringoldbrick", CreativeTabs.BUILDING_BLOCKS, true);
        registerBlock(LUNARIN_IRON_BRICK, "lunarinironbrick", CreativeTabs.BUILDING_BLOCKS, true);
		
    }
	
    public static Block registerBlock(Block block, String blockName, CreativeTabs tab, boolean registerItemModels)
    {
        block.setUnlocalizedName( Reference.MOD_ID.toLowerCase() + ":"+ blockName);        
        block.setCreativeTab(tab);
        
        // for vanilla blocks, just register a single variant with meta=0 and assume ItemBlock for the item class
        registerBlockWithItem(block, blockName, ItemBlock.class);
        registerBlockItemModel(block, blockName, 0);

        return block;
    }
	
    
    private static void registerBlockWithItem(Block block, String blockName, Class<? extends ItemBlock> clazz)
    {
        try {
            Item itemBlock = clazz != null ? (Item)clazz.getConstructor(Block.class).newInstance(block) : null;
            ResourceLocation location = new ResourceLocation(Reference.MOD_ID, blockName);

            block.setRegistryName(new ResourceLocation(Reference.MOD_ID, blockName));

            ForgeRegistries.BLOCKS.register(block);
            if (itemBlock != null)
            {
                itemBlock.setRegistryName(new ResourceLocation(Reference.MOD_ID, blockName));
                ForgeRegistries.ITEMS.register(itemBlock);
            }
        }
        catch (Exception e) {
            throw new RuntimeException("An error occurred associating an item block during registration of " + blockName, e);
        }
    }
	
    public static void registerBlockItemModel(Block block, String stateName, int stateMeta)
    {
        Item item = Item.getItemFromBlock(block);
        VillageNames.PROXY.registerItemVariantModel(item, stateName, stateMeta);
        
    }
    
	public static Item registerItem(Item item, String name, CreativeTabs tab) {
		// Commented out until I figure out how to register items again --AstroTibs
		item.setUnlocalizedName(name);
		if (tab != null) {
			item.setCreativeTab(tab);
		}
		
		item.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
		ForgeRegistries.ITEMS.register(item);
		VillageNames.PROXY.registerItemSided(item);
		
		return item;
	}
    
}
