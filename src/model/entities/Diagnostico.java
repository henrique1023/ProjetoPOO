package model.entities;

import java.util.Objects;

public class Diagnostico {
	private Integer IdDiag;
	private String NomeDiag;
	
	public Diagnostico() {
	}
	public Diagnostico(Integer idDiag, String nomeDiag) {
		this.IdDiag = idDiag;
		this.NomeDiag = nomeDiag;
	}

	public Integer getIdDiag() {
		return IdDiag;
	}

	public void setIdDiag(Integer idDiag) {
		IdDiag = idDiag;
	}

	public String getNomeDiag() {
		return NomeDiag;
	}

	public void setNomeDiag(String nomeDiag) {
		NomeDiag = nomeDiag;
	}

	@Override
	public String toString() {
		return "Diagnostico [IdDiag=" + IdDiag + ", NomeDiag=" + NomeDiag + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(IdDiag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Diagnostico other = (Diagnostico) obj;
		return Objects.equals(IdDiag, other.IdDiag);
	}

}
