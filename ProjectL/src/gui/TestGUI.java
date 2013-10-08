package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import cards.Card;

import players.Faction;
import players.Player;
import test.IconServer;

import main.GameState;

public class TestGUI implements GUI {

	private Color faction;
	private JLabel gold;
	private JLabel score;
	private JLabel discard;
	
	public TestGUI(Color faction)
	{
		this.faction = faction;
		JFrame playerScreen = new JFrame("Player Data");
		playerScreen.setLayout(new FlowLayout());
		playerScreen.setSize(500, 200);
		playerScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		discard = new JLabel();
		gold = new JLabel("Gold: ");
		score = new JLabel("Score: ");
		playerScreen.add(discard);
		playerScreen.add(gold);
		playerScreen.add(score);
		playerScreen.setVisible(true);
	}
	
	@Override
	public void update(GameState state) {
		Player player = state.getPlayer(faction);
		
		//Placeholder discard code- will be broken out into own method in future
		if(player.getDiscard().isEmpty())
		{
			discard.setText("The discard is currently empty");
			discard.setIcon(null);
		}
		else
		{
			discard.setText("");
			
			Iterator<Card> iterator = player.getDiscard().iterator();
			
			ImageIcon icon = (ImageIcon) state.getDeck()
					.getCardIcon(iterator.next());
			
			ImageIcon resized = new ImageIcon(icon.getImage()
					.getScaledInstance(40, 56, 0));
			
			discard.setIcon(resized);
		}
		
		gold.setText("Gold: " + player.getGold());
		score.setText("Score: " + player.getScore());

	}

}
