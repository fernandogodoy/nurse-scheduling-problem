package br.com.nsp.object;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.com.nsp.configuration.Config;
import br.com.nsp.configuration.object.Solluction;

public class HardConstraint implements Constraint {

	private Map<Nurse, List<Solluction>> solucao;
	
	private BigDecimal penalidade = BigDecimal.ZERO;

	public HardConstraint(Map<Nurse, List<Solluction>> solucao) {
		this.solucao = solucao;
	}
	
	@Override
	public BigDecimal calcular(){
		penalidade = BigDecimal.ZERO;
		for(int nurseCount = 1; nurseCount < solucao.size(); nurseCount ++){
			List<Solluction> listSols = getSolucoesPorEnfermeiro(nurseCount);
			String anterior = null;
			for (Solluction solluction : listSols) {
				Shift atual = solluction.getTurno();
				if(anterior != null && !anterior.equals(atual)){
					penalizarDia(atual, Shift.valueOf(anterior));
				}
				anterior = atual.name();
			}
			
		}
		return penalidade;
	}

	private void penalizarDia(Shift atual, Shift anterior) {
		if(atual.equals(Shift.M) && anterior.equals(Shift.T)){
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
		
		if(atual.equals(Shift.M) && anterior.equals(Shift.N)){
			penalidade = penalidade.add(Config.getValorPenalidadeRigida()); 	
		}
		
		if(atual.equals(Shift.T) && anterior.equals(Shift.N)){
			penalidade = penalidade.add(Config.getValorPenalidadeRigida());
		}
	}

	@Override
	public Map<Nurse, List<Solluction>> getSolucao() {
		return solucao;
	}
	
	
}
