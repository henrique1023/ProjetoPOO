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
import model.dao.EspecializacaoDao;
import model.entities.Especializacao;

public class EspecializacaoDaoJDBC implements EspecializacaoDao{

	private Connection conn;

	public EspecializacaoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Especializacao obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO espec " + "(nome_espec) " 
					+ "VALUES (?)" , Statement.RETURN_GENERATED_KEYS);

			st.setString(1, obj.getNomeEspeci());

			int rowAffected = st.executeUpdate();

			if (rowAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setIdEspeci(id);
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
	public void update(Especializacao obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE espec " + "SET nome_espec = ?" + "WHERE id = ?");
			st.setString(1, obj.getNomeEspeci());

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
			st = conn.prepareStatement("UPDATE espec SET deletado = 'V' " + "WHERE Id = ?");
			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Especializacao findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT espec.* FROM espec " + "WHERE espec.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Especializacao espec = instatiateEspecializacao(rs);

				return espec;
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
	public List<Especializacao> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT espec.* FROM espec " + "ORDER BY ID");

			rs = st.executeQuery();

			List<Especializacao> list = new ArrayList<>();

			while (rs.next()) {
				
				String dl =  rs.getString("deletado");
				char cdl = dl.charAt(0);
				if(cdl == 'F' || cdl == 'f') {
					Especializacao espec = instatiateEspecializacao(rs);
					list.add(espec);
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

	private Especializacao instatiateEspecializacao(ResultSet rs) throws SQLException {
		Especializacao espec = new Especializacao();
		espec.setIdEspeci(rs.getInt("id"));
		espec.setNomeEspeci(rs.getString("nome_espec"));
		return espec;
	}
}
