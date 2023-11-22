import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {
	@Test
    public void testGetName() {
        Category category = new Category("TestCategory", new ArrayList<>());
        assertEquals("TestCategory", category.getName());
    }

    @Test
    public void testGetWords() {
        Word apple = new Word("apple");
        Word banana = new Word("banana");
        ArrayList<Word> wordsList = new ArrayList<>();
        wordsList.add(apple);
        wordsList.add(banana);

        Category category = new Category("Fruits", wordsList);
        assertEquals(wordsList, category.getWords());
    }

    @Test
    public void testGetRandomWord() {
        Word apple = new Word("apple");
        Word banana = new Word("banana");
        ArrayList<Word> wordsList = new ArrayList<>();
        wordsList.add(apple);
        wordsList.add(banana);

        Category category = new Category("Fruits", wordsList);

        // Run multiple times to ensure randomness
        for (int i = 0; i < 10; i++) {
            Word randomWord = category.getRandomWord();
            assertTrue(wordsList.contains(randomWord));
        }
    }

    @Test
    public void testAddWord() {
        Word apple = new Word("apple");
        Word banana = new Word("banana");
        ArrayList<Word> wordsList = new ArrayList<>();
        wordsList.add(apple);

        Category category = new Category("Fruits", wordsList);

        assertEquals(1, category.getWords().size());

        category.addWord(banana);

        assertEquals(2, category.getWords().size());
        assertTrue(category.getWords().contains(banana));
    }

	@Test
    public void testIsGuessed() {
        Word word = new Word("test");

        assertTrue(word.isGuessed('t'));
        assertFalse(word.isGuessed('x'));
    }

    // @Test
    // public void testRevealLetter() {
    //     Word word = new Word("test");

    //     List<Integer> indices = word.revealLetter('t');
    //     assertEquals(1, indices.size());
    //     assertTrue(indices.contains(0));
    // }

    @Test
    public void testGetWord() {
        Word word = new Word("test");
        assertEquals("test", word.getWord());
    }

    // @Test
    // public void testIsComplete() {
    //     Word word = new Word("test");

    //     assertFalse(word.isComplete());

    //     word.revealLetter('t');
    //     word.revealLetter('e');
    //     word.revealLetter('s');

    //     assertTrue(word.isComplete());
    // }

    // @Test
    // public void testResetWord() {
    //     Word word = new Word("test");

    //     word.revealLetter('t');
    //     word.revealLetter('e');

    //     word.resetWord();

    //     assertFalse(word.isGuessed('t'));
    //     assertFalse(word.isGuessed('e'));
    //     assertFalse(word.isGuessed('s'));

    //     assertFalse(word.isComplete());
    // }

}
