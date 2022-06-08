package sae_ihm;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TutoringApplication extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
        URL fxmlFileUrl = getClass().getResource("main.fxml");
        if (fxmlFileUrl == null) {
                System.out.println("Impossible de charger le fichier fxml");
                return;
        }
        loader.setLocation(fxmlFileUrl);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Affectation de tutorat");
        stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
