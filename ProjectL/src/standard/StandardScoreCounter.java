package standard;

import cards.Card;
import players.Player;
import main.GameState;
import score.ScoreCounter;
import score.Treasure;

/**
 * Scource counter to use with the unmodified game
 * @author Chris
 *
 */
public class StandardScoreCounter implements ScoreCounter {

	@Override
	public GameState score(GameState state) {
		
		//Score the cards in the players' den
		for(Player p : state.getPlayerList())
		{
			int posGold = 0;
			int negGold = 0;
			posGold+= p.getGold();
			
			for(Card c : p.getDen())
			{
				int tempGold = c.score(state);
				if(tempGold >= 0)
				{
					posGold += tempGold;
				}
				else
				{
					negGold += tempGold;
				}
			}
			
			//Score the treasure in the players' loot bag
			for(String treasure : Treasure.allTreasures())
			{
				int amt = p.getLoot().countTreasure(treasure);
				
				switch(treasure)
				{
					case Treasure.CHEST: posGold += amt*5; break;
					case Treasure.JEWEL: posGold += amt*3; break;
					case Treasure.MAP: posGold += (amt/3)*12; break;
					case Treasure.GOODS: posGold += amt*1; break;
					case Treasure.OFFICER: posGold += amt*0; break;
					case Treasure.SABER: posGold += amt*0; break;
					case Treasure.RELIC: negGold += amt*-3; break;
				}
			}
			
			//we tabulate the final score like this to make sure all the negative treasures/cards factor in
			int score = posGold+negGold;
			if(score < 0)
			{
				score = 0;
			}
			
			p.addScore(score);
		}
		
		return state;
	}


}