package br.com.nsp.util;

import java.util.List;
import java.util.Map;

import br.com.nsp.configuration.object.Solluction;
import br.com.nsp.object.Nurse;

public class Util {

	public static int toInt(char valor) {
		return Integer.parseInt(Character.toString(valor));
	}
	
	public static void printToConsole(Map<Nurse, List<Solluction>> solucao){
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
}
