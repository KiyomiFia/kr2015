

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Field extends JLabel{
	private Constants.FieldState state;
	private int row;
	private int column;
	
	public Field(int column,int row){
		state = Constants.FieldState.GRASS;
		this.row = row;
		this.column = column;
		setIcon(new ImageIcon("pics\\grass.png"));
		setBounds((column)*Constants.PointFieldSize,
				(row)*Constants.PointFieldSize, 
				Constants.PointFieldSize, 
				Constants.PointFieldSize);
	}
	
	public Constants.FieldState getState(){
		return state;
	}
	
	public void setState(Constants.FieldState state){
		this.state = state;
		String name = "";
		if (state == Constants.FieldState.APPLE) name = "apple";
		if (state == Constants.FieldState.BODY) name = "body";
		if (state == Constants.FieldState.GRASS) name = "grass";
		if (state == Constants.FieldState.POISON) name = "poison";
		if (state == Constants.FieldState.HEADE) name = "heade";
		if (state == Constants.FieldState.HEADN) name = "headn";
		if (state == Constants.FieldState.HEADS) name = "heads";
		if (state == Constants.FieldState.HEADW) name = "headw";
		if (state == Constants.FieldState.BODYGET) name = "bodyget";
		if (state == Constants.FieldState.HEADEGET) name = "headeget";
		if (state == Constants.FieldState.HEADNGET) name = "headnget";
		if (state == Constants.FieldState.HEADSGET) name = "headsget";
		if (state == Constants.FieldState.HEADWGET) name = "headwget";
		setIcon(new ImageIcon("pics\\"+name+".png"));
	}
}
