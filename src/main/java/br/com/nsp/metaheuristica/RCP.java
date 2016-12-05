package br.com.nsp.metaheuristica;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.nsp.configuration.object.Instancy;
import br.com.nsp.configuration.object.Preference;
import br.com.nsp.configuration.object.Solluction;
import br.com.nsp.object.ConstraintCalculation;
import br.com.nsp.object.Day;
import br.com.nsp.object.Nurse;
import br.com.nsp.util.Util;

/**
 * Recombination and Cuts Procedure
 * 
 * @author Fernando
 *
 */
public class RCP {

	private static final int FIRST = 0;
	private Map<Nurse, List<Solluction>> solucao;
	private BigDecimal custoSolucaoInicial;
	private Instancy instancia;
	private Map<Nurse, List<Preference>> prefers;
	private List<Map<Nurse, List<Solluction>>> solucoes = new ArrayList<>();

	public RCP(Map<Nurse, List<Solluction>> solucao, Map<Nurse, List<Preference>> prefers, Instancy instancia) {
		this.solucao = solucao;
		this.prefers = prefers;
		this.instancia = instancia;
		this.custoSolucaoInicial = ConstraintCalculation.calcular(instancia, prefers, solucao);
	}

	public Map<Nurse, List<Solluction>> aplicar() {
		Arrays.asList(Day.values()).forEach(dia -> {
			Optional<Day> optional = Day.getDay(dia.ordinal() + 1);
			if (optional.isPresent()) {
				Day nextDay = optional.get();
				solucao.entrySet().forEach(mSoluc -> {
					Solluction solucaoAtual = getSolucaoAtual(mSoluc.getKey(), dia);
					solucao.entrySet().forEach(mSol -> {
						Map<Nurse, List<Solluction>> mapSolucoes = new LinkedHashMap<>(solucao);
						mapSolucoes.entrySet().stream().filter(f -> f.getKey().equals(mSol.getKey())).forEach(sols -> {
							sols.getValue().stream().filter(sol -> sol.getDia().equals(nextDay)).forEach(solDay -> {
								solDay.setTurno(solucaoAtual.getTurno());
								solucoes.add(mapSolucoes);
							});
						});
					});
				});
			}
		});
		return getMelhorSolucao(solucoes);
	}

	private Map<Nurse, List<Solluction>> getMelhorSolucao(List<Map<Nurse, List<Solluction>>> solucoes) {
		solucoes.forEach(sol -> {
			Util.printToConsole(sol);
			BigDecimal custo = ConstraintCalculation.calcular(instancia, prefers, sol);
			if (custo.compareTo(custoSolucaoInicial) == -1) {
				custoSolucaoInicial = custo;
				solucao = sol;
			}
		});

		return solucao;
	}

	private Solluction getSolucaoAtual(Nurse nurse, Day dia) {
		List<Solluction> listSol = solucao.get(nurse);
		List<Solluction> segundaDia = getSolucaoByDia(listSol, dia);
		return segundaDia.get(FIRST);
	}

	private List<Solluction> getSolucaoByDia(List<Solluction> sols, Day seg) {
		return sols.stream()
				.filter(s -> s.getDia().ordinal() == seg.ordinal())
				.collect(Collectors.toList());
	}
}
