package test;

import main.GameSettings;

public class CustomSettings implements GameSettings {

	private int gold;
	private int firstCards;
	private int laterCards;
	private int weekTotal;
	
	public CustomSettings(int gold, int firstCards, int laterCards, int weekTotal)
	{
		this.gold = gold;
		this.firstCards = firstCards;
		this.laterCards = laterCards;
		this.weekTotal = weekTotal;
	}
	
	@Override
	public int initialGold() {
		return gold;
	}

	@Override
	public int cardAmt(int week) {
		if(week == 0)
			return firstCards;
		else
			return laterCards;
	}

	@Override
	public int weekAmt() {
		return weekTotal;
	}

}
