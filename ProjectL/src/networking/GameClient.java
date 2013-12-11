package networking;

import gui.GUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream; 
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import score.Loot;

public class GameClient {

	private int port;
	private String name;
	private Socket socket;
	private boolean connected;
	private GUI g;
	
	public GameClient(int port, String name, GUI g)
	{
		this.port = port;
		this.name = name;
		connected = false;
		this.g = g;
	}
	
	public void connect()
	{
		try {
			socket = new Socket(name, port);
			connected = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void read()
	{
		if(connected)
		{
			try {
				BufferedReader in = new BufferedReader(
				        new InputStreamReader(socket.getInputStream()));
				String line = in.readLine();
				in.close();
				
				switch(line){
				case GameServer.GAMESTATE: {
					g.update(readGameInfo());
					break;
				}
				
				case GameServer.CARD_CHOICE: {
					CardInfo choice = readCardChoice();
					ObjectOutputStream objOut =
							new ObjectOutputStream(socket.getOutputStream());
					objOut.writeObject(choice);
					objOut.flush();
					objOut.close();
					break;
				}
				
				case GameServer.TREASURE_CHOICE: {
					String choice = readTreasureChoice();
					ObjectOutputStream objOut =
							new ObjectOutputStream(socket.getOutputStream());
					objOut.writeObject(choice);
					objOut.flush();
					objOut.close();
					break;
				}
				
				case GameServer.GENERAL_CHOICE: {
					String choice = readGeneralChoice();
					ObjectOutputStream objOut =
							new ObjectOutputStream(socket.getOutputStream());
					objOut.writeObject(choice);
					objOut.flush();
					objOut.close();
					break;
				}
				
				case GameServer.LOG: {
					readLog();
					break;
				}
				
				case GameServer.DIALOG: {
					readDialog();
					break;
				}
				
				case GameServer.LAUNCH: {
					g.launch();
					break;
				}
				
				};
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Reads a game info from the server
	 * @return
	 */
	public GameInfo readGameInfo()
	{
		if(connected)
		{
			try {
				ObjectInputStream objIn =
						new ObjectInputStream(socket.getInputStream());
				GameInfo info = (GameInfo) objIn.readObject();
				return info;
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Reads and processes a card choice from the server
	 * @return
	 */
	public CardInfo readCardChoice()
	{
		if(connected)
		{
			try {
				ObjectInputStream objIn =
						new ObjectInputStream(socket.getInputStream());
				String prompt = (String) objIn.readObject();
				CardInfo[] infos = (CardInfo[]) objIn.readObject();
				
				return g.makeChoice(prompt, infos);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Reads and processes a treasure choice from the server
	 * @return
	 */
	public String readTreasureChoice()
	{
		if(connected)
		{
			try {
				ObjectInputStream objIn =
						new ObjectInputStream(socket.getInputStream());
				String prompt = (String) objIn.readObject();
				Loot loot = (Loot) objIn.readObject();
				
				return g.makeChoice(prompt, loot);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Reads and processes a general choice from the server
	 * @return
	 */
	public String readGeneralChoice()
	{
		if(connected)
		{
			try {
				ObjectInputStream objIn =
						new ObjectInputStream(socket.getInputStream());
				String prompt = (String) objIn.readObject();
				String[] choices = (String[]) objIn.readObject();
				
				return g.makeChoice(prompt, choices);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Reads a log to the guis log
	 */
	public void readLog()
	{
		ObjectInputStream objIn;
		try {
			objIn = new ObjectInputStream(socket.getInputStream());
			String message = (String) objIn.readObject();
			
			g.displayLogMessage(message);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Spawns a dialog box on the clients gui
	 */
	public void readDialog()
	{
		ObjectInputStream objIn;
		try {
			objIn = new ObjectInputStream(socket.getInputStream());
			String title = (String) objIn.readObject();
			String message = (String) objIn.readObject();
			
			g.displayDialog(title, message);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
