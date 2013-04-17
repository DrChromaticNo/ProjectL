package ai;

import java.awt.Color;
import java.util.ArrayList;

import main.GameState;
import main.Time;
import cards.Card;

/**
 * This AI class does alpha beta pruning with the breadth of cards examined determined by the user
 * @author Chris
 *
 */
public class EstAI extends FullAI {

	private int cardBreadth;
	
	public EstAI(int cardBreadth)
	{
		this.cardBreadth = cardBreadth;
	}
	
	/**
	 * Iterates through all possible cards to select
	 * @param state the state before cards have been selected
	 * @param playerList the players yet to choose cards
	 * @param choiceList the cards which have been chosen so far
	 * @param alpha alpha for alpha/beta pruning
	 * @param beta beta for alpha/beta pruning
	 * @param faction the faction of the player whose score we are trying to maximize
	 * @return alpha if the player matches the player doing the action, beta otherwise 
	 */
	@Override
	protected int alphabetaCardPicking(GameState state, 
			ArrayList<Color> playerList, ArrayList<Card> choiceList, int alpha, int beta, Color faction)
	{
		//Base case, if there aren't any more players we go to the first action
		if(playerList.size() == 0)
		{
			GameState tempState = new GameState(state);
			for(Card c : choiceList)
			{
				tempState.getPlayer(c.getFaction()).removeFromHand(c);
				tempState.getBoard().addCard(c);
			}
			
			tempState.setTime(Time.DAY);
			
			return alphabeta(tempState, alpha, beta, faction);
		}
		else
		{
			//otherwise, we iterate through all the possibilities of a given player's card choices
			Color pColor = playerList.remove(0);
			Card[] cardList = state.getCounter()
					.rankCards(state, state.getPlayer(pColor).getHand().toArray(new Card[0]));
			
			if(faction.equals(pColor))
			{
				int i = 0; //we only examine as many cards as specified
				while(i < cardBreadth && i < cardList.length)
				{
					Card c = cardList[i];
					
					ArrayList<Card> tempChoice = new ArrayList<Card>(choiceList);
					tempChoice.add(c);
					
					alpha = Math.max(alpha, alphabetaCardPicking(state, new ArrayList<Color>(playerList), 
							tempChoice, alpha, beta, 
							faction));
					
					if(alpha >= beta)
						return alpha;
					
					i++;
				}
				//we also need to check if they have no cards in their hand
				if(state.getPlayer(pColor).getHand().isEmpty())
				{
					alpha = Math.max(alpha, alphabetaCardPicking(state, new ArrayList<Color>(playerList), 
							new ArrayList<Card>(choiceList), alpha, beta, 
							faction));
				}
				return alpha;
			}
			else
			{
				int i = 0;
				while(i < cardBreadth && i < cardList.length)
				{
					Card c = cardList[i];
					ArrayList<Card> tempChoice = new ArrayList<Card>(choiceList);
					tempChoice.add(c);
					
					beta = Math.min(beta, alphabetaCardPicking(state, 
							new ArrayList<Color>(playerList), tempChoice, alpha, beta,
							faction));
					if(alpha >= beta)
						return beta;
					
					i++;
				}
				if(state.getPlayer(pColor).getHand().isEmpty())
				{
					beta = Math.min(beta, alphabetaCardPicking(state, new ArrayList<Color>(playerList), 
							new ArrayList<Card>(choiceList), alpha, beta, 
							faction));
				}
				return beta;
			}
			
		}
	}

	
}
