package model.entities;

import java.util.Date;
import java.util.Objects;

public class Paciente {
	private Integer IdPasciente;
	private String nomePasciente;
	private Date dataAniversario;

	public Paciente() {
	}
	public Paciente(Integer idPasciente, String nomePasciente, Date dataAniversario) {
		this.IdPasciente = idPasciente;
		this.nomePasciente = nomePasciente;
		this.dataAniversario = dataAniversario;
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

	public Date getDataAniversario() {
		return dataAniversario;
	}

	public void setDataAniversario(Date dataAniversario) {
		this.dataAniversario = dataAniversario;
	}

	@Override
	public String toString() {
		return "Paciente [IdPasciente=" + IdPasciente + ", nomePasciente=" + nomePasciente + ", dataAniversario="
				+ dataAniversario + "]";
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
