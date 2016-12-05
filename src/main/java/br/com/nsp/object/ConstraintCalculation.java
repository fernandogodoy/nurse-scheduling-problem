package br.com.nsp.object;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import br.com.nsp.configuration.object.Instancy;
import br.com.nsp.configuration.object.Preference;
import br.com.nsp.configuration.object.Solluction;

public class ConstraintCalculation {

	public static BigDecimal calcular(Instancy instancia, Map<Nurse, List<Preference>> prefers,
			Map<Nurse, List<Solluction>> solucao) {

		BigDecimal custoSoft = calcularSoftConstraint(instancia, prefers, solucao);
		BigDecimal custoHard = calcularHardConstraint(instancia, solucao);
		BigDecimal custoTotal = custoHard.add(custoSoft);

		return custoTotal;
	}

	public static final BigDecimal calcularSoftConstraint(Instancy instancia, Map<Nurse, List<Preference>> prefers,
			Map<Nurse, List<Solluction>> solucao) {
		Constraint constraint = new SoftConstraint(instancia, prefers, solucao);
		return constraint.calcular();
	}
	
	public static final BigDecimal calcularHardConstraint(Instancy instancia, Map<Nurse, List<Solluction>> solucao) {
		Constraint constraint =  new HardConstraint(solucao, instancia);
		return constraint.calcular();
	}
}
