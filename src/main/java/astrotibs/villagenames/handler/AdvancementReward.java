package astrotibs.villagenames.handler;

import java.util.Random;

import astrotibs.villagenames.utility.Reference;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

/**
 * This method rewards the player for completing all five advancements.
 * It will be called each time an advancement is triggered.
 * @author AstroTibs
 */

public class AdvancementReward {
	
	public static void allFiveAdvancements(EntityPlayerMP playerMP) {
		
		if (
				playerMP.getServer().getPlayerList().getPlayerAdvancements(playerMP).getProgress(
						playerMP.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(Reference.MOD_ID+":archaeologist"))
						).isDone()
				&& playerMP.getServer().getPlayerList().getPlayerAdvancements(playerMP).getProgress(
						playerMP.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(Reference.MOD_ID+":ghosttown"))
						).isDone()
				&& playerMP.getServer().getPlayerList().getPlayerAdvancements(playerMP).getProgress(
						playerMP.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(Reference.MOD_ID+":laputa"))
						).isDone()
				&& playerMP.getServer().getPlayerList().getPlayerAdvancements(playerMP).getProgress(
						playerMP.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(Reference.MOD_ID+":maxrep"))
						).isDone()
				&& playerMP.getServer().getPlayerList().getPlayerAdvancements(playerMP).getProgress(
						playerMP.getServer().getAdvancementManager().getAdvancement(new ResourceLocation(Reference.MOD_ID+":minrep"))
						).isDone()
				) {
			
			// All five are done
			
			// Send a congratulation message as two parts
			String[] congratsStart = new String[]{
					"Nicely done!",
					"Congratulations!",
					"Thanks for using "+Reference.MOD_NAME+"!",
					"I'm glad you enjoy "+Reference.MOD_NAME+"!",
					"Great job!"
				};
			String[] congratsEnd = new String[]{
					"Here's a little something on me.",
					"Here's something as thanks.",
					"Take this as a memento.",
					"Have a keepsake.",
					"Here's a souvenir."
				};
			
			
			//  advancement grant AstroTibs only villagenames:ghosttown
			// advancement revoke AstroTibs only villagenames:ghosttown
			
			playerMP.sendMessage(new TextComponentString(""));
			playerMP.sendMessage(new TextComponentString(
					TextFormatting.GOLD + 
					congratsStart[new Random().nextInt(congratsStart.length)]+" "+congratsEnd[new Random().nextInt(congratsEnd.length)]
							+TextFormatting.RESET
					) );
			playerMP.sendMessage(new TextComponentString(
					TextFormatting.GOLD + 
					"It's a replica of a sword I used while testing "+Reference.MOD_NAME+" Version 3."
					+TextFormatting.RESET+" --AstroTibs"
					) );
			
			ItemStack uninstantiator = new ItemStack(Items.GOLDEN_SWORD, 1);
			uninstantiator.addEnchantment(Enchantment.getEnchantmentByLocation("unbreaking"), 3);
			uninstantiator.addEnchantment(Enchantment.getEnchantmentByLocation("sharpness"), 5);
			//uninstantiator.addEnchantment(Enchantment.getEnchantmentByLocation("looting"), 3);
			uninstantiator.setStackDisplayName("Un-Instantiator");
			
			EntityItem eitem = playerMP.entityDropItem(uninstantiator, 1);
	        eitem.setNoPickupDelay(); //No delay: directly into the inventory!
			
		}
		
	}
	
}