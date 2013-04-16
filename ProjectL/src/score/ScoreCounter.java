package score;

import cards.Card;
import main.GameState;


/**
 * An interface to create objects to score a given state with
 * @author Chris
 *
 */
public interface ScoreCounter {

	/**
	 * Scores all the players in a gameState
	 * @param state the state before all players have been scored
	 * @return the state after all players have been scored
	 */
	public GameState score(GameState state);
	
	/**
	 * Returns an ordered array from greatest to list of the ranking associated with playing each card
	 * on state
	 * @param state the state the cards are being ranked on
	 * @param cards the array of cards to be ranked
	 * @return an ordered array showing which cards are ranked highest to lowest
	 */
	public Card[] rankCards(GameState state, Card[] cards);

}
