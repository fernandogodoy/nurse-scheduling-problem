package br.com.nsp.configuration.object.file;

import java.util.List;

/**
 * Representação do arquivo de .gen
 * 
 * @author Fernando
 *
 */
public class FileGen {

	private String fileName;

	private List<LineGen> lines;

	public FileGen(String fileName, List<LineGen> lines) {
		this.fileName = fileName;
		this.lines = lines;
	}

	public String getFileName() {
		return fileName;
	}

	public List<LineGen> getLines() {
		return lines;
	}

}
