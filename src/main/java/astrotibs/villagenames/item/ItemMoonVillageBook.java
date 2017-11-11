package astrotibs.villagenames.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Example copied from Pam's Harvestcraft
public class ItemMoonVillageBook extends ItemWrittenBook {
	
	
	public ItemMoonVillageBook() {
		super();
		this.setUnlocalizedName("moonvillagebook");
		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxStackSize(1);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        /*
        if (!worldIn.isRemote) {
        	// This runs EVERY TIME you try to read a village book
            this.resolveContents(itemstack, playerIn);
        }
        */
        // I pulled this out of the IF condition above in order to get it to work on the server
        Minecraft.getMinecraft().displayGuiScreen(new GuiScreenBook(playerIn, itemstack, false));
        
        // This runs EVERY TIME you try to read a village book
        playerIn.openBook(itemstack, handIn);
        playerIn.addStat(StatList.getObjectUseStats(this));
        //.FAIL does not let the book do anything.
        //.PASS only works on the FIRST attempt to open a fresh Village book.
        //.SUCCESS works anytime.
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack p_77636_1_) {
        return false; // Returning "false" stops the book from glowing.
    }
	
	
}