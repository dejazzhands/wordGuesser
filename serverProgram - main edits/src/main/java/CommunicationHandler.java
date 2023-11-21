import java.util.ArrayList;
import java.util.List;



//CommunicationHandler is a class that contains functions that will be used to update the client.
//The functions will be called in the server program for each client that is connected to the server.

public class CommunicationHandler {

	public void communicationHandler(){
		
		
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
				}
				else {
					System.out.println("Error in getWordandSendBack");
				}
				
			}

			//a function that compares user input 
			public void verifyGuessLetter(wordGuesserInfo info, Word wordToGuess){
				//if the letter is in the word
				if (wordToGuess.getWord().contains(info.getLetterGuessbyClient().toString())){
					info.setCorrectLetterGuess(true);
				}
				else {
					info.setCorrectLetterGuess(false);
				}
			}
}
