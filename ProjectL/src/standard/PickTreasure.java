package standard;

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
			System.out.println(Faction.getPirateName(faction) + " chose " + choice);
			
			//There are a few special cases that we need to case on (with standard rules)
			
			if(choice.equals(Treasure.OFFICER))
			{
				System.out.println("A Spanish officer killed the ");
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
			return state.getPlayer(faction).chooseState(
					allActions(new GameState(state), card, time), card);
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
			
			Player rightP = null;
			if(playerIndex+1 < state.getPlayerList().length)
			{
				rightP = state.getPlayerList()[playerIndex+1];
			}
			
			if(leftP != null || rightP != null) //if there are neighbors
			{
				//we figure out if they have killable pirates
				String choice = "";
				while(true)
				{
					HashSet<Card> leftSet = new HashSet<Card>();
					if(leftP != null)
					{
						System.out.println(Faction.getPirateName(leftP.getFaction()) + " has ");
						for(Card c : leftP.getDen())
						{
							System.out.print(" " + c.abbreviate() + " ");
							leftSet.add(c);
						}
					}
					
					HashSet<Card> rightSet = new HashSet<Card>();
					if(leftP == null || 
							(rightP != null && !leftP.getFaction().equals(rightP.getFaction())))
					{
						System.out.println(Faction.getPirateName(rightP.getFaction()) + " has ");
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
						System.out.println("Choose a pirate to kill: ");
					
						DebugMenu menu = new DebugMenu();
						
						choice = menu.launch(state);
						
						for(Card c : leftSet)
						{
							if(c.abbreviate().equals(choice))
							{
								state.getPlayer(leftP.getFaction()).removeFromDen(c);
								state.getPlayer(leftP.getFaction()).addToDiscard(c);
								return state;
							}
						}
						
						for(Card c : rightSet)
						{
							if(c.abbreviate().equals(choice))
							{
								state.getPlayer(rightP.getFaction()).removeFromDen(c);
								state.getPlayer(rightP.getFaction()).addToDiscard(c);
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
	private GameState[] saberActionCPU(GameState state, Color faction)
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
		
		Player rightP = null;
		if(playerIndex+1 < state.getPlayerList().length)
		{
			rightP = state.getPlayerList()[playerIndex+1];
		}
		
		//collect a list of killable pirates
		
		HashSet<Card> leftSet = new HashSet<Card>();
		if(leftP != null)
		{
			for(Card c : leftP.getDen())
			{
				leftSet.add(c);
			}
		}
		
		HashSet<Card> rightSet = new HashSet<Card>();
		if(leftP == null || 
				(rightP != null && !leftP.getFaction().equals(rightP.getFaction())))
		{
			for(Card c : rightP.getDen())
			{
				rightSet.add(c);
			}
		}
		
		if(leftSet.isEmpty() && rightSet.isEmpty()) //if there are none
		{
			GameState[] choices = new GameState[1];
			choices[0] = state;
			return choices;
		}
		
		HashSet<GameState> choices = new HashSet<GameState>();
		
		//otherwise create the list of all possible states
		
		for(Card c : leftSet)
		{
			GameState tempState = new GameState(state);
			tempState.getPlayer(leftP.getFaction()).removeFromDen(c);
			tempState.getPlayer(leftP.getFaction()).addToDiscard(c);
			choices.add(tempState);
		}
		
		for(Card c : rightSet)
		{
			GameState tempState = new GameState(state);
			tempState.getPlayer(rightP.getFaction()).removeFromDen(c);
			tempState.getPlayer(rightP.getFaction()).addToDiscard(c);
			choices.add(tempState);
		}
		
		return choices.toArray(new GameState[choices.size()]);
	}

	@Override
	public int score(GameState state, Card card) {
		return 0; //this should never get called, unless some card maps directly to this action
		//(which would be theoretically possible but weird)
	}

	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		
		Color faction = card.getFaction();
		Loot Lbag = state.getBoard().getLoot(state.getDay());
		HashSet<GameState> choiceSet = new HashSet<GameState>();
		for(String s : Treasure.allTreasures())
		{
			if(Lbag.countTreasure(s) != 0)
			{
				GameState tempState = new GameState(state);
				Lbag = tempState.getBoard().getLoot(state.getDay());
				Lbag.addLoot(s, -1);
				tempState.getBoard().setLoot(tempState.getDay(), Lbag);
				tempState.getPlayer(faction).getLoot().addLoot(s, 1);
				
				if(s.equals(Treasure.OFFICER))
				{
					tempState.getBoard().removeCard(card);
					tempState.getPlayer(faction).addToDiscard(card);
					choiceSet.add(tempState);
				}
				else if(tempState.equals(Treasure.SABER))
				{
					for(GameState g : saberActionCPU(tempState, faction))
					{
						choiceSet.add(g);
					}
				}
				else
				{
					choiceSet.add(tempState);
				}
			}
		}
		
		return choiceSet.toArray(new GameState[choiceSet.size()]);
	}


}
