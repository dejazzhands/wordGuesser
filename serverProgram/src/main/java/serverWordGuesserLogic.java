import javafx.application.Platform;
import java.io.Serializable;


import java.util.ArrayList;

//wordGuesserGameLogic has word and category

public class serverWordGuesserLogic {
    //global variable wordToGuess to store the random word selected from the category
    Word wordToGuess = new Word("");
    private ArrayList<Character> guessedLetters; // This is to keep track of the letters that the user has guessed, regardless of whether they are correct or not
    private ArrayList<Category> categories;
    private int remainingGuessesPerWord; // This is to keep track of how many guesses the user has left for a word
    private int correctGuesses; // This is to keep track of how many letters the user has guessed correctly
    private int remainingGuessesPerCategory; // This is to keep track of how many times the user has attempted to guess a word in a category
    private int remainingGuessesGeneral;
    private Character letterGuessbyClient;

    public serverWordGuesserLogic() {
        this.guessedLetters = new ArrayList<>();
        this.remainingGuessesPerWord = 6;
        this.correctGuesses = 0;
        this.remainingGuessesPerCategory = 3; 
        this.categories = new ArrayList<>();
    }

    //getters and setters for private variables
    public void setRemainingGuessesPerWord(int remainingGuessesPerWord) {
        this.remainingGuessesPerWord = remainingGuessesPerWord;
    }

    public void setRemainingGuessesPerCategory(int remainingGuessesPerCategory) {
        this.remainingGuessesPerCategory = remainingGuessesPerCategory;
    }

    public void setGuessedLetters(ArrayList<Character> guessedLetters) {
        //add guessed letters obtained through info.letterGuessbyClient to guessedLetters
        this.guessedLetters = guessedLetters;
    }

    public void setCorrectGuesses(int correctGuesses) {
        this.correctGuesses = correctGuesses;
    }

    // Method to set the category after the object is created
    public void setCategory(ArrayList<Category> categories) {
        this.categories = categories;
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
            remainingGuessesPerCategory--;
            resetForNextCategory();
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


    public boolean isGameOver() {
        //the game is over if the user cannot guess a word within three attempts
        if(remainingGuessesGeneral == 0){
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

    public Word getWordToGuess() {
        return wordToGuess;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public int getRemainingGuessesPerCategory() {
        return remainingGuessesPerCategory;
    }

    public int getCorrectGuesses() {
        return correctGuesses;
    }

    public ArrayList<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public void setLetterGuessbyClient(Character letterGuessbyClient) {
        this.letterGuessbyClient = letterGuessbyClient;
    }

    public Character getLetterGuessbyClient() {
        return letterGuessbyClient;
    }


    //function that takes in category based on client input and returns a random word selected from the category
    public wordGuesserInfo getWordandSendBack(wordGuesserInfo info){

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
        if (info.currentCategory.equals("Fruits")){
            //since wordToGuess is a global variable, its value is set to the random word selected from the category, and won't be reset until the next category is chosen
            wordToGuess = categoryOne.getRandomWord();
            
        }
        else if (info.currentCategory.equals("Animals")){
            wordToGuess = categoryTwo.getRandomWord();
        }
        else if (info.currentCategory.equals("Colors")){
            wordToGuess = categoryThree.getRandomWord();
        }

        else {
            System.out.println("Error in getWordandSendBack");
        }

        info.numLetters = wordToGuess.length();

        return info;

    }

    //function that takes a character and checks if its in the word
    public wordGuesserInfo verifyGuessLetter(wordGuesserInfo info, String wordToGuess){
        if(wordToGuess.)
        return info;
    }


    //for the user to win, check if three words are guessed from three different categories
    public wordGuesserInfo verifyWin(wordGuesserInfo info){
        if (info.correctWordGuess == 3){
            info.win = true;
        }
        //insert condition for three different categories by eliminating categories once won.
        return info;
    }


}
