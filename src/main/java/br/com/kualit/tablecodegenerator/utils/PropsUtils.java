package br.com.kualit.tablecodegenerator.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

import br.com.kualit.tablecodegenerator.exception.InconsistentConfigFileException;
import br.com.kualit.tablecodegenerator.exception.InconsistentPathException;

public class PropsUtils {

	public static String getFileLocation() throws InconsistentPathException, IOException, InconsistentConfigFileException {

		String currentDir = (new File(".").getCanonicalPath()) + "\\config.txt";
		Properties props = new Properties();

		if (Paths.get(currentDir).toFile().exists()) {

			try (InputStream is = new FileInputStream(currentDir)) {
				props.load(is);
				String location = props.getProperty("location");
				
				if(location == null) {
					throw new InconsistentConfigFileException();
				}else {
					boolean exists = Paths.get(location).toFile().exists();
					
					if(!exists) {
						throw new InconsistentPathException(location);
					}
				}
				
				return location;
			}
		} else {
			throw new FileNotFoundException("O arquivo de configuração não existe");

		}

	}

}
