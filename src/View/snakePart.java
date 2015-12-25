package View;

public class SnakePart {
	private int x;
	private int y;
	
	public SnakePart(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public void setXY(int x,int y){
		this.x = x;
		this.y = y;
	}
}
