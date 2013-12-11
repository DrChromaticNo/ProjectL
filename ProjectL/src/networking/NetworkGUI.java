package networking;

import score.Loot;
import gui.GUI;

public class NetworkGUI implements GUI {

	private GameServer server;
	
	public NetworkGUI(int port)
	{
		server = new GameServer(port);
	}
	
	@Override
	public void update(GameInfo state) {
		server.writeGameState(state);
	}

	@Override
	public void displayLogMessage(String message) {
		server.writeLog(message);
		
	}

	@Override
	public void displayDialog(String title, String message) {
		server.writeDialog(title, message);
	}

	@Override
	public CardInfo makeChoice(String prompt, CardInfo[] cards) {
		return server.writeCardChoice(prompt, cards);
	}

	@Override
	public String makeChoice(String prompt, Loot loot) {
		return server.writeTreasureChoice(prompt, loot);
	}

	@Override
	public String makeChoice(String prompt, String[] choices) {
		return server.writeGeneralChoice(prompt, choices);
	}

	@Override
	public void launch() {
		server.getClient();
	}

}
