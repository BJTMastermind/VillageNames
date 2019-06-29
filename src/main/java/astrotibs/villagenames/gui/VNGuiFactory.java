package astrotibs.villagenames.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class VNGuiFactory implements IModGuiFactory 
{

	@Override
	public void initialize(Minecraft minecraftInstance) { //Called when instantiated to initialize with the active Minecraft instance
    }
	
	@Override
	public boolean hasConfigGui() {
		return true;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen) {
		return new VNGuiConfig(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

}