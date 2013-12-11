package networking;

import java.awt.Color;
import java.io.Serializable;

/**
 * A serializable class to pass around the important info/constructs from gamestate
 * @author Chris
 *
 */
public class GameInfo implements Serializable {

	//Contains same info as in the gamestate that creates it
	private static final long serialVersionUID = 8151726825174497127L;
	private PlayerInfo[] pList;
	private BoardInfo board;
	private int day;
	private int time;
	private int week;
	private boolean drawCards;
	
	public GameInfo(PlayerInfo[] playerList, BoardInfo board, int week, 
			int day, int time, boolean draw)
	{
		pList = playerList;
		this.board = board;
		this.week = week;
		this.day = day;
		this.time = time;
		this.drawCards = draw;
	}
	
	public PlayerInfo[] getPList()
	{
		return pList;
	}
	
	/**
	 * A helpful helper method for manipulating the gameinfo
	 * @param faction the faction to match a player to
	 * @return the player with the corresponding faction to the input, otherwise null
	 */
	public PlayerInfo getPlayer(Color faction)
	{
		for(PlayerInfo p : pList)
		{
			if(p.getFaction().equals(faction))
			{
				return p;
			}
		}
		return null;
	}
	
	public BoardInfo getBoard()
	{
		return board;
	}
	
	public int getDay()
	{
		return day;
	}
	
	public int getWeek()
	{
		return week;
	}
	
	public int getTime()
	{
		return time;
	}
	
	public boolean getDraw()
	{
		return drawCards;
	}
}
