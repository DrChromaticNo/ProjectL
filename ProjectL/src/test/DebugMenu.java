package test;

import java.util.HashMap;
import java.util.Scanner;

import players.Faction;
import players.Player;

import score.Treasure;

import cards.Card;

import main.GameState;

public class DebugMenu {

	/**
	 * Launches the debug menu, and eventually returns a string
	 * @param state the state the debug menu is being run on
	 * @return a string corresponding to a player response
	 */
	public String launch(GameState state)
	{
		Scanner inputScanner = new Scanner(System.in);
		
		System.out.println("d: Debug Menu");
		
		String debug = inputScanner.next();
		
		if(!debug.equals("d"))
		{
			return debug;
		}	
		
		while(true)
		{
			System.out.println("\nDebug Menu: \n");
			System.out.println("b: View Board");
			System.out.println("p: View Players");
			System.out.println("s: Submit Your Response");
			
			String choice = inputScanner.next();
			
			if(choice.equals("b"))
			{
				boardMenu(state, inputScanner);
			}
			else if(choice.equals("p"))
			{
				playerMenu(state, inputScanner);
			}
			else if(choice.equals("s"))
			{
				return inputScanner.next();
			}
		}
	}
	
	/**
	 * The menu to view the board
	 * @param state the state to view in
	 * @param inputScanner the inputscanner to get navigation info thru
	 */
	private void boardMenu(GameState state, Scanner inputScanner)
	{
		String choice = "";
		while(!choice.equals("q"))
		{
			System.out.println("\nBoard Menu: \n");
			System.out.println("d: View Deck");
			System.out.println("l: View Loot");
			System.out.println("q: Quit to Previous Menu\n");
			
			choice = inputScanner.next();
			
			//displays the cards on the deck
			if(choice.equals("d"))
			{
				Card[] deck = state.getBoard().getDeck();
				for(Card c : deck)
				{
					System.out.print(c.abbreviate() + " ");
				}
			}
			//displays the loot at a given day
			else if(choice.equals("l"))
			{
				choice = "";
				int day = -1;
				while(day < 0 || day > 6)
				{
					System.out.println("Select a Day: ");
					day = inputScanner.nextInt();
				}
				for(String s : Treasure.allTreasures())
				{
					if(state.getBoard().getLoot(day).countTreasure(s) != 0)
					{
						System.out.println(s + " x" + state.getBoard().getLoot(day).countTreasure(s));
					}
				}
			}
		}
	}
	
	private void playerMenu(GameState state, Scanner inputScanner)
	{
		HashMap<Integer, Player> pMap = new HashMap<Integer, Player>();
		
		int index = 0;
		for(Player p : state.getPlayerList())
		{
			pMap.put(index, p);
			index++;
		}
		
		String choice = "";
		
		while(!choice.equals("q"))
		{
			for(int dex : pMap.keySet())
			{
				System.out.println(dex + ": " + 
						Faction.getPirateName(pMap.get(dex).getFaction()));
			}
			
			System.out.println("\nPlayer Menu: \n");
			System.out.println("h: View Hand");
			System.out.println("di: View Discard");
			System.out.println("de: View Den");
			System.out.println("l: View Loot");
			System.out.println("g: View Gold");
			System.out.println("s: View Score");
			System.out.println("q: Quit to Previous Menu\n");
			
			choice = inputScanner.next();
			
			if(choice.equals("h"))
			{
				System.out.println("Choose a Player: ");
				int player = inputScanner.nextInt();
				for(Card c : pMap.get(player).getHand())
				{
					System.out.print(c.abbreviate());
				}
					
			}
			else if(choice.equals("di"))
			{
				System.out.println("Choose a Player: ");
				int player = inputScanner.nextInt();
				for(Card c : pMap.get(player).getDiscard())
				{
					System.out.print(c.abbreviate());
				}
			}
			else if(choice.equals("de"))
			{
				System.out.println("Choose a Player: ");
				int player = inputScanner.nextInt();
				for(Card c : pMap.get(player).getDen())
				{
					System.out.print(c.abbreviate());
				}
			}
			else if(choice.equals("g"))
			{
				System.out.println("Choose a Player: ");
				int player = inputScanner.nextInt();
				System.out.println(pMap.get(player).getGold());
			}
			else if(choice.equals("s"))
			{
				System.out.println("Choose a Player: ");
				int player = inputScanner.nextInt();
				System.out.println(pMap.get(player).getScore());
			}
			else if(choice.equals("l"))
			{
				System.out.println("Choose a Player: ");
				int player = inputScanner.nextInt();
				for(String s : Treasure.allTreasures())
				{
					if(pMap.get(player).getLoot().countTreasure(s) != 0)
					{
						System.out.println(s + " x" + pMap.get(player).getLoot().countTreasure(s));
					}
				}
			}
		}
	}
	
}
