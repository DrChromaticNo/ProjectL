package cards;

import java.awt.Color;


import main.GameState;
import main.Time;


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
	
	/**
	 * Initializes the card with the given faction, value, and deck
	 * @param faction the faction to associte w/ this card
	 * @param value the value to associate w/ this card
	 * @param deck the deck this card "belongs to"
	 */
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
	
	public Card (Card card)
	{
		this.faction = card.faction;
		this.value = card.value;
		this.deck = card.deck;
		silverNum = card.silverNum;
		dayPhase = card.dayPhase;
		eveningPhase = card.eveningPhase;
		nightPhase = card.nightPhase;
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
	
	public String abbreviate()
	{
		return deck.abbreviatedName(this);
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
					//Not sure if the card's equal function should be this deep: maybe use compareTo instead?
					Card card = (Card) other;
					if(dayPhase == card.dayPhase)
					{
						if(eveningPhase == card.eveningPhase)
						{
							if(nightPhase == card.nightPhase)
							{
								return true;
							}
						}
					}
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
		GameState end = deck.doPhase(Time.DAY, this, state);
		return end;
	}
	
	/**
	 * Get all the possible day states for this card
	 * @param state the state before the action
	 * @return the states of the game after this action is performed
	 */
	public GameState[] possibleDayActions(GameState state)
	{
		GameState[] states = deck.getPossiblePhases(Time.DAY, this, state);
		return states;
	}
	
	/**
	 * Performs the night action for this card
	 * @param state the state of the game before the action
	 * @return the state of the game after the action is performed
	 */
	public GameState eveningAction(GameState state)
	{
		GameState end = deck.doPhase(Time.DUSK, this, state);
		return end;
	}
	
	/**
	 * Get all the possible day states for this card
	 * @param state the state before the action
	 * @return the states of the game after this action is performed
	 */
	public GameState[] possibleEveningActions(GameState state)
	{
		GameState[] states = deck.getPossiblePhases(Time.DUSK, this, state);
		return states;
	}
	
	/**
	 * Performs the night action for this card
	 * @param state the state of the game before the action
	 * @return the state of the game after the action is performed
	 */
	public GameState nightAction(GameState state)
	{
		//Note: night actions occur simultaneously so they can never use the bag
		GameState end = deck.doPhase(Time.NIGHT, this, state);
		return end;
	}
	
	/**
	 * Get all the possible night states for this card
	 * @param state the state before the action
	 * @return the states of the game after this action is performed
	 */
	public GameState[] possibleNightActions(GameState state)
	{
		GameState[] states = deck.getPossiblePhases(Time.NIGHT, this, state);
		return states;
	}
	
	/**
	 * Perform this cards' end of game/score action
	 * @param state the state before this card has been scored
	 * @return the change in gold based on this card (positive or negative)
	 */
	public int score(GameState state)
	{
		return deck.scoreCard(this, state);
	}
	
	/**
	 * Resets all the phases to their default values (used when night phase resets or when card is
	 * moved to hand from discard or den
	 */
	public void resetPhases()
	{
		dayPhase = false;
		eveningPhase = false;
		nightPhase = false;
	}
	
	/**
	 * By inputing a time (from the Time class), checks if the card has acted the corresponding phase
	 * @param time The time corresponding to the phase to check
	 * @return the boolean corresponding to the phase having acted or not
	 */
	public boolean checkPhase(int time)
	{
		if(time == Time.DAY)
		{
			return dayPhase;
		}
		else if(time == Time.DUSK)
		{
			return eveningPhase;
		}
		else if(time == Time.NIGHT)
		{
			return nightPhase;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Sets a given time to true
	 * @param time the time of the phase to set to true
	 */
	public void setTrue(int time)
	{
		if(time == Time.DAY)
		{
			dayPhase = true;
		}
		else if(time == Time.DUSK)
		{
			eveningPhase = true;
		}
		else if(time == Time.NIGHT)
		{
			nightPhase = true;
		}
	}
	
	@Override
	public String toString()
	{
		return abbreviate();
	}
	
}
