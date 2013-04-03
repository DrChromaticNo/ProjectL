package main;

import java.awt.Color;

import cards.Deck;

import players.Player;
import score.ScoreCounter;
import score.TreasureBag;

/**
 * @author Chris
 * Keeps track of the state of the game
 */

public class GameState {

	//note, the players are ordered in clockwise orientation (the turn order and seating)
	private Player[] pList;
	private Board board;
	private int day;
	private int time;
	private int week;
	private Deck deck;
	private TreasureBag bag;
	private ScoreCounter counter;
	
	public GameState(Player[] playerlist, Board board, Deck deck, 
			TreasureBag bag, ScoreCounter counter)
	{
		pList = playerlist;
		this.board = board;
		day = 0;
		week = 0;
		time = Time.PICK_CARDS;
		this.deck = deck;
		this.bag = bag;
		this.counter = counter;
	}
	
	public GameState(GameState state)
	{
		pList = new Player[state.pList.length];
		for(int i = 0; i < state.pList.length; i++)
		{
			pList[i] = new Player(state.pList[i]);
		}
		board = new Board(state.board);
		day = state.day;
		time = state.time;
		week = state.week;
		deck = state.deck;
		bag = state.bag.copy();
		counter = state.counter;
	}
	
	public Player[] getPlayerList()
	{
		return pList;
	}
	
	public void setPlayerList(Player[] list)
	{
		pList = list;
	}
	
	public Player getPlayer(Color faction)
	{
		for(Player p : pList)
		{
			if(p.getFaction().equals(faction))
			{
				return p;
			}
		}
		return null;
	}
	
	public void setBoard(Board board)
	{
		this.board = board;
	}
	
	public Board getBoard()
	{
		return board;
	}
	
	public int getDay()
	{
		return day;
	}
	
	public void setDay(int day)
	{
		if(day >= 0 && day <= 6)
		{
			this.day = day;
		}
	}
	
	public int getWeek()
	{
		return week;
	}
	
	public void setWeek(int week)
	{
		this.week = week;
	}
	
	public int getTime()
	{
		return time;
	}
	
	public void setTime(int time)
	{
		this.time = time;
	}
	
	public Deck getDeck()
	{
		return deck;
	}
	
	public TreasureBag getBag()
	{
		return bag;
	}
	
	public ScoreCounter getCounter()
	{
		return counter;
	}
	
	@Override public boolean equals(Object other)
	{
		if(other instanceof GameState)
		{
			GameState state = (GameState) other;
			
			if(day == state.day)
			{
				if(week == state.week)
				{
					if(time == state.time)
					{
						if(pList.length == state.pList.length)
						{
							for(int i = 0; i < pList.length; i++)
							{
								if(!pList[i].equals(state.pList[i]))
								{
									return false;
								}
							}
							
							if(board.equals(state.board))
							{
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override public int hashCode()
	{
		return board.hashCode()*pList.length + day + week + time;
	}

}
