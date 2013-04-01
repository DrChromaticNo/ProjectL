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

import ai.FullAI;
import ai.SimpleAI;

import players.Faction;
import players.Player;

import score.Loot;
import score.ScoreCounter;
import score.Treasure;
import score.TreasureBag;
import standard.StandardScoreCounter;
import standard.StandardTreasureBag;
import test.TestDeck;
import cards.Card;
import cards.Deck;

public class Game {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//create deck and treasurebag and score class to use with this game
		Deck gameDeck = new TestDeck();
		
		TreasureBag gameBag = new StandardTreasureBag();
		
		ScoreCounter score = new StandardScoreCounter();
		
		int numPlayers = 3; //assume/require > 1, < 7
		Player[] playerList = new Player[numPlayers];
		
		ArrayList<Color> factionList = Faction.allFactions();
		
		if(factionList.size() < numPlayers)
		{
			throw new RuntimeException("there's too many players and not enough factions");
		}
		
		//randomly assign each player a faction
	/*	for(int i = 0; i < numPlayers; i++)
		{
			playerList[i] = new Player(chooseFaction(factionList));
		}
	*/
		
		playerList[0] = new Player(chooseFaction(factionList));
		
		System.out.println(playerList[0].getFaction());
		
		playerList[1] = new Player(chooseFaction(factionList), new FullAI());
		
		System.out.println(playerList[1].getFaction());
		
		playerList[2] = new Player(chooseFaction(factionList), new SimpleAI());
		
		System.out.println(playerList[2].getFaction());
		
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
			state.setWeek(week);
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
			
			state = weekLoop(state, gameDeck, gameBag, counter);
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
		resetBoardLoot(state);
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
		if(availibleCards.size() < numCards)
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
	private static void pickCards(GameState state, Deck gameDeck, TreasureBag bag, ScoreCounter counter)
	{
		HashSet<Card> chosenCards = new HashSet<Card>();
		
		for(Player p : state.getPlayerList())
		{
			Card choice = p.pickCard(state, gameDeck, bag, counter);
			if(choice != null)
			{
				chosenCards.add(choice);
			}
		}
		
		//Now that we've collected all the cards, remove them from the players' hands and
		//put them on the board
		for(Card c : chosenCards)
		{
			Player player = state.getPlayer(c.getFaction());
			player.removeFromHand(c);
			
			state.getBoard().addCard(c);
		}
	}
	
	/**
	 * Helper method to do the night state for one particular player at a time
	 * @param state the game state at the start of the night phase
	 * @param faction the faction of the player we're working on
	 * @param counter the scorecounter being used in this game
	 * @return the player after all his night phase actions have been completed
	 */
	private static Player nightPhaseHelper(GameState state, Color faction, 
			TreasureBag bag, ScoreCounter counter)
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
						
						int index = den.size()-1;
						
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
							state = den.get(index).nightAction(new GameState(state), bag, counter);
							
							//Gotta make sure to check if the card is still in play or not
							if(state.getPlayer(faction).getDen().contains(den.get(index)))
							{
								state.getPlayer(faction).removeFromDen(den.get(index));
								
								Card replacement = den.get(index);
								replacement.setTrue(Time.NIGHT);
								state.getPlayer(faction).addToDen(den.get(index));
							}
						}
					}
				}
				
				for(Card c : state.getPlayer(faction).getDen())
				{
					state.getPlayer(faction).removeFromDen(c);
					c.resetPhases();
					state.getPlayer(faction).addToDen(c);
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
	 * @param counter the scorecounter being used in this game
	 * @param gameBag the treasure bag to use for this week
	 */
	private static GameState weekLoop(GameState state, Deck gameDeck, 
			TreasureBag gameBag, ScoreCounter counter)
	{
		for(int day = 0; day <= 5; day++)
		{
			state.setDay(day);
			
			if(state.getTime() == Time.PICK_CARDS)
			{
				pickCards(state, gameDeck, gameBag, counter);
				System.out.println(" ");
				System.out.println("~DAY PHASE~");
				System.out.println(" ");
				state.setTime(Time.DAY);
			}
			
			while(state.getTime() != Time.PICK_CARDS)
			{
				state = oneAction(state, gameDeck, gameBag, counter);
			}
		}
		
		state = counter.score(new GameState(state));
		
		weekendClear(state);
		
		return state;
	}
	
	/**
	 * Performs one action of the game
	 * @param state the gamestate before the action
	 * @param gameDeck the deck being used for this action
	 * @param counter the scorecounter being used in this game
	 * @param gameBag the bag being used for this action
	 */
	private static GameState oneAction(GameState state, Deck gameDeck, 
			TreasureBag gameBag, ScoreCounter counter)
	{
		System.out.print("Board: ");
		
		//NOTE: this traversal might be backwards? need to check which way the cards get sorted
		for(Card c : state.getBoard().getDeck())
		{
			System.out.print(gameDeck.abbreviatedName(c) + " ");
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
				System.out.println(" ");
				System.out.println("~DUSK PHASE~");
				System.out.println(" ");
				state.setTime(Time.EVENING);
			}
			else
			{
				Card actionCard = deck[index];
				state = deck[index].dayAction(new GameState(state), gameBag, counter);
				
				//Check to make sure the card is still in play
				if(state.getBoard().getDeck().length > index 
						&& state.getBoard().getDeck()[index].equals(actionCard))
				{
					state.getBoard().getDeck()[index].setTrue(Time.DAY);
				}
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
				//clears the cards from the deck in preparation for the night phase
				for(Card c : state.getBoard().getDeck())
				{
					state.getPlayer(c.getFaction()).addToDen(c);
				}
				state.getBoard().clearDeck();
				
				System.out.println(" ");
				System.out.println("~NIGHT PHASE~");
				System.out.println(" ");
				state.setTime(Time.NIGHT);
			}
			else
			{
				Card actionCard = deck[index];
				state = deck[index].eveningAction(new GameState(state), gameBag, counter);
				
				//Check to make sure the card is still in play
				if(state.getBoard().getDeck().length > index 
						&& state.getBoard().getDeck()[index].equals(actionCard))
				{
					state.getBoard().getDeck()[index].setTrue(Time.EVENING);
				}
			}
		}
		else //the only other options is it being Time.NIGHT
		{
			//Since all these actions occur simultaineously
			//we get each player's response and then
			//update each of the player's states at once.
			
			//It is important to note a night phase action
			//CANNOT modify the state of other players or anything about the game
			//besides ADDING TO the treasure bag
			if(state.getBoard().getDeck().length != 0)
			{
				throw new RuntimeException("Board isn't clear in night phase");
			}
			
			Player[] stateList = state.getPlayerList();
			Player[] endList = new Player[stateList.length];
			
			for(int i = 0; i < stateList.length; i++)
			{
				endList[i] = nightPhaseHelper(new GameState(state), stateList[i].getFaction(), 
						gameBag, counter);
			}
			
			state.setPlayerList(endList);
			
			state.setTime(Time.PICK_CARDS);
		}
		
		return state;
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
			p.getLoot().emptyBag();
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
	
	private static void resetBoardLoot(GameState state)
	{
		for(int day = 0; day <=5; day++)
		{
			state.getBoard().setLoot(day, Treasure.getLoot());
		}
	}

}
