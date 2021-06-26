package br.com.kualit.tablecodegenerator.exception;

@SuppressWarnings("serial")
public class InconsistentConfigFileException extends Exception {

	public InconsistentConfigFileException() {
		super("Arquivo de configuração inválido. Precisa ter uma entrada chamada location");
	}
	
	
	

}
