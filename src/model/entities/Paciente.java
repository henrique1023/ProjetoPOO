package model.entities;

import java.util.Date;
import java.util.Objects;

public class Paciente {
	private Integer IdPaciente;
	private String nomePaciente;
	private Date dataAniversario;

	public Paciente() {
	}
	public Paciente(Integer idPaciente, String nomePaciente, Date dataAniversario) {
		this.IdPaciente = idPaciente;
		this.nomePaciente = nomePaciente;
		this.dataAniversario = dataAniversario;
	}

	public Integer getIdPaciente() {
		return IdPaciente;
	}

	public void setIdPasciente(Integer idPaciente) {
		IdPaciente = idPaciente;
	}

	public String getNomePasciente() {
		return nomePaciente;
	}

	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}

	public Date getDataAniversario() {
		return dataAniversario;
	}

	public void setDataAniversario(Date dataAniversario) {
		this.dataAniversario = dataAniversario;
	}

	@Override
	public String toString() {
		return "Paciente [IdPaciente=" + IdPaciente + ", nomePaciente=" + nomePaciente + ", dataAniversario="
				+ dataAniversario + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(IdPaciente);
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
		return Objects.equals(IdPaciente, other.IdPaciente);
	}

}
