package br.com.nsp.configuration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class Config {

	private final static List<String> genFiles = Arrays.asList("1.gen", "2.gen", "3.gen", "4.gen", "5.gen", "6.gen", "7.gen",
			"8.gen");
	
	private static final List<String> nspFiles = Arrays.asList("1.nsp");

	// =================== ENFERMEIROS ============================

	private static int qtdEnfermeiros = 25;

	// =================== ALGORITMO ============================

	private static double temperaturaInicial = 0.1;
	private static double temperaturaMinima = 0.000001;
	private static int numeroDeIteracoes = 1000;
	private static double taxaResfriamento = 0.3;

	// =================== PENALIDADES ============================

	private static BigDecimal valorPenalidadeRigida = new BigDecimal("1000");
	private static BigDecimal valorPenalidadeLeve = new BigDecimal("100");

	// ==============================================================

	public static BigDecimal getValorPenalidadeRigida() {
		return valorPenalidadeRigida;
	}

	public static BigDecimal getValorPenalidadeLeve() {
		return valorPenalidadeLeve;
	}

	public static double getTaxaResfriamento() {
		return taxaResfriamento;
	}

	public static int getNumeroDeIteracoes() {
		return numeroDeIteracoes;
	}

	public static double getTemperaturaMinima() {
		return temperaturaMinima;
	}

	public static double getTemperaturaInicial() {
		return temperaturaInicial;
	}

	public static int getQtdEnfermeiros() {
		return qtdEnfermeiros;
	}

	public static List<String> getGenFiles() {
		return genFiles;
	}
	
	public static List<String> getNspfiles() {
		return nspFiles;
	}

}
