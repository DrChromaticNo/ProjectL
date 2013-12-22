package test;

import java.awt.Color;

import javax.swing.SwingUtilities;

import org.junit.Test;

import main.Board;
import main.Game;
import main.GameState;
import networking.GameClient;
import networking.NetworkGUI;

import ai.SimpleAI;

import players.Faction;
import players.Player;
import standard.StandardScoreCounter;
import standard.StandardSettings;
import standard.StandardTreasureBag;

public class NetworkingTester {

	@Test
	public void networkTest1()
	{
		Player p1 = new Player(Faction.WHITE, new SimpleAI());
		Player p2 = new Player(Faction.BLACK, new NetworkGUI(11100));
		Player p3 = new Player(Faction.RED, new NetworkGUI(11101));
		
		final GameClient client = new GameClient(11100, "127.0.0.1", new TestGUI(Color.BLACK)); 
		final GameClient client2 = new GameClient(11101, "127.0.0.1", new TestGUI(Color.RED)); 
		
		Player[] list = {p1, p2, p3};
		
		Thread t = new Thread() {
	        public void run () {
	        	
	        	client.connect();
	        	while(true)
	        	{
	        		client.read();
	        	}
	        }
	    };
	    t.start();
	    
		Thread t2 = new Thread() {
	        public void run () {
	        	
	        	client2.connect();
	        	while(true)
	        	{
	        		client2.read();
	        	}
	        }
	    };
	    t2.start();
	    
	    GameState state = new GameState(list, new Board(), new TestDeck(), new StandardTreasureBag(),
	    		new StandardScoreCounter());
	    
	    Game.run(state, new StandardSettings());
	}
	
}
