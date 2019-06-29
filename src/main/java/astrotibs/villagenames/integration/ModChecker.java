package astrotibs.villagenames.integration;

import astrotibs.villagenames.utility.LogHelper;
import net.minecraftforge.fml.common.Loader;

public class ModChecker {
	
	public static void printDetectedMods() {
		if( Loader.isModLoaded("witchery") ) LogHelper.info("Detected mod: Witchery");
		//if(isGalacticraftLoaded) LogHelper.info("Detected mod: Galacticraft");
		if( Loader.isModLoaded("moremlanet") ) LogHelper.info("Detected mod: More Planets");
		if( Loader.isModLoaded("moremlanets") ) LogHelper.info("Detected mod: More Planets");
		//if(isHardcoreEnderExpansionLoaded) LogHelper.info("Detected mod: Hardcore Ender Expansion");
		if( Loader.isModLoaded("primitivemobs") ) LogHelper.info("Detected mod: Primitive Mobs");
		//if(isATGLoaded) LogHelper.info("Detected mod: Alternate Terrain Generation");
		//if(isGanysSurfaceLoaded) LogHelper.info("Detected mod: Gany's Surface");
		//if(isGanysNetherLoaded) LogHelper.info("Detected mod: Gany's Nether");
		//if(isRoguelikeDungeonsLoaded) LogHelper.info("Detected mod: Roguelike Dungeons");
		//if(isFossilsAndArchaeologyLoaded) LogHelper.info("Detected mod: Fossils and Archeology Revival");
		//if(isTwilightForestLoaded) LogHelper.info("Detected mod: The Twilight Forest");
		if( Loader.isModLoaded("artifacts") ) LogHelper.info("Detected mod: Artifacts");
		if( Loader.isModLoaded("mariculture") ) LogHelper.info("Detected mod: Mariculture");
		if( Loader.isModLoaded("toroquest") ) LogHelper.info("Detected mod: ToroQuest");
		if( Loader.isModLoaded("etfuturum") ) LogHelper.info("Detected mod: Et Futurum");
		if( Loader.isModLoaded("ganyssurface") ) LogHelper.info("Detected mod: Gany's Surface");
		//if(isGanysNetherLoaded) LogHelper.info("Detected mod: Gany's Nether");
		if( Loader.isModLoaded("chisel") ) LogHelper.info("Detected mod: Chisel");
		if( Loader.isModLoaded("biomesoplenty") ) LogHelper.info("Detected mod: Biomes O' Plenty");
		if( Loader.isModLoaded("bettervanilla") ) LogHelper.info("Detected mod: BetterVanilla");
		if( Loader.isModLoaded("carpentersblocks") ) LogHelper.info("Detected mod: Carpenter's Blocks");
		if( Loader.isModLoaded("harvestcraft") ) LogHelper.info("Detected mod: Pam's HarvestCraft");
		if( Loader.isModLoaded("botania") ) LogHelper.info("Detected mod: Botania");
		//if( Loader.isModLoaded("SpecialMobs") ) LogHelper.info("Detected mod: Special Mobs");
		/*
		if(isOptifineLoaded) {
			LogHelper.info("Detected mod: Optifine");
			LogHelper.warn("WARNING: a bug in OptiFine causes errors for local games. Sometimes, Codex-generated names don't save.");
		}
		*/
	}
	
}