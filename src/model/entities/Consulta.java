package model.entities;

import java.util.Date;

public class Consulta {
	private Integer idConsulta;
	private Diagnostico diagnostico;
	private Paciente paciente;
	private Profissional profissional;
	private Date dataConsul;

	public Consulta(Integer idConsulta, Diagnostico diagnostico, Paciente paciente, Profissional profissional,
			Date dataConsul) {
		this.idConsulta = idConsulta;
		this.diagnostico = diagnostico;
		this.paciente = paciente;
		this.profissional = profissional;
		this.dataConsul = dataConsul;
	}

	public Integer getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(Integer idConsulta) {
		this.idConsulta = idConsulta;
	}

	public Diagnostico getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(Diagnostico diagnostico) {
		this.diagnostico = diagnostico;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Profissional getProfissional() {
		return profissional;
	}

	public void setProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public Date getDataConsul() {
		return dataConsul;
	}

	public void setDataConsul(Date dataConsul) {
		this.dataConsul = dataConsul;
	}

}
