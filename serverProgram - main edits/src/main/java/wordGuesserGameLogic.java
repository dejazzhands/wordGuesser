import javafx.application.Platform;
import java.io.Serializable;


import java.util.ArrayList;

public class wordGuesserGameLogic {
    private String name;
    private ArrayList<Category> categories;
    private Word wordToGuess;
    private ArrayList<Character> guessedLetters;
    private int remainingGuessesPerWord;
    private int correctGuesses;
    private int categoryAttempts;

    public wordGuesserGameLogic(String name, ArrayList<Category> categories) {
        this.name = name;
        this.categories = categories;
        this.guessedLetters = new ArrayList<>();
        this.remainingGuessesPerWord = 3;
        this.correctGuesses = 0;
        this.categoryAttempts = 0;
    }

    public void guessLetter(char letter) {
        if (!guessedLetters.contains(letter)) {
            guessedLetters.add(letter);
            if (wordToGuess.isGuessed(letter)) {
                // Correct guess
                correctGuesses++;
            } else {
                // Incorrect guess
                remainingGuessesPerWord--;
            }
        }
    }

    public void handleCategorySelection() {
        if (correctGuesses == 0 && remainingGuessesPerWord > 0) {
            // Client guessed the word within 6 letter guesses
            // They cannot guess another word in the same category but must choose from the two remaining
            resetForNextCategory();
        } else {
            // Client did not guess the word correctly or used all guesses
            // They are free to choose from any of the three categories for another word
            categoryAttempts++;
            if (categoryAttempts < 3) {
                resetForNextCategory();
            }
        }
    }

    public int getRemainingGuessesPerWord() {
        return remainingGuessesPerWord;
    }

    public boolean isGameWon() {
        return correctGuesses == 3;
    }

    public boolean isGameOver() {
        if (remainingGuessesPerWord == 0 || categoryAttempts == 3) {
            // Game over conditions: out of chances of guessing or max category attempts reached
            return true;
        } 
        return false;
    }

    private void resetForNextCategory() {
        guessedLetters.clear();
        remainingGuessesPerWord = 6;
        correctGuesses = 0;
    }


    public void setWordToGuess(Word wordToGuess) {
        this.wordToGuess = wordToGuess;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public String getCategoryName() {
        return name;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public int getCategoryAttempts() {
        return categoryAttempts;
    }
}
