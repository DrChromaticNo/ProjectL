package gui;

import score.Loot;
import cards.Card;
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
	
	/**
	 * Displays the passed message in the GUI
	 * @param message the message to be displayed
	 */
	public void displayMessage(String message);
	
	/**
	 * Prompts the player to choose from an array of cards
	 * @param prompt the text prompt for the choice
	 * @param cards the cards to be chosen from
	 * @return the choice that the user makes
	 */
	public Card makeChoice(String prompt, Card[] cards);
	
	/**
	 * Prompts the player to choose from a loot bag
	 * @param prompt the text prompt for the choice
	 * @param loot the loot bag the player is choosing from
	 * @return the choice that the user makes
	 */
	public String makeChoice(String prompt, Loot loot);
	
	/**
	 * Prompts the player to choose from an array of strings
	 * @param prompt the text prompt for the choice
	 * @param choices the strings the player has to choose from
	 * @return the choice that the user makes
	 */
	public String makeChoice(String prompt, String[] choices);
	
	/**
	 * Makes the GUI visible
	 */
	public void launch();
	
}
