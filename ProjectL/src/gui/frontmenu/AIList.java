package gui.frontmenu;

import ai.AI;
import ai.DepthEstAI;
import ai.SimpleAI;

public class AIList {

	public static Class<AI>[] get()
	{
		return new Class[]{SimpleAI.class, DepthEstAI.class};
	}
	
	public static String[] getNames()
	{
		return new String[]{"Easy","Hard"};
	}
	
	public static Class[][] getParamTypes()
	{
		return new Class[][]{null, new Class[]{int.class, int.class}};
	}
	
	public static Object[][] getParams()
	{
		return new Object[][]{null, new Object[]{2,1}};
	}
	
}
