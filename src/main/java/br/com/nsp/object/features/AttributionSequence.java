package br.com.nsp.object.features;

import br.com.nsp.util.Util;

/**
 * Representação para atribuições consecutivas. <br/>
 * <br/>
 * Corresponde ao minimo e maximo de dias trabalhados para que haja uma Folga.
 * 
 * @author Fernando
 *
 */
public class AttributionSequence {

	private int max;

	private int min;

	public AttributionSequence(char min, char max) {
		this.max = Util.toInt(max);
		this.min = Util.toInt(min);
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

}
