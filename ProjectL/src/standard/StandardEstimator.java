package standard;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import ai.Estimator;

import players.Player;

import score.Loot;
import score.Treasure;
import standard.actions.Barkeep;
import standard.actions.Beggar;
import standard.actions.Brute;
import standard.actions.CabinBoy;
import standard.actions.Carpenter;
import standard.actions.FreedSlave;
import standard.actions.FrenchOfficer;
import standard.actions.Monkey;
import standard.actions.Mutineer;
import standard.actions.Parrot;
import standard.actions.Preacher;
import standard.actions.Recruiter;
import standard.actions.VoodooWitch;
import standard.actions.Waitress;

import main.GameState;
import main.Time;
import cards.Card;

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
			switch(state.getDeck().getCardName(card)) {
		
			case Parrot.NAME: estimate = parrotEst(state,card);
				break;
			case Monkey.NAME: estimate = monkeyEst(state,card);
				break;
			case Beggar.NAME: estimate = beggarEst(state,card);
				break;
			case Recruiter.NAME: estimate = recruiterEst(state,card);
				break;
			case CabinBoy.NAME: estimate = cabinboyEst(state,card);
				break;
			case Preacher.NAME: estimate = preacherEst(state,card);
				break;
			case Barkeep.NAME: estimate = barkeepEst(state,card);
				break;
			case Waitress.NAME: estimate = waitressEst(state,card);
				break;
			case Carpenter.NAME: estimate = carpenterEst(state,card);
				break;
			case FrenchOfficer.NAME: estimate = frenchofficerEst(state,card);
				break;
			case VoodooWitch.NAME: estimate = voodoowitchEst(state,card);
				break;
			case FreedSlave.NAME: estimate = freedslaveEst(state,card);
				break;
			case Mutineer.NAME: estimate = mutineerEst(state,card);
				break;
			case Brute.NAME: estimate = bruteEst(state,card);
				break;
			}
		}
		
		return estimate + card.getValue();
	}
	
	/**
	 * The estimate for the brute card
	 * @param state state the brute is being played in
	 * @param card the card being played
	 * @return the estimate for the brute
	 */
	private int bruteEst(GameState state, Card card) {
		//Not sure what to do with this...
		return 0;
	}

	/**
	 * The estimate for the mutineer card
	 * @param state state the mutineer is being played in
	 * @param card the card being played
	 * @return the estimate for the mutineer
	 */
	private int mutineerEst(GameState state, Card card) {
		int day = state.getDay();
		
		int span = 6-day; //the number of nights the mutineer is optimally active
		
		int denSize = state.getPlayer(card.getFaction()).getDen().size();
		
		//We either run of time to kill people,
		//or run out of people to kill
		return Math.min(span, denSize)*2;
	}

	/**
	 * The estimate for the freed slave card
	 * @param state state the freed slave is being played in
	 * @param card the card being played
	 * @return the estimate for the freed slave
	 */
	private int freedslaveEst(GameState state, Card card) {
		Color faction = card.getFaction();
		int est = 0;
		
		for(Card c : state.getPlayer(faction).getDen())
		{
			if(c.getValue() > card.getValue())
			{
				est++;
			}
		}
		return est;
	}

	/**
	 * The estimate for the voodoo witch card
	 * @param state state the voodoo witch is being played in
	 * @param card the card being played
	 * @return the estimate for the voodoo witch
	 */
	private int voodoowitchEst(GameState state, Card card) {
		
		//2 times the cards in the discard
		return state.getPlayer(card.getFaction()).getDiscard().size()*2;
		
	}

	/**
	 * The estimate for the french officer card
	 * @param state state the french officer is being played in
	 * @param card the card being played
	 * @return the estimate for the french officer
	 */
	private int frenchofficerEst(GameState state, Card card) {
		Color faction = card.getFaction();
		//If the player has less than 9 gold, they get 5 more
		//Otherwise, the card does nothing
		if(state.getPlayer(faction).getGold() < 9)
		{
			return 5;
		}
		else
		{
			return 0;
		}
	}

	/**
	 * The estimate for the parrot card
	 * @param state state the parrot is being played in
	 * @param card the card being played
	 * @return the estimate for the parrot
	 */
	private int parrotEst(GameState state, Card card) {
		//The reason that the parrot is a min value is because it is essentially identical to another card
		//So you should always play that one instead (unless you want a lot of cards to be in the discard...)
		return -100000;
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
			for(int day = state.getDay()+1; day < 6; day++)
			{
				GameState currState = new GameState(state);
				state.setDay(day);
				est = Math.max(est, estimate(currState,c)); 
			}
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
	 * The estimate for the waitress card
	 * @param state state the waitress is being played in
	 * @param card the card being played
	 * @return the estimate for the waitress
	 */
	private int waitressEst(GameState state, Card card)
	{
		Player player = state.getPlayer(card.getFaction());
		
		int maps = player.getLoot().countTreasure(Treasure.MAP);
		
		//Check to see if the player has any maps and if they'd actually want to sell those maps
		if(maps > 0)
		{
			maps = maps%3;
			int sellDays = Math.min(6-state.getDay(), maps);
			return sellDays*3;
		}
		else
		{
			return 0;
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
		if(treasure != null)
		{
			switch(treasure)
			{
				case Treasure.CHEST: value+=5; break;
				case Treasure.JEWEL: value+=3; break;
				case Treasure.MAP: if(state.getPlayer(card.getFaction())
						.getLoot().countTreasure(Treasure.MAP)%3 == 2)
					     {
							value+=12; 
					     }
						else
						{
							if(state.getDeck().getCardName(card).equals(Waitress.NAME))
							{
								value+=3;
							}
							else
							{
								value+=0;
							}
						} break;
				case Treasure.GOODS: value+=1; break;
				case Treasure.OFFICER: value+=0; break;
				case Treasure.SABER: value+=0; break;
				case Treasure.RELIC: value+=-3; break;
			}
			
			//Now, we modify it based on the value you lose (or gain) by having either of these cards killed
			if(treasure.equals(Treasure.OFFICER))
			{
				if(state.getDeck().getCardName(card).equals(Preacher.NAME))
				{
					value+=-5;
				}
				else if(state.getDeck().getCardName(card).equals(Carpenter.NAME))
				{
					value+=-10;
				}
			}
		}
		else
		{
			//The null value represents not picking a treasure
			value+=0;
		}
		
		return value;
	}
	
	@Override
	public Card[] rankCards(final GameState state, Card[] cards) {
		ArrayList<Card> list = new ArrayList<Card>();
		for(Card c : cards)
		{
			list.add(c);
		}
		
		final HashMap<Card, Integer> map = new HashMap<Card, Integer>();
		
		for(Card c : cards)
		{
			map.put(c, estimate(state, c));
		}
		
		//we sort the arraylist with respect to the estimator class
		Collections.sort(list, new Comparator<Card>() {
			@Override
			public int compare(cards.Card arg0, cards.Card arg1) {
				return -1*(map.get(arg0) - map.get(arg1));
			}});
		
		return list.toArray(cards);
	}

	@Override
	public String[] rankTreasures(GameState state, String[] treasures, Card card) {
		
		ArrayList<String> treasureList = new ArrayList<String>();
		
		for(String t : treasures)
		{
			treasureList.add(t);
		}
		
		final HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		for(String t : treasures)
		{
			map.put(t, treasureValue(state, card, t));
		}
		
		//we sort the arraylist with respect to the estimator class
		Collections.sort(treasureList, new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				return -1*(map.get(arg0) - map.get(arg1));
			}});
		
		return treasureList.toArray(new String[map.keySet().size()]);
	}

}
