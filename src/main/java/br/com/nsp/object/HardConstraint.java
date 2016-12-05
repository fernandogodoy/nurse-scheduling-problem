package br.com.nsp.object;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.com.nsp.configuration.Config;
import br.com.nsp.configuration.object.Instancy;
import br.com.nsp.configuration.object.Solluction;
import br.com.nsp.object.features.AfternoonShift;
import br.com.nsp.object.features.AttributionSequence;
import br.com.nsp.object.features.DayOff;
import br.com.nsp.object.features.MorningShift;
import br.com.nsp.object.features.NightShift;

public class HardConstraint implements Constraint {

	private Map<Nurse, List<Solluction>> solucao;

	private BigDecimal penalidade = BigDecimal.ZERO;
	private BigDecimal totalPenalidade = BigDecimal.ZERO;
	
	private AttributionSequence attributionSequence;
	private MorningShift morningShift;
	private AfternoonShift afternoonShift;
	private NightShift nightShift;
	private DayOff dayOff;

	public HardConstraint(Map<Nurse, List<Solluction>> solucao, Instancy instancia) {
		this.solucao = solucao;
		attributionSequence = instancia.getAttributionSequence();
		morningShift = instancia.getMorningShift();
		afternoonShift = instancia.getAfternoonShift();
		nightShift = instancia.getNightShift();
		dayOff = instancia.getDayOff();
	}

	private BigDecimal penalizarTurno() {
		penalidade = BigDecimal.ZERO;
		solucao.entrySet().forEach(sols ->{
			List<Solluction> listSols = getSolucoesPorEnfermeiro(sols.getKey());
			penalizarMinMaxManha(listSols);
			penalizarMinMaxTarde(listSols);
			penalizarMinMaxNoite(listSols);
			penalizarMinMaxFolga(listSols);

			int manha = 0;
			int tarde = 0;
			int contadorNoite = 0;
			int folga = 0;
			int atribuicoes = 0;
			int manhaConsecutiva = 0;
			int tardeConsecutiva = 0;
			int noiteConsecutiva = 0;
			int folgaConsecutiva = 0;
			int atribuicaoConsecutiva = 0;
			for (Solluction solluction : listSols) {
				Shift turno = solluction.getTurno();
				contarAtribuicoesConsecutivo(turno, atribuicoes, atribuicaoConsecutiva);
				contarManhaConsecutivo(turno, manha, manhaConsecutiva);
				contarTardeConsecutivo(turno, tarde, tardeConsecutiva);
				contarNoiteConsecutivo(turno, contadorNoite, noiteConsecutiva);
				contarFolgaConsecutivo(turno, folga, folgaConsecutiva);
			}

			atribuirPenalidade(manhaConsecutiva, tardeConsecutiva, noiteConsecutiva, folgaConsecutiva, atribuicaoConsecutiva);
		});
		
		for (int nurseCount = 1; nurseCount < solucao.size(); nurseCount++) {
			

		}
		return penalidade;
	}

	private void atribuirPenalidade(int manhaConsecutiva, int tardeConsecutiva, int noiteConsecutiva,
			int folgaConsecutiva, int atribuicaoConsecutiva) {
		if (atribuicaoConsecutiva < attributionSequence.getMin() || atribuicaoConsecutiva > attributionSequence.getMax()) {
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
		if (manhaConsecutiva < morningShift.getMinConsecutivas() || manhaConsecutiva > morningShift.getMaxConsecutivas()) {
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
		if (tardeConsecutiva < afternoonShift.getMinConsecutivas() || tardeConsecutiva > afternoonShift.getMaxConsecutivas()) {
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
		if (noiteConsecutiva < nightShift.getMinConsecutivas() || noiteConsecutiva > nightShift.getMaxConsecutivas()) {
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
		if (folgaConsecutiva < dayOff.getMinConsecutivas() || folgaConsecutiva > dayOff.getMaxConsecutivas()) {
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
	}

	private void contarAtribuicoesConsecutivo(Shift turno, int contador, int contadorConsecutivo) {
		if (!turno.equals(Shift.F)) {
			contador++;
		} else {
			if (contador > 1) {
				contadorConsecutivo = contador > contadorConsecutivo ? contador : contadorConsecutivo;
			}
			contador = 0;
		}
	}

	private void contarFolgaConsecutivo(Shift turno, int contador, int contadorConsecutivo) {
		if (turno.equals(Shift.F)) {
			contador++;
		} else {
			if (contador > 1) {
				contadorConsecutivo = contador > contadorConsecutivo ? contador : contadorConsecutivo;
			}
			contador = 0;
		}
	}

	private void contarNoiteConsecutivo(Shift turno, int contador, int contadorConsecutivo) {
		if (turno.equals(Shift.N)) {
			contador++;
		} else {
			if (contador > 1) {
				contadorConsecutivo = contador > contadorConsecutivo ? contador : contadorConsecutivo;
			}
			contador = 0;
		}
	}

	private void contarTardeConsecutivo(Shift turno, int contador, int contadorConsecutivo) {
		if (turno.equals(Shift.T)) {
			contador++;
		} else {
			if (contador > 1) {
				contadorConsecutivo = contador > contadorConsecutivo ? contador : contadorConsecutivo;
			}
			contador = 0;
		}
	}

	private void contarManhaConsecutivo(Shift turno, int contador, int contadorConsecutivo) {
		if (turno.equals(Shift.M)) {
			contador++;
		} else {
			if (contador > 1) {
				contadorConsecutivo = contador > contadorConsecutivo ? contador : contadorConsecutivo;
			}
			contador = 0;
		}
	}

	private void penalizarMinMaxFolga(List<Solluction> listSols) {
		long count = countTurnos(listSols, Shift.F);
		if (count < dayOff.getMin() || count > dayOff.getMax()) {
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
	}

	private void penalizarMinMaxNoite(List<Solluction> listSols) {
		long count = countTurnos(listSols, Shift.N);
		if (count < nightShift.getMin() || count > nightShift.getMax()) {
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
	}

	private void penalizarMinMaxTarde(List<Solluction> listSols) {
		long count = countTurnos(listSols, Shift.T);
		if (count < afternoonShift.getMin() || count > afternoonShift.getMax()) {
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
	}

	private void penalizarMinMaxManha(List<Solluction> listSols) {
		long count = countTurnos(listSols, Shift.M);
		if (count < morningShift.getMin() || count > morningShift.getMax()) {
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
	}
	
	private long countTurnos(List<Solluction> listSols, Shift shift) {
		long count = listSols.parallelStream()
							.filter(sol -> sol.getTurno().equals(shift))
							.count();
		return count;
	}

	@Override
	public BigDecimal calcular() {
		totalPenalidade = totalPenalidade.add(penalizarTurnoSubsequente());
		totalPenalidade = totalPenalidade.add(penalizarTurno());
		return totalPenalidade;
	}

	private BigDecimal penalizarTurnoSubsequente() {
		penalidade = BigDecimal.ZERO;
		solucao.entrySet().forEach(sols ->{
			List<Solluction> listSols = getSolucoesPorEnfermeiro(sols.getKey());
			String anterior = null;
			for (Solluction solluction : listSols) {
				Shift atual = solluction.getTurno();
				if (anterior != null && !anterior.equals(atual)) {
					penalizarDia(atual, Shift.valueOf(anterior));
				}
				anterior = atual.name();
			}
		});
		return penalidade;
	}

	private void penalizarDia(Shift atual, Shift anterior) {
		if (atual.equals(Shift.T) && anterior.equals(Shift.N)) {
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}

		if (atual.equals(Shift.M) && anterior.equals(Shift.N)) {
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
	}

	@Override
	public Map<Nurse, List<Solluction>> getSolucao() {
		return solucao;
	}

}
