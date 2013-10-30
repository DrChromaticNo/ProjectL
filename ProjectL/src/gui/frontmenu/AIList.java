package gui.frontmenu;

import standard.StandardEstimator;
import ai.AI;
import ai.DepthEstAI;
import ai.Estimator;
import ai.SimpleAI;

public class AIList {

	@SuppressWarnings("unchecked")
	public static Class<AI>[] get()
	{
		return new Class[]{SimpleAI.class, DepthEstAI.class};
	}
	
	public static String[] getNames()
	{
		return new String[]{"Easy","Hard"};
	}
	
	@SuppressWarnings("rawtypes")
	public static Class[][] getParamTypes()
	{
		return new Class[][]{null, new Class[]{int.class, int.class, Estimator.class}};
	}
	
	public static Object[][] getParams()
	{
		return new Object[][]{null, new Object[]{2,1,new StandardEstimator()}};
	}
	
}
