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


    public void fruits() {

        Word apple = new Word("apple");
        Word banana = new Word("banana");
        Word orange = new Word("orange");

        ArrayList<Word> fruitsList = new ArrayList<Word>();
        fruitsList.add(apple);
        fruitsList.add(banana);
        fruitsList.add(orange);

        Category categoryOne = new Category("fruits", fruitsList);
    }

    public void animals() {

        Word dog = new Word("dog");
        Word cat = new Word("cat");
        Word bird = new Word("bird");

        ArrayList<Word> animalsList = new ArrayList<Word>();
        animalsList.add(dog);
        animalsList.add(cat);
        animalsList.add(bird);

        Category categoryTwo = new Category("animals", animalsList);
    }

    public void colors() {

        Word red = new Word("red");
        Word blue = new Word("blue");
        Word green = new Word("green");

        ArrayList<Word> colorsList = new ArrayList<Word>();
        colorsList.add(red);
        colorsList.add(blue);
        colorsList.add(green);

        Category categoryThree = new Category("colors", colorsList);
    }
}
