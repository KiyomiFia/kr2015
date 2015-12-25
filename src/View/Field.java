package View;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Field extends JLabel{
	private String state;
	private int row;
	private int column;
	
	public Field(int column,int row){
		state = "grass";
		this.row = row;
		this.column = column;
		setIcon(new ImageIcon("pics\\"+state+".png"));
		setBounds((column)*View.Costants.PointFieldSize,(row)*View.Costants.PointFieldSize, View.Costants.PointFieldSize, View.Costants.PointFieldSize);
	}
	
	public String getState(){
		return state;
	}
	
	public void setState(String state){
		this.state = state;
		setIcon(new ImageIcon("pics\\"+state+".png"));
	}
}
