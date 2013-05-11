package cards;

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
}
