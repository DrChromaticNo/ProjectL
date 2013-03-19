package score;

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
		Loot map = new Loot(treasures);
		return map;
	}

}
