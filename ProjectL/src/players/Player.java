package players;

import java.awt.Color;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import ai.AI;

import main.GameState;

import score.Loot;
import score.Treasure;

import cards.Card;
import cards.Deck;

/**
 * @author Chris
 * The Player object holds all the objects a player holds, their gold, score, 
 * treasures, hand, discard pile, etc.
 */

public class Player {

	//which faction this player is
	private Color faction;
	// true if the player is a CPU, false if not (CPU not yet implemented so will always be false)
	private boolean CPU;
	// the players current accessible gold
	private int gold;
	// the players current scored gold
	private int score;
	// the players current booty tokens
	private Loot loot;
	// self-explanatory: the hand, discard, and den
	private Set<Card> hand;
	private Set<Card> discard;
	private Set<Card> den;
	private AI ai;
	
	
	public Player(Color faction)
	{
		this.faction = faction;
		CPU = false;
		score = 0;
		gold = 0;
		loot = Treasure.getLoot();
		hand = new HashSet<Card>();
		discard = new HashSet<Card>();
		den = new HashSet<Card>();
	}
	
	public Player(Color faction, AI ai)
	{
		this(faction);
		CPU = true;
		this.ai = ai;
	}
	
	public Player(Player player)
	{
		faction = player.faction;
		CPU = player.CPU;
		score = player.score;
		gold = player.gold;
		loot = new Loot(player.loot);
		hand = new HashSet<Card>(player.getHand());
		discard = new HashSet<Card>(player.getDiscard());
		den = new HashSet<Card>(player.getDen());
	}
	
	public int getGold()
	{
		return gold;
	}
	
	public void addGold(int mod)
	{
		gold+=mod;
		if(gold < 0)
		{
			gold = 0;
		}
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void addScore(int mod)
	{
		score+=mod;
	}
	
	public Color getFaction()
	{
		return faction;
	}
	
	public void addToHand(Card card)
	{
		hand.add(card);
	}
	
	public void addToDiscard(Card card)
	{
		discard.add(card);
	}
	
	public void addToDen(Card card)
	{
		den.add(card);
	}
	
	public void removeFromHand(Card card)
	{
		hand.remove(card);
	}
	
	public void removeFromDiscard(Card card)
	{
		discard.remove(card);
	}
	
	public void removeFromDen(Card card)
	{
		den.remove(card);
	}
	
	public void clearDiscard()
	{
		discard.clear();
	}
	
	public void clearDen()
	{
		den.clear();
	}
	
	//NOTE: all three of these are deep copies
	
	public Set<Card> getHand()
	{
		HashSet<Card> newHand = new HashSet<Card>();
		for(Card c : hand)
		{
			newHand.add(new Card(c));
		}
		return newHand;
	}
	
	public Set<Card> getDiscard()
	{
		HashSet<Card> newDiscard = new HashSet<Card>();
		for(Card c : discard)
		{
			newDiscard.add(new Card(c));
		}
		return newDiscard;
	}
	
	public Set<Card> getDen()
	{
		HashSet<Card> newDen = new HashSet<Card>();
		for(Card c : den)
		{
			newDen.add(new Card(c));
		}
		return newDen;
	}
	
	public Loot getLoot()
	{
		return loot;
	}
	
	/**
	 * Chooses a card to play on the board and returns it
	 * @param state the current state of the game
	 * @param gameDeck the deck being used with the game
	 * @return the card this player wants to play
	 */
	public Card pickCard(GameState state, Deck gameDeck)
	{
		if(hand.isEmpty())
		{
			return null;
		}
		
		if(CPU)
		{
			Card[] cards = new Card[hand.size()];
			cards = hand.toArray(cards);
			
			Card chosenCard = ai.chooseCard(cards, new GameState(state), gameDeck);
			hand.remove(chosenCard);
			return chosenCard;
		}
		else
		{
			Scanner inputScanner = new Scanner(System.in);
			
			while(true)
			{	
				System.out.println("You have availible cards: ");
				
				for(Card c : hand)
				{
					System.out.print(gameDeck.abbreviatedName(c) + " ");
				}
				
				System.out.println();
				System.out.println("Please choose one of these cards");
				
				String choice = inputScanner.next();
				
				for(Card c : hand)
				{
					if(gameDeck.abbreviatedName(c).equals(choice))
					{
						hand.remove(c);
						return c;
					}
				}
			}
		}
	}
	
	public boolean checkCPU()
	{
		return CPU;
	}

}
