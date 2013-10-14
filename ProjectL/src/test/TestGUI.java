package test;

import gui.GUI;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.Card;

import players.Faction;
import players.Player;
import score.Loot;
import score.Treasure;
import score.TreasureBag;
import standard.IconServer;

import main.GameState;

public class TestGUI implements GUI {

	//The faction of the player that this UI represents
	private Color faction;
	//The panel which holds the 3 main panels
	private JPanel mainPanel;
	//The panel which holds the player data: score, gold, discard, treasures, den, and hand
	private JPanel playerPanel;
	//The panel which holds the current game states: treasures for each day, and current card layout on the board
	private JPanel gamePanel;
	//The gamestate that this UI will pull its info from
	private GameState latest;
	//The class to serve the various icons
	private IconServer server;
	//A map to help connect cards to icons that represent them
	private HashMap<Color, HashMap<Integer, Icon>> cardIconMap;
	//A map to help connect treasures to icons that represent them
	private HashMap<String, Icon> treasureIconMap;
	
	public TestGUI(Color faction)
	{
		this.faction = faction;
		server = new IconServer();
		cardIconMap = createCardIconMap();
		treasureIconMap = createTreasureIconMap();
		
		JFrame playerScreen = new JFrame("Project L");
		playerScreen.setLayout(new GridLayout(2,1));
		playerScreen.setSize(700, 440);
		playerScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gamePanel = new JPanel();
		
		playerPanel = new JPanel();
		
		playerScreen.add(gamePanel);
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
				map.get(faction).put(i, server.getCardIcon(faction, i));
			}
		}
		
		return map;
	}
	
	/**
	 * Generates a map that links treasure name to an icon
	 * @return the map that links treasure name to the icon that represents the analogous treasure
	 */
	private HashMap<String, Icon> createTreasureIconMap()
	{
		HashMap<String, Icon> map = new HashMap<String, Icon>();
		
		for(String treasure : Treasure.allTreasures())
		{
			map.put(treasure, server.getTreasureIcon(treasure));
		}
		
		return map;
	}
	
	@Override
	public void update(GameState state) {
		latest = new GameState(state);
		updatePlayerPanel();
	}
	
	private void updatePlayerPanel()
	{
		Player player = latest.getPlayer(faction);
		playerPanel.removeAll();
		playerPanel.setLayout(new FlowLayout());
		
		updatePlayerDiscardPile(player);
		
		playerPanel.add(new JLabel("Gold: " + player.getGold()));
		
		playerPanel.add(new JLabel("Score: " + player.getScore()));
		
		updatePlayerTreasureDisplay();
		
		updatePlayerDenAndHand(player);
		
		playerPanel.revalidate();

	}
	
	/**
	 * Updates the discard pile in the player panel
	 * @param player the player whose discard pile is being updated
	 */
	private void updatePlayerDiscardPile(Player player)
	{
		if(player.getDiscard().isEmpty())
		{
			playerPanel.add(new JLabel("The discard is currently empty"));
		}
		else
		{
			JPanel discardPile = new JPanel();
			discardPile.setLayout(new BoxLayout(discardPile, BoxLayout.Y_AXIS));
			discardPile.add(new JLabel("Discard"));
			discardPile.add(getCardGroupDisplayPanel(player.getDiscard()
					.toArray(new Card[0])));
			
			playerPanel.add(discardPile);
		}
	}
	
	/**
	 * Updates the treasure display button in the player panel
	 */
	private void updatePlayerTreasureDisplay()
	{
		JButton bagButton = new JButton();
		
		ImageIcon bagIcon = new ImageIcon(server.bag.getImage()
				.getScaledInstance(40, 56, 1));
		
		bagButton.setIcon(bagIcon);
		
		bagButton.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFrame frame = new JFrame("Your Loot");
				frame.add(getTreasureDisplayPanel(
						latest.getPlayer(faction).getLoot(), -1));	
				frame.pack();
				frame.setVisible(true);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {	
			}
			@Override
			public void mouseExited(MouseEvent arg0) {	
			}
			@Override
			public void mousePressed(MouseEvent arg0) {	
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
		playerPanel.add(bagButton);
	}
	
	/**
	 * Updates the den and hand display for the given player on the player panel
	 * @param player the player whose den and hand need to be updated
	 */
	private void updatePlayerDenAndHand(Player player)
	{
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
			denAndHand.add(new JLabel("Your den is currently empty"));
		}
		else
		{
			denAndHand.add(new JLabel("Den"));
			denAndHand.add(getCardGroupDisplayPanel(player.getDen()
					.toArray(new Card[0])));
		}
		
		playerPanel.add(denAndHand);
	}
	
	/**
	 * Returns a panel displaying a list of the cards provided as input
	 * @param cards the cards whose icons will be displayed
	 * @return the panel displaying a list of the card icons
	 */
	private JPanel getCardGroupDisplayPanel(Card[] cards)
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		for(Card c : cards)
		{
			JLabel label = new JLabel();
			
			final ImageIcon icon = (ImageIcon) getCardIcon(c);
			
			ImageIcon resized = new ImageIcon(icon.getImage()
					.getScaledInstance(40, 56, 0));
			
			label.setIcon(resized);
			
			//Make it so clicking on a small image of a card will
			//pop up a bigger window with the full size picture
			label.addMouseListener(new MouseListener(){
				@Override
				public void mouseClicked(MouseEvent arg0) {
					
					JFrame frame = new JFrame();
					JPanel image = new JPanel();
					frame.add(image);
					image.add(new JLabel(icon));
					frame.pack();
					frame.setVisible(true);
	
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {}
				@Override
				public void mouseExited(MouseEvent arg0) {}
				@Override
				public void mousePressed(MouseEvent arg0) {}
				@Override
				public void mouseReleased(MouseEvent arg0) {}
			});
			
			panel.add(label);
			
		}
		
		return panel;
	}
	
	/**
	 * Returns a panel showing all the treasures in the given loot
	 * @param loot the loot to show the treasures of
	 * @param max the max # of treasures to show (a negative input will show all treasures)
	 * @return the panel showing all treasures in the given loot
	 */
	private JPanel getTreasureDisplayPanel(Loot loot, int max)
	{
		JPanel panel = new JPanel();
		
		double test;
		
		//First, we determine the dimensions (always a square)
		if(max >= 0)
		{
			test = Math.sqrt(max);
		}
		else
		{
			int total = 0;
			
			for(String treasure : Treasure.allTreasures())
			{
				total+= loot.countTreasure(treasure);
			}
			
			max = total;
			test = Math.sqrt(total);
		}
		
		int side = (int) test;
		if((side^2) != max)
		{
			side++;
		}
		
		//Next, we create the panel and populate it with images
		panel.setLayout(new GridLayout(side,side));
		
		int count = 0;
		int tCount = 0;
		int index = 0;
		String[] list = Treasure.allTreasures();
		
		while(count < max && index < list.length)
		{
			if(tCount < loot.countTreasure(list[index]))
			{
				JLabel label = new JLabel();
				ImageIcon resized = new ImageIcon(((ImageIcon) getTreasureIcon(list[index]))
						.getImage().getScaledInstance(40, 56, 0));
				label.setIcon(resized);
				panel.add(label);
				count++;
				tCount++;
			}
			else
			{
				index++;
				tCount = 0;
			}
		}
		
		return panel;
		
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
	
	/**
	 * Given a treasure, returns the icon corresponding to it
	 * @param treasure the treasure for which to display an icon
	 * @return the icon corresponding to the treasure
	 */
	private Icon getTreasureIcon(String treasure) {	
		return treasureIconMap.get(treasure);
	}

}
