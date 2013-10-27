package standard;

import main.GameSettings;

/**
 * Standard game settings
 * @author Chris
 *
 */

public class StandardSettings implements GameSettings {

	GameSettings settings;
	
	public StandardSettings()
	{
		settings = new CustomSettings(10, 9, 6, 3);
	}
	
	@Override
	public int initialGold() {
		return settings.initialGold();
	}

	@Override
	public int cardAmt(int week) {
		return settings.cardAmt(week);
	}

	@Override
	public int weekAmt() {
		return settings.weekAmt();
	}

}
