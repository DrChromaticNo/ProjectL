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

import players.Player;
import standard.StandardScoreCounter;
import standard.StandardSettings;
import standard.StandardTreasureBag;

public class NetworkingTester {

	@Test
	public void networkTest1()
	{
		Player p1 = new Player(Color.WHITE, new SimpleAI());
		Player p2 = new Player(Color.BLACK, new NetworkGUI(11100));
		Player[] list = {p1, p2};
		
		final GameClient client = new GameClient(11100, "127.0.0.1", new TestGUI(Color.BLACK));
		
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
	    
	    GameState state = new GameState(list, new Board(), new TestDeck(), new StandardTreasureBag(),
	    		new StandardScoreCounter());
	    
	    Game.run(state, new StandardSettings());
	}
	
}
