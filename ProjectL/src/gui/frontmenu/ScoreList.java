package gui.frontmenu;

import score.ScoreCounter;
import standard.StandardScoreCounter;

/**
 * Returns the score counter and score counter names for the front menu to use
 * @author Chris
 *
 */
public class ScoreList {

	@SuppressWarnings("unchecked")
	public static Class<ScoreCounter>[] get()
	{
		return new Class[]{StandardScoreCounter.class};
	}
	
	public static String[] getNames()
	{
		return new String[]{"Standard Score Counter"};	
	}
	
	
}
