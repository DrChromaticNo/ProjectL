package main;

import resources.Player;

/**
 * @author Chris
 * Keeps track of the state of the game
 */

public class GameState {

	//note, the players are ordered in clockwise orientation (the turn order and seating)
	private Player[] pList;
	private Board board;
	
	public GameState(Player[] playerlist, Board board)
	{
		pList = playerlist;
		this.board = board;
	}
	
	public GameState(GameState state)
	{
		pList = new Player[state.pList.length];
		for(int i = 0; i < state.pList.length; i++)
		{
			pList[i] = new Player(state.pList[i]);
		}
		board = new Board(state.board);
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
