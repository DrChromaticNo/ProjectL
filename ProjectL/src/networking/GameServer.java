package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import score.Loot;

public class GameServer {

	public static final String GAMESTATE = "gamestate";
	public static final String CARD_CHOICE = "cardchoice";
	public static final String TREASURE_CHOICE = "treasurechoice";
	public static final String GENERAL_CHOICE = "generalchoice";
	public static final String LOG = "log";
	public static final String DIALOG = "dialog";
	public static final String LAUNCH = "launch";
	
	private ServerSocket socket;
	private Socket client;
	
	public GameServer(int port)
	{
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Socket getClient()
	{
		try {
			client = socket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return client;
	}
	
	/**
	 * Writes a gamestate to the currently connected client
	 * @param state the state to write
	 */
	public void writeGameState (GameInfo state)
	{
	    try {
			PrintWriter printOut =
			        new PrintWriter(client.getOutputStream(), true);
			printOut.write(GAMESTATE);
			printOut.flush();
			printOut.close();
			
			ObjectOutputStream objOut =
					new ObjectOutputStream(client.getOutputStream());
			objOut.writeObject(state);
			objOut.flush();
			objOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the card choice to the client and retrieves the answer
	 * @param prompt the prompt to the client
	 * @param choices the choices sent to the client
	 * @return the info chosen by the client
	 */
	public CardInfo writeCardChoice(String prompt, CardInfo[] choices)
	{
		PrintWriter printOut;
		try {
			printOut = new PrintWriter(client.getOutputStream(), true);
			printOut.write(CARD_CHOICE);
			printOut.flush();
			printOut.close();
			
			ObjectOutputStream objOut =
					new ObjectOutputStream(client.getOutputStream());
			objOut.writeObject(prompt);
			objOut.writeObject(choices);
			objOut.flush();
			objOut.close();
			
			ObjectInputStream objIn =
					new ObjectInputStream(client.getInputStream());
			CardInfo info = (CardInfo) objIn.readObject();
			objIn.close();
			
			return info;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Writes the treasure choice to the client and retrieves the answer
	 * @param prompt the prompt to the client
	 * @param loot the loot choices to send to the client
	 * @return the info chosen by the client
	 */
	public String writeTreasureChoice(String prompt, Loot loot)
	{
		PrintWriter printOut;
		try {
			printOut = new PrintWriter(client.getOutputStream(), true);
			printOut.write(TREASURE_CHOICE);
			printOut.flush();
			printOut.close();
			
			ObjectOutputStream objOut =
					new ObjectOutputStream(client.getOutputStream());
			objOut.writeObject(prompt);
			objOut.writeObject(loot);
			objOut.flush();
			objOut.close();
			
			ObjectInputStream objIn =
					new ObjectInputStream(client.getInputStream());
			String choice = (String) objIn.readObject();
			objIn.close();
			
			return choice;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Writes the general choice to the client and retrieves the answer
	 * @param prompt the prompt to the client
	 * @param choices the choices to send to the client
	 * @return the choice chosen by the client
	 */
	public String writeGeneralChoice(String prompt, String[] choices)
	{
		PrintWriter printOut;
		try {
			printOut = new PrintWriter(client.getOutputStream(), true);
			printOut.write(GENERAL_CHOICE);
			printOut.flush();
			printOut.close();
			
			ObjectOutputStream objOut =
					new ObjectOutputStream(client.getOutputStream());
			objOut.writeObject(prompt);
			objOut.writeObject(choices);
			objOut.flush();
			objOut.close();
			
			ObjectInputStream objIn =
					new ObjectInputStream(client.getInputStream());
			String choice = (String) objIn.readObject();
			objIn.close();
			
			return choice;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Writes a message to the client's log
	 * @param message
	 */
	public void writeLog(String message)
	{
		PrintWriter printOut;
		try {
			printOut = new PrintWriter(client.getOutputStream(), true);
			printOut.write(LOG);
			printOut.write(message);
			printOut.flush();
			printOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes a dialog window to the clients gui
	 * @param title
	 * @param message
	 */
	public void writeDialog(String title, String message)
	{
		PrintWriter printOut;
		try {
			printOut = new PrintWriter(client.getOutputStream(), true);
			printOut.write(DIALOG);
			printOut.write(title);
			printOut.write(message);
			printOut.flush();
			printOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tells the client to launch their gui!
	 */
	public void writeLaunchCommand()
	{
		PrintWriter printOut;
		try {
			printOut = new PrintWriter(client.getOutputStream(), true);
			printOut.write(LAUNCH);
			printOut.flush();
			printOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
