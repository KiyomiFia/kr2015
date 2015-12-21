package View;

public class snakePart {
	private int x;
	private int y;
	private String way;
	
	public snakePart(int x, int y, String way){
		this.x = x;
		this.y = y;
		this.way = way;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public String getWay(){
		return way;
	}
	
	public void setXY(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public void setWay(String way){
		this.way = way;
	}
}
