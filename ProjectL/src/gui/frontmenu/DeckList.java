package gui.frontmenu;

import cards.Deck;
import test.TestDeck;

/**
 * Returns all the decks for the play menu to use
 * @author Chris
 *
 */
public class DeckList {

	public static Class<Deck>[] get()
	{
		return new Class[]{TestDeck.class};
	}
	
}
