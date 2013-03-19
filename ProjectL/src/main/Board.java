package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import score.Loot;
import score.Treasure;


import cards.Card;

/**
 * @author Chris
 * Keeps track of the cards on deck and the treasure
 */

public class Board {

	//the deck should always be in sorted order
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
	
	public Board(Board board)
	{
		deck = new ArrayList<Card>(board.deck);
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
	}
	
	public void removeCard(Card card)
	{
		deck.remove(card);
		Collections.sort(deck);
	}
	
	public Card[] getDeck()
	{
		return (Card[]) deck.toArray();
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
	
}
