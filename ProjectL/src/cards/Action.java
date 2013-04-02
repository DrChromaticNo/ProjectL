package cards;

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
	 * @param time the time this action is occuring
	 * @return the gamestate after the action has occurred
	 */
	public GameState doAction(GameState state, Card card, int time);
	
	/**
	 * Get a list of all possible states resulting from this action
	 * @param state the state before the action
	 * @param card the card this action is happening because of
	 * @param time the time this action is occuring
	 * @return the possible gamestates after the action has occurred.
	 */
	public GameState[] allActions(GameState state, Card card, int time);
	
	/**
	 * Score the card
	 * @param state the state before the card is scored
	 * @param card the card that is being scored
	 * @return the score of the card
	 */
	public int score(GameState state, Card card);

}
