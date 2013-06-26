package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import main.Board;
import main.Game;
import main.GameState;

import org.junit.Before;
import org.junit.Test;

import ai.DepthAI;
import ai.DepthEstAI;
import ai.EstAI;
import ai.FullAI;
import ai.SimpleAI;

import players.Faction;
import players.Player;

import score.ScoreCounter;
import score.TreasureBag;
import standard.StandardScoreCounter;
import standard.StandardTreasureBag;
import cards.Deck;

public class AISanityTest {

	private int BIG_INT = 100;
	private double wins;
	private double loss;
	private double tie;
	Deck gameDeck;
	TreasureBag gameBag;
	ScoreCounter score;
	
	@Before
	public void initialize()
	{
		gameDeck = new TestDeck();
		
		gameBag = new StandardTreasureBag();
		
		score = new StandardScoreCounter();
		
		wins = 0;
		loss = 0;
		tie = 0;
	}
	
	@Test
	public void depth0opponent1() {
		
		for(int i = 0; i < BIG_INT; i++)
		{
			int numPlayers = 2; //assume/require > 1, < 7
			Player[] playerList = new Player[numPlayers];
			
			ArrayList<Color> factionList = Faction.allFactions();
		
			playerList[0] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[1] = new Player(chooseFaction(factionList), new DepthAI(0));
			
			Color check = playerList[1].getFaction();
			
			GameState state = new GameState(playerList, new Board(), gameDeck, gameBag, score);
			
			Set<Player> winners = Game.run(state);
			
			boolean won = false;
			for(Player p : winners)
			{
				if(p.getFaction().equals(check))
				{
					won = true;
					wins++;
					if(winners.size() > 1)
					{
						tie++;
					}
				}
			}
			if(!won)
			{
				loss++;
			}
			
		}
		
		assertEquals(true, wins/(wins+loss) > .9);
	}
	
	@Test
	public void depthest01opponent3()
	{
		for(int i = 0; i < BIG_INT; i++)
		{
			int numPlayers = 4; //assume/require > 1, < 7
			Player[] playerList = new Player[numPlayers];
			
			ArrayList<Color> factionList = Faction.allFactions();
		
			playerList[0] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[1] = new Player(chooseFaction(factionList), new DepthEstAI(0,1));
			
			playerList[2] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[3] = new Player(chooseFaction(factionList), new SimpleAI());
			
			Color check = playerList[1].getFaction();
			
			GameState state = new GameState(playerList, new Board(), gameDeck, gameBag, score);
			
			Set<Player> winners = Game.run(state);
			
			boolean won = false;
			for(Player p : winners)
			{
				if(p.getFaction().equals(check))
				{
					won = true;
					wins++;
					if(winners.size() > 1)
					{
						tie++;
					}
				}
			}
			if(!won)
			{
				loss++;
			}
			
		}
		
		assertEquals(true, wins/(wins+loss) > .8);
	}
	
	@Test
	public void depthest01opponent5()
	{
		for(int i = 0; i < BIG_INT; i++)
		{
			int numPlayers = 6; //assume/require > 1, < 7
			Player[] playerList = new Player[numPlayers];
			
			ArrayList<Color> factionList = Faction.allFactions();
		
			playerList[0] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[1] = new Player(chooseFaction(factionList), new DepthEstAI(0,1));
			
			playerList[2] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[3] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[4] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[5] = new Player(chooseFaction(factionList), new SimpleAI());
			
			Color check = playerList[1].getFaction();
			
			GameState state = new GameState(playerList, new Board(), gameDeck, gameBag, score);
			
			Set<Player> winners = Game.run(state);
			
			boolean won = false;
			for(Player p : winners)
			{
				if(p.getFaction().equals(check))
				{
					won = true;
					wins++;
					if(winners.size() > 1)
					{
						tie++;
					}
				}
			}
			if(!won)
			{
				loss++;
			}
			
		}
		
		assertEquals(true, wins/(wins+loss) > .8);
	}
	
	@Test
	public void depthest11opponent3()
	{
		for(int i = 0; i < BIG_INT; i++)
		{
			int numPlayers = 4; //assume/require > 1, < 7
			Player[] playerList = new Player[numPlayers];
			
			ArrayList<Color> factionList = Faction.allFactions();
		
			playerList[0] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[1] = new Player(chooseFaction(factionList), new DepthEstAI(1,1));
			
			playerList[2] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[3] = new Player(chooseFaction(factionList), new SimpleAI());
			
			Color check = playerList[1].getFaction();
			
			GameState state = new GameState(playerList, new Board(), gameDeck, gameBag, score);
			
			Set<Player> winners = Game.run(state);
			
			boolean won = false;
			for(Player p : winners)
			{
				if(p.getFaction().equals(check))
				{
					won = true;
					wins++;
					if(winners.size() > 1)
					{
						tie++;
					}
				}
			}
			if(!won)
			{
				loss++;
			}
			
		}
		
		assertEquals(true, wins/(wins+loss) > .8);
	}
	
	@Test
	public void depthest02opponent3()
	{
		for(int i = 0; i < BIG_INT; i++)
		{
			int numPlayers = 4; //assume/require > 1, < 7
			Player[] playerList = new Player[numPlayers];
			
			ArrayList<Color> factionList = Faction.allFactions();
		
			playerList[0] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[1] = new Player(chooseFaction(factionList), new DepthEstAI(0,2));
			
			playerList[2] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[3] = new Player(chooseFaction(factionList), new SimpleAI());
			
			Color check = playerList[1].getFaction();
			
			GameState state = new GameState(playerList, new Board(), gameDeck, gameBag, score);
			
			Set<Player> winners = Game.run(state);
			
			boolean won = false;
			for(Player p : winners)
			{
				if(p.getFaction().equals(check))
				{
					won = true;
					wins++;
					if(winners.size() > 1)
					{
						tie++;
					}
				}
			}
			if(!won)
			{
				loss++;
			}
			
		}
		
		assertEquals(true, wins/(wins+loss) > .8);
	}
	
	@Test
	public void depthest11opponent5()
	{
		for(int i = 0; i < BIG_INT; i++)
		{
			int numPlayers = 6; //assume/require > 1, < 7
			Player[] playerList = new Player[numPlayers];
			
			ArrayList<Color> factionList = Faction.allFactions();
		
			playerList[0] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[1] = new Player(chooseFaction(factionList), new DepthEstAI(1,1));
			
			playerList[2] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[3] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[4] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[5] = new Player(chooseFaction(factionList), new SimpleAI());
			
			Color check = playerList[1].getFaction();
			
			GameState state = new GameState(playerList, new Board(), gameDeck, gameBag, score);
			
			Set<Player> winners = Game.run(state);
			
			boolean won = false;
			for(Player p : winners)
			{
				if(p.getFaction().equals(check))
				{
					won = true;
					wins++;
					if(winners.size() > 1)
					{
						tie++;
					}
				}
			}
			if(!won)
			{
				loss++;
			}
			
		}
		
		assertEquals(true, wins/(wins+loss) > .8);
	}
	
	@Test
	public void depthest02opponent5()
	{
		for(int i = 0; i < BIG_INT; i++)
		{
			int numPlayers = 6; //assume/require > 1, < 7
			Player[] playerList = new Player[numPlayers];
			
			ArrayList<Color> factionList = Faction.allFactions();
		
			playerList[0] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[1] = new Player(chooseFaction(factionList), new DepthEstAI(0,2));
			
			playerList[2] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[3] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[4] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[5] = new Player(chooseFaction(factionList), new SimpleAI());
			
			Color check = playerList[1].getFaction();
			
			GameState state = new GameState(playerList, new Board(), gameDeck, gameBag, score);
			
			Set<Player> winners = Game.run(state);
			
			boolean won = false;
			for(Player p : winners)
			{
				if(p.getFaction().equals(check))
				{
					won = true;
					wins++;
					if(winners.size() > 1)
					{
						tie++;
					}
				}
			}
			if(!won)
			{
				loss++;
			}
			
		}
		
		assertEquals(true, wins/(wins+loss) > .8);
	}
	
	@Test
	public void depthest21opponent5()
	{
		for(int i = 0; i < BIG_INT; i++)
		{
			int numPlayers = 6; //assume/require > 1, < 7
			Player[] playerList = new Player[numPlayers];
			
			ArrayList<Color> factionList = Faction.allFactions();
		
			playerList[0] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[1] = new Player(chooseFaction(factionList), new DepthEstAI(2,1));
			
			playerList[2] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[3] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[4] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[5] = new Player(chooseFaction(factionList), new SimpleAI());
			
			Color check = playerList[1].getFaction();
			
			GameState state = new GameState(playerList, new Board(), gameDeck, gameBag, score);
			
			Set<Player> winners = Game.run(state);
			
			boolean won = false;
			for(Player p : winners)
			{
				if(p.getFaction().equals(check))
				{
					won = true;
					wins++;
					if(winners.size() > 1)
					{
						tie++;
					}
				}
			}
			if(!won)
			{
				loss++;
			}
			
		}
		
		assertEquals(true, wins/(wins+loss) > .8);
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

}
