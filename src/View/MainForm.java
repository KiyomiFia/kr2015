package View;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;

import javax.swing.*;
import java.util.*;
public class MainForm{

	private JFrame frame;
	private Field[][] field = new Field[View.Costants.FieldMaxX][View.Costants.FieldMaxY];
	private JPanel fieldPanel;
	private JLabel scoreText;
	private SnakePart[] snake = new SnakePart[View.Costants.FieldMaxX*View.Costants.FieldMaxY];
	private int snakeLength = 0;
	private Random random = new Random();
	private int appleX;
	private int appleY;
	private int poisonX;
	private int poisonY;
	private int previousTailX;
	private int previousTailY;
	private State state;
	private int level = 0;
	private int winLength = View.Costants.FieldMaxX*View.Costants.FieldMaxY/2;
	private Task t = new Task();
	private Timer timer = new Timer(true);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainForm() {
		state = State.LEFT;
		windowHello();
		initialize();
		
		t.setMainForm(this);
		timer.scheduleAtFixedRate(t, 0, View.Costants.LevelSpeed[level]);
		
		createField();
		createSnake();
		createApple();
		createPoison();
		Action();
	}

	//View.Costants.LevelSpeed[level]; не знаю куда это нужно поставить
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100,
				View.Costants.FieldMaxX*View.Costants.PointFieldSize+6,
				View.Costants.FieldMaxY*View.Costants.PointFieldSize+66);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel StateInfoPanel = new JPanel();
		StateInfoPanel.setBackground(new Color(102, 204, 255));
		frame.getContentPane().add(StateInfoPanel, BorderLayout.NORTH);
		StateInfoPanel.setLayout(new BorderLayout(0, 0));
		
		scoreText = new JLabel("Score: "+snakeLength);
		scoreText.setFont(new Font("Vivaldi", Font.BOLD, 30));
		StateInfoPanel.add(scoreText);
		
		fieldPanel = new JPanel();
		frame.getContentPane().add(fieldPanel, BorderLayout.CENTER);
		fieldPanel.setLayout(null);
	}

	public void updateTimer(){
		timer.cancel();
		t = new Task();
		t.setMainForm(this);
		timer = new Timer(true);
		timer.scheduleAtFixedRate(t, 0, View.Costants.LevelSpeed[level]);
	}
	
	public void createField(){
		for (int i=0;i<View.Costants.FieldMaxX;i++){
			for (int j=0;j<View.Costants.FieldMaxY;j++){
				field[i][j] = new Field(i,j);
				fieldPanel.add(field[i][j]);
			}
		}
	}
	
	public void createSnake(){
		snakeLength = 1;
		snake[0] = new SnakePart(View.Costants.StartHeadX,View.Costants.StartHeadY);
		previousTailX = View.Costants.StartHeadX+1;
		previousTailY = View.Costants.StartHeadY;
		state = State.LEFT;
		field[View.Costants.StartHeadX][View.Costants.StartHeadY].setState("headw");
		level = 0;
		addBody();
		addBody();
	}
	
	public void addBody(){
		snake[snakeLength] = new SnakePart(previousTailX,previousTailY);
		field[snake[snakeLength].getX()][snake[snakeLength].getY()].setState("body");
		snakeLength++;
		scoreText.setText("Score: "+snakeLength);
		if (snakeLength > ((winLength-3)/10*(level+1))) {
			level++;
			updateTimer();
		}
		if (snakeLength == winLength) windowWin();
	}
	
	public void makeMove(String way){
		int x = snake[0].getX();
		int y = snake[0].getY();
		field[snake[snakeLength-1].getX()][snake[snakeLength-1].getY()].setState("grass");
		previousTailX = snake[snakeLength-1].getX();
		previousTailY = snake[snakeLength-1].getY();
		for (int i=snakeLength-1;i>0;i--){
			snake[i].setXY(snake[i-1].getX(), snake[i-1].getY());
			field[snake[i].getX()][snake[i].getY()].setState("body");
		}
		if (state == State.UP) y--;
		if (state == State.DOWN) y++;
		if (state == State.RIGHT) x++;
		if (state == State.LEFT) x--;
		
		if (x == -1) x = View.Costants.FieldMaxX-1;
		if (y == -1) y = View.Costants.FieldMaxY-1;
		if (x == View.Costants.FieldMaxX) x = 0;
		if (y == View.Costants.FieldMaxY) y = 0; 
		
		snake[0].setXY(x,y);
		field[snake[0].getX()][snake[0].getY()].setState("head"+way);
	}
	
	public void createApple(){
		boolean created = false;
		while (!created) {
			appleX = random.nextInt(View.Costants.FieldMaxX);
			appleY = random.nextInt(View.Costants.FieldMaxY);
			if (field[appleX][appleY].getState().equals("grass")) {
				field[appleX][appleY].setState("apple");
				created = true;
				System.out.println(created);
			}
		}
	}
	
	public void createPoison(){
		boolean created = false;
		field[poisonX][poisonY].setState("grass");
		while (!created) {
			poisonX = random.nextInt(View.Costants.FieldMaxX);
			poisonY = random.nextInt(View.Costants.FieldMaxY);
			if (field[poisonX][poisonY].getState().equals("grass")) {
				field[poisonX][poisonY].setState("poison");
				created = true;
			}
		}
	}
	
	public void windowHello(){
		ImageIcon icon = new ImageIcon("pics\\hello.png");
		JOptionPane.showMessageDialog(frame,
			    "Press arrow keys to move.\n"+
			    "Eat apples and avoid poisons.\n"+
			    "You win if your length "+winLength,
			    "Welocome in Snake",
			    JOptionPane.INFORMATION_MESSAGE,
			    icon);
	}
	
	public void windowGameOver(){
		ImageIcon icon = new ImageIcon("pics\\gameover.png");
		JOptionPane.showMessageDialog(frame,
			    "You lose. Try again.",
			    "",
			    JOptionPane.INFORMATION_MESSAGE,
			    icon);
		redrawAll();
	}
	
	public void windowWin(){
		ImageIcon icon = new ImageIcon("pics\\win.png");
		JOptionPane.showMessageDialog(frame,
			    "Congratulations. Let's start again.",
			    "You win",
			    JOptionPane.INFORMATION_MESSAGE,
			    icon);
		redrawAll();
	}
	
	public void redrawAll(){
		for (int i=0;i<View.Costants.FieldMaxX;i++){
			for (int j=0;j<View.Costants.FieldMaxY;j++) 
				field[i][j].setState("grass");
		}	
		createSnake();
		createApple();
		createPoison();
	}
	
	public int getSnakeBodyX(int i){
		return snake[i].getX();
	}
	
	public int getSnakeBodyY(int i){
		return snake[i].getY();
	}

	public boolean isHitInBody(){
		boolean ans = false;
		int x = getSnakeBodyX(0);
		int y = getSnakeBodyY(0);
		
		if (state == State.UP) y--;
		if (state == State.DOWN) y++;
		if (state == State.RIGHT) x++;
		if (state == State.LEFT) x--;
		
		if (x == -1) x = View.Costants.FieldMaxX-1;
		if (y == -1) y = View.Costants.FieldMaxY-1;
		if (x == View.Costants.FieldMaxX) x = 0;
		if (y == View.Costants.FieldMaxY) y = 0; 
		
		for (int i=1;i<snakeLength;i++){
			if ((x == getSnakeBodyX(i)) &&
				(y == getSnakeBodyY(i)))
					ans = true; 
		}
		return ans;
	}
	
	public void Eat(){
		addBody();
		createApple();
		createPoison();
	}
	
	public void makeMoveUp(){
			if (isHitInBody()){
				windowGameOver();
				redrawAll();
			}
			else makeMove("n");
			if ((getSnakeBodyX(0) == appleX) && (getSnakeBodyY(0) == appleY)) Eat();
			if ((getSnakeBodyX(0) == poisonX) && (getSnakeBodyY(0) == poisonY)) windowGameOver();
	}
	
	public void makeMoveRight(){
			if (isHitInBody()){
				windowGameOver();
				redrawAll();
			}
			else makeMove("e");
			if ((getSnakeBodyX(0) == appleX) && (getSnakeBodyY(0) == appleY)) Eat();
			if ((getSnakeBodyX(0) == poisonX) && (getSnakeBodyY(0) == poisonY)) windowGameOver();
	}
	
	public void makeMoveDown(){
			if (isHitInBody()){
				windowGameOver();
				redrawAll();
			}
			else makeMove("s");
			if ((getSnakeBodyX(0) == appleX) && (getSnakeBodyY(0) == appleY)) Eat();
			if ((getSnakeBodyX(0) == poisonX) && (getSnakeBodyY(0) == poisonY)) windowGameOver();
	}
	
	public void makeMoveLeft(){
			if (isHitInBody()){
				windowGameOver();
				redrawAll();
			}
			else makeMove("w");
			if ((getSnakeBodyX(0) == appleX) && (getSnakeBodyY(0) == appleY)) Eat();
			if ((getSnakeBodyX(0) == poisonX) && (getSnakeBodyY(0) == poisonY)) windowGameOver();
	}
	
	public void Action(){
		frame.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					if (!(state == State.LEFT)) 
						state = State.RIGHT;
				if (e.getKeyCode() == KeyEvent.VK_LEFT) 
					if (!(state == State.RIGHT)) 
						state = State.LEFT ;
				if (e.getKeyCode() == KeyEvent.VK_UP) 
					if (!(state == State.DOWN))
						state = State.UP;
				if (e.getKeyCode() == KeyEvent.VK_DOWN) 
					if (!(state == State.UP))
						state = State.DOWN;
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void step(){
		switch (state) {
		case RIGHT:
			makeMoveRight();
			break;
		case LEFT:
			makeMoveLeft();
			break;
		case UP:
			makeMoveUp();
			break;
		case DOWN:
			makeMoveDown();
			break;
		}
	}
	
	public enum State{
		RIGHT, LEFT, UP, DOWN
	}
}
