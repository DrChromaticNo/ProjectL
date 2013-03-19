package main;

import resources.Player;

public class GameState {

	//note, the players are ordered in clockwise orientation (the turn order and seating)
	private Player[] pList;
	private Board board;
	
	public GameState(Player[] playerlist, Board board)
	{
		pList = playerlist;
		this.board = board;
	}
	
	public Player[] getPlayerList()
	{
		return pList;
	}
	
	public Board getBoard()
	{
		return board;
	}

}
