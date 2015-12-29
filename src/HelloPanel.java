import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

public class HelloPanel extends JPanel {
	
	private JLabel gameButton;
	private JLabel helpButton;
	private JLabel multiplayerButton;
	
	/**
	 * Create the panel.
	 */
	public HelloPanel() {
		setLayout(null);
		
		multiplayerButton = new JLabel("");
		multiplayerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		multiplayerButton.setBounds(69, 373, 239, 76);
		add(multiplayerButton);
		
		helpButton = new JLabel("");
		helpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		helpButton.setBounds(69, 277, 239, 76);
		add(helpButton);
		
		gameButton = new JLabel("");
		gameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		gameButton.setBounds(69, 182, 239, 76);
		add(gameButton);
		
		JLabel back = new JLabel("");
		back.setIcon(new ImageIcon("pics\\menu.png"));
		back.setBounds(0, 0, 756, 480);
		add(back);
		
		React();
	}
	
	public void React(){
		gameButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				myObservable.notifyObservers("open game");
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
		
		helpButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				Hello();
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
		
		multiplayerButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				myObservable.notifyObservers("open multi");
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
    
    private void Hello(){
    	ImageIcon icon = new ImageIcon("pics\\hello.png");
		JOptionPane.showMessageDialog(this,
			    "Press arrow keys to move.\n"+
			    "Eat apples and avoid poisons.\n"+
			    "You win if your length "+Constants.FieldMaxX*Constants.FieldMaxY/2,
			    "Welocome in Snake",
			    JOptionPane.INFORMATION_MESSAGE,
			    icon);
    }
    
    public void addObserver(Observer observer){
        myObservable.addObserver(observer);
    }
}
