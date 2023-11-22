import javafx.application.Platform;
import java.io.Serializable;


import java.util.ArrayList;

//wordGuesserGameLogic has word and category

public class serverWordGuesserLogic {
    private Word wordToGuess;
    private ArrayList<Character> guessedLetters;
    private int remainingGuessesPerWord;
    private int correctGuesses;
    private int categoryAttempts;
    private ArrayList<Category> categories;
    private Character letterGuessbyClient;

    public serverWordGuesserLogic() {
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

    public void handleNewCategorySelection() {
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

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public int getCategoryAttempts() {
        return categoryAttempts;
    }

    public int getCorrectGuesses() {
        return correctGuesses;
    }

    public ArrayList<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public Word getWordToGuess() {
        return wordToGuess;
    }

    public void setLetterGuessbyClient(Character letterGuessbyClient) {
        this.letterGuessbyClient = letterGuessbyClient;
    }

    public Character getLetterGuessbyClient() {
        return letterGuessbyClient;
    }


    //function that takes in category based on client input and returns a random word selected from the category
    public wordGuesserInfo getWordandSendBack (wordGuesserInfo info){
        Word apple = new Word("apple");
        Word banana = new Word("banana");
        Word orange = new Word("orange");

        ArrayList<Word> fruitsList = new ArrayList<Word>();
        fruitsList.add(apple);
        fruitsList.add(banana);
        fruitsList.add(orange);

        Category categoryOne = new Category("Fruits", fruitsList);

        Word dog = new Word("dog");
        Word cat = new Word("cat");
        Word bird = new Word("bird");

        ArrayList<Word> animalsList = new ArrayList<Word>();
        animalsList.add(dog);
        animalsList.add(cat);
        animalsList.add(bird);

        Category categoryTwo = new Category("Animals", animalsList);
        
        Word red = new Word("red");
        Word blue = new Word("blue");
        Word green = new Word("green");

        ArrayList<Word> colorsList = new ArrayList<Word>();
        colorsList.add(red);
        colorsList.add(blue);
        colorsList.add(green);

        Category categoryThree = new Category("Colors", colorsList);

        //if category matches info.getCurrentCategory(), then get random word from that category
        if (info.getCurrentCategory().equals("Fruits")){
            Word randomWord = categoryOne.getRandomWord();
            info.setNumLetters(randomWord.length());
        }
        else if (info.getCurrentCategory().equals("Animals")){
            Word randomWord = categoryTwo.getRandomWord();
            info.setNumLetters(randomWord.length());
        }
        else if (info.getCurrentCategory().equals("Colors")){
            Word randomWord = categoryThree.getRandomWord();
            info.setNumLetters(randomWord.length());
        }
        else {
            System.out.println("Error in getWordandSendBack");
        }

        return info;
        
    }

    //a function that compares user input 
    public wordGuesserInfo verifyGuessLetter(wordGuesserInfo info, Word wordToGuess){
        //if the letter is in the word
        if (wordToGuess.getWord().contains(info.getLetterGuessbyClient().toString())){
            info.setCorrectLetterGuess(true);
        }
        else {
            info.setCorrectLetterGuess(false);
        }
        return info;
    }
}
