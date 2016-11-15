package br.com.nsp.configuration;

import static java.util.stream.Collectors.groupingBy;
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.nsp.configuration.object.Demand;
import br.com.nsp.configuration.object.Instancy;
import br.com.nsp.configuration.object.Preference;
import br.com.nsp.configuration.object.file.FileGen;
import br.com.nsp.configuration.object.file.FileGenLayout;
import br.com.nsp.configuration.object.file.FileNsp;
import br.com.nsp.configuration.object.file.LineGen;
import br.com.nsp.configuration.object.file.LineNsp;
import br.com.nsp.object.Day;
import br.com.nsp.object.Nurse;
import br.com.nsp.object.Shift;

public class MannagerConfig {

	private static final String DIRECTORY_NSP = "nsp/";
	private static final String DIRECTORY_GEN = "gens/";
	private int contador;
	private int index;
	private final List<FileGen> genFiles = new ArrayList<>();
	private final List<FileNsp> nsfFiles = new ArrayList<>();
	private final List<Nurse> enfermeiros = new ArrayList<>();

	private List<Instancy> instancias = new ArrayList<>();
	private List<Preference> preferencias = new LinkedList<>();
	private List<Demand> demandas = new LinkedList<>();
	private int lineNumber;

	public MannagerConfig() {
		this.gerarPreferencias();
		this.gerarDemanda();
		this.gerarInstancias();
	}

	private void gerarInstancias() {
		List<FileGen> files = getGenFiles();
		instancias.addAll(files.stream()
				.map(file -> new FileGenLayout(file)).map(layout -> new Instancy(layout, this))
				.collect(Collectors.toList()));
	}
	
	private void gerarDemanda(){
		List<FileNsp> files = getNsfFiles();
		files.forEach(nspF ->{
			List<String> list = nspF.getLines().stream()
											.filter(linha -> linha.toString().length() > 8 && linha.toString().length() < 10)
											.map(linha -> linha.toString())
											.collect(Collectors.toList());
			
			carregarDemandas(list);
		});
	}

	private void carregarDemandas(List<String> list) {
		contador = 0;
		list.forEach(texto -> {
			char[] charArray = stripLineText(texto).toCharArray();
			for (int x = 0; x < charArray.length; x++) {
				demandas.add(new Demand(Day.values()[contador], Shift.values()[x], charArray[x]));
			}
			contador++;
		});
	}

	private void gerarPreferencias() {
		List<FileNsp> files = getNsfFiles();
		files.forEach(nspF -> {
			nspF.getLines().stream()
						.filter(linha -> linha.toString().length() > 10)
						.forEach(linha -> {
							carregarPreferencias(linha.toString(), getEnfermeiros().get(index++));
						});
		});
	}

	private Map<Nurse, List<Preference>> groupByNurse() {
		return preferencias.stream()
				.collect(groupingBy(Preference::getEnfermeiro));
	}
	
	private Map<Day, List<Demand>> groupByDemand(){
		return demandas.stream()
				.collect(groupingBy(Demand::getDia));
	}

	private void loadGenFiles() {
		Config.getGenFiles().forEach(file -> {
			try {
				Path path = Paths.get(ClassLoader.getSystemResource(DIRECTORY_GEN + file).toURI());
				lineNumber = 0;
				List<LineGen> linhas = Files.lines(path)
												.filter(line -> line.length() > 1)
												.map(line -> new LineGen(++lineNumber, deleteWhitespace(line).toCharArray()))
												.collect(Collectors.toList());
				genFiles.add(new FileGen(file, linhas));
			} catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void loadNspFiles() {
		Config.getNspfiles().forEach(file -> {
			try {
				Path path = Paths.get(ClassLoader.getSystemResource(DIRECTORY_NSP+ file).toURI());
				List<LineNsp> lines = Files.lines(path)
												.filter(line -> line.length() > 1)
												.map(line -> line.split(","))
												.map(line -> new LineNsp(line))
												.collect(Collectors.toList());
				nsfFiles.add(new FileNsp(file, lines));
			} catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void carregarPreferencias(String linha, Nurse nurse) {
		contador = 0;
		String texto = stripLineText(linha);
		int index = 0;
		while (index < texto.length()) {
			String pesos = texto.substring(index, Math.min(index + 4, texto.length()));
			char[] charArray = pesos.toCharArray();
			Arrays.asList(Shift.values()).forEach(turno -> {
				preferencias.add(new Preference(nurse, Day.values()[contador], turno, charArray[turno.ordinal()]));
			});
			contador++;
			index += 4;
		}

	}

	private String stripLineText(String linha) {
		return deleteWhitespace(linha).replaceAll("[^\\w]", "");
	}

	public List<FileGen> getGenFiles() {
		if (genFiles.isEmpty()) {
			loadGenFiles();
		}
		return genFiles;
	}

	public List<FileNsp> getNsfFiles() {
		if (nsfFiles.isEmpty()) {
			loadNspFiles();
		}
		return nsfFiles;
	}

	public List<Nurse> getEnfermeiros() {
		if (enfermeiros.isEmpty()) {
			nurseLoad();
		}
		return enfermeiros;
	}

	private void nurseLoad() {
		for (int count = 1; count <= Config.getQtdEnfermeiros(); count++) {
			enfermeiros.add(new Nurse(count));
		}
	}

	public Instancy getInstancia(String fileName) {
		return instancias.stream()
				.filter(instancia -> instancia.getFileName().equals(fileName))
				.findFirst()
				.get();
	}

	public List<Instancy> getInstancias() {
		return instancias;
	}

	public Map<Nurse, List<Preference>> getPreferencias() {
		return groupByNurse();
	}
	
	public Map<Day, List<Demand>> getDemandas() {
		return groupByDemand();
	}

	public List<Demand> getDemandas(Day dia){
		return getDemandas().get(dia);
	}
	
	public Integer getQtdDemandaPorTurno(Day dia, Shift turno){
		return getDemandas().get(dia).stream()
				.filter(dem -> dem.getTurno().equals(turno))
				.findFirst()
				.get()
				.getQtdExigida();
	}
	
	public Integer sumQtdDemanda(Day dia){
		List<Demand> demandas = getDemandas().get(dia);
		return demandas.stream()
				.map(dem -> dem.getQtdExigida())
				.reduce((x, y) -> x + y)
				.get();
	}
	
}
