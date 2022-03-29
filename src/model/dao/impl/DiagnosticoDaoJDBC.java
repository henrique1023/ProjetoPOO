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
import model.dao.DiagnosticoDao;
import model.entities.Diagnostico;

public class DiagnosticoDaoJDBC implements DiagnosticoDao {

	private Connection conn;

	public DiagnosticoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Diagnostico obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO diagnostico " + "(NomeDiagno) " + "VALUES " + "(?)",
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNomeDiag());

			int rowAffected = st.executeUpdate();

			if (rowAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setIdDiag(id);
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
	public void update(Diagnostico obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE diagnostico " + "SET NomeDiagno = ? " + "WHERE Id = ?");
			st.setString(1, obj.getNomeDiag());

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
			st = conn.prepareStatement("DELETE FROM diagnostico " + "WHERE Id = ?");
			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Diagnostico findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT diagnostico.* FROM diagnostico " + "WHERE diagnostico.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Diagnostico diag = instatiateDiagnostico(rs);

				return diag;
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
	public List<Diagnostico> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT diagnostico.* FROM diagnostico " + "ORDER BY ID");

			rs = st.executeQuery();

			List<Diagnostico> list = new ArrayList<>();

			while (rs.next()) {

				Diagnostico diagnostico = instatiateDiagnostico(rs);
				list.add(diagnostico);
			}

			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	private Diagnostico instatiateDiagnostico(ResultSet rs) throws SQLException {
		Diagnostico diagnostico = new Diagnostico();
		diagnostico.setIdDiag(rs.getInt("Id"));
		return diagnostico;
	}
}
