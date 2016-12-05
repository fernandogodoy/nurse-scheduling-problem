package br.com.nsp.metaheuristica;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import br.com.nsp.configuration.MannagerConfig;
import br.com.nsp.configuration.object.Instancy;
import br.com.nsp.configuration.object.Preference;
import br.com.nsp.configuration.object.Solluction;
import br.com.nsp.object.ConstraintCalculation;
import br.com.nsp.object.Nurse;
import br.com.nsp.util.Util;

public class RCPTest {
	
	MannagerConfig mc;
	Instancy instancia ;
	Map<Nurse, List<Solluction>> solucao;
	Map<Nurse, List<Preference>> preferencias;
	
	@Before
	public void init(){
		mc = new MannagerConfig();
		instancia = mc.getInstancia("1.gen");
		solucao = instancia.gerarSolucao();
		preferencias = mc.getPreferencias();
	}

	@Test
	public void test() {

		Util.printToConsole(solucao);
		BigDecimal custo1 = ConstraintCalculation.calcular(instancia, mc.getPreferencias(), solucao);

		RCP rcp = new RCP(solucao, preferencias, instancia);
		Map<Nurse, List<Solluction>> solucaoMelhorada = rcp.aplicar();
		assertEquals(25, solucaoMelhorada.size());
		
		BigDecimal custo2 =  ConstraintCalculation.calcular(instancia, mc.getPreferencias(), solucaoMelhorada);
		assertTrue("Custo da solução não foi melhorado" , custo1.compareTo(custo2) == 1);
		Util.printToConsole(solucaoMelhorada);
		
	}

}
