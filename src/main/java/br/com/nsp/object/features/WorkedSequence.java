package br.com.nsp.object.features;

import br.com.nsp.util.Util;

/**
 * Representação para sequencia de dias de trabalho.
 * 
 * @author Fernando
 *
 */
public class WorkedSequence {

	private int max;

	private int min;

	public WorkedSequence(char min, char max) {
		this.max = Util.toInt(max);
		this.min = Util.toInt(min);
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}
}
