package br.com.nsp.configuration.object.file;

import java.util.List;

/**
 * Representação do arquivo .nsp
 * 
 * @author Fernando
 *
 */
public class FileNsp {

	private String fileName;

	private List<LineNsp> lines;

	public FileNsp(String fileName, List<LineNsp> lines) {
		this.fileName = fileName;
		this.lines = lines;
	}

	public String getFileName() {
		return fileName;
	}

	public List<LineNsp> getLines() {
		return lines;
	}
}
