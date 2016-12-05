package br.com.nsp.object;

import java.util.Arrays;
import java.util.Optional;

/**
 * Representação para dias da semana
 * 
 * @author Fernando
 *
 */
public enum Day {

	SEG("Segunda-Feira"), 
	TER("Terça-Feira"), 
	QUA("Quarta-Feira"), 
	QUI("Quinta-Feira"), 
	SEX("Sexta-Feira"), 
	SAB("Sábado"), 
	DOM("Domingo");

	private String descricao;

	Day(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}
	
	public static Optional<Day> getDay(Integer ordinal){
		return Arrays.asList(Day.values())
				.parallelStream()
				.filter(dia -> dia.ordinal() == ordinal)
				.findFirst();
	}
}
