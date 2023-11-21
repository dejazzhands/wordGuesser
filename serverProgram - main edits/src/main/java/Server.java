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
						info.setGuessedWords(new ArrayList<String>());
						info.setRemainingGuesses(6);
						info.setNumLetters(0);
						info.setCurrentCategory("");
						info.setCorrectLetterGuess(false);
						info.setGameOver(false);
						info.setWin(false);
						ClientThread t = clients.get(i);
						//sending information
						t.out.writeObject(info);
					}
					catch(Exception e) {
						System.out.println("Error in startGame");
					}
				}
			}


			//function that takes in category based on client input and returns a random word selected from the category
			public void getWordandSendBack (wordGuesserInfo info){
				Word apple = new Word("apple");
				Word banana = new Word("banana");
				Word orange = new Word("orange");
		
				ArrayList<Word> fruitsList = new ArrayList<Word>();
				fruitsList.add(apple);
				fruitsList.add(banana);
				fruitsList.add(orange);
		
				Category categoryOne = new Category("fruits", fruitsList);

				Word dog = new Word("dog");
				Word cat = new Word("cat");
				Word bird = new Word("bird");
		
				ArrayList<Word> animalsList = new ArrayList<Word>();
				animalsList.add(dog);
				animalsList.add(cat);
				animalsList.add(bird);
		
				Category categoryTwo = new Category("animals", animalsList);
				
				Word red = new Word("red");
				Word blue = new Word("blue");
				Word green = new Word("green");
		
				ArrayList<Word> colorsList = new ArrayList<Word>();
				colorsList.add(red);
				colorsList.add(blue);
				colorsList.add(green);
		
				Category categoryThree = new Category("colors", colorsList);

				//if category matches info.getCurrentCategory(), then get random word from that category
				if (info.getCurrentCategory().equals("fruits")){
					Word randomWord = categoryOne.getRandomWord();
					info.setNumLetters(randomWord.getNumLetters());
				}
				else if (info.getCurrentCategory().equals("animals")){
					Word randomWord = categoryTwo.getRandomWord();
					info.setNumLetters(randomWord.getNumLetters());
				}
				else if (info.getCurrentCategory().equals("colors")){
					Word randomWord = categoryThree.getRandomWord();
					info.setNumLetters(randomWord.getNumLetters());

				else {
					System.out.println("Error in getWordandSendBack");
				}
				
			}

			//a function that compares user input 
			public void verifyGuessLetter(wordGuesserInfo info){
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
				
				if (count == 1){
					startGame();
				}

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
			}//end of run
		}//end of client thread
}
