package model.dao;

import java.util.List;

import model.entities.Consulta;

public interface ConsultaDao {

	void insert(Consulta obj);

	void update(Consulta obj);

	void deleteById(Integer id);

	Consulta findById(Integer id);

	List<Consulta> findAll();
}
