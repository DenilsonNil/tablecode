package br.com.kualit.tablecodegenerator.service;

import java.io.BufferedWriter;
import java.io.FileWriter;

import br.com.kualit.tablecodegenerator.utils.AlertUtils;
import br.com.kualit.tablecodegenerator.utils.PropsUtils;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class FileService extends Service<String> {

	private String fileContent;
	private String fileName;
	private String message = "";

	public FileService(String fileContent, String fileName) {
		this.fileContent = fileContent;
		this.fileName = fileName + ".java";
	}

	@Override
	protected Task<String> createTask() {
		return new Task<String>() {

			@Override
			protected String call() {
				try {
					String fileLocation = PropsUtils.getFileLocation();
					String completePath = fileLocation + fileName;
					try (BufferedWriter bw = new BufferedWriter(new FileWriter(completePath))) {

						bw.write(fileContent);

					}

				} catch (Exception e) {
					message = e.getMessage();
					this.cancel();
				}

				if (isCancelled()) {
					cancelled();
				}

				return null;
			}

			@Override
			protected void succeeded() {
				AlertUtils.showWarningMessage("Arquivo gerado com sucesso ");
			}

			@Override
			protected void cancelled() {
				AlertUtils.showWarningMessage(message);
			}

		};

	}

}
