/**
 * @author: Chris 
 * The main game runnable, running this should run the game!
 */

package main;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import players.Faction;
import players.Player;

import score.ScoreCounter;
import score.TreasureBag;
import cards.Card;
import cards.Deck;

public class Game {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//create deck and treasurebag to use in game, fill these in later
		Deck gameDeck = null;
		
		TreasureBag gameBag = null;
		
		ScoreCounter score = null;
		
		int numPlayers = 2; //assume/require > 1, < 7
		Player[] playerList = new Player[numPlayers];
		
		ArrayList<Color> factionList = Faction.allFactions();
		
		if(factionList.size() > numPlayers)
		{
			throw new RuntimeException("there's too many players and not enough factions");
		}
		
		//randomly assign each player a faction
		for(int i = 0; i < numPlayers; i++)
		{
			playerList[i] = new Player(chooseFaction(factionList));
		}
		
		//now, create the gamestate
		GameState state = new GameState(playerList,new Board());
		
		run(state, gameDeck, gameBag, score);

	}
	
	/**
	 * Given an arraylist of factions, chooses a random one of them
	 * @param factionList the list of remaining factions
	 * @return a faction (color)
	 */
	private static Color chooseFaction(ArrayList<Color> factionList)
	{
		Random randomColor = new Random();
		int choice = randomColor.nextInt(factionList.size());
		return factionList.remove(choice);
	}
	
	/**
	 * The main game loop, iterates through 3 weeks of play
	 * @param state the initial state
	 * @param gameDeck the deck used for the game
	 * @param gameBag the bag used for the game
	 */
	public static void run(GameState state, Deck gameDeck, TreasureBag gameBag,
			ScoreCounter counter)
	{
		//get the list of all the drawable cards
		ArrayList<Integer> availibleCards = gameDeck.allCards();
		
		for(int week = 1; week <= 3; week++)
		{
			gameBag.resetBag();
			placeTreasures(state, gameBag);
			distributeInitialGold(state, 10);
			
			//this section either draws the initial hand (at the start of the game)
			//or adds six cards to the existing hands
			if(week == 1)
			{
				drawCards(state, availibleCards, 9, gameDeck);
			}
			else
			{
				drawCards(state, availibleCards, 6, gameDeck);
			}
			
			weekLoop(state, gameDeck, gameBag, counter);
		}
		
		int max = Integer.MIN_VALUE;
		HashSet<Player> winners = new HashSet<Player>();
		
		for(Player p : state.getPlayerList())
		{
			if(p.getScore() == max)
			{
				winners.add(p);
			}
			else if(p.getScore() > max)
			{
				winners.clear();
				winners.add(p);
				max = p.getScore();
			}
		}
		
		for(Player p : winners)
		{
			System.out.println(Faction.getPirateName(p.getFaction()) + 
					" won with a score of " + p.getScore() + "!");
		}
	}
	
	/**
	 * Places the treasures for the start of a week
	 * @param state the state of the game at the start of the week
	 * @param gameBag the treasure bag being used for the game
	 */
	private static void placeTreasures(GameState state, TreasureBag gameBag)
	{
		for(int i = 0; i < 6; i++)
		{
			for(Player p : state.getPlayerList())
			{
				state.getBoard().getLoot(i).addLoot(gameBag.randomTreasure(), 1);
			}
		}
	}
	
	/**
	 * Draws cards from the deck and deals them to each player
	 * @param state the state to distribute the cards in
	 * @param gameDeck the deck being used for this game
	 * @param numCards the # of cards to give to each player
	 */
	private static void drawCards(GameState state, ArrayList<Integer> availibleCards, 
			int numCards, Deck gameDeck)
	{
		if(availibleCards.size() > numCards)
		{
			throw new RuntimeException("not enough cards to draw from the deck!");
		}
		
		Random cardPicker = new Random();
		
		for(int i = 0; i < numCards-1; i++)
		{
			int card = availibleCards.remove(
					cardPicker.nextInt(availibleCards.size()));
			
			for(Player p : state.getPlayerList())
			{
				p.addToHand(new Card(p.getFaction(), card, gameDeck));
			}
		}
	}
	
	/**
	 * Gathers cards from the players and puts them on the board
	 * @param state the current state of the game
	 * @param gameDeck the deck being used with this game
	 */
	private static void pickCards(GameState state, Deck gameDeck)
	{
		HashSet<Card> chosenCards = new HashSet<Card>();
		
		for(Player p : state.getPlayerList())
		{
			Card choice = p.pickCard(state, gameDeck);
			if(choice != null)
			{
				chosenCards.add(choice);
			}
		}
		
		for(Card c : chosenCards)
		{
			state.getBoard().addCard(c);
		}
	}
	
	/**
	 * Helper method to do the night state for one particular player at a time
	 * @param state the game state at the start of the night phase
	 * @param faction the faction of the player we're working on
	 * @return the player after all his night phase actions have been completed
	 */
	private static Player nightPhaseHelper(GameState state, Color faction)
	{
		for(Player p : state.getPlayerList())
		{
			if(p.getFaction().equals(faction))
			{
				while(state.getTime() == Time.NIGHT)
				{
					Player player = state.getPlayer(faction);
					
					if(player != null)
					{
						ArrayList<Card> den = new ArrayList<Card>(player.getDen());
						Collections.sort(den);
						
						int index = den.size();
						
						while(index >= 0 && den.get(index).checkPhase(Time.NIGHT))
						{
							index--;
						}
						
						if(index < 0)
						{
							state.setTime(Time.PICK_CARDS);
						}
						else
						{
							state = den.get(index).nightAction(state);
						}
					}
				}
				
				return state.getPlayer(faction);
				
			}
		}
		
		return null;
	}
	
	/**
	 * Performs the actions for one week of play
	 * @param state the game state at the start of the week
	 * @param gameDeck the deck to use for this week
	 * @param gameBag the treasure bag to use for this week
	 */
	private static void weekLoop(GameState state, Deck gameDeck, TreasureBag gameBag, ScoreCounter counter)
	{
		for(int day = 0; day <= 5; day++)
		{
			state.setDay(day);
			
			if(state.getTime() == Time.PICK_CARDS)
			{
				pickCards(state, gameDeck);
				state.setTime(Time.DAY);
			}
			
			while(state.getTime() != Time.PICK_CARDS)
			{
				oneAction(state, gameDeck, gameBag);
			}
		}
		
		state = counter.score(new GameState(state));
		
		weekendClear(state);
	}
	
	/**
	 * Performs one action of the game
	 * @param state the gamestate before the action
	 * @param gameDeck the deck being used for this action
	 * @param gameBag the bag being used for this action
	 */
	private static void oneAction(GameState state, Deck gameDeck, TreasureBag gameBag)
	{
		System.out.println("Board: ");
		
		//NOTE: this traversal might be backwards? need to check which way the cards get sorted
		for(Card c : state.getBoard().getDeck())
		{
			System.out.println(gameDeck.abbreviatedName(c) + " ");
		}
		
		//this if statement performs either 1 day action or 1 evening action
		//or all of the night actions (since they happen concurrently)
		if(state.getTime() == Time.DAY)
		{
			Card[] deck = state.getBoard().getDeck();
			
			int index = 0;
			
			while(index < deck.length && deck[index].checkPhase(Time.DAY))
			{
				index++;
			}
			
			if(index >= deck.length)
			{
				state.setTime(Time.EVENING);
			}
			else
			{
				state = deck[index].dayAction(new GameState(state), gameBag);
			}
		}
		else if(state.getTime() == Time.EVENING)
		{
			Card[] deck = state.getBoard().getDeck();
			
			int index = deck.length-1;
				
			while(index >= 0 && deck[index].checkPhase(Time.EVENING))
			{
				index--;
			}
				
			if(index < 0)
			{
				state.setTime(Time.NIGHT);
			}
			else
			{
				state = deck[index].eveningAction(new GameState(state), gameBag);
			}
		}
		else //the only other options is it being Time.NIGHT
		{
			if(state.getBoard().getDeck().length != 0)
			{
				throw new RuntimeException("Board isn't clear in night phase");
			}
			
			Player[] stateList = state.getPlayerList();
			Player[] endList = new Player[stateList.length];
			
			for(int i = 0; i < stateList.length; i++)
			{
				endList[i] = nightPhaseHelper(new GameState(state), stateList[i].getFaction());
			}
			
			state.setPlayerList(endList);
			
			state.setTime(Time.PICK_CARDS);
		}
	}
	
	/**
	 * Empties the den and discard in preparation for a new week
	 * @param state the state before the clearing
	 */
	private static void weekendClear(GameState state)
	{
		for(Player p : state.getPlayerList())
		{
			p.clearDen();
			p.clearDiscard();
		}
	}
	
	/**
	 * Clears the players' gold and sets a new amount
	 * @param state the state before the gold is redistributed
	 * @param gold the gold to distribute to each player
	 */
	private static void distributeInitialGold(GameState state, int gold)
	{
		for(Player p : state.getPlayerList())
		{
			p.addGold(-p.getGold());
			p.addGold(gold);
		}
	}

}
