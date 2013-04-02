package ai;

import java.util.Random;

import main.GameState;
import players.Player;
import cards.Card;

/**
 * This class randomly chooses a course of action to take
 * @author Chris
 *
 */
public class SimpleAI implements AI {

	@Override
	public GameState choose(Player player, GameState[] states, Card card) {
		
		Random choice = new Random();
		return states[choice.nextInt(states.length)];
	}

	@Override
	public Card chooseCard(Player player, Card[] cards, GameState state) {
		
		Random choice = new Random();
		return cards[choice.nextInt(cards.length)];
	}

}
