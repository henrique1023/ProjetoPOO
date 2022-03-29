package model.entities;

import java.util.Date;

public class Profissional {
	private Especializacao especializacao;
	private Integer idProfi;
	private String nome;
	private String email;
	private Date dataAniver;
	private Double salarioBase;

	public Profissional() {
	}

	public Profissional(Especializacao especializacao, Integer idProfi, String nome, String email, Date dataAniver,
			Double salarioBase) {
		this.especializacao = especializacao;
		this.idProfi = idProfi;
		this.nome = nome;
		this.email = email;
		this.dataAniver = dataAniver;
		this.salarioBase = salarioBase;
	}

	public Especializacao getEspecializacao() {
		return especializacao;
	}

	public void setEspecializacao(Especializacao especializacao) {
		this.especializacao = especializacao;
	}

	public Integer getIdProfi() {
		return idProfi;
	}

	public void setIdProfi(Integer idProfi) {
		this.idProfi = idProfi;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataAniver() {
		return dataAniver;
	}

	public void setDataAniver(Date dataAniver) {
		this.dataAniver = dataAniver;
	}

	public Double getSalarioBase() {
		return salarioBase;
	}

	public void setSalarioBase(Double salarioBase) {
		this.salarioBase = salarioBase;
	}

	@Override
	public String toString() {
		return "Profissional [especializacao=" + especializacao + ", idProfi=" + idProfi + ", nome=" + nome + ", email="
				+ email + ", dataAniver=" + dataAniver + ", salarioBase=" + salarioBase + "]";
	}

}
