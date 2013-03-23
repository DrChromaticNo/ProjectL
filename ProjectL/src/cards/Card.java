package cards;

import java.awt.Color;

import score.TreasureBag;

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
	 * @param bag the treasurebag to use with this phase
	 * @return the state of the game after the action is performed
	 */
	public GameState dayAction(GameState state, TreasureBag bag)
	{
		GameState end = deck.doPhase(Time.DAY, this, state, bag);
		dayPhase = true;
		return end;
	}
	
	/**
	 * Performs the night action for this card
	 * @param state the state of the game before the action
	 * @param bag the treasurebag to use with this phase
	 * @return the state of the game after the action is performed
	 */
	public GameState eveningAction(GameState state, TreasureBag bag)
	{
		GameState end = deck.doPhase(Time.EVENING, this, state, bag);
		eveningPhase = true;
		return end;
	}
	
	/**
	 * Performs the evening action for this card
	 * @param state the state of the game before the action
	 * @return the state of the game after the action is performed
	 */
	public GameState nightAction(GameState state, TreasureBag bag)
	{
		//Note: night actions occur simultaneously so they can never use the bag
		GameState end = deck.doPhase(Time.NIGHT, this, state, bag);
		nightPhase = true;
		return end;
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
		else if(time == Time.EVENING)
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

}
