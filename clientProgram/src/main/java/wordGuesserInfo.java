import java.io.Serializable;
import java.util.ArrayList;

public class wordGuesserInfo implements Serializable {

    Integer numLetters;
    Boolean correctLetterGuess;
    Integer remainingGuesses;
    String currentCategory; // This is the category that the user has chosen
    ArrayList<String> guessedWords;
    Boolean win;
    Boolean gameOver;
    Boolean categoryChosen; // This is to check if the user has chosen a category
    Character letterGuessbyClient;
    String msg;
    
    // Getter and setter methods for all the fields
    public int getNumLetters() {
        return numLetters;
    }

    public void setNumLetters(int numLetters) {
        this.numLetters = numLetters;
    }

    public void setLetterGuessbyClient(Character letterGuessbyClient) {
        this.letterGuessbyClient = letterGuessbyClient;
    }

    public Character getLetterGuessbyClient() {
        return letterGuessbyClient;
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

    public void setCategoryChosen(boolean categoryChosen){
        this.categoryChosen = categoryChosen;
    }

    public boolean isCategoryChosen(){
        return categoryChosen;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public wordGuesserInfo() {
        this.numLetters = 0;
        this.correctLetterGuess = false;
        this.remainingGuesses = 6;
        this.currentCategory = "this is server";
        this.guessedWords = new ArrayList<>();
        this.win = false;
        this.gameOver = false;
        this.categoryChosen = false;
        this.letterGuessbyClient = ' ';
    }


    void restartGame() {
        this.numLetters = 0;
        this.correctLetterGuess = false;
        this.remainingGuesses = 6;
        this.currentCategory = "this is server";
        this.guessedWords.clear();
        this.win = false;
        this.gameOver = false;
        this.categoryChosen = false;
        this.letterGuessbyClient = ' ';
    }
    
    
}
