package standard.actions;

import java.awt.Color;

import players.Faction;
import players.Player;

import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the french officer card
 * @author Chris
 * 
 * Day: Gain 5 gold if you have less than 9 gold
 * Dusk: Choose 1 Treasure
 * Night: N/A
 * End: N/A
 *
 */
public class FrenchOfficer implements Action {

	public static final String NAME = "French Officer";
	
	@Override
	public GameState doAction(GameState state, Card card, int time) {
		
		if(time == Time.DAY)
		{
			state = frenchDay(state, card, true);
		}
		else if(time == Time.DUSK)
		{
			PickTreasure temp = new PickTreasure();
			state = temp.doAction(state, card, time);
		}
		else if(time == Time.NIGHT)
		{
			//Do nothing
		}
		
		return state;
	}

	/**
	 * Performs the French Officers' day action
	 * @param state the gamestate before the action
	 * @param card the french officer card
	 * @param output boolean to determine if the action should print to the log or not
	 * @return the state after the day action has occured
	 */
	private GameState frenchDay(GameState state, Card card, boolean output) {
		Color faction = card.getFaction();
		Player player = state.getPlayer(faction);
		int gold = player.getGold();
		if(gold >= 9)
		{
			if(output)
			{
				state.messageAllGUIs(Faction.getPirateName(faction) + " has " + gold + " gold so the " +
					"French Officer (" + card.abbreviate() + ") does nothing");
			}
		}
		else
		{
			GameState moreGold = new GameState(state);
			moreGold.getPlayer(faction).addGold(5);
			if(output)
			{
				state.messageAllGUIs(Faction.getPirateName(faction) + " has " + gold + " gold so the " +
					"French Officer (" + card.abbreviate() + ") adds 5 gold");
			}
			state = moreGold;
		}
		return state;
		
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			GameState frenchState = new GameState(state);
			states = new GameState[]{frenchDay(frenchState,card,false)};
		}
		else if(time == Time.DUSK)
		{
			PickTreasure temp = new PickTreasure();
			states = temp.allActions(new GameState(state), card, time);
		}
		else if(time == Time.NIGHT)
		{
			//Nothing to do
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
		return "Day: Gain 5 gold if you have less than 9 gold" +
		"\nDusk: Choose 1 treasure" + 
		"\nNight: N/A" +
		"\nEnd: N/A\n";
	}

}
