package br.com.kualit.tablecodegenerator.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtils {
	
	public static void showWarningMessage(String conteudo) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Atenção");
		alert.setHeaderText("Mensagem de alerta importante!");
		alert.setContentText(conteudo);
		alert.show();
		
	}

}
