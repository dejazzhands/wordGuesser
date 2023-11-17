import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server{

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	
	
	Server(Consumer<Serializable> call){
	
		callback = call;
		server = new TheServer();
		server.start();
	}
	
	
	public class TheServer extends Thread{
		
		public void run() {
		
			try(ServerSocket mysocket = new ServerSocket(5555);){
		    System.out.println("Server is waiting for a client!");
		  
			
		    while(true) {
		
				ClientThread c = new ClientThread(mysocket.accept(), count);
				callback.accept("client has connected to server: " + "client #" + count);
				clients.add(c);
				c.start();
				c.startGame();
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
			
			public void updateClients(wordGuesserInfo message) {
				for(int i = 0; i < clients.size(); i++) {
					ClientThread t = clients.get(i);
					try {
					 t.out.writeObject(message);
					}
					catch(Exception e) {
						System.out.println("Error in updateClients");
					}
				}
			}

			//start the game
			public void startGame() {
				for(int i = 0; i < clients.size(); i++) {
					try {
						//start a new game by initializing wordGuesserInfo
						wordGuesserInfo info = new wordGuesserInfo();
						//use default values, just write it out to the client
						clients.get(i).out.writeObject(info);
					}
					catch(Exception e) {
						System.out.println("Error in startGame");
					}
				}
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
				
				updateClients(null);
				while(true){
					try {
						wordGuesserInfo data = (wordGuesserInfo) in.readObject();
						callback.accept("client: " + count + " sent: " + data);
						updateClients(data);
					}
					catch(Exception e) {
						callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
						clients.remove(this);
						break;
					}
				}





				//update a client and then start the game

				// updateClients("new client on server: client #"+count);
				//  while(true) {
				// 	    try {
				// 	    	wordGuesserInfo data = (wordGuesserInfo) in.readObject();
				// 	    	callback.accept("client: " + count + " sent: " + data);
				// 	    	updateClients(data);
				// 	    	}
				// 	    catch(Exception e) {
				// 	    	callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
				// 	    	clients.remove(this);
				// 	    	break;
				// 	    }
				// 	}
				}//end of run

			
			
		}//end of client thread
}


	
	

	
