package gui.frontmenu;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class FrontMenu {
	
	private static final int MENU_X = 290;
	private static final int MENU_Y = 400;
	private static final int BUTTON_X = 100;
	private static final int BUTTON_Y = 100;
	private static final float GAME_TITLE_SIZE = 64.0f;
	private static final String MENU_TITLE = "Main Menu";
	private static final String GAME_TITLE = "Project L";
	
	public static void main(String[] args)
	{
		launch();
	}
	
	/**
	 * Launches the front menu for the game
	 */
	public static void launch()
	{
		JFrame frame = new JFrame(MENU_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(MENU_X, MENU_Y);
		frame.setResizable(false);
		
		Container panel = frame.getContentPane();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JLabel gameTitle = new JLabel(GAME_TITLE);
		gameTitle.setFont(gameTitle.getFont()
				.deriveFont(GAME_TITLE_SIZE));
		
		panel.add(gameTitle);
		gameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		addButtons(panel);
		
		frame.setVisible(true);
		
	}
	
	/**
	 * Sets up the button layout in the menu
	 * @param panel the panel to add the buttons to
	 */
	private static void addButtons(Container panel)
	{
		JButton play = new JButton("Play");
		
		JButton host = new JButton("Host");
		
		JButton join = new JButton("Join");
		
		Dimension buttonDim = new Dimension(BUTTON_X, BUTTON_Y);
		play.setPreferredSize(buttonDim);
		play.setMaximumSize(buttonDim);
		host.setPreferredSize(buttonDim);
		host.setMaximumSize(buttonDim);
		join.setPreferredSize(buttonDim);
		join.setMaximumSize(buttonDim);

		panel.add(Box.createRigidArea(new Dimension(0,50)));
		panel.add(play);
		panel.add(Box.createRigidArea(new Dimension(0,50)));
		panel.add(host);
		panel.add(Box.createRigidArea(new Dimension(0,50)));
		panel.add(join);
		panel.add(Box.createRigidArea(new Dimension(0,50)));
		
		play.setAlignmentX(Component.CENTER_ALIGNMENT);
		host.setAlignmentX(Component.CENTER_ALIGNMENT);
		join.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
}
