package br.com.nsp.object;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import br.com.nsp.configuration.MannagerConfig;
import br.com.nsp.configuration.object.Instancy;

public class PenaltyTest {
	
	private Penalty penalty;

	@Before
	public void init(){
		MannagerConfig mc = new MannagerConfig();
		Instancy instancia = mc.getInstancia("1.gen");
		penalty = new Penalty(instancia, mc.getPreferencias(), instancia.gerarSolucao());
	}
	
	@Test
	public void testAtribuicoesConsecultivas(){
		BigDecimal valor = penalty.penalizarAtribuicoesConsecutivas();
		assertNotEquals(BigDecimal.ZERO, valor);
	}
	
	@Test
	public void testSequenciaDiasTrabalahados(){
		BigDecimal valor = penalty.penalizarLimiteDiasTrabalhados();
		assertNotEquals(BigDecimal.ZERO, valor);
	}

	@Test
	public void testPreferencias() {
		BigDecimal valor = penalty.penalizarPreferencias();
		assertNotEquals(BigDecimal.ZERO, valor);
	}

}
