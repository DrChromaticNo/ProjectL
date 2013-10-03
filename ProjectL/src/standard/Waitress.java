package standard;

import java.awt.Color;

import players.Faction;
import players.Player;
import score.Treasure;
import test.DebugMenu;

import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

public class Waitress implements Action {

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
			state = waitressNightAction(state, card);
		}
		return state;
	}
	
	/**
	 * Performs the waitress action
	 * @param state the state in which it occurs
	 * @param card the waitress card
	 * @return allows the player to sell one map for 3 gold
	 */
	private GameState waitressNightAction(GameState state, Card card)
	{
		Color faction = card.getFaction();
		Player player = state.getPlayer(faction);
		
		GameState[] choices = waitressNightChoices(state, card);
		
		if(player.checkCPU())
		{
			GameState sell = null;
			if(choices.length > 1)
			{
				sell = choices[1];
			}
			
			GameState choice = player.chooseState(choices, card);
			
			if(sell != null && sell.equals(choice))
			{
				System.out.println(Faction.getPirateName(faction) + " sold 1 map for 3 gold.");
			}
			
			return choice;
		}
		else
		{
			if(choices.length > 1)
			{
				System.out.println("Do you want to sell 1 map for 3 gold (y or n)?");
				
				DebugMenu menu = new DebugMenu();
				
				String ans = menu.launch(state);
				
				while(!ans.equals("y") && !ans.equals ("n"))
				{
					System.out.println("Do you want to sell 1 map for 3 gold (y or n)?");
					ans = menu.launch(state);
				}
				
				if(ans.equals("y"))
				{
					return choices[1];
				}
				else
				{
					return choices[0];
				}
			}
			else
			{
				return choices[0];
			}
		}
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
			states = temp.allActions(state, card, time);
		}
		else if(time == Time.NIGHT)
		{
			states = waitressNightChoices(state, card);
		}
		
		return states;
	}
	
	/**
	 * Creates a simple 1 or 2 choice array for the waitress action: do you sell a map or not?
	 * @param state the state you make the choice in
	 * @param card the waitress card
	 * @return a 1 length array with the default state if you have no maps, a 2 choice array if you do with the default
	 * state and the state corresponding to selling 1 map for 3 gold
	 */
	private GameState[] waitressNightChoices(GameState state, Card card)
	{
		Color faction = card.getFaction();
		Player player = state.getPlayer(faction);
		
		GameState[] choices = new GameState[1];
		choices[0] = state;
		
		if(player.getLoot().countTreasure(Treasure.MAP) > 0)
		{
			GameState possible = new GameState(state);
			possible.getPlayer(faction).getLoot().addLoot(Treasure.MAP, -1);
			possible.getPlayer(faction).addGold(3);
			choices = new GameState[2];
			
			choices[0] = state;
			choices[1] = possible;
		}
		
		return choices;
	}

	@Override
	public int score(GameState state, Card card) {
		return 0;
	}

}
