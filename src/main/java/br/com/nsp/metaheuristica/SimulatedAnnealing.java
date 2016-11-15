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
import br.com.nsp.object.Nurse;
import br.com.nsp.object.Penalty;

public class SimulatedAnnealing {

	public static void main(String[] args) {
		SimulatedAnnealing sa = new SimulatedAnnealing();
		MannagerConfig mConfig = new MannagerConfig();
		Instancy instancia = mConfig.getInstancia("1.gen");
		Map<Nurse, List<Preference>> prefers = mConfig.getPreferencias();
		
		Map<Nurse, List<Solluction>> melhorSolucao = new HashMap<>();
		BigDecimal custoMelhorSolucao = new BigDecimal("10000000");
		
		System.out.println("======================================================");
		System.out.println("Iniciando Resolução instância: " + instancia.getFileName());
		
		Map<Nurse, List<Solluction>> solucao = instancia.gerarSolucao();
		Penalty penalidade = new Penalty(instancia, prefers, solucao);
		BigDecimal custo = penalidade.calcular();
		
		melhorSolucao = solucao;
		custoMelhorSolucao = custo;
		
		System.out.println("Custo da Solução Inicial: " + custo);
		System.out.println("======================================================");
		
		double temperaturaInicial = Config.getTemperaturaInicial();
		double temperaturaMinima = Config.getTemperaturaMinima();
		double taxaResfriamento = Config.getTaxaResfriamento();
		
		Instant inicio = Instant.now();

		while (temperaturaInicial > temperaturaMinima) {
			int iteracoes = Config.getNumeroDeIteracoes();
			while (iteracoes >= 1) {

				Map<Nurse, List<Solluction>> novaSolucao = instancia.gerarSolucao();
				Penalty novaPenalidade = new Penalty(instancia, prefers, novaSolucao);
				BigDecimal custoNovaSolucao = novaPenalidade.calcular();

				if (sa.probabilidadeAceitacao(custo, custoNovaSolucao, temperaturaInicial) > Math.random()) {
					custo = custoNovaSolucao;
					solucao = novaSolucao;
				}

				if (custo.compareTo(custoMelhorSolucao) == -1) {
					System.out.println("Removeu: " + custoMelhorSolucao.subtract(custoNovaSolucao) + " penalidades");
					custoMelhorSolucao = custo;
					melhorSolucao = solucao;
					temperaturaInicial = Config.getTemperaturaInicial();
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
		sa.escreverSolucao(melhorSolucao);
	}
	
	private void escreverSolucao(Map<Nurse, List<Solluction>> solucao){
		StringBuilder sb = new StringBuilder();
		solucao.entrySet().stream().forEach(sol ->{
			sb.append(sol.getKey()).append(" [ ");
			sol.getValue().forEach(val ->{
				sb.append(val.getDia()).append(" - ").append(val.getTurno()).append(" / ");
			});
			sb.delete(sb.length() - 3, sb.length());
			sb.append(" ]\n");
			
		});
		System.out.println(sb.toString());
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
