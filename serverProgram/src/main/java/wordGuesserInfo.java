import java.io.Serializable;
import java.util.ArrayList;

public class wordGuesserInfo implements Serializable {

    Integer numLetters;
    Boolean correctLetterGuess;
    Integer correctWordGuess;
    Boolean incorrectLetterGuess;
    Integer remainingLetterGuesses;
    Integer remainingCategoryGuesses;
    String currentCategory; // This is the category that the user has chosen
    ArrayList<String> guessedWords;
    Boolean win;
    Boolean gameOver;
    Boolean categoryChosen; // This is to check if the user has chosen a category
    Boolean guessDone; //This is to check if user guessed a letter, and so it's easy to tell when a new letter is guessed
    Character letterGuessbyClient;
    String msg;
    String restartProtocol;
    String guessProtocol;
    //protocol strings


    public wordGuesserInfo() {
        this.numLetters = 0;
        this.correctLetterGuess = false;
        this.incorrectLetterGuess = false;
        this.remainingLetterGuesses = 6;
        this.remainingCategoryGuesses = 3;
        this.currentCategory = "this is server";
        this.guessedWords = new ArrayList<>();
        this.win = false;
        this.gameOver = false;
        this.categoryChosen = false;
        this.guessDone = false;
        this.letterGuessbyClient = ' ';
        this.msg = "";
        this.restartProtocol = "";
        this.guessProtocol = "";
        
    }

    void guessLetter(Character letter) {
        this.letterGuessbyClient = letter;
    }
    
    
}
