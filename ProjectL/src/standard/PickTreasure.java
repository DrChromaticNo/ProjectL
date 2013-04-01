package standard;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

import players.Faction;
import players.Player;
import main.GameState;
import score.Loot;
import score.ScoreCounter;
import score.Treasure;
import score.TreasureBag;
import test.DebugMenu;
import cards.Action;
import cards.Card;
import cards.Deck;

/**
 * The class to choose a generic treasure from the ship
 * @author Chris
 *
 */
public class PickTreasure implements Action {

	@Override
	public GameState doAction(GameState state, Card card, TreasureBag bag, 
			Deck deck, ScoreCounter counter, int time) 
	{
		Color faction = card.getFaction();
		if(!state.getPlayer(faction).checkCPU())
		{	
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
				
				if(!allZero)
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
					allActions(new GameState(state), card, bag, deck, counter, time),
					card, deck, bag, counter);
		}
		return state;
	}
	
	private GameState saberAction(GameState state, Color faction)
	{
		if(!state.getPlayer(faction).checkCPU())
		{
			int playerIndex = 0;
			for(int i = 0; i < state.getPlayerList().length; i++)
			{
				if(state.getPlayerList()[i].getFaction().equals(faction))
				{
					playerIndex = i;
				}
			}
			
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
			
			if(leftP != null || rightP != null)
			{
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
					
					if(leftSet.isEmpty() && rightSet.isEmpty())
					{
						return state;
					}
					
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
			throw new RuntimeException("for some reason, there is only one player!");
		}
		else
		{
			return state;
		}
	}
	
	private GameState[] saberActionCPU(GameState state, Color faction)
	{
		int playerIndex = 0;
		for(int i = 0; i < state.getPlayerList().length; i++)
		{
			if(state.getPlayerList()[i].getFaction().equals(faction))
			{
				playerIndex = i;
			}
		}
		
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
		
		if(leftSet.isEmpty() && rightSet.isEmpty())
		{
			GameState[] choices = new GameState[1];
			choices[0] = state;
			return choices;
		}
		
		HashSet<GameState> choices = new HashSet<GameState>();
		
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
		return 0;
	}

	@Override
	public GameState[] allActions(GameState state, Card card, TreasureBag bag,
			Deck deck, ScoreCounter counter, int time) {
		
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
