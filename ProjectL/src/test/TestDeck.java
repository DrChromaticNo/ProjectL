package test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
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
		ArrayList<Integer> cards = new ArrayList<Integer>(40);
		for(int i = 0; i < 30; i++)
		{
			cards.add(9);
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
				carpenterDay(state, card.getFaction());
			}
			else if(time == Time.EVENING)
			{
				chooseTreasure(state, card.getFaction());
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
	private static void carpenterDay(GameState state, Color faction)
	{
		Player player = state.getPlayer(faction);
		System.out.println(Faction.getPirateName(faction) + " lost " + player.getGold()/2 + 
				" gold due to their Carpenter!");
		player.addGold(-player.getGold()/2);
	}
	
	private static void chooseTreasure(GameState state, Color faction)
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
				System.out.println("The following treasures are availible: ");
				for(String s : Treasure.allTreasures())
				{
					if(bag.countTreasure(s) != 0)
					{
						allZero = false;
						System.out.println(" " + s + " x" + bag.countTreasure(s));
					}
				}
				
				if(!allZero)
				{
					System.out.println("Please choice one treasure:");
					choice = inputScanner.next();
				}
			}
			
			inputScanner.close();
			bag.addLoot(choice, -1);
			state.getPlayer(faction).getLoot().addLoot(choice, 1);
			System.out.println(Faction.getPirateName(faction) + " chose " + choice);
		}
	}
	

}
