package score;

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

}
