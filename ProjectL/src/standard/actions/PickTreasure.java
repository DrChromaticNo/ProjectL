package standard.actions;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

import players.Faction;
import players.Player;
import main.GameState;
import score.Loot;
import score.Treasure;
import test.DebugMenu;
import cards.Action;
import cards.Card;

/**
 * The class to choose a generic treasure from the ship
 * @author Chris
 *
 */
public class PickTreasure implements Action {

	@Override
	public GameState doAction(GameState state, Card card, int time) 
	{
		Color faction = card.getFaction();
		Player player = state.getPlayer(faction);
		if(!player.checkCPU())
		{	
			//First we just display all the possible choices for treasure
			String choice = "";
			Loot Lbag = state.getBoard().getLoot(state.getDay());
			choice = player.getGUI().makeChoice("Please choose one treasure:", Lbag);
			
			Lbag.addLoot(choice, -1);
			state.getBoard().setLoot(state.getDay(), Lbag);
			player.getLoot().addLoot(choice, 1);
			if(!choice.equals(Treasure.SABER))
			{
				state.messageAllGUIs(Faction.getPirateName(faction) + " chose " + choice);
			}
			
			//There are a few special cases that we need to case on (with standard rules)
			
			if(choice.equals(Treasure.OFFICER))
			{
				state.getBoard().removeCard(card);
				player.addToDiscard(card);
			}
			else if(choice.equals(Treasure.SABER))
			{
				state = saberAction(state, faction);
			}
		}
		else //if the player is a CPU, we generate all possible outcomes for treasure choice
		{	
			HashMap<GameState, String> choiceMap = allActionsWithPhrases(new GameState(state), card, time);
			
			GameState choice = state.getPlayer(faction)
					.chooseState(choiceMap.keySet().toArray(new GameState[0]), card);
			
			state.messageAllGUIs(choiceMap.get(choice));
			
			return choice;
		}
		return state;
	}
	
	/**
	 * Does the action for the saber in standard rules (killing)
	 * @param state the state before the saber action
	 * @param faction the faction weilding the saber
	 * @return the state after the saber action
	 */
	private GameState saberAction(GameState state, Color faction)
	{
		if(!state.getPlayer(faction).checkCPU())
		{
			//First we need to find the player's index in the list
			int playerIndex = 0;
			for(int i = 0; i < state.getPlayerList().length; i++)
			{
				if(state.getPlayerList()[i].getFaction().equals(faction))
				{
					playerIndex = i;
				}
			}
			
			//we then use this to determine the players to their left and right
			//and see if they have any killable pirates
			
			Player leftP = null;
			if(playerIndex-1 >= 0)
			{
				leftP = state.getPlayerList()[playerIndex-1];
			}
			else
			{
				leftP = state.getPlayerList()[state.getPlayerList().length-1];
			}
			
			Player rightP = null;
			if(playerIndex+1 < state.getPlayerList().length)
			{
				rightP = state.getPlayerList()[playerIndex+1];
			}
			else
			{
				rightP = state.getPlayerList()[0];
			}
			
			if(leftP != null || rightP != null) //if there are neighbors
			{
				//we figure out if they have killable pirates
				HashSet<Card> leftSet = new HashSet<Card>();
				if(!leftP.getFaction().equals(faction))
				{
					for(Card c : leftP.getDen())
					{
						leftSet.add(c);
					}
				}
					
				HashSet<Card> rightSet = new HashSet<Card>();
				if(!rightP.getFaction().equals(faction) && !rightP.getFaction().equals(leftP.getFaction()))
				{
					for(Card c : rightP.getDen())
					{
						rightSet.add(c);
					}
				}
					
				if(leftSet.isEmpty() && rightSet.isEmpty()) //if there are none we just return
				{
					state.messageAllGUIs(Faction.getPirateName(faction) + " chose a saber" +
							" but couldn't kill anyone");
					return state;
				}
					
				//otherwise we prompt the user to kill one of them
				
				HashSet<Card> total = new HashSet<Card>(leftSet);
				total.addAll(rightSet);
				
				Card choice = state.getPlayer(faction).getGUI().makeChoice("Since you chose a saber, " +
						"choose a pirate in one of your neighbor's dens to kill:", 
						total.toArray(new Card[total.size()]));
				
				
				for(Card c : leftSet)
				{
					if(c.equals(choice))
					{
						state.getPlayer(leftP.getFaction()).removeFromDen(c);
						state.getPlayer(leftP.getFaction()).addToDiscard(c);
						state.messageAllGUIs(Faction.getPirateName(faction) + " chose a saber" +
								" and killed " + c.abbreviate());
						return state;
					}
				}
				
				for(Card c : rightSet)
				{
					if(c.equals(choice))
					{
						state.getPlayer(rightP.getFaction()).removeFromDen(c);
						state.getPlayer(rightP.getFaction()).addToDiscard(c);
						state.messageAllGUIs(Faction.getPirateName(faction) + " chose a saber" +
								" and killed " + c.abbreviate());
						return state;
					}
				}
			}
			throw new RuntimeException("for some reason, there is only one player!");
		}
		else
		{
			return state;
		}
	}
	
	/**
	 * Does the same as saber action, but just collects the list of states after each possible pirate has been killed
	 * @param state the state before the saber action
	 * @param faction the faction of the player weilding the saber
	 * @return the list of possible post-killing states
	 */
	private HashMap<GameState, String> saberActionCPU(GameState state, Color faction)
	{
		//get the players' index
		int playerIndex = 0;
		for(int i = 0; i < state.getPlayerList().length; i++)
		{
			if(state.getPlayerList()[i].getFaction().equals(faction))
			{
				playerIndex = i;
			}
		}
		
		//determine their left and right neighbors
		
		Player leftP = null;
		if(playerIndex-1 >= 0)
		{
			leftP = state.getPlayerList()[playerIndex-1];
		}
		else
		{
			leftP = state.getPlayerList()[state.getPlayerList().length-1];
		}
		
		Player rightP = null;
		if(playerIndex+1 < state.getPlayerList().length)
		{
			rightP = state.getPlayerList()[playerIndex+1];
		}
		else
		{
			rightP = state.getPlayerList()[0];
		}
		
		//collect a list of killable pirates
		
		HashSet<Card> leftSet = new HashSet<Card>();
		if(!leftP.getFaction().equals(faction))
		{
			for(Card c : leftP.getDen())
			{
				leftSet.add(c);
			}
		}
		
		HashSet<Card> rightSet = new HashSet<Card>();
		if(!rightP.getFaction().equals(faction) || !rightP.equals(leftP.getFaction()))
		{
			for(Card c : rightP.getDen())
			{
				rightSet.add(c);
			}
		}
		
		HashMap<GameState, String> choices = new HashMap<GameState, String>();
		
		if(leftSet.isEmpty() && rightSet.isEmpty()) //if there are none
		{
			choices.put(state, Faction.getPirateName(faction) + " chose a saber but couldn't kill anyone");
			return choices;
		}
		
		//otherwise create the list of all possible states
		
		for(Card c : leftSet)
		{
			GameState tempState = new GameState(state);
			tempState.getPlayer(leftP.getFaction()).removeFromDen(c);
			tempState.getPlayer(leftP.getFaction()).addToDiscard(c);
			choices.put(tempState, Faction.getPirateName(faction) 
					+ " chose a saber and killed " + c.abbreviate());
		}
		
		for(Card c : rightSet)
		{
			GameState tempState = new GameState(state);
			tempState.getPlayer(rightP.getFaction()).removeFromDen(c);
			tempState.getPlayer(rightP.getFaction()).addToDiscard(c);
			choices.put(tempState, Faction.getPirateName(faction) 
					+ " chose a saber and killed " + c.abbreviate());
		}
		
		return choices;
	}

	@Override
	public int score(GameState state, Card card) {
		return 0; //this should never get called, unless some card maps directly to this action
		//(which would be theoretically possible but weird)
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		
		return state.getCounter().rankTreasures(allActionsWithTreasures(state, card, time), card);
	}
	
	/**
	 * A helper method to get display strings for the various treasure choices
	 * @param state the state to choose the possibilities from
	 * @param card the card doing the choosing
	 * @param time the time the choosing is happening (should always be dusk)
	 * @return the hashmap connecting states to possible display strings
	 */
	private HashMap<GameState, String> allActionsWithPhrases(GameState state, Card card, int time)
	{
		Color faction = card.getFaction();
		Loot Lbag = state.getBoard().getLoot(state.getDay());
		HashMap<GameState, String> choiceSet = new HashMap<GameState, String>();
		for(String s : Treasure.allTreasures())
		{
			if(Lbag.countTreasure(s) != 0)
			{
				GameState tempState = new GameState(state);
				Lbag = tempState.getBoard().getLoot(state.getDay());
				Lbag.addLoot(s, -1);
				tempState.getBoard().setLoot(tempState.getDay(), Lbag);
				tempState.getPlayer(faction).getLoot().addLoot(s, 1);
				String choice = s;
				
				if(s.equals(Treasure.OFFICER))
				{
					tempState.getBoard().removeCard(card);
					tempState.getPlayer(faction).addToDiscard(card);
					choiceSet.put(tempState, Faction.getPirateName(faction) + " chose " + choice);
				}
				else if(s.equals(Treasure.SABER))
				{
					HashMap<GameState, String> saberList = saberActionCPU(tempState, faction);
					choiceSet.putAll(saberList);
				}
				else
				{
					choiceSet.put(tempState, Faction.getPirateName(faction) + " chose " + choice);
				}
			}
		}
		
		return choiceSet;
	}
	
	/**
	 * A helper method to get mappings for the various treasure choices
	 * @param state the state to choose the possibilities from
	 * @param card the card doing the choosing
	 * @param time the time the choosing is happening (should always be dusk)
	 * @return the hashmap connecting states to treasures
	 **/
	private HashMap<GameState, String> allActionsWithTreasures(GameState state, Card card, int time)
	{
		Color faction = card.getFaction();
		Loot Lbag = state.getBoard().getLoot(state.getDay());
		HashMap<GameState, String> choiceSet = new HashMap<GameState, String>();
		for(String s : Treasure.allTreasures())
		{
			if(Lbag.countTreasure(s) != 0)
			{
				GameState tempState = new GameState(state);
				Lbag = tempState.getBoard().getLoot(state.getDay());
				Lbag.addLoot(s, -1);
				tempState.getBoard().setLoot(tempState.getDay(), Lbag);
				tempState.getPlayer(faction).getLoot().addLoot(s, 1);
				String choice = s;
				
				if(s.equals(Treasure.OFFICER))
				{
					tempState.getBoard().removeCard(card);
					tempState.getPlayer(faction).addToDiscard(card);
					choiceSet.put(tempState, Treasure.OFFICER);
				}
				else if(s.equals(Treasure.SABER))
				{
					HashMap<GameState, String> saberList = saberActionCPU(tempState, faction);
					for(GameState saberState : saberList.keySet())
					{
						choiceSet.put(saberState, Treasure.SABER);
					}
				}
				else
				{
					choiceSet.put(tempState, choice);
				}
			}
		}
		
		return choiceSet;
	}
}
