package model.dao;

import db.DB;
import model.dao.impl.ConsultaDaoJDBC;
import model.dao.impl.DiagnosticoDaoJDBC;
import model.dao.impl.EspecializacaoDaoJDBC;
import model.dao.impl.PacienteDaoJDBC;
import model.dao.impl.ProfissionalDaoJDBC;

public class DaoFactory {
	
	public static DiagnosticoDao createDiagnosticoDao() {
		return new DiagnosticoDaoJDBC(DB.getConnection());
	}
	
	public static ConsultaDao createConsultaDao() {
		return new ConsultaDaoJDBC(DB.getConnection());
	}
	
	public static PacienteDao createPacienteDao() {
		return new PacienteDaoJDBC(DB.getConnection());
	}
	
	public static ProfissionalDao createProfissionalDao() {
		return new ProfissionalDaoJDBC(DB.getConnection());
	}
	
	public static EspecializacaoDao createEspecializacaoDao() {
		return new EspecializacaoDaoJDBC(DB.getConnection());
	}
}
