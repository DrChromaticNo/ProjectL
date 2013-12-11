package main;

import java.awt.Color;

import networking.GameInfo;
import networking.PlayerInfo;

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
	//In the default game, the days run from 0 to 5
	private int day;
	private int time;
	//In the default game, the weeks run from 0 to 2
	private int week;
	private Deck deck;
	private TreasureBag bag;
	private ScoreCounter counter;
	private boolean drawCards; //Represents the set-up before the game starts proper
	//false if not yet started, true once everything is set up
	private static final int SLEEP_TIME = 1000;
	
	public GameState(Player[] playerlist, Board board, Deck deck, 
			TreasureBag bag, ScoreCounter counter)
	{
		pList = playerlist;
		this.board = board;
		day = 0;
		week = 0;
		time = Time.PICK_CARDS;
		drawCards = false;
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
		drawCards = state.drawCards;
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
	
	public boolean checkDraw()
	{
		return drawCards;
	}
	
	public void setDraw(boolean draw)
	{
		drawCards = draw;
	}
	
	/**
	 * Tells the gamestate to update all the GUIS attached to its various players
	 */
	public void updateGUIs()
	{
		boolean human = false;
		for(Player p : pList)
		{
			if(!p.checkCPU())
			{
				human = true;
				p.getGUI().update(getGI());
			}
		}
		
		if(human)
		{
			sleep();
		}
	}
	
	/**
	 * Helper method to cause the GUI updates to not happen super quickly
	 */
	private static void sleep()
	{
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Spits out the game info that refers to this state
	 * @return the GI corresponding to this state
	 */
	public GameInfo getGI()
	{
		PlayerInfo[] infos = new PlayerInfo[pList.length];
		
		for(int i = 0; i < pList.length; i++)
		{
			infos[i] = pList[i].getPI();
		}
		
		return new GameInfo(infos, board.getBI(), week, day, time, drawCards);
	}
	
	/**
	 * Tells the gamestate to launch all the GUIS attached to its various players
	 */
	public void launchGUIs()
	{
		for(Player p : pList)
		{
			if(!p.checkCPU())
			{
				p.getGUI().launch();
			}
		}
	}
	
	/**
	 * Sends the given message to all the GUIs
	 * @param message the message to display in all the GUIs
	 */
	public void log(String message)
	{
		for(Player p : pList)
		{
			if(!p.checkCPU())
			{
				p.getGUI().displayLogMessage(message);
			}
		}
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
						if(drawCards == state.drawCards)
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
		}
		return false;
	}
	
	@Override public int hashCode()
	{
		return board.hashCode()*pList.length + day + week + time;
	}

}
