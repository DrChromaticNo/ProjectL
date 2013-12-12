package standard.actions;

import gui.GUI;

import java.util.HashMap;

import networking.CardInfo;
import cards.Card;

public class GUIInterfacer {

	/**
	 * Allows the action class to use the makeCardChoice method from GUIs with less code hassle
	 * @param text the text prompt to pass to the GUI
	 * @param cardList the list of the cards to choose from
	 * @param gui the gui to do the actual processing
	 * @return the answer from the gui in card form
	 */
	public static Card makeCardChoice(String text, Card[] cardList, GUI gui)
	{
		CardInfo[] infos = new CardInfo[cardList.length];
		HashMap<CardInfo, Card> map = new HashMap<CardInfo, Card>();
		
		for(int i = 0; i < cardList.length; i++)
		{
			infos[i] = cardList[i].getCI();
			map.put(infos[i], cardList[i]);
		}
		
		CardInfo choice = gui.makeChoice(text, infos);
		
		return map.get(choice);
	}
	
}
