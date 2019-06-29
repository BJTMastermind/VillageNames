package astrotibs.villagenames.capabilities;

/**
 * Constructed following McHorse's tutorial at:
 * https://www.planetminecraft.com/blog/forge-tutorial-capability-system/
 * @author AstroTibs
 */

//Added in v3.1
public interface IModularSkin {
	
	public void setProfession(int p);
	public void setCareer(int c);
	public void setBiomeType(int bt);
	public void setProfessionLevel(int pl);
	
	public int getProfession();
	public int getCareer();
	public int getBiomeType();
	public int getProfessionLevel();
	
}
