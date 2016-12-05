package br.com.nsp.configuration.object;

import br.com.nsp.object.Alocation;
import br.com.nsp.object.Day;
import br.com.nsp.object.Nurse;
import br.com.nsp.object.Shift;

public class Solluction {

	private Nurse enfermeiro;

	private Day dia;

	private Shift turno;
	
	private Alocation alocation;

	public Solluction(Nurse enfermeiro, Day dia, Shift turno, Alocation alocation) {
		this.enfermeiro = enfermeiro;
		this.dia = dia;
		this.turno = turno;
		this.alocation = alocation;
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
	
	public Alocation getAlocation() {
		return alocation;
	}
	
	public void setTurno(Shift turno) {
		this.turno = turno;
	}

	@Override
	public String toString() {
		return "dia=" + dia + ", turno=" + turno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		result = prime * result + ((enfermeiro == null) ? 0 : enfermeiro.hashCode());
		result = prime * result + ((turno == null) ? 0 : turno.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Solluction other = (Solluction) obj;
		if (dia != other.dia)
			return false;
		if (enfermeiro == null) {
			if (other.enfermeiro != null)
				return false;
		} else if (!enfermeiro.equals(other.enfermeiro))
			return false;
		if (turno != other.turno)
			return false;
		return true;
	}
	

}
