import java.util.*;

public class Switcher implements Observer{
	
	private HelloPanel helloPanel;
	private GamePanel gamePanel;
	private MultiplayerPanel multiPanel;
	
	public Switcher(){
		helloPanel = new HelloPanel();
		helloPanel.addObserver(this);
		
		gamePanel = new GamePanel();
		gamePanel.setVisible(false);
		gamePanel.addObserver(this);
		
		multiPanel = new MultiplayerPanel();
		multiPanel.setVisible(false);
		multiPanel.addObserver(this);
	}
	
	public void openGame(){
		helloPanel.setVisible(false);
		gamePanel.go();
		gamePanel.redrawAll();
		gamePanel.setVisible(true);
	}
	
	public void openMultiplayer(){
		helloPanel.setVisible(false);
		multiPanel.go();
		multiPanel.redrawAll();
		multiPanel.setVisible(true);
	}
	
	public void openHelloFromGame(){
		gamePanel.stop();
		gamePanel.setVisible(false);
		helloPanel.setVisible(true);
	}
	
	public void openHelloFromMulti(){
		multiPanel.stop();
		multiPanel.setVisible(false);
		helloPanel.setVisible(true);
	}

	@Override
	public void update(Observable from, Object arg) {
		if (arg.toString().equals("open game")) openGame();
		if (arg.toString().equals("open multi")) openMultiplayer();
		if (arg.toString().equals("open hello from game")) openHelloFromGame();
		if (arg.toString().equals("open hello from multi")) openHelloFromMulti();
	}
	
	public HelloPanel getHelloPanel(){
		return helloPanel;
	}
	
	public GamePanel getGamePanel(){
		return gamePanel;
	}
	
	public MultiplayerPanel getMultiplayerPanel(){
		return multiPanel;
	}
}
