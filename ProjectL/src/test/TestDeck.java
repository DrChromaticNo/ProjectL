package test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import players.Faction;
import players.Player;

import main.GameState;
import main.Time;
import score.Loot;
import score.Treasure;
import score.TreasureBag;
import cards.Card;
import cards.Deck;

/**
 * A deck to help test the basic functionality of the game, only has 1 card (30 copies of it)
 * @author Chris
 *
 */
public class TestDeck implements Deck {

	private HashMap<Color, String> abbrvMap;
	private HashMap<Integer, HashMap<Color, Integer>> silverMap;
	
	public TestDeck()
	{
		abbrvMap = createAbbrvMap();
		silverMap = createSilverMap();
	}
	
	@Override
	public int getSilverNum(Color faction, int value) {
		
		if(silverMap.containsKey(value))
		{
			if(silverMap.get(value).containsKey(faction))
			{
				return silverMap.get(value).get(faction);
			}
		}
		
		throw new RuntimeException("that faction/value combination is not compatible with this deck");
		
	}

	@Override
	public ArrayList<Integer> allCards() {
		ArrayList<Integer> cards = new ArrayList<Integer>(30);
		for(int i = 0; i < 30; i++)
		{
			cards.add(new Integer(9));
		}
		
		return cards;
	}

	@Override
	public GameState doPhase(int time, Card card, GameState state,
			TreasureBag bag) {
		
		if(card.getValue() == 9)
		{
			if(time == Time.DAY)
			{
				state = carpenterDay(state, card.getFaction());
			}
			else if(time == Time.EVENING)
			{
				state = chooseTreasure(state, card.getFaction(), card);
			}
			else //it is night phase
			{
				//Do nothing
			}
		}
		
		return state;
	}

	@Override
	public int scoreCard(Card card, GameState state) {
		
		if(card.getValue() == 9)
		{
			return 10;
		}
		
		return 0;
	}

	@Override
	public String abbreviatedName(Card card) {
		
		return abbrvMap.get(card.getFaction()) + card.getValue();
	}

	/**
	 * Initializes the map to help create abbreviations of card names
	 * @return the map that maps (roughly) the first letter of each color to its color
	 */
	private static HashMap<Color, String> createAbbrvMap()
	{
		HashMap<Color, String> map = new HashMap<Color, String>();
		
		map.put(Faction.BLACK, "BK");
		map.put(Faction.BLUE, "BL");
		map.put(Faction.GREEN, "G");
		map.put(Faction.RED, "R");
		map.put(Faction.WHITE, "W");
		map.put(Faction.YELLOW, "Y");
		
		return map;
	}
	
	/**
	 * Creates the map to determine the silver number of a card
	 * @return the map that will determine the silver number of a card
	 */
	private static HashMap<Integer, HashMap<Color, Integer>> createSilverMap()
	{
		HashMap<Integer, HashMap<Color, Integer>> map = new HashMap<Integer, HashMap<Color, Integer>>();
		
		map.put(9, new HashMap<Color, Integer>());
		
		//note: there is a pattern to faction/color values in game,
		//when i make the real deck i should exploit that
		map.get(9).put(Faction.WHITE, 1);
		map.get(9).put(Faction.YELLOW, 2);
		map.get(9).put(Faction.RED, 3);
		map.get(9).put(Faction.BLUE, 4);
		map.get(9).put(Faction.GREEN, 5);
		map.get(9).put(Faction.BLACK, 6);
		
		return map;
	}
	
	/**
	 * Performs the day action for the carpenter
	 * @param state the state before the action
	 * @param faction the faction of the carpenter card
	 */
	private GameState carpenterDay(GameState state, Color faction)
	{
		Player player = state.getPlayer(faction);
		System.out.println();
		System.out.println(Faction.getPirateName(faction) + " lost " + player.getGold()/2 + 
				" gold due to their Carpenter!");
		System.out.println();
		player.addGold(-player.getGold()/2);
		return state;
	}
	
	/**
	 * Chooses a treasure from the day's remaining treasures
	 * @param state the state of the game before the treasure is chosen
	 * @param faction the faction of the player accessing the treasure
	 */
	private GameState chooseTreasure(GameState state, Color faction, Card card)
	{
		if(!state.getPlayer(faction).checkCPU())
		{
			Scanner inputScanner = new Scanner(System.in);
			
			String choice = "";
			Loot bag = state.getBoard().getLoot(state.getDay());
			boolean allZero = false;
			while(bag.countTreasure(choice) == 0 && !allZero)
			{
				allZero = true;
				int treasureMap = 0;
				HashMap<Integer, String> tMap = new HashMap<Integer, String>();
				
				System.out.println("The following treasures are availible: ");
				
				for(String s : Treasure.allTreasures())
				{
					if(bag.countTreasure(s) != 0)
					{
						allZero = false;
						System.out.println(treasureMap + ": " + s + " x" + bag.countTreasure(s));
						tMap.put(treasureMap, s);
						treasureMap++;
					}
				}
				
				if(!allZero)
				{
					System.out.println("Please choose one treasure:");
					int pick = inputScanner.nextInt();
					choice = tMap.get(pick);
				}
			}
			
			bag.addLoot(choice, -1);
			state.getBoard().setLoot(state.getDay(), bag);
			state.getPlayer(faction).getLoot().addLoot(choice, 1);
			System.out.println(Faction.getPirateName(faction) + " chose " + choice);
			
			if(choice.equals(Treasure.OFFICER))
			{
				System.out.println("The Spanish officer killed the ");
				state.getBoard().removeCard(card);
				state.getPlayer(faction).addToDiscard(card);
			}
			else if(choice.equals(Treasure.SABER))
			{
				
			}
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
			
			if(leftP != null && rightP != null)
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
							System.out.print(" " + abbreviatedName(c) + " ");
							leftSet.add(c);
						}
					}
					
					HashSet<Card> rightSet = new HashSet<Card>();
					if(rightP != null && !leftP.getFaction().equals(rightP.getFaction()))
					{
						System.out.println(Faction.getPirateName(rightP.getFaction()) + " has ");
						for(Card c : rightP.getDen())
						{
							System.out.print(" " + abbreviatedName(c) + " ");
							rightSet.add(c);
						}
					}
					
					if(leftSet.isEmpty() && rightSet.isEmpty())
					{
						return state;
					}
					
					System.out.println("Choose a pirate to kill: ");
					
					Scanner inputScanner = new Scanner(System.in);
					
					choice = inputScanner.next();
					
					for(Card c : leftSet)
					{
						if(abbreviatedName(c).equals(choice))
						{
							state.getPlayer(leftP.getFaction()).removeFromDen(c);
							state.getPlayer(leftP.getFaction()).addToDiscard(c);
							return state;
						}
					}
					
					for(Card c : rightSet)
					{
						if(abbreviatedName(c).equals(choice))
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

}
