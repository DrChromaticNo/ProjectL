package main;

import java.util.ArrayList;
import java.util.HashMap;

import resources.Loot;

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
	}
	
}
