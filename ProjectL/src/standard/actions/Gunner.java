package standard.actions;

import java.awt.Color;
import java.util.HashMap;
import java.util.HashSet;

import players.Faction;
import players.Player;

import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * Class that represents the gunner card
 * 
 * Day: Pay 3 gold and discard a character from any den
 * Evening: Choose 1 treasure
 * Night: N/A
 * End: N/A
 * 
 * @author Chris
 *
 */
public class Gunner implements Action {

	public static final String NAME = "Gunner";
	
	public static final String DESC = "Day: Pay 3 gold and discard a character from any den " +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: N/A" +
			"\nEnd: N/A\n";
	
	@Override
	public GameState doAction(GameState state, Card card, int time) {
		if(time == Time.DAY)
		{
			state = gunnerDay(state, card, true);
		}
		else if(time == Time.DUSK)
		{
			PickTreasure temp = new PickTreasure();
			state = temp.doAction(state, card, time);
		}
		else if(time == Time.NIGHT)
		{
			//Do nothing
		}
		
		return state;
	}

	private GameState gunnerDay(GameState state, Card card, boolean output)
	{
		Color faction = card.getFaction();
		
		String log = "";
		
		//First, we pay the gunner
		
		log = log + Faction.getPirateName(faction) + " paid ";
		
		int payment = 0;
		
		if(state.getPlayer(faction).getGold() > 3)
		{
			payment = 3;
		}
		else
		{
			payment = state.getPlayer(faction).getGold();
		}
		
		GameState endState = new GameState(state);
		
		endState.getPlayer(faction).addGold(-payment);
		log = log + payment + " gold to their Gunner (" + card.abbreviate() + ") and";
		
		//Next we kill the appropriate card (or don't)
		
		Card[] possibleArray = new Card[0];
		HashSet<Card> possibleSet = new HashSet<Card>();
		
		for(Player p : endState.getPlayerList())
		{
			possibleSet.addAll(p.getDen());
		}
		
		if(possibleSet.isEmpty())
		{
			log = log + " wasn't able to kill anybody!";
			if(output)
			{
				endState.log(log);
			}
			return endState;
		}
		
		possibleArray = possibleSet.toArray(possibleArray);
		if(endState.getPlayer(faction).checkCPU())
		{
			HashMap<GameState, String> map = allKillOutcomes(state, possibleArray);
			endState = endState.getPlayer(faction)
					.chooseState(map.keySet().toArray(new GameState[0]), card);
			log = log + " killed " + map.get(endState) + "!";
		}
		else
		{
			Card choice = endState.getPlayer(faction).getGUI()
					.makeChoice("Choose a card for the Gunner to shoot:", possibleArray);
			
			Player widower = endState.getPlayer(choice.getFaction());
			widower.removeFromDen(choice);
			widower.addToDiscard(choice);
			
			log = log + " killed " + choice.abbreviate() + "!";
		}
		
		if(output)
			endState.log(log);
		
		return endState;
	}
	
	/**
	 * Returns a hashmap mapping states to the cards killed in that state
	 * @param state the original state
	 * @param cards the cards to be killed
	 * @return the map
	 */
	private HashMap<GameState, String> allKillOutcomes(GameState state, Card[] cards)
	{
		HashMap<GameState, String> map = new HashMap<GameState, String>();
		for(Card c : cards)
		{
			GameState endState = new GameState(state);
			
			Color faction = c.getFaction();
			
			endState.getPlayer(faction).removeFromDen(c);
			endState.getPlayer(faction).addToDiscard(c);
			
			map.put(endState, c.abbreviate());
		}
		
		return map;
	}
	
	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			GameState root = new GameState(state);
			Color faction = card.getFaction();
			int amt = 3;
			if(root.getPlayer(faction).getGold() < 3)
			{
				amt = root.getPlayer(faction).getGold();
			}
			root.getPlayer(faction).addGold(-amt);
			
			Card[] possibleArray = new Card[0];
			HashSet<Card> possibleSet = new HashSet<Card>();
			
			for(Player p : root.getPlayerList())
			{
				possibleSet.addAll(p.getDen());
			}
			
			if(possibleSet.isEmpty())
			{
				states[0] = root;
			}
			else
			{
				possibleArray = possibleSet.toArray(possibleArray);
				states = allKillOutcomes(root, possibleArray).keySet().toArray(states);
			}
		}
		else if(time == Time.DUSK)
		{
			PickTreasure temp = new PickTreasure();
			states = temp.allActions(new GameState(state), card, time);
		}
		else if(time == Time.NIGHT)
		{
			//Nothing to do
		}
		
		return states;
	}

	@Override
	public int score(GameState state, Card card) {
		return 0;
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getDesc() {
		return DESC;
	}

}
