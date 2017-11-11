package astrotibs.villagenames.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import astrotibs.villagenames.handler.ItemEventHandler;
import astrotibs.villagenames.integration.ModChecker;
import astrotibs.villagenames.name.NameGenerator;
import astrotibs.villagenames.nbt.VNWorldDataAbandonedBase;
import astrotibs.villagenames.nbt.VNWorldDataEndCity;
import astrotibs.villagenames.nbt.VNWorldDataEndIsland;
import astrotibs.villagenames.nbt.VNWorldDataEndTower;
import astrotibs.villagenames.nbt.VNWorldDataFortress;
import astrotibs.villagenames.nbt.VNWorldDataFronosVillage;
import astrotibs.villagenames.nbt.VNWorldDataKoentusVillage;
import astrotibs.villagenames.nbt.VNWorldDataMansion;
import astrotibs.villagenames.nbt.VNWorldDataMineshaft;
import astrotibs.villagenames.nbt.VNWorldDataMonument;
import astrotibs.villagenames.nbt.VNWorldDataMoonVillage;
import astrotibs.villagenames.nbt.VNWorldDataNibiruVillage;
import astrotibs.villagenames.nbt.VNWorldDataStronghold;
import astrotibs.villagenames.nbt.VNWorldDataTemple;
import astrotibs.villagenames.nbt.VNWorldDataVillage;
import astrotibs.villagenames.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
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
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    	ItemStack itemstack = player.getHeldItem(hand);
    	
    	try {
    		
        	if (!world.isRemote) {
        		
        		if (player.inventory.hasItemStack(new ItemStack(Items.BOOK))) {
        			// Now, check what dimension you're in
        			
        			MapGenStructureData structureData;
        			World worldIn = player.world;
        			int[ ] BB = new int[6];
        			
        			ArrayList<String> structureTypes = new ArrayList();
        			
        			if (player.dimension==0) {
        				// Player is in Overworld
        				//structureTypes.add("Mansion");
        				structureTypes.add("Monument");
        				structureTypes.add("Mineshaft");
        				structureTypes.add("Stronghold");
        				structureTypes.add("Temple");
        				structureTypes.add("Village");
        			}
        			else if (player.dimension==-1) {
        				// Player is in Nether
        				structureTypes.add("Fortress");
        			}
        			else if (player.dimension==1) {
        				// Player is in End
        				structureTypes.add("EndCity");
        				if (ModChecker.isHardcoreEnderExpansionLoaded) {
        					structureTypes.add("hardcoreenderdragon_EndTower");
        					structureTypes.add("hardcoreenderdragon_EndIsland");
        					}
        			}
        			else {
        				// Catch-all for other mod structures, since I can't be CERTAIN about dimension...
        				if (ModChecker.isGalacticraftLoaded) {
        					structureTypes.add("MoonVillage");
        				}
        				if (ModChecker.isMorePlanetsLoaded) {
        					structureTypes.add("KoentusVillage");
        				}
        				if (ModChecker.isMorePlanetsLoaded) {
        					structureTypes.add("FronosVillage");
        				}
        				if (ModChecker.isMorePlanetsLoaded) {
        					structureTypes.add("NibiruVillage");
        				}
        				if (ModChecker.isGalacticraftLoaded) {
        					structureTypes.add("GC_AbandonedBase");
        				}
        			}
        			
        			String headerTags = new String();
    				String namePrefix = new String();
    				String nameRoot = new String();
    				String nameSuffix = new String();
        			String structureType = new String();
        			int signX = -1;
        			int signY = -1;
        			int signZ = -1;
        			
        			structureLoop:
        			for (String s: structureTypes) {
        				structureType = s;
        				
        				try {
        					
            				structureData = (MapGenStructureData)worldIn.getPerWorldStorage().getOrLoadData(MapGenStructureData.class, s);
            				NBTTagCompound nbttagcompound = structureData.getTagCompound();
            				
            				Iterator itr = nbttagcompound.getKeySet().iterator();
            				
            				while (itr.hasNext()) {
            					Object element = itr.next();
            					
            					NBTBase nbtbase = nbttagcompound.getTag(element.toString());
            					
            					if (nbtbase.getId() == 10) {
            						NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbtbase;
            						
            						try {
            							int[] boundingBox = nbttagcompound2.getIntArray("BB");
            							// Now check to see if the player is inside the feature
            							/*
            							LogHelper.info(" ");
            							LogHelper.info("Structure: "+s);
            							LogHelper.info("element: "+element);
            							LogHelper.info("BB: "+boundingBox[0]+", "+boundingBox[1]+", "+boundingBox[2]);
            							LogHelper.info("BB: "+boundingBox[3]+", "+boundingBox[4]+", "+boundingBox[5]);
            							*/
            							if (
        									   player.posX >= boundingBox[0]
        									&& player.posY >= boundingBox[1]
        									&& player.posZ >= boundingBox[2]
        									&& player.posX <= boundingBox[3]
        									&& player.posY <= boundingBox[4]
        									&& player.posZ <= boundingBox[5]
            								) {
            								//LogHelper.info("You're inside, at x="+player.posX+", y="+player.posY+", z="+player.posZ);
            								// Player is inside bounding box.
            								int ChunkX = nbttagcompound2.getInteger("ChunkX");
            								int ChunkZ = nbttagcompound2.getInteger("ChunkZ");
            								//LogHelper.info("Chunk X/Z are "+ChunkX+", "+ChunkZ);
            								String structureName;
            								String[] structureInfoArray = ItemEventHandler.tryGetStructureInfo(s, boundingBox, worldIn);
            								//LogHelper.info("StructureInfoArray loaded");
            								namePrefix = structureInfoArray[0];
            								nameRoot = structureInfoArray[1];
            								nameSuffix = structureInfoArray[2];
            								//LogHelper.info("StructureInfoArray broken up");
            								// If none is found, these strings are "null" which parseInt does not like very much
            								try {signX = Integer.parseInt(structureInfoArray[3]);} catch (Exception e) {}
            								try {signY = Integer.parseInt(structureInfoArray[4]);} catch (Exception e) {}
            								try {signZ = Integer.parseInt(structureInfoArray[5]);} catch (Exception e) {}
            								//LogHelper.info("sign position attempted to be distributed");
            								// If a name was NOT returned, then we need to generate a new one, as is done below:
            								
            								int[] structureCoords = new int[] {
            										(boundingBox[0]+boundingBox[3])/2,
            										(boundingBox[1]+boundingBox[4])/2,
            										(boundingBox[2]+boundingBox[5])/2,
            										};
            								//LogHelper.info("Structure info: "+structureInfoArray[0]+", "+structureInfoArray[1]+", "+structureInfoArray[2]);
            								if (structureInfoArray[0]==null && structureInfoArray[1]==null && structureInfoArray[2]==null) {
            									//Structure has no name. Generate it here.
            									
            									if (s.equals("Village")) {
            										VNWorldDataVillage data = VNWorldDataVillage.forWorld(world);
            										structureInfoArray = NameGenerator.newVillageName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									else if (s.equals("Mineshaft")) {
            										VNWorldDataMineshaft data = VNWorldDataMineshaft.forWorld(world);
            										structureInfoArray = NameGenerator.newMineshaftName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									else if (s.equals("Stronghold")) {
            										VNWorldDataStronghold data = VNWorldDataStronghold.forWorld(world);
            										structureInfoArray = NameGenerator.newStrongholdName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									else if (s.equals("Temple")) {
            										VNWorldDataTemple data = VNWorldDataTemple.forWorld(world);
            										structureInfoArray = NameGenerator.newTempleName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									else if (s.equals("Fortress")) {
            										VNWorldDataFortress data = VNWorldDataFortress.forWorld(world);
            										structureInfoArray = NameGenerator.newFortressName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            									}
            									
            									else if (s.equals("Monument")) {
            										VNWorldDataMonument data = VNWorldDataMonument.forWorld(world);
            										structureInfoArray = NameGenerator.newMonumentName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									
            									
            									else if (s.equals("EndCity")) {
            										VNWorldDataEndCity data = VNWorldDataEndCity.forWorld(world);
            										structureInfoArray = NameGenerator.newEndCityName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									
            									
            									else if (s.equals("Mansion")) {
            										VNWorldDataMansion data = VNWorldDataMansion.forWorld(world);
            										structureInfoArray = NameGenerator.newMansionName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									
            									else if (s.equals("MoonVillage")) {
            										VNWorldDataMoonVillage data = VNWorldDataMoonVillage.forWorld(world);
            										structureInfoArray = NameGenerator.newAlienVillageName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									else if (s.equals("KoentusVillage")) {
            										VNWorldDataKoentusVillage data = VNWorldDataKoentusVillage.forWorld(world);
            										structureInfoArray = NameGenerator.newAlienVillageName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									else if (s.equals("hardcoreenderdragon_EndTower")) {
            										VNWorldDataEndTower data = VNWorldDataEndTower.forWorld(world);
            										structureInfoArray = NameGenerator.newEndCityName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									else if (s.equals("hardcoreenderdragon_EndIsland")) {
            										VNWorldDataEndIsland data = VNWorldDataEndIsland.forWorld(world);
            										structureInfoArray = NameGenerator.newEndCityName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									else if (s.equals("FronosVillage")) {
            										VNWorldDataFronosVillage data = VNWorldDataFronosVillage.forWorld(world);
            										structureInfoArray = NameGenerator.newAlienVillageName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            										
            									}
            									else if (s.equals("NibiruVillage")) {
            										VNWorldDataNibiruVillage data = VNWorldDataNibiruVillage.forWorld(world);
            										structureInfoArray = NameGenerator.newAlienVillageName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            									}
            									else if (s.equals("GC_AbandonedBase")) {
            										//LogHelper.info("This is triggering");
            										VNWorldDataAbandonedBase data = VNWorldDataAbandonedBase.forWorld(world);
            										structureInfoArray = NameGenerator.newAlienVillageName();
            										
            										// Gotta copy this thing to each IF condition I think
            										headerTags = structureInfoArray[0];
            										namePrefix = structureInfoArray[1];
            										nameRoot = structureInfoArray[2];
            										nameSuffix = structureInfoArray[3];
            										int townColorMeta = 15;
            										
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
            										nbttaglist.appendTag(nbttagcompound1);
            										
            										// .getTagList() will return all the entries under the specific village name.
            										NBTTagCompound tagCompound = data.getData();
            										
            										data.getData().setTag("x"+structureCoords[0]+"y"+structureCoords[1]+"z"+structureCoords[2]+"_fromcodex", nbttaglist);
            										data.markDirty();
            									}
            									
            									structureName = structureInfoArray[1]+" "+structureInfoArray[2]+" "+structureInfoArray[3];
            									structureName = structureName.trim();
            									
            								}
            								else {
            									//Structure has a name. Unpack it here.
            									structureName = structureInfoArray[0]+" "+structureInfoArray[1]+" "+structureInfoArray[2];
            									structureName = structureName.trim();
            								}
            								
            								break structureLoop;
            								
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
        			
        			// Actually form the book contents and write the book
        			
        			//Here are the contents of the book up front
        			String bookContents = "{\"text\":\""; //As of 1.9.4 I need to enclose book contents in {"text":"lorem ipsum"}
        			bookContents += "\n\u00a7r\u00a70";

        			// I don't care if the structure has a sign. We have to cut the name up into arbitrary sign strings for the book.
        			String topLine;

        			// These lines split top lines into two words
        			
        			if (structureType.equals("EndCity")) {topLine = "End City:";}
        			else if (structureType.equals("MoonVillage")) {topLine = "Moon Village:";}
        			else if (structureType.equals("KoentusVillage")) {topLine = "Koentus Village:";}
        			else if (structureType.equals("hardcoreenderdragon_EndIsland")) {topLine = "Island Fortress:";}
        			else if (structureType.equals("hardcoreenderdragon_EndTower")) {topLine = "End Tower:";}
        			else if (structureType.equals("FronosVillage")) {topLine = "Fronos Village:";}
        			else if (structureType.equals("NibiruVillage")) {topLine = "Nibiru Village:";}
        			else if (structureType.equals("GC_AbandonedBase")) {topLine = "Abandoned Base:";}
        			else {topLine = structureType+":";}
        			
        			String sign0 = new String();
        			String sign1 = new String();
        			String sign2 = new String();
        			String sign3 = new String();
        			
        			if ( (namePrefix.length() + 1 + nameRoot.length()) > 15 ) {
        				// Prefix+Root is too long, so move prefix to line 1
        				sign0 = headerTags+ topLine.trim();
        				sign1 = namePrefix.trim();
        				if ( (nameRoot.length() + 1 + nameSuffix.length()) > 15 ) {
        					// Root+Suffix is too long, so move suffix to line 3
        					sign2 = nameRoot.trim();
        					sign3 = nameSuffix.trim();
        				}
        				else {
        					// Fit Root+Suffix onto line 2
        					sign2 = (nameRoot+" "+nameSuffix).trim();
        				}
        			}
        			else if ( (namePrefix.length() + 1 + nameRoot.length() + 1 + nameSuffix.length()) <= 15 ) {
        				// Whole name fits on one line! Put it all on line 2.
        				sign1 = headerTags+ topLine;
        				sign2 = (namePrefix+" "+nameRoot+" "+nameSuffix).trim();
        			}
        			else {
        				// Only Prefix and Root can fit together on line 2.
        				sign1 = headerTags+ topLine.trim();
        				sign2 = (namePrefix+" "+nameRoot).trim();
        				sign3 = nameSuffix.trim();
        			}

        			// Add name of town
        			bookContents += "\u00a7r\u00a70"+topLine+"\n";

        			if ( sign0.length() == 0 || sign0.equals("\u00a7r") ) {
        				bookContents += "\u00a7r\u00a70\u00a7l"+sign2;
        			}
        			else {
        				bookContents += "\u00a7r\u00a70\u00a7l"+sign1 + "\n" + "\u00a7r\u00a70\u00a7l"+sign2;
        			}
        			bookContents +=
        					"\n" + "\u00a7r\u00a70\u00a7l"+sign3 + 
        					"\n\n" +
        					"\u00a7r\u00a70Located at:\n"+
        					"\u00a7r\u00a70x = \u00a7l"+signX+"\u00a7r\ny = \u00a7l"+signY+"\u00a7r\nz = \u00a7l"+signZ;

        			// These lines clarify when a feature is not on the Overworld
        			if (player.dimension==-1) {
        				bookContents +=
        						"\n" +
        						"\u00a7r\u00a70(Nether)\n";
        			}
        			else if (player.dimension==1) {
        				bookContents +=
        						"\n" +
        						"\u00a7r\u00a70(End dimension)\n";
        			}
        			else if (structureType.equals("MoonVillage")) {
        				bookContents +=
        						"\n" +
        						"\u00a7r(Moon)\n";
        			}
        			else if (structureType.equals("KoentusVillage")) {
        				bookContents +=
        						"\n" +
        						"\u00a7r(Koentus)\n";
        			}
        			else if (structureType.equals("FronosVillage")) {
        				bookContents +=
        						"\n" +
        						"\u00a7r(Fronos)\n";
        			}
        			else if (structureType.equals("GC_AbandonedBase")) {
        				bookContents +=
        						"\n" +
        						"\u00a7r(Asteroid Belt)\n";
        			}
        			
        			bookContents += "\"}"; //As of 1.9.4 I need to enclose book contents in {"text":"lorem ipsum"}
        			
        			List<String> pages = new ArrayList<String>();
        			
        			ItemStack book = new ItemStack(ModItems.villagebook);
        			
        			// Change the icon up depending on surroundings
        			if (structureType.equals("Village")) {book = new ItemStack(ModItems.villagebook);}
        			else if (structureType.equals("Mineshaft")) {book = new ItemStack(ModItems.mineshaftbook);}
        			else if (structureType.equals("Stronghold")) {book = new ItemStack(ModItems.strongholdbook);}
        			else if (structureType.equals("Temple")) {book = new ItemStack(ModItems.templebook);}
        			else if (structureType.equals("Monument")) {book = new ItemStack(ModItems.monumentbook);}
        			else if (structureType.equals("Mansion")) {book = new ItemStack(ModItems.mansionbook);}
        			else if (structureType.equals("Fortress")) {book = new ItemStack(ModItems.fortressbook);}
        			else if (structureType.equals("EndCity")) {book = new ItemStack(ModItems.endcitybook);}
        			else if (structureType.equals("MoonVillage")) {book = new ItemStack(ModItems.moonvillagebook);}
        			else if (structureType.equals("KoentusVillage")) {book = new ItemStack(ModItems.koentusvillagebook);}
        			else if (structureType.equals("hardcoreenderdragon_EndIsland")) {book = new ItemStack(ModItems.endcitybook);}
        			else if (structureType.equals("hardcoreenderdragon_EndTower")) {book = new ItemStack(ModItems.endcitybook);}
        			else if (structureType.equals("FronosVillage")) {book = new ItemStack(ModItems.fronosvillagebook);}
        			else if (structureType.equals("NibiruVillage")) {book = new ItemStack(ModItems.nibiruvillagebook);}
        			else if (structureType.equals("GC_AbandonedBase")) {book = new ItemStack(ModItems.abandonedbasebook);}
        			
        			if (book.getTagCompound() == null) {
        				book.setTagCompound(new NBTTagCompound());
        			}
        			
        			String nameCompound = namePrefix + " " +  nameRoot + " " + nameSuffix;
        			String authorName = player.getDisplayNameString();
        			
        			// Set the title
        			book.getTagCompound().setString("title", nameCompound.trim() );
        			// Set the author
        			book.getTagCompound().setString("author", authorName );
        			
        			// Set the book's contents
        			NBTTagList pagesTag = new NBTTagList();
        			
        			// Page 1, with the feature information
        			pagesTag.appendTag(new NBTTagString(bookContents));
        			
        			book.getTagCompound().setTag("pages", pagesTag);
        			
        			// Don't make a book if the sign coords are all -1.
        			
        			if (signX!=-1 && signY!=-1 && signZ!=-1) {
        				// Consume Book and Codex
            			player.inventory.clearMatchingItems(Items.BOOK, -1, 1, null);
            			player.inventory.clearMatchingItems(ModItems.codex, -1, 1, null);
            			
            			// Give the book to the player
            	        EntityItem eitem = (player).entityDropItem(book, 1);
            	        eitem.setNoPickupDelay(); //No delay: directly into the inventory!
        			}
        			
        		}
        		else {
        		}
        	}
    		
    	}
    	catch (Exception e) {
    		// Something went wrong. Cancel the whole Codex action.
    	}
    	
		//return itemStack;
		return new ActionResult(EnumActionResult.PASS, itemstack);
	}
    
}