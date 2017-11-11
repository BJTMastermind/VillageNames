package astrotibs.villagenames.item;

import astrotibs.villagenames.reference.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Example copied from Pam's Harvestcraft
public class ItemKoentusVillageBook extends ItemWrittenBook {
	
	
	public ItemKoentusVillageBook() {
		super();
		this.setUnlocalizedName("koentusvillagebook");
		this.setCreativeTab(CreativeTabs.MISC);
		this.setMaxStackSize(1);
	}
	
	@Override
    public String getUnlocalizedName()
    {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }
	
    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }
    
    
    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
	
    @Override
	@SideOnly(Side.CLIENT)
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		
		// I pulled this out of the IF condition above in order to get it to work on the server
		Minecraft.getMinecraft().displayGuiScreen(new GuiScreenBook(playerIn, itemStackIn, false));
		
		//It seems to work without the playerIn. lines but I'm leaving them in anyway...
		playerIn.openBook(itemStackIn, hand);
        playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack p_77636_1_) {
        return false; // Returning "false" stops the book from glowing.
    }
	
	
}