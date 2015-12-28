

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class MainForm{

	private JFrame frame;
	private HelloPanel helloPanel;
	private GamePanel gamePanel;
	private MultiplayerPanel multiPanel;
	private Switcher switcher = new Switcher();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 756, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		helloPanel = switcher.getHelloPanel();
		helloPanel.setBounds(0, 0, 756, 480);
		frame.add(helloPanel);
		
		gamePanel = switcher.getGamePanel();
		gamePanel.setBounds(0, 0, 756, 480);
		gamePanel.setVisible(false);
		frame.add(gamePanel);
		
		multiPanel = switcher.getMultiplayerPanel();
		multiPanel.setBounds(0, 0, 756, 480);
		multiPanel.setVisible(false);
		frame.add(multiPanel);
		
		Action();
	}
	
	public void Action(){
		frame.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if (gamePanel.isVisible()){
					if (e.getKeyCode() == KeyEvent.VK_RIGHT)
						if (!(gamePanel.getState() == Constants.State.LEFT))
							gamePanel.setState(Constants.State.RIGHT);
					if (e.getKeyCode() == KeyEvent.VK_LEFT) 
						if (!(gamePanel.getState() == Constants.State.RIGHT))
							gamePanel.setState(Constants.State.LEFT);
					if (e.getKeyCode() == KeyEvent.VK_UP) 
						if (!(gamePanel.getState() == Constants.State.DOWN))
							gamePanel.setState(Constants.State.UP);
					if (e.getKeyCode() == KeyEvent.VK_DOWN) 
						if (!(gamePanel.getState() == Constants.State.UP))
							gamePanel.setState(Constants.State.DOWN);
				}
				
				if (multiPanel.isVisible()){
					if (e.getKeyCode() == KeyEvent.VK_RIGHT)
						if (!(multiPanel.getMyState() == Constants.State.LEFT))
							multiPanel.setMyState(Constants.State.RIGHT);
					if (e.getKeyCode() == KeyEvent.VK_LEFT) 
						if (!(multiPanel.getMyState() == Constants.State.RIGHT))
							multiPanel.setMyState(Constants.State.LEFT);
					if (e.getKeyCode() == KeyEvent.VK_UP) 
						if (!(multiPanel.getMyState() == Constants.State.DOWN))
							multiPanel.setMyState(Constants.State.UP);
					if (e.getKeyCode() == KeyEvent.VK_DOWN) 
						if (!(multiPanel.getMyState() == Constants.State.UP))
							multiPanel.setMyState(Constants.State.DOWN);
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}
