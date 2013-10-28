package standard.actions;

import java.awt.Color;

import players.Faction;
import players.Player;
import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the voodoo witch card
 * @author Chris
 * 
 * Day: Get 2 gold per card in your discard
 * Dusk: Choose 1 Treasure
 * Night: N/A
 * End: N/A
 *
 */
public class VoodooWitch implements Action {

	@Override
	public GameState doAction(GameState state, Card card, int time) {
		
		if(time == Time.DAY)
		{
			state = VoodooDay(state, card, true);
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
	 * Does the voodoo witch's day action
	 * @param state the state to do the action in
	 * @param card the card initiating the action
	 * @param output if the action should output to the guis or not
	 * @return the state after performing the day action
	 */
	private GameState VoodooDay(GameState state, Card card, boolean output)
	{
		Color faction = card.getFaction();
		Player p = state.getPlayer(card.getFaction());
		
		int discards = p.getDiscard().size();
		
		GameState wealth = new GameState(state);
		
		if(output)
		{
			state.messageAllGUIs("The Voodoo Witch (" + card.abbreviate() + ") gives " 
					+ Faction.getPirateName(faction) + " " + 2*discards + " gold");
		}
		
		wealth.getPlayer(faction).addGold(2*discards);
		
		return wealth;
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			states[0] = VoodooDay(new GameState(state), card, false);
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
	
}
