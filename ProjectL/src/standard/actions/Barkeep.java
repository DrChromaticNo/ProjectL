package standard.actions;

import players.Faction;
import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the barkeep card
 * @author Chris
 * 
 * Day: N/A
 * Dusk: Choose 1 treasure
 * Night: Gain 1 gold
 * End: N/A
 *
 */
public class Barkeep implements Action {

	public static final String NAME = "Barkeep";
	
	@Override
	public GameState doAction(GameState state, Card card, int time) {
		if(time == Time.DAY)
		{
			//Do nothing
		}
		else if(time == Time.DUSK)
		{
			PickTreasure temp = new PickTreasure();
			state = temp.doAction(state, card, time);
		}
		else if(time == Time.NIGHT)
		{
			state.log(("The Barkeep (" + card.abbreviate() + ") gives " 
					+ Faction.getPirateName(card.getFaction()) + " 1 gold"));
			
			state = barNight(state, card);
		}
		
		return state;
	}
	
	private GameState barNight(GameState state, Card card)
	{
		state.getPlayer(card.getFaction()).addGold(1);
		return state;
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			//Do nothing
		}
		else if(time == Time.DUSK)
		{
			PickTreasure temp = new PickTreasure();
			states = temp.allActions(new GameState(state), card, time);
		}
		else if(time == Time.NIGHT)
		{
			states[0] = barNight(state, card);
		}
		
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
				"\nDusk: Choose 1 treasure" + 
				"\nNight: Gain 1 gold" +
				"\nEnd: N/A\n";
	}

}
