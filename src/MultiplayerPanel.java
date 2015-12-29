import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class MultiplayerPanel extends JPanel {
	
	private JLabel scoreText;
	private JLabel backButton;
	private JPanel fieldPanel;
	private Field[][] field = new Field[Constants.FieldMaxX][Constants.FieldMaxY];
	private Snake greenSnake = new Snake(true);
	private Snake violetSnake = new Snake(false);
	private Bonus apple1 = new Bonus();
	private Bonus apple2 = new Bonus();
	private Bonus poison = new Bonus();
	private TaskMultiplayer timer;
	
	/**
	 * Create the panel.
	 */
	public MultiplayerPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel StateInfoPanel = new JPanel();
		StateInfoPanel.setBackground(new Color(102, 204, 255));
		add(StateInfoPanel, BorderLayout.NORTH);
		StateInfoPanel.setLayout(new BorderLayout(0, 0));
		
		scoreText = new JLabel("Green  2:2  Violet");
		scoreText.setHorizontalTextPosition(SwingConstants.CENTER);
		scoreText.setHorizontalAlignment(SwingConstants.CENTER);
		scoreText.setFont(new Font("Comic Sans MS", Font.PLAIN, 36));
		StateInfoPanel.add(scoreText, BorderLayout.CENTER);
		
		backButton = new JLabel("Back   ");
		backButton.setHorizontalAlignment(SwingConstants.CENTER);
		StateInfoPanel.add(backButton, BorderLayout.EAST);
		backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		backButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 36));
		
		fieldPanel = new JPanel();
		add(fieldPanel, BorderLayout.CENTER);
		fieldPanel.setLayout(null);
		
		createField();
		timer = new TaskMultiplayer(this,500);
		React();
	}

	public void createField(){
		for (int i=0;i<Constants.FieldMaxX;i++){
			for (int j=0;j<Constants.FieldMaxY;j++){
				field[i][j] = new Field(i,j);
				fieldPanel.add(field[i][j]);
			}
		}
	}
	
	public void drawSnake(Snake snake){
		field[snake.getSnakeBodyX(0)][snake.getSnakeBodyY(0)].setState(snake.getSnakeHeadState());
		if (snake.isStandart()){
			for (int i=1;i<snake.getSnakeLength();i++){
				field[snake.getSnakeBodyX(i)][snake.getSnakeBodyY(i)].setState(Constants.FieldState.BODY);
			}
		}
		else{
			for (int i=1;i<snake.getSnakeLength();i++){
				field[snake.getSnakeBodyX(i)][snake.getSnakeBodyY(i)].setState(Constants.FieldState.BODYGET);
			}
		}
	}
	
	public void createApple(Bonus apple){
		apple.setType(Constants.FieldState.APPLE);
		boolean created = false;
		
		while (!created) {
			apple.create();
			if (field[apple.getX()][apple.getY()].getState() == Constants.FieldState.GRASS) {
				field[apple.getX()][apple.getY()].setState(apple.getType());
				created = true;
			}
		}
	}
	
	public void createPoison(){
		poison.setType(Constants.FieldState.POISON);
		boolean created = false;
		field[poison.getX()][poison.getY()].setState(Constants.FieldState.GRASS);
		
		while (!created) {
			poison.create();
			if (field[poison.getX()][poison.getY()].getState() == Constants.FieldState.GRASS) {
				field[poison.getX()][poison.getY()].setState(poison.getType());
				created = true;
			}
		}
	}
	
	public void Eat(Snake snake,Bonus apple){
		snake.addBody();
		drawSnake(snake);
		createApple(apple);
		createPoison();
		if (snake.getSnakeLength() == Constants.FieldMaxX*Constants.FieldMaxY/4) {
			if (snake.isStandart()) Win("Green");
			else Win("Violet");
			}
	}
	
	public void Win(String name){
		if (this.isVisible()){
			ImageIcon icon = new ImageIcon("pics\\win.png");
			JOptionPane.showMessageDialog(this,
			    	"Congratulations. "+name+" win. Let's start again.",
			    	"You win",
			    	JOptionPane.INFORMATION_MESSAGE,
			    	icon);
		}
			redrawAll();
	}
	
	public boolean isGreenHitInBody(){
		boolean ans = false;
		int x = greenSnake.getSnakeBodyX(0);
		int y = greenSnake.getSnakeBodyY(0);
		
		if (greenSnake.getState() == Constants.State.UP) y--;
		if (greenSnake.getState() == Constants.State.DOWN) y++;
		if (greenSnake.getState() == Constants.State.RIGHT) x++;
		if (greenSnake.getState() == Constants.State.LEFT) x--;
		
		if (x == -1) x = Constants.FieldMaxX-1;
		if (y == -1) y = Constants.FieldMaxY-1;
		if (x == Constants.FieldMaxX) x = 0;
		if (y == Constants.FieldMaxY) y = 0; 
		
		for (int i=1;i<greenSnake.getSnakeLength();i++){
			if ((x == greenSnake.getSnakeBodyX(i)) &&
				(y == greenSnake.getSnakeBodyY(i)))
					ans = true; 
		}
		
		for (int i=0;i<violetSnake.getSnakeLength();i++){
			if ((x == violetSnake.getSnakeBodyX(i)) &&
				(y == violetSnake.getSnakeBodyY(i)))
					ans = true; 
		}
		
		return ans;
	}
	
	public boolean isVioletHitInBody(){
		boolean ans = false;
		int x = violetSnake.getSnakeBodyX(0);
		int y = violetSnake.getSnakeBodyY(0);
		
		if (violetSnake.getState() == Constants.State.UP) y--;
		if (violetSnake.getState() == Constants.State.DOWN) y++;
		if (violetSnake.getState() == Constants.State.RIGHT) x++;
		if (violetSnake.getState() == Constants.State.LEFT) x--;
		
		if (x == -1) x = Constants.FieldMaxX-1;
		if (y == -1) y = Constants.FieldMaxY-1;
		if (x == Constants.FieldMaxX) x = 0;
		if (y == Constants.FieldMaxY) y = 0; 
		
		for (int i=0;i<greenSnake.getSnakeLength();i++){
			if ((x == greenSnake.getSnakeBodyX(i)) &&
				(y == greenSnake.getSnakeBodyY(i)))
					ans = true; 
		}
		
		for (int i=1;i<violetSnake.getSnakeLength();i++){
			if ((x == violetSnake.getSnakeBodyX(i)) &&
				(y == violetSnake.getSnakeBodyY(i)))
					ans = true; 
		}
		
		return ans;
	}
	
	public void redrawField(){
		for (int i=0;i<Constants.FieldMaxX;i++){
			for (int j=0;j<Constants.FieldMaxY;j++) 
				field[i][j].setState(Constants.FieldState.GRASS);
		}	
		field[apple1.getX()][apple1.getY()].setState(Constants.FieldState.APPLE);
		field[apple2.getX()][apple2.getY()].setState(Constants.FieldState.APPLE);
		field[poison.getX()][poison.getY()].setState(Constants.FieldState.POISON);
	}
	
	public void redrawAll(){
		greenSnake = new Snake(true);
		violetSnake = new Snake(false);
		scoreText.setText("Green  "+greenSnake.getSnakeLength()+":"+violetSnake.getSnakeLength()+"  Violet");
		redrawField();
		drawSnake(greenSnake);
		drawSnake(violetSnake);
		createApple(apple1);
		createApple(apple2);
		createPoison();
		setTimer(500);
	}
	
	public void makeGreenMove(){
		if (isGreenHitInBody()){
			Win("Violet");
			redrawAll();
		}
		else greenSnake.makeMove();
		redrawField();
		drawSnake(greenSnake);
		drawSnake(violetSnake);
		if ((greenSnake.getSnakeBodyX(0) == apple1.getX()) && (greenSnake.getSnakeBodyY(0) == apple1.getY())) {
			Eat(greenSnake,apple1);
			scoreText.setText("Green  "+greenSnake.getSnakeLength()+":"+violetSnake.getSnakeLength()+"  Violet");
		}
		if ((greenSnake.getSnakeBodyX(0) == apple2.getX()) && (greenSnake.getSnakeBodyY(0) == apple2.getY())) {
			Eat(greenSnake,apple2);
			scoreText.setText("Green  "+greenSnake.getSnakeLength()+":"+violetSnake.getSnakeLength()+"  Violet");
		}
		if ((greenSnake.getSnakeBodyX(0) == poison.getX()) && (greenSnake.getSnakeBodyY(0) == poison.getY())) Win("Violet");
	}
	
	public void makeVioletMove(){
		if (isVioletHitInBody()){
			Win("Green");
			redrawAll();
		}
		else violetSnake.makeMove();
		redrawField();
		drawSnake(greenSnake);
		drawSnake(violetSnake);
		if ((violetSnake.getSnakeBodyX(0) == apple1.getX()) && (violetSnake.getSnakeBodyY(0) == apple1.getY())) {
			Eat(violetSnake,apple1);
			scoreText.setText("Green  "+greenSnake.getSnakeLength()+":"+violetSnake.getSnakeLength()+"  Violet");
		}
		if ((violetSnake.getSnakeBodyX(0) == apple2.getX()) && (violetSnake.getSnakeBodyY(0) == apple2.getY())) {
			Eat(violetSnake,apple2);
			scoreText.setText("Green  "+greenSnake.getSnakeLength()+":"+violetSnake.getSnakeLength()+"  Violet");
		}
		if ((violetSnake.getSnakeBodyX(0) == poison.getX()) && (violetSnake.getSnakeBodyY(0) == poison.getY())) Win("Green");
	}
	
	public void step(){
		makeGreenMove();
		makeVioletMove();
	}
	
	public void setTimer(long time){
		timer.setTimeBetweenSteps(time);
	}
	
	public void go(){
		timer.setIsPause(false);
	}
	
	public void stop(){
		timer.setIsPause(true);
	}
	
	public Constants.State getGreenState(){
		return greenSnake.getState();
	}
	
	public void setGreenState(Constants.State state){
		greenSnake.setState(state);
	}
	
	public Constants.State getVioletState(){
		return violetSnake.getState();
	}
	
	public void setVioletState(Constants.State state){
		violetSnake.setState(state);
	}
	
	public void setApple1(int x,int y){
		apple1.setX(x);
		apple1.setY(y);
	}
	
	public void setApple2(int x,int y){
		apple2.setX(x);
		apple2.setY(y);
	}
	
	public void setPoison(int x,int y){
		poison.setX(x);
		poison.setY(y);
	}
	
	public void React(){
		backButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				myObservable.notifyObservers("open hello from multi");
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
	}
	
	private Observable myObservable = new Observable(){
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    };
    
    public void addObserver(Observer observer){
        myObservable.addObserver(observer);
    }
}
