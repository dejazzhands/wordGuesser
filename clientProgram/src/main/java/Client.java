import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;
import java.io.Serializable;
import java.util.ArrayList;


public class Client extends Thread {

    int portNumber;
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;

    private Consumer<Serializable> callback;

    Client(Consumer<Serializable> call, int port) {
        callback = call;
        portNumber = port;  
    }


    public void run() {
        try {
            socketClient = new Socket("127.0.0.1", portNumber);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        } 
        catch (Exception e) {
            System.out.println("Connection error : Client did not connect to server");
            e.printStackTrace();
        }

        while (true) {
            try {
                    wordGuesserInfo message = new wordGuesserInfo();
                    message = (wordGuesserInfo) in.readObject();

                    callback.accept(message);
                    System.out.println("Client received wordGuesserInfo");

                    //if the number of letters is not 0, that means the client has received updated wordguesserinfo after sending category
                    if (message.numLetters != 0) {
                        System.out.println("Client received updated wordGuesserInfo after sending category");
                        System.out.println("The number of letters is " + message.numLetters);
                        break;
                    }

                    //if the letterGuessbyClient is not null, that means the client has received updated wordguesserinfo after sending letter
                    if (message.letterGuessbyClient != ' ') {
                        System.out.println("Client received updated wordGuesserInfo after sending letter");
                        System.out.println("The letter guessed by client is " + message.letterGuessbyClient);
                        break;
                    }

            } catch (Exception e) {
                System.out.println("Client error : Client did not receive wordGuesserInfo");
                e.printStackTrace();
                break; // Exit the loop when an exception occurs
            }
        }
        
    }

	public void send(wordGuesserInfo data) {
		try {
            out.reset();
			out.writeObject(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}
