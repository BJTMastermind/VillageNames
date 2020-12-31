package astrotibs.villagenames.advancements;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import astrotibs.villagenames.config.GeneralConfig;
import astrotibs.villagenames.utility.LogHelper;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Adapted from Jabelar's Minecraft Forge Modding Tutorials
 * http://jabelarminecraft.blogspot.com/p/minecraft-modding-custom-triggers-aka.html
 */

 // advancement grant AstroTibs only villagenames:archaeologist
// advancement revoke AstroTibs only villagenames:archaeologist

public class ModTriggers
{
    public static final CustomTrigger LAPUTA = new CustomTrigger("laputa");
    public static final CustomTrigger ARCHAEOLOGIST = new CustomTrigger("archaeologist");
    public static final CustomTrigger MAXREP = new CustomTrigger("maxrep");
    public static final CustomTrigger MINREP = new CustomTrigger("minrep");
    public static final CustomTrigger GHOSTTOWN = new CustomTrigger("ghosttown");
    
    // Add manual triggers for all the structure types. This is needed for the Archaeologist Advancement.
    public static final CustomTrigger CODEX_VILLAGE = new CustomTrigger("codex_village");
    public static final CustomTrigger CODEX_JUNGLETEMPLE = new CustomTrigger("codex_jungletemple");
    public static final CustomTrigger CODEX_DESERTPYRAMID = new CustomTrigger("codex_desertpyramid");
    public static final CustomTrigger CODEX_SWAMPHUT = new CustomTrigger("codex_swamphut");
    public static final CustomTrigger CODEX_IGLOO = new CustomTrigger("codex_igloo");
    public static final CustomTrigger CODEX_TEMPLE = new CustomTrigger("codex_temple");
    public static final CustomTrigger CODEX_MANSION = new CustomTrigger("codex_mansion");
    public static final CustomTrigger CODEX_MONUMENT = new CustomTrigger("codex_monument");
    public static final CustomTrigger CODEX_STRONGHOLD = new CustomTrigger("codex_stronghold");
    public static final CustomTrigger CODEX_MINESHAFT = new CustomTrigger("codex_mineshaft");
    public static final CustomTrigger CODEX_FORTRESS = new CustomTrigger("codex_fortress");
    public static final CustomTrigger CODEX_ENDCITY = new CustomTrigger("codex_endcity");
    
    public static final CustomTrigger CODEX_MOONVILLAGE = new CustomTrigger("codex_moonvillage");
    public static final CustomTrigger CODEX_KOENTUSVILLAGE = new CustomTrigger("codex_koentusvillage");
    public static final CustomTrigger CODEX_FRONOSVILLAGE = new CustomTrigger("codex_fronosvillage");
    public static final CustomTrigger CODEX_NIBIRUVILLAGE = new CustomTrigger("codex_nibiruvillage");
    public static final CustomTrigger CODEX_ABANDONEDBASE = new CustomTrigger("codex_abandonedbase");
    
    /*
     * This array just makes it convenient to register all the criteria.
     */
    public static final CustomTrigger[] TRIGGER_ARRAY = new CustomTrigger[] {
            LAPUTA,
            ARCHAEOLOGIST,
            MAXREP,
            MINREP,
            GHOSTTOWN,
            
            CODEX_VILLAGE,
            CODEX_JUNGLETEMPLE,
            CODEX_DESERTPYRAMID,
            CODEX_SWAMPHUT,
            CODEX_IGLOO,
            CODEX_TEMPLE,
            CODEX_MANSION,
            CODEX_MONUMENT,
            CODEX_STRONGHOLD,
            CODEX_MINESHAFT,
            CODEX_FORTRESS,
            CODEX_ENDCITY,
            
            CODEX_MOONVILLAGE,
            CODEX_KOENTUSVILLAGE,
            CODEX_FRONOSVILLAGE,
            CODEX_NIBIRUVILLAGE,
            CODEX_ABANDONEDBASE
            };
    
    
    public static void registerTriggers()
    {
    	// DEBUG
    	if(GeneralConfig.debugMessages) {LogHelper.info("Registering custom triggers");}
    	
    	Method method;
    	
    	method = ReflectionHelper.findMethod(CriteriaTriggers.class, "register", "func_192118_a", ICriterionTrigger.class);
    	
    	method.setAccessible(true);
    	
    	for (int i=0; i < TRIGGER_ARRAY.length; i++)
    	{
    		try
    		{
    			method.invoke(null, TRIGGER_ARRAY[i]);
    		}
    		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
    		{
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	} 
    }
}