package main;

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
	
	public GameState(Player[] playerlist, Board board)
	{
		pList = playerlist;
		this.board = board;
		day = 0;
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
	}
	
	public Player[] getPlayerList()
	{
		return pList;
	}
	
	public void setPlayerList(Player[] list)
	{
		pList = list;
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

}
