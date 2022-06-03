package model.services;

import java.util.List;

import model.dao.ConsultaDao;
import model.dao.DaoFactory;
import model.entities.Consulta;

public class ConsultaService {

	private ConsultaDao dao = DaoFactory.createConsultaDao();
	
	public List<Consulta> findAll(){
		return dao.findAll();
	}
	
	//esse metodo que salva no banco o objeto passado
	public void saveOrUptade(Consulta obj) {
		if(obj.getIdConsulta() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}
	}
	
	public void remove(Consulta obj) {
		dao.deleteById(obj.getIdConsulta());
	}
}
