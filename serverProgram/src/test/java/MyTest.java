import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {
	@Test
    public void testGuessLetterCorrect() {
        serverWordGuesserLogic gameLogic = new serverWordGuesserLogic();
        gameLogic.initializeGame(new Word("test"), new ArrayList<>());

        gameLogic.guessLetter('t');

        assertEquals(1, gameLogic.getCorrectGuesses());
        assertFalse(gameLogic.isGameOver());
        assertFalse(gameLogic.isGameWon());
    }

    @Test
    public void testGuessLetterIncorrect() {
        serverWordGuesserLogic gameLogic = new serverWordGuesserLogic();
        gameLogic.initializeGame(new Word("test"), new ArrayList<>());

        gameLogic.guessLetter('x');

        assertEquals(2, gameLogic.getRemainingGuessesPerWord());
        assertFalse(gameLogic.isGameOver());
        assertFalse(gameLogic.isGameWon());
    }

    @Test
    public void testHandleNewCategorySelection() {
        serverWordGuesserLogic gameLogic = new serverWordGuesserLogic();
        gameLogic.initializeGame(new Word("test"), new ArrayList<>());

        // Simulate incorrect guesses
        gameLogic.guessLetter('x');
        gameLogic.guessLetter('y');
        gameLogic.guessLetter('z');

        // Attempt to change category after incorrect guesses
        gameLogic.handleNewCategorySelection();

        // Check if the game is reset for the next category
        assertEquals(6, gameLogic.getRemainingGuessesPerWord());
        assertEquals(0, gameLogic.getCorrectGuesses());
        assertTrue(gameLogic.getGuessedLetters().isEmpty());
    }


    @Test
    public void testLetterGuessVerification() {
        serverWordGuesserLogic gameLogic = new serverWordGuesserLogic();
        Word wordToGuess = new Word("test");

        // Set up the game state
        gameLogic.setWordToGuess(wordToGuess);
        wordGuesserInfo info = new wordGuesserInfo();
        info.setLetterGuessbyClient('t');

        // Verify the letter guess
        gameLogic.verifyGuessLetter(info, wordToGuess);

        // Check if the correct letter guess flag is set in the info object
        assertTrue(info.isCorrectLetterGuess());
    }
}
