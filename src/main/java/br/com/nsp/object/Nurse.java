package br.com.nsp.object;

/**
 * Representação de um Enfermeiro a ser utilizado no problema.
 * 
 * @author Fernando
 *
 */
public class Nurse {

	private int identificacao;

	public Nurse(int identificacao) {
		this.identificacao = identificacao;
	}

	public int getIdentificacao() {
		return identificacao;
	}

	@Override
	public String toString() {
		return "" + identificacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + identificacao;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Nurse other = (Nurse) obj;
		if (identificacao != other.identificacao)
			return false;
		return true;
	}

}
