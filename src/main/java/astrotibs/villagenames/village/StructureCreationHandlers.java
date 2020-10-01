package astrotibs.villagenames.village;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.village.StructureVillageVN.StartVN;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertAnimalPen1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertAnimalPen2;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertArmorer1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertButcherShop1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertCartographerHouse1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertFarm1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertFarm2;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertFisher1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertFletcherHouse1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertLargeFarm1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertLibrary1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertMason1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertMediumHouse1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertMediumHouse2;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertShepherdHouse1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertSmallHouse1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertSmallHouse2;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertSmallHouse3;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertSmallHouse4;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertSmallHouse5;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertSmallHouse6;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertSmallHouse7;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertSmallHouse8;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertStreetDecor1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertStreetSubstitute1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertStreetSubstitute2;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertStreetSubstitute3;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertTannery1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertTemple1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertTemple2;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertToolSmith1;
import astrotibs.villagenames.village.biomestructures.DesertStructures.DesertWeaponsmith1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsAccessory1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsAnimalPen1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsAnimalPen2;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsAnimalPen3;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsArmorerHouse1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsBigHouse1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsButcherShop1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsButcherShop2;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsCartographer1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsFisherCottage1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsFletcherHouse1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsLargeFarm1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsLibrary1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsLibrary2;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsMasonsHouse1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsMediumHouse1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsMediumHouse2;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsMeetingPoint4;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsMeetingPoint5;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsShepherdsHouse1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsSmallFarm1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsSmallHouse1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsSmallHouse2;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsSmallHouse3;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsSmallHouse4;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsSmallHouse5;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsSmallHouse6;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsSmallHouse7;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsSmallHouse8;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsStable1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsStable2;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsStreetDecor1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsTannery1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsTemple3;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsTemple4;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsToolSmith1;
import astrotibs.villagenames.village.biomestructures.PlainsStructures.PlainsWeaponsmith1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaAnimalPen1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaAnimalPen2;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaAnimalPen3;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaArmorer1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaButchersShop1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaButchersShop2;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaCartographer1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaFisherCottage1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaFletcherHouse1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaLargeFarm1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaLargeFarm2;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaLibrary1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaMason1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaMediumHouse1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaMediumHouse2;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaShepherd1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaSmallFarm;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaSmallHouse1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaSmallHouse2;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaSmallHouse3;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaSmallHouse4;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaSmallHouse5;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaSmallHouse6;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaSmallHouse7;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaSmallHouse8;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaStreetDecor1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaStreetSubstitute1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaStreetSubstitute2;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaStreetSubstitute3;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaStreetSubstitute4;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaTannery1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaTemple1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaTemple2;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaToolSmith1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaWeaponsmith1;
import astrotibs.villagenames.village.biomestructures.SavannaStructures.SavannaWeaponsmith2;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyAnimalPen1;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyAnimalPen2;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyArmorerHouse1;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyArmorerHouse2;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyButchersShop1;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyButchersShop2;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyCartographerHouse1;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyFarm1;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyFarm2;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyFisherCottage;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyFletcherHouse1;
import astrotibs.villagenames.village.biomestructures.SnowyStructures.SnowyLibrary1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaAnimalPen1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaArmorer2;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaArmorerHouse1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaButcherShop1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaCartographerHouse1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaFisherCottage1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaFletcherHouse1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaLargeFarm1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaLibrary1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaMasonsHouse1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaMediumFarm1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaMediumHouse1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaMediumHouse2;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaMediumHouse3;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaMediumHouse4;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaShepherdsHouse1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaSmallFarm1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaSmallHouse1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaSmallHouse2;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaSmallHouse3;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaSmallHouse4;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaSmallHouse5;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaStreetDecor1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaTannery1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaTemple1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaToolSmith1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaWeaponsmith1;
import astrotibs.villagenames.village.biomestructures.TaigaStructures.TaigaWeaponsmith2;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageCreationHandler;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class StructureCreationHandlers
{
	// Plains flower planter
	public static class PlainsAccessory1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsAccessory1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsAccessory1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsAccessory1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsAccessory1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains small animal pen
	public static class PlainsAnimalPen1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsAnimalPen1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsAnimalPen1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsAnimalPen1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsAnimalPen1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains large animal pen
	public static class PlainsAnimalPen2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsAnimalPen2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsAnimalPen2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsAnimalPen2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsAnimalPen2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains decorated animal pen
	public static class PlainsAnimalPen3_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsAnimalPen3_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsAnimalPen3.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsAnimalPen3.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsAnimalPen3.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains armorer
	public static class PlainsArmorerHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsArmorerHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsArmorerHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsArmorerHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsArmorerHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains large house
	public static class PlainsBigHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsBigHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsBigHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsBigHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsBigHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}

	// Plains Butcher 1
	public static class PlainsButcherShop1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsButcherShop1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsButcherShop1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsButcherShop1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsButcherShop1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Butcher 2
	public static class PlainsButcherShop2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsButcherShop2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsButcherShop2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsButcherShop2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsButcherShop2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Cartographer 1
	public static class PlainsCartographer1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsCartographer1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsCartographer1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsCartographer1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsCartographer1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Fisher Cottage 1
	public static class PlainsFisherCottage1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsFisherCottage1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsFisherCottage1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsFisherCottage1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsFisherCottage1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Fletcher House 1
	public static class PlainsFletcherHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsFletcherHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsFletcherHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsFletcherHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsFletcherHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Large Farm 1
	public static class PlainsLargeFarm1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsLargeFarm1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsLargeFarm1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsLargeFarm1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsLargeFarm1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Library 1
	public static class PlainsLibrary1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsLibrary1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsLibrary1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsLibrary1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsLibrary1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Library 2
	public static class PlainsLibrary2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsLibrary2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsLibrary2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsLibrary2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsLibrary2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Mason House 1
	public static class PlainsMasonsHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsMasonsHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsMasonsHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsMasonsHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsMasonsHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Medium House 1
	public static class PlainsMediumHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsMediumHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsMediumHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsMediumHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsMediumHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Medium House 2
	public static class PlainsMediumHouse2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsMediumHouse2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsMediumHouse2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsMediumHouse2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsMediumHouse2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Market 1
	public static class PlainsMeetingPoint4_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsMeetingPoint4_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsMeetingPoint4.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsMeetingPoint4.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsMeetingPoint4.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Market 2
	public static class PlainsMeetingPoint5_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsMeetingPoint5_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsMeetingPoint5.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsMeetingPoint5.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsMeetingPoint5.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Shepherd House
	public static class PlainsShepherdsHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsShepherdsHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsShepherdsHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsShepherdsHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsShepherdsHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Small Farm
	public static class PlainsSmallFarm1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsSmallFarm1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsSmallFarm1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsSmallFarm1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsSmallFarm1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Small House 1
	public static class PlainsSmallHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsSmallHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsSmallHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsSmallHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsSmallHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Small House 2
	public static class PlainsSmallHouse2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsSmallHouse2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsSmallHouse2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsSmallHouse2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsSmallHouse2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Small House 3
	public static class PlainsSmallHouse3_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsSmallHouse3_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsSmallHouse3.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsSmallHouse3.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsSmallHouse3.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Small House 4
	public static class PlainsSmallHouse4_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsSmallHouse4_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsSmallHouse4.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsSmallHouse4.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsSmallHouse4.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Small House 5
	public static class PlainsSmallHouse5_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsSmallHouse5_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsSmallHouse5.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsSmallHouse5.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsSmallHouse5.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Small House 6
	public static class PlainsSmallHouse6_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsSmallHouse6_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsSmallHouse6.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsSmallHouse6.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsSmallHouse6.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Small House 7
	public static class PlainsSmallHouse7_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsSmallHouse7_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsSmallHouse7.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsSmallHouse7.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsSmallHouse7.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Small House 8
	public static class PlainsSmallHouse8_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsSmallHouse8_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsSmallHouse8.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsSmallHouse8.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsSmallHouse8.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Cobblestone Stable
	public static class PlainsStable1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsStable1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsStable1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsStable1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsStable1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Terracotta Stable
	public static class PlainsStable2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsStable2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsStable2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsStable2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsStable2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Tannery
	public static class PlainsTannery1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsTannery1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsTannery1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsTannery1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsTannery1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Terracotta Temple
	public static class PlainsTemple3_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsTemple3_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsTemple3.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsTemple3.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsTemple3.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Cobblestone Temple
	public static class PlainsTemple4_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsTemple4_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsTemple4.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsTemple4.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsTemple4.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Tool Smithy
	public static class PlainsToolSmith1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsToolSmith1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsToolSmith1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsToolSmith1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsToolSmith1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Weapon Smithy
	public static class PlainsWeaponsmith1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsWeaponsmith1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsWeaponsmith1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsWeaponsmith1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsWeaponsmith1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Plains Road Decor
	public static class PlainsStreetDecor1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernPlainsStreetDecor1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(PlainsStreetDecor1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return PlainsStreetDecor1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return PlainsStreetDecor1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Small Animal Pen
	public static class DesertAnimalPen1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertAnimalPen1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertAnimalPen1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertAnimalPen1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertAnimalPen1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Covered Animal Pen
	public static class DesertAnimalPen2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertAnimalPen2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertAnimalPen2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertAnimalPen2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertAnimalPen2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Armorer House
	public static class DesertArmorer1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertArmorer1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertArmorer1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertArmorer1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertArmorer1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Butcher Shop
	public static class DesertButcherShop1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertButcherShop1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertButcherShop1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertButcherShop1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertButcherShop1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Cartographer House
	public static class DesertCartographerHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertCartographerHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertCartographerHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertCartographerHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertCartographerHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Small Farm
	public static class DesertFarm1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertFarm1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertFarm1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertFarm1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertFarm1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Large Farm
	public static class DesertFarm2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertFarm2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertFarm2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertFarm2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertFarm2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Fisher Cottage
	public static class DesertFisher1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertFisher1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertFisher1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertFisher1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertFisher1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Fletcher House
	public static class DesertFletcherHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertFletcherHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertFletcherHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertFletcherHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertFletcherHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Large Farm
	public static class DesertLargeFarm1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertLargeFarm1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertLargeFarm1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertLargeFarm1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertLargeFarm1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Library
	public static class DesertLibrary1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertLibrary1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertLibrary1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertLibrary1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertLibrary1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Mason House
	public static class DesertMason1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertMason1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertMason1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertMason1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertMason1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Medium House
	public static class DesertMediumHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertMediumHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertMediumHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertMediumHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertMediumHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Big House
	public static class DesertMediumHouse2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertMediumHouse2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertMediumHouse2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertMediumHouse2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertMediumHouse2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Shepherd House
	public static class DesertShepherdHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertShepherdHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertShepherdHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertShepherdHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertShepherdHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Small House 1
	public static class DesertSmallHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertSmallHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertSmallHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertSmallHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertSmallHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Small House 2
	public static class DesertSmallHouse2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertSmallHouse2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertSmallHouse2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertSmallHouse2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertSmallHouse2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Small House 3
	public static class DesertSmallHouse3_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertSmallHouse3_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertSmallHouse3.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertSmallHouse3.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertSmallHouse3.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Small House 4
	public static class DesertSmallHouse4_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertSmallHouse4_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertSmallHouse4.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertSmallHouse4.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertSmallHouse4.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Small House 5
	public static class DesertSmallHouse5_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertSmallHouse5_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertSmallHouse5.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertSmallHouse5.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertSmallHouse5.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Small House 6
	public static class DesertSmallHouse6_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertSmallHouse6_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertSmallHouse6.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertSmallHouse6.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertSmallHouse6.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Small House 7
	public static class DesertSmallHouse7_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertSmallHouse7_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertSmallHouse7.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertSmallHouse7.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertSmallHouse7.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Small House 8
	public static class DesertSmallHouse8_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertSmallHouse8_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertSmallHouse8.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertSmallHouse8.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertSmallHouse8.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Tannery 1
	public static class DesertTannery1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertTannery1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertTannery1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertTannery1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertTannery1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Temple 1
	public static class DesertTemple1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertTemple1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertTemple1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertTemple1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertTemple1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Temple 2
	public static class DesertTemple2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertTemple2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertTemple2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertTemple2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertTemple2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Tool Smithy
	public static class DesertToolSmith1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertToolSmith1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertToolSmith1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertToolSmith1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertToolSmith1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Weapon Smithy
	public static class DesertWeaponsmith1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertWeaponsmith1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertWeaponsmith1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertWeaponsmith1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertWeaponsmith1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Road Decor 1
	public static class DesertStreetDecor1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertStreetDecor1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertStreetDecor1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertStreetDecor1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertStreetDecor1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Road Terracotta Accent 1
	public static class DesertStreetSubstitute1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertStreetSubstitute1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertStreetSubstitute1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertStreetSubstitute1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertStreetSubstitute1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Road Terracotta Accent 2
	public static class DesertStreetSubstitute2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertStreetSubstitute2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertStreetSubstitute2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertStreetSubstitute2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertStreetSubstitute2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Road Decor 2
	public static class DesertStreetSubstitute3_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernDesertStreetSubstitute3_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(DesertStreetSubstitute3.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return DesertStreetSubstitute3.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return DesertStreetSubstitute3.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Animal Pen
	public static class TaigaAnimalPen1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaAnimalPen1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaAnimalPen1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaAnimalPen1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaAnimalPen1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Armorer Hut
	public static class TaigaArmorer2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaArmorer2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaArmorer2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaArmorer2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaArmorer2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Armorer House
	public static class TaigaArmorerHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaArmorerHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaArmorerHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaArmorerHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaArmorerHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Butcher Shop
	public static class TaigaButcherShop1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaButcherShop1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaButcherShop1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaButcherShop1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaButcherShop1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Cartographer House
	public static class TaigaCartographerHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaCartographerHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaCartographerHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaCartographerHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaCartographerHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Fisher Cottage
	public static class TaigaFisherCottage1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaFisherCottage1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaFisherCottage1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaFisherCottage1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaFisherCottage1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Fletcher House
	public static class TaigaFletcherHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaFletcherHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaFletcherHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaFletcherHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaFletcherHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Large Farm
	public static class TaigaLargeFarm1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaLargeFarm1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaLargeFarm1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaLargeFarm1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaLargeFarm1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Medium Farm
	public static class TaigaMediumFarm1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaMediumFarm1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaMediumFarm1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaMediumFarm1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaMediumFarm1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Library
	public static class TaigaLibrary1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaLibrary1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaLibrary1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaLibrary1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaLibrary1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Mason House
	public static class TaigaMasonsHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaMasonsHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaMasonsHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaMasonsHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaMasonsHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Medium House 1
	public static class TaigaMediumHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaMediumHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaMediumHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaMediumHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaMediumHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Medium House 2
	public static class TaigaMediumHouse2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaMediumHouse2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaMediumHouse2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaMediumHouse2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaMediumHouse2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Medium House 3
	public static class TaigaMediumHouse3_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaMediumHouse3_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaMediumHouse3.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaMediumHouse3.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaMediumHouse3.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Medium House 4
	public static class TaigaMediumHouse4_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaMediumHouse4_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaMediumHouse4.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaMediumHouse4.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaMediumHouse4.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Shepherd House 1
	public static class TaigaShepherdsHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaShepherdsHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaShepherdsHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaShepherdsHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaShepherdsHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Small Farm
	public static class TaigaSmallFarm1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaSmallFarm1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaSmallFarm1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaSmallFarm1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaSmallFarm1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Small House 1
	public static class TaigaSmallHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaSmallHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaSmallHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaSmallHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaSmallHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Small House 2
	public static class TaigaSmallHouse2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaSmallHouse2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaSmallHouse2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaSmallHouse2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaSmallHouse2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Small House 3
	public static class TaigaSmallHouse3_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaSmallHouse3_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaSmallHouse3.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaSmallHouse3.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaSmallHouse3.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Small House 4
	public static class TaigaSmallHouse4_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaSmallHouse4_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaSmallHouse4.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaSmallHouse4.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaSmallHouse4.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Small House 5
	public static class TaigaSmallHouse5_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaSmallHouse5_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaSmallHouse5.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaSmallHouse5.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaSmallHouse5.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Tannery
	public static class TaigaTannery1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaTannery1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaTannery1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaTannery1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaTannery1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Temple
	public static class TaigaTemple1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaTemple1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaTemple1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaTemple1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaTemple1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Tool Smith
	public static class TaigaToolSmith1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaToolSmith1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaToolSmith1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaToolSmith1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaToolSmith1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Weapon Smith House
	public static class TaigaWeaponsmith1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaWeaponsmith1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaWeaponsmith1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaWeaponsmith1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaWeaponsmith1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Weapon Smith Station
	public static class TaigaWeaponsmith2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaWeaponsmith2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaWeaponsmith2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaWeaponsmith2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaWeaponsmith2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Taiga Road Decor
	public static class TaigaStreetDecor1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernTaigaStreetDecor1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(TaigaStreetDecor1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return TaigaStreetDecor1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return TaigaStreetDecor1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Covered Animal Pen
	public static class SavannaAnimalPen1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaAnimalPen1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaAnimalPen1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaAnimalPen1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaAnimalPen1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Large Animal Pen
	public static class SavannaAnimalPen2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaAnimalPen2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaAnimalPen2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaAnimalPen2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaAnimalPen2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Medium Animal Pen
	public static class SavannaAnimalPen3_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaAnimalPen3_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaAnimalPen3.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaAnimalPen3.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaAnimalPen3.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Armorer House
	public static class SavannaArmorer1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaArmorer1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaArmorer1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaArmorer1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaArmorer1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Butcher Shop 1
	public static class SavannaButchersShop1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaButchersShop1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaButchersShop1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaButchersShop1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaButchersShop1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Butcher Shop 3
	public static class SavannaButchersShop2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaButchersShop2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaButchersShop2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaButchersShop2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaButchersShop2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Cartographer House
	public static class SavannaCartographer1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaCartographer1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaCartographer1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaCartographer1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaCartographer1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Fisher Cottage
	public static class SavannaFisherCottage1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaFisherCottage1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaFisherCottage1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaFisherCottage1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaFisherCottage1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Fletcher House
	public static class SavannaFletcherHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaFletcherHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaFletcherHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaFletcherHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaFletcherHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Methodical Farm
	public static class SavannaLargeFarm1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaLargeFarm1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaLargeFarm1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaLargeFarm1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaLargeFarm1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Haphazard Farm
	public static class SavannaLargeFarm2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaLargeFarm2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaLargeFarm2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaLargeFarm2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaLargeFarm2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Library
	public static class SavannaLibrary1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaLibrary1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaLibrary1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaLibrary1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaLibrary1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Mason
	public static class SavannaMason1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaMason1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaMason1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaMason1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaMason1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Medium House 1
	public static class SavannaMediumHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaMediumHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaMediumHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaMediumHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaMediumHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Medium House 2
	public static class SavannaMediumHouse2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaMediumHouse2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaMediumHouse2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaMediumHouse2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaMediumHouse2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Shepherd House
	public static class SavannaShepherd1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaShepherd1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaShepherd1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaShepherd1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaShepherd1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Small Farm
	public static class SavannaSmallFarm_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaSmallFarm_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaSmallFarm.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaSmallFarm.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaSmallFarm.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Small House 1
	public static class SavannaSmallHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaSmallHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaSmallHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaSmallHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaSmallHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Small House 2
	public static class SavannaSmallHouse2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaSmallHouse2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaSmallHouse2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaSmallHouse2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaSmallHouse2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Small House 3
	public static class SavannaSmallHouse3_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaSmallHouse3_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaSmallHouse3.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaSmallHouse3.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaSmallHouse3.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Small House 4
	public static class SavannaSmallHouse4_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaSmallHouse4_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaSmallHouse4.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaSmallHouse4.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaSmallHouse4.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Small House 5
	public static class SavannaSmallHouse5_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaSmallHouse5_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaSmallHouse5.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaSmallHouse5.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaSmallHouse5.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Small House 6
	public static class SavannaSmallHouse6_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaSmallHouse6_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaSmallHouse6.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaSmallHouse6.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaSmallHouse6.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Small House 7
	public static class SavannaSmallHouse7_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaSmallHouse7_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaSmallHouse7.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaSmallHouse7.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaSmallHouse7.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Small House 8
	public static class SavannaSmallHouse8_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaSmallHouse8_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaSmallHouse8.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaSmallHouse8.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaSmallHouse8.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Tannery 1
	public static class SavannaTannery1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaTannery1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaTannery1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaTannery1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaTannery1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Temple 1
	public static class SavannaTemple1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaTemple1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaTemple1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaTemple1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaTemple1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Temple 2
	public static class SavannaTemple2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaTemple2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaTemple2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaTemple2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaTemple2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Tool Smithy
	public static class SavannaToolSmith1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaToolSmith1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaToolSmith1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaToolSmith1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaToolSmith1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Weapon Smithy 1
	public static class SavannaWeaponsmith1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaWeaponsmith1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaWeaponsmith1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaWeaponsmith1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaWeaponsmith1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Weapon Smithy 2
	public static class SavannaWeaponsmith2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaWeaponsmith2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaWeaponsmith2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaWeaponsmith2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaWeaponsmith2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Savanna Road Decor
	public static class SavannaStreetDecor1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaStreetDecor1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaStreetDecor1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaStreetDecor1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaStreetDecor1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Roadside Farm 1
	public static class SavannaStreetSubstitute1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaStreetSubstitute1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaStreetSubstitute1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaStreetSubstitute1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaStreetSubstitute1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Roadside Farm 2
	public static class SavannaStreetSubstitute2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaStreetSubstitute2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaStreetSubstitute2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaStreetSubstitute2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaStreetSubstitute2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Roadside Farm 3
	public static class SavannaStreetSubstitute3_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaStreetSubstitute3_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaStreetSubstitute3.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaStreetSubstitute3.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaStreetSubstitute3.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Desert Roadside Farm 4
	public static class SavannaStreetSubstitute4_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSavannaStreetSubstitute4_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SavannaStreetSubstitute4.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SavannaStreetSubstitute4.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SavannaStreetSubstitute4.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Animal Pen 1
	public static class SnowyAnimalPen1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyAnimalPen1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyAnimalPen1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyAnimalPen1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyAnimalPen1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Animal Pen 2
	public static class SnowyAnimalPen2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyAnimalPen2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyAnimalPen2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyAnimalPen2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyAnimalPen2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Armorer House 1
	public static class SnowyArmorerHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyArmorerHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyArmorerHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyArmorerHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyArmorerHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Armorer House 2
	public static class SnowyArmorerHouse2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyArmorerHouse2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyArmorerHouse2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyArmorerHouse2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyArmorerHouse2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Butcher House
	public static class SnowyButchersShop1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyButchersShop1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyButchersShop1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyButchersShop1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyButchersShop1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Butcher Igloo
	public static class SnowyButchersShop2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyButchersShop2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyButchersShop2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyButchersShop2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyButchersShop2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Cartographer House
	public static class SnowyCartographerHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyCartographerHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyCartographerHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyCartographerHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyCartographerHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Square Farm
	public static class SnowyFarm1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyFarm1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyFarm1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyFarm1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyFarm1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Patch Farm
	public static class SnowyFarm2_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyFarm2_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyFarm2.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyFarm2.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyFarm2.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Fisher Cottage
	public static class SnowyFisherCottage_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyFisherCottage_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyFisherCottage.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyFisherCottage.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyFisherCottage.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Fletcher House
	public static class SnowyFletcherHouse1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyFletcherHouse1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyFletcherHouse1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyFletcherHouse1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyFletcherHouse1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
	// Snowy Library
	public static class SnowyLibrary1_Handler implements IVillageCreationHandler
	{
		ArrayList<Double> ali = GeneralConfig.componentModernSnowyLibrary1_vals;
		
	    @Override
	    public PieceWeight getVillagePieceWeight(Random random, int villageSize)
	    {
	    	double weightDouble = ali.get(0); int weightStochastic = MathHelper.floor_double(weightDouble) + (random.nextDouble()<(weightDouble%1) ? 1:0);
	    	double lowerLimitDouble = villageSize * ali.get(1) + ali.get(2); int lowerLimitStochastic = MathHelper.floor_double(lowerLimitDouble) + (random.nextDouble()<(lowerLimitDouble%1) ? 1:0);
	    	double upperLimitDouble = villageSize * ali.get(3) + ali.get(4); int upperLimitStochastic = MathHelper.floor_double(upperLimitDouble) + (random.nextDouble()<(upperLimitDouble%1) ? 1:0);
	    	return new PieceWeight(SnowyLibrary1.class, weightStochastic, MathHelper.getRandomIntegerInRange(random, lowerLimitStochastic, upperLimitStochastic));
	    }
	    
	    @Override
	    public Class<?> getComponentClass() {return SnowyLibrary1.class;}
	    
	    @Override
	    public Object buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int x, int y, int z, int horizIndex, int componentType)
	    {
	    	if (startPiece instanceof StartVN) {return SnowyLibrary1.buildComponent((StartVN)startPiece, pieces, random, x, y, z, horizIndex, componentType);} return null;
	    }
	}
	
}