package ai;

import players.Player;
import score.ScoreCounter;
import score.TreasureBag;
import cards.Card;
import cards.Deck;
import main.GameState;

/**
 * The main interface for all AI players
 * @author Chris
 *
 */
public interface AI {

	/**
	 * Given a set of possible moves, select one to play
	 * @param states the set of potential moves
	 * @param deck the deck being used for this game
	 * @param bag the bag being used for this game
	 * @return the one to play
	 */
	public GameState choose(Player player, GameState[] states, Deck deck, TreasureBag bag, ScoreCounter counter);
	
	/**
	 * Given a hand, choose a card from it to play
	 * @param cards the cards to choose from
	 * @param state the state before all cards have been picked
	 * @param deck the deck being used for this game
	 * @return the card chosen to play
	 */
	public Card chooseCard(Player player, Card[] cards, GameState state, Deck deck);
}
