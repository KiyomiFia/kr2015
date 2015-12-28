
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Point extends JLabel{

	private String state;
	private int row;
	private int column;
	
	public Point(int column,int row){
		state = "grass";
		this.row = row;
		this.column = column;
		setIcon(new ImageIcon(state+".png"));
		setBounds((column)*50,(row)*50, 50, 50);
	}
	
	public String getState(){
		return state;
	}
	
	public void setState(String state){
		this.state = state;
		setIcon(new ImageIcon(state+".png"));
	}
	
	
}
