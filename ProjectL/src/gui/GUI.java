package gui;

import main.GameState;

/**
 * Objects created with this interface should represent a given player's current status and should be able to
 * prompt/return choices for various actions in the game
 * @author Chris
 *
 */

public interface GUI {

	/**
	 * Updates the gui to reflect the passed state
	 * @param state the state to update the gui to reflect
	 */
	public void update(GameState state);
}
