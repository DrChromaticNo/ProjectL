package standard.actions;

import java.awt.Color;
import java.util.HashMap;

import players.Faction;
import players.Player;

import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the recruiter card
 * @author Chris
 * 
 * Day: Take 1 character from the den to your hand
 * Dusk: Choose 1 treasure
 * Night: N/A
 * End: N/A
 *
 */
public class Recruiter implements Action {

	@Override
	public GameState doAction(GameState state, Card card, int time) {
		if(time == Time.DAY)
		{
			state = carpenterDay(state, card);
		}
		else if(time == Time.DUSK)
		{
			PickTreasure temp = new PickTreasure();
			state = temp.doAction(state, card, time);
		}
		else if(time == Time.NIGHT)
		{
			// Do nothing
		}
		
		return state;
	}

	private GameState carpenterDay(GameState state, Card card)
	{
		Color faction = card.getFaction();
		if(state.getPlayer(faction).checkCPU())
		{
			HashMap<GameState, String> choiceMap = dayActionWithPhrases(state, card);
			
			GameState choice = state.getPlayer(faction)
					.chooseState(choiceMap.keySet().toArray(new GameState[0]), card);
			
			state.messageAllGUIs(choiceMap.get(choice));
			return choice;
		}
		else
		{
			while(true)
			{
				Player p = state.getPlayer(faction);
				if(p.getDen().isEmpty()) //there are no pirates in the den to recruit
				{
					state.messageAllGUIs("The Recruiter (" + card.abbreviate() + 
							") had nobody to recruit");
					return state;
				}
				
				Card choice = p.getGUI().makeChoice("Choose one of these cards to recruit from your den:",
						p.getDen().toArray(new Card[0]));
				
				for(Card c : p.getDen())
				{
					if(c.equals(choice)) //we remove the card chosen and add it back to the hand
					{
						state.messageAllGUIs("The Recruiter (" + card.abbreviate() + 
								") recruited " + c.abbreviate() + " from " + Faction.getPirateName(faction) +
								"'s den to their hand");
						state.getPlayer(faction).removeFromDen(c);
						c.resetPhases();
						state.getPlayer(faction).addToHand(c);
						return state;
					}
				}
			}
		}
	}
	
	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			states = dayActionWithPhrases(new GameState(state), card).keySet().toArray(new GameState[0]);
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
	
	/**
	 * Get all the possible recruiting options and text to display
	 * @param state the state before choosing all the possible recruiting options
	 * @param card the card doing the recruiting
	 * @return the map of states to phrases
	 */
	private HashMap<GameState, String> dayActionWithPhrases(GameState state, Card card)
	{
		HashMap<GameState, String> choiceMap = new HashMap<GameState, String>();
		Color faction = card.getFaction();
		Player p = state.getPlayer(faction);
		if(p.getDen().isEmpty()) //there are no pirates in the den to recruit
		{
			choiceMap.put(state, "The Recruiter (" + card.abbreviate() + 
					") had nobody to recruit");
			return choiceMap;
		}
		for(Card c : p.getDen())
		{
			GameState tempState = new GameState(state);
			tempState.getPlayer(faction).removeFromDen(c);
			c.resetPhases();
			tempState.getPlayer(faction).addToHand(c);
			choiceMap.put(tempState, "The Recruiter (" + card.abbreviate() + 
					") recruited " + c.abbreviate() + " from " + Faction.getPirateName(faction) +
					"'s den to their hand");
		}
		return choiceMap;
	}

	@Override
	public int score(GameState state, Card card) {
		return 0;
	}

}
