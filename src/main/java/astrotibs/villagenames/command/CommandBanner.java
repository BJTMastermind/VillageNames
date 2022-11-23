package astrotibs.villagenames.command;

import java.util.ArrayList;
import java.util.List;

import astrotibs.villagenames.banner.BannerGenerator;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandBanner extends CommandBase {

	@Override
	public String getName() {
		return "vn_banner";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "�c/"+getName()+" <forced base color OR \"village\"> (optional)";
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
		
		if (!sender.getEntityWorld().isRemote && sender.getCommandSenderEntity() instanceof EntityPlayer) {
			
			EntityPlayer entity = (EntityPlayer)sender.getCommandSenderEntity();
			
			String[] arguments = new String[]{"", ""};
			
			if (args.length==1)
			{
				arguments[0] = args[0];
			}
			else if (args.length==2)
			{
				arguments[0] = args[0];
				arguments[1] = args[1];
			}
			else if (args.length > 2)
			{
				sender.sendMessage( new TextComponentString(getUsage(null)) );
				return;
			}
			
			// Player wants to retrieve the banner for the current village
			if (arguments[0].toLowerCase().equals("village") && sender.getCommandSenderEntity() != null)
			{
				Object[] villageBannerData = BannerGenerator.getVillageBannerData(entity);
				NBTTagCompound bannerNBT = new NBTTagCompound();
				String villageNameForBanner = "";
				if (villageBannerData!=null) {
					bannerNBT = (NBTTagCompound) villageBannerData[0];
					villageNameForBanner = (String) villageBannerData[1];}
				
				if (!(villageNameForBanner.equals("")))
				{
					// Successfully generate a village banner and give it to the player
					EntityItem eitem = entity.entityDropItem(BannerGenerator.makeBanner(bannerNBT, villageNameForBanner), 1);
			        eitem.setNoPickupDelay(); // No delay: directly into the inventory!
			        return;
				}
				else
				{
					sender.sendMessage( new TextComponentString("Not inside a village.") );
					return;
				}
			}
			
			for (int i=0; i<arguments.length; i++)
			{
				// Player wants to create a random banner with a specific base color
				if (arguments[i].equals("-1"))
				{
					sender.sendMessage( new TextComponentString("Unknown color: -1") );
					return;
				} 
				else if (arguments[i].toLowerCase().equals("black")) {arguments[i] = "0";}
				else if (arguments[i].toLowerCase().equals("red")) {arguments[i] = "1";}
				else if (arguments[i].toLowerCase().equals("green")) {arguments[i] = "2";}
				else if (arguments[i].toLowerCase().equals("brown")) {arguments[i] = "3";}
				else if (arguments[i].toLowerCase().equals("blue")) {arguments[i] = "4";}
				else if (arguments[i].toLowerCase().equals("purple")) {arguments[i] = "5";}
				else if (arguments[i].toLowerCase().equals("cyan")) {arguments[i] = "6";}
				else if (arguments[i].toLowerCase().equals("light_gray")
						||arguments[i].toLowerCase().equals("lightgray")
						||arguments[i].toLowerCase().equals("silver")) {arguments[i] = "7";}
				else if (arguments[i].toLowerCase().equals("gray")) {arguments[i] = "8";}
				else if (arguments[i].toLowerCase().equals("pink")) {arguments[i] = "9";}
				else if (arguments[i].toLowerCase().equals("lime")) {arguments[i] = "10";}
				else if (arguments[i].toLowerCase().equals("yellow")) {arguments[i] = "11";}
				else if (arguments[i].toLowerCase().equals("light_blue")
						||arguments[i].toLowerCase().equals("lightblue")) {arguments[i] = "12";}
				else if (arguments[i].toLowerCase().equals("magenta")) {arguments[i] = "13";}
				else if (arguments[i].toLowerCase().equals("orange")) {arguments[i] = "14";}
				else if (arguments[i].toLowerCase().equals("white")) {arguments[i] = "15";}
			}
			
			// Convert number argument to an actual integer
			int forcedmeta = -1;
			int forcedmeta2 = -1;
			
			try {forcedmeta = arguments[0].equals("") ? -1 : Integer.parseInt(arguments[0]);}
			catch (Exception e)
			{
				sender.sendMessage( new TextComponentString("Unknown color: " + arguments[0]) );
				return;
			}
			try {forcedmeta2 = arguments[1].equals("") ? -1 : Integer.parseInt(arguments[1]);}
			catch (Exception e)
			{
				sender.sendMessage( new TextComponentString("Unknown color: " + arguments[1]) );
				return;
			}
			
			// User submitted a non-color meta number
			if (forcedmeta > 15 || forcedmeta < -1)
			{
				sender.sendMessage( new TextComponentString("Unknown color: " + forcedmeta) );
				return;
			}
			// User submitted a non-color meta number
			if (forcedmeta2 > 15 || forcedmeta2 < -1)
			{
				sender.sendMessage( new TextComponentString("Unknown color: " + forcedmeta2) );
				return;
			}
			
			// Generate a random banner, perhaps using argument as a forced base color
			if (sender.getCommandSenderEntity() != null && sender.getCommandSenderEntity() instanceof EntityPlayer)
			{
				Object[] newRandomBanner = BannerGenerator.randomBannerArrays(entity.world.rand, forcedmeta, forcedmeta2);
				ArrayList<String> patternArray = (ArrayList<String>) newRandomBanner[0];
				ArrayList<Integer> colorArray = (ArrayList<Integer>) newRandomBanner[1];
				
				ItemStack villagebanner = BannerGenerator.makeBanner(patternArray, colorArray);
				
				// At this stage, you probably have a banner. Give it to the command sender.
				if (villagebanner != null)
				{
					EntityItem eitem = entity.entityDropItem(villagebanner, 1);
			        eitem.setNoPickupDelay(); // No delay: directly into the inventory!
				}
			}
			
		}
		
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	/**
     * Adds the strings available in this command to the given list of tab completion options.
     */
	@SuppressWarnings("unchecked")
	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] strings, BlockPos pos)
    {
        if (strings.length == 1) {
        	return getListOfStringsMatchingLastWord(strings, new String[]{
        			"village",
        			"black",
        			"red",
        			"green",
        			"brown",
        			"blue",
        			"purple",
        			"cyan",
        			"light_gray",
        			"gray",
        			"pink",
        			"lime",
        			"yellow",
        			"light_blue",
        			"magenta",
        			"orange",
        			"white"
        			});
        }
        else {
        	return new ArrayList<String>();
        }
    }

}
