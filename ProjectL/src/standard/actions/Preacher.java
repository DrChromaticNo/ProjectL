package standard.actions;

import java.awt.Color;
import java.util.HashMap;

import score.Loot;
import score.Treasure;
import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the preacher card
 * @author Chris
 * 
 * Day: Lose all treasure except for 1
 * Dusk: Choose 1 treasure
 * Night: N/A
 * End: Score 5 gold
 *
 */
public class Preacher implements Action {

	public static final String NAME = "Preacher";
	
	@Override
	public GameState doAction(GameState state, Card card, int time) {
		
		if(time == Time.DAY)
		{
			state = preacherDay(state, card);
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
	
	private GameState preacherDay(GameState state, Card card)
	{
		if(state.getPlayer(card.getFaction()).checkCPU())
		{
			HashMap<GameState, String> choiceMap = allPreacherDay(state, card);
			
			GameState choice = state.getPlayer(card.getFaction())
					.chooseState(choiceMap.keySet().toArray(new GameState[0]), card);
			
			state.log(choiceMap.get(choice));
			
			return choice;
		}
		else
		{
			Color faction = card.getFaction();
			Loot bag = state.getPlayer(faction).getLoot();
			boolean allEmpty = true;
			
			for(String s : Treasure.allTreasures()) //list all treasures
			{
				if(bag.countTreasure(s) != 0)
				{
					allEmpty = false;
				}
			}
				
			if(allEmpty)
			{
				state.log("The Preacher (" + card.abbreviate() + 
						") had no treasures to choose from");
				return state;
			}
			
			String treasure = state.getPlayer(faction).getGUI().makeChoice("Choose one treasure to keep, " +
					"the rest will be discarded:", bag);
	
			state.log("The Preacher (" + card.abbreviate() + 
					") discarded all treasures besides 1 " + treasure);
			
			for(String s : Treasure.allTreasures())
			{
				if(s.equals(treasure))
				{
					state.getPlayer(faction).getLoot()
						.addLoot(s, (-state.getPlayer(faction).getLoot().countTreasure(s))+1);
				}
				else
				{
					state.getPlayer(faction).getLoot()
						.addLoot(s, -state.getPlayer(faction).getLoot().countTreasure(s));
				}
			}
			return state;
		}
	}
	
	/**
	 * Creates a map of all the possible choices for the preacher's day action to display phrases
	 * @param state the state before the preacher's day action
	 * @param card the card doing the action
	 * @return map of all the possible choices for the preacher's day action to display phrases
	 */
	private HashMap<GameState, String> allPreacherDay(GameState state, Card card)
	{
		Color faction = card.getFaction();
		HashMap<GameState, String> choiceMap = new HashMap<GameState, String>();
		Loot bag = state.getPlayer(faction).getLoot();
		boolean allEmpty = true;
		
		for(String s : Treasure.allTreasures()) //list all treasures
		{
			if(bag.countTreasure(s) != 0)
			{
				allEmpty = false;
			}
		}
		
		if(allEmpty)
		{
			choiceMap.put(state, "The Preacher (" + card.abbreviate() + 
					") had no treasures to choose from");
			return choiceMap;
		}
		
		for(String treasure : Treasure.allTreasures())
		{
			if(bag.countTreasure(treasure) != 0) //if we chose a valid treasure, remove all others and set it to 1
			{
				GameState tempState = new GameState(state);
				for(String s : Treasure.allTreasures())
				{
					if(s.equals(treasure))
					{
						tempState.getPlayer(faction).getLoot()
							.addLoot(s, (-state.getPlayer(faction).getLoot().countTreasure(s))+1);
					}
					else
					{
						tempState.getPlayer(faction).getLoot()
							.addLoot(s, -state.getPlayer(faction).getLoot().countTreasure(s));
					}
				}
				choiceMap.put(tempState, "The Preacher (" + card.abbreviate() + 
						") discarded all treasures besides 1 " + treasure);
			}
		}
		
		return choiceMap;
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			states = allPreacherDay(state, card).keySet().toArray(new GameState[0]);
		}
		else if(time == Time.DUSK)
		{
			PickTreasure temp = new PickTreasure();
			states = temp.allActions(state, card, time);
		}
		else if(time == Time.NIGHT)
		{
			//Do nothing
		}
		
		return states;
		
	}

	@Override
	public int score(GameState state, Card card) {
		return 5;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDesc() {
		return "Day: Lose all treasure except for 1" +
		"\nDusk: Choose 1 treasure" + 
		"\nNight: N/A" +
		"\nEnd: Score 5 gold\n";
	}

}
