package br.com.nsp.object;

/**
 * Representation for alocation day of work
 * 
 * @author Fernando
 *
 */
public class Alocation {

	private Nurse nurse;

	private TypeAlocation typeAlocation;

	public Alocation(Nurse nurse, TypeAlocation typeAlocation) {
		this.nurse = nurse;
		this.typeAlocation = typeAlocation;
	}
	
	public Nurse getEnfermeiro() {
		return nurse;
	}
	
	public TypeAlocation getType() {
		return typeAlocation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nurse == null) ? 0 : nurse.hashCode());
		result = prime * result + ((typeAlocation == null) ? 0 : typeAlocation.hashCode());
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
		Alocation other = (Alocation) obj;
		if (nurse == null) {
			if (other.nurse != null)
				return false;
		} else if (!nurse.equals(other.nurse))
			return false;
		if (typeAlocation != other.typeAlocation)
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "Enfermeiro= " + nurse + " tipoTurno= " + typeAlocation;
	}
	

}
