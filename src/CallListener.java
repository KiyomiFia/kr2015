import java.io.IOException;
import java.net.*;

public class CallListener {
    private InetSocketAddress localAddress;
    private volatile boolean isBusy;
    private String remoteNick, remoteAddress;
    private ServerSocket serverSocket;

    public CallListener(String localIP){
        this.localAddress = new InetSocketAddress(localIP, Constants.PORT);
    }

    private String receiveRemoteNick(Connection connection) throws IOException{
        Command c = connection.receive();
        return c.toString().substring((Constants.START_NICK_POSITION));
    }

    public Connection getConnection () throws IOException{
        Socket socket = serverSocket.accept();
        remoteAddress = serverSocket.getInetAddress().getCanonicalHostName();
        Connection connection = new Connection(socket);
        remoteNick = receiveRemoteNick(connection);

        return connection;
    }

    public boolean isBusy(){
        return isBusy;
    }

    public void setBusy(boolean isBusy){
        this.isBusy = isBusy;
    }

    public SocketAddress getListenAddress (){
        return localAddress;
    }

    public void setListenAddress(InetSocketAddress listenAddress){
        localAddress = listenAddress;
    }

    public String getRemoteNick(){
        return remoteNick;
    }

    public String getRemoteAddress(){
        return remoteAddress;
    }

    public static void main(String[] args) throws IOException {
        // Maybe this main for testing?
        CallListener c = new CallListener("Lammer");
        c.getConnection();
        System.out.println(c.getRemoteNick());
    }
}
