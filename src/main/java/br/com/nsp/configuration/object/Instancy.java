package br.com.nsp.configuration.object;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.nsp.configuration.Config;
import br.com.nsp.configuration.MannagerConfig;
import br.com.nsp.configuration.object.file.FileGenLayout;
import br.com.nsp.object.Day;
import br.com.nsp.object.Nurse;
import br.com.nsp.object.Shift;
import br.com.nsp.object.features.AfternoonShift;
import br.com.nsp.object.features.AttributionSequence;
import br.com.nsp.object.features.DayOff;
import br.com.nsp.object.features.MorningShift;
import br.com.nsp.object.features.NightShift;
import br.com.nsp.object.features.Problem;
import br.com.nsp.object.features.WorkedSequence;

public class Instancy {

	private String fileName;

	private Problem problem;
	private WorkedSequence workedSequence;
	private AttributionSequence attributionSequence;
	private MannagerConfig mConfig;
	private MorningShift morningShift;
	private AfternoonShift afternoonShift;
	private NightShift nightShift;
	private DayOff dayOff;

	public Instancy(FileGenLayout layout, MannagerConfig mConfig) {
		this.mConfig = mConfig;
		this.fileName = layout.getFileName();
		this.problem = layout.getProblem();
		this.workedSequence = layout.getWorkedSequence();
		this.attributionSequence = layout.getAttributionSequence();
		this.morningShift = layout.getMorningShift();
		afternoonShift = layout.getAfternoonShift();
		nightShift = layout.getNightShift();
		dayOff = layout.getDayOff();
	}

	public Map<Nurse, List<Solluction>> gerarSolucao() {
		Map<Day, Map<Shift, List<Nurse>>> map = designarDiasTrabalho();
		return criarQuadroTrabalho(map);
	}

	private Map<Nurse, List<Solluction>> criarQuadroTrabalho(Map<Day, Map<Shift, List<Nurse>>> map) {
		List<Solluction> solucoes = new LinkedList<>();
		for (Nurse nurse : mConfig.getEnfermeiros()) {
			map.entrySet().forEach(mdia ->{
				mdia.getValue().entrySet().forEach(mturno ->{
					List<Nurse> list = mturno.getValue();
					if(list.contains(nurse)){
						solucoes.add(new Solluction(nurse, mdia.getKey(), mturno.getKey()));
					}
				});
			});
		}
		return solucoes.parallelStream()
				.collect(groupingBy(Solluction::getEnfermeiro));
	}

	private Map<Day, Map<Shift, List<Nurse>>> designarDiasTrabalho() {
		Map<Day, Map<Shift, List<Nurse>>> mDays = new LinkedHashMap<>(); 
		
		List<Shift> turnos = Arrays.asList(Shift.values());
		Arrays.asList(Day.values()).forEach(dia -> {
			Map<Shift, List<Nurse>> map = designarTurnosPorDia(dia, turnos);
			mDays.put(dia, map);
		});
		
		return mDays;
	}

	private Map<Shift, List<Nurse>> designarTurnosPorDia(Day dia, List<Shift> turnos) {
		Map<Shift, List<Nurse>> map = new HashMap<>();
		List<Nurse> enfermeiros = new ArrayList<>(mConfig.getEnfermeiros());
		
		int qtdTurnosExtras = calcularQtdTurnosExtras(dia);
		Integer qtdDemanda = mConfig.sumQtdDemanda(dia);
		
		while (enfermeiros.size() > qtdDemanda && (qtdTurnosExtras > 0 || map.isEmpty())) {
				turnos.forEach(turno -> {
					Collections.shuffle(enfermeiros);
					List<Nurse> selecionados = selecionarEnfermeirosPorDemanda(dia, enfermeiros, turno);
					enfermeiros.removeAll(selecionados);
					List<Nurse> list = map.get(turno);
					if (list == null) {
						list = new ArrayList<>();
					}
					list.addAll(selecionados);
					map.put(turno, list);
				});
			qtdTurnosExtras--;
		}
		map.put(Shift.F, enfermeiros);
		
		return map;
	}

	private List<Nurse> selecionarEnfermeirosPorDemanda(Day dia, List<Nurse> enfermeiros, Shift turno) {
		return enfermeiros.parallelStream()
				.limit(mConfig.getQtdDemandaPorTurno(dia, turno))
				.collect(toList());
	}

	/**
	 * Calcula quantos turnos extras serão gerados. 
	 * 
	 * @param dia
	 * @return
	 */
	private int calcularQtdTurnosExtras(Day dia) {
		int qtdTotalDemandaDia = mConfig.sumQtdDemanda(dia);
		int qtdEnfermeiros = Config.getQtdEnfermeiros();
		return (qtdEnfermeiros / qtdTotalDemandaDia) -1;
	}

	public String getFileName() {
		return fileName;
	}

	public WorkedSequence getWorkedSequence() {
		return workedSequence;
	}

	public Problem getProblem() {
		return problem;
	}

	public AttributionSequence getAttributionSequence() {
		return attributionSequence;
	}

	public MorningShift getMorningShift() {
		return morningShift;
	}

	public AfternoonShift getAfternoonShift() {
		return afternoonShift;
	}

	public NightShift getNightShift() {
		return nightShift;
	}

	public DayOff getDayOff() {
		return dayOff;
	}

}
