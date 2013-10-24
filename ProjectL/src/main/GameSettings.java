package main;

/**
 * An interface to govern basic game values, designed to be easily modifiable
 * @author Chris
 *
 */

public interface GameSettings {

	public int initialGold();
	
	public int cardAmt(int week);
	
	public int weekAmt();
	
}
