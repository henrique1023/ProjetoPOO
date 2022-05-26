package model.entities;

import java.util.Objects;

public class Especializacao {
	private Integer IdEspeci;
	private String NomeEspeci;
	
	public Especializacao() {
	}
	public Especializacao(Integer idEspeci, String nomeEspeci) {
		IdEspeci = idEspeci;
		NomeEspeci = nomeEspeci;
	}

	public Integer getIdEspeci() {
		return IdEspeci;
	}

	public void setIdEspeci(Integer idEspeci) {
		IdEspeci = idEspeci;
	}

	public String getNomeEspeci() {
		return NomeEspeci;
	}

	public void setNomeEspeci(String nomeEspeci) {
		NomeEspeci = nomeEspeci;
	}

	@Override
	public String toString() {
		return NomeEspeci;
	}

	@Override
	public int hashCode() {
		return Objects.hash(IdEspeci);
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
		return Objects.equals(IdEspeci, other.IdEspeci);
	}

}
