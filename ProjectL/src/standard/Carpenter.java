package standard;

import java.awt.Color;

import players.Faction;
import players.Player;
import main.GameState;
import main.Time;
import score.ScoreCounter;
import score.TreasureBag;
import cards.Action;
import cards.Card;
import cards.Deck;

/**
 * The class that represents the carpenter card
 * @author Chris
 *
 */
public class Carpenter implements Action {

	@Override
	public GameState doAction(GameState state, Card card, TreasureBag bag, 
			Deck deck, ScoreCounter counter, int time) 
	{
		if(time == Time.DAY)
		{
			state = carpenterDay(state, card);
		}
		else if(time == Time.EVENING)
		{
			PickTreasure pick = new PickTreasure();
			state = pick.doAction(state, card, bag, deck, counter, time);
		}
		else if(time == Time.NIGHT)
		{
			//Nothing to do
		}
		
		return state;
	}
	
	private GameState carpenterDay(GameState state, Card card)
	{
		Color faction = card.getFaction();
		Player player = state.getPlayer(faction);
		System.out.println();
		System.out.println(Faction.getPirateName(faction) + " lost " + player.getGold()/2 + 
				" gold due to their Carpenter!");
		System.out.println();
		player.addGold(-player.getGold()/2);
		return state;
	}

	@Override
	public int score(GameState state, Card card) 
	{
		return 10;
	}

	@Override
	public GameState[] allActions(GameState state, Card card, TreasureBag bag,
			Deck deck, ScoreCounter counter, int time) {
		
		GameState[] states = new GameState[1];
		states[0] = state;
		
		if(time == Time.DAY)
		{
			states[1] = doAction(new GameState(state), card, bag.copy(), deck, counter, time);
		}
		else if(time == Time.EVENING)
		{
			PickTreasure temp = new PickTreasure();
			states = temp.allActions(new GameState(state), card, bag.copy(), deck, counter, time);
		}
		else if(time == Time.NIGHT)
		{
			//Nothing to do
		}
		
		return states;
	}

}
