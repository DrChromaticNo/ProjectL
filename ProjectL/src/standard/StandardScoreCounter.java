package standard;

import cards.Card;
import players.Faction;
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
	public GameState score(GameState state, boolean output) {
		
		//Score the cards in the players' den
		for(final Player p : state.getPlayerList())
		{
			String report = "";
			report = report + "--" + Faction.getPirateName(p.getFaction()) + " Score Report--\n";
			int posGold = 0;
			int negGold = 0;
			
			report = report + "The final gold was " + p.getGold() + "\n";
			posGold+= p.getGold();
			
			for(Card c : p.getDen())
			{
				int tempGold = c.score(state);
				if(tempGold >= 0)
				{
					if(tempGold != 0)
					{
						report = report + "The " + state.getDeck().getCardName(c) + 
								" gives " + tempGold + "\n";
					}
					posGold += tempGold;
				}
				else
				{
					report = report + "The " + state.getDeck().getCardName(c) + 
							" gives " + tempGold + "\n";
					negGold += tempGold;
				}
			}
			
			//Score the treasure in the players' loot bag
			for(String treasure : Treasure.allTreasures())
			{
				int amt = p.getLoot().countTreasure(treasure);
				
				switch(treasure)
				{
					case Treasure.CHEST: 
					{
						if(amt != 0)
						{
							report = report + "The " + amt + " " + Treasure.CHEST + 
								" gives " + amt*5 + "\n";
						}
						posGold += amt*5;
						break;
					}
					case Treasure.JEWEL:
					{
						if(amt != 0)
						{
							report = report + "The " + amt + " " + Treasure.JEWEL + 
								" gives " + amt*3 + "\n";
						}
						posGold += amt*3; 
						break;
					}
					case Treasure.MAP:
					{
						if(amt != 0)
						{
							report = report + "The " + amt + " " + Treasure.MAP + 
								" gives " + (amt/3)*12 + "\n";
						}
						posGold += (amt/3)*12; 
						break;
					}
					case Treasure.GOODS:
					{
						if(amt != 0)
						{
							report = report + "The " + amt + " " + Treasure.GOODS + 
								" gives " + amt*1 + "\n";
						}
						posGold += amt*1; 
						break;
					}
					case Treasure.OFFICER: posGold += amt*0; break;
					case Treasure.SABER: posGold += amt*0; break;
					case Treasure.RELIC:
					{
						if(amt != 0)
						{
							report = report + "The " + amt + " " + Treasure.RELIC + 
								" gives " + amt*-3 + "\n";
						}
						negGold += amt*-3; 
						break;
					}
				}
			}
			
			//we tabulate the final score like this to make sure all the negative treasures/cards factor in
			int score = posGold+negGold;
			if(score < 0)
			{
				report = report + "The ending score was less than 0, and was rounded up to 0\n";
				score = 0;
			}
			
			report = report + "The total ending score was " + p.getScore() + " + " + score + " = ";
			p.addScore(score);
			report = report + p.getScore();
			
			//If this is a player and we are displaying output, we pop up a window
			if(output && !p.checkCPU())
			{
				final String displayMsg = report;
				Thread t = new Thread() {
			        public void run () {
			        	p.getGUI().displayDialog(displayMsg);
			        }
			    };
			    t.start();
			}
		}
		
		return state;
	}
}
