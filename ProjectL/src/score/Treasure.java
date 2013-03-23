package score;

/**
 * @author Chris
 * Factory class to provide treasure references and create loot maps
 */

public class Treasure {

	//once again, could be expanded to have more options but make sure
	//the deck/treasurebag you're using supports them
	public static final String RELIC = "cursed relic(s)";
	public static final String CHEST = "chest(s)";
	public static final String JEWEL = "jewel(s)";
	public static final String GOODS = "barrel(s) of goods";
	public static final String OFFICER = "spanish officer(s)";
	public static final String MAP = "treasure map(s)";
	public static final String SABER = "saber(s)";
	
	
	/**
	 * Returns a map of strings to loot with every value set to 0
	 * @return a map of all treasure strings to loot amts with every value 0
	 */
	public static Loot getLoot()
	{
		String[] treasures = new String[7];
		treasures[0] = RELIC;
		treasures[1] = CHEST;
		treasures[2] = JEWEL;
		treasures[3] = GOODS;
		treasures[4] = OFFICER;
		treasures[5] = MAP;
		treasures[6] = SABER;
		Loot map = new Loot(treasures);
		return map;
	}
	
	/**
	 * Get a list of all the possible treasures
	 * @return a list of all the possible treasures
	 */
	public static String[] allTreasures()
	{
		String[] treasures = new String[7];
		treasures[0] = RELIC;
		treasures[1] = CHEST;
		treasures[2] = JEWEL;
		treasures[3] = GOODS;
		treasures[4] = OFFICER;
		treasures[5] = MAP;
		treasures[6] = SABER;
		return treasures;
	}

}
