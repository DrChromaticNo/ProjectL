package score;

/**
 * Interface designed to represent the bag of treasures that the game pulls from
 * @author Chris
 *
 */

public interface TreasureBag {

	/**
	 * removes a random treasure from the bag and returns it
	 * @return a string corresponding to a treasure
	 */
	public String randomTreasure();
	
	/**
	 * Changes the amt of treasure by the given value
	 * @param treasure the treasure whose amount is changing
	 * @param value the amount it's being changed by
	 */
	public void modBag(String treasure, int value);
	
	/**
	 * Resets the bag to its starting values
	 */
	public void resetBag();
	
	/**
	 * Make a deep copy of this treasurebag
	 * @return a deep copy of this treasurebag
	 */
	public TreasureBag copy();
	
}
