package astrotibs.villagenames.capabilities;

/**
 * Constructed following McHorse's tutorial at:
 * https://www.planetminecraft.com/blog/forge-tutorial-capability-system/
 * @author AstroTibs
 */

//Added in v3.1
public class ModularSkin implements IModularSkin {
	
	private int profession = -1;
	private int career = -1;
	private int biomeType = -1;
	private int professionLevel = -1;
	private int skinTone = -99; // Added in v3.2

	
	@Override
	public void setProfession(int p) {this.profession = p;}

	@Override
	public void setCareer(int c) {this.career = c;}

	@Override
	public void setBiomeType(int bt) {this.biomeType = bt;}

	// Added in v3.2
	@Override
	public void setSkinTone(int st) {this.skinTone = st;}
	
	
	@Override
	public void setProfessionLevel(int pl) {this.professionLevel = pl;}

	@Override
	public int getProfession() {return this.profession;}

	@Override
	public int getCareer() {return this.career;}
	
	@Override
	public int getBiomeType() {return this.biomeType;}

	@Override
	public int getProfessionLevel() {return this.professionLevel;}

	// Added in v3.2
	@Override
	public int getSkinTone() {return this.skinTone;}
	
	
}
