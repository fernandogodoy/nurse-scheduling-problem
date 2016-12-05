package br.com.nsp.metaheuristica;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.nsp.configuration.Config;
import br.com.nsp.configuration.MannagerConfig;
import br.com.nsp.configuration.object.Instancy;
import br.com.nsp.configuration.object.Preference;
import br.com.nsp.configuration.object.Solluction;
import br.com.nsp.object.ConstraintCalculation;
import br.com.nsp.object.Nurse;
import br.com.nsp.util.Util;

public class SimulatedAnnealing {

	public static void main(String[] args) {
		
		Instant inicio = Instant.now();
		
		SimulatedAnnealing sa = new SimulatedAnnealing();
		MannagerConfig mConfig = new MannagerConfig();
		
		Instancy instancia = mConfig.getInstancia("1.gen");
		Map<Nurse, List<Preference>> prefers = mConfig.getPreferencias();
		
		Map<Nurse, List<Solluction>> melhorSolucao = new HashMap<>();
		BigDecimal custoMelhorSolucao = new BigDecimal("10000000");
		
		System.out.println("======================================================");
		System.out.println("Iniciando Resolução instância: " + instancia.getFileName());
		
		Map<Nurse, List<Solluction>> solucao = instancia.gerarSolucao();
		BigDecimal custoTotal = ConstraintCalculation.calcular(instancia, prefers, solucao);
		
		melhorSolucao = solucao;
		custoMelhorSolucao = custoTotal;
		
		System.out.println("Custo da Solução Inicial: " + custoTotal);
		System.out.println("======================================================");
		
		double temperaturaInicial = Config.getTemperaturaInicial();
		double temperaturaMinima = Config.getTemperaturaMinima();
		double taxaResfriamento = Config.getTaxaResfriamento();
		
		while (temperaturaInicial > temperaturaMinima) {
			int iteracoes = Config.getNumeroDeIteracoes();
			while (iteracoes >= 1) {

				Map<Nurse, List<Solluction>> novaSolucao = instancia.gerarSolucao();
				BigDecimal custoTotalNovaSolucao = ConstraintCalculation.calcular(instancia, prefers, novaSolucao);

				if (sa.probabilidadeAceitacao(custoTotal, custoTotalNovaSolucao, temperaturaInicial) > Math.random()) {
					custoTotal = custoTotalNovaSolucao;
					solucao = novaSolucao;
				}

				if (custoTotal.compareTo(custoMelhorSolucao) == -1) {
					System.out.println("Removeu: " + custoMelhorSolucao.subtract(custoTotal) + " penalidades");
					custoMelhorSolucao = custoTotal;
					melhorSolucao = solucao;
					temperaturaInicial = Config.getTemperaturaInicial();
					iteracoes = Config.getNumeroDeIteracoes();
				}
				iteracoes--;
			}
			temperaturaInicial *= temperaturaInicial * taxaResfriamento;
		}

		Instant fim = Instant.now();
		System.out.println("======================================================");
		System.out.println("Total Penalidades= " + custoMelhorSolucao);
		System.out.println("Tempo gasto em segundos:" + Duration.between(inicio, fim).getSeconds());
		System.out.println("======================================================");
		
		Util.printToConsole(melhorSolucao);
	}

	/**
	 * Fator de Boltzmann
	 * 
	 * @param custoAtual
	 * @param custoNovaSolucao
	 * @param temperatura
	 * @return
	 */
	
	public double probabilidadeAceitacao(BigDecimal custoAtual, BigDecimal custoNovaSolucao, double temperatura) {

		if (custoAtual.compareTo(custoNovaSolucao) == 1) {
			return 1.0;
		}

		long atual = custoAtual.longValue();
		long novoCusto = custoNovaSolucao.longValue();
		return Math.exp((atual - novoCusto) / temperatura);

	}

}
