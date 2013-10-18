package test;

import gui.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import cards.Card;

import players.Faction;
import players.Player;
import score.Loot;
import score.Treasure;
import standard.IconServer;

import main.GameState;

public class TestGUI implements GUI {

	//The faction of the player that this UI represents
	private Color faction;
	//This frame holds the panel which is the log for messages/player actions
	private JFrame logFrame;
	//This pane allows the user to scroll through the log
	private JScrollPane logScrollPane;
	//This panel holds the actual log used to hold messages/player actions
	private JEditorPane log;
	//The panel which holds the player data: score, gold, discard, treasures, den, and hand
	private JPanel playerPanel;
	//The panel which holds the current game states: treasures for each day, and current card layout on the board
	private JPanel gamePanel;
	//The panel which will hold the opponents data
	private JPanel opponentPanel;
	//The frame which holds the previous 3 panels
	private JFrame playerScreen;
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
		
		//First, we handle the main player GUI
		this.faction = faction;
		server = new IconServer();
		cardIconMap = createCardIconMap();
		treasureIconMap = createTreasureIconMap();
		
		playerScreen = new JFrame("Project L");
		playerScreen.setSize(800, 660);
		playerScreen.setLayout(new GridLayout(3,1));
		playerScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		opponentPanel = new JPanel();
		gamePanel = new JPanel();
		playerPanel = new JPanel();
		
		playerScreen.add(opponentPanel);
		playerScreen.add(gamePanel);
		playerScreen.add(playerPanel);
		
		playerScreen.setVisible(true);
		
		//Next, we initialize the log functionality
		
		logFrame = new JFrame("Log");
		logFrame.setSize(350, 660);
		logFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		logFrame.setLayout(new BorderLayout());
		logFrame.setResizable(false);

		log = new JEditorPane();
		log.setEditable(false);
		
		logScrollPane = new JScrollPane(log);
		logScrollPane.setVerticalScrollBarPolicy(JScrollPane
				.VERTICAL_SCROLLBAR_AS_NEEDED);
		logScrollPane.setHorizontalScrollBarPolicy(JScrollPane
				.HORIZONTAL_SCROLLBAR_NEVER);
		logFrame.add(logScrollPane, BorderLayout.CENTER);
		logScrollPane.setVisible(true);
		logScrollPane.setPreferredSize(new Dimension(350,660));
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
		updateOpponentPanel();
		updateGamePanel();
		updatePlayerPanel();
		playerScreen.repaint();
	}
	
	/**
	 * Updates the top row of the player screen
	 */
	private void updateOpponentPanel()
	{
		opponentPanel.removeAll();
		Player[] playerList = latest.getPlayerList();
		
		int index = 0;
		
		while(!playerList[index].getFaction().equals(faction))
		{
			index++;
		}
		
		int end = index;
		int writer = 0;
		Player[] opponentList = new Player[playerList.length-1];
		index--;
		while(end != index)
		{
			if(index < 0)
			{
				index = playerList.length-1;
			}
			else
			{
				opponentList[writer] = playerList[index];
				writer++;
				index--;
			}
		}
		
		opponentPanel.setLayout(
				new GridLayout(1, opponentList.length));
		
		for(Player p : opponentList)
		{
			opponentPanel.add(getOpponentPortrait(p));
		}
		
		opponentPanel.revalidate();
	}
	
	/**
	 * Gets the opponent display for the given player
	 * @param player the panel is being generated for
	 * @return the panel displaying the data relevant to the given player, as an opponent
	 */
	private JPanel getOpponentPortrait(Player player)
	{
		JPanel panel = new JPanel();
		
		if(player.getDen().isEmpty())
		{
			panel.setLayout(new GridLayout(2,1));
			panel.add(getGoldScoreDisplayPanel(player));
			JLabel label = new JLabel("Den is currently empty");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			panel.add(label);
		}
		else
		{
			panel.setLayout(new GridLayout(3,1));
			panel.add(getGoldScoreDisplayPanel(player));
			JLabel label = new JLabel("Den");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			panel.add(label);
			panel.add(getCardGroupDisplayPanel(player
					.getDen().toArray(new Card[0])));
		}
		
		panel.setBorder(BorderFactory
				.createLineBorder(player.getFaction()));
		
		return panel;
	}
	
	/**
	 * Updates the middle row of the player screen
	 */
	private void updateGamePanel()
	{
		gamePanel.removeAll();
		gamePanel.setLayout(new GridLayout(2,6));
		
		Card[] deck = latest.getBoard().getDeck();
		
		for(int i = 0; i < 6-deck.length; i++)
		{
			gamePanel.add(new JLabel());
		}
	
		for(Card c : deck)
		{
			JLabel label = getClickableCardLabel(c);
			
			label.setHorizontalAlignment(SwingConstants.CENTER);
			
			gamePanel.add(label);
		}
		
		for(int i = 0; i < 6; i++)
		{
			
			JPanel panel = getTreasureDisplayPanel(latest
					.getBoard().getLoot(i), 6);
			
			if(latest.getDay() == i)
			{
				panel.setBorder(BorderFactory.createLoweredBevelBorder());
			}
			else
			{
				panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			}
			
			gamePanel.add(panel);
		}
		
		gamePanel.revalidate();
	}
	
	/**
	 * Updates lower row of the player screen
	 */
	private void updatePlayerPanel()
	{
		Player player = latest.getPlayer(faction);
		playerPanel.removeAll();
		playerPanel.setLayout(new FlowLayout());
		
		updatePlayerDiscardPile(player);
		
		updatePlayerGoldScoreLog(player);
		
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
			discardPile.add(new JLabel("Discard", SwingConstants.CENTER));
			discardPile.add(getCardGroupDisplayPanel(player.getDiscard()
					.toArray(new Card[0])));
			
			playerPanel.add(discardPile);
		}
	}
	
	/**
	 * Updates the display which houses the gold, score, and log button
	 * @param player the player who owns the display
	 */
	private void updatePlayerGoldScoreLog(Player player)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel gsPane = getGoldScoreDisplayPanel(player);
		gsPane.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(gsPane);
		
		JButton btn = new JButton("Log");
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				logFrame.setVisible(true);
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
		
		panel.add(btn);
		
		playerPanel.add(panel);
		
	}
	
	/**
	 * Displays the player name, score, and gold counter
	 * @param player the player whose gold score and name are being updated
	 */
	private JPanel getGoldScoreDisplayPanel(Player player)
	{
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 0;
		
		JLabel playerName = new JLabel(Faction.getPirateName(player.getFaction()));
		
		playerName.setForeground(player.getFaction());
		
		panel.add(playerName, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		
		panel.add(new JLabel("Gold: " + player.getGold()), c);
		
		c.gridx = 1;
		c.gridy = 1;
		
		panel.add(new JLabel(" "), c);
		
		c.gridx = 2;
		c.gridy = 1;
		
		panel.add(new JLabel("Score: " + player.getScore()), c);
		
		return panel;
	}
	
	/**
	 * Updates the treasure display button in the player panel
	 */
	private void updatePlayerTreasureDisplay()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		panel.add(new JLabel("Loot",
				SwingConstants.CENTER), BorderLayout.NORTH);
		
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
		panel.add(bagButton, BorderLayout.CENTER);
		playerPanel.add(panel);
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
			denAndHand.add(new JLabel("Your hand is currently empty",
					SwingConstants.CENTER));
		}
		else
		{
			denAndHand.add(new JLabel("Hand"),
					SwingConstants.CENTER);
			denAndHand.add(getCardGroupDisplayPanel(player.getHand()
					.toArray(new Card[0])));
		}
		
		if(player.getDen().isEmpty())
		{
			denAndHand.add(new JLabel("Your den is currently empty",
					SwingConstants.CENTER));
		}
		else
		{
			denAndHand.add(new JLabel("Den",
					SwingConstants.CENTER));
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
			JLabel label = getClickableCardLabel(c);
			
			panel.add(label);
		}
		
		return panel;
	}
	
	/**
	 * Helper method to get an individual clickable label for an individual card
	 * @param c the card to get the clickable lavel for
	 * @return the clickable label with the card icon
	 */
	private JLabel getClickableCardLabel(Card c)
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
		
		return label;
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

	@Override
	public void displayMessage(String message) {
		
		//Save the old position of the bar so it doesn't snap back to the top
		int val = logScrollPane.getVerticalScrollBar().getValue();
		log.setText(log.getText() + "\n" + message);
		logScrollPane.getVerticalScrollBar().setValue(val);
		
	}

}
