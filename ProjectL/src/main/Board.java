package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import networking.BoardInfo;
import networking.CardInfo;

import score.Loot;
import score.Treasure;


import cards.Card;

/**
 * @author Chris
 * Keeps track of the cards on deck and the treasure
 */

public class Board {

	//the deck should always be in sorted order (not to be confused with a deck of cards,
	//this is the deck of the ship
	private ArrayList<Card> deck;
	//the ship loot is in order of the days it is accessed in
	private Loot[] shipLoot;
	
	public Board()
	{
		deck = new ArrayList<Card>();
		shipLoot = new Loot[6];
		for(int i = 0; i < shipLoot.length; i++)
		{
			shipLoot[i] = Treasure.getLoot();
		}
	}
	
	/**
	 * Deep copy constructor
	 * @param board the board to copy
	 */
	public Board(Board board)
	{
		deck = new ArrayList<Card>();
		for(Card c : board.deck)
		{
			deck.add(new Card(c));
		}
		Collections.sort(deck);
		shipLoot = new Loot[6];
		for(int i = 0; i < shipLoot.length; i++)
		{
			shipLoot[i] = new Loot(board.shipLoot[i]);
		}
	}
	
	public void clearDeck()
	{
		deck.clear();
	}
	
	public void addCard(Card card)
	{
		deck.add(card);
		Collections.sort(deck);
		if(deck.size() > 6)
		{
			throw new RuntimeException("the deck of the board has too many cards!");
		}
	}
	
	public void removeCard(Card card)
	{
		deck.remove(card);
		Collections.sort(deck);
	}
	
	public Card[] getDeck()
	{
		Card[] newDeck = new Card[deck.size()];
		for(int i = 0; i < deck.size(); i++)
		{
			newDeck[i] = deck.get(i);
		}
		return newDeck;
	}
	
	public Loot getLoot(int day)
	{
		if(day >= 0 && day <= 5)
		{
			return shipLoot[day];
		}
		throw new RuntimeException("day out of bounds");
	}
	
	public void setLoot(int day, Loot loot)
	{
		if(day >= 0 && day <= 5)
		{
			shipLoot[day] = loot;
		}
	}
	
	public Loot[] getAllLoot()
	{
		return shipLoot;
	}
	
	@Override public boolean equals(Object other)
	{
		if(other instanceof Board)
		{
			Board board = (Board) other;
			if(deck.equals(board.deck))
			{
				if(shipLoot.length == board.shipLoot.length)
				{
					for(int i = 0; i < shipLoot.length; i++)
					{
						if(!shipLoot[i].equals(board.shipLoot[i]))
						{
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	@Override public int hashCode()
	{
		return deck.hashCode()*shipLoot.length;
	}
	
	/**
	 * Returns the boardinfo for this board
	 * @return
	 */
	public BoardInfo getBI()
	{
		CardInfo[] cards = new CardInfo[deck.size()];
		for(int i = 0; i < cards.length; i++)
		{
			cards[i] = deck.get(i).getCI();
		}
		
		return new BoardInfo(cards, shipLoot);
	}
	
}
