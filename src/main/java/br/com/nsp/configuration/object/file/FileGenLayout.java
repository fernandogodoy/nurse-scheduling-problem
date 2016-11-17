package br.com.nsp.configuration.object.file;

import br.com.nsp.object.features.AfternoonShift;
import br.com.nsp.object.features.AttributionSequence;
import br.com.nsp.object.features.DayOff;
import br.com.nsp.object.features.MorningShift;
import br.com.nsp.object.features.NightShift;
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
	private static final int MORNING_SHIFT = 4;
	private static final int AFTERNOON_SHIFT = 5;
	private static final int NIGHT_SHIFT = 6;
	private static final int DAY_OFF = 7;

	private String fileName;
	private Problem problem;
	private WorkedSequence workedSequence;
	private AttributionSequence attributionSequence;
	private MorningShift morningShift;
	private AfternoonShift afternoonShift;
	private NightShift nightShift;
	private DayOff dayOff;

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
			case MORNING_SHIFT:
				morningShift = new MorningShift(caracteres[2], caracteres[3], caracteres[0], caracteres[1]);
				break;
			case AFTERNOON_SHIFT:
				afternoonShift = new AfternoonShift(caracteres[2], caracteres[3], caracteres[0], caracteres[1]);
				break;
			case NIGHT_SHIFT:
				nightShift = new NightShift(caracteres[2], caracteres[3], caracteres[0], caracteres[1]);
				break;
			case DAY_OFF:
				dayOff = new DayOff(caracteres[2], caracteres[3], caracteres[0], caracteres[1]);
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

	public MorningShift getMorningShift() {
		return morningShift;
	}

	public AfternoonShift getAfternoonShift() {
		return afternoonShift;
	}

	public NightShift getNightShift() {
		return nightShift;
	}

	public DayOff getDayOff() {
		return dayOff;
	}
}
