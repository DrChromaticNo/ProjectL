package resources;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chris
 * Factory class to provide treasure references and create loot maps
 */

public class Treasure {

	//as an example, need to implement the rest
	public static final String RELIC = "relic";
	
	/**
	 * Returns a map of strings to loot with every value set to 0
	 * @return a map of all treasure strings to loot amts with every value 0
	 */
	public static Loot getLoot()
	{
		String[] treasures = new String[1];
		treasures[0] = RELIC;
		int[] values = new int[1];
		values[0] = 0;
		Loot map = new Loot(treasures,values);
		return map;
	}

}
