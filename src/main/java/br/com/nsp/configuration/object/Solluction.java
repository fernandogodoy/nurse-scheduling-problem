package br.com.nsp.configuration.object;

import br.com.nsp.object.Day;
import br.com.nsp.object.Nurse;
import br.com.nsp.object.Shift;

public class Solluction {

	private Nurse enfermeiro;

	private Day dia;

	private Shift turno;

	public Solluction(Nurse enfermeiro, Day dia, Shift turno) {
		this.enfermeiro = enfermeiro;
		this.dia = dia;
		this.turno = turno;
	}

	public Nurse getEnfermeiro() {
		return enfermeiro;
	}

	public Day getDia() {
		return dia;
	}

	public Shift getTurno() {
		return turno;
	}

	@Override
	public String toString() {
		return "dia=" + dia + ", turno=" + turno;
	}

}
