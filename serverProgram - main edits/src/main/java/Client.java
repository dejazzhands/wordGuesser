import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread {

    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;

    private Consumer<Serializable> callback;
    private Consumer<WordGuesserInfo> wordguessInfo;

    Client(Consumer<WordGuesserInfo> wordguessInfo) {
        
    }

    public void run() {
        try {
            socketClient = new Socket("127.0.0.1", 5555);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        } catch (Exception e) {
            System.out.println("Client error");
        }

        while (true) {
            try {
                WordGuesserInfo message = (WordGuesserInfo) in.readObject();
                wordguessInfo.accept(message);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

	public void send(wordGuesserInfo data) {
		try {
			out.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
