package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.PacienteDao;
import model.entities.Paciente;

public class PacienteService {
	
	private PacienteDao dao = DaoFactory.createPacienteDao();
	
	public List<Paciente> findAll(){
		return dao.findAll();
	}
	
	//esse metodo que salva no banco o objeto passado
	public void saveOrUptade(Paciente obj) {
		if(obj.getIdPaciente() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}
	}
	
	public void remove(Paciente obj) {
		dao.deleteById(obj.getIdPaciente());
	}
	
	public List<Paciente> findByNome(String nome){
		return dao.findByNome(nome);
	}
}
