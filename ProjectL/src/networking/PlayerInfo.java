package networking;

import java.awt.Color;
import java.io.Serializable;
import java.util.Set;

import score.Loot;
/**
 * Like GameInfo, a serializable class to hold the player info for a given player
 * @author Chris
 *
 */
public class PlayerInfo implements Serializable {

	private static final long serialVersionUID = -2310939341965351008L;
	
	private Color faction;
	private boolean CPU;
	private int gold;
	private int score;
	private Loot loot;
	private Set<CardInfo> hand;
	private Set<CardInfo> discard;
	private Set<CardInfo> den;
	
	public PlayerInfo(Color faction, boolean CPU, int gold, int score, Loot loot,
			Set<CardInfo> hand, Set<CardInfo> discard, Set<CardInfo> den)
	{
		this.faction = faction;
		this.CPU = CPU;
		this.gold = gold;
		this.score = score;
		this.loot = loot;
		this.hand = hand;
		this.discard = discard;
		this.den = den;
	}
	
	public Color getFaction()
	{
		return faction;
	}
	
	public boolean getCPU()
	{
		return CPU;
	}
	
	public int getGold()
	{
		return gold;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public Loot getLoot()
	{
		return loot;
	}
	
	public Set<CardInfo> getHand()
	{
		return hand;
	}
	
	public Set<CardInfo> getDiscard()
	{
		return discard;
	}
	
	public Set<CardInfo> getDen()
	{
		return den;
	}

}
