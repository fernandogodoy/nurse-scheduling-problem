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
import java.util.Optional;

import br.com.nsp.configuration.Config;
import br.com.nsp.configuration.MannagerConfig;
import br.com.nsp.configuration.object.file.FileGenLayout;
import br.com.nsp.object.Alocation;
import br.com.nsp.object.Day;
import br.com.nsp.object.Nurse;
import br.com.nsp.object.Shift;
import br.com.nsp.object.TypeAlocation;
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
		this.afternoonShift = layout.getAfternoonShift();
		this.nightShift = layout.getNightShift();
		this.dayOff = layout.getDayOff();
	}

	public Map<Nurse, List<Solluction>> gerarSolucao() {
		Map<Day, Map<Shift, List<Alocation>>> map = designarDiasTrabalho();
		Map<Nurse, List<Solluction>> escala = criarQuadroTrabalho(map);
		return escala;
	}
	
	private Map<Nurse, List<Solluction>> criarQuadroTrabalho(Map<Day, Map<Shift, List<Alocation>>> map) {
		List<Solluction> solucoes = new LinkedList<>();
		for (Nurse nurse : mConfig.getEnfermeiros()) {
			map.entrySet().forEach(mdia ->{
				mdia.getValue().entrySet().forEach(mturno ->{
					List<Alocation> list = mturno.getValue();
					Optional<Alocation> optional = list.stream().filter(aloc -> aloc.getEnfermeiro().equals(nurse)).findFirst();
					if(optional.isPresent()){
						solucoes.add(new Solluction(nurse, mdia.getKey(), mturno.getKey(), optional.get()));
					}
				});
			});
		}
		return solucoes.parallelStream()
				.collect(groupingBy(Solluction::getEnfermeiro));
	}

	private Map<Day, Map<Shift, List<Alocation>>> designarDiasTrabalho() {
		Map<Day, Map<Shift, List<Alocation>>> mDays = new LinkedHashMap<>(); 
		
		List<Shift> turnos = Arrays.asList(Shift.values());
		Arrays.asList(Day.values()).forEach(dia -> {
			Map<Shift, List<Alocation>> map = designarTurnosPorDia(dia, turnos);
			mDays.put(dia, map);
		});
		
		return mDays;
	}

	private Map<Shift, List<Alocation>> designarTurnosPorDia(Day dia, List<Shift> turnos) {
		Map<Shift, List<Alocation>> map = new HashMap<>();
		List<Nurse> enfermeiros = new ArrayList<>(mConfig.getEnfermeiros());
		
		int qtdTurnosExtras = calcularQtdTurnosExtras(dia);
		Integer qtdDemanda = mConfig.sumQtdDemanda(dia);
		boolean isTurnoExtra = false;
		while (enfermeiros.size() > qtdDemanda && (qtdTurnosExtras > 0 || map.isEmpty())) {
			TypeAlocation type = isTurnoExtra ? TypeAlocation.E: TypeAlocation.D;
				turnos.forEach(turno -> {
					List<Nurse> selecionados = selecionarEnfermeirosPorDemanda(dia, enfermeiros, turno);
					enfermeiros.removeAll(selecionados);
					List<Alocation> list = map.get(turno);
					if(list == null){
						list = new ArrayList<>();
					}
					List<Alocation> alocations = selecionados.stream()
																.map(m -> new Alocation(m, type))
																.collect(toList());
					
					list.addAll(alocations);
					map.put(turno, list);
				});
			qtdTurnosExtras--;
			isTurnoExtra = true;
		}
		
		List<Alocation> alocations = enfermeiros.stream()
												.map(enf -> new Alocation(enf, TypeAlocation.E))
												.collect(toList());
		map.put(Shift.F, alocations);
		
		return map;
	}

	/**
	 * Seleciona enfermeiros de forma aleatória de acordo com a demanda do dia.
	 * 
	 * @param dia
	 * @param enfermeiros
	 * @param turno
	 * @return
	 */
	private List<Nurse> selecionarEnfermeirosPorDemanda(Day dia, List<Nurse> enfermeiros, Shift turno) {
		Collections.shuffle(enfermeiros);
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
