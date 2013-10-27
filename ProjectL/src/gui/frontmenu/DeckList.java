package gui.frontmenu;

import cards.Deck;
import test.TestDeck;

/**
 * Returns all the decks for the play menu to use
 * @author Chris
 *
 */
public class DeckList {

	@SuppressWarnings("unchecked")
	public static Class<Deck>[] get()
	{
		return new Class[]{TestDeck.class};
	}
	
	public static String[] getNames()
	{
		return new String[]{"Standard Deck"};
	}
	
}
