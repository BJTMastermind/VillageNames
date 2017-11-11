package astrotibs.villagenames.item;

//import astrotibs.villagenames.utility.LogHelper;
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
//import net.minecraft.item.ItemEditableBook;
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
public class ItemTempleBook extends ItemWrittenBook {
	
	public ItemTempleBook() {
        this.setMaxStackSize(1);
        this.setUnlocalizedName("templebook");
        this.setCreativeTab(CreativeTabs.MISC);
    }
	
    // Not sure if I need the below in 1.8
    /*
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon(this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".") + 1));
    }
    */
    
    
	/*
    public static boolean validBookTagContents(NBTTagCompound nbt) {
    	
        if (!ItemVillageBook.isNBTValid(nbt)) {
            return false;
        }
        else if (!nbt.hasKey("title", 8)) {
            return false;
        }
        else {
        	// This particular branch is fired (and returns TRUE) when you first try to open a freshly-generated Village Book.
        	// This enclosing method is never called once the book is "proper."
            String s = nbt.getString("title");
            return s != null && s.length() <= 32 ? nbt.hasKey("author", 8) : false;
        }
    }
    */

    /**
     * Gets the generation of the book (how many times it has been cloned)
     */
	/*
    public static int getGeneration(ItemStack book) {
        return book.getTagCompound().getInteger("generation");
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
    	// This thing triggers CONSTANTLY because it's render-related.
        if (stack.hasTagCompound()) {
            NBTTagCompound nbttagcompound = stack.getTagCompound();
            String s = nbttagcompound.getString("title");

            if (!StringUtils.isNullOrEmpty(s)) {
                return s;
            }
        }

        return super.getItemStackDisplayName(stack);
    }
	*/
    /**
     * allows items to add custom lines of information to the mouseover description
     */
	@Override
	/*
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
    	// This lights up like a Christmas tree when you try to mouse over the item: no surprise.
        if (stack.hasTagCompound()) {
            NBTTagCompound nbttagcompound = stack.getTagCompound();
            String s = nbttagcompound.getString("author");

            if (!StringUtils.isNullOrEmpty(s)) {
                tooltip.add(TextFormatting.GRAY + I18n.translateToLocalFormatted("book.byAuthor", new Object[] {s}));
            }
            
            tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("book.generation." + nbttagcompound.getInteger("generation")));
        }
    }
	*/
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
    
    
    /**
     * this method returns true if the book's NBT Tag List "pages" is valid
     */
    /*
    public static boolean isNBTValid(NBTTagCompound nbt) {
    	
        if (nbt == null) {
            return false;
        }
        else if (!nbt.hasKey("pages", 9)) {
            return false;
        }
        else {
        	// This particular branch is fired when you first try to open a freshly-generated Village Book.
        	// This enclosing method is never called once the book is "proper."
            NBTTagList nbttaglist = nbt.getTagList("pages", 8);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                String s = nbttaglist.getStringTagAt(i);

                if (s.length() > 32767) {
                    return false;
                }
            }
            return true;
        }
    }
    */
    /*
    private void resolveContents(ItemStack stack, EntityPlayer player) {
        if (stack.getTagCompound() != null) {
            NBTTagCompound nbttagcompound = stack.getTagCompound();

            // This whole batch runs whenever you try to read the book, but there's nothing to do after the first attempt.
            
            if (!nbttagcompound.getBoolean("resolved")) {
                nbttagcompound.setBoolean("resolved", true);
                
                if (validBookTagContents(nbttagcompound)) {
                	
                	// This runs when you first try to open a freshly-generated Village Book
                	
                    NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        String s = nbttaglist.getStringTagAt(i);
                        ITextComponent itextcomponent;

                        try {
                            itextcomponent = ITextComponent.Serializer.fromJsonLenient(s);
                            itextcomponent = TextComponentUtils.processComponent(player, itextcomponent, player);
                        }
                        catch (Exception var9) {
                            itextcomponent = new TextComponentString(s);
                        }

                        nbttaglist.set(i, new NBTTagString(ITextComponent.Serializer.componentToJson(itextcomponent)));
                    }

                    nbttagcompound.setTag("pages", nbttaglist);

                    if (player instanceof EntityPlayerMP && player.getHeldItemMainhand() == stack) {
                    	// This runs when you first try to open a freshly-generated Village Book
                        Slot slot = player.openContainer.getSlotFromInventory(player.inventory, player.inventory.currentItem);
                        ((EntityPlayerMP)player).connection.sendPacket(new SPacketSetSlot(0, slot.slotNumber, stack));
                    }
                }
            }
        }
    }
	*/
    /**
     * Returns true if this item has an enchantment glint. By default, this returns
     * <code>stack.isItemEnchanted()</code>, but other items can override it (for instance, written books always return
     * true).
     *  
     * Note that if you override this method, you generally want to also call the super version (on {@link Item}) to get
     * the glint for enchanted items. Of course, that is unnecessary if the overwritten version always returns true.
     */
    @Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return false;
    }
	
}
