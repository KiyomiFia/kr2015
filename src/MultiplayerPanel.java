import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class MultiplayerPanel extends JPanel {
	
	private JTextField textField;
	private JLabel openButton;
	private JLabel scoreText;
	private JLabel connectButton;
	private JLabel backButton;
	private JPanel fieldPanel;
	private Field[][] field = new Field[Constants.FieldMaxX][Constants.FieldMaxY];
	private Snake mySnake = new Snake(true);
	private Snake socketSnake = new Snake(false);
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
		
		openButton = new JLabel("Open for connection");
		openButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		openButton.setHorizontalAlignment(SwingConstants.CENTER);
		openButton.setHorizontalTextPosition(SwingConstants.CENTER);
		openButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		StateInfoPanel.add(openButton, BorderLayout.WEST);
		
		scoreText = new JLabel("You   2:2  Friend");
		scoreText.setHorizontalTextPosition(SwingConstants.CENTER);
		scoreText.setHorizontalAlignment(SwingConstants.CENTER);
		scoreText.setFont(new Font("Comic Sans MS", Font.PLAIN, 26));
		StateInfoPanel.add(scoreText, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		StateInfoPanel.add(panel, BorderLayout.EAST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		textField = new JTextField();
		textField.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 0;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		connectButton = new JLabel("Connect   ");
		connectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		connectButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		GridBagConstraints gbc_connectButton = new GridBagConstraints();
		gbc_connectButton.insets = new Insets(0, 0, 5, 0);
		gbc_connectButton.gridx = 1;
		gbc_connectButton.gridy = 0;
		panel.add(connectButton, gbc_connectButton);
		
		backButton = new JLabel("Back   ");
		backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		backButton.setFont(new Font("Comic Sans MS", Font.PLAIN, 15));
		GridBagConstraints gbc_backButton = new GridBagConstraints();
		gbc_backButton.gridx = 1;
		gbc_backButton.gridy = 1;
		panel.add(backButton, gbc_backButton);
		
		fieldPanel = new JPanel();
		add(fieldPanel, BorderLayout.CENTER);
		fieldPanel.setLayout(null);
		
		createField();
		//timer = new TaskMultiplayer(this,500);
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
		if (snake.getSnakeLength() == Constants.FieldMaxX*Constants.FieldMaxY/4) Win();
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
	
	public boolean isMyHitInBody(){
		boolean ans = false;
		int x = mySnake.getSnakeBodyX(0);
		int y = mySnake.getSnakeBodyY(0);
		
		if (mySnake.getState() == Constants.State.UP) y--;
		if (mySnake.getState() == Constants.State.DOWN) y++;
		if (mySnake.getState() == Constants.State.RIGHT) x++;
		if (mySnake.getState() == Constants.State.LEFT) x--;
		
		if (x == -1) x = Constants.FieldMaxX-1;
		if (y == -1) y = Constants.FieldMaxY-1;
		if (x == Constants.FieldMaxX) x = 0;
		if (y == Constants.FieldMaxY) y = 0; 
		
		for (int i=1;i<mySnake.getSnakeLength();i++){
			if ((x == mySnake.getSnakeBodyX(i)) &&
				(y == mySnake.getSnakeBodyY(i)))
					ans = true; 
		}
		
		for (int i=0;i<socketSnake.getSnakeLength();i++){
			if ((x == socketSnake.getSnakeBodyX(i)) &&
				(y == socketSnake.getSnakeBodyY(i)))
					ans = true; 
		}
		
		return ans;
	}
	
	public boolean isSocketHitInBody(){
		boolean ans = false;
		int x = socketSnake.getSnakeBodyX(0);
		int y = socketSnake.getSnakeBodyY(0);
		
		if (socketSnake.getState() == Constants.State.UP) y--;
		if (socketSnake.getState() == Constants.State.DOWN) y++;
		if (socketSnake.getState() == Constants.State.RIGHT) x++;
		if (socketSnake.getState() == Constants.State.LEFT) x--;
		
		if (x == -1) x = Constants.FieldMaxX-1;
		if (y == -1) y = Constants.FieldMaxY-1;
		if (x == Constants.FieldMaxX) x = 0;
		if (y == Constants.FieldMaxY) y = 0; 
		
		for (int i=0;i<mySnake.getSnakeLength();i++){
			if ((x == mySnake.getSnakeBodyX(i)) &&
				(y == mySnake.getSnakeBodyY(i)))
					ans = true; 
		}
		
		for (int i=1;i<socketSnake.getSnakeLength();i++){
			if ((x == socketSnake.getSnakeBodyX(i)) &&
				(y == socketSnake.getSnakeBodyY(i)))
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
		mySnake = new Snake(true);
		socketSnake = new Snake(false);
		scoreText.setText("You   "+mySnake.getSnakeLength()+":"+socketSnake.getSnakeLength()+"  Friend");
		redrawField();
		drawSnake(mySnake);
		drawSnake(socketSnake);
		createApple(apple1);
		createApple(apple2);
		createPoison();
		setTimer(500);
	}
	
	public void makeMyMove(){
		if (isMyHitInBody()){
			GameOver();
			redrawAll();
		}
		else mySnake.makeMove();
		redrawField();
		drawSnake(mySnake);
		drawSnake(socketSnake);
		if ((mySnake.getSnakeBodyX(0) == apple1.getX()) && (mySnake.getSnakeBodyY(0) == apple1.getY())) {
			Eat(mySnake,apple1);
			scoreText.setText("You   "+mySnake.getSnakeLength()+":"+socketSnake.getSnakeLength()+"  Friend");
		}
		if ((mySnake.getSnakeBodyX(0) == apple2.getX()) && (mySnake.getSnakeBodyY(0) == apple2.getY())) {
			Eat(mySnake,apple2);
			scoreText.setText("You   "+mySnake.getSnakeLength()+":"+socketSnake.getSnakeLength()+"  Friend");
		}
		if ((mySnake.getSnakeBodyX(0) == poison.getX()) && (mySnake.getSnakeBodyY(0) == poison.getY())) GameOver();
	}
	
	public void makeSocketMove(){
		if (isSocketHitInBody()){
			Win();
			redrawAll();
		}
		else socketSnake.makeMove();
		redrawField();
		drawSnake(mySnake);
		drawSnake(socketSnake);
		if ((socketSnake.getSnakeBodyX(0) == apple1.getX()) && (socketSnake.getSnakeBodyY(0) == apple1.getY())) {
			Eat(socketSnake,apple1);
			scoreText.setText("You   "+mySnake.getSnakeLength()+":"+socketSnake.getSnakeLength()+"  Friend");
		}
		if ((socketSnake.getSnakeBodyX(0) == apple2.getX()) && (socketSnake.getSnakeBodyY(0) == apple2.getY())) {
			Eat(socketSnake,apple2);
			scoreText.setText("You   "+mySnake.getSnakeLength()+":"+socketSnake.getSnakeLength()+"  Friend");
		}
		if ((socketSnake.getSnakeBodyX(0) == poison.getX()) && (socketSnake.getSnakeBodyY(0) == poison.getY())) Win();
	}
	
	public void step(){
		makeMyMove();
		makeSocketMove();
	}
	
	public void setTimer(long time){
		timer.setTimeBetweenSteps(time);
	}
	
	public void stop(){
		timer.setTimeBetweenSteps(0);
	}
	
	public Constants.State getMyState(){
		return mySnake.getState();
	}
	
	public void setMyState(Constants.State state){
		mySnake.setState(state);
	}
	
	public Constants.State getSocketState(){
		return socketSnake.getState();
	}
	
	public void setSocketState(Constants.State state){
		socketSnake.setState(state);
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
	
	public void successConnectionNotification(String connectIP){
        JOptionPane.showMessageDialog(this, "User " + connectIP + " accept your game");
    }
	
	public void notEnoughParametersNotification(){
        JOptionPane.showMessageDialog(this, "Not enough actual parameters");
    }

    public void remoteUserIsBusyNotification(String remoteIP){
        JOptionPane.showMessageDialog(this, "User " + remoteIP + " is busy");
    }

    public void remoteUserIsRejectedYourCallNotification(String remoteIP){
        JOptionPane.showMessageDialog(this, "User " + remoteIP + " has declined your call.");
    }

    public void remoteUserIsNotAccessibleNotification(){
        JOptionPane.showMessageDialog(this, "User is not accessible");
    }

    public boolean acceptOrRejectMessage(String remoteIP){
        Object[] options = {"Receive","Reject"};

        int dialogResult = JOptionPane.showOptionDialog(this, "User with ip " + remoteIP +
                        " is trying to connect with you", "Receive connection",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return dialogResult == JOptionPane.YES_OPTION;
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
