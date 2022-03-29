package model.dao;

import java.util.List;

import model.entities.Diagnostico;


public interface DiagnosticoDao {
	
	void insert(Diagnostico obj);
	void update(Diagnostico obj);
	void deleteById(Integer id);
	Diagnostico findById(Integer id);
	List<Diagnostico> findAll();
}
