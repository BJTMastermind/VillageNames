package astrotibs.villagenames.village.chestloot;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.WeightedRandom;

/*
 * Adapted from 1.8's net.minecraft.util.WeightedRandomChestContent
 */
public class WeightedRandomChestContent extends WeightedRandom.Item
{
    public ItemStack theItemId; // The Item/Block ID to generate in the Chest 
    public int minStackSize; // The minimum stack size of generated item
    public int maxStackSize; // The maximum stack size of generated item

    public WeightedRandomChestContent(Item item, int meta, int minimumChance, int maximumChance, int itemWeightIn)
    {
        super(itemWeightIn);
        this.theItemId = new ItemStack(item, 1, meta);
        this.minStackSize = minimumChance;
        this.maxStackSize = maximumChance;
    }

    public WeightedRandomChestContent(ItemStack stack, int minimumChance, int maximumChance, int itemWeightIn)
    {
        super(itemWeightIn);
        this.theItemId = stack;
        this.minStackSize = minimumChance;
        this.maxStackSize = maximumChance;
    }
    
    public static void generateChestContents(Random random, List<WeightedRandomChestContent> listIn, IInventory inv, int max)
    {
        for (int i = 0; i < max; ++i)
        {
            WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(random, listIn);
            ItemStack[] stacks = weightedrandomchestcontent.generateChestContent(random, inv);

            for (ItemStack itemstack1 : stacks)
            {
                inv.setInventorySlotContents(random.nextInt(inv.getSizeInventory()), itemstack1);
            }
        }
    }
    
    /**
     * Allow a mod to submit a custom implementation that can delegate item stack generation beyond simple stack lookup
     *
     * @param random The current random for generation
     * @param newInventory The inventory being generated (do not populate it, but you can refer to it)
     * @return An array of {@link ItemStack} to put into the chest
     */
    protected ItemStack[] generateChestContent(Random random, IInventory newInventory)
    {
        return ChestGenHooks.generateStacks(random, theItemId, minStackSize, maxStackSize);
    }
    
    public static void generateDispenserContents(Random random, List<WeightedRandomChestContent> listIn, TileEntityDispenser dispenser, int max)
    {
        for (int i = 0; i < max; ++i)
        {
            WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(random, listIn);
            ItemStack[] stacks = weightedrandomchestcontent.generateChestContent(random, dispenser);

            for (ItemStack itemstack1 : stacks)
            {
                dispenser.setInventorySlotContents(random.nextInt(dispenser.getSizeInventory()), itemstack1);
            }
        }
    }
    
    public static List<WeightedRandomChestContent> func_177629_a(List<WeightedRandomChestContent> p_177629_0_, WeightedRandomChestContent... p_177629_1_)
    {
        List<WeightedRandomChestContent> list = Lists.newArrayList(p_177629_0_);
        Collections.addAll(list, p_177629_1_);
        return list;
    }
}
