import java.io.IOException;
import java.util.*;

public class CommandListenerThread implements Runnable{
	private volatile Connection connection;
    private Command lastCommand;
    private volatile boolean isDisconnected;
    private Thread t;

    private Observable myObservable = new Observable(){
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    };

    public CommandListenerThread() {
        connection = null;
        t = new Thread(this);
        t.start();
    }

    public CommandListenerThread(Connection connection) {
        this.connection = connection;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        System.out.println("Start receiving");
        while (!isDisconnected){
            try {
                lastCommand = connection.receive();
            } catch (IOException | NoSuchElementException e) {
                    lastCommand = new Command(Command.CommandType.DISCONNECT);
            }

            if(lastCommand.toString().equals("DISCONNECT") || lastCommand.toString().equals("REJECT"))
                stopReceiving();

            if (connection.isActual())                     //How kill all threads with this connection???
              myObservable.notifyObservers(lastCommand);
            else
                stopReceiving();
        }
        System.out.println("Stop receiving");
    }

    public void addObserver(Observer observer){
        myObservable.addObserver(observer);
    }

    public void stopReceiving(){
        isDisconnected = true;
    }
}
