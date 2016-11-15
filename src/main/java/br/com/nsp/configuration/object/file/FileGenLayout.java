package br.com.nsp.configuration.object.file;

import br.com.nsp.object.features.AttributionSequence;
import br.com.nsp.object.features.Problem;
import br.com.nsp.object.features.WorkedSequence;

/**
 * Representação do layout do arquivo
 * 
 * @author Fernando
 *
 */
public class FileGenLayout {

	private static final int PROBLEM = 1;
	private static final int WORKED_SEQUENCE = 2;
	private static final int ATTIBUTION_SEQUENCE = 3;

	private String fileName;
	private Problem problem;
	private WorkedSequence workedSequence;
	private AttributionSequence attributionSequence;

	public FileGenLayout(FileGen file) {
		this.fileName = file.getFileName();

		file.getLines().forEach(line -> {
			char[] caracteres = line.getCaracteres();
			switch (line.getLineNumber()) {
			case PROBLEM:
				problem = new Problem(caracteres[0], caracteres[1]);
				break;
			case WORKED_SEQUENCE:
				workedSequence = new WorkedSequence(caracteres[0], caracteres[1]);
				break;
			case ATTIBUTION_SEQUENCE:
				attributionSequence = new AttributionSequence(caracteres[0], caracteres[1]);
				break;
			}
		});

	}

	public String getFileName() {
		return fileName;
	}

	public Problem getProblem() {
		return problem;
	}

	public WorkedSequence getWorkedSequence() {
		return workedSequence;
	}

	public AttributionSequence getAttributionSequence() {
		return attributionSequence;
	}

}
