package gui.frontmenu;

import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JoinMenu implements PropertyChangeListener {

	private JFrame frame;
	private JPanel panel;
	private JFormattedTextField port;
	private final int portcols = 7;
	private final int ipcols = 9;
	private JFormattedTextField ip;
	private int portNum;
	private String ipNum;
	
	public JoinMenu()
	{
		frame = new JFrame("Join");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEADING));
		
		panel.add(new JLabel( "IP: "));
		ip = new JFormattedTextField();
		ip.addPropertyChangeListener("value", this);
		ip.setValue(new String("127.0.0.1"));
		ip.setColumns(ipcols);
		
		panel.add(ip);
		
		panel.add(new JLabel("Port: "));
		port = new JFormattedTextField();
		port.addPropertyChangeListener("value", this);
		port.setValue(new Integer(0));
		port.setColumns(portcols);
		
		panel.add(port);
		
		JButton btn = new JButton("Join");
		panel.add(btn);
		
		frame.add(panel);
	}
	
	public void launch()
	{
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if(arg0.getSource() == port)
		{
			portNum = (int) port.getValue();
		}
		else if(arg0.getSource() == ip)
		{
			ipNum = (String) ip.getValue();
		}
	}
	
}
