package astrotibs.villagenames.capabilities;

/**
 * Constructed following McHorse's tutorial at:
 * https://www.planetminecraft.com/blog/forge-tutorial-capability-system/
 * @author AstroTibs
 */

public interface IModularSkin {
	
	public void setProfession(int p);
	public void setCareer(int c);
	public void setBiomeType(int bt);
	public void setProfessionLevel(int pl);
	public void setSkinTone(int st);
	
	public int getProfession();
	public int getCareer();
	public int getBiomeType();
	public int getProfessionLevel();
	public int getSkinTone();

}
