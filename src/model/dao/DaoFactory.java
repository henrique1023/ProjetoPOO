package model.dao;

import db.DB;
import model.dao.ConsultaDaoJDBC;
import model.dao.DiagnosticoDaoJDBC;

public class DaoFactory {
	
	public static DiagnosticoDao createDiagnosticoDao() {
		return new DiagnosticoDaoJDBC(DB.getConnection());
	}
	
	public static ConsultaDao createConsultaDao() {
		return new ConsultaDaoJDBC(DB.getConnection());
	}
}
