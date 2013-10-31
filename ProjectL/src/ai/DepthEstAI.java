package ai;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import main.GameState;
import main.Time;
import players.Player;
import score.Loot;
import score.Treasure;
import cards.Card;

/**
 * This AI class does depth limited/full complexity alpha beta pruning
 * @author Chris
 *
 */
public class DepthEstAI implements AI {

	private int maxDepth;
	private int cardBreadth;
	private Estimator est;
	
	public DepthEstAI(int depth, int breadth, Estimator est)
	{
		maxDepth = depth;
		cardBreadth = breadth;
		this.est = est;
	}
	
	public Estimator getEstimator()
	{
		return est;
	}
	
	@Override
	public GameState choose(Player player, GameState[] states, Card card) {
		
		for(GameState s : states)
		{
			s = findSetToTrue(s,card);
		}
		
		//If there's only one choice, we have no choice but to do it
		if(states.length == 1)
		{
			return states[0];
		}
		
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		GameState choice = null;
		
		//We perform alpha beta search on each state to see if
		//it's the best
		for(GameState s : states)
		{
			int check = alphabeta(new GameState(s), alpha, beta, 
					player.getFaction(), maxDepth);
			
			System.out.println("\ncheck: " + check);
			
			//Check to see if this state is better
			if(check > alpha)
			{
				alpha = check;
				choice = s;
			}
		}
		
		return choice;
	}
	
	/**
	 * Controls which alpha beta routine to call
	 * @param state the game's state at this point
	 * @param alpha the alpha for alpha/beta pruning
	 * @param beta the beta for alpha/beta pruning
	 * @param faction the faction of the player whose score we want to maximize
	 * @param the current depth of the pruning
	 * @return alpha if the player matches the player doing the action, beta otherwise
	 */
	private int alphabeta(GameState state, int alpha, int beta, Color faction, int depth)
	{
		
		int val = 0;
		
		//If we need to choose cards to play on the board we go to the
		//"choosing cards" routine
		if(state.getTime() == Time.PICK_CARDS)
		{
			ArrayList<Color> playerList = new ArrayList<Color>();
			for(Player p : state.getPlayerList())
			{
				playerList.add(p.getFaction());
			}
			
			val = alphabetaCardPicking(new GameState(state), playerList,
					new ArrayList<Card>(), alpha, beta, faction, depth);
		}
		else //if we need to do an action or score, we go to the action or score routine
		{	
			val = ABactionOrScore(new GameState(state), alpha, beta, faction, depth);
		}

		return val;
	}
	
	/**
	 * Called to determine the heuristic value of a given end state
	 * @param state the state being scored
	 * @param faction the faction of the player who we care about
	 * @return the value of the position for the player
	 */
	private int alphabetaScore(GameState state, Color faction)
	{
		state = state.getCounter().score(state);
		
		int playerScore = 0;
		Color maxPlayer = null;
		int maxScore = Integer.MIN_VALUE;
		for(Player p : state.getPlayerList())
		{
			//determine the player's score
			if(p.getFaction().equals(faction))
			{
				playerScore = p.getScore();
			}
			
			//determine the best score
			if(p.getScore() >= maxScore)
			{
				maxScore = p.getScore();
				if(!p.getFaction().equals(faction))
				{
					maxPlayer = p.getFaction();
				}
			}
		}
		//Okay, now we need to figure out if the player had the best score or not
		if(maxPlayer != null)
		{	
			//we need to find the player in 2nd place
			maxScore = Integer.MIN_VALUE;
			for(Player p : state.getPlayerList())
			{
				if(!p.getFaction().equals(faction))
				{    
					if(p.getScore() >= maxScore)
					{
						maxScore = p.getScore();
					}
				}
			}
			
	//		System.out.println("score: " + (playerScore - maxScore));
			
			return playerScore - maxScore; //should always be a positive value
		}
		else
		{	
	//		System.out.println("score: " + (playerScore - maxScore));
			
			return playerScore - maxScore; //should always be a negative value or 0
		}
	}
	
	/**
	 * Figures out which the next action in the day is and scores the board if it's time to be scored,
	 * otherwise figures out the next action and does it
	 * @param state the state of the game to be analyzed at this point
	 * @param alpha the alpha for alpha/beta pruning
	 * @param beta the beta for alpha/beta pruning
	 * @param faction the faction of the player whose score we are trying to maximize
	 * @param depth the depth of the current pruning
	 * @return alpha if the player matches the player doing the action, beta otherwise 
	 */
	private int ABactionOrScore (GameState state, int alpha, int beta, Color faction, int depth)
	{	
		//iterate through normal game "actions" until we find the next action
		if(state.getTime() == Time.DAY)
		{
			Card[] shipDeck = state.getBoard().getDeck();
			
			int index = 0;
			
			while(index < shipDeck.length && shipDeck[index].checkPhase(Time.DAY))
			{
				index++;
			}
			
			if(index >= shipDeck.length) //this means all day actions have been completed
			{
				state.setTime(Time.DUSK);
				return alphabeta(state, alpha, beta, faction, depth);
			}
			else //we case on the next day action
			{
				Card actionCard = shipDeck[index];
				
				GameState[] states = shipDeck[index]
						.possibleDayActions(new GameState(state));
				
				for(GameState s : states)
				{
					//Check to make sure the card is still in play
					if(s.getBoard().getDeck().length > index 
							&& s.getBoard().getDeck()[index].equals(actionCard))
					{
						s.getBoard().getDeck()[index].setTrue(Time.DAY);
					}
				}
				
				//the "meat" of the alpha beta pruning happens in this next section
				if(faction.equals(actionCard.getFaction()))
				{
					for(GameState s : states)
					{
						alpha = Math.max(alpha, 
								alphabeta(s, alpha, beta, faction, depth));
						if(alpha >= beta)
							return alpha;
					}
					return alpha;
				}
				else
				{
					for(GameState s : states)
					{
						beta = Math.min(beta, 
								alphabeta(s, alpha, beta, faction, depth));
						if(alpha >= beta)
							return beta;
					}
					return beta;
				}
			}
		}
		else if(state.getTime() == Time.DUSK)
		{
			Card[] shipDeck = state.getBoard().getDeck();
			
			int index = shipDeck.length-1;
				
			while(index >= 0 && shipDeck[index].checkPhase(Time.DUSK))
			{
				index--;
			}
				
			if(index < 0) //this means all the dusk/evening actions have been completed
			{
				//clears the cards from the deck in preparation for the night phase
				for(Card c : state.getBoard().getDeck())
				{
					state.getPlayer(c.getFaction()).addToDen(c);
				}
				state.getBoard().clearDeck();
				
				state.setTime(Time.NIGHT);
				return alphabeta(state, alpha, beta, faction, depth);
			}
			else //we case on the next dusk/evening action
			{
				Card actionCard = shipDeck[index];
				
				GameState[] states = shipDeck[index]
						.possibleDuskActions(new GameState(state));
				
				for(GameState s : states)
				{
					//Check to make sure the card is still in play
					if(s.getBoard().getDeck().length > index 
							&& s.getBoard().getDeck()[index].equals(actionCard))
					{
						s.getBoard().getDeck()[index].setTrue(Time.DUSK);
					}
				}
				
				//Here is the ordering of the list of states with the estimator
				
				ArrayList<String> tList = new ArrayList<String>();
				final HashMap<GameState, String> tMap = new HashMap<GameState, String>();
				for(GameState s : states)
				{
					String t = diffStateTreasure(state, s, actionCard);
					tList.add(t);
					tMap.put(s, t);
				}
				
				String[] orderedT = 
						est.rankTreasures(state, tList.toArray(new String[0]), actionCard);
				
				//Create a map that maps the treasures to their index in the sorted array
				ArrayList<GameState> stateList = new ArrayList<GameState>(tMap.keySet());
				final HashMap<String, Integer> indexes = new HashMap<String, Integer>();
				for(int i = 0; i < orderedT.length; i++)
				{
					indexes.put(orderedT[i], i);
				}
				
				//Sort gamestates by associated treasure's array index
				Collections.sort(stateList, new Comparator<GameState>(){
					@Override
					public int compare(GameState arg0, GameState arg1) {
						return (indexes.get(tMap.get(arg0)) - indexes.get(tMap.get(arg1)));
					}
					
				});
				
				//Now that we've sorted the states, we can use them
				
				states = stateList.toArray(states);
				
				//Do alpha beta stuff on the resulting sets
				if(faction.equals(actionCard.getFaction()))
				{
					for(int i = 0; i < cardBreadth && i < states.length; i++)
					{
						GameState s = states[i];
						alpha = Math.max(alpha, 
								alphabeta(s, alpha, beta, faction, depth));
						if(alpha >= beta)
							return alpha;
					}
					return alpha;
				}
				else
				{
					for(int i = 0; i < cardBreadth && i < states.length; i++)
					{
						GameState s = states[i];
						beta = Math.min(beta, 
								alphabeta(s, alpha, beta, faction, depth));
						if(alpha >= beta)
							return beta;
					}
					return beta;
				}
			}
		}
		else //the only other options is it being Time.NIGHT
		{
			//Since all these actions occur simultaineously
			//we get each player's response and then
			//update each of the player's states at once.
			
			//It is important to note a night phase action
			//CANNOT modify the state of other players or anything about the game
			//besides ADDING TO the treasure bag
			if(state.getBoard().getDeck().length != 0)
			{
				throw new RuntimeException("Board isn't clear in night phase");
			}
			
			for(Player p : state.getPlayerList())
			{
				ArrayList<Card> den = new ArrayList<Card>(p.getDen());
				Collections.sort(den);
				
				int index = den.size()-1;
				
				while(index >= 0 && den.get(index).checkPhase(Time.NIGHT))
				{
					index--;
				}
				
				if(index >= 0) //this means that there some night actions left to do for this player
				{
					GameState[] states = den.get(index)
							.possibleNightActions(new GameState(state));
					
					Color cardFaction = den.get(index).getFaction();
					for(GameState s : states)
					{
						//Gotta make sure to check if the card is still in play or not
						if(s.getPlayer(cardFaction).getDen().contains(den.get(index)))
						{
							s.getPlayer(cardFaction).removeFromDen(den.get(index));
							
							Card replacement = den.get(index);
							replacement.setTrue(Time.NIGHT);
							s.getPlayer(cardFaction).addToDen(den.get(index));
						}
					}
					
					//Do alpha beta stuff on the resulting sets
					if(faction.equals(den.get(index).getFaction()))
					{
						for(GameState s : states)
						{
							alpha = Math.max(alpha, 
									alphabeta(s, alpha, beta, faction, depth));
							if(alpha >= beta)
								return alpha;
						}
						return alpha;
					}
					else
					{
						for(GameState s : states)
						{
							beta = Math.min(beta, 
									alphabeta(s, alpha, beta, faction, depth));
							if(alpha >= beta)
								return beta;
						}
						return beta;
					}
				}
			}
			
			//once we're done doing all the night cases, we reset the states on all the cards
			for(Player p : state.getPlayerList())
			{
				for(Card c : p.getDen())
				{
					c.resetPhases();
				}
			}
			
			if(state.getDay() == 5 || depth <= 0) //this means we're at the end of the week OR at our maximum depth
			{
				return alphabetaScore(new GameState(state), faction);
			}
			else //otherwise we go to the next day's cases
			{
				state.setTime(Time.PICK_CARDS);
				state.setDay(state.getDay()+1);
				return alphabeta(state, alpha, beta, faction, depth-1);
			}
		}
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
	protected int alphabetaCardPicking(GameState state, 
			ArrayList<Color> playerList, ArrayList<Card> choiceList, 
			int alpha, int beta, Color faction, int depth)
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
			
			return alphabeta(tempState, alpha, beta, faction, depth);
		}
		else
		{
			//otherwise, we iterate through all the possibilities of a given player's card choices
			Color pColor = playerList.remove(0);
			Card[] cardList = est
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
							faction, depth));
					
					if(alpha >= beta)
						return alpha;
					
					i++;
				}
				//we also need to check if they have no cards in their hand
				if(state.getPlayer(pColor).getHand().isEmpty())
				{
					alpha = Math.max(alpha, alphabetaCardPicking(state, new ArrayList<Color>(playerList), 
							new ArrayList<Card>(choiceList), alpha, beta, 
							faction, depth));
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
							faction, depth));
					if(alpha >= beta)
						return beta;
					
					i++;
				}
				if(state.getPlayer(pColor).getHand().isEmpty())
				{
					beta = Math.min(beta, alphabetaCardPicking(state, new ArrayList<Color>(playerList), 
							new ArrayList<Card>(choiceList), alpha, beta, 
							faction, depth));
				}
				return beta;
			}
			
		}
	}

	@Override
	public Card chooseCard(Player player, Card[] cards, GameState state) {
		
		//If there's only one choice, we have no choice but to do it
		if(cards.length == 1)
		{
			return cards[0];
		}
		
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		Card choice = null;
		
		ArrayList<Color> playerList = new ArrayList<Color>();
		for(Player p : state.getPlayerList())
		{
			playerList.add(p.getFaction());
		}
		playerList.remove(player.getFaction());
		
		//We perform alpha beta search on each state to see if
		//it's the best
		for(Card card : cards)
		{
			ArrayList<Card> cardChoices = new ArrayList<Card>();
			cardChoices.add(card);
				
			int check = alphabetaCardPicking(new GameState(state), 
					new ArrayList<Color>(playerList), cardChoices, alpha, beta, 
					player.getFaction(), maxDepth);
				
			System.out.println("\ncheck: " + check);
				
			//Check to see if this state is better
			if(check > alpha)
			{
				alpha = check;
				choice = card;
			}
		}
		
		return choice;
	}
	
	/**
	 * Searches through the state to find the card and sets the time on it to true
	 * @param state the state to search through
	 * @param card the card who needs to have its state changed
	 * @return the state after you flip the card
	 */
	private GameState findSetToTrue(GameState state, Card card)
	{
		//Check the hand
		for(Card c : state.getPlayer(card.getFaction()).getHand())
		{
			if(c.equals(card))
			{
				state.getPlayer(card.getFaction()).removeFromHand(c);
				c.setTrue(state.getTime());
				state.getPlayer(card.getFaction()).addToHand(c);
			}
		}
		
		//Check the discard
		for(Card c : state.getPlayer(card.getFaction()).getDiscard())
		{
			if(c.equals(card))
			{
				state.getPlayer(card.getFaction()).removeFromDiscard(c);
				c.setTrue(state.getTime());
				state.getPlayer(card.getFaction()).addToDiscard(c);
			}
		}
		
		//Check the den
		for(Card c : state.getPlayer(card.getFaction()).getDen())
		{
			if(c.equals(card))
			{
				state.getPlayer(card.getFaction()).removeFromDen(c);
				c.setTrue(state.getTime());
				state.getPlayer(card.getFaction()).addToDen(c);
			}
		}
		
		//Check the board
		for(Card c : state.getBoard().getDeck())
		{
			if(c.equals(card))
			{
				state.getBoard().removeCard(c);
				c.setTrue(state.getTime());
				state.getBoard().addCard(c);
			}
		}
		
		return state;
	}
	
	private String diffStateTreasure(GameState old, GameState curr, Card card)
	{
		Color faction = card.getFaction();
		Loot oldLoot = old.getPlayer(faction).getLoot();
		Loot currLoot = curr.getPlayer(faction).getLoot();
		
		if(oldLoot.equals(currLoot))
			return null;
		
		for(String t : Treasure.allTreasures())
		{
			if(oldLoot.countTreasure(t) != currLoot.countTreasure(t))
			{
				return t;
			}
		}
		
		return null;
	}
}
