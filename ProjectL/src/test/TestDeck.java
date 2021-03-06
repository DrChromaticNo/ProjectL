package test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;


import players.Faction;

import main.GameState;
import standard.actions.*;
import cards.Action;
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
	private HashMap<Integer, Action> actionMap;
	
	public TestDeck()
	{
		abbrvMap = createAbbrvMap();
		silverMap = createSilverMap();
		actionMap = createCardActionMap();
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
		int totalCards = 15;
		ArrayList<Integer> cards = new ArrayList<Integer>(totalCards*2);
		for(int i = 1; i < totalCards+1; i++)
		{
			cards.add(i);
		}
		for(int i = totalCards+1; i < 30; i++)
		{
			cards.add(i-totalCards);
		}
		return cards;
	}

	@Override
	public GameState doPhase(int time, Card card, GameState state) {
		
		if(actionMap.containsKey(card.getValue()))
		{
			state = actionMap.get(card.getValue()).doAction(state, card, time);
		}
		return state;
	}

	@Override
	public int scoreCard(Card card, GameState state) {

		if(actionMap.containsKey(card.getValue()))
		{
			return actionMap.get(card.getValue()).score(state, card);
		}
		else
		{
			return 0;
		}
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
		
		map.put(1, new HashMap<Color, Integer>());
		
		map.get(1).put(Faction.YELLOW, 1);
		map.get(1).put(Faction.RED, 2);
		map.get(1).put(Faction.BLUE, 3);
		map.get(1).put(Faction.GREEN, 4);
		map.get(1).put(Faction.BLACK, 5);
		map.get(1).put(Faction.WHITE, 6);
		
		for(int i = 2; i <= 30; i++)
		{
			map.put(i, new HashMap<Color, Integer>());
			
			if(i % 6 == 1)
			{
				for(Color f : Faction.allFactions())
				{
					map.get(i).put(f, map.get(i-1).get(f));
				}
			}
			else
			{
				for(Color f : Faction.allFactions())
				{
					if(map.get(i-1).get(f) == 6)
					{
						map.get(i).put(f, 1);
					}
					else
					{
						map.get(i).put(f, map.get(i-1).get(f)+1);
					}
				}
			}
		}
		
		return map;
	}
	
	private static HashMap<Integer, Action> createCardActionMap()
	{
		HashMap<Integer, Action> map = new HashMap<Integer, Action>();
		
		map.put(1, new Parrot());
		map.put(2, new Monkey());
		map.put(3, new Beggar());
		map.put(4, new Recruiter());
		map.put(5, new CabinBoy());
		map.put(6, new Preacher());
		map.put(7, new Barkeep());
		map.put(8, new Waitress());
		map.put(9, new Carpenter());
		map.put(10, new FrenchOfficer());
		map.put(11, new VoodooWitch());
		map.put(12, new FreedSlave());
		map.put(13, new Mutineer());
		map.put(14, new Brute());
		map.put(15, new Gunner());
		
		return map;
	}

	@Override
	public GameState[] getPossiblePhases(int time, Card card, GameState state) {
		
		GameState[] states = new GameState[1];
		states[0] = state;
		if(actionMap.containsKey(card.getValue()))
		{
			states = actionMap.get(card.getValue()).allActions(state, card, time);
		}
		return states;
	}
	

	@Override
	public String getCardName(Card card)
	{
		return actionMap.get(card.getValue()).getName();
	}
	

	@Override
	public String getCardDesc(Card card)
	{
		return actionMap.get(card.getValue()).getDesc();
	}
	
	
}
