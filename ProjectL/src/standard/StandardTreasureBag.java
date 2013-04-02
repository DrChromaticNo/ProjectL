package standard;

import java.util.Random;

import score.Loot;
import score.Treasure;
import score.TreasureBag;

/**
 * Treasure bag to use with the unmodified game
 * @author Chris
 *
 */
public class StandardTreasureBag implements TreasureBag {

	private Loot bag;
	private int totalTreasures;
	
	public StandardTreasureBag()
	{
		bag = Treasure.getLoot();
		resetBag();
	}
	
	public StandardTreasureBag(StandardTreasureBag tBag)
	{
		bag = new Loot(tBag.bag);
		totalTreasures = tBag.totalTreasures;
	}
	
	@Override
	public String randomTreasure() {
		Random random = new Random();
		int pick = random.nextInt(totalTreasures)+1;
		for(String s : Treasure.allTreasures())
		{
			pick = pick - bag.countTreasure(s);
			if(pick <= 0)
			{
				modBag(s,-1);
				return s;
			}
		}
		throw new RuntimeException("couldn't pick a treasure");
	}

	@Override
	public void modBag(String treasure, int value) {
		bag.addLoot(treasure, value);
		totalTreasures+=value;
	}

	@Override
	public void resetBag() {
		bag.addLoot(Treasure.CHEST, -bag.countTreasure(Treasure.CHEST));
		bag.addLoot(Treasure.CHEST, 4);
		
		bag.addLoot(Treasure.JEWEL, -bag.countTreasure(Treasure.JEWEL));
		bag.addLoot(Treasure.JEWEL, 6);
		
		bag.addLoot(Treasure.GOODS, -bag.countTreasure(Treasure.GOODS));
		bag.addLoot(Treasure.GOODS, 10);
		
		bag.addLoot(Treasure.OFFICER, -bag.countTreasure(Treasure.OFFICER));
		bag.addLoot(Treasure.OFFICER, 6);
		
		bag.addLoot(Treasure.SABER, -bag.countTreasure(Treasure.SABER));
		bag.addLoot(Treasure.SABER, 6);
		
		bag.addLoot(Treasure.MAP, -bag.countTreasure(Treasure.MAP));
		bag.addLoot(Treasure.MAP, 8);
		
		bag.addLoot(Treasure.RELIC, -bag.countTreasure(Treasure.RELIC));
		bag.addLoot(Treasure.RELIC, 10);
		
		totalTreasures = 50;
	}

	@Override
	public TreasureBag copy() {
		return new StandardTreasureBag(this);
	}


}
