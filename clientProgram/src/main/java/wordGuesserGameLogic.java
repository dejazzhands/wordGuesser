import javafx.application.Platform;
import java.io.Serializable;


import java.util.ArrayList;

//wordGuesserGameLogic has word and category

public class wordGuesserGameLogic {
    private Word wordToGuess;
    private ArrayList<Character> guessedLetters;
    private int remainingGuessesPerWord;
    private int correctGuesses;
    private int categoryAttempts;
    private ArrayList<Category> categories;

    public wordGuesserGameLogic() {
        this.guessedLetters = new ArrayList<>();
        this.remainingGuessesPerWord = 3;
        this.correctGuesses = 0;
        this.categoryAttempts = 0;
        this.categories = new ArrayList<>();
    }

    // Method to set the category after the object is created
    public void setCategory(ArrayList<Category> categories) {
        this.categories = categories;
    }

    // Method to initialize the game with a specific word and category
    public void initializeGame(Word wordToGuess, ArrayList<Category> categories) {
        this.wordToGuess = wordToGuess;
        this.categories = categories;
        // Additional initialization logic if needed
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

    // Method to check if the entire word has been guessed
    public boolean isWordGuessed() {
        for (char letter : wordToGuess.getWord().toCharArray()) {
            if (!guessedLetters.contains(letter)) {
                return false;
            }
        }
        return true;
    }

    // Method to get the current state of the guessed word (showing guessed letters, hiding others)
    public String getCurrentWordState() {
        StringBuilder currentState = new StringBuilder();
        for (char letter : wordToGuess.getWord().toCharArray()) {
            if (guessedLetters.contains(letter)) {
                currentState.append(letter);
            } else {
                currentState.append("_"); // Use any character to represent a hidden letter
            }
        }
        return currentState.toString();
    }

    // Method to check if a letter has already been guessed
    public boolean isLetterGuessed(char letter) {
        return guessedLetters.contains(letter);
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

    public void resetForNextCategory() {
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

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public int getCategoryAttempts() {
        return categoryAttempts;
    }
}
