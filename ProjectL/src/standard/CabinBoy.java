package standard;

import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the cabin boy card
 * @author Chris
 * 
 * Day: N/A
 * Dusk: N/A
 * Night: N/A
 * End: N/A
 *
 */
public class CabinBoy implements Action {

	@Override
	public GameState doAction(GameState state, Card card, int time) {
		
		if(time == Time.EVENING)
		{
			System.out.println("\nThe Cabin Boy (" + card.abbreviate() + ") chooses no treasures\n");
		}
		
		return state; //The cabin boy never does anything at day, dusk, or night
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		GameState[] states = new GameState[1];
		states[0] = state;
		return states;
	}

	@Override
	public int score(GameState state, Card card) {
		return 0;
	}

}
