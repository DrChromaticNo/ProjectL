package test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import main.Board;
import main.Game;
import main.GameState;

import org.junit.Before;
import org.junit.Test;

import ai.DepthEstAI;

import players.Faction;
import players.Player;

import score.ScoreCounter;
import score.TreasureBag;
import standard.StandardEstimator;
import standard.StandardScoreCounter;
import standard.StandardSettings;
import standard.StandardTreasureBag;
import cards.Deck;

public class BestFactionTournament {

	private int BIG_INT = 100;
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
			ArrayList<Color> factionList = Faction.allFactions();
			Player[] gameOne = new Player[]{new Player(chooseFaction(factionList), 
					new DepthEstAI(2,1,new StandardEstimator())),
					new Player(chooseFaction(factionList), 
					new DepthEstAI(2,1,new StandardEstimator()))};
			
			Player[] gameTwo = new Player[]{new Player(chooseFaction(factionList), 
					new DepthEstAI(2,1,new StandardEstimator())),
					new Player(chooseFaction(factionList), 
					new DepthEstAI(2,1,new StandardEstimator()))};
			
			Player[] gameThree = new Player[]{new Player(chooseFaction(factionList), 
					new DepthEstAI(2,1,new StandardEstimator())),
					new Player(chooseFaction(factionList), 
					new DepthEstAI(2,1,new StandardEstimator()))};
			
			Set<Player> gameOneWinners = null;
			Set<Player> gameTwoWinners = null;
			Set<Player> gameThreeWinners = null;
			
			while(gameOneWinners == null || gameOneWinners.size() != 1)
			{
				gameOne = new Player[]{new Player(gameOne[0].getFaction(), 
						new DepthEstAI(2,1,new StandardEstimator())),
						new Player(gameOne[1].getFaction(), 
						new DepthEstAI(2,1,new StandardEstimator()))};
				gameOneWinners = Game.run(new GameState(gameOne, new Board(), gameDeck,gameBag,score), 
						new StandardSettings());
			}
			
			while(gameTwoWinners == null || gameTwoWinners.size() != 1)
			{
				gameTwo = new Player[]{new Player(gameTwo[0].getFaction(), 
						new DepthEstAI(2,1,new StandardEstimator())),
						new Player(gameTwo[1].getFaction(), 
						new DepthEstAI(2,1,new StandardEstimator()))};
				
				gameTwoWinners = Game.run(new GameState(gameTwo, new Board(), gameDeck,gameBag,score), 
						new StandardSettings());
			}
			
			while(gameThreeWinners == null || gameThreeWinners.size() != 1)
			{
				gameThree = new Player[]{new Player(gameThree[0].getFaction(), 
						new DepthEstAI(2,1,new StandardEstimator())),
						new Player(gameThree[1].getFaction(), 
						new DepthEstAI(2,1,new StandardEstimator()))};
				
				gameThreeWinners = Game.run(new GameState(gameThree, new Board(), gameDeck,gameBag,score), 
						new StandardSettings());
			}
			
			Set<Player> finals = null;
			
			Set<Player> finalPlayers = new HashSet<Player>();
			finalPlayers.addAll(gameOneWinners);
			finalPlayers.addAll(gameTwoWinners);
			finalPlayers.addAll(gameThreeWinners);
			
			while(finals == null || finals.size() != 1)
			{
				HashSet<Player> newFinals = new HashSet<Player>();
				for(Player p : finalPlayers)
				{
					newFinals.add(new Player(p.getFaction(), 
						new DepthEstAI(2,1,new StandardEstimator())));
				}
				
				finalPlayers = newFinals;
				
				finals = Game.run(new GameState(finalPlayers.toArray(new Player[3]), 
						new Board(), gameDeck,gameBag,score), 
						new StandardSettings());
			}
			
			for(Player p : finals)
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
