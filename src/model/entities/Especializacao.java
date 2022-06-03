package model.entities;

import java.util.Objects;

public class Especializacao {
	private Integer idEspeci;
	private String nomeEspeci;
	
	public Especializacao() {
	}

	public Especializacao(Integer idEspeci, String nomeEspeci) {
		super();
		this.idEspeci = idEspeci;
		this.nomeEspeci = nomeEspeci;
	}

	public Integer getIdEspeci() {
		return idEspeci;
	}

	public void setIdEspeci(Integer idEspeci) {
		this.idEspeci = idEspeci;
	}

	public String getNomeEspeci() {
		return nomeEspeci;
	}

	public void setNomeEspeci(String nomeEspeci) {
		this.nomeEspeci = nomeEspeci;
	}

	@Override
	public String toString() {
		return "Especializacao [idEspeci=" + idEspeci + ", nomeEspeci=" + nomeEspeci + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(idEspeci);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Especializacao other = (Especializacao) obj;
		return Objects.equals(idEspeci, other.idEspeci);
	}
	

}
