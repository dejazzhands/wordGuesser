import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;


public class Server {

	int count = 1;	
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;
	int port;


	serverWordGuesserLogic logic = new serverWordGuesserLogic();
	wordGuesserInfo info = new wordGuesserInfo();

	Server(Consumer<Serializable> call, int portNum){ // Modify this line to accept the port number
	
		callback = call;
		port = portNum;
		server = new TheServer();
		server.start();

	}

	public void closeServer(){
		for(int i = 0; i < clients.size(); i++) {
			ClientThread t = clients.get(i);
			try {
				t.connection.close();
			}
			catch(IOException e) {
				System.out.println("Error in closeServer");
			}
			try {
				server.mySocket.close();

			} catch(IOException e) {
				System.out.println("Error in closeServer");
			}
		}
	}
	
	public class TheServer extends Thread{
		ServerSocket mySocket;
		
		public void run() {
		
			try{ // Modify this line to use the port number
				mySocket = new ServerSocket(port);
		    	System.out.println("Server is waiting for a client!");
				info.msg = "Server is waiting for a client!";
				callback.accept(info);
		  
				while(true) {
			
					ClientThread c = new ClientThread(mySocket.accept(), count);
					info.msg = "client has connected to server: " + "client #" + count;
					//accepting client info
					callback.accept(info);
					clients.add(c);
					c.start();

					count++;
					
				}
				
			}//end of try
			catch(Exception e) {
				info.msg = "Server socket did not launch";
				callback.accept(info);
			}


		}// end of run()

			
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
			try{
				this.out.writeObject(message);
			}
			catch(Exception e){
				System.out.println("Error in updateClients");
			}
		}
		
		public void run(){
				
			try {

				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			} catch(Exception e) {
				System.out.println("Streams not open");
			}
			
			info.msg = ("Connection successful to client: " + count);
			updateClients(info);

			while(true){

				System.out.println("Waiting for client " + count + " input");

				try {

					info = (wordGuesserInfo) in.readObject();
					System.out.println("Client " + count + " sent: " + info.msg);
					callback.accept(info);

					//if the categorychosen is true and the currentcategory is either fruits, animals, or colors, then get the word and send it back
					if (info.categoryChosen == true && (info.getCurrentCategory().equals("Fruits") || info.getCurrentCategory().equals("Animals") || info.getCurrentCategory().equals("Colors"))){
						info = logic.getWordandSendBack(info);
						out.writeObject(info);
						out.reset();
					}
			
				}
				catch(Exception e) {
					info.msg = "OOPS something went wrong with the socket from client " + count + "....closing down!";
					callback.accept(info);
					info.msg = ("Client " + count + " has left the server!");
					count--;
					System.out.println("Count is: " + count);
					updateClients(info);
					clients.remove(this);
					break;
				}

			} //end of while

		}//end of run


	}
		//end of client thread
}
