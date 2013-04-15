package cards;

import main.GameState;

public interface Estimator {

	
	/**
	 * Estimates the value of playing card for the given state
	 * @param state the state the card is being played on
	 * @param card the card being played
	 * @return the heuristic estimated value of playing a given card on a given state
	 */
	public int estimate(GameState state, Card card);
}
