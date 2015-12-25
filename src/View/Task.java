package View;

import java.util.*;

public class Task extends TimerTask{
	private MainForm mainForm;
	
	@Override
	public void run() {
		mainForm.step();
		
	}
	
	public void setMainForm(MainForm mainForm){
		this.mainForm = mainForm;
	}

}
