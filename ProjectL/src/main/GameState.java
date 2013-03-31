package main;

import java.awt.Color;

import players.Player;

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
	
	public GameState(Player[] playerlist, Board board)
	{
		pList = playerlist;
		this.board = board;
		day = 0;
		week = 0;
		time = Time.PICK_CARDS;
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

}
