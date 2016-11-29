package br.com.nsp.object;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.com.nsp.configuration.object.Solluction;

public interface Constraint {
	
	
	public Map<Nurse, List<Solluction>> getSolucao();
	

	public BigDecimal calcular();
	
	public default List<Solluction> getSolucoesPorEnfermeiro(Integer identificador) {
		return getSolucao().entrySet().parallelStream()
				.filter(sol -> sol.getKey().getIdentificacao() == identificador)
				.findFirst()
				.get()
				.getValue();
	}

}
