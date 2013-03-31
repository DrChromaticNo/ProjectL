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
import score.ScoreCounter;
import score.Treasure;
import score.TreasureBag;
import standard.Carpenter;
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
		ArrayList<Integer> cards = new ArrayList<Integer>(30);
		for(int i = 0; i < 30; i++)
		{
			cards.add(new Integer(9));
		}
		
		return cards;
	}

	@Override
	public GameState doPhase(int time, Card card, GameState state,
			TreasureBag bag, ScoreCounter counter) {
		
		if(actionMap.containsKey(card.getValue()))
		{
			state = actionMap.get(card.getValue()).doAction(state, card, bag, this, counter, time);
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
	
	private static HashMap<Integer, Action> createCardActionMap()
	{
		HashMap<Integer, Action> map = new HashMap<Integer, Action>();
		
		map.put(9, new Carpenter());
		
		return map;
	}

	@Override
	public GameState[] getPossiblePhases(int time, Card card, GameState state,
			TreasureBag bag, ScoreCounter counter) {
		
		GameState[] states = new GameState[1];
		states[0] = state;
		if(actionMap.containsKey(card.getValue()))
		{
			states = actionMap.get(card.getValue()).allActions(state, card, bag, this, counter, time);
		}
		return states;
	}
}
