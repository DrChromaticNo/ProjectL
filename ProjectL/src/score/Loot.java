package score;

import java.io.Serializable;
import java.util.HashMap;

public class Loot implements Serializable {

	private static final long serialVersionUID = -9071743227697984746L;
	private HashMap<String, Integer> loot;
	
	/**
	 * Instantiates a loot bag with each treasure having a value of 0
	 * @param treasures all the treasures that the loot contains
	 */
	public Loot(String[] treasures)
	{
		loot = new HashMap<String, Integer>();
		for(int i = 0; i < treasures.length; i++)
		{
			loot.put(treasures[i], 0);
		}
	}
	
	public Loot(Loot bag)
	{
		loot = new HashMap<String, Integer>(bag.loot);
	}
	
	public int countTreasure(String treasure)
	{
		if(loot.containsKey(treasure))
		{
			return loot.get(treasure);
		}
		else
		{
			return 0;
		}
	}
	
	public void emptyBag()
	{
		for(String s : loot.keySet())
		{
			loot.put(s, 0);
		}
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
	
	@Override public boolean equals(Object other)
	{
		if(other instanceof Loot)
		{
			Loot bag = (Loot) other;
			if(loot.equals(bag.loot))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override public int hashCode()
	{
		return loot.hashCode();
	}

}
