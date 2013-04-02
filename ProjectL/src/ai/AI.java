package ai;

import players.Player;
import cards.Card;
import main.GameState;

/**
 * The main interface for all AI players
 * @author Chris
 *
 */
public interface AI {

	/**
	 * Given a set of possible moves, select one to play
	 * @param states the move from which all the other moves will happen
	 * @card the card being used for this action
	 * @return the one to play
	 */
	public GameState choose(Player player, GameState[] states, Card card);
	
	/**
	 * Given a hand, choose a card from it to play
	 * @param cards the cards to choose from
	 * @param state the state before all cards have been picked
	 * @return the card chosen to play
	 */
	public Card chooseCard(Player player, Card[] cards, GameState state);
}
