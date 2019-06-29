package astrotibs.villagenames.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import astrotibs.villagenames.VillageNames;
import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.handler.AchievementReward;
import astrotibs.villagenames.handler.EntityInteractHandler;
import astrotibs.villagenames.handler.WriteBookHandler;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.nbt.VNWorldDataStructure;
import astrotibs.villagenames.utility.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.JsonSerializableSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureData;

public class ItemCodex extends Item {
	
	public ItemCodex() {
		super();
		this.setCreativeTab(CreativeTabs.MISC);
		this.setUnlocalizedName("codex");
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
    
    /**
     * Called whenever this item is equipped and the right mouse button is pressed.
     */
    @Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
    	
    	Random random = world.rand;
    	
    	if (!world.isRemote) {
    		
    		if (player.inventory.hasItemStack(new ItemStack(Items.BOOK))) {
    			
    			MapGenStructureData structureData;
    			World worldIn = player.worldObj;
    			int[ ] BB = new int[6];
    			boolean playerIsInVillage = false; // Set to true if you're in a village; used for Ghost Town achievement.
    			
    			// Form arrays of all the default stuff
    			ArrayList<String> nameTypes = new ArrayList();
    			ArrayList<String> structureTypes = new ArrayList();
    			ArrayList<String> structureTitles = new ArrayList();
    			ArrayList<String> dimensionNames = new ArrayList();
    			ArrayList<String> bookTypes = new ArrayList();
    			
    			if (player.dimension==0) { // Player is in Overworld
    				
    				nameTypes.add("mansion");
    				structureTypes.add("Mansion");
    				structureTitles.add("Mansion");
    				dimensionNames.add("");
    				bookTypes.add("mansion");
    				
    				nameTypes.add("monument");
    				structureTypes.add("Monument");
    				structureTitles.add("Monument");
    				dimensionNames.add("");
    				bookTypes.add("monument");
    				
    				nameTypes.add("stronghold");
    				structureTypes.add("Stronghold");
    				structureTitles.add("Stronghold");
    				dimensionNames.add("");
    				bookTypes.add("stronghold");

    				nameTypes.add("temple");
    				structureTypes.add("Temple");
    				structureTitles.add("Temple");
    				dimensionNames.add("");
    				bookTypes.add("temple");
    				
    				nameTypes.add("village");
    				structureTypes.add("Village");
    				structureTitles.add("Village");
    				dimensionNames.add("");
    				bookTypes.add("village");
    				
    				// Mineshaft is last priority because there's so damn many of them.
    				// I once had one overlap with a Stronghold, and the Codex only identified the Mineshaft.
    				nameTypes.add("mineshaft");
    				structureTypes.add("Mineshaft");
    				structureTitles.add("Mineshaft");
    				dimensionNames.add("");
    				bookTypes.add("mineshaft");
    			}
    			else if (player.dimension==-1) { // Player is in Nether
    				
    				nameTypes.add("fortress");
    				structureTypes.add("Fortress");
    				structureTitles.add("Fortress");
    				dimensionNames.add("The Nether");
    				bookTypes.add("fortress");
    			}
    			else if (player.dimension==1) { // Player is in End
    				
    				nameTypes.add("endcity");
    				structureTypes.add("EndCity");
    				structureTitles.add("End City");
    				dimensionNames.add("The End");
    				bookTypes.add("endcity");
    			}
    			
    			// Add in custom structure types from the config
    			
    			// keys: "NameTypes", "StructureTypes", "StructureTitles", "DimensionNames", "BookTypes", "ClassPaths"
				Map<String, ArrayList> mappedModStructureNames = GeneralConfig.unpackModStructures(GeneralConfig.modStructureNames);
				
				nameTypes.addAll( mappedModStructureNames.get("NameTypes") );
				structureTypes.addAll( mappedModStructureNames.get("StructureTypes") );
				structureTitles.addAll( mappedModStructureNames.get("StructureTitles") );
				dimensionNames.addAll( mappedModStructureNames.get("DimensionNames") );
				bookTypes.addAll( mappedModStructureNames.get("BookTypes") );
				
    			String headerTags = new String();
				String namePrefix = new String();
				String nameRoot = new String();
				String nameSuffix = new String();
    			String structureTitle = "";
				String dimensionName = "";
    			String bookType = "";
    			
    			int signX = -1;
    			int signY = -1;
    			int signZ = -1;
    			
    			structureLoop:
    			for (int i=0; i < structureTypes.size(); i++) {
    				
    				try {
    					// Load in the vanilla structure file
        				structureData = (MapGenStructureData)worldIn.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, structureTypes.get(i));
        				NBTTagCompound nbttagcompound = structureData.getTagCompound();
        				
        				// Iterate through the entries
        				Iterator itr = nbttagcompound.getKeySet().iterator();
        				
        				while (itr.hasNext()) {
        					Object element = itr.next();
        					
        					NBTBase nbtbase = nbttagcompound.getTag(element.toString());
        					
        					if (nbtbase.getId() == 10) {
        						NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbtbase;
        						
        						try {
        							
            						if ( !(structureTypes.get(i)).equals("Village")
            								|| nbttagcompound2.getBoolean("Valid") // Either this is a village with a "valid" tag, or it's not a village.
            								) {
            							int[] boundingBox = nbttagcompound2.getIntArray("BB");
            							// Now check to see if the player is inside the feature
            							if (
        									   player.posX >= boundingBox[0]
        									&& player.posY >= boundingBox[1]
        									&& player.posZ >= boundingBox[2]
        									&& player.posX <= boundingBox[3]
        									&& player.posY <= boundingBox[4]
        									&& player.posZ <= boundingBox[5]
            								) { // Player is inside bounding box.
            								
            								// Specifically check if this is a Village.
            								// If so, you can pass this for checking the Ghost Town achievement.
            								if (structureTypes.get(i).equals("Village")) {
            										playerIsInVillage = true;
            									}
            								
            								String structureName;
            								String[] structureInfoArray = WriteBookHandler.tryGetStructureInfo(structureTypes.get(i), boundingBox, worldIn);
            								
            								namePrefix = structureInfoArray[0];
            								nameRoot = structureInfoArray[1];
            								nameSuffix = structureInfoArray[2];
            								
            								// If none is found, these strings are "null" which parseInt does not like very much
            								try {signX = Integer.parseInt(structureInfoArray[3]);} catch (Exception e) {}
            								try {signY = Integer.parseInt(structureInfoArray[4]);} catch (Exception e) {}
            								try {signZ = Integer.parseInt(structureInfoArray[5]);} catch (Exception e) {}
            								
            								// If a name was NOT returned, then we need to generate a new one, as is done below:
            								
            								int[] structureCoords = new int[] {
    												(boundingBox[0]+boundingBox[3])/2,
    												(boundingBox[1]+boundingBox[4])/2,
    												(boundingBox[2]+boundingBox[5])/2,
    												};
            								
            								
            								// Set a bunch of variables to be used to make the book
            								bookType = bookTypes.get(i);
            								structureTitle = structureTitles.get(i);
            								dimensionName = dimensionNames.get(i);
            								String structureType = structureTypes.get(i);
            								
            								// If you're in a Temple, figure out which kind specifically
            								
            								try {
                					            if (structureType.equals("Temple")) {
                					            	
                					            	Biome biomeYoureIn = world.getBiomeGenForCoords(new BlockPos(MathHelper.floor_double(player.posX), 0, MathHelper.floor_double(player.posZ)));
                					            	
                					            	if (
                					            			biomeYoureIn == Biomes.JUNGLE || 
                					            			biomeYoureIn == Biomes.JUNGLE_HILLS ||
                					            			biomeYoureIn == Biomes.JUNGLE_EDGE ||
                					            			biomeYoureIn == Biomes.MUTATED_JUNGLE ||
                					            			biomeYoureIn == Biomes.MUTATED_JUNGLE_EDGE
                					            			) {
                					            		structureType = "JungleTemple";
                					            		structureTitle = "Jungle Temple";
                					            		bookType = "jungletemple";
                					            	}
                					            	else if (
                					            			biomeYoureIn == Biomes.DESERT ||
                					            			biomeYoureIn == Biomes.DESERT_HILLS ||
                					            			biomeYoureIn == Biomes.MUTATED_DESERT
                					            			) {
                					            		structureType = "DesertPyramid";
                					            		structureTitle = "Desert Pyramid";
                					            		bookType = "desertpyramid";
                					            	}
                					            	else if (
                					            			biomeYoureIn == Biomes.SWAMPLAND ||
                					            			biomeYoureIn == Biomes.MUTATED_SWAMPLAND
                					            			) {
                					            		structureType = "SwampHut";
                					            		structureTitle = "Swamp Hut";
                					            		bookType = "swamphut";
                					            	}
                					            	else if (
                					            			biomeYoureIn == Biomes.ICE_PLAINS ||
                					            			biomeYoureIn == Biomes.COLD_TAIGA ||
                					            			biomeYoureIn == Biomes.ICE_MOUNTAINS ||
                					            			biomeYoureIn == Biomes.MUTATED_ICE_FLATS ||
                					            			biomeYoureIn == Biomes.COLD_BEACH ||
                					            			biomeYoureIn == Biomes.COLD_TAIGA_HILLS ||
                					            			biomeYoureIn == Biomes.MUTATED_TAIGA_COLD
                					            			) {
                					            		structureType = "Igloo";
                					            		structureTitle = "Igloo";
                					            		bookType = "igloo";
                					            	}
                					            }
                					            
            								}
            								catch (Exception e) {} // Something went wrong, so it'll just use the default "temple" stuff.
    										
            								
            								
            								if (structureInfoArray[0]==null && structureInfoArray[1]==null && structureInfoArray[2]==null) {
            									//Structure has no name. Generate it here.
            									
            									//forWorld(World world, String key, String toptagIn)
            									VNWorldDataStructure data = VNWorldDataStructure.forWorld(world, "villagenames3_" + structureTypes.get(i), "NamedStructures");
            									
        										structureInfoArray = NameGenerator.newRandomName(nameTypes.get(i));
        										
        										
        										// Changed color block in v3.1banner
        	                        			// Generate banner info, regardless of if we make a banner.
        	                            		Object[] newRandomBanner = BannerGenerator.randomBannerArrays(random, -1);
        	                    				ArrayList<String> patternArray = (ArrayList<String>) newRandomBanner[0];
        	                    				ArrayList<Integer> colorArray = (ArrayList<Integer>) newRandomBanner[1];
        	                    				ItemStack villageBanner = BannerGenerator.makeBanner(patternArray, colorArray);
        	                            		int townColorMeta = 15-colorArray.get(0);
        	                            		
        										
        										headerTags = structureInfoArray[0];
        										namePrefix = structureInfoArray[1];
        										nameRoot = structureInfoArray[2];
        										nameSuffix = structureInfoArray[3];
        										
        										// Make the data bundle to save to NBT
        										NBTTagList nbttaglist = new NBTTagList();
        										
        										NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        										signX = structureCoords[0];
        										signY = structureCoords[1];
        										signZ = structureCoords[2];
        										nbttagcompound1.setInteger("signX", signX);
        										nbttagcompound1.setInteger("signY", signY);
        										nbttagcompound1.setInteger("signZ", signZ);
        										nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
        										nbttagcompound1.setString("namePrefix", namePrefix);
        										nbttagcompound1.setString("nameRoot", nameRoot);
        										nbttagcompound1.setString("nameSuffix", nameSuffix);
        										nbttagcompound1.setBoolean("fromCodex", true);
        										if (!structureType.equals(structureTypes.get(i)) ) nbttagcompound1.setString("templeType", bookType);

        										// Added in v3.1banner
                                                // Form and append banner info
                                                nbttagcompound1.setTag("BlockEntityTag", BannerGenerator.getNBTFromBanner(villageBanner));
        										
        										nbttaglist.appendTag(nbttagcompound1);
        										
        										// .getTagList() will return all the entries under the specific village name.
        										//NBTTagCompound tagCompound = data.getData();
        										
        										data.getData().setTag((namePrefix + " " + nameRoot + " " + nameSuffix).trim() + ", x" + signX + " y" + signY + " z" + signZ, nbttaglist);
        										data.markDirty();
            									
            									structureName = structureInfoArray[1]+" "+structureInfoArray[2]+" "+structureInfoArray[3];
            									structureName = structureName.trim();
            								}
            								else {
            									//Structure has a name. Unpack it here.
            									structureName = structureInfoArray[0]+" "+structureInfoArray[1]+" "+structureInfoArray[2];
            									structureName = structureName.trim();
            								}
            								
            								
            								
            								// --- Archaeologist Achievement --- //
            								
            								try { // I'm enclosing this whole thing in a try/catch just in case casting to EntityPlayerMP fails
            									EntityPlayerMP playerMP = (EntityPlayerMP)player;
                								
            									// Get list of structures that the player used a Codex in
                					            JsonSerializableSet jsonserializableset = (JsonSerializableSet)playerMP.getStatFile().getProgress(VillageNames.archaeologist);

                					            if (jsonserializableset == null) {
                					            	// Load in the player statistics file and make a new JsonSerializableList
                					                jsonserializableset = (JsonSerializableSet)playerMP.getStatFile().setProgress(VillageNames.archaeologist, new JsonSerializableSet());
                					            }
                					            
                					            // Add the structure type you searched into the list if it's not there already
                					            jsonserializableset.add(structureType);
                					            
                					            // Now check the size of that thar list!
                					            if (jsonserializableset.size() >= VillageNames.numberStructuresArchaeologist) {
                					            	
                					            	// Give the player the achievement if they do not have it already
                					            	if ( !playerMP.getStatFile().hasAchievementUnlocked(VillageNames.archaeologist) ) {
                					            		//player.triggerAchievement(VillageNames.archaeologist);
                					            		player.addStat(VillageNames.archaeologist);
                					            		AchievementReward.allFiveAchievements(playerMP);
                					            	}
                					            }
                					            
            								}
            								catch (Exception e) {} // Any of the above was illegal; probably the casting?
            								
            					            
            								// Since a valid structure was found, you can terminate the search here.
            								break structureLoop;
            							}
            						}
        							
        							
        						}
        						catch (Exception e) {
        							// There's a tag like [23,-3] (chunk location) but there's no bounding box tag.
        						}
        					}
        				}
    				}
    				catch (Exception e) {
    				}
    			}

    			
    			// Don't make a book if the sign coords are all -1.
    			
    			if (signX!=-1 && signY!=-1 && signZ!=-1) {
    				
    				// Generate a book using my brand-new handy dandy method
        			WriteBookHandler.codexWriteNewVillageBook (
        					bookType, player.getDisplayName().getUnformattedText(), // player.getDisplayName() is an IChatComponent in 1.8
        					signX, signY, signZ,
        					structureTitle, dimensionName,
        					namePrefix, nameRoot, nameSuffix,
        					player, world.villageCollectionObj.getNearestVillage(
        							new BlockPos(MathHelper.floor_double(player.posX),
        							MathHelper.floor_double(player.posY),
        							MathHelper.floor_double(player.posZ)),
        							EntityInteractHandler.villageRadiusBuffer), playerIsInVillage
        					);
    				}
    		}
    	}
    	
		//return itemStack;
		return new ActionResult(EnumActionResult.PASS, itemStack);
	}
        
    
}