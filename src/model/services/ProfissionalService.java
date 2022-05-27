package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.ProfissionalDao;
import model.entities.Profissional;

public class ProfissionalService {

	private ProfissionalDao dao = DaoFactory.createProfissionalDao();

	public List<Profissional> findAll() {
		return dao.findAll();
	}

	// esse metodo que salva no banco o objeto passado
	public void saveOrUptade(Profissional obj) {
		if (obj.getIdProfi() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Profissional obj) {
		dao.deleteById(obj.getIdProfi());
	}

	public List<Profissional> findByNome(String nome) {
		return dao.findByNome(nome);
	}
}
