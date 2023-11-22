import java.util.ArrayList;

public class Category {
    private String name;
    private ArrayList<Word> words;

    public Category(String name, ArrayList<Word> words) {
        this.name = name;
        this.words = words;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public Word getRandomWord() {
        int randomIndex = (int) (Math.random() * words.size());
        return words.get(randomIndex);
    }

    public void addWord(Word word) {
        words.add(word);
    }
}
