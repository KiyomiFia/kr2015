import java.util.Random;

public class Bonus {
	private int x = 0;
	private int y = 0;
	private Constants.FieldState type;
	private Random random = new Random();
	
	public void create(){
		x = random.nextInt(Constants.FieldMaxX);
		y = random.nextInt(Constants.FieldMaxY);
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public Constants.FieldState getType(){
		return type;
	}
	
	public void setType(Constants.FieldState type){	
		this.type = type;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
}
