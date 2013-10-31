package ai;

import cards.Card;
import main.GameState;

/**
 * A class to facilitate ranking cards based on the heuristic value of playing them
 * @author Chris
 *
 */
public interface Estimator {

	
	/**
	 * Estimates the value of playing card for the given state
	 * @param state the state the card is being played on
	 * @param card the card being played
	 * @return the heuristic estimated value of playing a given card on a given state
	 */
	public int estimate(GameState state, Card card);
	
	/**
	 * Estimates the value of this card choosing a particular treasure at this state 
	 * @param state the state in which the treasure is being chosen
	 * @param card the card choosing that treasure
	 * @param treasure treasure being chosen
	 * @return the value for that player choosing this particular treasure
	 */
	public int treasureValue(GameState state, Card card, String treasure);
	
	/**
	 * Returns an ordered array from greatest to least of the ranking associated with playing each card
	 * on state
	 * @param state the state the cards are being ranked on
	 * @param cards the array of cards to be ranked
	 * @return an ordered array showing which cards are ranked highest to lowest
	 */
	public Card[] rankCards(GameState state, Card[] cards);
	
	/**
	 * Returns an ordered array from greatest to least of the ranking associated with picking each treasure
	 * on state
	 * @param state the state to choose the treasure in
	 * @param treasures the potential treasures to choose from
	 * @param card the card that is being picking this treasure
	 * @return an ordered array showing which treasures are ranked highest to lowest
	 */
	public String[] rankTreasures(GameState state, String[] treasures, Card card);
}
