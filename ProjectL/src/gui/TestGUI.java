package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import cards.Card;

import players.Faction;
import players.Player;
import test.IconServer;

import main.GameState;

public class TestGUI implements GUI, ActionListener {

	//The faction of the player that this UI represents
	private Color faction;
	//The panel which holds the player data: score, gold, discard, den, and hand
	private JPanel playerPanel;
	//The gamestate that this UI will pull its info from
	private GameState latest;
	//The class to serve the various icons
	private IconServer server;
	//A map to help connect cards to icons that represent them
	private HashMap<Color, HashMap<Integer, Icon>> cardIconMap;
	
	private static final String DISCARD = "discard";
	
	public TestGUI(Color faction)
	{
		this.faction = faction;
		server = new IconServer();
		cardIconMap = createCardIconMap();
		
		JFrame playerScreen = new JFrame("Player Data");
		playerScreen.setLayout(new FlowLayout());
		playerScreen.setSize(500, 200);
		playerScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		playerPanel = new JPanel();
		playerPanel.setLayout(new FlowLayout());
		
		playerScreen.add(playerPanel);
		playerScreen.setVisible(true);
	}
	
	/**
	 * Generates a map that links color and value to an icon
	 * @return the map that links color and value to the icon that represents the analogous card
	 */
	private HashMap<Color, HashMap<Integer, Icon>> createCardIconMap()
	{
		HashMap<Color, HashMap<Integer, Icon>> map = new HashMap<Color, HashMap<Integer, Icon>>();
		
		for(Color faction : Faction.allFactions())
		{
			map.put(faction, new HashMap<Integer, Icon>());
			for(int i = 2; i <= 9; i++)
			{
				map.get(faction).put(i, server.getIcon(faction, i));
			}
		}
		
		return map;
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
		
		JPanel denAndHand = new JPanel();
		denAndHand.setLayout(new BoxLayout(denAndHand, BoxLayout.Y_AXIS));
		
		if(player.getHand().isEmpty())
		{
			denAndHand.add(new JLabel("Your hand is currently empty"));
		}
		else
		{
			denAndHand.add(new JLabel("Hand"));
			denAndHand.add(getCardGroupDisplayPanel(player.getHand()
					.toArray(new Card[0])));
		}
		
		if(player.getDen().isEmpty())
		{
			denAndHand.add(new JLabel("The den is currently empty"));
		}
		else
		{
			denAndHand.add(new JLabel("Den"));
			denAndHand.add(getCardGroupDisplayPanel(player.getDen()
					.toArray(new Card[0])));
		}
		
		playerPanel.add(denAndHand);
		
		playerPanel.revalidate();

	}
	
	/**
	 * Creates the button that shows the discard pile
	 * @return the button that, when pressed, shows the contents of the discard pile
	 */
	private JButton createDiscardButton()
	{
		Player player = latest.getPlayer(faction);
		
		Iterator<Card> iterator = player.getDiscard().iterator();
		
		ImageIcon icon = (ImageIcon) getCardIcon(iterator.next());
		
		ImageIcon resized = new ImageIcon(icon.getImage()
				.getScaledInstance(40, 56, 0));
		
		JButton button = new JButton(resized);
		button.setActionCommand(DISCARD);
		button.addActionListener(this);
		
		return button;
		
	}
	
	/**
	 * Returns a panel displaying a list of the cards provided as input
	 * @param cards the cards whose icons will be displayed
	 * @return the panel displaying a list of the card icons
	 */
	private JPanel getCardGroupDisplayPanel(Card[] cards)
	{
		JPanel panel = new JPanel();
		
		for(Card c : cards)
		{
			JLabel label = new JLabel();
			
			ImageIcon icon = (ImageIcon) getCardIcon(c);
			
			ImageIcon resized = new ImageIcon(icon.getImage()
					.getScaledInstance(40, 56, 0));
			
			label.setIcon(resized);
			
			panel.add(label);
			
		}
		
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getActionCommand().equals(DISCARD))
		{
			JFrame frame = new JFrame("The discard pile");
			
			JPanel panel = getCardGroupDisplayPanel(latest.getPlayer(faction)
					.getDiscard().toArray(new Card[0]));
			
			frame.add(panel);
			frame.pack();
			frame.setVisible(true);
		}
	}
	
	/**
	 * Given a card, returns the icon corresponding to it
	 * @param card the card for which to display an icon
	 * @return the icon corresponding to the card
	 */
	private Icon getCardIcon(Card card) {
		
		return cardIconMap.get(card.getFaction())
				.get(card.getValue());
	}

}
