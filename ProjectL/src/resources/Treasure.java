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
	public static Map<String, Integer> getLootMap()
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		//note: when implementing the rest, put 'em here too
		map.put(Treasure.RELIC, 0);
		return map;
	}

}
