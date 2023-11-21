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
import javafx.stage.Stage;

import java.beans.EventHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class Game extends Application {

    TextField letterGuess, categoryChoice, portNumber, ipAddress, winDisplay = new TextField();
    Button startServer;
    Stage primaryStage;
    HBox letterBox;
    Word targetWord;
    int remainingAttempts;
    Map<Character, Square> letterSquares;

    BorderPane startPane;
    Client clientConnection;
    VBox gameLayout, winMessages = new VBox();
    wordGuesserInfo serializable = new wordGuesserInfo();
    wordGuesserGameLogic gameLogic = new wordGuesserGameLogic();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Word Guesser Game");

            //write title text on top of the image
            Text title = new Text("Word Guesser");
            //size of title should be big
            title.setStyle("-fx-font-size: 50px");
            title.setFill(Color.WHITE);

            // create a pane to display word guesser 1 .png in the center
            startPane = new BorderPane();
            Image myImage = new Image("word guesser 1.png");
            //place image in the center
            startPane.setCenter(new ImageView(myImage));
            //color startPane background with FFEBE0B
            startPane.setStyle("-fx-background-color: #FFBE0B");

            //button to connect to server and textfield
            startServer = new Button("Connect to Server");
            portNumber = new TextField();

            startPane.setTop(title);
            startPane.setBottom(startServer);
            startPane.setLeft(portNumber);

            Scene startScene = new Scene(startPane, 900, 600);
            primaryStage.setScene(startScene);
            primaryStage.show();
    }

    // private void connectToServer(ActionEvent event) {
    //     if (!portNumber.getText().isEmpty()) {
    //         int port = Integer.parseInt(portNumber.getText());
    //         String host = "localhost";  // Change this if your server is on a different machine
    //         clientConnection = new Client(host, port, data -> {
    //             Platform.runLater(() -> {
    //                 winMessages.getChildren().add(new Text(data.toString()));
    //             });
    //         });
    //         clientConnection.start();

    //         setupGameScene();
    //     } else {
    //         showAlert("Please enter a valid port number.");
    //     }
    // }

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

}
