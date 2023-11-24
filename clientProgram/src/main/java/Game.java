import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.beans.EventHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class Game extends Application {

    TextField letterGuess, categoryChoice, portNumber, ipAddress, winDisplay = new TextField();
    Text letterGuessedCorrectDisplay;
    Button startClient;
    HBox letterBox;
    Word targetWord;
    int remainingAttempts;
    Map<Character, Square> letterSquares;

    HashMap<String, Scene> sceneMap;
    Stage primaryStage = new Stage();
    

    BorderPane startPane;
    Client clientConnection;
    VBox gameLayout, winMessages = new VBox();
    wordGuesserInfo serializable = new wordGuesserInfo();
    wordGuesserGameLogic gameLogic = new wordGuesserGameLogic();


    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
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

    public Scene createRulesScene(){
        BorderPane rulesPane = new BorderPane();
        //place finalrule.png in the middle of the screen
        Image myImage = new Image("finalrule.png");

        rulesPane.setCenter(new ImageView(myImage));

        Scene rulesScene = new Scene(rulesPane, 900, 600);
        
        Button understand = new Button("I understand");
        HBox startBox = new HBox(understand);
        startBox.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(new ImageView(myImage), startBox);
        rulesPane.setCenter(stackPane);
        
        

        return rulesScene;
    }

    public Scene createCategoryScene(wordGuesserInfo serializable){
        BorderPane categoryPane = new BorderPane();
        categoryPane.setStyle("-fx-background-color: #FB5607");

        //initialize category choice buttons with different background images
        Button category1 = new Button("Fruits");
        //set background image for category 1 button as category1background.png
        category1.setBackground(new Background(new BackgroundImage(new Image("category1background.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        category1.setPrefSize(300, 300);
        category1.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");

        Button category2 = new Button("Animals");
        //set background image for category 2 button as category2background.png
        category2.setBackground(new Background(new BackgroundImage(new Image("category2background.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        category2.setPrefSize(300,300);
        category2.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");

        Button category3 = new Button("Colors");
        //set background image for category 3 button as category3background.png
        category3.setBackground(new Background(new BackgroundImage(new Image("category3background.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
        category3.setPrefSize(300,300);
        category3.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");

        //setonaction for category buttons
        category1.setOnAction(e -> {
            serializable.currentCategory = "Fruits";
            serializable.categoryChosen = true;
            clientConnection.send(serializable);
            System.out.println("Category 'Fruits' selected and sent to server.");
            sceneMap.put("game", createGameScene(serializable));
            primaryStage.setScene(sceneMap.get( "game"));
        });

        category2.setOnAction(e -> {
            serializable.currentCategory = "Animals";
            serializable.categoryChosen = true;
            clientConnection.send(serializable);
            System.out.println("Category 'Animals' selected and sent to server.");

        });

        category3.setOnAction(e -> {
            serializable.currentCategory = "Colors";
            serializable.categoryChosen = true;
            clientConnection.send(serializable);
            System.out.println("Category 'Colors' selected and sent to server.");
        });



        HBox categoryBox = new HBox(30, category1, category2, category3);

        VBox container = new VBox(200, categoryBox);

        categoryBox.setAlignment(Pos.CENTER);

        categoryPane.setCenter(categoryBox);

        Scene categoryScene = new Scene(categoryPane, 900, 600);

        return categoryScene;
    }



    public Scene createGameScene(wordGuesserInfo serializable){

        BorderPane gamePane = new BorderPane();
        gamePane.setStyle("-fx-background-color: #8338EC");

        //get number of letters from the server using getwordandsendback logic
    
        //Display number of guess attempt remaining, category name
        Text remainingGuesses = new Text("Remaining Guesses for this Category: " + serializable.remainingCategoryGuesses + "   ");
        Text category = new Text(" Category: " + serializable.currentCategory + "    ");
        Text numofLetters = new Text("Number of Letters: " + serializable.numLetters);

        remainingGuesses.setFill(Color.WHITE);
        remainingGuesses.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        category.setFill(Color.WHITE);
        category.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        numofLetters.setFill(Color.WHITE);
        numofLetters.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        HBox gameStats = new HBox(remainingGuesses, category, numofLetters);
        HBox squaresBox = new HBox();

        //code for square boxes
        Integer numLetters = serializable.numLetters;
        //make as many square boxes as the number of letters in the word

        squaresBox.setSpacing(10);
        squaresBox.setAlignment(Pos.CENTER);

        for(int i = 0; i < numLetters; i++) {
            Rectangle square = new Rectangle(150, 150);
            //fill with white color
            square.setFill(Color.WHITE);
            //add each square to the squaresBox
            squaresBox.getChildren().add(square);
        }
        
        TextField guessLetterField = new TextField();
        guessLetterField.setPromptText("Guess a single letter");
        Button submitGuess = new Button("Submit Guess");
        HBox guessLetterBox = new HBox(guessLetterField, submitGuess);

        //set on action for submitGuess button. use letter in guessletterfield and set it to letterGuessbyClient
        submitGuess.setOnAction(e -> {
            String guess = guessLetterField.getText();
            if(guess.length() == 1) {
                char letter = guess.charAt(0);
                if(Character.isLetter(letter)) { //to check if user input is a letter.
                    serializable.letterGuessbyClient = letter;
                    clientConnection.send(serializable);
                    System.out.println("Letter guess " + letter + " sent to server.");
                }
            }
        });

        
        gamePane.setCenter(squaresBox);
        gamePane.setBottom(guessLetterBox);
        gamePane.setTop(gameStats);

        Scene gameScene = new Scene(gamePane, 900, 600);

        return gameScene;

    }

    //IF GUESSED CORRECTLY//
    public Scene createCorrectGuessScene(wordGuesserInfo serializable)
    {
        BorderPane correctPane = new BorderPane();
        correctPane.setStyle("-fx-background-color: #8338EC");
        //FIX THIS LATER//
        Image myImage = new Image("finalrule.png");

        Scene correctScene = new Scene(correctPane, 900, 600);
        correctPane.setCenter(new ImageView(myImage));

        Button returnButton = new Button("Keep Playing");
        HBox returnBox = new HBox(returnButton);
        returnBox.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(new ImageView(myImage), returnBox);
        correctPane.setCenter(stackPane);
        
        returnButton.setOnAction(e -> primaryStage.setScene(sceneMap.get("game")));

        return correctScene;
    }

    //IF GUESSED INCORRECTLY//
    public Scene createIncorrectGuessScene(wordGuesserInfo serializable)
    {
        BorderPane incorrectPane = new BorderPane();
        incorrectPane.setStyle("-fx-background-color: #8338EC");
        //FIX THIS LATER//
        Image myImage = new Image("finalrule.png");

        Scene incorrectScene = new Scene(incorrectPane, 900, 600);
        incorrectPane.setCenter(new ImageView(myImage));

        Button returnButton = new Button("Try Again");
        HBox returnBox = new HBox(returnButton);
        returnBox.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(new ImageView(myImage), returnBox);
        incorrectPane.setCenter(stackPane);
        
        returnButton.setOnAction(e -> primaryStage.setScene(sceneMap.get("game")));

        return incorrectScene;
    }

    //IF GAME OVER//
    public Scene createGameOver(wordGuesserInfo serializable)
    {
        BorderPane gameOverPane = new BorderPane();
        gameOverPane.setStyle("-fx-background-color: #8338EC");
        //FIX THIS LATER//
        Image myImage = new Image("finalrule.png");

        Scene gameOverScene = new Scene(gameOverPane, 900, 600);
        gameOverPane.setCenter(new ImageView(myImage));

        Button returnButton = new Button("New Game?");
        HBox returnBox = new HBox(returnButton);
        returnBox.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(new ImageView(myImage), returnBox);
        gameOverPane.setCenter(stackPane);
        
        returnButton.setOnAction(e -> primaryStage.setScene(sceneMap.get("start")));

        return gameOverScene;
    }

    //IF GAME WON//
    public Scene createGameWon(wordGuesserInfo serializable)
    {
        BorderPane winPane = new BorderPane();
        winPane.setStyle("-fx-background-color: #8338EC");
        //FIX THIS LATER//
        Image myImage = new Image("finalrule.png");

        Scene gameWon = new Scene(winPane, 900, 600);
        winPane.setCenter(new ImageView(myImage));

        Button returnButton = new Button("New Game?");
        Button returnButton2 = new Button("Quit Game");
        HBox returnBox = new HBox(returnButton, returnButton2);
        returnBox.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(new ImageView(myImage), returnBox);
        winPane.setCenter(stackPane);
        
        returnButton.setOnAction(e -> primaryStage.setScene(sceneMap.get("start")));
        returnButton2.setOnAction(e -> Platform.exit());

        return gameWon;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

            primaryStage.setTitle("Word Guesser Game");

            final wordGuesserInfo[] infoArray = {new wordGuesserInfo()};

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


            sceneMap = new HashMap<String, Scene>();
            //startScene is the client connection scene, where we ask for port number
            sceneMap.put("start", startScene);
            //rulesScene is the scene that displays the rules of the game
            sceneMap.put("rules", createRulesScene());
                
            primaryStage.setScene(startScene);
            primaryStage.show();

            //when startClient button is clicked
            startClient.setOnAction (e -> {
                //if port number is not empty, connect to server
                if(!portNumber.getText().isEmpty()) {  

                    Integer portNum = Integer.valueOf(portNumber.getText());

                    if(portNum <= 0){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Please enter a valid port number");
                        alert.showAndWait();
                    }
                    
                    else { //successfully logs into the game, displays menu options
                        // START OF LOGIN BUTTON //
            
                        //menu bar with options to view rules and quit game
                        MenuItem menu1 = new MenuItem("Rules");
                        MenuItem menu2 = new MenuItem("Quit");
            
                        Menu menu = new Menu("Options", null, menu1, menu2);
                        MenuBar menuBar = new MenuBar(menu);
                        menuBar.getMenus().add(menu);
                        
                        BorderPane rootGame = new BorderPane();

                        menu1.setOnAction((value)-> {
                            primaryStage.setScene(sceneMap.get("rules"));
                            primaryStage.show();
                        });

                        menu2.setOnAction((value)-> {
                            Platform.exit();
                            System.exit(0);
                        });

                        rootGame.setTop(menuBar);

                        //as category is chosen, receive wordguesserinfo object from server at the SAME TIME, because the 
                        //category scene is being called as of the moment.
                        clientConnection = new Client(data -> {
                            //non-visible text field to display the letters if they are guessed correctly
                            Text letterGuessedCorrectDisplay = new Text();
                            // client connection is established, and we are receiving data from the server. inside platform.runlater, we are
                            // assigning the category and word length for the user, displaying the attempts remaining for the word, attempts for the category,
                            // and how many words are guessed correctly.
                            Platform.runLater(()->{
                                wordGuesserInfo clientInfo = (wordGuesserInfo) data;
                                infoArray[0] = clientInfo;
                                
                                Scene categoryScene = createCategoryScene(clientInfo);
                                //set scene to category scene, which will display the function createCategoryScene
                                primaryStage.setScene(categoryScene);   
                                sceneMap.put("category", categoryScene);
                    
                                System.out.println("Client received: " + clientInfo);
                                System.out.println("Category chosen by client " + clientInfo.currentCategory);



                                if (clientInfo.numLetters != 0) {
                                    System.out.println("Client received updated wordGuesserInfo after sending category");
                                    System.out.println("The number of letters is " + clientInfo.numLetters);
                                    //create game scene
                                    Scene gameScene = createGameScene(clientInfo);
                                    //set scene to game scene, which will display the function createGameScene
                                    primaryStage.setScene(gameScene);
                                    sceneMap.put("game", gameScene);
                                }
                            });
                        }, portNum);

                        clientConnection.start();     

                        //use this remaining portion of the code to do submitguess request.

                    }
                                    
                }//end of if portnumber is not empty




            }); //end of startClient.setOnAction
    }



}
