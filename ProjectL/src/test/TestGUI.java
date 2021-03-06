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
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import cards.Card;

import players.Faction;
import players.Player;
import score.Loot;
import score.Treasure;
import standard.IconServer;
import utility.Cache;

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
	//The object to serve the various icons
	private IconServer iServer;
	//A map to help connect treasures to icons that represent them
	private HashMap<String, Icon> treasureIconMap;
	//The component that spawns all user prompt dialogs
	private JOptionPane dialogSpawner;
	//The cache to hold the icons
	private Cache<Card,Icon> iconCache;
	
	private static final int GUI_WIDTH = 800;
	private static final int GUI_HEIGHT = 660;
	private static final int LOG_WIDTH = GUI_WIDTH/2;
	private static final int LOG_RIGHT_MARGIN = 15;
	private static final int LOG_LEFT_MARGIN = 5;
	private static final int CARD_WIDTH = 40;
	private static final int CARD_HEIGHT = 56;
	private static final int ICON_CACHE_SIZE = 10;
	
	public TestGUI(Color faction)
	{
		//Setting up the icon cache
		iconCache = new Cache<Card,Icon>(ICON_CACHE_SIZE);
		
		//Handling the main player GUI
		this.faction = faction;
		iServer = new IconServer();
		treasureIconMap = createTreasureIconMap();
		
		playerScreen = new JFrame("Project L");
		playerScreen.setSize(GUI_WIDTH, GUI_HEIGHT);
		playerScreen.setLayout(new GridLayout(3,1));
		playerScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		opponentPanel = new JPanel();
		gamePanel = new JPanel();
		playerPanel = new JPanel();
		
		playerScreen.add(opponentPanel);
		playerScreen.add(gamePanel);
		playerScreen.add(playerPanel);
	    
		//Next, we initialize the log functionality
		
		logFrame = new JFrame("Log");
		logFrame.setSize(LOG_WIDTH, GUI_HEIGHT);
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
		logScrollPane.setPreferredSize(new Dimension(LOG_WIDTH,GUI_HEIGHT));
		
	}
	
	public void launch()
	{
		Thread t = new Thread() {
	        public void run () {
					SwingUtilities.invokeLater(new Runnable() {

					    public void run () {
							playerScreen.setVisible(true);
					    }
					});
	        }
	    };
	    t.start();	
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
			map.put(treasure, iServer.getTreasureIcon(treasure));
		}
		
		return map;
	}
	
	@Override
	public void update(GameState state) {
		latest = new GameState(state);
		Thread t = new Thread() {

	        public void run () {
	            try {
					SwingUtilities.invokeAndWait(new Runnable() {

					    public void run () {
							updateOpponentPanel();
							updateGamePanel();
							updatePlayerPanel();
							playerScreen.repaint();
					    }
					});
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	    };
	    t.start();
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
		
		ImageIcon bagIcon = new ImageIcon(iServer.bag.getImage()
				.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 1));
		
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
	private JLabel getClickableCardLabel(final Card c)
	{
		JLabel label = new JLabel();
		
		final ImageIcon icon = (ImageIcon) getCardIcon(c);
		
		ImageIcon resized = new ImageIcon(icon.getImage()
				.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 0));
		
		label.setIcon(resized);
		
		//Make it so clicking on a small image of a card will
		//pop up a bigger window with the full size picture
		label.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JFrame frame = new JFrame(latest
						.getDeck().getCardName(c));
				
				JPanel image = new JPanel();
				frame.add(image);
				image.setLayout(new BorderLayout());
				
				ImageIcon resized = new ImageIcon(icon.getImage()
						.getScaledInstance(icon.getIconWidth()/2, 
								icon.getIconHeight()/2, 0));
				
				image.add(new JLabel(resized), BorderLayout.SOUTH);
				image.add(getFullCardInfoPanel(c), BorderLayout.CENTER);
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
						.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 0));
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
		
		Icon icon = iconCache.get(card);
		
		if(icon == null)
		{
			iconCache.put(card, iServer.getCardIcon(
				card.getFaction(), card.getValue()));
		}
		
		icon = iconCache.get(card);
		
		return icon;
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
	public synchronized void displayLogMessage(final String message) {
		
		//Manages the field to give enough room for the scrollbar, if visible
		if(logScrollPane.getVerticalScrollBar().isShowing())
		{
			log.setMargin(new Insets(0,LOG_LEFT_MARGIN,0,LOG_RIGHT_MARGIN));
		}
		else
		{
			log.setMargin(new Insets(0,LOG_LEFT_MARGIN,0,LOG_LEFT_MARGIN));
		}
		//Save the old position of the bar so it doesn't snap back to the top
		int val = logScrollPane.getVerticalScrollBar().getValue();
		log.setText(log.getText() + "\n" + message);
		logScrollPane.getVerticalScrollBar().setValue(val);
		
	}
	
	/**
	 * Helper method to spawn a dialog window for given choices
	 * @param title the title of the dialog window
	 * @param prompt the text prompt displayed inside the window
	 * @param choices the choices the user has to pick from
	 * @return the choice that the user decides
	 */
	private Object spawnChoiceWindow(String title, String prompt, Object[] choices)
	{
		//This will create the dialogue window
		dialogSpawner = new JOptionPane(prompt, JOptionPane.QUESTION_MESSAGE,
				JOptionPane.DEFAULT_OPTION, null, choices, null);
		
		final JDialog dialog = dialogSpawner.createDialog(null, title);
		dialog.setModal(false);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		Thread t = new Thread() {
		     public void run () {
		         try {
					SwingUtilities.invokeAndWait(new Runnable() {
					        public void run () {
					        	dialog.setVisible(true);
					        }
			         });
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		      }
		};
		t.start();		
		
		Object result = null;
		while(result == null || result.equals(JOptionPane.UNINITIALIZED_VALUE))
		{
			result = dialogSpawner.getValue();
		}
		
		dialog.dispose();
		
		return result;
	}

	@Override
	public Card makeChoice(String prompt, Card[] cards) {
		HashMap<Icon, Card> dict = new HashMap<Icon, Card>();
		Icon[] icons = new Icon[cards.length];
		int index = 0;
		
		for(Card c : cards)
		{
			ImageIcon icon = (ImageIcon) getCardIcon(c);
			
			ImageIcon resized = new ImageIcon(icon.getImage()
					.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 0));
			
			icons[index] = resized;
			index++;
			dict.put(resized, c);
		}
		
		Object result = spawnChoiceWindow("Card choice", prompt, icons);
		
		return dict.get(result);
	}

	@Override
	public String makeChoice(String prompt, Loot loot) {
		HashMap<Icon, String> dict = new HashMap<Icon, String>();
		int allTreasures = 0;
		
		for(String treasure : Treasure.allTreasures())
		{
			allTreasures += loot.countTreasure(treasure);
		}
		
		Icon[] icons = new Icon[allTreasures];
		int index = 0;
		
		for(String treasure : Treasure.allTreasures())
		{
			//We create an icon for each treasure, even if they are duplicates
			int tCount = loot.countTreasure(treasure);
			while(tCount > 0)
			{
				tCount--;
				Icon resized = new ImageIcon(((ImageIcon) getTreasureIcon(treasure))
					.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 0));
				icons[index] = resized;
				index++;
				dict.put(resized, treasure);
			}
		}
		
		Object result = spawnChoiceWindow("Treasure choice", prompt, icons);
		
		return dict.get(result);
	}

	@Override
	public String makeChoice(String prompt, String[] choices) {
		Object result = spawnChoiceWindow("Choice", prompt, choices);
		
		return (String) result;
	}
	
	/**
	 * Creats a panel with the silver number and action description for a given card
	 * @param card the card to get the info panel for
	 * @return the info panel for that card
	 */
	private JPanel getFullCardInfoPanel(Card card)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		int silver = latest.getDeck()
			.getSilverNum(card.getFaction(), card.getValue());
		
		String desc = latest.getDeck().getCardDesc(card);
		
		String valueInfo = "Value: " + card.getValue();
		
		String silverInfo = "Silver number: " + silver;
		
		panel.add(new JLabel(valueInfo));
		panel.add(new JLabel(silverInfo));
		
		Scanner scanner = new Scanner(desc);
		scanner.useDelimiter("\n");
		while(scanner.hasNext())
		{
			String next = scanner.next();
			panel.add(new JLabel(next));
		}
		scanner.close();
		
		return panel;
	}

	@Override
	public void displayDialog(String title, String message) {
		JOptionPane.showOptionDialog(null, message, title,
		        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
		        null, new String[]{"ok"}, "ok");
	}

}
