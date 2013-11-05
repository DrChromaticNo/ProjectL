package standard.actions;

import java.awt.Color;

import players.Faction;
import players.Player;

import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the mutineer card
 * @author Chris
 * 
 * Day: N/A
 * Dusk: Choose 1 treasure
 * Night: Discard the card in the den with the lowest rank (other than Mutineer) and gain 2 gold
 * End: N/A
 *
 */
public class Mutineer implements Action {

	public static final String NAME = "Mutineer";
	
	private static final String DESC = "Day: N/A" +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: Discard the card in the den with the lowest rank " +
			"\n(other than Mutineer) and gain 2 gold" +
			"\nEnd: N/A\n";
	
	@Override
	public GameState doAction(GameState state, Card card, int time) {
		if(time == Time.DAY)
		{
			//Do nothing
		}
		else if(time == Time.DUSK)
		{
			PickTreasure temp = new PickTreasure();
			state = temp.doAction(state, card, time);
		}
		else if(time == Time.NIGHT)
		{
			state = mutineerNightAction(state, card, true);
		}
		return state;
	}

	/**
	 * Performs the mutineer night action
	 * @param state the state that the card is being played in
	 * @param card the card that is performing the action
	 * @param output whether or not this method should produce output to the guis
	 * @return the gamestate after the action has been performed
	 */
	private GameState mutineerNightAction(GameState state, Card card, boolean output)
	{
		GameState end = new GameState(state);
		
		Color faction = card.getFaction();
		Player player = end.getPlayer(faction);
		
		//If only the mutineer remains, the pirate can't be offered up for
		//2 gold and nothing happens
		if(player.getDen().size() <= 1)
		{
			if(output)
			{
				end.log("The Mutineer (" + card.abbreviate() + 
						") was the only card in the den, so nothing happens");
			}
			
			return end;
		}
		
		Card least = null;
		int leastVal = Integer.MAX_VALUE;
		
		//Otherwise, the mutineer kills the least ranked pirate for 2 gold
		for(Card c : player.getDen())
		{
			if(c.getValue() < leastVal && !c.equals(card))
			{
				least = c;
				leastVal = c.getValue();
			}
		}
		
		player.removeFromDen(least);
		player.addToDiscard(least);
		player.addGold(2);
		
		if(output)
		{
			end.log("The Mutineer (" + card.abbreviate() + 
					") killed " + least.abbreviate() + " and gave " + Faction.getPirateName(faction)
					+ " 2 gold");
		}
		
		return end;
	}
	
	@Override
	public GameState[] allActions(GameState state, Card card, int time) {
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			//Do nothing
		}
		else if(time == Time.DUSK)
		{
			PickTreasure temp = new PickTreasure();
			states = temp.allActions(state, card, time);
		}
		else if(time == Time.NIGHT)
		{
			states = new GameState[]{mutineerNightAction(state, card, false)};
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
