package br.com.nsp.object.features;

import br.com.nsp.util.Util;

/**
 * Representação para penalidades relacionados ao turno da manhã
 * 
 * @author Fernando
 *
 */
public class NightShift {

	int min;

	int max;

	int minConsecutivas;

	int maxConsecutivas;

	public NightShift(char min, char max, char minConsecutivas, char maxConsecutivas) {
		this.min = Util.toInt(min);
		this.max = Util.toInt(max);
		this.minConsecutivas = Util.toInt(minConsecutivas);
		this.maxConsecutivas = Util.toInt(maxConsecutivas);
	}

	public int getMax() {
		return max;
	}

	public int getMaxConsecutivas() {
		return maxConsecutivas;
	}

	public int getMin() {
		return min;
	}

	public int getMinConsecutivas() {
		return minConsecutivas;
	}

}
