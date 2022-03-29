package model.dao;

import java.util.List;

import model.entities.Especializacao;

public interface EspecializacaoDao {
	void insert(Especializacao obj);
	void update(Especializacao obj);
	void deleteById(Integer id);
	Especializacao findById(Integer id);
	List<Especializacao> findAll();
}
