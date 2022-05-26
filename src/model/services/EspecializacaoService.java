package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.EspecializacaoDao;
import model.entities.Especializacao;

public class EspecializacaoService {

	private EspecializacaoDao dao = DaoFactory.createEspecializacaoDao();
	
	public List<Especializacao> findAll(){
		return dao.findAll();
	}
	
	//esse metodo que salva no banco o objeto passado
	public void saveOrUptade(Especializacao obj) {
		if(obj.getIdEspeci() == null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}
	}
	
	public void remove(Especializacao obj) {
		dao.deleteById(obj.getIdEspeci());
	}
}
