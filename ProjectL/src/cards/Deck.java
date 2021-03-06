package cards;

import java.awt.Color;
import java.util.ArrayList;

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
	 * @return The list of all cards in this deck in an array
	 */
	public ArrayList<Integer> allCards();
	
	/**
	 * Performs the given cards' time-phase action (if one exists)
	 * @param time the time corresponding to the phase
	 * @param card The card
	 * @param state the state before the action
	 * @return the state after the action
	 */
	public GameState doPhase(int time, Card card, GameState state);
	
	/**
	 * Get all the possible phases for this action at this time
	 * @param time the time corresponding to the phase
	 * @param card The card
	 * @param state the state before the action
	 * @return an array of all the possible resulting states from this action
	 */
	public GameState[] getPossiblePhases(int time, Card card, GameState state);
	
	/**
	 * Perform the card's end of game/score action
	 * @param card The card to score with
	 * @param state the game state before this card has been scored
	 * @return the change in gold based on the card
	 */
	public int scoreCard(Card card, GameState state);
	

	/**
	 * Gives the shortened form of the card's name
	 * @param card the card whose name will be shortened
	 * @return the shortened name
	 */
	public String abbreviatedName(Card card);
	
	/**
	 * Gives the full form of the card's name
	 * @param card the card to get the name of
	 * @return the full name of the card
	 */
	public String getCardName(Card card);
	
	/**
	 * Gives the description of the card
	 * @param card the card to get the description of
	 * @return the description of the card
	 */
	public String getCardDesc(Card card);
	

}
