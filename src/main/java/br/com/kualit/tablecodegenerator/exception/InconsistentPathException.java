package br.com.kualit.tablecodegenerator.exception;

@SuppressWarnings("serial")
public class InconsistentPathException extends Exception {

	public InconsistentPathException(String location) {
		super("O caminho especificado no arquivo de configuração é inválido ou não existe. " +location);
	}

}
