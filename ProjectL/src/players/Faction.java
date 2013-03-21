package players;

import java.awt.Color;
import java.util.ArrayList;

/**
 * @author Chris
 * Class to manage factions
 */

public class Faction {

	//again, as an example until implemented
	public static final Color RED = Color.RED;
	public static final Color YELLOW = Color.YELLOW;
	
	public static String getPirateName(Color faction)
	{
		return "";
	}
	
	public static String getShipName(Color faction)
	{
		return "";
	}
	
	public static ArrayList<Color> allFactions()
	{
		ArrayList<Color> factions = new ArrayList<Color>();
		factions.add(RED);
		factions.add(YELLOW);
		return factions;
	}

}
