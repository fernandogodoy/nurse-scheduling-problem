package br.com.nsp.configuration.object;

import br.com.nsp.object.Day;
import br.com.nsp.object.Nurse;
import br.com.nsp.object.Shift;
import br.com.nsp.util.Util;

public class Preference {

	private Nurse enfermeiro;

	private Shift turno;

	private Integer peso;

	private Day dia;

	public Preference(Nurse enfermeiro, Day day, Shift turno, char peso) {
		this.enfermeiro = enfermeiro;
		this.dia = day;
		this.turno = turno;
		this.peso = Util.toInt(peso);
	}
	
	public Nurse getEnfermeiro() {
		return enfermeiro;
	}
	
	public Shift getTurno() {
		return turno;
	}
	
	public Integer getPeso() {
		return peso;
	}
	
	public Day getDia() {
		return dia;
	}

	@Override
	public String toString() {
		return "[enf= " + enfermeiro + " dia= " + dia + ", turno=" + turno + ", peso=" + peso + "]";
	}

}
