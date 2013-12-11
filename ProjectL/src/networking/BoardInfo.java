package networking;

import java.io.Serializable;

import score.Loot;

/**
 * Class to hold the transmittable information about the ship's board
 * @author Chris
 *
 */

public class BoardInfo implements Serializable {

	private static final long serialVersionUID = 1705137466302384248L;
	private CardInfo[] deck;
	private Loot[] shipLoot;
	
	public BoardInfo(CardInfo[] deck, Loot[] shipLoot)
	{
		this.deck = deck;
		this.shipLoot = shipLoot;
	}
	
	public CardInfo[] getDeck()
	{
		return deck;
	}
	
	public Loot[] getLoot()
	{
		return shipLoot;
	}
	
}
