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

public class Penalty {

	private Map<Nurse, List<Solluction>> solucao;
	private Map<Nurse, List<Preference>> preferencias;
	
	private int nurseCount;
	private int contador;
	private int contadorConsecutivo;
	private BigDecimal totalPenalidade = BigDecimal.ZERO;
	private BigDecimal penalidade = BigDecimal.ZERO;
	private WorkedSequence workedSequence;
	private AttributionSequence attributionSequence;
	private MorningShift morningShift;
	private AfternoonShift afternoonShift;
	private NightShift nightShift;
	private DayOff dayOff;

	public Penalty(Instancy instancia, Map<Nurse, List<Preference>> preferencias, Map<Nurse, List<Solluction>> solucao) {
		this.preferencias = preferencias;
		this.solucao = solucao;
		workedSequence = instancia.getWorkedSequence();
		attributionSequence = instancia.getAttributionSequence();
		morningShift = instancia.getMorningShift();
		afternoonShift = instancia.getAfternoonShift();
		nightShift = instancia.getNightShift();
		dayOff = instancia.getDayOff();
	}
	
	public BigDecimal calcular(){
		totalPenalidade = totalPenalidade.add(penalizarPreferencias());
		totalPenalidade = totalPenalidade.add(penalizarLimiteDiasTrabalhados());
		totalPenalidade = totalPenalidade.add(penalizarAtribuicoesConsecutivas());
		totalPenalidade = totalPenalidade.add(penalizarTurnoManha());
		totalPenalidade = totalPenalidade.add(penalizarTurnoTarde());
		totalPenalidade = totalPenalidade.add(penalizarTurnoNoite());
		totalPenalidade = totalPenalidade.add(penalizarFolga());
		return totalPenalidade;
	}
	
	private BigDecimal penalizarTurnoManha() {
		penalidade = BigDecimal.ZERO;
		nurseCount =1;
		for(; nurseCount < solucao.size(); nurseCount ++){
			List<Solluction> listSols = getSolucoes(nurseCount);
			long count = listSols.parallelStream()
								.filter(sol -> sol.getTurno().equals(Shift.M))
								.count();
			if (count < morningShift.getMin() || count > morningShift.getMax()) {
				penalidade = penalidade.add(Config.getValorPenalidadeRigida());
			}
			
			contador = 0;
			contadorConsecutivo = 0;
			for (Solluction solluction : listSols) {
				if (solluction.getTurno().equals(Shift.M)) {
					contador++;
				} else {
					if(contador > 1){
						contadorConsecutivo = contador > contadorConsecutivo ? contador : contadorConsecutivo;
					}
					contador = 0;
				}
			}
			if (contadorConsecutivo < morningShift.getMinConsecutivas() || contadorConsecutivo > morningShift.getMaxConsecutivas()) {
				penalidade = penalidade.add(Config.getValorPenalidadeRigida());
			}
			
		}
		return penalidade;
	}
	
	private BigDecimal penalizarTurnoTarde() {
		penalidade = BigDecimal.ZERO;
		nurseCount =1;
		for(; nurseCount < solucao.size(); nurseCount ++){
			List<Solluction> listSols = getSolucoes(nurseCount);
			long count = listSols.parallelStream()
								.filter(sol -> sol.getTurno().equals(Shift.T))
								.count();
			if (count < afternoonShift.getMin() || count > afternoonShift.getMax()) {
				penalidade = penalidade.add(Config.getValorPenalidadeRigida());
			}
			
			contador = 0;
			contadorConsecutivo = 0;
			for (Solluction solluction : listSols) {
				if (solluction.getTurno().equals(Shift.T)) {
					contador++;
				} else {
					if(contador > 1){
						contadorConsecutivo = contador > contadorConsecutivo ? contador : contadorConsecutivo;
					}
					contador = 0;
				}
			}
			if (contadorConsecutivo < afternoonShift.getMinConsecutivas() || contadorConsecutivo > afternoonShift.getMaxConsecutivas()) {
				penalidade = penalidade.add(Config.getValorPenalidadeRigida());
			}
			
		}
		return penalidade;
	}
	
	private BigDecimal penalizarTurnoNoite() {
		penalidade = BigDecimal.ZERO;
		nurseCount =1;
		for(; nurseCount < solucao.size(); nurseCount ++){
			List<Solluction> listSols = getSolucoes(nurseCount);
			long count = listSols.parallelStream()
								.filter(sol -> sol.getTurno().equals(Shift.N))
								.count();
			if (count < nightShift.getMin() || count > nightShift.getMax()) {
				penalidade = penalidade.add(Config.getValorPenalidadeRigida());
			}
			
			contador = 0;
			contadorConsecutivo = 0;
			for (Solluction solluction : listSols) {
				if (solluction.getTurno().equals(Shift.N)) {
					contador++;
				} else {
					if(contador > 1){
						contadorConsecutivo = contador > contadorConsecutivo ? contador : contadorConsecutivo;
					}
					contador = 0;
				}
			}
			if (contadorConsecutivo < nightShift.getMinConsecutivas() || contadorConsecutivo > nightShift.getMaxConsecutivas()) {
				penalidade = penalidade.add(Config.getValorPenalidadeRigida());
			}
			
		}
		return penalidade;
	}
	
	private BigDecimal penalizarFolga() {
		penalidade = BigDecimal.ZERO;
		nurseCount =1;
		for(; nurseCount < solucao.size(); nurseCount ++){
			List<Solluction> listSols = getSolucoes(nurseCount);
			long count = listSols.parallelStream()
								.filter(sol -> sol.getTurno().equals(Shift.F))
								.count();
			if (count < dayOff.getMin() || count > dayOff.getMax()) {
				penalidade = penalidade.add(Config.getValorPenalidadeRigida());
			}
			
			contador = 0;
			contadorConsecutivo = 0;
			for (Solluction solluction : listSols) {
				if (solluction.getTurno().equals(Shift.F)) {
					contador++;
				} else {
					if(contador > 1){
						contadorConsecutivo = contador > contadorConsecutivo ? contador : contadorConsecutivo;
					}
					contador = 0;
				}
			}
			if (contadorConsecutivo < dayOff.getMinConsecutivas() || contadorConsecutivo > dayOff.getMaxConsecutivas()) {
				penalidade = penalidade.add(Config.getValorPenalidadeRigida());
			}
			
		}
		return penalidade;
	}

	public BigDecimal penalizarAtribuicoesConsecutivas() {
		penalidade = BigDecimal.ZERO;
		nurseCount = 1;
		for (; nurseCount < solucao.size(); nurseCount++) {
			List<Solluction> listSols = getSolucoes(nurseCount);
			contador = 0;
			
			for (Solluction solluction : listSols) {
				if (solluction.getTurno().equals(Shift.F)) {
					contador++;
				} else {
					if(contador > 1){
						contadorConsecutivo = contador > contadorConsecutivo ? contador : contadorConsecutivo;
					}
					contador = 0;
				}
			}
			
			if (contadorConsecutivo < attributionSequence.getMin() || contadorConsecutivo > attributionSequence.getMax()) {
				penalidade = penalidade.add(Config.getValorPenalidadeRigida());
			}
		}

		return penalidade;
	}

	public BigDecimal penalizarLimiteDiasTrabalhados(){
		penalidade = BigDecimal.ZERO;
		nurseCount =1;
		for(; nurseCount < solucao.size(); nurseCount ++){
			List<Solluction> listSols = getSolucoes(nurseCount);
			long count = listSols.parallelStream()
								.filter(sol -> !sol.getTurno().equals(Shift.F))
								.count();
			if (count < workedSequence.getMin() || count > workedSequence.getMax()) {
				penalidade = penalidade.add(Config.getValorPenalidadeRigida());
			}
		}
		return penalidade;
	}
	
	public BigDecimal penalizarPreferencias(){
		penalidade = BigDecimal.ZERO;
		Arrays.asList(Day.values()).forEach(dia ->{
			nurseCount =1;
			for(;nurseCount < solucao.size(); nurseCount++){
				List<Preference> listPrefs = getPreferencias(nurseCount);
				List<Solluction> listSols = getSolucoes(nurseCount);
				Preference preference = getPreferencia(listPrefs, dia);
				Solluction solluction = getSolucao(listSols, dia);
				if(!solluction.getTurno().equals(preference.getTurno())){
					penalidade = penalidade.add(new BigDecimal(preference.getPeso()));
				}
			}
		});;
		
		return penalidade;
	}

	private Preference getPreferencia(List<Preference> listPrefs, Day dia) {
		return listPrefs.parallelStream()
				.filter(pref -> pref.getEnfermeiro().getIdentificacao() == nurseCount)
				.filter(pref -> pref.getDia().equals(dia))
				.findFirst()
				.get();
	}

	private Solluction getSolucao(List<Solluction> listSols, Day dia) {
		return listSols.parallelStream()
				.filter(sol -> sol.getEnfermeiro().getIdentificacao() == nurseCount)
				.filter(sol -> sol.getDia().equals(dia))
				.findFirst()
				.get();
	}

	private List<Solluction> getSolucoes(Integer identificador) {
		return solucao.entrySet().parallelStream()
				.filter(sol -> sol.getKey().getIdentificacao() == identificador)
				.findFirst()
				.get()
				.getValue();
	}

	private List<Preference> getPreferencias(Integer identificador ) {
		return preferencias.entrySet().parallelStream()
				.filter(pre -> pre.getKey().getIdentificacao() == identificador)
				.findFirst()
				.get()
				.getValue();
	}

}
