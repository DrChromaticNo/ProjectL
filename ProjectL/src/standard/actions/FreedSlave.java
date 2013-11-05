package standard.actions;

import java.awt.Color;

import players.Faction;
import players.Player;

import main.GameState;
import main.Time;
import cards.Action;
import cards.Card;

/**
 * The class that represents the freed slave card
 * @author Chris
 * 
 * Day: N/A
 * Dusk: Choose 1 treasure
 * Night: Gain 1 gold for each card in the den with rank high than Freed Slave
 * End: N/A
 *
 */
public class FreedSlave implements Action {

	public static final String NAME = "Freed Slave";
	
	private static final String DESC = "Day: N/A" +
			"\nDusk: Choose 1 treasure" + 
			"\nNight: Gain 1 gold for each card in the den with rank higher than Freed Slave" +
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
			state = freedSlaveNightAction(state, card, true);
		}
		return state;
	}
	
	/**
	 * Performs the freed slave night action
	 * @param state the state that the card is being played in
	 * @param card the card that is performing the action
	 * @param output whether or not this method should produce output to the guis
	 * @return the gamestate after the action has been performed
	 */
	private GameState freedSlaveNightAction(GameState state, Card card, boolean output)
	{
		GameState end = new GameState(state);
		Color faction = card.getFaction();
		Player player = end.getPlayer(faction);
		int val = card.getValue();
		int extraGold = 0;
		
		for(Card c : player.getDen())
		{
			if(c.getValue() > val)
			{
				extraGold++;
			}
		}
		
		player.addGold(extraGold);
		
		if(output)
		{
			end.log("The Freed Slave (" + card.abbreviate() + ") gives " 
					+ Faction.getPirateName(faction) + " " + extraGold + " gold");
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
			states = new GameState[]{freedSlaveNightAction(state, card, false)};
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
