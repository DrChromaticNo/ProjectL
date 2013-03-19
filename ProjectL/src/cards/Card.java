package cards;

import java.awt.Color;

import main.GameState;

import players.Player;


/**
 * @author Chris
 * Card object to be used in hands, discard, and on the board
 */

public class Card implements Comparable<Card>{

	private Color faction;
	private int value;
	private int silverNum;
	private Deck deck;
	private boolean dayPhase;
	private boolean eveningPhase;
	private boolean nightPhase;
	
	public Card (Color faction, int value, Deck deck)
	{
		this.faction = faction;
		this.value = value;
		this.deck = deck;
		silverNum = deck.getSilverNum(faction, value);
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
			if(this.faction.equals
					(((Card) other).faction))
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
	
	/**
	 * Performs the day action for this card
	 * @param state the state of the game before the action
	 * @return the state of the game after the action is performed
	 */
	public GameState dayAction(GameState state)
	{
		GameState end = deck.dayPhase(this, state);
		dayPhase = true;
		return end;
	}
	
	/**
	 * Performs the night action for this card
	 * @param state the state of the game before the action
	 * @return the state of the game after the action is performed
	 */
	public GameState eveningAction(GameState state)
	{
		GameState end = deck.eveningPhase(this, state);
		eveningPhase = true;
		return end;
	}
	
	/**
	 * Performs the evening action for this card
	 * @param state the state of the game before the action
	 * @return the state of the game after the action is performed
	 */
	public GameState nightAction(GameState state)
	{
		GameState end = deck.nightPhase(this, state);
		nightPhase = true;
		return end;
	}
	
	public void resetPhases()
	{
		dayPhase = false;
		eveningPhase = false;
		nightPhase = false;
	}
	
	public boolean getDayPhase()
	{
		return dayPhase;
	}
	
	public boolean getEveningPhase()
	{
		return eveningPhase;
	}
	
	public boolean getNightPhase()
	{
		return nightPhase;
	}

}
