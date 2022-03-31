package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DiagnosticoDao;
import model.entities.Diagnostico;

public class DiagnosticoService {

	private DiagnosticoDao dao = new DaoFactory().createDiagnosticoDao();
	
	public List<Diagnostico> findAll(){
		return dao.findAll();
	}
	
	//esse metodo que salva no banco o objeto passado
	public void saveOrUptade(Diagnostico obj) {
		if(obj.getIdDiag() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}
	}
	
	public void remove(Diagnostico obj) {
		dao.deleteById(obj.getIdDiag());
	}
}
