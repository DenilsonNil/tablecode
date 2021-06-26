package br.com.kualit.tablecodegenerator.controller;

import java.util.LinkedHashSet;
import java.util.Set;

import br.com.kualit.tablecodegenerator.service.FileService;
import javafx.fxml.FXML;

public abstract class FileController {

	protected Set<String> imports = new LinkedHashSet<String>();
	protected Set<String> atributos = new LinkedHashSet<String>();
	protected String fileContent = "";
	protected FileService fileService;

	protected abstract void configureChoiceBox();
	protected abstract void generateFileContent();
	
	@FXML
	protected abstract void createJavaFile();
	
	

}