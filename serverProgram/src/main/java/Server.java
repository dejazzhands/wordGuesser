import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class Server {

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	int port; // Add this line to store the port number

	serverWordGuesserLogic logic = new serverWordGuesserLogic();


	Server(Consumer<Serializable> call, int port){ // Modify this line to accept the port number
	
		callback = call;
		this.port = port; // Add this line to store the port number
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(port)){ // Modify this line to use the port number
		    	System.out.println("Server is waiting for a client!");
		  
				while(true) {
			
					ClientThread c = new ClientThread(mysocket.accept(), count);
					callback.accept("client has connected to server: " + "client #" + count);
					clients.add(c);
					c.start();
					count++;
					
				}
			}//end of try
				catch(Exception e) {
					callback.accept("Server socket did not launch");
				}
			}//end of while
		}
	

		class ClientThread extends Thread{
		
			Socket connection;
			int count;
			ObjectInputStream in;
			ObjectOutputStream out;
			
			ClientThread(Socket s, int count){
				this.connection = s;
				this.count = count;	
			}
			

			//fix updateClients for every client number
			public void updateClients(wordGuesserInfo message){
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
						t.out.writeObject(message);
					}
					catch(Exception e) {}
				}
			}

			public void startGame() {

				for(int i = 0; i < clients.size(); i++) {
					try {
						wordGuesserInfo info = new wordGuesserInfo();
						ClientThread t = clients.get(i);
						t.out.writeObject(info);
					}
					catch(Exception e) {
						System.out.println("Error in startGame");
					}
				}

			}

			public wordGuesserInfo handleNewCategoryRequest(wordGuesserInfo clientInfo) {
                // Call getWordandSendBack on the serverWordGuesserLogic instance
                wordGuesserInfo updatedInfo = new wordGuesserInfo(logic.getWordandSendBack(clientInfo));
				return updatedInfo;
            }

			
			public void run(){
					
				try {
					in = new ObjectInputStream(connection.getInputStream());
					out = new ObjectOutputStream(connection.getOutputStream());
					connection.setTcpNoDelay(true);
				}
				catch(Exception e) {
					System.out.println("Streams not open");
				}
				
				if (count >= 1){
					//it gets here
					startGame();
				}


				while(true){
					try {
						wordGuesserInfo data = (wordGuesserInfo) in.readObject();
						callback.accept("client: " + count + " sent: " + data);
						
						// Check if the client sent a new category
						if (!data.getCurrentCategory().isEmpty()) {
							// Handle the modified category as needed
							String modifiedCategory = data.getCurrentCategory();
							callback.accept("Modified category received: " + modifiedCategory);
							data = handleNewCategoryRequest(data);
							// Update other clients with the modified category
							updateClients(data);
						}
				
					}
					catch(Exception e) {
						callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
						clients.remove(this);
						break;
					}
				}
			}//end of run


		}
		//end of client thread
}
