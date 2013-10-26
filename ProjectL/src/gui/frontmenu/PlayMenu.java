package gui.frontmenu;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.Deck;

/**
 * The screen to hold the assignments for the game's settings
 * @author Chris
 *
 */
public class PlayMenu implements ActionListener {
	
	private static final String PLAYER_NUM_COMMAND = "player number";
	private static final String PLAY_COMMAND = "play";
	private static final String MENU_COMMAND = "menu";
	private Class<Deck>[] decks;
	private JFrame frame;
	private int playerNumber;
	private JPanel playerList;
	
	public void launch()
	{
		frame = new JFrame("Play");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		
		frame.add(panel);
		
		panel.add(new JLabel("Number of players:"));
		panel.add(getPlayerNumberSelect());
		
		panel.add(new JLabel("Deck:"));
		panel.add(getDeckSelect());
		
		JButton playBtn = new JButton("Play");
		playBtn.setActionCommand(PLAY_COMMAND);
		playBtn.addActionListener(this);
		panel.add(playBtn);
		
		JButton menuBtn = new JButton("Main Menu");
		menuBtn.setActionCommand(MENU_COMMAND);
		menuBtn.addActionListener(this);
		panel.add(menuBtn);
		
		playerList = new JPanel();
		updatePlayerList();
		panel.add(playerList);
		
		frame.pack();
		frame.setVisible(true);
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
		String[] deckNames = new String[decks.length];
		int index = 0;
		
		for(Class<Deck> c : decks)
		{
			Deck d = null;
			try {
				d = (Deck) c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			deckNames[index] = d.getName();
			index++;
		}
		
		JComboBox<String> deckSelect = new JComboBox<String>(deckNames);
		deckSelect.setSelectedIndex(0);
		
		return deckSelect;
	}
	
	private void updatePlayerList()
	{
		playerList.removeAll();
		playerList.setLayout(new GridLayout(playerNumber,1));
		for(int i = 0; i < playerNumber; i++)
		{
			playerInfo info = new playerInfo();
			playerList.add(info.getPanel());
		}
		
		frame.pack();
		frame.revalidate();
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
			
		}
		else if(e.getActionCommand().equals(MENU_COMMAND))
		{
			FrontMenu menu = new FrontMenu();
			frame.dispose();
			menu.launch();
		}
	}
	
	/**
	 * This class represents one players worth of data- type and ai or gui
	 * @author Chris
	 *
	 */
	private class playerInfo implements ActionListener
	{
		private static final String HUMAN = "Human";
		private static final String AI = "AI";
		private static final String CHANGE_COMMAND = "change";
		private String playerType = AI;
		private JPanel panel;
		private JComboBox<String> specificType;
		
		public playerInfo()
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
				specificType = new JComboBox<String>(new String[]{"robot blah"});
			}
			else
			{
				specificType = new JComboBox<String>(new String[]{"human blah"});
			}
			
			panel.add(specificType);
			panel.revalidate();
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getActionCommand().equals(CHANGE_COMMAND))
			{
				JComboBox cb = (JComboBox) arg0.getSource();
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
	}
	
}
