package br.com.nsp.configuration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class MannagerConfigTest {
	
	
	@Test
	public void testGerarDemanda(){
		MannagerConfig mannagerConfig = new MannagerConfig();
		assertEquals(7, mannagerConfig.getDemandas().size());
		
	}
	
	@Test
	public void testGerarPreferencia() {
		MannagerConfig managerConfig = new MannagerConfig();
		assertEquals(25, managerConfig.getPreferencias().size());
	}
	
	@Test
	public void testGerarInstancia() {
		MannagerConfig managerConfig = new MannagerConfig();
		assertEquals(8, managerConfig.getGenFiles().size());
	}

	@Test
	public void testLoadFilesGen() {
		MannagerConfig managerConfig = new MannagerConfig();
		assertEquals(8, managerConfig.getGenFiles().size());
	}
	
	@Test
	public void testLoadNspGen() throws URISyntaxException, IOException {
		MannagerConfig managerConfig = new MannagerConfig();
		assertEquals(1, managerConfig.getNsfFiles().size());
	}
	
}
