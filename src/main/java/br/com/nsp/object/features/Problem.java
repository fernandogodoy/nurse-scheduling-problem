package br.com.nsp.object.features;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.nsp.object.Day;
import br.com.nsp.object.Shift;
import br.com.nsp.util.Util;

/**
 * Representação das caracteristicas do problema.
 * 
 * @author Fernando
 *
 */
public class Problem {

	private List<Day> diasTrabalho;
	private List<Shift> turnosTrabalho;

	public Problem(char qtdDiasTrabalhado, char qtdTurnosTrabalho) {
		this.diasTrabalho = getDays(qtdDiasTrabalhado);
		this.turnosTrabalho = getShifts(qtdTurnosTrabalho);
	}

	private List<Day> getDays(char qtd) {
		return Arrays.asList(Day.values()).parallelStream()
				.filter(d -> d.ordinal() <= Util.toInt(qtd))
				.collect(Collectors.toList());
	}

	private List<Shift> getShifts(char qtd) {
		return Arrays.asList(Shift.values()).parallelStream()
				.filter(d -> d.ordinal() <= Util.toInt(qtd))
				.collect(Collectors.toList());
	}

	public List<Day> getDiasTrabalho() {
		return diasTrabalho;
	}

	public List<Shift> getTurnosTrabalho() {
		return turnosTrabalho;
	}

}
