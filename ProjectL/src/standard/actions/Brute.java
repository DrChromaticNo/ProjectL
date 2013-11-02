package standard.actions;

import java.awt.Color;

import players.Faction;

import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the brute card
 * @author Chris
 * 
 * Day: Discard the highest ranked card on the ship
 * Dusk: Choose 1 treasure
 * Night: N/A
 * End: N/A
 *
 */
public class Brute implements Action {

	public static final String NAME = "Brute";
	private static final String DESC = "Day: Discard the highest ranked card on the ship" +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: N/A" +
			"\nEnd: Score 10 gold\n";
	
	@Override
	public GameState doAction(GameState state, Card card, int time) {
		if(time == Time.DAY)
		{
			state = bruteDay(state, card, true);
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
	
	/**
	 * The day action for the brute
	 * @param state the state the action is happening in
	 * @param card the card initiating the action
	 * @param output whether or not the method should output to the guis
	 * @return the state after the action has happened
	 */
	private GameState bruteDay(GameState state, Card card, boolean output)
	{
		GameState end = new GameState(state);
		//First, we need to find the highest ranked card
		
		Card[] deck = end.getBoard().getDeck();
		
		Card highest = end.getBoard().getDeck()[deck.length-1];
		
		end.getBoard().removeCard(highest);
		
		Color faction = card.getFaction();
		Color deadFaction = highest.getFaction();
		
		end.getPlayer(deadFaction).addToDiscard(highest);
		
		if(output)
		{
			if(card.equals(highest))
			{
				end.messageAllGUIs(Faction.getPirateName(faction) + "'s Brute (" 
						+ card.abbreviate() + ") knocked himself out!");
			}
			else
			{
				end.messageAllGUIs(Faction.getPirateName(faction) + "'s Brute (" 
						+ card.abbreviate() + ") knocked out " + highest.abbreviate() + "!");
			}
		}
		
		return end;
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			states = new GameState[]{bruteDay(state, card, false)};
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
		return 0;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDesc() {
		return DESC;
	}

}
