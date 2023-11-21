import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Square extends BorderPane {
    private char letter;
    private boolean isRevealed;
    private Text text;

    public Square(char letter) {
        super();
        this.letter = letter;
        this.isRevealed = false;
        this.text = new Text();

        // Create a Rectangle for the background
        Rectangle background = new Rectangle(150, 150);
        //use the wordbackground.png image as the background
        background.setFill(new javafx.scene.image.Image(getClass().getResourceAsStream("wordbackground.png")).getPixelReader().getColor(0, 0));

        // Set initial style for the Text node
        text.setFont(Font.font("Arial", 20));
        text.setFill(Color.BLACK);

        // Add the background and text to the StackPane
        getChildren().addAll(background, text);

        style();
    }

    public void style() {
        // Additional styling if needed
    }

    public char getLetter() {
        return letter;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void reveal() {
        isRevealed = true;
        text.setText(Character.toString(letter));
        setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.WHITE, new javafx.scene.layout.CornerRadii(0), new Insets(0))));
    }

    public void hide() {
        isRevealed = false;
        text.setText("");
        setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(Color.RED, new javafx.scene.layout.CornerRadii(0), new Insets(0))));
    }

    public String toString() {
        if (isRevealed) {
            return Character.toString(letter);
        } else {
            return "_";
        }
    }
}
