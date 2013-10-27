package gui.frontmenu;

import score.TreasureBag;
import standard.StandardTreasureBag;

/**
 * Returns the treasure bags and bag names for the front menu to use
 * @author Chris
 *
 */
public class BagList {

	@SuppressWarnings("unchecked")
	public static Class<TreasureBag>[] get()
	{
		return new Class[]{StandardTreasureBag.class};
	}
	
	public static String[] getNames()
	{
		return new String[]{"Standard Treasure Bag"};	
	}
	
}
