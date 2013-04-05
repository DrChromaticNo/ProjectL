package standard;

import java.awt.Color;

import players.Faction;

import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the carpenter card
 * @author Chris
 * 
 * Day: The player with the card of highest rank on the ship gives you 3 gold
 * Dusk: Choose 1 Treasure
 * Night: N/A
 * End: N/A
 *
 */
public class Beggar implements Action {

	@Override
	public GameState doAction(GameState state, Card card, int time) {
		if(time == Time.DAY)
		{
			state = beggarDayAction(state, card);
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
	
	private GameState beggarDayAction(GameState state, Card card)
	{
		//find the player who owes gold
		Color highest = state.getBoard().getDeck()
				[state.getBoard().getDeck().length-1].getFaction();
		
		if(highest.equals(card.getFaction())) //if that player is us we do nothing
		{
			System.out.println("\nThe beggar (" 
					+ card.abbreviate() + ") is the card of highest rank so nothing happens\n");
			
			return state;
		}
		else //otherwise we get 3 gold (or whatever is left)
		{
			if(state.getPlayer(highest).getGold() >= 3)
			{
				System.out.println("\nThe beggar (" 
						+ card.abbreviate() + ") gets 3 gold from " + Faction.getPirateName(highest) + "\n");
				state.getPlayer(highest).addGold(-3);
				state.getPlayer(card.getFaction()).addGold(3);
				return state;
			}
			else
			{
				int smallGold = state.getPlayer(highest).getGold();
				System.out.println("\nThe beggar (" 
						+ card.abbreviate() + ") gets " + smallGold 
						+ " gold from " + Faction.getPirateName(highest) + "\n");
				state.getPlayer(highest).addGold(-smallGold);
				state.getPlayer(card.getFaction()).addGold(smallGold);
				return state;
			}
		}
	}
	
	//this is the same as beggarDay but with no text output
	private GameState beggarDayAll(GameState state, Card card)
	{
		Color highest = state.getBoard().getDeck()
				[state.getBoard().getDeck().length-1].getFaction();
		
		if(highest.equals(card.getFaction()))
		{
			return state;
		}
		else
		{
			if(state.getPlayer(highest).getGold() >= 3)
			{
				state.getPlayer(highest).addGold(-3);
				state.getPlayer(card.getFaction()).addGold(3);
				return state;
			}
			else
			{
				int smallGold = state.getPlayer(highest).getGold();
				state.getPlayer(highest).addGold(-smallGold);
				state.getPlayer(card.getFaction()).addGold(smallGold);
				return state;
			}
		}
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			states[0] = beggarDayAll(new GameState(state), card);
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
