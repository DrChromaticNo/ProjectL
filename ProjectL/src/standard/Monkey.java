package standard;

import java.awt.Color;

import players.Faction;
import players.Player;
import score.Treasure;

import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * Class that represents the monkey card
 * 
 * Day: Transfer all cursed relics from your den to the den of the player to your left
 * Evening: Choose 1 treasure
 * Night: N/A
 * End: N/A
 * 
 * @author Chris
 *
 */

public class Monkey implements Action {

	@Override
	public GameState doAction(GameState state, Card card, int time) {
		
		if(time == Time.DAY)
		{
			state = monkeyDay(state, card);
		}
		else if(time == Time.EVENING)
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
	 * Transfers cursed relics from your den to the den to your left
	 * @param state the state of the game before this action
	 * @param card the card doing this action
	 * @return the state after the relics have been transferred
	 */
	private GameState monkeyDay(GameState state, Card card)
	{
		Color faction = card.getFaction();
		//get the players' index
		int playerIndex = 0;
		for(int i = 0; i < state.getPlayerList().length; i++)
		{
			if(state.getPlayerList()[i].getFaction().equals(faction))
			{
				playerIndex = i;
			}
		}
		
		//Determine left neighbor
		
		Player leftP = null;
		if(playerIndex-1 >= 0)
		{
			leftP = state.getPlayerList()[playerIndex-1];
		}
		else
		{
			leftP = state.getPlayerList()[state.getPlayerList().length-1];
		}
		
		if(leftP.getFaction().equals(faction))
		{
			throw new RuntimeException("only one player!");
		}
		else //transfer relics to the left
		{
			int relics = state.getPlayer(faction).getLoot().countTreasure(Treasure.RELIC);
			state.getPlayer(faction).getLoot().addLoot(Treasure.RELIC, -relics);
			state.getPlayer(leftP.getFaction()).getLoot().addLoot(Treasure.RELIC, relics);
			
//			System.out.println(Faction.getPirateName(faction) +  " transferred " + relics + 
//					" relics to " + Faction.getPirateName(leftP.getFaction()));
		}
		
		return state;
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			states[0] = doAction(new GameState(state), card, time);
		}
		else if(time == Time.EVENING)
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
