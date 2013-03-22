package players;

import java.awt.Color;
import java.util.ArrayList;

/**
 * @author Chris
 * Class to manage factions
 */

public class Faction {

	//theoretically could extend game to support more factions
	//it's already been balanced at 6 though so I'm hardcoding it as this to start
	//Extend this class in another and change references in Game to accomplish this
	//The default deck will only support these colors, though, so you'll need to be sure the
	//deck you use with modified factions supports them fully
	public static final Color RED = Color.RED;
	public static final Color YELLOW = Color.YELLOW;
	public static final Color WHITE = Color.WHITE;
	public static final Color GREEN = Color.GREEN;
	public static final Color BLACK = Color.BLACK;
	public static final Color BLUE = Color.BLUE;
	
	/**
	 * Get the pirate name corresponding to their color
	 * @param faction the faction whose leader's name you want
	 * @return the pirate name corresponding to that faction
	 */
	public static String getPirateName(Color faction)
	{
		if(faction.equals(RED))
		{
			return "Ignatius Bell";
		}
		else if(faction.equals(YELLOW))
		{
			return "Jason Swallow";
		}
		else if(faction.equals(WHITE))
		{
			return "Stanley Rackum";
		}
		else if(faction.equals(GREEN))
		{
			return "Damien Le Maugeois";
		}
		else if(faction.equals(BLACK))
		{
			return "Dirk Chivers";
		}
		else if(faction.equals(BLUE))
		{
			return "Slackey Jack";
		}
		else
		{
			return "Unknown Pirate";
		}
	}
	
	/**
	 * Get the ship name corresponding to a faction
	 * @param faction the faction whose ship you want to get
	 * @return the name of the ship corresponding to that faction
	 */
	public static String getShipName(Color faction)
	{
		if(faction.equals(RED))
		{
			return "Queen's Fancy";
		}
		else if(faction.equals(YELLOW))
		{
			return "Sea Lion";
		}
		else if(faction.equals(WHITE))
		{
			return "Seeker";
		}
		else if(faction.equals(GREEN))
		{
			return "Misericorde";
		}
		else if(faction.equals(BLACK))
		{
			return "Conquista";
		}
		else if(faction.equals(BLUE))
		{
			return "Neptune";
		}
		else
		{
			return "Unknown Ship";
		}
	}
	
	/**
	 * Get a list of all the factions
	 * @return a list of all the factions (by color)
	 */
	public static ArrayList<Color> allFactions()
	{
		ArrayList<Color> factions = new ArrayList<Color>();
		factions.add(RED);
		factions.add(YELLOW);
		factions.add(BLACK);
		factions.add(BLUE);
		factions.add(WHITE);
		factions.add(GREEN);
		return factions;
	}

}
