import java.io.Serializable;
import java.util.ArrayList;

public class wordGuesserInfo implements Serializable {
    // Number of letters for the current word being guessed
    private int numLetters;
    
    // Indicates whether the guessed letter is correct
    private boolean correctLetterGuess;
    
    // Number of guesses remaining
    private int remainingGuesses;
    
    // Category of the current word being guessed
    private String currentCategory;
    
    // List of words that have been guessed to avoid repetition
    private ArrayList<String> guessedWords;
    
    // Indicates whether the client has won the game
    private boolean win;
    
    // Indicates whether the game is over
    private boolean gameOver;
    
    // Getter and setter methods for all the fields
    
    public int getNumLetters() {
        return numLetters;
    }

    public void setNumLetters(int numLetters) {
        this.numLetters = numLetters;
    }

    public boolean isCorrectLetterGuess() {
        return correctLetterGuess;
    }

    public void setCorrectLetterGuess(boolean correctLetterGuess) {
        this.correctLetterGuess = correctLetterGuess;
    }

    public int getRemainingGuesses() {
        return remainingGuesses;
    }

    public void setRemainingGuesses(int remainingGuesses) {
        this.remainingGuesses = remainingGuesses;
    }

    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public ArrayList<String> getGuessedWords() {
        return guessedWords;
    }

    public void setGuessedWords(ArrayList<String> guessedWords) {
        this.guessedWords = guessedWords;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
