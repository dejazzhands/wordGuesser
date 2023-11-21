import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class Game extends Application {

    TextField letterGuess, categoryChoice, portNumber, ipAddress, winDisplay = new TextField();
    Button startClient;
    Stage primaryStage;
    HBox letterBox;
    Word targetWord;
    int remainingAttempts;
    Map<Character, Square> letterSquares;

    HashMap<String, Scene> sceneMap;
    

    BorderPane startPane;
    Client clientConnection;
    VBox gameLayout, winMessages = new VBox();
    wordGuesserInfo serializable = new wordGuesserInfo();
    wordGuesserGameLogic gameLogic = new wordGuesserGameLogic();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    public void fruits() {


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


    //create letter squares for the word, for the number of letters of the word
    public void createLetterSquares() {
        // TODO Auto-generated method stub
        for(int i = 0; i < targetWord.getWord().length(); i++) {
            char letter = targetWord.getWord().charAt(i);
            Square square = new Square(letter);
            letterSquares.put(letter, square);
            gameLayout.getChildren().add(square);
        }
    }


    public void startNewGame(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to play again?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            if(result.get() == ButtonType.OK){
                gameLogic.resetForNextCategory();
            }
            if(result.get() == ButtonType.CANCEL){
                Platform.exit();    
                System.exit(0);
            }
        }
    }

    public void handleGuess() {
        // TODO Auto-generated method stub
        String guess = letterGuess.getText();
        if(guess.length() == 1) {
            char letter = guess.charAt(0);
            if(Character.isLetter(letter)) {
                gameLogic.guessLetter(letter);
                letterSquares.get(letter).reveal();
                if(gameLogic.isWordGuessed()) {

                    clientConnection.send(serializable);
                    //display message
                    winMessages.getChildren().add(new Text("You guessed the word!"));
                    startNewGame();

                } else if(gameLogic.getRemainingGuessesPerWord() == 0) {
                    // Client used all guesses
                    // Send a message to the server
                    clientConnection.send(serializable);
                    // Display a message to the client
                    winMessages.getChildren().add(new Text("You used all your guesses!"));
                    // Start a new game
                    startNewGame();
                }
            }
        }
    }



    public Scene createCategoryScene(){
        BorderPane categoryPane = new BorderPane();
        categoryPane.setStyle("-fx-background-color: #FB5607");

        //initialize category choice buttons with different background images
        Button category1 = new Button("Fruits");
        //set background image for category 1 button as category1background.png
        category1.setBackground(new Background(new BackgroundImage(new Image("category1background.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        category1.setPrefSize(300, 300);

        Button category2 = new Button("Animals");
        //set background image for category 2 button as category2background.png
        category2.setBackground(new Background(new BackgroundImage(new Image("category2background.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        category2.setPrefSize(300,300);

        Button category3 = new Button("Colors");
        //set background image for category 3 button as category3background.png
        category3.setBackground(new Background(new BackgroundImage(new Image("category3background.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        category3.setPrefSize(300,300);

        //setonaction for category buttons
        category1.setOnAction(e -> {

            Word apple = new Word("apple");
            Word banana = new Word("banana");
            Word orange = new Word("orange");

            ArrayList<Word> fruitsList = new ArrayList<Word>();
            fruitsList.add(apple);
            fruitsList.add(banana);
            fruitsList.add(orange);

            Category categoryOne = new Category("fruits", fruitsList);

            //set category to fruits
            serializable.setCurrentCategory("Fruits");
            //pick a random word from the fruits category
            

            //send wordguesserinfo object to server
            clientConnection.send(serializable);

            primaryStage.setScene(sceneMap.get("game"));
        });

        category2.setOnAction(e -> {
            //set category to animals
            serializable.setCurrentCategory("Animals");
            //send wordguesserinfo object to server
            clientConnection.send(serializable);
            primaryStage.setScene(sceneMap.get("game"));
        });

        category3.setOnAction(e -> {
            //set category to colors
            serializable.setCurrentCategory("Colors");
            //send wordguesserinfo object to server
            clientConnection.send(serializable);
            primaryStage.setScene(sceneMap.get("game"));
        });

        
        HBox categoryBox = new HBox(30, category1, category2, category3);

        VBox container = new VBox(200, categoryBox);

        categoryBox.setAlignment(Pos.CENTER);

        categoryPane.setCenter(categoryBox);

        Scene categoryScene = new Scene(categoryPane, 900, 600);

        return categoryScene;
    }


    public Scene createRulesScene(){
        BorderPane rulesPane = new BorderPane();
        //place finalrule.png in the middle of the screen
        Image myImage = new Image("finalrule.png");

        rulesPane.setCenter(new ImageView(myImage));

        Scene rulesScene = new Scene(rulesPane, 1000, 800);

        Button startGame = new Button("Start Game");

        HBox startBox = new HBox(startGame);

        startGame.setOnAction(e -> primaryStage.setScene(sceneMap.get("game")));

        rulesPane.setBottom(startBox);

        return rulesScene;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Word Guesser Game");

            //write title text on top of the image
            Text title = new Text("WORD GUESSER");
            //size of title should be big and located in the center
            title.setStyle("-fx-font-size: 50px");
            title.setTextAlignment(TextAlignment.CENTER);
            title.setFill(Color.WHITE);

            // create a pane to display word guesser 1 .png in the center
            startPane = new BorderPane();
            Image myImage = new Image("word guesser 1.png");
            //place image in the center
            startPane.setCenter(new ImageView(myImage));
            //color startPane background with FFEBE0B
            startPane.setStyle("-fx-background-color: #FFBE0B");

            //button to connect to server and textfield
            startClient = new Button("Connect to Server");
            portNumber = new TextField();

            //group the button and textfield together
            letterBox = new HBox(10, portNumber, startClient);
            //place the button and textfield in the center
            letterBox.setAlignment(Pos.CENTER);

            startPane.setTop(title);
            startPane.setBottom(letterBox);

            Scene startScene = new Scene(startPane, 900, 600);
            primaryStage.setScene(startScene);
            primaryStage.show();


            sceneMap = new HashMap<String, Scene>();
            sceneMap.put("start", startScene);
            sceneMap.put("rules", createRulesScene());
            sceneMap.put("category", createCategoryScene());

            startClient.setOnAction(e -> {
                //if port number is not empty, connect to server
                if(!portNumber.getText().isEmpty()) {                       
                    clientConnection = new Client(data -> {
                        Platform.runLater(() -> {
                            //send wordguesserinfo object to server
                            clientConnection.send(serializable);
                        });
                    });

                    clientConnection.start();

                    primaryStage.setScene(sceneMap.get("rules"));
                    primaryStage.show();
                }
            });
    }



}
