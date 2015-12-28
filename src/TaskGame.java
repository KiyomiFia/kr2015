import java.util.*;

public class TaskGame implements Runnable{
	private GamePanel gamePanel;
	private Thread t;
	private volatile long timeBetweenSteps;
	private volatile boolean isPause;
	
	public TaskGame(GamePanel gamePanel, long timeBetweenSteps){
		this.gamePanel = gamePanel;
		this.timeBetweenSteps = timeBetweenSteps;
		isPause = true;
		t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		while(true){
			if (!isPause){
				gamePanel.makeMove();
				try {
					t.sleep(timeBetweenSteps);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		}
	}
	
	public void setTimeBetweenSteps(long time){
		timeBetweenSteps = time;
	}
	
	public void setIsPause(boolean isPause){
		this.isPause = isPause;
	}

}
