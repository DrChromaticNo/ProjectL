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

import players.Faction;
import players.Player;
import ai.DepthEstAI;

import cards.Deck;

import score.ScoreCounter;
import score.TreasureBag;
import standard.StandardEstimator;
import standard.StandardScoreCounter;
import standard.StandardSettings;
import standard.StandardTreasureBag;

public class AISmallCompete {

	private int BIG_INT = 1000;
	private double p1Wins;
	private double p2Wins;
	Deck gameDeck;
	TreasureBag gameBag;
	ScoreCounter score;
	
	@Before
	public void initialize()
	{
		gameDeck = new TestDeck();
		
		gameBag = new StandardTreasureBag();
		
		score = new StandardScoreCounter();
		
		p1Wins = 0;
		p2Wins = 0;
	}
	
	@Test
	public void depthest02vsdepthest21()
	{
		for(int i = 0; i < BIG_INT; i++)
		{
			int numPlayers = 2; //assume/require > 1, < 7
			Player[] playerList = new Player[numPlayers];
			
			ArrayList<Color> factionList = Faction.allFactions();
			
			playerList[0] = new Player(chooseFaction(factionList), new DepthEstAI(0,2, new StandardEstimator()));
			
			playerList[1] = new Player(chooseFaction(factionList), new DepthEstAI(2,1, new StandardEstimator()));
			
			//P1 is 0,2
			Color check1 = playerList[0].getFaction();
			//P2 is 2,1
			Color check2 = playerList[1].getFaction();
			
			GameState state = new GameState(playerList, new Board(), gameDeck, gameBag, score);
			
			Set<Player> winners = Game.run(state, new StandardSettings());
			
			for(Player p : winners)
			{
				if(p.getFaction().equals(check1))
				{
					p1Wins++;
				}
				else if(p.getFaction().equals(check2))
				{
					p2Wins++;
				}
			}
			
		}
		
		System.out.println(p1Wins);
		System.out.println(p2Wins);
		assertTrue(p2Wins > p1Wins);
	}
	
	private static Color chooseFaction(ArrayList<Color> factionList)
	{
		Random randomColor = new Random();
		int choice = randomColor.nextInt(factionList.size());
		return factionList.remove(choice);
	}

}
