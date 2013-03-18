package resources;

import java.util.Map;

/**
 * @author Chris
 * The Player object holds all the objects a player holds, their gold, score, treasures, hand, discard pile, etc.
 */

public class Player {

	//which faction this player is
	private Faction faction;
	// true if the player is a CPU, false if not (CPU not yet implemented so will always be false)
	private boolean CPU;
	// the players current accessible gold
	private int gold;
	// the players current scored gold
	private int score;
	// the players current booty tokens
	private Map<String, Integer> loot;
	
	
	public Player(Faction f)
	{
		faction = f;
		CPU = false;
		score = 0;
		gold = 0;
		loot = Treasure.getLootMap();
	}
	
	public int getGold()
	{
		return gold;
	}
	
	public void addGold(int mod)
	{
		gold+=mod;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void addScore(int mod)
	{
		score+=mod;
	}

}
