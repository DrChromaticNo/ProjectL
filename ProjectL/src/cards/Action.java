package cards;

import score.ScoreCounter;
import score.TreasureBag;
import main.GameState;

/**
 * Manages the actions for a specific kind of card or treasure
 * @author Chris
 *
 */
public interface Action {
	
	/**
	 * Do the action corresponding to these parameters and return the gamestate afterwards
	 * @param state the state before the action
	 * @param card the card this action is happening because of
	 * @param bag the treasure bag
	 * @param time the time this action is occuring
	 * @param deck the deck being used in this game
	 * @param counter the scorecounter being used in this game
	 * @return the gamestate after the action has occured
	 */
	public GameState doAction(GameState state, Card card, TreasureBag bag, 
			Deck deck, ScoreCounter counter, int time);
	
	/**
	 * Score the card
	 * @param state the state before the card is scored
	 * @param card the card that is being scored
	 * @return the score of the card
	 */
	public int score(GameState state, Card card);

}
