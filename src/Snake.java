

public class Snake {
	private SnakePart[] snake = new SnakePart[Constants.FieldMaxX*Constants.FieldMaxY/2];
	private int snakeLength = 0;
	private int previousTailX;
	private int previousTailY;
	private Constants.State state;
	private boolean standart;
	
	public Snake(boolean standart){
		this.standart = standart;
		if (standart) {
			state = Constants.State.LEFT;
			snakeLength = 1;
			snake[0] = new SnakePart(Constants.FieldMaxX-3,Constants.FieldMaxY-1);
			previousTailX = snake[0].getX()+1;
			previousTailY = snake[0].getY();
		}
		else {
			state = Constants.State.RIGHT;
			snakeLength = 1;
			snake[0] = new SnakePart(2,0);
			previousTailX = snake[0].getX()-1;
			previousTailY = snake[0].getY();
		}
			addBody();
			previousTailX++;	
	}
	
	public void addBody(){
		snake[snakeLength] = new SnakePart(previousTailX,previousTailY);
		snakeLength++;
	}
	
	public void makeMove(){
		int x = snake[0].getX();
		int y = snake[0].getY();
		previousTailX = snake[snakeLength-1].getX();
		previousTailY = snake[snakeLength-1].getY();
		for (int i=snakeLength-1;i>0;i--){
			snake[i].setXY(snake[i-1].getX(), snake[i-1].getY());
		}
		if (state == Constants.State.UP) y--;
		if (state == Constants.State.DOWN) y++;
		if (state == Constants.State.RIGHT) x++;
		if (state == Constants.State.LEFT) x--;
		
		if (x == -1) x = Constants.FieldMaxX-1;
		if (y == -1) y = Constants.FieldMaxY-1;
		if (x == Constants.FieldMaxX) x = 0;
		if (y == Constants.FieldMaxY) y = 0; 
		
		snake[0].setXY(x,y);
	}
	
	public int getSnakeBodyX(int i){
		return snake[i].getX();
	}
	
	public int getSnakeBodyY(int i){
		return snake[i].getY();
	}
	
	public void setState(Constants.State state){
		this.state = state;
	}
	
	public Constants.State getState(){
		return state;
	}
	
	public Constants.FieldState getSnakeHeadState(){
		Constants.FieldState ans = Constants.FieldState.GRASS;
		if (standart){
			if (state == Constants.State.UP) ans = Constants.FieldState.HEADN;
			if (state == Constants.State.DOWN) ans = Constants.FieldState.HEADS;
			if (state == Constants.State.RIGHT) ans = Constants.FieldState.HEADE;
			if (state == Constants.State.LEFT) ans = Constants.FieldState.HEADW;
		}
		else{
			if (state == Constants.State.UP) ans = Constants.FieldState.HEADNGET;
			if (state == Constants.State.DOWN) ans = Constants.FieldState.HEADSGET;
			if (state == Constants.State.RIGHT) ans = Constants.FieldState.HEADEGET;
			if (state == Constants.State.LEFT) ans = Constants.FieldState.HEADWGET;
		}
		return ans;
	}
	
	public int getSnakeLength(){
		return snakeLength;
	}
	
	public boolean isStandart(){
		return standart;
	}
}
