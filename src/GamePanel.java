import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private JLabel scoreText;
	private JLabel exitButton;
	private JPanel fieldPanel;
	private Field[][] field = new Field[Constants.FieldMaxX][Constants.FieldMaxY];
	private Bonus apple = new Bonus();
	private Bonus poison = new Bonus();
	private Snake snake = new Snake(true);
	private TaskGame timer;
	
	/**
	 * Create the panel.
	 */
	public GamePanel() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel StateInfoPanel = new JPanel();
		StateInfoPanel.setBackground(new Color(102, 204, 255));
		add(StateInfoPanel, BorderLayout.NORTH);
		StateInfoPanel.setLayout(new BorderLayout(0, 0));
		
		scoreText = new JLabel("Score: "+2);
		scoreText.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
		StateInfoPanel.add(scoreText, BorderLayout.WEST);
		
		exitButton = new JLabel("Back ");
		exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		exitButton.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
		StateInfoPanel.add(exitButton, BorderLayout.EAST);
		
		fieldPanel = new JPanel();
		add(fieldPanel, BorderLayout.CENTER);
		fieldPanel.setLayout(null);
		
		createField();
		drawSnake();
		createApple();
		createPoison();
		timer = new TaskGame(this,500);
		
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
	
	public void drawSnake(){
		field[snake.getSnakeBodyX(0)][snake.getSnakeBodyY(0)].setState(snake.getSnakeHeadState());
		for (int i=1;i<snake.getSnakeLength();i++){
			field[snake.getSnakeBodyX(i)][snake.getSnakeBodyY(i)].setState(Constants.FieldState.BODY);
		}
	}
	
	public void createApple(){
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
	
	public void Eat(){
		snake.addBody();
		drawSnake();
		createApple();
		createPoison();
		scoreText.setText("Score: "+snake.getSnakeLength());
		if (snake.getSnakeLength() == Constants.FieldMaxX*Constants.FieldMaxY/2) Win();
	}
	
	public void redrawField(){
		for (int i=0;i<Constants.FieldMaxX;i++){
			for (int j=0;j<Constants.FieldMaxY;j++) 
				field[i][j].setState(Constants.FieldState.GRASS);
		}	
		field[apple.getX()][apple.getY()].setState(Constants.FieldState.APPLE);
		field[poison.getX()][poison.getY()].setState(Constants.FieldState.POISON);
	}
	
	public void redrawAll(){
		redrawField(); 
		snake = new Snake(true);
		drawSnake();
		createApple();
		createPoison();
		setTimer(500);
	}
	
	public void makeMove(){
		if (isHitInBody()){
			GameOver();
			redrawAll();
		}
		else snake.makeMove();
		redrawField();
		drawSnake();
		if ((snake.getSnakeBodyX(0) == apple.getX()) && (snake.getSnakeBodyY(0) == apple.getY())) Eat();
		if ((snake.getSnakeBodyX(0) == poison.getX()) && (snake.getSnakeBodyY(0) == poison.getY())) GameOver();
	}

	public void GameOver(){
		if (this.isVisible()){
		ImageIcon icon = new ImageIcon("pics\\gameover.png");
		JOptionPane.showMessageDialog(this,
			    "You lose. Try again.",
			    "",
			    JOptionPane.INFORMATION_MESSAGE,
			    icon);
		}
		redrawAll();
	}
	
	public void Win(){
		if (this.isVisible()){
		ImageIcon icon = new ImageIcon("pics\\win.png");
		JOptionPane.showMessageDialog(this,
			    "Congratulations. Let's start again.",
			    "You win",
			    JOptionPane.INFORMATION_MESSAGE,
			    icon);
		}
		redrawAll();
	}
	
	public boolean isHitInBody(){
		boolean ans = false;
		int x = snake.getSnakeBodyX(0);
		int y = snake.getSnakeBodyY(0);
		
		if (snake.getState() == Constants.State.UP) y--;
		if (snake.getState() == Constants.State.DOWN) y++;
		if (snake.getState() == Constants.State.RIGHT) x++;
		if (snake.getState() == Constants.State.LEFT) x--;
		
		if (x == -1) x = Constants.FieldMaxX-1;
		if (y == -1) y = Constants.FieldMaxY-1;
		if (x == Constants.FieldMaxX) x = 0;
		if (y == Constants.FieldMaxY) y = 0; 
		
		for (int i=1;i<snake.getSnakeLength();i++){
			if ((x == snake.getSnakeBodyX(i)) &&
				(y == snake.getSnakeBodyY(i)))
					ans = true; 
		}
		return ans;
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
	
	public void setState(Constants.State state){
		snake.setState(state);
	}
	
	public Constants.State getState(){
		return snake.getState();
	}
	
	public void React(){
		exitButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				myObservable.notifyObservers("open hello from game");
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
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
