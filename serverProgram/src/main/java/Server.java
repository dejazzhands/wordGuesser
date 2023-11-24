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
	Word wordGuess = new Word("");
	serverWordGuesserLogic logic;
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

				System.out.println("Game is running");

				try {

					info = (wordGuesserInfo) in.readObject();
					logic = new serverWordGuesserLogic();
					System.out.println("Client " + count + " sent: " + info.msg);
					callback.accept(info);

					// REMINDER: use if statements related to the states of variables in wordGuesserInfo to determine what to do next

					//if the categorychosen is true and the currentcategory is either fruits, animals, or colors, then get the word and send it back
					if (info.categoryChosen == true && (info.currentCategory.equals("Fruits") || info.currentCategory.equals("Animals") || info.currentCategory.equals("Colors"))){
						
						// Generate the word only if it hasn't been generated yet, then apply getWordandSendBack to get the word and send back information to client.
						if (wordGuess.getWord().isEmpty()) {
							info = logic.getWordandSendBack(info);
						}
						out.writeObject(info);
						out.reset();
					}

					//add logic for new guesses here
					if (info.letterGuessbyClient != ' '){
						//print wordToGuess just to be sure it is initialized
						System.out.println("The word to guess is " + wordGuess.getWord());
						//compare the letter guessed by the client to the wordToGuess
						info = logic.verifyGuessLetter(info, wordGuess.getWord());
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
