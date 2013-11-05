package standard.actions;

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

	public static final String NAME = "Cabin Boy";
	
	@Override
	public GameState doAction(GameState state, Card card, int time) {
		
		if(time == Time.DUSK)
		{
			state.log("The Cabin Boy (" + card.abbreviate() + ") chooses no treasures");
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

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDesc() {
		return "Day: N/A" +
		"\nDusk: N/A" + 
		"\nNight: N/A" +
		"\nEnd: N/A\n";
	}

}
