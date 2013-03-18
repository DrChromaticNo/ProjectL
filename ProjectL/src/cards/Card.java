package cards;

import java.awt.Color;

/**
 * @author Chris
 * Card object to be used in hands, discard, and on the board
 */

public class Card implements Comparable<Card>{

	private Color faction;
	private int value;
	private int silverNum;
	private boolean dayPhase;
	private boolean eveningPhase;
	private boolean nightPhase;
	
	public Card (Color faction, int value)
	{
		this.faction = faction;
		this.value = value;
		silverNum = Deck.getSilverNum(faction, value);
		dayPhase = false;
		eveningPhase = false;
		nightPhase = false;
	}
	
	public Color getFaction()
	{
		return faction;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public int getSilver()
	{
		return silverNum;
	}
	
	@Override public boolean equals(Object other)
	{
		if(other instanceof Card)
		{
			if(this.faction.equals(((Card) other).faction))
			{
				if(this.value == ((Card) other).value)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	@Override public int hashCode()
	{
		return faction.hashCode() + value;
	}

	@Override
	public int compareTo(Card arg0) {
		
	}

}
