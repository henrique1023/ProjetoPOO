package application.mainfx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static Scene mainScene;
	@Override
	public void start(Stage primaryStage) {
		try {
			// essa instacia serve para manipular a tela mais facilmente
			// adicionando o arquivo do FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);

			mainScene = new Scene(scrollPane);
			primaryStage.setScene(mainScene);
<<<<<<< HEAD:src/application/mainfx/Main.java
			primaryStage.setTitle("Consultario Mario Caetano");
=======
			primaryStage.setTitle("Consult�rio M�rio Caetano");
>>>>>>> master:src/application/Main.java
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
