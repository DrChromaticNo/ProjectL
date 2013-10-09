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
		if(!state.getPlayer(faction).checkCPU())
		{	
			//First we just display all the possible choices for treasure
			String choice = "";
			Loot Lbag = state.getBoard().getLoot(state.getDay());
			boolean allZero = false;
			while(Lbag.countTreasure(choice) == 0 && !allZero)
			{
				allZero = true;
				int treasureMap = 0;
				HashMap<Integer, String> tMap = new HashMap<Integer, String>();
				
				System.out.println("The following treasures are availible: ");
				
				for(String s : Treasure.allTreasures())
				{
					if(Lbag.countTreasure(s) != 0)
					{
						allZero = false;
						System.out.println(treasureMap + ": " + s + " x" + Lbag.countTreasure(s));
						tMap.put(treasureMap, s);
						treasureMap++;
					}
				}
				
				if(!allZero) //providing there are any, we ask the player to choose one of them
				{
					System.out.println("Please choose one treasure:");
					DebugMenu menu = new DebugMenu();
					int pick = Integer.parseInt(menu.launch(state));
					choice = tMap.get(pick);
				}
			}
			
			Lbag.addLoot(choice, -1);
			state.getBoard().setLoot(state.getDay(), Lbag);
			state.getPlayer(faction).getLoot().addLoot(choice, 1);
			System.out.println("\n" + Faction.getPirateName(faction) + " chose " + choice + "\n");
			
			//There are a few special cases that we need to case on (with standard rules)
			
			if(choice.equals(Treasure.OFFICER))
			{
				state.getBoard().removeCard(card);
				state.getPlayer(faction).addToDiscard(card);
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
			
			System.out.println(choiceMap.get(choice));
			
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
				String choice = "";
				while(true)
				{
					HashSet<Card> leftSet = new HashSet<Card>();
					if(!leftP.getFaction().equals(faction))
					{
						System.out.println(Faction.getPirateName(leftP.getFaction()) + " has \n");
						for(Card c : leftP.getDen())
						{
							System.out.print(" " + c.abbreviate() + " ");
							leftSet.add(c);
						}
					}
					
					HashSet<Card> rightSet = new HashSet<Card>();
					if(!rightP.getFaction().equals(faction) && !rightP.getFaction().equals(leftP.getFaction()))
					{
						System.out.println(Faction.getPirateName(rightP.getFaction()) + " has \n");
						for(Card c : rightP.getDen())
						{
							System.out.print(" " + c.abbreviate() + " ");
							rightSet.add(c);
						}
					}
					
					if(leftSet.isEmpty() && rightSet.isEmpty()) //if there are none we just return
					{
						return state;
					}
					
					while(true) //otherwise we prompt the user to kill one of them
					{
						System.out.println("\nChoose a pirate to kill: ");
					
						DebugMenu menu = new DebugMenu();
						
						choice = menu.launch(state);
						
						for(Card c : leftSet)
						{
							if(c.abbreviate().equals(choice))
							{
								state.getPlayer(leftP.getFaction()).removeFromDen(c);
								state.getPlayer(leftP.getFaction()).addToDiscard(c);
								System.out.println(" and killed " + c.abbreviate());
								return state;
							}
						}
						
						for(Card c : rightSet)
						{
							if(c.abbreviate().equals(choice))
							{
								state.getPlayer(rightP.getFaction()).removeFromDen(c);
								state.getPlayer(rightP.getFaction()).addToDiscard(c);
								System.out.println(" and killed " + c.abbreviate());
								return state;
							}
						}
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
			choices.put(state, "\n" + Faction.getPirateName(faction) + " chose saber(s) but couldn't kill anyone \n");
			return choices;
		}
		
		//otherwise create the list of all possible states
		
		for(Card c : leftSet)
		{
			GameState tempState = new GameState(state);
			tempState.getPlayer(leftP.getFaction()).removeFromDen(c);
			tempState.getPlayer(leftP.getFaction()).addToDiscard(c);
			choices.put(tempState, "\n" + Faction.getPirateName(faction) 
					+ " chose saber(s) and killed " + c.abbreviate() + "\n");
		}
		
		for(Card c : rightSet)
		{
			GameState tempState = new GameState(state);
			tempState.getPlayer(rightP.getFaction()).removeFromDen(c);
			tempState.getPlayer(rightP.getFaction()).addToDiscard(c);
			choices.put(tempState, "\n" + Faction.getPirateName(faction) 
					+ " chose saber(s) and killed " + c.abbreviate() + "\n");
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
					choiceSet.put(tempState, "\n" + Faction.getPirateName(faction) + " chose " + choice + "\n");
				}
				else if(s.equals(Treasure.SABER))
				{
					HashMap<GameState, String> saberList = saberActionCPU(tempState, faction);
					choiceSet.putAll(saberList);
				}
				else
				{
					choiceSet.put(tempState, "\n" + Faction.getPirateName(faction) + " chose " + choice + "\n");
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
