package br.com.nsp.configuration.object;

import br.com.nsp.object.Day;
import br.com.nsp.object.Shift;
import br.com.nsp.util.Util;

public class Demand {

	private Day dia;

	private Shift turno;

	private Integer qtdExigida;

	public Demand(Day dia, Shift turno, char qtd) {
		this.dia = dia;
		this.turno = turno;
		this.qtdExigida = Util.toInt(qtd);
	}

	public Integer getQtdExigida() {
		return qtdExigida;
	}

	public Shift getTurno() {
		return turno;
	}

	public Day getDia() {
		return dia;
	}
	
	@Override
	public String toString() {
		return "dia=" + dia + ", turno=" + turno + ", demanda=" + qtdExigida;
	}

}
