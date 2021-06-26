package br.com.kualit.tablecodegenerator.utils;

import java.util.LinkedHashSet;
import java.util.Set;

public class StringUtils {
	
	public static String modelName;
	public static Set<String> modelAttributes = new LinkedHashSet<String>();
	public static String listMethodName = "list()";
	
	public static  String configuraTipoProperty(String tipoSelecionado) {
		return converteTipo(tipoSelecionado.split("Property")[0]);
	}
	
	public static String configuraNomeProperty(String nomeDaProperty) {
		String primeiraLetraMaiuscula = nomeDaProperty.substring(0,1).toUpperCase();
		String nomePropertyCamelCase = primeiraLetraMaiuscula + nomeDaProperty.substring(1);
		return nomePropertyCamelCase;
	}
	
	
	public static String getListMethod() {
		return "\n\nprivate ObservableList<"+modelName+"> " +listMethodName+"{\n"
				+ "return FXCollections.observableArrayList();\n}";
	}
	
	
	private static String converteTipo(String word) {
		if(word.equals("Integer")) {
			word = word.substring(0, 3).replace("I", "i");
		}
		
		else if(word.equals("Boolean")) {
			word = word.replace("B", "b");
		}
		
		else if(word.equals("Double")) {
			word = word.replace("D", "d");
		}
		
		return word;
	}
}
