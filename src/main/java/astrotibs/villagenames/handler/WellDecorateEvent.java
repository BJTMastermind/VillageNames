package astrotibs.villagenames.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import astrotibs.villagenames.banner.BannerGenerator;
import astrotibs.villagenames.block.ModBlocksVN;
import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.nbt.VNWorldDataStructure;
import astrotibs.villagenames.utility.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureData;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Adapted from BetterVillages by GoToLink:
 * https://github.com/GotoLink/BetterVillages/blob/aae028988da827d9cfd1b98b44b42ba9802ab9a2/bettervillages/BetterVillages.java
 * @author AstroTibs
 */
public class WellDecorateEvent {
	
	Random random = new Random(); // Enabled in v3.2.1 - To ensure simultaneous villages don't get the same name
	
	@SubscribeEvent
	public void onPopulating(PopulateChunkEvent.Post event) {
		
		if (
				event.isHasVillageGenerated() && event.getWorld().provider.getDimension() == 0
				&& !event.getWorld().isRemote
				) {
			
			// v3.1.2 - Removed config pre-load values to be more human readable 
			
			int i = (event.getChunkX() << 4) + 8;//Villages are offset
			int k = (event.getChunkZ() << 4) + 8;
			int y;
	        Block id;
	        Block id2;
	        Block id3;
			int[] field;
			int[] field2;
			int[] field3;
			List<int[]> listOpaque;
			List<int[]> listWater;
			List<int[]> listWater2;
			List<int[]> listWater3;

			
			
            // Re-worked in v3.1banner
			int inwardYaw = GeneralConfig.villageBanners ? GeneralConfig.signYaw : 4; // Value from 0 to 4 indicating how inward the signs are oriented. 0 is away from the well. 4 has the sign and banner facing each other.
			int signLocation = random.nextInt(4); // One of the four corners             // 0=NW, 1=NE, 2=SE, 3=SW
            int signXOffset = (1-Math.abs((signLocation+1)/2-1)*2)*2;                    // NW: -2, NE: 2, SE: 2, SW: -2
            int signZOffset = ((signLocation/2)*2-1)*2;                                  // NW: -2, NE: -2, SE: 2, SW: 2
            int signHeightOffGround = 2;
            Block signBase = Blocks.COBBLESTONE;
            // The banner is at an adjacent corner. If Village Banners is turned off, set its location to the same as the Sign (this serves a function)
            int bannerLocation = GeneralConfig.villageBanners ? (signLocation + (random.nextBoolean() ? 1 : -1) + 4)%4 : signLocation;
            int bannerXOffset = (1-Math.abs((bannerLocation+1)/2-1)*2)*2;                // NW: -2, NE: 2, SE: 2, SW: -2
            int bannerZOffset = ((bannerLocation/2)*2-1)*2;                              // NW: -2, NE: -2, SE: 2, SW: 2
            
            // Set the banner and sign orientation based off of the position of both
            int signOrientation=0;
            int bannerOrientation=0;
            
            switch (signLocation)
            {
            	case 0: // Sign is in the Northwest
            		switch (bannerLocation)
            		{
	            		case 1: // Banner is in the Northeast
	            			signOrientation = 8+inwardYaw; // Turn sign to the northeast
	            			bannerOrientation = 8-inwardYaw; // Turn banner to the northwest
	            			bannerXOffset++; // Move banner one more block to the east
	            			break;
	            		case 3: // Banner is in the Southwest
	            			signOrientation = 4-inwardYaw; // Turn sign to the southwest
	            			bannerOrientation = 4+inwardYaw; // Turn banner to the northwest
	            			bannerZOffset++; // Move banner one more block to the south
	            			break;
	            		default:
	            			// Banner is unused. Point the sign inward at a full 90 degrees
	            			signOrientation = random.nextBoolean() ? 8+inwardYaw : 4-inwardYaw;
	                        bannerOrientation=0;
	                        break;
            		}
            		break;
            		
            	case 1: // Sign is in the Northeast
            		switch (bannerLocation)
            		{
	            		case 0: // Banner is in the Northwest
	            			signOrientation = 8-inwardYaw; // Turn sign to the northwest
	            			bannerOrientation = 8+inwardYaw; // Turn banner to the northeast
	            			bannerXOffset--; // Move banner one more block to the west
	            			break;
	            		case 2: // Banner is in the Southeast
	            			signOrientation = 12+inwardYaw; // Turn sign to the southeast
	            			bannerOrientation = 12-inwardYaw; // Turn banner to the northeast
	            			bannerZOffset++; // Move banner one more block to the south
	            			break;
	            		default: 
	            			// Banner is unused. Point the sign inward at a full 90 degrees
	            			signOrientation = random.nextBoolean() ? 8-inwardYaw : 12+inwardYaw;
	                        bannerOrientation=0;
	                        break;
            		}
            		break;
            		
            	case 2: // Sign is in the Southeast
            		switch (bannerLocation)
            		{
	            		case 1: // Banner is in the Northeast
	            			signOrientation =  12-inwardYaw; // Turn sign to the northeast
	            			bannerOrientation = 12+inwardYaw; // Turn banner to the southeast
	            			bannerZOffset--; // Move banner one more block to the north
	            			break;
	            		case 3: // Banner is in the Southwest
	            			signOrientation = 0+inwardYaw; // Turn sign to the southwest
	            			bannerOrientation = 0-inwardYaw; // Turn banner to the southeast
	            			bannerXOffset--; // Move banner one more block to the west
	            			break;
	            		default:
	            			// Banner is unused. Point the sign inward at a full 90 degrees
	            			signOrientation = random.nextBoolean() ? 12-inwardYaw : 0+inwardYaw;
	                        bannerOrientation=0;
	                        break;
            		}
            		break;
            		
            	case 3: // Sign is in the Southwest
            		switch (bannerLocation)
            		{
	            		case 0: // Banner is in the Northwest
	            			signOrientation = 4+inwardYaw; // Turn sign to the northwest
	            			bannerOrientation = 4-inwardYaw; // Turn banner to the southwest
	            			bannerZOffset--; // Move banner one more block to the north
	            			break;
	            		case 2: // Banner is in the Southeast
	            			signOrientation = 0-inwardYaw; // Turn sign to the southeast
	            			bannerOrientation = 0+inwardYaw; // Turn banner to the southwest
	            			bannerXOffset++; // Move banner one more block to the east
	            			break;
	            		default: 
	            			// Banner is unused. Point the sign inward at a full 90 degrees
	            			signOrientation = random.nextBoolean() ? 4+inwardYaw : 0-inwardYaw;
	                        bannerOrientation=0;
	                        break;
            		}
            		break;
            }
            // Make sure the orientations are between 0 and 15
            signOrientation = (signOrientation+16)%16;
            bannerOrientation = (bannerOrientation+16)%16;
            
            
            
            
            int isWellCorner = 0;
			for (int x = i; x < i + 16; x++) {
				for (int z = k; z < k + 16; z++) {//Search within chunk
					y = event.getWorld().getHeight(new BlockPos(x, 64, z)).getY();//.getHeightValue(x, z);//block on top of a "solid" block
					if (y > 1) {
						y--;
						id = event.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
						while (
								id.isAir( id.getBlockState().getBaseState(), event.getWorld(), new BlockPos(x, y, z))
								|| id.isLeaves( id.getBlockState().getBaseState(), event.getWorld(), new BlockPos(x, y, z))
								) {// (id.isAir(event.getWorld(), new BlockPos(x, y, z)) || id.isLeaves(event.getWorld(), new BlockPos(x, y, z))) {
							y--;
							id = event.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
						}
						World world = event.getWorld();
						BlockPos blockpos1 = new BlockPos(x, y - 4, z);
						BlockPos blockpos2 = new BlockPos(x, y - 5, z);
						BlockPos blockpos3 = new BlockPos(x, y - 6, z);
						// This part decorates wells!
						if (id.isNormalCube( id.getBlockState().getBaseState(), world, new BlockPos(x, y, z) ) ) {//found solid block in open air
							
							id = event.getWorld().getBlockState(new BlockPos(x, y - 4, z)).getBlock();
							id2 = event.getWorld().getBlockState(new BlockPos(x, y - 5, z)).getBlock();
							id3 = event.getWorld().getBlockState(new BlockPos(x, y - 6, z)).getBlock();
							if (isWaterId(world, blockpos1) && isWaterId(world, blockpos2) && isWaterId(world, blockpos3)) {//found water under solid block layer
                                y -= 4;
                                field = new int[]{x, y, z};
                                field2 = new int[]{x, y-1, z};
                                field3 = new int[]{x, y-2, z};
                                listWater = getBorder(event.getWorld(), id, field);
                                listWater2 = getBorder(event.getWorld(), id2, field2);
                                listWater3 = getBorder(event.getWorld(), id3, field3);
                                listOpaque = getOpaqueBorder(event.getWorld(), field);
                                if (listWater.size() == 3 && listWater2.size() == 3 && listWater3.size() == 3 && listOpaque.size() == 5) {//found 3 water blocks AND 5 opaque blocks surrounding one water block on THREE levels, assuming this is a village well
                                	
                                	// Now I need to get tricky with the sign generation.
                                	isWellCorner ++; //1=NW, 2=SW, 3=NE, 4=SE
                                	
                                	if (1-Math.abs(isWellCorner/2-1)+((isWellCorner-1)%2)*2 == signLocation) {
                                		
                                		// Call the name generator here
                                		String[] newVillageName = NameGenerator.newRandomName("Village");
                                		String headerTags = newVillageName[0];
                                		String namePrefix = newVillageName[1];
                                		String nameRoot = newVillageName[2];
                                		String nameSuffix = newVillageName[3];
                                		
                                		//This ensures that a sign won't generate in a well post
                                		Block blockSignPlaceholder = event.getWorld().getBlockState( new BlockPos(x+signXOffset, y+2, z+signZOffset) ).getBlock();
                                		if ( blockSignPlaceholder.isAir( blockSignPlaceholder.getBlockState().getBaseState(), event.getWorld(), new BlockPos(x+signXOffset, y+2, z+signZOffset)) ||
                                			 blockSignPlaceholder.isLeaves( blockSignPlaceholder.getBlockState().getBaseState(), event.getWorld(), new BlockPos(x+signXOffset, y+2, z+signZOffset))
                                			 ) {
                                			
                                			// Set the sign now!

                                			
                                			// Changed color block in v3.1banner
                                			// Generate banner info, regardless of if we make a banner.
                                    		Object[] newRandomBanner = BannerGenerator.randomBannerArrays(event.getWorld().rand, -1);
                            				ArrayList<String> patternArray = (ArrayList<String>) newRandomBanner[0];
                            				ArrayList<Integer> colorArray = (ArrayList<Integer>) newRandomBanner[1];
                            				
                            				ItemStack villageBanner = BannerGenerator.makeBanner(patternArray, colorArray);
                                    		int townColorMeta = 15-colorArray.get(0);
                                    		
                                			
                                    		// ---------------------------------------- //
                                    		// -------- Determine Village Size -------- //
                                    		// ---------------------------------------- //
                                    		
                                    		// In this section, I determine how big the village in generation will be
                                    		// and use that information to inform the village sign.
                                    		
                                    		int signX = (x+signXOffset);
                                    		int signY = y+2;
                                    		int signZ = (z+signZOffset);
                                    		int villageArea = -1; // If a village area value is not ascertained, this will remain as -1.

                                    		// Updated in v3.2.1 to allow for Open Terrain Generation compatibility
                                    		
                                    		MapGenStructureData structureData;
                                    		NBTTagCompound nbttagcompound = null;

                                    		try
                                    		{
                                    			structureData = (MapGenStructureData)event.getWorld().getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "Village");
                                    			nbttagcompound = structureData.getTagCompound();
                                    		}
                                    		catch (Exception e) // Village.dat does not exist
                                    		{
                                    			try
                                        		{
                                        			structureData = (MapGenStructureData)event.getWorld().getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "OTGVillage");
                                        			nbttagcompound = structureData.getTagCompound();
                                        		}
                                        		catch (Exception e1) {} // OTGVillage.dat does not exist
                                    		}

                                    		// v3.2.1 - At this point, you may or may not have data to work with.

                                    		try
                                    		{
                                    			Iterator itr0 = nbttagcompound.getKeySet().iterator();
                                				
                                				while (itr0.hasNext()) { // Go through every village in the Village.nbt list
                                					Object element0 = itr0.next();
                                					
                                					NBTBase nbtbase = nbttagcompound.getTag(element0.toString());
                                					
                                					if (nbtbase.getId() == 10) {
                                						
                                						try {
                                							NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbtbase;
                                    						
                                							int[] boundingBox = nbttagcompound2.getIntArray("BB");
                                							// Check to see if the well is inside the village
                                							if (
                            									   signX >= boundingBox[0]
                            									&& signY >= boundingBox[1]
                            									&& signZ >= boundingBox[2]
                            									&& signX <= boundingBox[3]
                            									&& signY <= boundingBox[4]
                            									&& signZ <= boundingBox[5]
                                								) {
                                								
                                								// Player is inside bounding box.
                                								int ChunkX = nbttagcompound2.getInteger("ChunkX");
                                								int ChunkZ = nbttagcompound2.getInteger("ChunkZ");
                                								villageArea = (boundingBox[3]-boundingBox[0])*(boundingBox[3]-boundingBox[0]);
                                								
                                								if (GeneralConfig.debugMessages) {
                                									LogHelper.info("Village located at ChunkX: " + ChunkX + ", ChunkZ: " + ChunkZ 
                                										+ " with area " + villageArea
                                										);
                                									}

                                								// Update sign coordinates so it's central to the BB
                                								signX = (boundingBox[0] + boundingBox[3])/2;
                                								signY = (boundingBox[1] + boundingBox[4])/2;
                                								signZ = (boundingBox[2] + boundingBox[5])/2;
                                								
                                								}
    	                            						}
    	                            						catch (Exception e) {
    	                            							if (GeneralConfig.debugMessages) {
    	                        									LogHelper.warn("Village bounding box could not be determined.");
    	                            						}
    	                            					}
                                					}
                                				}
                                    		}
                                    		catch (Exception e) {} // Failed to evaluate the bounding box

                                    		// Added in 3.0
                                    		// Assign the top line of the sign based on the area of the Village, determined by bounding box
                                    		
                            				int topLineRand = random.nextInt(4);
                            				
                            				String topLine = "Welcome to"; // The top line will be blank or "welcome" for rand is 0 or 1
                            				
                            				if (topLineRand > 1) { // Half the time (rand is 2 or 3): actually make the top line size-dependent!
                            					
                            					// Each step down in area is a factor of 2.4
                            					
	                            					     if (villageArea > 12000) {topLine = "City" + ((topLineRand%2)==0 ? ":" : " of");}
	                            					else if (villageArea > 5000) {topLine = "Town" + ((topLineRand%2)==0 ? ":" : " of");}
	                            					else if (villageArea > 2083) {topLine = "Village" + ((topLineRand%2)==0 ? ":" : " of");}
	                            					else if (villageArea > 868) {topLine = "Hamlet" + ((topLineRand%2)==0 ? ":" : " of");}
	                            					else if (villageArea > 0) {topLine = "Outpost" + ((topLineRand%2)==0 ? ":" : " of");}
	                            					     // If none of the above are true, then the size could not be ascertained,
	                            					     // so it defaults to either a blank first line or "Welcome to."
	                            				}
                            				
                                    		topLine = topLine.replaceAll("\\^", " ");
											
                                    		VNWorldDataStructure data = VNWorldDataStructure.forWorld(event.getWorld(), "villagenames3_Village", "NamedStructures");
                                    		
                                    		// Added in 1.1
                                    		// This checks to see if the village has already been named
                                    		
                                    		NBTTagCompound tagCompound = data.getData();
                                    		
                                    		Set tagmapKeyset = tagCompound.getKeySet(); //Gets the town key list: "coordinates"
                                    		
                                    		Iterator itr = tagmapKeyset.iterator();
                                    		String townSignEntry;
                                    		
                                    		while(itr.hasNext()) {
                                    			Object element = itr.next();
                                    			
                                    			townSignEntry = element.toString(); //Text name of village header (e.g. "x535y80z39")
                                    			//The only index that has data is 0:
                                    			NBTTagCompound tagList = tagCompound.getTagList(townSignEntry, tagCompound.getId()).getCompoundTagAt(0);
                                    			
                                    			int townX = tagList.getInteger("signX");
                                    			int townY = tagList.getInteger("signY");
                                    			int townZ = tagList.getInteger("signZ");
                                    			
                                    			int radiussearch = 32;
                                    			if ( (signX-townX)*(signX-townX) + (signY-townY)*(signY-townY) + (signZ-townZ)*(signZ-townZ) <= radiussearch*radiussearch ) {
                                    				// This village already has a name.
                                    				townColorMeta = tagList.getInteger("townColor");
                                    				namePrefix = tagList.getString("namePrefix");
                                    				nameRoot = tagList.getString("nameRoot");
                                    				nameSuffix = tagList.getString("nameSuffix");
                                    				break;
                                    			}
                                    			
                                    		}
                                    		                                    		
                                    		if (GeneralConfig.wellDecorations && GeneralConfig.nameSign) {
                                        		// Cobblestone wall pole and sign proper
                                    			event.getWorld().setBlockState(new BlockPos(x+signXOffset, y+1, z+signZOffset), Blocks.COBBLESTONE_WALL.getDefaultState() );
                                    			event.getWorld().setBlockState(new BlockPos(x+signXOffset, y+2, z+signZOffset), Blocks.STANDING_SIGN.getStateFromMeta(signOrientation) );
                                    		}

                                    		// Added in v3.1banner
                                    		if (GeneralConfig.wellDecorations && GeneralConfig.villageBanners && signLocation!=bannerLocation) {
                                    			
                                    			BlockPos bannerBasePos = new BlockPos(x+bannerXOffset, y+1, z+bannerZOffset);
                                    			
                                    			event.getWorld().setBlockState(bannerBasePos, Blocks.COBBLESTONE_WALL.getDefaultState() );
                                    			event.getWorld().setBlockState(bannerBasePos.up(), Blocks.STANDING_BANNER.getStateFromMeta(bannerOrientation) );
                                    			
                                    			TileEntity tilebanner = new TileEntityBanner();
                                    			((TileEntityBanner) tilebanner).setItemValues(villageBanner);
                                        		event.getWorld().setTileEntity(bannerBasePos.up(), tilebanner);
                                    		}
                                    		
            		    			        if (GeneralConfig.wellDecorations && GeneralConfig.wellBoundary) {
            		    			        	
                                    			// Clay base OR concrete
                                    			if (GeneralConfig.addConcrete && GeneralConfig.concreteWell) {
                                    				
                                    				// v3.1.2 - Moved inside this if condition to pre-empt crashes
                                    				Block concreteBlock = ModBlocksVN.CONCRETE;
                                        			
                                        			// Block used for the well roof
                                        			Block roofGlazedBlock = ModBlocksVN.WHITE_GLAZED_TERRACOTTA;
                                        			switch (townColorMeta) {
    	                                    			// Case 0 is handled by default
    	                                    			case 1: roofGlazedBlock=ModBlocksVN.ORANGE_GLAZED_TERRACOTTA; break;
    	                                    			case 2: roofGlazedBlock=ModBlocksVN.MAGENTA_GLAZED_TERRACOTTA; break;
    	                                    			case 3: roofGlazedBlock=ModBlocksVN.LIGHT_BLUE_GLAZED_TERRACOTTA; break;
    	                                    			case 4: roofGlazedBlock=ModBlocksVN.YELLOW_GLAZED_TERRACOTTA; break;
    	                                    			case 5: roofGlazedBlock=ModBlocksVN.LIME_GLAZED_TERRACOTTA; break;
    	                                    			case 6: roofGlazedBlock=ModBlocksVN.PINK_GLAZED_TERRACOTTA; break;
    	                                    			case 7: roofGlazedBlock=ModBlocksVN.GRAY_GLAZED_TERRACOTTA; break;
    	                                    			case 8: roofGlazedBlock=ModBlocksVN.SILVER_GLAZED_TERRACOTTA; break;
    	                                    			case 9: roofGlazedBlock=ModBlocksVN.CYAN_GLAZED_TERRACOTTA; break;
    	                                    			case 10: roofGlazedBlock=ModBlocksVN.PURPLE_GLAZED_TERRACOTTA; break;
    	                                    			case 11: roofGlazedBlock=ModBlocksVN.BLUE_GLAZED_TERRACOTTA; break;
    	                                    			case 12: roofGlazedBlock=ModBlocksVN.BROWN_GLAZED_TERRACOTTA; break;
    	                                    			case 13: roofGlazedBlock=ModBlocksVN.GREEN_GLAZED_TERRACOTTA; break;
    	                                    			case 14: roofGlazedBlock=ModBlocksVN.RED_GLAZED_TERRACOTTA; break;
    	                                    			case 15: roofGlazedBlock=ModBlocksVN.BLACK_GLAZED_TERRACOTTA; break;
                                        			}
                                        			
                                    				// Generate new-style concrete
                                    				for (int pedY = y-3; pedY <= y; pedY++) {
                                    					for (int rimi = 2; rimi > -3; rimi--) {
                                    						// This builds the rim around the well
                                    						event.getWorld().setBlockState(new BlockPos(x+(signXOffset/2*rimi), pedY, z+signZOffset), concreteBlock.getStateFromMeta(townColorMeta) );
                                    						event.getWorld().setBlockState(new BlockPos(x+signXOffset, pedY, z+(-signZOffset/2*(1+rimi))), concreteBlock.getStateFromMeta(townColorMeta) );
                                    						event.getWorld().setBlockState(new BlockPos(x-(signXOffset*3/2), pedY, z+(signZOffset/2*rimi)), concreteBlock.getStateFromMeta(townColorMeta) );
                                    						event.getWorld().setBlockState(new BlockPos(x+(-signXOffset/2*(1+rimi)), pedY, z-(signZOffset*3/2)), concreteBlock.getStateFromMeta(townColorMeta) );
                                    					}
                                    				}
                                    				
                                    				// Set glazed terracotta in well roof!
                                    				int metaSpin = random.nextInt(4)+4; // I've got to add 4 because modulo doesn't work properly with negative numbers :P
                                    				int metaChirality = random.nextBoolean() ? 1 : -1;
                                    				
                                    				event.getWorld().setBlockState(new BlockPos(x, y+4, z), roofGlazedBlock.getStateFromMeta( (metaSpin)%4 ) ); // v3.1.2 - added modulo4 so that this matches the other three corners
                                    				event.getWorld().setBlockState(new BlockPos(x, y+4, z-(signZOffset/2)), roofGlazedBlock.getStateFromMeta( (metaSpin + metaChirality)%4 ) );
                                    				event.getWorld().setBlockState(new BlockPos(x-(signXOffset/2), y+4, z-(signZOffset/2)), roofGlazedBlock.getStateFromMeta( (metaSpin + metaChirality*2)%4) );
                                    				event.getWorld().setBlockState(new BlockPos(x-(signXOffset/2), y+4, z), roofGlazedBlock.getStateFromMeta( (metaSpin + metaChirality*3)%4 ) );
                                    				
                                    			}
                                    			else {
                                    				// Generate old-style clay base
                                    				for (int pedY = y-3; pedY <= y; pedY++) {
                                    					event.getWorld().setBlockState( new BlockPos(x+signXOffset,pedY,z+signZOffset), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(townColorMeta) );	
                                    				}
                                    			}
                                    			
                                    		}
                                    		
                                    		// Okay now that the three components have been generated
                                    		// I need to find a way to fit them onto a sign.
                                    		TileEntitySign signContents = new TileEntitySign();
                                    		
                                    		if ( (namePrefix.length() + 1 + nameRoot.length()) > 15 ) {
                                    			// Prefix+Root is too long, so move prefix to line 1
                                    			signContents.signText[0] = new TextComponentString(headerTags+ topLine.trim());
                                    			signContents.signText[1] = new TextComponentString(namePrefix.trim());
                                    			if ( (nameRoot.length() + 1 + nameSuffix.length()) > 15 ) {
                                    				// Root+Suffix is too long, so move suffix to line 3
                                    				signContents.signText[2] = new TextComponentString(nameRoot.trim());
                                    				signContents.signText[3] = new TextComponentString(nameSuffix.trim());
                                    			}
                                    			else {
                                    				// Fit Root+Suffix onto line 2
                                    				signContents.signText[2] = new TextComponentString((nameRoot+" "+nameSuffix).trim());
                                    			}
                                    		}
                                    		else if ( (namePrefix.length() + 1 + nameRoot.length() + 1 + nameSuffix.length()) <= 15 ) {
                                    			// Whole name fits on one line! Put it all on line 2.
                                    			signContents.signText[1] = new TextComponentString(headerTags+ topLine);
                                    			signContents.signText[2] = new TextComponentString((namePrefix+" "+nameRoot+" "+nameSuffix).trim());
                                    		}
                                    		else {
                                    			// Only Prefix and Root can fit together on line 2.
                                    			signContents.signText[1] = new TextComponentString(headerTags+ topLine.trim());
                                    			signContents.signText[2] = new TextComponentString((namePrefix+" "+nameRoot).trim());
                                    			signContents.signText[3] = new TextComponentString(nameSuffix.trim());
                                    		}
                                    		// If top line is blank, roll everything up one line:
                                    		if (topLine.equals("")) {
                                    			for (int isign=0; isign <3; isign++) {
                                    				signContents.signText[isign] = signContents.signText[isign+1];	
                                    			}
                                    			signContents.signText[3] = new TextComponentString("");
                                    		}
                                    		
                                    		// Put the stuff on the sign.
                                    		if (GeneralConfig.wellDecorations && GeneralConfig.nameSign) event.getWorld().setTileEntity(new BlockPos(x+signXOffset, y+2, z+signZOffset), signContents);
                                    		                                    		
                                    		// Make the data bundle to save to NBT
                                    		NBTTagList nbttaglist = new NBTTagList();
                                    		
                                    		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                                            nbttagcompound1.setInteger("signX", signX);
                                            nbttagcompound1.setInteger("signY", signY);
                                            nbttagcompound1.setInteger("signZ", signZ);
                                            nbttagcompound1.setInteger("townColor", townColorMeta); //In case we want to make clay, carpet, wool, glass, etc
                                            nbttagcompound1.setString("namePrefix", namePrefix);
                                            nbttagcompound1.setString("nameRoot", nameRoot);
                                            nbttagcompound1.setString("nameSuffix", nameSuffix);
                                            nbttagcompound1.setString("sign0", signContents.signText[0].getFormattedText());
                                            nbttagcompound1.setString("sign1", signContents.signText[1].getFormattedText());
                                            nbttagcompound1.setString("sign2", signContents.signText[2].getFormattedText());
                                            nbttagcompound1.setString("sign3", signContents.signText[3].getFormattedText());
                                            
                                            // Added in v3.1banner
                                            // Form and append banner info
                                            nbttagcompound1.setTag("BlockEntityTag", BannerGenerator.getNBTFromBanner(villageBanner));
                                            
                                            nbttaglist.appendTag(nbttagcompound1);
                                            // Save the data under a "Villages" entry with unique name based on sign coords
                                            data.getData().setTag((namePrefix + " " + nameRoot + " " + nameSuffix).trim() + ", x" + signX + " y" + signY + " z" + signZ, nbttaglist);
                                    		data.markDirty();
                                		}
                                		else { //The stupid thing is generating a sign inside the well structure for some reason.
                                			
                                			int signX = (x+signXOffset);
                                    		int signY = y+2;
                                    		int signZ = (z+signZOffset);
                                    		LogHelper.error("Tried to generate a sign inside a well's post at x="+signX+" y="+signY+" z="+signZ);
                                		}
                                		
                                	}
                                	
                                	if (GeneralConfig.wellDecorations && GeneralConfig.wellSlabs) {
                                		field = listOpaque.remove(1);
                                    	event.getWorld().setBlockState(new BlockPos(field[0], field[1] + 1, field[2]), Blocks.STONE_SLAB.getDefaultState() );
                                    	field = listOpaque.remove(2);
                                        event.getWorld().setBlockState(new BlockPos(field[0], field[1] + 1, field[2]), Blocks.STONE_SLAB.getDefaultState() );
                                	}
                                    
                                    while (event.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock() == id) {
                                        y--;
                                    }
                                    field = new int[]{x, y, z};
                                    listOpaque = getOpaqueBorder(event.getWorld(), field);
                                    
                                }
                            }
                            continue;
						}
					}
				}
			}
		}
		
		
		
		// Piggybacking the mushroom replacer
		if (
				GeneralConfig.swampHutMushroomPot
				&& !event.getWorld().isRemote
				&& event.getWorld().provider.getDimension() == 0
				&& event.getWorld().getBiomeGenForCoords( new BlockPos(event.getChunkX() * 16 + 8, 0, event.getChunkZ() * 16 + 8) ) == Biomes.SWAMPLAND
				) {
			
			//Block blocktoscan;
			//int woolmeta;
			
			// Search for intersected temples and replace their wool with terracotta
			searchHutAndReplacePot(event, 8);
		}
		
		
		
	}
    
    /**
     *
     * @param world The world containing the blocks
     * @param id The block searched
     * @param x, y, z The coordinates of the center to search around
     * @return true if all faces of the center are attached to the searched block
     */
    private static boolean hasAround(World world, Block id, int x, int y, int z){
        return world.getBlockState(new BlockPos(x-1, y, z)).getBlock() == id && 
        		world.getBlockState(new BlockPos(x+1, y, z)).getBlock() == id && 
        		world.getBlockState(new BlockPos(x, y, z-1)).getBlock() == id && 
        		world.getBlockState(new BlockPos(x, y, z+1)).getBlock() == id;
    }

    /**
     *
     * @param world The world containing the blocks
     * @param id The block searched
     * @param field The coordinates of the center to search around
     * @return A list of coordinates that contain the same block, around the center, at the same height
     */
	private static List<int[]> getBorder(World world, Block id, int[] field) {
		List<int[]> list = new ArrayList<int[]>();
		for (int x = field[0] - 1; x < field[0] + 2; x++) {
			for (int z = field[2] - 1; z < field[2] + 2; z++) {
				if ((x != field[0] || z != field[2]) && world.getBlockState(new BlockPos(x, field[1], z)).getBlock() == id)
					list.add(new int[] { x, field[1], z });
			}
		}
		return list;
	}


	/**
	*
	* @param world The world containing the blocks
	* @param id The block searched
	* @param field The coordinates of the center to search around
	* @return A list of coordinates that contain opaque cubes, around the center, at the same height
	*/
	private static List<int[]> getOpaqueBorder(World world, int[] field) {
		List<int[]> list = new ArrayList<int[]>();
		for (int x = field[0] - 1; x < field[0] + 2; x++) {
			for (int z = field[2] - 1; z < field[2] + 2; z++) {
				Block borderBlock = world.getBlockState( new BlockPos(x, field[1], z)).getBlock();
				if ((x != field[0] || z != field[2]) && borderBlock.isNormalCube(borderBlock.getBlockState().getBaseState(), world, new BlockPos(x, field[1], z)) )
					list.add(new int[] { x, field[1], z });
			}
		}
		return list;
	}
	
	
    /**
     * Roughly estimates if a center block is cornered by blocks of the given type.
     * Actually searching for two unaligned border blocks
     *
     * @param world The world containing the blocks
     * @param id The block searched
     * @param pos The coordinates of the center to search around
     * @return true if the center block is cornered by the block type
     */
	private static boolean isCorner(World world, Block id, int[] pos) {
		List<int[]> list = getBorder(world, id, pos);
		if (list.size() < 2)
			return false;
		int[] a = list.get(0);
		int[] b = list.get(1);
		return a[0] != b[0] && a[2] != b[2];
	}
		
    /**
     * Wrapper method to identify water type blocks.
     *
     * @param id The block to compare
     * @return true if the block material is water
     */
	private static boolean isWaterId(World worldIn, BlockPos blockpos) {
		return worldIn.getBlockState(blockpos).getMaterial() == Material.WATER;
	}

	
	
	/**
	 * Inputs PopulateChunkEvent.Post event, and determines whether the x, z in that event
	 * falls within the buffer-expanded bounding box, of ANY generated Temple structure.
	 * It returns the coordBaseMode integer, or -1 if no Temple was found.
	 */
	public void searchHutAndReplacePot(PopulateChunkEvent.Post event, int buffer)
    {
		MapGenStructureData structureData = null; // Made null in v3.2.1
		if (
				event.getWorld().provider.getDimension()==0
				&& !event.getWorld().isRemote
				) { // Player is in the Overworld
			try {

				// Updated in v3.2.1 to allow for Open Terrain Generation compatibility
        		NBTTagCompound nbttagcompound = null;

        		try
        		{
        			structureData = (MapGenStructureData)event.getWorld().getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "Temple");
        			nbttagcompound = structureData.getTagCompound();
        		}
        		catch (Exception e)
        		{
        			try
            		{
            			structureData = (MapGenStructureData)event.getWorld().getPerWorldStorage().getOrLoadData(MapGenStructureData.class, "OTGTemple");
            			nbttagcompound = structureData.getTagCompound();
            		}
            		catch (Exception e1) {}
        		}
				
				Iterator itr = nbttagcompound.getKeySet().iterator();
				while (itr.hasNext()) {
					Object element = itr.next();
					NBTBase nbtbase = nbttagcompound.getTag(element.toString());
					if (nbtbase.getId() == 10) {
						NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbtbase;
						try {
							int[] boundingBox = nbttagcompound2.getIntArray("BB");
							
							int xChunkCenter = (event.getChunkX()<<4);
							int zChunkCenter = (event.getChunkZ()<<4);
							
							// Now check to see if the spawn entity is inside the monument
							if (
								   xChunkCenter >= boundingBox[0]-buffer
								//&& event.y >= boundingBox[1]
								&& zChunkCenter >= boundingBox[2]-buffer
								&& xChunkCenter <= boundingBox[3]+buffer
								//&& event.y <= boundingBox[4]
								&& zChunkCenter <= boundingBox[5]+buffer
								) {
								// Event is inside bounding box.
								
								
						        // Replace wool
								Block blocktoscan;
								int potmeta;
								IBlockState iblockstate;
								BlockPos potPos;
								
								for (int k = 50; k <= 80; k++) {
									for (int i=-16; i < 16; i++) {
										for (int j=-16; j < 16; j++) {
											
											potPos = new BlockPos((event.getChunkX() << 4)+i, k, (event.getChunkZ() << 4)+j);
											
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
													
													if (GeneralConfig.debugMessages) LogHelper.info("Updating swamp hut mushroom pot at " + (boundingBox[0]+boundingBox[3])/2 + " " + (boundingBox[2]+boundingBox[5])/2);
													
													nbttagcompound2.setBoolean("VNMushroomPotFixed", true);
													structureData.setDirty(true);
													
													return;
													
												}
											}
										}
									}
								}
								
								
								
							}
						}
						catch (Exception e) {
							if (GeneralConfig.debugMessages) LogHelper.warn("Failed to evaluate Temple bounding box");
						}
					}
				}
			}
			catch (Exception e) { // This fails when the Monument list is empty (i.e. none have been generated).
			}
		}
		return;
    }
	
	
}
