package br.com.nsp.object;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import br.com.nsp.configuration.Config;
import br.com.nsp.configuration.object.Instancy;
import br.com.nsp.configuration.object.Preference;
import br.com.nsp.configuration.object.Solluction;
import br.com.nsp.object.features.WorkedSequence;

public class SoftConstraint implements Constraint {

	private Map<Nurse, List<Solluction>> solucao;
	private Map<Nurse, List<Preference>> preferencias;
	
	private BigDecimal totalPenalidade = BigDecimal.ZERO;
	private BigDecimal penalidade = BigDecimal.ZERO;
	private WorkedSequence workedSequence;


	public SoftConstraint(Instancy instancia, Map<Nurse, List<Preference>> preferencias, Map<Nurse, List<Solluction>> solucao) {
		this.preferencias = preferencias;
		this.solucao = solucao;
		workedSequence = instancia.getWorkedSequence();

	}
	
	@Override
	public BigDecimal calcular(){
		totalPenalidade = totalPenalidade.add(penalizarPreferencias());
		totalPenalidade = totalPenalidade.add(penalizarLimiteDiasTrabalhados());
		return totalPenalidade;
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
