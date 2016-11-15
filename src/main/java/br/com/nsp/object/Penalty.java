package br.com.nsp.object;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.nsp.configuration.Config;
import br.com.nsp.configuration.object.Instancy;
import br.com.nsp.configuration.object.Preference;
import br.com.nsp.configuration.object.Solluction;
import br.com.nsp.object.features.AttributionSequence;
import br.com.nsp.object.features.WorkedSequence;

public class Penalty {

	private Map<Nurse, List<Solluction>> solucao;
	private Map<Nurse, List<Preference>> preferencias;
	
	private int nurseCount;
	private int contador;
	private BigDecimal totalPenalidade = BigDecimal.ZERO;
	private BigDecimal penalidade = BigDecimal.ZERO;
	private WorkedSequence workedSequence;
	private AttributionSequence attributionSequence;

	public Penalty(Instancy instancia, Map<Nurse, List<Preference>> preferencias, Map<Nurse, List<Solluction>> solucao) {
		this.preferencias = preferencias;
		this.solucao = solucao;
		workedSequence = instancia.getWorkedSequence();
		attributionSequence = instancia.getAttributionSequence();
	}
	
	public BigDecimal calcular(){
		totalPenalidade = totalPenalidade.add(penalizarPreferencias());
		totalPenalidade = totalPenalidade.add(penalizarLimiteDiasTrabalhados());
		totalPenalidade = totalPenalidade.add(penalizarAtribuicoesConsecutivas());
		return totalPenalidade;
	}
	
	public BigDecimal penalizarAtribuicoesConsecutivas() {
		penalidade = BigDecimal.ZERO;
		nurseCount = 1;
		for (; nurseCount < solucao.size(); nurseCount++) {
			List<Solluction> listSols = getSolucoes(nurseCount);
			contador = 0;
			listSols.forEach(sol -> {
				if (sol.getTurno().equals(Shift.F) && (contador <= attributionSequence.getMin())) {
					penalidade = penalidade.add(Config.getValorPenalidadeRigida());
					contador = 0;
				} else if (!sol.getTurno().equals(Shift.F) && (contador > attributionSequence.getMax())) {
					penalidade = penalidade.add(Config.getValorPenalidadeRigida());
					contador = 0;
				} else if (sol.getTurno().equals(Shift.F)
						&& (contador > attributionSequence.getMin() && contador < attributionSequence.getMax())) {
					contador = 0;
				}
				contador++;
			});
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
