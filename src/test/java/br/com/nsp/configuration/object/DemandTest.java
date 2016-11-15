package br.com.nsp.configuration.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import br.com.nsp.configuration.MannagerConfig;
import br.com.nsp.object.Day;
import br.com.nsp.object.Shift;

public class DemandTest {
	
	
	@Test
	public void testGetQtdDemandasPorDiaAndTurno() {
		MannagerConfig mc = new MannagerConfig();
		assertSame(3, mc.getQtdDemandaPorTurno(Day.SEG, Shift.M));
		assertSame(3, mc.getQtdDemandaPorTurno(Day.SEG, Shift.T));
		assertSame(2, mc.getQtdDemandaPorTurno(Day.SEG, Shift.N));
		assertSame(0, mc.getQtdDemandaPorTurno(Day.SEG, Shift.F));
	}
	
	@Test
	public void testSumDemandasPorDia() {
		MannagerConfig mc = new MannagerConfig();
		assertSame(8, mc.sumQtdDemanda(Day.SEG));
		assertSame(3, mc.sumQtdDemanda(Day.TER));
		assertSame(7, mc.sumQtdDemanda(Day.QUA));
		assertSame(4, mc.sumQtdDemanda(Day.QUI));
		assertSame(6, mc.sumQtdDemanda(Day.SEX));
		assertSame(3, mc.sumQtdDemanda(Day.SAB));
		assertSame(4, mc.sumQtdDemanda(Day.DOM));
	}
	
	@Test
	public void testGetDemandasPorDia() {
		MannagerConfig mc = new MannagerConfig();
		assertEquals(4, mc.getDemandas(Day.SEG).size());
	}

	@Test
	public void testLoadDemandas() {
		MannagerConfig mc = new MannagerConfig();
		assertEquals(7, mc.getDemandas().size());
	}

}
