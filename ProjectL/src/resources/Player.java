package resources;

import java.awt.Color;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cards.Card;

/**
 * @author Chris
 * The Player object holds all the objects a player holds, their gold, score, 
 * treasures, hand, discard pile, etc.
 */

public class Player {

	//which faction this player is
	private Color faction;
	// true if the player is a CPU, false if not (CPU not yet implemented so will always be false)
	private boolean CPU;
	// the players current accessible gold
	private int gold;
	// the players current scored gold
	private int score;
	// the players current booty tokens
	private Map<String, Integer> loot;
	private Set<Card> hand;
	private Set<Card> discard;
	private Set<Card> den;
	
	
	public Player(Color faction)
	{
		this.faction = faction;
		CPU = false;
		score = 0;
		gold = 0;
		loot = Treasure.getLootMap();
		hand = new HashSet<Card>();
		discard = new HashSet<Card>();
		den = new HashSet<Card>();
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
	
	public Color getFaction()
	{
		return faction;
	}
	
	public void addToHand(Card card)
	{
		hand.add(card);
	}
	
	public void addToDiscard(Card card)
	{
		discard.add(card);
	}
	
	public void addToDen(Card card)
	{
		den.add(card);
	}
	
	public void removeFromHand(Card card)
	{
		hand.remove(card);
	}
	
	public void removeFromDiscard(Card card)
	{
		discard.remove(card);
	}
	
	public void removeFromDen(Card card)
	{
		den.remove(card);
	}
	
	public void clearDiscard()
	{
		discard.clear();
	}
	
	public void clearDen()
	{
		den.clear();
	}
	
	public Set<Card> getHand()
	{
		return hand;
	}
	
	public Set<Card> getDiscard()
	{
		return discard;
	}
	
	public Set<Card> getDen()
	{
		return den;
	}
	
	public Map<String, Integer> getLoot()
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
				throw new RuntimeException("Whomever is playing " + 
						Faction.getPirateName(faction) + " has negative " + treasure);
			}
		}
	}

}
