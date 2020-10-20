package astrotibs.villagenames.village;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import astrotibs.villagenames.config.village.VillageGeneratorConfigHandler;
import astrotibs.villagenames.utility.FunctionsVN;
import astrotibs.villagenames.village.biomestructures.DesertStructures;
import astrotibs.villagenames.village.biomestructures.PlainsStructures;
import astrotibs.villagenames.village.biomestructures.SavannaStructures;
import astrotibs.villagenames.village.biomestructures.SnowyStructures;
import astrotibs.villagenames.village.biomestructures.TaigaStructures;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces.Road;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent.EventType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MapGenVillageVN extends MapGenVillage
{
	/**
	 * Adapted from RTG 
	 * https://github.com/Team-RTG/Realistic-Terrain-Generation/blob/1.7.10-master/src/main/java/rtg/event/EventManagerRTG.java
	 */
	@SubscribeEvent(priority = EventPriority.LOW)
    public void onInitMapGen(InitMapGenEvent event)
	{
		if (event.getType() == EventType.VILLAGE && VillageGeneratorConfigHandler.newVillageGenerator)
        {
			// Do a try/catch because in case the Overworld has not yet loaded
	        try {event.setNewGen(new MapGenVillageVN());}
	        catch (Exception e) {return;}
        }
	}
	
    private int size;
    private int distance; // Maximum distance between villages
    private int minTownSeparation; // Minimum distance between villages
    
    public MapGenVillageVN()
    {
    	this.size = VillageGeneratorConfigHandler.newVillageSize-1; // Because vanilla is "0" and default provided value is 1
    	
    	// Set spacings
    	this.minTownSeparation = VillageGeneratorConfigHandler.newVillageSpacingMedian - VillageGeneratorConfigHandler.newVillageSpacingSpread;
    	if (this.minTownSeparation<1) {this.minTownSeparation=1;}
    	
        this.distance = VillageGeneratorConfigHandler.newVillageSpacingMedian + VillageGeneratorConfigHandler.newVillageSpacingSpread;
        
    }
    
    // Same as vanilla
    public MapGenVillageVN(Map par1Map)
    {
    	this();
    	Iterator iterator = par1Map.entrySet().iterator();
    	
        while (iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();

            if (((String)entry.getKey()).equals("size"))
            {
                this.size = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.size, 0);
            }
            else if (((String)entry.getKey()).equals("distance"))
            {
                this.distance = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.distance, this.minTownSeparation + 1);
            }
        }
    }
    
    @Override
    public String getStructureName()
    {
        return "Village";
    }
    
    @Override
    protected boolean canSpawnStructureAtCoords(int chunkXin, int chunkZin)
    {
        int chunkX = chunkXin;
        int chunkZ = chunkZin;
        
        // Handle negative chunk values
        if (chunkXin < 0) {chunkXin -= this.distance - 1;}
        if (chunkZin < 0) {chunkZin -= this.distance - 1;}
        
        // The (floor) number of [max Village chunk distance]s this chunk is
        int chunkXModulated = chunkXin / this.distance;
        int chunkZModulated = chunkZin / this.distance;
        
        // Set the random seed based on number of X, Z spacings
        Random random = this.worldObj.setRandomSeed(chunkXModulated, chunkZModulated, 10387312); // Idk the significance of this number. May be unique to "Village" structures?
        
        // Get the chunk X and Z, floored by the number of max village spacings
        chunkXModulated *= this.distance;
        chunkZModulated *= this.distance;
        
        // Add random offset based on village spacing min and max values
        chunkXModulated += random.nextInt(this.distance - this.minTownSeparation);
        chunkZModulated += random.nextInt(this.distance - this.minTownSeparation);
        
        // Return "true" if this chunk X & Z is flagged for village construction AND the biome is allowed as per the config
        if (chunkX == chunkXModulated && chunkZ == chunkZModulated)
        {
        	Biome biome = this.worldObj.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8)); // Biome doesn't care about Y
        	
        	if (VillageGeneratorConfigHandler.spawnBiomesNames != null) // Biome list is not empty
    		{
        		Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
        		
    			for (int i = 0; i < mappedBiomes.get("BiomeNames").size(); i++)
    			{
    				if (mappedBiomes.get("BiomeNames").get(i).equals(biome.getBiomeName()))
    				{
    					BiomeManager.addVillageBiome(biome, true); // Set biome to be able to spawn villages
    					
    					return true;
    				}
    			}
    		}
        }
        return false;
    }
    
    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new MapGenVillageVN.Start(this.worldObj, this.rand, chunkX, chunkZ, this.size);
    }
    
    // Copied from vanilla
    public static class Start extends StructureStart
    {
        /** well ... thats what it does */
        private boolean hasMoreThanTwoComponents;
        
        public Start() {}

        public Start(World world, Random random, int chunkX, int chunkZ, int villageSize)
        {
            super(chunkX, chunkZ);

            // Choose starter type based on biome
            int posX = (chunkX << 4) + 2;
            int posZ = (chunkZ << 4) + 2;
            BiomeProvider biomeProvider = world.getBiomeProvider();
            Biome biome = biomeProvider.getBiome(new BlockPos(posX, 64, posZ));
			Map<String, ArrayList<String>> mappedBiomes = VillageGeneratorConfigHandler.unpackBiomes(VillageGeneratorConfigHandler.spawnBiomesNames);
			FunctionsVN.VillageType startVillageType;
			
			// Attempt to swap it with the config value
			try {
            	String mappedVillageType = (String) (mappedBiomes.get("VillageTypes")).get(mappedBiomes.get("BiomeNames").indexOf(biome.getBiomeName()));
            	if (mappedVillageType.equals("")) {startVillageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
            	else {startVillageType = FunctionsVN.VillageType.getVillageTypeFromName(mappedVillageType, FunctionsVN.VillageType.PLAINS);}
            	}
			catch (Exception e) {startVillageType = FunctionsVN.VillageType.getVillageTypeFromBiome(biomeProvider, posX, posZ);}
			
            
            
            // My modified version, which allows the user to disable each building
            List list = StructureVillageVN.getStructureVillageWeightedPieceList(random, villageSize, startVillageType);
            
            // Generate the "start" component and add it to the list
            StructureVillageVN.StartVN start = null;
            
            // Select a starter at random
            
            StructureVillageVN.StartVN[] plainsStarters = new StructureVillageVN.StartVN[]
            {
	        		new PlainsStructures.PlainsFountain01(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Fountain
	        		new PlainsStructures.PlainsMeetingPoint1(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Plains Well
	        		new PlainsStructures.PlainsMeetingPoint2(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Market
	        		new PlainsStructures.PlainsMeetingPoint3(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Tree
            };
            
            StructureVillageVN.StartVN[] desertStarters = new StructureVillageVN.StartVN[]
            {
	            	new DesertStructures.DesertMeetingPoint1(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Fountain with structure
	            	new DesertStructures.DesertMeetingPoint2(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Desert well
	            	new DesertStructures.DesertMeetingPoint3(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Desert market
            };
            
            StructureVillageVN.StartVN[] taigaStarters = new StructureVillageVN.StartVN[]
            {
	            	new TaigaStructures.TaigaMeetingPoint1(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Simple grass plot with two houses
	            	new TaigaStructures.TaigaMeetingPoint2(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Taiga Well
            };
            
            StructureVillageVN.StartVN[] savannaStarters = new StructureVillageVN.StartVN[]
            {
	            	new SavannaStructures.SavannaMeetingPoint1(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Savanna market
	            	new SavannaStructures.SavannaMeetingPoint2(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Savanna fountain
	            	new SavannaStructures.SavannaMeetingPoint3(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Savanna double well
	            	new SavannaStructures.SavannaMeetingPoint4(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Savanna single well
            };
            
            StructureVillageVN.StartVN[] snowyStarters = new StructureVillageVN.StartVN[]
            {
            		new SnowyStructures.SnowyMeetingPoint1(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Ice spire
            		new SnowyStructures.SnowyMeetingPoint2(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Frozen Fountain
            		new SnowyStructures.SnowyMeetingPoint3(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize), // Snowy Pavilion
            };
            
            if (startVillageType==FunctionsVN.VillageType.DESERT)
            {
            	start = desertStarters[random.nextInt(desertStarters.length)];
            }
            else if (startVillageType==FunctionsVN.VillageType.TAIGA)
            {
            	start = taigaStarters[random.nextInt(taigaStarters.length)];
            }
            else if (startVillageType==FunctionsVN.VillageType.SAVANNA)
            {
            	start = savannaStarters[random.nextInt(savannaStarters.length)];
            }
            else if (startVillageType==FunctionsVN.VillageType.SNOWY)
            {
            	start = snowyStarters[random.nextInt(snowyStarters.length)];
            }
            else // Plains if nothing else matches
            {
            	start = plainsStarters[random.nextInt(plainsStarters.length)];
            }

            
            // Force a specific starter for testing purposes
            //start = new DesertStructures.DesertMeetingPoint3(world.getBiomeProvider(), 0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2, list, villageSize);
            
            // Add well to the component list
            this.components.add(start);
            
            // Build the town center and get that ball rollin homie
            start.buildComponent(start, this.components, random);
            
            List paths =      start.pendingRoads; // Paths
            List components = start.pendingHouses; // Village Components
            int counter; // Used a couple times

            while (!paths.isEmpty() || !components.isEmpty())
            {
                StructureComponent structurecomponent;
                
                if (paths.isEmpty()) // There are components remaining, but no paths
                {
                    counter = random.nextInt(components.size());
                    structurecomponent = (StructureComponent)components.remove(counter);
                    structurecomponent.buildComponent(start, this.components, random);
                }
                else // There are paths remaining, but no components
                {
                    counter = random.nextInt(paths.size());
                    structurecomponent = (StructureComponent)paths.remove(counter);
                    structurecomponent.buildComponent(start, this.components, random);
                }
            }

            this.updateBoundingBox();
            counter = 0;
            Iterator iterator = this.components.iterator();
            
            while (iterator.hasNext())
            {
                StructureComponent structurecomponent_temp = (StructureComponent)iterator.next();

                if (!(structurecomponent_temp instanceof Road)) {++counter;}
            }
            
            this.hasMoreThanTwoComponents = counter > 2;
        }

        /**
         * currently only defined for Villages, returns true if Village has more than 2 non-road components
         */
        @Override
        public boolean isSizeableStructure()
        {
            return this.hasMoreThanTwoComponents;
        }
        
        @Override
        public void writeToNBT(NBTTagCompound tagCompound)
        {
            super.writeToNBT(tagCompound);
            tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
        }
        
        @Override
        public void readFromNBT(NBTTagCompound tagCompound)
        {
            super.readFromNBT(tagCompound);
            this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
        }
    }
}
