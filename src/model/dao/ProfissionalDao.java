package model.dao;

import java.util.List;

import model.entities.Especializacao;
import model.entities.Profissional;

public interface ProfissionalDao {
	void insert(Profissional obj);
	void update(Profissional obj);
	void deleteById(Integer id);
	Profissional findById(Integer id);
	List<Profissional> findAll();
	List<Profissional> findByEspecializacao(Especializacao especializacao);
}
