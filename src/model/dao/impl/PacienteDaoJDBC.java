package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.PacienteDao;
import model.entities.Paciente;

public class PacienteDaoJDBC implements PacienteDao {

	private Connection conn;

	public PacienteDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Paciente obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO paciente " + "(NomePaci, DataAniv, cpf, telefone, deletado) " + "VALUES " +
					"(?, ?, ?, ?, 'f')" ,
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNomePaciente());
			st.setDate(2, new java.sql.Date(obj.getDataAniversario().getTime()));
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getTelefone());

			int rowAffected = st.executeUpdate();

			if (rowAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setIdPaciente(id);
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
	public void update(Paciente obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE paciente " + "SET NomePaci = ?, DataAniv = ?, cpf = ?, telefone = ? " 
										+ "WHERE Id = ?");
			st.setString(1, obj.getNomePaciente());
			st.setDate(2, new java.sql.Date(obj.getDataAniversario().getTime()));
			st.setString(3, obj.getCpf());
			st.setString(4, obj.getTelefone());
			st.setInt(5, obj.getIdPaciente());

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
			st = conn.prepareStatement("UPDATE paciente SET deletado = 'v' " + "WHERE Id = ?");
			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Paciente findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT paciente.* FROM paciente " + "WHERE paciente.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Paciente dep = instatiatePaciente(rs);

				return dep;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Paciente> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT paciente.* FROM paciente " + "ORDER BY ID");

			rs = st.executeQuery();

			List<Paciente> list = new ArrayList<>();

			while (rs.next()) {

				Paciente paciente = instatiatePaciente(rs);
				String teste =  rs.getString("deletado");
				char c = teste.charAt(0);
				if(c == 'f') {
					list.add(paciente);
				}
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	private Paciente instatiatePaciente(ResultSet rs) throws SQLException {
		Paciente paciente = new Paciente();
		paciente.setIdPaciente(rs.getInt("Id"));
		paciente.setNomePaciente(rs.getString("NomePaci"));
		paciente.setDataAniversario(new java.util.Date(rs.getTimestamp("DataAniv").getTime()));
		paciente.setCpf(rs.getString("cpf"));
		paciente.setTelefone(rs.getString("telefone"));
		return paciente;
	}

	@Override
	public List<Paciente> findByNome(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT paciente.* FROM paciente WHERE NomePaci LIKE '" + nome +"%'" + "ORDER BY ID");

			rs = st.executeQuery();

			List<Paciente> list = new ArrayList<>();

			while (rs.next()) {

				Paciente paciente = instatiatePaciente(rs);
				list.add(paciente);
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
