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
 * Class that represents the parrot card
 * 
 * Day: The parrot card is discarded and replaced by a card from your hand (in the appropriate place)
 * Dusk: N/A
 * Night: N/A
 * End: N/A
 * 
 * @author Chris
 *
 */

public class Parrot implements Action {

	public static final String NAME = "Parrot";
	
	@Override
	public GameState doAction(GameState state, Card card, int time) {
		
		if(time == Time.DAY)
		{
			state = parrotDay(state, card);
		}
		else if(time == Time.DUSK)
		{
			//Do nothing
		}
		else if(time == Time.NIGHT)
		{
			//Do nothing
		}
		
		return state;
	}
	
	/**
	 * Performs the parrot day action
	 * @param state the state to perform the action in
	 * @param card the parrot card
	 * @return the parrot being discarded and a new card played
	 */
	private GameState parrotDay(GameState state, Card card)
	{
		Color faction = card.getFaction();
		Player p = state.getPlayer(faction);
		
		if(p.getHand().isEmpty())
		{
			GameState end = new GameState(state);
			end.getBoard().removeCard(card);
			end.getPlayer(faction).addToDiscard(card);
			state.log(Faction.getPirateName(faction) + "'s " +
					"Parrot (" + card.abbreviate() + ") died but nothing could take its place");
			return end;
		}
		
		if(p.checkCPU())
		{
			HashMap<GameState, String> choiceMap = new HashMap<GameState, String>();
			for(Card c : p.getHand())
			{
				GameState choice = getParrotState(state, card, c);
				choiceMap.put(choice, 
						Faction.getPirateName(faction) + "'s Parrot (" + card.abbreviate() + 
						") died and " + c.abbreviate() + 
						" took its place");
			}
			GameState result = 
					p.chooseState(choiceMap.keySet().toArray(new GameState[0]), card);
			state.log(choiceMap.get(result));
			return result;
		}
		else
		{
			
			Card choice = p.getGUI().makeChoice("Choose a card from your hand to replace the parrot: ",
					p.getHand().toArray(new Card[0]));
			GameState end = getParrotState(state, card, choice);
			
			state.log(Faction.getPirateName(faction) + "'s Parrot (" + card.abbreviate() + 
					") died and " + choice.abbreviate() + 
					" took its place");
			
			return end;
		}
	}
	
	/**
	 * Removes the parrot and plays the choice in its place
	 * @param state the state before the action
	 * @param parrot the parrot card
	 * @param choice the card being played from the hand to replace the parrot
	 * @return the state after the action
	 */
	private GameState getParrotState(GameState state, Card parrot, Card choice)
	{
		GameState endState = new GameState(state);
		
		Color faction = parrot.getFaction();
		Player p = endState.getPlayer(faction);
		p.removeFromHand(choice);
		p.addToDiscard(parrot);
		endState.getBoard().removeCard(parrot);
		endState.getBoard().addCard(choice);
		
		return endState;
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			Color faction = card.getFaction();
			Player p = state.getPlayer(faction);
			if(p.getHand().isEmpty())
			{
				GameState end = new GameState(state);
				end.getBoard().removeCard(card);
				end.getPlayer(faction).addToDiscard(card);
				states[0] = end;
			}
			else
			{
				states = new GameState[p.getHand().size()];
				int index = 0;
				for(Card c : p.getHand())
				{
					states[index] = getParrotState(state, card, c);
					index++;
				}
			}
		}
		else if(time == Time.DUSK)
		{
			//Nothing to do
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
		return "Day: The parrot card is discarded " +
		"and replaced by a card from your hand (in the appropriate place)"+
		"\nDusk: N/A" +
		"\nNight: N/A" +
	 	"\nEnd: N/A\n";
	}

}
