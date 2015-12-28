import java.io.IOException;
import java.net.*;
import java.util.*;

public class CallListenerThread implements Runnable{
	private CallListener callListener;
    private volatile boolean isClose;
    private Caller.CallStatus statusOfLastConnection;
    private Thread t;
    private volatile boolean isReceive, flag;
    private Connection lastConnection = null;  //for accept and decline when busy

    private Observable myObservable = new Observable(){
        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(arg);
        }
    };

    public CallListenerThread(){
        t = new Thread(this);
        isClose = false;
        t.start();
    }

    public CallListenerThread(CallListener callListener){
        this.callListener = callListener;
        t = new Thread(this);
        isClose = false;
        t.start();
    }

    public SocketAddress getListenAddress(){
        return callListener.getListenAddress();
    }

    public String getLocalNick (){
        return callListener.getLocalNick();
    }

    public SocketAddress getRemoteAddress(){
        return null; //where it take?
    }

    public boolean isBusy (){
        return callListener.isBusy();
    }

    public synchronized void setBusy(boolean isBusy){
        callListener.setBusy(isBusy);
    }

    public void setListenAddress(InetSocketAddress newAddress) {
        callListener.setListenAddress(newAddress);
    }

    public void setLocalNick (String newNick){
        callListener.setLocalNick(newNick);
    }

    public void run() {
        while(!isClose){
            try {
                Connection connection = callListener.getConnection();
                myObservable.notifyObservers(callListener);
                waitAnswer();

                if (!isReceive)
                    sendBusyOrRejected(connection);
                else
                    acceptNewConnectionAndDisconnectPrev(connection);
            }
            catch (IOException e){
                System.out.println("Close");
                statusOfLastConnection = Caller.CallStatus.NOT_ACCESSIBLE;
                e.printStackTrace();
            }
        }
    }

    private void sendBusyOrRejected(Connection connection) throws IOException{
        if (callListener.isBusy()){
            connection.sendNickBusy(getLocalNick());
            statusOfLastConnection = Caller.CallStatus.BUSY;
        }
        else {
            connection.sendNickHello(getLocalNick());
            connection.reject();
            statusOfLastConnection = Caller.CallStatus.REJECTED;
        }
    }

    private void disconnectLastConnection() {
        if (callListener.isBusy())
            try {
                lastConnection.disconnect();
            } catch (IOException | NullPointerException ex) {
                ex.printStackTrace();
            }
    }

    private void acceptNewConnectionAndDisconnectPrev(Connection connection) throws IOException{
        disconnectLastConnection();
        acceptNewConnection(connection);
    }

    private void acceptNewConnection(Connection connection) throws IOException{
        connection.sendNickHello(getLocalNick());
        setBusy(true);

        statusOfLastConnection = Caller.CallStatus.OK;
        connection.accept();

        lastConnection = connection;
        myObservable.notifyObservers(connection);

        runCommandListener(connection);
    }

    private void runCommandListener(Connection connection){
        CommandListenerThread commandListenerThread = new CommandListenerThread(connection);
        commandListenerThread.addObserver(LogicManager.logicForDialog); // Logic in dialog panel
    }

    public void stop(){
        isClose = true;
    }

    public void setReceive(boolean isReceive){
        this.isReceive = isReceive;
    }

    public void suspend(){
        flag = false;
    }

    private synchronized void waitAnswer(){
        if (!flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(t + " Thread interrupted: " + e);
            }
        }
    }

    public synchronized void resume(){
        flag = true;
        notify();
    }

    public void addObserver(Observer observer){
        myObservable.addObserver(observer);
    }

    public String getRemoteNick (){
        return callListener.getRemoteNick();
    }
}
