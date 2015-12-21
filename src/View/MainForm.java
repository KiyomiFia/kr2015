package View;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Random;

import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;

public class MainForm {

	private JFrame frame;
	private Point[][] field = new Point[10][8];
	private JPanel fieldPanel;
	private JLabel scoreText;
	private int heady = 0;
	private int headx = 0;
	private snakePart[] snake = new snakePart[80];
	private int snakeLength = 0;
	private Random random = new Random();
	

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
		initialize();
		createField();
		createSnake();
		createApple();
		createApple();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 506, 466);
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
	
	private void createField(){
		for (int i=0;i<10;i++){
			for (int j=0;j<8;j++){
				field[i][j] = new Point(i,j);
				fieldPanel.add(field[i][j]);
			}
		}
	}
	
	private void createSnake(){
		headx = random.nextInt(10);
		heady = random.nextInt(8);
		snakeLength = 1;
		snake[0] = new snakePart(headx,heady,"w");
		field[headx][heady].setState("headw");
		addSnakeBody();
		addSnakeBody();
	}
	
	public void addSnakeBody(){
		int x=0;
		int y=0;
		if (snake[snakeLength-1].getWay().equals("n")) y++;
		if (snake[snakeLength-1].getWay().equals("s")) y--;
		if (snake[snakeLength-1].getWay().equals("e")) x--;
		if (snake[snakeLength-1].getWay().equals("w")) x++;
		snake[snakeLength] = new snakePart(snake[snakeLength-1].getX()+x,snake[snakeLength-1].getY()+y,snake[snakeLength-1].getWay());
		field[snake[snakeLength].getX()][snake[snakeLength].getY()].setState("body");
		snakeLength++;
		scoreText.setText("Score: "+snakeLength);
	}
	
	public void makeMove(String way){
		field[snake[snakeLength-1].getX()][snake[snakeLength-1].getY()].setState("grass");
		for (int i=snakeLength-1;i>0;i--){
			snake[i].setXY(snake[i-1].getX(), snake[i-1].getY());
			snake[i].setWay(snake[i-1].getWay());
			field[snake[i].getX()][snake[i].getY()].setState("body");
		}
		if (way.equals("n")) heady--;
		if (way.equals("s")) heady++;
		if (way.equals("e")) headx++;
		if (way.equals("w")) headx--;
		snake[0].setXY(headx, heady);
		snake[0].setWay(way);
		field[headx][heady].setState("head"+way);
	}
	
	public void createApple(){
		int x = random.nextInt(10);
		int y = random.nextInt(8);
		if (field[y][x].getState().equals("grass")) field[y][x].setState("apple");
	}
	
	private void windowStart(){
		
	}
	
	public void windowGameOver(){
		
	}
}
