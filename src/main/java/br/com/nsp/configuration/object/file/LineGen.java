package br.com.nsp.configuration.object.file;

/**
 * Represenção de uma linha do arquivo .gen
 * 
 * @author Fernando
 *
 */
public class LineGen {

	private int lineNumber;
	private char[] caracteres;

	public LineGen(int lineNumber, char[] caracteres) {
		this.lineNumber = lineNumber;
		this.caracteres = caracteres;
	}

	public char[] getCaracteres() {
		return caracteres;
	}

	public int getLineNumber() {
		return lineNumber;
	}
}
