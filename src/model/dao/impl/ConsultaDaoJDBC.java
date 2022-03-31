package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.ConsultaDao;
import model.entities.Consulta;
import model.entities.Diagnostico;
import model.entities.Especializacao;
import model.entities.Paciente;
import model.entities.Profissional;

public class ConsultaDaoJDBC implements ConsultaDao {

	private Connection conn;

	public ConsultaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Consulta obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO consulta " + "(DataConsul, PacienteId, ProfissionalId, DiagnoId "
					+ "VALUES " + "(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setDate(1, new java.sql.Date(obj.getDataConsul().getTime()));
			st.setInt(2, obj.getPaciente().getIdPaciente());
			st.setInt(3, obj.getProfissional().getIdProfi());
			st.setInt(4, obj.getDiagnostico().getIdDiag());

			int rowAffected = st.executeUpdate();

			if (rowAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setIdConsulta(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Consulta obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE consulta "
					+ "SET DataConsul = ?, PacienteId = ?, ProfissionalId = ?, DiagnoId = ? " + "WHERE Id = ?");

			st.setDate(1, new java.sql.Date(obj.getDataConsul().getTime()));
			st.setInt(2, obj.getPaciente().getIdPaciente());
			st.setInt(3, obj.getProfissional().getIdProfi());
			st.setInt(4, obj.getDiagnostico().getIdDiag());
			st.setInt(6, obj.getIdConsulta());

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("DELETE FROM consulta " + "WHERE Id = ?");
			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Consulta findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT consulta.*,paciente.NomePaci as PacNome, "
					+ "paciente.DataAniv as PacieData, " + "profissional.Email as ProfiEmail, "
					+ "profissional.DataAniv as ProfiData, " + "profissional.SalarioBase as ProfiSal, "
					+ "profissional.EspecId as ProfiEspec, " + "diagnostico.NomeDiagno as DiagnoNome "
					+ "FROM consulta INNER JOIN paciente ON consulta.PacienteId = paciente.Id "
					+ "INNER JOIN profissional ON consulta.ProfissionalId = profissional.Id "
					+ "INNER JOIN diagnostico ON consulta.DiagnoId = diagnostico.Id " + "WHERE consulta.IdConsul = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Paciente paciente = instatiatePaciente(rs);
				Profissional profi = instatiateProfissional(rs);
				Diagnostico diag = instatiateDiagnostico(rs);

				Consulta prof = instatiateConsulta(rs, paciente, profi, diag);
				return prof;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}

	}

	private Profissional instatiateProfissional(ResultSet rs) throws SQLException {
		Profissional prof = new Profissional();
		prof.setIdProfi(rs.getInt("ProfissionalId"));
		prof.setDataAniver(new java.util.Date(rs.getTimestamp("ProfiData").getTime()));
		prof.setEmail(rs.getString("ProfiEmail"));
		Especializacao esp = setEspec(rs);
		prof.setEspecializacao(esp);
		return prof;
	}

	private Especializacao setEspec(ResultSet rs) throws SQLException {
		PreparedStatement st = null;
		ResultSet rss = null;
		Especializacao espec = new Especializacao();
		st = conn.prepareStatement("SELECT espec.* FROM espec WHERE espec.Id = ?");
		st.setInt(1, rs.getInt("ProfiEspec"));
		rss = st.executeQuery();
		espec.setIdEspeci(rss.getInt("Id"));
		espec.setNomeEspeci(rss.getString("NomeEspec"));
		return espec;
	}

	private Consulta instatiateConsulta(ResultSet rs, Paciente paciente, Profissional profi, Diagnostico diag)
			throws SQLException {
		Consulta consu = new Consulta();
		consu.setIdConsulta(rs.getInt("IdConsul"));
		consu.setDataConsul(new java.util.Date(rs.getTimestamp("DataConsul").getTime()));
		consu.setPaciente(paciente);
		consu.setProfissional(profi);
		consu.setDiagnostico(diag);
		return consu;
	}

	private Paciente instatiatePaciente(ResultSet rs) throws SQLException {
		Paciente paciente = new Paciente();
		paciente.setIdPaciente(rs.getInt("PacienteId"));
		paciente.setNomePaciente(rs.getString("PascNome"));
		paciente.setDataAniversario(new java.util.Date(rs.getTimestamp("PacieData").getTime()));
		return paciente;
	}

	private Diagnostico instatiateDiagnostico(ResultSet rs) throws SQLException {
		Diagnostico diag = new Diagnostico();
		diag.setIdDiag(rs.getInt("DiagnoId"));
		diag.setNomeDiag(rs.getString("DiagnoNome"));
		return null;
	}

	@Override
	public List<Consulta> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT consulta.*,paciente.NomePaci as PacNome, "
					+ "paciente.DataAniv as PacieData, " + "profissional.Email as ProfiEmail, "
					+ "profissional.DataAniv as ProfiData, " + "profissional.SalarioBase as ProfiSal, "
					+ "profissional.EspecId as ProfiEspec, " + "diagnostico.NomeDiagno as DiagnoNome "
					+ "FROM consulta INNER JOIN paciente ON consulta.PascienteId = paciente.Id "
					+ "INNER JOIN profissional ON consulta.ProfissionalId = profissional.Id "
					+ "INNER JOIN diagnostico ON consulta.DiagnoId = diagnostico.Id " + "ORDER BY Id");

			rs = st.executeQuery();

			List<Consulta> list = new ArrayList<>();
			Map<Integer, Profissional> mapProf = new HashMap<>();
			Map<Integer, Paciente> mapPasc = new HashMap<>();
			Map<Integer, Diagnostico> mapDiag = new HashMap<>();

			while (rs.next()) {

				Profissional profi = mapProf.get(rs.getInt("ProfissionalId"));
				Paciente paci = mapPasc.get(rs.getInt("PascienteId"));
				Diagnostico diag = mapDiag.get(rs.getInt("DiagnoId"));

				if (profi == null) {
					profi = instatiateProfissional(rs);
					mapProf.put(rs.getInt("ProfissionalId"), profi);
				}

				if (paci == null) {
					paci = instatiatePaciente(rs);
					mapPasc.put(rs.getInt("PascienteId"), paci);
				}

				if (diag == null) {
					diag = instatiateDiagnostico(rs);
					mapDiag.put(rs.getInt("DianoId"), diag);
				}

				Consulta consu = instatiateConsulta(rs, paci, profi, diag);
				list.add(consu);
			}

			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
