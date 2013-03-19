package resources;

import java.util.HashMap;

public class Loot {

	private HashMap<String, Integer> loot;
	
	/**
	 * Instantiates a loot bag with each treasure having the corresponding index in value's amounts
	 * They must have the same length
	 * @param treasures all the treasures that the loot contains
	 * @param values the amt of each loot
	 */
	public Loot(String[] treasures, int[] values)
	{
		loot = new HashMap<String, Integer>();
		if(treasures.length == values.length)
		{
			for(int i = 0; i < treasures.length; i++)
			{
				loot.put(treasures[i], values[i]);
			}
		}
	}
	
	public HashMap<String, Integer> getBag()
	{
		return loot;
	}
	
	/**
	 * Given a Treasure string, modifies the amount of that loot by the mod
	 * @param treasure String corresponding to the type of treasure
	 * @param mod how much to increase or decrease the amt of treasure by
	 */
	public void addLoot(String treasure, int mod)
	{
		if(loot.containsKey(treasure))
		{
			loot.put(treasure, loot.get(treasure)+mod);
			if(loot.get(treasure) < 0)
			{
				throw new RuntimeException("negative treasure?!");
			}
		}
	}

}
