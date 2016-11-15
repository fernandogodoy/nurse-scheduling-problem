package br.com.nsp.configuration.object;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import br.com.nsp.configuration.MannagerConfig;
import br.com.nsp.object.Nurse;

public class InstancyTest {

	@Test
	public void testGerarSolucao(){
		MannagerConfig mc = new MannagerConfig();
		Instancy instancia = mc.getInstancia("1.gen");
		Map<Nurse, List<Solluction>> solucao = instancia.gerarSolucao();
		assertEquals(25, solucao.size());
	}
	
	@Test
	public void testCriacaoInstancias() {
		MannagerConfig mc = new MannagerConfig();
		List<Instancy> instancias = mc.getInstancias();
		assertEquals(8, instancias.size());
		
		
	}

}
