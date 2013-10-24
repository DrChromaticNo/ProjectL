package test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import main.Board;
import main.Game;
import main.GameState;

import org.junit.Before;
import org.junit.Test;

import players.Faction;
import players.Player;
import ai.SimpleAI;

import cards.Deck;

import score.ScoreCounter;
import score.TreasureBag;
import standard.StandardScoreCounter;
import standard.StandardSettings;
import standard.StandardTreasureBag;

public class BestFactionTest {

	private int BIG_INT = 10000;
	private double BLwins;
	private double BKwins;
	private double Rwins;
	private double Ywins;
	private double Gwins;
	private double Wwins;
	Deck gameDeck;
	TreasureBag gameBag;
	ScoreCounter score;
	
	@Before
	public void initialize()
	{
		gameDeck = new TestDeck();
		
		gameBag = new StandardTreasureBag();
		
		score = new StandardScoreCounter();
		
		BLwins = 0;
		BKwins = 0;
		Rwins = 0;
		Ywins = 0;
		Gwins = 0;
		Wwins = 0;
		
	}
	
	@Test
	public void bestFaction()
	{
		for(int i = 0; i < BIG_INT; i++)
		{
			int numPlayers = 6; //assume/require > 1, < 7
			Player[] playerList = new Player[numPlayers];
			
			ArrayList<Color> factionList = Faction.allFactions();
		
			playerList[0] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[1] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[2] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[3] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[4] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[5] = new Player(chooseFaction(factionList), new SimpleAI());
			
			GameState state = new GameState(playerList, new Board(), gameDeck, gameBag, score);
			
			Set<Player> winners = Game.run(state, new StandardSettings());
			
			for(Player p : winners)
			{
				if(p.getFaction().equals(Faction.BLACK))
				{
					BKwins++;
				}
				else if(p.getFaction().equals(Faction.BLUE))
				{
					BLwins++;
				}
				else if(p.getFaction().equals(Faction.RED))
				{
					Rwins++;
				}
				else if(p.getFaction().equals(Faction.YELLOW))
				{
					Ywins++;
				}
				else if(p.getFaction().equals(Faction.GREEN))
				{
					Gwins++;
				}
				else if(p.getFaction().equals(Faction.WHITE))
				{
					Wwins++;
				}
			}
			
			System.err.println("Test " + i);
		}
		
		System.out.println("BKwins: " + BKwins);
		System.out.println("BLwins: " + BLwins);
		System.out.println("Rwins: " + Rwins);
		System.out.println("Ywins: " + Ywins);
		System.out.println("Gwins: " + Gwins);
		System.out.println("Wwins: " + Wwins);
	}
	
	private static Color chooseFaction(ArrayList<Color> factionList)
	{
		Random randomColor = new Random();
		int choice = randomColor.nextInt(factionList.size());
		return factionList.remove(choice);
	}

}
