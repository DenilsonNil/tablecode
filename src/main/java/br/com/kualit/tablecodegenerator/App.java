package br.com.kualit.tablecodegenerator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application{
	private static final String LAYOUT_FXML = "/ModelLayout.fxml";
	private static final String TITULO_JANELA = "Criação do modelo";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(@SuppressWarnings("exports") Stage primaryStage) throws Exception {

		primaryStage.setTitle(TITULO_JANELA);
		primaryStage.setResizable(false);
		
		Pane root = FXMLLoader.load(getClass().getResource(LAYOUT_FXML));
		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.show();

		
		
	}
}
