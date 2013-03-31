package ai;

import java.util.Random;

import main.GameState;
import players.Player;
import score.ScoreCounter;
import score.TreasureBag;
import cards.Card;
import cards.Deck;

/**
 * This class randomly chooses a course of action to take
 * @author Chris
 *
 */
public class SimpleAI implements AI {

	@Override
	public GameState choose(Player player, GameState[] states, Deck deck,
			TreasureBag bag, ScoreCounter counter) {
		
		Random choice = new Random();
		return states[choice.nextInt(states.length)];
	}

	@Override
	public Card chooseCard(Player player, Card[] cards, GameState state,
			Deck deck, TreasureBag bag, ScoreCounter counter) {
		
		Random choice = new Random();
		return cards[choice.nextInt(cards.length)];
	}

}
