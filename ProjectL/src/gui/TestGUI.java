package gui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

import players.Player;

import main.GameState;

public class TestGUI implements GUI {

	private Color faction;
	private JLabel gold;
	private JLabel score;
	
	public TestGUI(Color faction)
	{
		this.faction = faction;
		JFrame playerScreen = new JFrame("Player Data");
		playerScreen.setLayout(new FlowLayout());
		playerScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gold = new JLabel("Gold: ");
		score = new JLabel("Score: ");
		playerScreen.add(gold);
		playerScreen.add(score);
		playerScreen.setVisible(true);
	}
	
	@Override
	public void update(GameState state) {
		Player player = state.getPlayer(faction);
		gold.setText("Gold: " + player.getGold());
		score.setText("Score: " + player.getScore());

	}

}
