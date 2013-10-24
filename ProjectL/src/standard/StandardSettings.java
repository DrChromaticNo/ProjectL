package standard;

import main.GameSettings;

/**
 * Standard game settings
 * @author Chris
 *
 */

public class StandardSettings implements GameSettings {

	@Override
	public int initialGold() {
		return 10;
	}

	@Override
	public int cardAmt(int week) {
		if(week == 0)
			return 9;
		else
			return 6;
	}

	@Override
	public int weekAmt() {
		return 3;
	}

}
