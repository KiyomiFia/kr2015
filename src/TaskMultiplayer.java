
public class TaskMultiplayer implements Runnable{
	private MultiplayerPanel multiPanel;
	private Thread t;
	private volatile long timeBetweenSteps;
	private volatile boolean isPause;
	
	public TaskMultiplayer(MultiplayerPanel multiPanel, long timeBetweenSteps){
		this.multiPanel = multiPanel;
		this.timeBetweenSteps = timeBetweenSteps;
		isPause = true;
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while(true){
			if (!isPause){
				multiPanel.step();
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
