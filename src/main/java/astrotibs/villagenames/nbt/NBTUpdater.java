package astrotibs.villagenames.nbt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.LogHelper;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureData;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NBTUpdater {
	
	/**
	 * The point of this event is to update the old villagenames.nbt files to the new format
	 * so that they will be compatible with the new config which allows users to enter in
	 * their own structure types so that VN will identify and name them.
	 * 
	 * This should only fire once on each world load.	 
	 */
	@SubscribeEvent(receiveCanceled=true)
	public void retroGenElders(WorldEvent.Load event) {
		
		World world = event.getWorld();
		
		if(!world.isRemote) {
			
			boolean warnPlayer = false;
			
		    /* 
		     * STRUCTURE		key					toptag
		     * 
		     * Villages:		villagenames		Villages
		     * Temples:			villagenames_te		Temples
		     * Mineshafts:		villagenames_mi		Mineshafts
		     * Strongholds:		villagenames_st		Strongholds
		     * Monuments:		villagenames_mo		Monuments
		     * Mansions:		villagenames_ma		Mansions
		     * Fortress:		villagenames_fo		Fortresses
		     * End City:		villagenames_ec		EndCities
		     * 
		     * Abandoned Base:	villagenames_gcab	AbandonedBases
		     * End Islands:		villagenames_heei	hardcoreenderdragon_EndIsland
		     * End Towers:		villagenames_heet	hardcoreenderdragon_EndTower
		     * Fronos Villages:	villagenames_mpfv	FronosVillages
		     * Koentus Vill:	villagenames_mpkv	KoentusVillages
		     * Moon Villages:	villagenames_gcmv	MoonVillages
		     * Nibiru Villages:	villagenames_mpnv	NibiruVillages
		     */
			
			// Make hashmap linking old vn filename with its top-level tag name
			Map<String,String> old_dataname_map =new HashMap();
			old_dataname_map.put("villagenames",   "Villages");
			old_dataname_map.put("villagenames_te","Temples");
			old_dataname_map.put("villagenames_mi","Mineshafts");
			old_dataname_map.put("villagenames_st","Strongholds");
			old_dataname_map.put("villagenames_mo","Monuments");
			old_dataname_map.put("villagenames_ma","Mansions");
			old_dataname_map.put("villagenames_fo","Fortresses");
			old_dataname_map.put("villagenames_ec","EndCities");
			
			old_dataname_map.put("villagenames_gcab","AbandonedBases");
			old_dataname_map.put("villagenames_heei","hardcoreenderdragon_EndIsland");
			old_dataname_map.put("villagenames_heet","hardcoreenderdragon_EndTower");
			old_dataname_map.put("villagenames_mpfv","FronosVillages");
			old_dataname_map.put("villagenames_mpkv","KoentusVillages");
			old_dataname_map.put("villagenames_gcmv","MoonVillages");
			old_dataname_map.put("villagenames_mpnv","NibiruVillages");
			
			String[] new_titleTags = new String[]{
					"Village",
					"Temple",
					"Mineshaft",
					"Stronghold",
					"Monument",
					"Mansion",
					"Fortress",
					"EndCity",
					"GC_AbandonedBase",
					"hardcoreenderdragon_EndIsland",
					"hardcoreenderdragon_EndTower",
					"FronosVillage",
					"KoentusVillage",
					"MoonVillage",
					"NibiruVillage",
					"DUMMY"
					};
			
			String[] old_topTags = new String[]{
					"Villages",
					"Temples",
					"Mineshafts",
					"Strongholds",
					"Monuments",
					"Mansions",
					"Fortresses",
					"EndCities",
					"AbandonedBases",
					"hardcoreenderdragon_EndIsland",
					"hardcoreenderdragon_EndTower",
					"FronosVillages",
					"KoentusVillages",
					"MoonVillages",
					"NibiruVillages",
					"NamedStructures"
					};
			String[] old_filenames = new String[]{
					"villagenames",
					"villagenames_te",
					"villagenames_mi",
					"villagenames_st",
					"villagenames_mo",
					"villagenames_ma",
					"villagenames_fo",
					"villagenames_ec",
					"villagenames_gcab",
					"villagenames_heei",
					"villagenames_heet",
					"villagenames_mpfv",
					"villagenames_mpkv",
					"villagenames_gcmv",
					"villagenames_mpnv",
					"villagenames_DUMMY"
					};
			
			for (int i=0 ; i < old_filenames.length ; i++) {
				
				// Load village data
    			VNWorldDataStructure data_old = VNWorldDataStructure.forWorld(world, old_filenames[i], old_topTags[i]);
    			NBTTagCompound tagCompound_old = data_old.getData();
    			Set tagmapKeyset_old = tagCompound_old.getKeySet(); //Gets the town key list: "coordinates"
				
    			VNWorldDataStructure data_new = VNWorldDataStructure.forWorld(world, "villagenames3_" + new_titleTags[i], "NamedStructures");
    			NBTTagCompound tagCompound_new = data_new.getData();
				Set tagmapKeyset_new = tagCompound_new.getKeySet(); //Gets the town key list: "coordinates"
				
				
				if (tagmapKeyset_new.size() <= 0 ) { // The new version of the nbt file has not yet been made.
					if ( tagmapKeyset_old.size() >0 ) { // Re-create the already generated data and make new nbt files!
						
						warnPlayer = true;
						
						Iterator itr = tagmapKeyset_old.iterator();
						String townSignEntry_old;
				        
						LogHelper.info("ATTENTION! The old data file " + old_filenames[i] + ".nat will no longer be used. "
								+ "Its data has been moved into the new file villagenames3_" + new_titleTags[i] + ".dat");
						
						while(itr.hasNext()) { // Going through the list of VN villages
				        	Object element = itr.next();
				        	townSignEntry_old = element.toString(); //Text name of village header (e.g. "x535y80z39")
				        	
				        	
				        	//The only index that has data is 0:
				            NBTTagCompound tagList_old = tagCompound_old.getTagList(townSignEntry_old, tagCompound_old.getId()).getCompoundTagAt(0);
				            
				            
				            // Make the data bundle to save to NBT
	                		NBTTagList nbttaglist_new = new NBTTagList();
	                		
	                		NBTTagCompound nbttagcompound1 = tagList_old;//new NBTTagCompound();
	                        
	                		// Check to see if certain entry tag strings exist
	                		if (townSignEntry_old.indexOf("_nosign")!=-1) { nbttagcompound1.setBoolean("preVN", true); }
	                		if (townSignEntry_old.indexOf("_fromvillager")!=-1) { nbttagcompound1.setBoolean("fromEntity", true); }
	                		if (townSignEntry_old.indexOf("_fromcodex")!=-1) { nbttagcompound1.setBoolean("fromCodex", true); }
	                		
	                        nbttaglist_new.appendTag(nbttagcompound1);
				            
				            
				            // Retrieve the structure name
				            String namePrefix = tagList_old.getString("namePrefix");
				            String nameRoot = tagList_old.getString("nameRoot");
				            String nameSuffix = tagList_old.getString("nameSuffix");
				            String signX = tagList_old.getString("signX");
				            String signY = tagList_old.getString("signY");
				            String signZ = tagList_old.getString("signZ");
				            
				            data_new.getData().setTag((namePrefix + " " + nameRoot + " " + nameSuffix).trim() + ", x" + signX + " y" + signY + " z" + signZ, nbttaglist_new);
				        }
						data_new.markDirty();
					}
				}
				else { // The new version of the NBT file has been made.
					if (tagmapKeyset_old.size() > tagmapKeyset_new.size() ) {
						// There are entries in the new VN file, which implies that this has already run once.
						// However, there are MORE entries in the old file, which implies either the player
						// has temporarily reverted to a previous version of VN and has discovered new structures,
						// or else I have done something wrong. Either way, notify the player.
						LogHelper.error("ATTENTION! The new data file" + new_titleTags[i] + ".dat has entries, but the old " + old_filenames[i] + ".dat" + "has MORE entries.");
						LogHelper.error("This could happen if you switched to an older version of "+Reference.MOD_NAME+" after running a newer one. Either way, those new names will be ignored.");
					}
				}
			}
			
			
			
			// Piggybacking the witch hut mushroom pot updater
			
			if (
					GeneralConfig.swampHutMushroomPot
					&& world.provider.getDimension()==0
					) { // Player is in the Overworld
				
				MapGenStructureData structureData;
				try {
					structureData = (MapGenStructureData)event.getWorld().getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "Temple");
					NBTTagCompound nbttagcompound = structureData.getTagCompound();
					
					Iterator itr = nbttagcompound.getKeySet().iterator();
					
					while (itr.hasNext()) { // Go through list of already-generated Swamp Huts
						Object element = itr.next();
						
						try {
							NBTBase nbtbase = nbttagcompound.getTag(element.toString());
							if (nbtbase.getId() == 10) { //10 is NBT tag compound, I think
								NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbtbase;
								
								int[] boundingBoxIA = nbttagcompound2.getIntArray("BB");
								StructureBoundingBox boundingBox = new StructureBoundingBox(boundingBoxIA);
								
								
								
								boolean hasHadWoolReplaced = nbttagcompound2.getBoolean("VNMushroomPotFixed");
								
								String templeType = nbttagcompound2.getTagList("Children", 10).getCompoundTagAt(0).getString("id");
								
								if (templeType.equals("TeSH") && !hasHadWoolReplaced) { // This Swamp Hut was generated before the 3.0 update, so here's our chance to update the mushroom pot
									
									int ChunkX = nbttagcompound2.getInteger("ChunkX");
									int ChunkZ = nbttagcompound2.getInteger("ChunkZ");
									
							        //Calculate the center position of the swamp hut
							        double swampHutXCenter = (boundingBox.maxX+boundingBox.minX)/2.0D;
							        double swampHutZCenter = (boundingBox.maxZ+boundingBox.minZ)/2.0D;
							        
							        // Run the method to update the flower pot
							        updateSwampHutPot(event, ChunkX, ChunkZ);
							        
									
									if (GeneralConfig.debugMessages) LogHelper.info("Updating swamp hut mushroom pot at " + (int)swampHutXCenter + " " + (int)swampHutZCenter);
									
									// Set a new temple tag to indicate that the wool has been replaced
									
									nbttagcompound2.setBoolean("VNMushroomPotFixed", true);
									structureData.setDirty(true);
								}
							}
						} catch (Exception e) {
							LogHelper.warn("Failed to evaluate flower pot status of Swamp Hut");
						}
						
					}
				}
				catch (Exception e) { // This fails when the Temple list is empty (i.e. none have been generated).
					if (GeneralConfig.debugMessages) {LogHelper.warn("Failed to load Temple list, or none exists.");}
				}
			}
			
			
			
			
			
		}
	}
	
	
	private void updateSwampHutPot(WorldEvent.Load event, int ChunkX, int ChunkZ) {
		
		
        // Replace wool
		Block blocktoscan;
		int potmeta;
		IBlockState iblockstate;
		BlockPos potPos;
		
		for (int k = 50; k <= 80; k++) {
			for (int i=-16; i < 16; i++) {
				for (int j=-16; j < 16; j++) {
					
					potPos = new BlockPos((ChunkX << 4)+i, k, (ChunkZ << 4)+j);
					
					iblockstate = event.getWorld().getBlockState( potPos );
					blocktoscan = iblockstate.getBlock();
					
					if (blocktoscan == Blocks.FLOWER_POT) {
						
						//potmeta = event.world.getblockstate;
						potmeta = blocktoscan.getMetaFromState(iblockstate);
						
						if (potmeta != 7 ) { // This is blue wool at y=64
							
							// Replace the wool block
							
							TileEntity flowerPotWithRedMushroom = (new BlockFlowerPot()).createNewTileEntity(event.getWorld(), 7);
							
							event.getWorld().setBlockState(potPos, Blocks.FLOWER_POT.getDefaultState());
                    		
							event.getWorld().setTileEntity(potPos, flowerPotWithRedMushroom);
							
							return;
							
						}
					}
				}
			}
		}
		
		
	}
	
	
}
