import javafx.application.Platform;
import javafx.beans.binding.When;

import java.util.ArrayList;


public class wordGuesserGameLogic {
    //this class contains all logic that were placed inside Client.java in the wireframe
    private String name;
    private ArrayList<Category> categories;
    private Word wordToGuess;
    private ArrayList<Character> guessedLetters;
    private int remainingGuesses;
    private int correctGuesses;
    private int categoryAttempts;

    public boolean guessLetter(char letter){
        //check if letter is in word
        //if it is, add it to guessedLetters
        //if it isn't, decrement remainingGuesses

        if(wordToGuess.isGuessed(letter)){
            guessedLetters.add(letter);
            correctGuesses++;
            return true;
        }
        else{
            remainingGuesses--;
            return false;
        }
    }

    //getRemainingGuesses method to check remaining guesses after a guess
    public int getRemainingGuesses(){
        return remainingGuesses;
    }

    

    // If the client guesses the word within 6 letter guesses, they can not guess at another
    // word in the same category but must chose from the two remaining. If they do not guess
    // the word correctly, they are free to choose from any of the three categories for another
    // word. Clients may guess at a maximum of three words per category. If they do not make
    // a correct guess within three attempts, the game is over. The game is won when the
    // client successfully guesses one word in each category. When the game is over, the
    // client can either play again or quit. 


    
}
