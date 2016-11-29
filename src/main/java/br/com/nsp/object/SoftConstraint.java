package br.com.nsp.object;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.nsp.configuration.Config;
import br.com.nsp.configuration.object.Instancy;
import br.com.nsp.configuration.object.Preference;
import br.com.nsp.configuration.object.Solluction;
import br.com.nsp.object.features.AfternoonShift;
import br.com.nsp.object.features.AttributionSequence;
import br.com.nsp.object.features.DayOff;
import br.com.nsp.object.features.MorningShift;
import br.com.nsp.object.features.NightShift;
import br.com.nsp.object.features.WorkedSequence;

public class SoftConstraint implements Constraint {

	private Map<Nurse, List<Solluction>> solucao;
	private Map<Nurse, List<Preference>> preferencias;
	
	private BigDecimal totalPenalidade = BigDecimal.ZERO;
	private BigDecimal penalidade = BigDecimal.ZERO;
	private WorkedSequence workedSequence;
	private AttributionSequence attributionSequence;
	private MorningShift morningShift;
	private AfternoonShift afternoonShift;
	private NightShift nightShift;
	private DayOff dayOff;

	public SoftConstraint(Instancy instancia, Map<Nurse, List<Preference>> preferencias, Map<Nurse, List<Solluction>> solucao) {
		this.preferencias = preferencias;
		this.solucao = solucao;
		workedSequence = instancia.getWorkedSequence();
		attributionSequence = instancia.getAttributionSequence();
		morningShift = instancia.getMorningShift();
		afternoonShift = instancia.getAfternoonShift();
		nightShift = instancia.getNightShift();
		dayOff = instancia.getDayOff();
	}
	
	@Override
	public BigDecimal calcular(){
		totalPenalidade = totalPenalidade.add(penalizarPreferencias());
		totalPenalidade = totalPenalidade.add(penalizarLimiteDiasTrabalhados());
		totalPenalidade = totalPenalidade.add(penalizarTurno());
		return totalPenalidade;
	}
	
	private BigDecimal penalizarTurno() {
		penalidade = BigDecimal.ZERO;
		for(int nurseCount =1; nurseCount < solucao.size(); nurseCount ++){
			List<Solluction> listSols = getSolucoesPorEnfermeiro(nurseCount);
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
			
			if (atribuicaoConsecutiva < attributionSequence.getMin() || atribuicaoConsecutiva > attributionSequence.getMax()) {
				penalidade = penalidade.add(Config.getValorPenalidadeLeve());
			}
			if (manhaConsecutiva < morningShift.getMinConsecutivas() || manhaConsecutiva > morningShift.getMaxConsecutivas()) {
				penalidade = penalidade.add(Config.getValorPenalidadeLeve());
			}
			if (tardeConsecutiva < afternoonShift.getMinConsecutivas() || tardeConsecutiva > afternoonShift.getMaxConsecutivas()) {
				penalidade = penalidade.add(Config.getValorPenalidadeLeve());
			}
			if (noiteConsecutiva < nightShift.getMinConsecutivas() || noiteConsecutiva > nightShift.getMaxConsecutivas()) {
				penalidade = penalidade.add(Config.getValorPenalidadeLeve());
			}
			if (folgaConsecutiva < dayOff.getMinConsecutivas() || folgaConsecutiva > dayOff.getMaxConsecutivas()) {
				penalidade = penalidade.add(Config.getValorPenalidadeLeve());
			}
			
		}
		return penalidade;
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
			penalidade = penalidade.add(Config.getValorPenalidadeLeve());
		}
	}
	
	private void penalizarMinMaxNoite(List<Solluction> listSols) {
		long count = countTurnos(listSols, Shift.N);
		if (count < nightShift.getMin() || count > nightShift.getMax()) {
			penalidade = penalidade.add(Config.getValorPenalidadeLeve());
		}
	}
	
	private void penalizarMinMaxTarde(List<Solluction> listSols) {
		long count = countTurnos(listSols, Shift.T);
		if (count < afternoonShift.getMin() || count > afternoonShift.getMax()) {
			penalidade = penalidade.add(Config.getValorPenalidadeLeve());
		}
	}

	private void penalizarMinMaxManha(List<Solluction> listSols) {
		long count = countTurnos(listSols, Shift.M);
		if (count < morningShift.getMin() || count > morningShift.getMax()) {
			penalidade = penalidade.add(Config.getValorPenalidadeLeve());
		}
	}

	private long countTurnos(List<Solluction> listSols, Shift shift) {
		long count = listSols.parallelStream()
							.filter(sol -> sol.getTurno().equals(shift))
							.count();
		return count;
	}

	public BigDecimal penalizarLimiteDiasTrabalhados(){
		penalidade = BigDecimal.ZERO;
		for(int nurseCount = 1; nurseCount < solucao.size(); nurseCount ++){
			List<Solluction> listSols = getSolucoesPorEnfermeiro(nurseCount);
			long count = listSols.parallelStream()
								.filter(sol -> !sol.getTurno().equals(Shift.F))
								.count();
			if (count < workedSequence.getMin() || count > workedSequence.getMax()) {
				penalidade = penalidade.add(Config.getValorPenalidadeLeve());
			}
		}
		return penalidade;
	}
	
	public BigDecimal penalizarPreferencias(){
		penalidade = BigDecimal.ZERO;
		Arrays.asList(Day.values()).forEach(dia ->{
			for(int nurseCount = 1;nurseCount < solucao.size(); nurseCount++){
				List<Preference> listPrefs = getPreferencias(nurseCount);
				List<Solluction> listSols = getSolucoesPorEnfermeiro(nurseCount);
				Preference preference = getPreferencia(listPrefs, dia, nurseCount);
				Solluction solluction = getSolucao(listSols, dia, nurseCount);
				if(!solluction.getTurno().equals(preference.getTurno())){
					penalidade = penalidade.add(new BigDecimal(preference.getPeso()));
				}
			}
		});
		
		return penalidade;
	}

	private Preference getPreferencia(List<Preference> listPrefs, Day dia, int nurseCount) {
		return listPrefs.parallelStream()
				.filter(pref -> pref.getEnfermeiro().getIdentificacao() == nurseCount)
				.filter(pref -> pref.getDia().equals(dia))
				.findFirst()
				.get();
	}

	private Solluction getSolucao(List<Solluction> listSols, Day dia, int nurseCount) {
		return listSols.parallelStream()
				.filter(sol -> sol.getEnfermeiro().getIdentificacao() == nurseCount)
				.filter(sol -> sol.getDia().equals(dia))
				.findFirst()
				.get();
	}


	private List<Preference> getPreferencias(Integer identificador ) {
		return preferencias.entrySet().parallelStream()
				.filter(pre -> pre.getKey().getIdentificacao() == identificador)
				.findFirst()
				.get()
				.getValue();
	}

	@Override
	public Map<Nurse, List<Solluction>> getSolucao() {
		return solucao;
	}
	

}
