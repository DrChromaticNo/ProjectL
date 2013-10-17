package standard.actions;

import java.awt.Color;

import players.Faction;
import players.Player;
import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the carpenter card
 * @author Chris
 * 
 * Day: Lose half your gold
 * Dusk: Choose 1 treasure
 * Night: N/A
 * End: Score 10 gold
 *
 */
public class Carpenter implements Action {

	@Override
	public GameState doAction(GameState state, Card card, int time) 
	{
		if(time == Time.DAY)
		{
			state = carpenterDay(state, card);
		}
		else if(time == Time.DUSK)
		{
			PickTreasure pick = new PickTreasure();
			state = pick.doAction(state, card, time);
		}
		else if(time == Time.NIGHT)
		{
			//Nothing to do
		}
		
		return state;
	}
	
	private GameState carpenterDay(GameState state, Card card)
	{
		Color faction = card.getFaction();
		Player player = state.getPlayer(faction);
		state.messageAllGUIs(Faction.getPirateName(faction) + " lost " + player.getGold()/2 + 
				" gold due to their Carpenter (" + card.abbreviate() + ")");
		player.addGold(-player.getGold()/2);
		return state;
	}
	
	//The only difference here is no text output
	private GameState carpenterDayAll(GameState state, Card card)
	{
		Color faction = card.getFaction();
		Player player = state.getPlayer(faction);
		player.addGold(-player.getGold()/2);
		return state;
	}

	@Override
	public int score(GameState state, Card card) 
	{
		return 10;
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			states[0] = carpenterDayAll(new GameState(state), card);
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

}
