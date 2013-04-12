package test;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import main.Board;
import main.Game;
import main.GameState;

import org.junit.Test;

import ai.DepthAI;
import ai.FullAI;
import ai.SimpleAI;

import players.Faction;
import players.Player;

import score.ScoreCounter;
import score.TreasureBag;
import standard.StandardScoreCounter;
import standard.StandardTreasureBag;
import cards.Deck;

public class AITest {

	private int BIG_INT = 100;
	
	@Test
	public void depth0opponent1() {
		
		Deck gameDeck = new TestDeck();
		
		TreasureBag gameBag = new StandardTreasureBag();
		
		ScoreCounter score = new StandardScoreCounter();
		
		double wins = 0;
		double loss = 0;
		double tie = 0;
		
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
	public void depth0opponent3() {
		
		Deck gameDeck = new TestDeck();
		
		TreasureBag gameBag = new StandardTreasureBag();
		
		ScoreCounter score = new StandardScoreCounter();
		
		double wins = 0;
		double loss = 0;
		double tie = 0;
		
		for(int i = 0; i < BIG_INT; i++)
		{
			int numPlayers = 4; //assume/require > 1, < 7
			Player[] playerList = new Player[numPlayers];
			
			ArrayList<Color> factionList = Faction.allFactions();
		
			playerList[0] = new Player(chooseFaction(factionList), new SimpleAI());
			
			playerList[1] = new Player(chooseFaction(factionList), new DepthAI(0));
			
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
		
		assertEquals(true, wins/(wins+loss) > .9);
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
