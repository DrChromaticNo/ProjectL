package cards;

import java.awt.Color;

import resources.Player;

/**
 * @author Chris
 * Card object to be used in hands, discard, and on the board
 */

public class Card implements Comparable<Card>{

	private Player player;
	private int value;
	private int silverNum;
	private boolean dayPhase;
	private boolean eveningPhase;
	private boolean nightPhase;
	
	public Card (Player player, int value)
	{
		this.player = player;
		this.value = value;
		silverNum = Deck.getSilverNum(player.getFaction(), value);
		dayPhase = false;
		eveningPhase = false;
		nightPhase = false;
	}
	
	public Player getPlayer()
	{
		return player;
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
			if(this.player.getFaction().equals
					(((Card) other).getPlayer().getFaction()))
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
		return player.getFaction().hashCode() + value;
	}

	@Override
	public int compareTo(Card arg0) {
		
		if(this.equals(arg0))
		{
			return 0;
		}
		if(this.value > arg0.value)
		{
			return 1;
		}
		else if(this.value < arg0.value)
		{
			return -1;
		}
		else
		{
			if(this.silverNum > arg0.silverNum)
			{
				return 1;
			}
			else if(this.silverNum < arg0.silverNum)
			{
				return -1;
			}
			else
			{
				throw new RuntimeException("two cards have different " +
						"factions but the same value and silver number");
			}
		}
		
	}

}
