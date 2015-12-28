
public class TaskMultiplayer implements Runnable{
	private MultiplayerPanel multiPanel;
	private Thread t;
	private volatile long timeBetweenSteps;
	private volatile boolean pause;
	
	public TaskMultiplayer(MultiplayerPanel multiPanel, long timeBetweenSteps){
		this.multiPanel = multiPanel;
		this.timeBetweenSteps = timeBetweenSteps;
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while(true){
			if (pause)
			multiPanel.step();
			try {
				t.sleep(timeBetweenSteps);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
	}
	
	public void setTimeBetweenSteps(long time){
		timeBetweenSteps = time;
	}
}
