package gui.frontmenu;

import gui.GUI;
import test.TestGUI;

/**
 * Returns a list of all the GUIs for the play menu to use
 * @author Chris
 *
 */
public class GUIList {

	public static Class<GUI>[] get()
	{
		return new Class[]{TestGUI.class};
	}
	
	public static String[] getNames()
	{
		return new String[]{"Standard GUI"};
	}
	
}
