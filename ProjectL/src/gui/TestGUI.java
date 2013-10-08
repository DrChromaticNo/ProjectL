package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.Card;

import players.Faction;
import players.Player;
import test.IconServer;

import main.GameState;

public class TestGUI implements GUI, ActionListener {

	private Color faction;
	private JPanel playerPanel;
	private GameState latest;
	private IconServer server;
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
		
		playerPanel.revalidate();

	}
	
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
	
	private Icon getCardIcon(Card card) {
		
		return cardIconMap.get(card.getFaction())
				.get(card.getValue());
	}

}
