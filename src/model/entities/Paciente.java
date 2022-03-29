package model.entities;

import java.util.Objects;

public class Paciente {
	private Integer IdPasciente;
	private String nomePasciente;
	private Integer idadePasciente;

	public Paciente(Integer idPasciente, String nomePasciente, Integer idadePasciente) {
		this.IdPasciente = idPasciente;
		this.nomePasciente = nomePasciente;
		this.idadePasciente = idadePasciente;
	}

	public Integer getIdPasciente() {
		return IdPasciente;
	}

	public void setIdPasciente(Integer idPasciente) {
		IdPasciente = idPasciente;
	}

	public String getNomePasciente() {
		return nomePasciente;
	}

	public void setNomePasciente(String nomePasciente) {
		this.nomePasciente = nomePasciente;
	}

	public Integer getIdadePasciente() {
		return idadePasciente;
	}

	public void setIdadePasciente(Integer idadePasciente) {
		this.idadePasciente = idadePasciente;
	}

	@Override
	public String toString() {
		return "Paciente [IdPasciente=" + IdPasciente + ", nomePasciente=" + nomePasciente + ", idadePasciente="
				+ idadePasciente + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(IdPasciente);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paciente other = (Paciente) obj;
		return Objects.equals(IdPasciente, other.IdPasciente);
	}

}
