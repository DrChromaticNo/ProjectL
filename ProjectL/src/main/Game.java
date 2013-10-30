/**
 * @author: Chris 
 * The main game runnable, running this should run the game!
 */

package main;


import gui.frontmenu.FrontMenu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import players.Faction;
import players.Player;

import score.Treasure;
import cards.Card;

public class Game {
	private static GameSettings settings;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		FrontMenu menu = new FrontMenu();
		menu.launch();

	}
	
	/**
	 * The main game loop, iterates through x weeks of play
	 * @param state the initial state
	 */
	public static Set<Player> run(GameState state, GameSettings gSettings)
	{
		//Setup global game settings
		settings = gSettings;
		
		//Launch the GUIs
		state.launchGUIs();
		
		//Hello!
		state.messageAllGUIs("The game is starting!\nLook for a log of game events here");
		
		//get the list of all the drawable cards
		ArrayList<Integer> availibleCards = state.getDeck().allCards();
		
		for(int week = state.getWeek(); 
				week < settings.weekAmt(); week++)
		{
			state.setWeek(week);
			
			if(!state.checkDraw())
			{
				state.getBag().resetBag();
				placeTreasures(state);
				distributeInitialGold(state, 
						settings.initialGold());
				//this section either draws the initial hand (at the start of the game)
				//or adds six cards to the existing hands
				drawCards(state, availibleCards,
						settings.cardAmt(week));
			}
			
			state.updateGUIs();
			
			state = weekLoop(state);
		}
		
		int max = Integer.MIN_VALUE;
		HashSet<Player> winners = new HashSet<Player>();
		
		//determine the winners of the game!
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
			state.messageAllGUIs((Faction.getPirateName(p.getFaction()) + 
					" won with a score of " + p.getScore() + "!"));
		}
		
		return winners;
	}
	
	/**
	 * Places the treasures for the start of a week
	 * @param state the state of the game at the start of the week
	 */
	private static void placeTreasures(GameState state)
	{
		resetBoardLoot(state);
		for(int i = 0; i < 6; i++)
		{
			for(@SuppressWarnings("unused") Player p : state.getPlayerList())
			{
				state.getBoard().getLoot(i)
					.addLoot(state.getBag().randomTreasure(), 1);
			}
		}
	}
	
	/**
	 * Draws cards from the deck and deals them to each player
	 * @param state the state to distribute the cards in
	 * @param numCards the # of cards to give to each player
	 */
	private static void drawCards(GameState state, ArrayList<Integer> availibleCards, 
			int numCards)
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
				p.addToHand(new Card(p.getFaction(), card, state.getDeck()));
			}
		}
		
		state.setDraw(true);
	}
	
	/**
	 * Gathers cards from the players and puts them on the board
	 * @param state the current state of the game
	 */
	private static void pickCards(GameState state)
	{
		HashSet<Card> chosenCards = new HashSet<Card>();
		
		for(Player p : state.getPlayerList())
		{
			Card choice = p.pickCard(state);
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
							state = den.get(index).nightAction(new GameState(state));
							
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
	 */
	private static GameState weekLoop(GameState state)
	{
		for(int day = state.getDay(); day <= 5; day++)
		{
			state.setDay(day);
			
			if(state.getTime() == Time.PICK_CARDS)
			{
				pickCards(state);
				state.messageAllGUIs("~DAY PHASE (" + (day+1) + ")~");
				state.setTime(Time.DAY);
				state.updateGUIs();
			}
			
			while(state.getTime() != Time.PICK_CARDS)
			{
				state = oneAction(state);
				state.updateGUIs();
			}
		}
		
		state = state.getCounter().score(new GameState(state));
		
		weekendClear(state);
		
		return state;
	}
	
	/**
	 * Performs one action of the game
	 * @param state the gamestate before the action
	 */
	private static GameState oneAction(GameState state)
	{
		
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
				state.messageAllGUIs("~DUSK PHASE~");
				state.setTime(Time.DUSK);
			}
			else
			{
				Card actionCard = deck[index];
				state = deck[index].dayAction(new GameState(state));
				
				//Check to make sure the card is still in play
				if(state.getBoard().getDeck().length > index 
						&& state.getBoard().getDeck()[index].equals(actionCard))
				{
					state.getBoard().getDeck()[index].setTrue(Time.DAY);
				}
			}
		}
		else if(state.getTime() == Time.DUSK)
		{
			Card[] deck = state.getBoard().getDeck();
			
			int index = deck.length-1;
				
			while(index >= 0 && deck[index].checkPhase(Time.DUSK))
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
				
				state.messageAllGUIs("~NIGHT PHASE~");
				state.setTime(Time.NIGHT);
			}
			else
			{
				Card actionCard = deck[index];
				state = deck[index].duskAction(new GameState(state));
				
				//Check to make sure the card is still in play
				if(state.getBoard().getDeck().length > index 
						&& state.getBoard().getDeck()[index].equals(actionCard))
				{
					state.getBoard().getDeck()[index].setTrue(Time.DUSK);
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
				endList[i] = nightPhaseHelper(new GameState(state), stateList[i].getFaction());
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
		state.setDay(0);
		state.setDraw(false);
		for(Player p : state.getPlayerList())
		{
			p.clearDen();
			p.clearDiscard();
			p.getLoot().emptyBag();
		}
		state.updateGUIs();
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
	
	/**
	 * Returns all the loot to the default emptiness
	 * @param state
	 */
	private static void resetBoardLoot(GameState state)
	{
		for(int day = 0; day <=5; day++)
		{
			state.getBoard().setLoot(day, Treasure.getLoot());
		}
	}
}
