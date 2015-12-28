import java.io.IOException;
import java.net.*;

public class Connection {
	private Socket socket;
    private volatile boolean isActual;

    public Connection(Socket socet) {
        this.socket = socet;
        isActual = true;
    }

    public void sendNickHello(String nick) throws IOException {
        socket.getOutputStream().write((Constants.ChatApp_VERSION + " user " + safe(nick) + "\n").getBytes(Constants.CHARSET_NAME));
        socket.getOutputStream().flush();
    }

    public void sendNickBusy(String nick) throws IOException {
        socket.getOutputStream().write((Constants.ChatApp_VERSION + " user " + safe(nick) + " busy\n").getBytes(Constants.CHARSET_NAME));
        socket.getOutputStream().flush();
    }

    public void accept() throws IOException {
        waitStartAccepting(Constants.WAIT_UNTIL_REMOTE_USER_START_ACCEPTING_IN_MILLIS);

        socket.getOutputStream().write("Accepted\n".getBytes(Constants.CHARSET_NAME));
        socket.getOutputStream().flush();
    }

    public void reject() throws IOException {
        waitStartAccepting(Constants.WAIT_UNTIL_REMOTE_USER_START_ACCEPTING_IN_MILLIS);

        socket.getOutputStream().write("Rejected\n".getBytes(Constants.CHARSET_NAME));
        socket.getOutputStream().flush();
    }

    public void sendMessage(String message) throws IOException {
        socket.getOutputStream().write("Message\n".getBytes(Constants.CHARSET_NAME));
        socket.getOutputStream().write((message + "\n").getBytes(Constants.CHARSET_NAME));
        socket.getOutputStream().flush();
    }

    public void disconnect() throws IOException {
        socket.getOutputStream().write("Disconnect\n".getBytes(Constants.CHARSET_NAME));
        socket.getOutputStream().flush();

        isActual = false;
        socket.close();
    }

    private void waitStartAccepting(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored){}
    }

    public boolean isActual(){
        return isActual;
    }

    public Command receive() throws IOException {
        Scanner in = new Scanner(new BufferedInputStream(socket.getInputStream()));
        String line = in.nextLine();

        if (line.startsWith(Constants.ChatApp_VERSION) && line.contains(" user ")) {       //if NickCommand was received

            return recognizeNickCommand(line);

        } else if (line.toUpperCase().equals(Command.CommandType.MESSAGE.toString())) {    //if MessageCommand was received

            line = in.nextLine();
            return new MessageCommand(line);

        } else if (SIMPLE_COMMAND_HASH_MAP.containsKey(line.toLowerCase())) {              //if simple Command was received

            return SIMPLE_COMMAND_HASH_MAP.get(line.toLowerCase());

        } else return null;                                                                //if rubbish was received
    }

    private NickCommand recognizeNickCommand(String line){
        Boolean isBusy = line.toLowerCase().endsWith(" busy");
        String nick;

                if (isBusy)
            nick = line.substring(line.indexOf(" user ") + 6, line.indexOf(" busy"));
        else
            nick = line.substring(line.indexOf(" user ") + 6, line.length());

        return new NickCommand(Constants.ChatApp_VERSION, safe(nick), isBusy);
    }

    private static String safe(String s){
        return s.replaceAll("['\";]", "").replaceAll("\\s", "");
    }

    static final HashMap<String, Command> SIMPLE_COMMAND_HASH_MAP = new HashMap<String, Command>(){{
        put("accepted", new Command(Command.CommandType.ACCEPT));
        put("disconnect", new Command(Command.CommandType.DISCONNECT));
        put("rejected", new Command(Command.CommandType.REJECT));
    }};
}
