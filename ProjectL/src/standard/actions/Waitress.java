package standard.actions;

import java.awt.Color;

import players.Player;
import score.Treasure;

import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the waitress card
 * @author Chris
 * 
 * Day: N/A
 * Dusk: Choose 1 treasure
 * Night: If you have at least one Treasure Map, you can sell it for 3 gold
 * End: N/A
 *
 */
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
				state.messageAllGUIs("The Waitress (" + card.abbreviate() + ") sold 1 map for 3 gold.");
			}
			
			return choice;
		}
		else
		{
			if(choices.length > 1)
			{	
				String ans = player.getGUI().makeChoice("Do you want to sell 1 map for 3 gold?", 
						new String[]{"Yes", "No"});
				
				if(ans.equals("Yes"))
				{
					state.messageAllGUIs("The Waitress (" + card.abbreviate() + ") sold 1 map for 3 gold.");
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

	@Override
	public String getName() {
		return "Waitress";
	}

	@Override
	public String getDesc() {
		return "Day: N/A" +
		"\nDusk: Choose 1 treasure" + 
		"\nNight: If you have at least one treasure map, you can sell it for 3 gold" +
		"\nEnd: N/A\n";
	}

}
