import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.ArrayList;
import javafx.scene.control.Alert;

public class GuiServer extends Application {
	
	TextField portNumber;
	Button serverChoice, clientChoice, b1;
	HBox buttonBox;
	Scene startScene;
	BorderPane startPane;
	Server serverConnection;
	VBox serverBox;

	
	ListView<String> listItems, listItems2;
	
	wordGuesserInfo info = new wordGuesserInfo();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Networked Client/Server GUI Example");
		
		this.serverChoice = new Button("Server");
		this.serverChoice.setStyle("-fx-pref-width: 300px");
		this.serverChoice.setStyle("-fx-pref-height: 300px");
		
		this.serverChoice.setOnAction(e -> { 
			try{
				int port = Integer.parseInt(portNumber.getText());
				System.out.println(port);
				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						listItems.getItems().add(data.toString());
					});
	
				}, port);
				primaryStage.setScene(createServerGui());
				primaryStage.setTitle("This is the Server");
			} catch (Exception ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Invalid Port Number");
				alert.setContentText("Please enter a valid port number");
				alert.showAndWait();
			}								
		});
		
		serverBox = new VBox();
		portNumber = new TextField(); // Add this line to create a TextField for the port number
		portNumber.setPromptText("Enter port number"); // Add this line to set the prompt text for the TextField

		this.buttonBox = new HBox(400, serverChoice, portNumber); // Add the TextField to the HBox
		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(buttonBox);

		startScene = new Scene(startPane, 800,800);
		
		listItems = new ListView<String>();
		listItems2 = new ListView<String>();
		
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
		 
		
		primaryStage.setScene(startScene);
		primaryStage.show();
		
	}
	

	public Scene createServerGui() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: coral");
		
		pane.setCenter(listItems);
	
		return new Scene(pane, 500, 400);
		
	}
	

}
