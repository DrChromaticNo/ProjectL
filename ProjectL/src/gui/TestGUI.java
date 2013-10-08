package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.Card;

import players.Player;

import main.GameState;

public class TestGUI implements GUI, ActionListener {

	private Color faction;
	private JPanel playerPanel;
	private GameState latest;
	private static final String DISCARD = "discard";
	
	public TestGUI(Color faction)
	{
		this.faction = faction;
		JFrame playerScreen = new JFrame("Player Data");
		playerScreen.setLayout(new FlowLayout());
		playerScreen.setSize(500, 200);
		playerScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		playerPanel = new JPanel();
		playerPanel.setLayout(new FlowLayout());
		
		playerScreen.add(playerPanel);
		playerScreen.setVisible(true);
	}
	
	@Override
	public void update(GameState state) {
		latest = new GameState(state);
		Player player = latest.getPlayer(faction);
		playerPanel.removeAll();
		
		//Placeholder discard code- will be broken out into own method in future
		if(player.getDiscard().isEmpty())
		{
			playerPanel.add(new JLabel("The discard is currently empty"));
		}
		else
		{
			playerPanel.add(createDiscardButton());
		}
		
		playerPanel.add(new JLabel("Gold: " + player.getGold()));
		
		playerPanel.add(new JLabel("Score: " + player.getScore()));
		
		playerPanel.revalidate();

	}
	
	private JButton createDiscardButton()
	{
		Player player = latest.getPlayer(faction);
		
		Iterator<Card> iterator = player.getDiscard().iterator();
		
		ImageIcon icon = (ImageIcon) latest.getDeck()
				.getCardIcon(iterator.next());
		
		ImageIcon resized = new ImageIcon(icon.getImage()
				.getScaledInstance(40, 56, 0));
		
		JButton button = new JButton(resized);
		button.setActionCommand(DISCARD);
		button.addActionListener(this);
		
		return button;
		
	}
	
	private void launchCardGroupDisplay(String title, Card[] cards)
	{
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		
		frame.add(panel);
		for(Card c : cards)
		{
			JLabel label = new JLabel();
			
			ImageIcon icon = (ImageIcon) latest.getDeck().getCardIcon(c);
			
			ImageIcon resized = new ImageIcon(icon.getImage()
					.getScaledInstance(40, 56, 0));
			
			label.setIcon(resized);
			
			panel.add(label);
			
		}
		
		frame.pack();
		
		frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getActionCommand().equals(DISCARD))
		{
			launchCardGroupDisplay("The discard pile", latest.getPlayer(faction)
					.getDiscard().toArray(new Card[0]));
		}
	}

}
