package gui.frontmenu;

import gui.GUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.Board;
import main.Game;
import main.GameSettings;
import main.GameState;

import players.Faction;
import players.Player;
import score.ScoreCounter;
import score.TreasureBag;
import standard.CustomSettings;
import standard.StandardScoreCounter;
import standard.StandardSettings;
import standard.StandardTreasureBag;

import cards.Deck;
import ai.AI;

/**
 * The screen to hold the assignments for the game's settings
 * @author Chris
 *
 */
public class PlayMenu implements ActionListener, PropertyChangeListener {
	
	private static final String PLAYER_NUM_COMMAND = "player number";
	private static final String PLAY_COMMAND = "play";
	private static final String MENU_COMMAND = "menu";
	private static final String SETTINGS_COMMAND = "settings";
	private static final String DEFAULT_COMMAND = "default";
	private Class<Deck>[] decks;
	private Class<TreasureBag>[] bags;
	private Class<ScoreCounter>[] counters;
	private JFrame frame;
	private JFrame settingsFrame;
	private int playerNumber;
	private JPanel playerList;
	private PlayerInfo[] infos;
	private JComboBox<String> deckSelect;
	private JComboBox<String> bagSelect;
	private JComboBox<String> counterSelect;
	private JFormattedTextField initalGold;
	private JFormattedTextField initalCardDraw;
	private JFormattedTextField cardDraw;
	private JFormattedTextField weekNumber;
	
	
	/**
	 * Sets up and displays the play menu
	 */
	public void launch()
	{
		frame = new JFrame("Play");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		
		frame.add(panel);
		
		panel.add(new JLabel("Number of players:"));
		panel.add(getPlayerNumberSelect());
		
		JButton settingsBtn = new JButton("Advanced Settings");
		settingsBtn.setActionCommand(SETTINGS_COMMAND);
		settingsBtn.addActionListener(this);
		panel.add(settingsBtn);
		
		createSettingsPanel();
		
		JButton playBtn = new JButton("Play");
		playBtn.setActionCommand(PLAY_COMMAND);
		playBtn.addActionListener(this);
		panel.add(playBtn);
		
		JButton menuBtn = new JButton("Main Menu");
		menuBtn.setActionCommand(MENU_COMMAND);
		menuBtn.addActionListener(this);
		panel.add(menuBtn);
		
		infos = new PlayerInfo[0];
		
		playerList = new JPanel();
		updatePlayerList();
		panel.add(playerList);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Creates the advanced settings panel
	 */
	private void createSettingsPanel()
	{
		settingsFrame = new JFrame("Advanced Settings");
		settingsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		settingsFrame.add(panel);
		
		panel.add(getFieldsPanel());
		
		JPanel dropDown = new JPanel();
		dropDown.setLayout(new BoxLayout(dropDown, BoxLayout.Y_AXIS));
		
		JPanel deckPanel = new JPanel();
		deckPanel.add(new JLabel("Deck:"));
		deckPanel.add(getDeckSelect());
		dropDown.add(deckPanel);
		
		JPanel bagPanel = new JPanel();
		bagPanel.add(new JLabel("Treasure Bag:"));
		bagPanel.add(getBagSelect());
		dropDown.add(bagPanel);
		
		JPanel scorePanel = new JPanel();
		scorePanel.add(new JLabel("Score Counter:"));
		scorePanel.add(getScoreSelect());
		dropDown.add(scorePanel);
		
		panel.add(dropDown);
		
		JButton defaultSettings = new JButton("Reset to Default");
		defaultSettings.setActionCommand(DEFAULT_COMMAND);
		defaultSettings.addActionListener(this);
		panel.add(defaultSettings);
		
		settingsFrame.pack();
	}
	
	/**
	 * Creates the panel that has all the optional fields to create a game setting object
	 * @return
	 */
	private JPanel getFieldsPanel()
	{
		int cols = 5;
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
		
		JPanel initalGoldPanel = new JPanel();
		initalGoldPanel.add(new JLabel("Initial Gold: "));
		initalGold = new JFormattedTextField();
		initalGold.setValue(new Integer(10));
		initalGold.addPropertyChangeListener("value", this);
		initalGold.setColumns(cols);
		initalGoldPanel.add(initalGold);
		fieldsPanel.add(initalGoldPanel);
		
		JPanel initalDrawPanel = new JPanel();
		initalDrawPanel.add(new JLabel("Initial Number of Cards: "));
		initalCardDraw = new JFormattedTextField();
		initalCardDraw.setValue(new Integer(9));
		initalCardDraw.addPropertyChangeListener("value", this);
		initalCardDraw.setColumns(cols);
		initalDrawPanel.add(initalCardDraw);
		fieldsPanel.add(initalDrawPanel);
		
		JPanel drawPanel = new JPanel();
		drawPanel.add(new JLabel("Card draw after the first week: "));
		cardDraw = new JFormattedTextField();
		cardDraw.setValue(new Integer(6));
		cardDraw.addPropertyChangeListener("value", this);
		cardDraw.setColumns(cols);
		drawPanel.add(cardDraw);
		fieldsPanel.add(drawPanel);
		
		JPanel weekPanel = new JPanel();
		weekPanel.add(new JLabel("Number of weeks: "));
		weekNumber = new JFormattedTextField();
		weekNumber.setValue(new Integer(3));
		weekNumber.addPropertyChangeListener("value", this);
		weekNumber.setColumns(cols);
		weekPanel.add(weekNumber);
		fieldsPanel.add(weekPanel);
		
		return fieldsPanel;
	}
	
	/**
	 * Resets the advanced settings to their defaults
	 */
	private void defaultSettings()
	{
		initalGold.setValue(new Integer(10));
		initalCardDraw.setValue(new Integer(9));
		cardDraw.setValue(new Integer(6));
		weekNumber.setValue(new Integer(3));
		deckSelect.setSelectedIndex(0);
		bagSelect.setSelectedIndex(0);
		counterSelect.setSelectedIndex(0);
	}
	
	/**
	 * Based on the fields, create a game settings object
	 * @return creates a game settings object based on the fields
	 */
	private GameSettings getSettings()
	{
		int gold = (int) initalGold.getValue();
		int initalDraw = (int) initalCardDraw.getValue();
		int draw = (int) cardDraw.getValue();
		int weeks = (int) weekNumber.getValue();
		
		return new CustomSettings(gold, initalDraw, draw, weeks);
	}
	
	/**
	 * Gets the drop down select for the number of players
	 * @return
	 */
	private JComboBox<String> getPlayerNumberSelect()
	{
		String[] playerNums = new String[5];
		for(int i = 2; i <= 6; i++)
		{
			playerNums[i-2] = Integer.toString(i);
		}
		
		JComboBox<String> playerNumSelect = new JComboBox<String>(playerNums);
		playerNumber = 2;
		playerNumSelect.setSelectedIndex(0);
		playerNumSelect.setActionCommand(PLAYER_NUM_COMMAND);
		playerNumSelect.addActionListener(this);
		
		return playerNumSelect;
	}
	
	/**
	 * Gets the drop down select for the deck
	 * @return
	 */
	private JComboBox<String> getDeckSelect()
	{
		decks = DeckList.get();
		String[] deckNames = DeckList.getNames();
		
		deckSelect = new JComboBox<String>(deckNames);
		deckSelect.setSelectedIndex(0);
		
		return deckSelect;
	}
	
	/**
	 * Gets the drop down select for the treasure bag
	 * @return
	 */
	private JComboBox<String> getBagSelect()
	{
		bags = BagList.get();
		String[] bagNames = BagList.getNames();
		
		bagSelect = new JComboBox<String>(bagNames);
		bagSelect.setSelectedIndex(0);
		
		return bagSelect;
	}
	
	/**
	 * Gets the drop down select for the score counter
	 * @return
	 */
	private JComboBox<String> getScoreSelect()
	{
		counters = ScoreList.get();
		String[] counterNames = ScoreList.getNames();
		
		counterSelect = new JComboBox<String>(counterNames);
		counterSelect.setSelectedIndex(0);
		
		return counterSelect;
	}
	
	/**
	 * Changes the amount of player infos that are displayed depending on the selected number
	 * of players
	 */
	private void updatePlayerList()
	{
		playerList.removeAll();
		playerList.setLayout(new GridLayout(playerNumber,1));
		PlayerInfo[] freshInfos = new PlayerInfo[playerNumber];
		for(int i = 0; i < playerNumber; i++)
		{
			if(i+1 > infos.length)
			{
				PlayerInfo info = new PlayerInfo();
				playerList.add(info.getPanel());
				freshInfos[i] = info;
			}
			else
			{
				playerList.add(infos[i].getPanel());
				freshInfos[i] = infos[i];
			}
		}
		
		infos = freshInfos;
		
		Thread t = new Thread() {

	        public void run () {
					try {
						SwingUtilities.invokeAndWait(new Runnable() {

						    public void run () {
								frame.pack();
								frame.revalidate();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(PLAYER_NUM_COMMAND))
		{
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = 
					(JComboBox<String>) e.getSource();
			String selected = (String) cb.getSelectedItem();
			playerNumber = Integer
					.parseInt(selected);
			
			updatePlayerList();
		}
		else if(e.getActionCommand().equals(PLAY_COMMAND))
		{	
			play();
		}
		else if(e.getActionCommand().equals(MENU_COMMAND))
		{
			Thread t = new Thread() {
		        public void run () {
						SwingUtilities.invokeLater(new Runnable() {

						    public void run () {
								FrontMenu menu = new FrontMenu();
								frame.dispose();
								menu.launch();
						    }
						});
		        }
		    };
		    t.start();	
		}
		else if(e.getActionCommand().equals(SETTINGS_COMMAND))
		{
			Thread t = new Thread() {
		        public void run () {
						SwingUtilities.invokeLater(new Runnable() {

						    public void run () {
						    	settingsFrame.setVisible(true);
						    }
						});
		        }
		    };
		    t.start();	
		}
		else if(e.getActionCommand().equals(DEFAULT_COMMAND))
		{
			defaultSettings();
		}
	}
	
	/**
	 * Sets up and starts a game given the fields
	 */
	private void play()
	{	
		Player[] pList = new Player[playerNumber];
		ArrayList<Color> factions = Faction.allFactions();
		ArrayList<PlayerInfo> infoList = new ArrayList<PlayerInfo>(infos.length);
		for(PlayerInfo info : infos)
		{
			infoList.add(info);
		}
		
		Random random = new Random();
		int index = 0;
		while(infoList.size() != 0)
		{
			int next = random.nextInt(infoList.size());
			pList[index] = infoList.remove(next)
					.getPlayer(chooseFaction(factions));
			index++;
		}
		
		GameState temp = null;
		try {
			temp = new GameState(pList, new Board(), 
					decks[deckSelect.getSelectedIndex()].newInstance(), 
					bags[bagSelect.getSelectedIndex()].newInstance(),
					counters[counterSelect.getSelectedIndex()].newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		final GameState state = temp;
		
		Thread gameThread = new Thread() {
	        public void run () {
	        	Game.run(state, getSettings());
	        }
	    };
	    gameThread.start();
	    
		Thread t = new Thread() {
	        public void run () {
					SwingUtilities.invokeLater(new Runnable() {

					    public void run () {
					    	frame.dispose();
					    	settingsFrame.dispose();
					    }
					});
	        }
	    };
	    t.start();	
	}
	
	/**
	 * Given an arraylist of factions, chooses a random one of them
	 * @param factionList the list of remaining factions
	 * @return a faction (color)
	 */
	private static Color chooseFaction(ArrayList<Color> factionList)
	{
		Random randomColor = new Random();
		int choice = randomColor.nextInt(factionList.size());
		return factionList.remove(choice);
	}
	
	/**
	 * This class represents one players worth of data- type and ai or gui
	 * @author Chris
	 *
	 */
	private class PlayerInfo implements ActionListener
	{
		private static final String HUMAN = "Human";
		private static final String AI = "AI";
		private static final String CHANGE_COMMAND = "change";
		private String playerType = AI;
		private JPanel panel;
		private JComboBox<String> specificType;
		
		public PlayerInfo()
		{
			panel = new JPanel();
			
			JComboBox<String> type = new JComboBox<String>(new String[]{AI,HUMAN});
			type.setSelectedIndex(0);
			type.setActionCommand(CHANGE_COMMAND);
			type.addActionListener(this);
			panel.add(type);
			
			updateTypeMenu();
		}
		
		/**
		 * Updates the drop down to reflect if the player is a human or an ai
		 */
		private void updateTypeMenu()
		{
			if(specificType != null)
			{
				panel.remove(specificType);
			}
			
			if(playerType.equals(AI))
			{
				specificType = new JComboBox<String>(AIList.getNames());
			}
			else
			{
				specificType = new JComboBox<String>(GUIList.getNames());
			}
			
			panel.add(specificType);
			
			Thread t = new Thread() {

		        public void run () {
						try {
							SwingUtilities.invokeAndWait(new Runnable() {

							    public void run () {
									frame.revalidate();
									frame.pack();
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
		 * Returns the selected GUI initialized with the given faction, if applicable
		 * @param faction the faction the initialize
		 * @return  the GUI initialized with the given faction, if applicable
		 */
		@SuppressWarnings("unchecked")
		private GUI getGUI(Color faction)
		{
			if(playerType.equals(HUMAN))
			{	
				@SuppressWarnings("rawtypes")
				Class gui = GUIList.get()[specificType.getSelectedIndex()];
				@SuppressWarnings("rawtypes")
				Constructor cntr = null;
				try {
					cntr = gui.getConstructor(new Class[]{Color.class});
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				} catch (SecurityException e1) {
					e1.printStackTrace();
				}
				
				GUI playerGUI = null;
				try {
					playerGUI = (GUI) cntr.newInstance(faction);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				return playerGUI;
			}
			return null;
		}
		
		/**
		 * Returns the selected AI, if applicable
		 * @return  the AI, if applicable
		 */
		@SuppressWarnings("unchecked")
		private AI getAI()
		{
			if(playerType.equals(AI))
			{
				int index = specificType.getSelectedIndex();
				
				@SuppressWarnings("rawtypes")
				Class ai = AIList.get()[index];
				
				@SuppressWarnings("rawtypes")
				Constructor cntr = null;
				AI cpuAI = null;
				
				if(AIList.getParamTypes()[index] != null)
				{
					try {
						cntr = ai.getConstructor(AIList.getParamTypes()[index]);
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					}
					
					try {
						cpuAI = (ai.AI) cntr.newInstance(AIList.getParams()[index]);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}
				}
				else
				{
					try {
						cpuAI = (ai.AI) ai.newInstance();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				
				return cpuAI;
			}
			
			return null;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getActionCommand().equals(CHANGE_COMMAND))
			{
				@SuppressWarnings("unchecked")
				JComboBox<String> cb = (JComboBox<String>)
						arg0.getSource();
				playerType = (String) cb.getSelectedItem();
				updateTypeMenu();
			}
		}
		
		/**
		 * Returns the panel holding the player info
		 * @return
		 */
		public JPanel getPanel()
		{
			return panel;
		}
		
		/**
		 * Gets the player with the given info
		 * @param faction the faction of the player
		 * @return the player object with the select GUI or AI
		 */
		public Player getPlayer(final Color faction)
		{	
			if(playerType.equals(HUMAN))
			{					    
			    return new Player(faction, getGUI(faction));
			}
			else
			{
				return new Player(faction, getAI());
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() == initalGold)
		{
			if((int)initalGold.getValue() < 0)
			{
				initalGold.setValue(new Integer(0));
			}
		}
		else if(evt.getSource() == initalCardDraw)
		{
			if((int)initalCardDraw.getValue() < 0)
			{
				initalCardDraw.setValue(new Integer(0));
			}
		}
		else if(evt.getSource() == cardDraw)
		{
			if((int)cardDraw.getValue() < 0)
			{
				cardDraw.setValue(new Integer(1));
			}
		}
		else if(evt.getSource() == weekNumber)
		{
			if((int)weekNumber.getValue() < 1)
			{
				weekNumber.setValue(new Integer(1));
			}
		}
		
	}
	
}
