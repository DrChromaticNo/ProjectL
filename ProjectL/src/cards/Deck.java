package cards;

import java.awt.Color;

import main.GameState;

/**
 * @author Chris
 * Class to manage the finer points of cards, and to do game state modifications
 */

public interface Deck {

	/**
	 * Given a valid faction and card value, returns the silver number of that card
	 * @param faction the Card's faction
	 * @param value the card's value
	 * @return The silver number of that card, according to this deck
	 */
	public int getSilverNum(Color faction, int value);
	
	/**
	 * To get the list of all the valid card numbers for this deck
	 * @return The list of all cards in this deck in an array (sorted in order?)
	 */
	public int[] allCards();
	
	
	/**
	 * Performs the given cards' time-phase action (if one exists)
	 * @param time the time corresponding to the phase
	 * @param card The card
	 * @param state the state before the action
	 * @return the state after the action
	 */
	public GameState doPhase(int time, Card card, GameState state);


}
