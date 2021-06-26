module TableCodeGeneratorJavaFX {
	exports br.com.kualit.tablecodegenerator;
	exports br.com.kualit.tablecodegenerator.controller;
	exports br.com.kualit.tablecodegenerator.model;
	

	requires transitive javafx.base;
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;
	
	opens br.com.kualit.tablecodegenerator.controller;
}