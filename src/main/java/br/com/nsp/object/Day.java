package br.com.nsp.object;

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
}
