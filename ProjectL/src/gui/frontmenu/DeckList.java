package gui.frontmenu;

import cards.Deck;
import test.TestDeck;

public class DeckList {

	public static Class<Deck>[] get()
	{
		return new Class[]{TestDeck.class};
	}
	
}
