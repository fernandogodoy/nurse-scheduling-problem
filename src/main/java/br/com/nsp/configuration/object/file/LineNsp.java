package br.com.nsp.configuration.object.file;

import java.util.Arrays;

/**
 * Representação para um linha do arquivo .nsp
 * 
 * @author Fernando
 *
 */
public class LineNsp {

	private String[] line;

	public LineNsp(String[] line) {
		this.line = line;
	}
	
	@Override
	public String toString() {
		return Arrays.toString(line);
	}
}
