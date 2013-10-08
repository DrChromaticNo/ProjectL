package test;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.ImageIcon;

import players.Faction;

/**
 * This class will provide the icons for the various cards
 * (currently configured just to use the placeholder ones I created)
 * @author Chris
 *
 */
public class IconServer {
	
	private static final String root = "Programmer Art";
	private static final String BK = "/Black";
	private static final String BL = "/Blue";
	private static final String R = "/Red";
	private static final String G = "/Green";
	private static final String W = "/White";
	private static final String Y = "/Yellow";
	
	private HashMap<Color, String> colorMap = new HashMap<Color, String>();
	private HashMap<Integer, String> numMap = new HashMap<Integer, String>();
	
	public IconServer()
	{
		colorMap.put(Faction.BLACK, BK);
		colorMap.put(Faction.BLUE, BL);
		colorMap.put(Faction.RED, R);
		colorMap.put(Faction.GREEN, G);
		colorMap.put(Faction.WHITE, W);
		colorMap.put(Faction.YELLOW, Y);
		
		for(int i = 2; i <= 9; i++)
		{
			numMap.put(i, "/" + i + ".png");
		}
	}
	
	/**
	 * Returns the icon corresponding to the provided credentials
	 * @param faction the faction of the card
	 * @param val the value of the card
	 * @return the icon corresponding to the faction and value
	 */
	public ImageIcon getIcon(Color faction, int val)
	{
		return new ImageIcon(root + colorMap.get(faction) + numMap.get(val));
	}
}
