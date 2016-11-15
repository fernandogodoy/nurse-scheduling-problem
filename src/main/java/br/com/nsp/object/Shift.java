package br.com.nsp.object;

/**
 * Representação para turnos de trabalho
 * 
 * @author Fernando
 *
 */
public enum Shift {

	M("Manhã"), T("Tarde"), N("Noite"), F("Folga");

	private String descricao;

	Shift(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}
}
