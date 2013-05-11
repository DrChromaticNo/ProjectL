package standard;

import java.awt.Color;

import score.Loot;
import score.Treasure;

import main.GameState;
import main.Time;
import cards.Card;
import cards.Estimator;

/**
 * The Estimator to be used with the standard deck, and standard scorer
 * @author Chris
 *
 */
public class StandardEstimator implements Estimator {

	@Override
	public int estimate(GameState state, Card card) {
		int estimate = 0;
		
		if(state.getTime() == Time.PICK_CARDS) //In order to use this class we assume 
			//(and enforce) that we're in the card picking stage
		{
			switch(card.getValue()) {
		
			case 2: estimate = monkeyEst(state,card);
				break;
			case 3: estimate = beggarEst(state,card);
				break;
			case 4: estimate = recruiterEst(state,card);
				break;
			case 5: estimate = cabinboyEst(state,card);
				break;
			case 6: estimate = preacherEst(state,card);
				break;
			case 7: estimate = barkeepEst(state,card);
				break;
			case 9: estimate = carpenterEst(state,card);
				break;
			}
		}
		
		return estimate;
	}
	
	/**
	 * The estimate for the monkey card
	 * @param state state the monkey is being played in
	 * @param card the card being played
	 * @return the estimate for the monkey
	 */
	private int monkeyEst(GameState state, Card card)
	{
		Color faction = card.getFaction();
		
		int relics = state.getPlayer(faction).getLoot()
				.countTreasure(Treasure.RELIC);
		
		return 3*relics; //For each relic, we removed it and thus "recover" 3 gold we'd otherwise lose
	}
	
	/**
	 * The estimate for the beggar card
	 * @param state state the beggar is being played in
	 * @param card the card being played
	 * @return the estimate for the beggar
	 */
	private int beggarEst(GameState state, Card card)
	{
		return 3; //optimally, we get 3 gold from the beggar
	}
	
	/**
	 * The estimate for the recruiter card
	 * @param state state the recruiter is being played in
	 * @param card the card being played
	 * @return the estimate for the recruiter
	 */
	private int recruiterEst(GameState state, Card card)
	{
		//For this one, we take the value of the best card in the den
		//(Needs work)
		Color faction = card.getFaction();
		
		int est = 0;
		
		for(Card c : state.getPlayer(faction).getDen())
		{
			est = Math.max(est, estimate(state,c)); 
		}
		
		return est;
	}
	
	/**
	 * The estimate for the cabin boy card
	 * @param state state the cabin boy is being played in
	 * @param card the card being played
	 * @return the estimate for the cabin boy
	 */
	private int cabinboyEst(GameState state, Card card)
	{
		return 0; //Because it leaves things unchanged?
		//Should maybe take into account if there are all cursed relics
	}
	
	/**
	 * The estimate for the preacher card
	 * @param state state the preacher is being played in
	 * @param card the card being played
	 * @return the estimate for the preacher
	 */
	private int preacherEst(GameState state, Card card)
	{
		Color faction = card.getFaction();
		
		Loot bag = new Loot(state.getPlayer(faction).getLoot());
		
		int est = 0;
		
		//figure out which treasure we keep
		if (bag.countTreasure(Treasure.CHEST) != 0)
		{
			bag.addLoot(Treasure.CHEST, -1);
			est+=5;
		}
		else if(bag.countTreasure(Treasure.JEWEL) != 0)
		{
			bag.addLoot(Treasure.JEWEL, -1);
			est+=3;
		}
		else if(bag.countTreasure(Treasure.GOODS) != 0)
		{
			bag.addLoot(Treasure.GOODS, -1);
			est+=1;
		}
		else if(bag.countTreasure(Treasure.OFFICER) != 0)
		{
			bag.addLoot(Treasure.OFFICER, -1);
			est+=0;
		}
		else if(bag.countTreasure(Treasure.SABER) != 0)
		{
			bag.addLoot(Treasure.SABER, -1);
			est+=0;
		}
		else if(bag.countTreasure(Treasure.MAP) != 0)
		{
			bag.addLoot(Treasure.MAP, -1);
			est+=0;
		}
		else if(bag.countTreasure(Treasure.RELIC) != 0)
		{
			bag.addLoot(Treasure.RELIC, -1);
			est+=-3;
		}
		
		//figure out the impact of the treasures we discard
		for(String s : Treasure.allTreasures())
		{
			int amt = bag.countTreasure(s);
			
			switch(s)
			{
				case Treasure.CHEST: est += amt*-5; break;
				case Treasure.JEWEL: est += amt*-3; break;
				case Treasure.MAP: est += amt*0; break;
				case Treasure.GOODS: est += amt*-1; break;
				case Treasure.OFFICER: est += amt*0; break;
				case Treasure.SABER: est += amt*0; break;
				case Treasure.RELIC: est += amt*3; break;
			}
		}
		
		if(checkForOfficers(state)) //we check to see if the preacher might die before returning to the den
			//doesn't take into account sabers in the future, though, which is a problem
		{
			return est;
		}
		else
		{
			return est+5;
		}
	}
	
	
	/**
	 * The estimate for the barkeep card
	 * @param state state the barkeep is being played in
	 * @param card the card being played
	 * @return the estimate for the barkeep
	 */
	private int barkeepEst(GameState state, Card card)
	{
		int day = state.getDay();
		
		int span = 6-day; //the number of nights the barkeep is optimally active
		
		if(checkForOfficers(state))
		{
			return 0;
		}
		else
		{
			return span;
		}
	}
	
	/**
	 * The estimate for the carpenter card
	 * @param state state the carpenter is being played in
	 * @param card the card being played
	 * @return the estimate for the carpenter
	 */
	private int carpenterEst(GameState state, Card card)
	{
		Color faction = card.getFaction();
		
		int est = -state.getPlayer(faction).getGold()/2; //we lose half our gold
		
		if(checkForOfficers(state)) //we maybe get 10 gold at the end of the game
		{
			return est;
		}
		else
		{
			return est+10;
		}
	}
	
	
	
	/**
	 * Helper method to check if any spanish officers are possible on the day chosen
	 * @param state the state to check spanish officers in
	 * @return true if there are any, false otherwise
	 */
	private boolean checkForOfficers(GameState state) //should also check for sabers in future?
	{
		return state.getBoard().getLoot(state.getDay())
				.countTreasure(Treasure.OFFICER) > 0;
	}

	@Override
	public int treasureValue(GameState state, Card card, String treasure) {
		int value = 0;
		//First, we get the base value of the treasure
		switch(treasure)
		{
			case Treasure.CHEST: value+=5;
			case Treasure.JEWEL: value+=3;
			case Treasure.MAP: if(state.getPlayer(card.getFaction())
					.getLoot().countTreasure(Treasure.MAP)%3 == 2)
				     {
						value+=12; 
				     }
					else
					{
						value+=0;
					}
			case Treasure.GOODS: value+=1;
			case Treasure.OFFICER: value+=0;
			case Treasure.SABER: value+=0;
			case Treasure.RELIC: value+=-3;
		}
		
		//Now, we modify it based on the value you lose by having either of these cards killed
		if(treasure.equals(Treasure.OFFICER))
		{
			if(card.getValue() == 6)
			{
				value+=-5;
			}
			else if(card.getValue() == 9)
			{
				value+=-10;
			}
		}
		
		return value;
	}

}
