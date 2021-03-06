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
import model.dao.ProfissionalDao;
import model.entities.Especializacao;
import model.entities.Profissional;

public class ProfissionalDaoJDBC implements ProfissionalDao{

	private Connection conn;

	public ProfissionalDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Profissional obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("INSERT INTO profissional " + "(nome_profissional, email, data_aniv, "
					+ "salario_base, espec_id) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataAniver().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getEspecializacao().getIdEspeci());

			int rowAffected = st.executeUpdate();

			if (rowAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setIdProfi(id);
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
	public void update(Profissional obj) {
		PreparedStatement st = null;

		try {
			st = conn.prepareStatement("UPDATE profissional "
					+ "SET nome_profissional = ?, email = ?, data_aniv = ?, salario_base = ?, "
					+ "espec_id = ? " + "WHERE id = ?");
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataAniver().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getEspecializacao().getIdEspeci());
			st.setInt(6, obj.getIdProfi());

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
			st = conn.prepareStatement("UPDATE profissional SET deletado = 'V' " + "WHERE id = ?");
			st.setInt(1, id);

			st.executeUpdate();

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Profissional findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT profissional.*,espec.nome_espec as EspecNome " + "FROM profissional INNER JOIN espec "
							+ "ON profissional.espec_id = espec.id " + "WHERE profissional.id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Especializacao espec = instatiateEspecializacao(rs);

				Profissional prof = instatiateProfissional(rs, espec);
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
	
	@Override
	public List<Profissional> findByNome(String nome) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"SELECT profissional.*, espec.nome_espec as EspecNome " 
							+ "FROM profissional INNER JOIN espec "
							+ "ON profissional.espec_id = espec.id " 
							+ "WHERE profissional.nome_profissional LIKE '" + nome + "%'");
			rs = st.executeQuery();
			
			List<Profissional> list = new ArrayList<>();
			Map<Integer, Especializacao> map = new HashMap<>();
			
			while (rs.next()) {
				String dl = rs.getString("deletado");
				char cdl = dl.charAt(0);
				if(cdl == 'F') {
					Especializacao espec = map.get(rs.getInt("espec_id"));
					
					if (espec == null) {
						espec = instatiateEspecializacao(rs);
						map.put(rs.getInt("espec_id"), espec);
					}
	
					Profissional prof = instatiateProfissional(rs, espec);
					list.add(prof);
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

	private Profissional instatiateProfissional(ResultSet rs, Especializacao espec) throws SQLException {
		Profissional prof = new Profissional();
		prof.setIdProfi(rs.getInt("id"));
		prof.setNome(rs.getString("nome_profissional"));
		prof.setEmail(rs.getString("email"));
		prof.setSalarioBase(rs.getDouble("salario_base"));
		prof.setDataAniver(new java.util.Date(rs.getTimestamp("data_aniv").getTime()));
		prof.setEspecializacao(espec);
		return prof;
	}

	private Especializacao instatiateEspecializacao(ResultSet rs) throws SQLException {
		Especializacao espec = new Especializacao();
		espec.setIdEspeci(rs.getInt("espec_id"));
		espec.setNomeEspeci(rs.getString("EspecNome"));
		return espec;
	}

	@Override
	public List<Profissional> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT profissional.*, espec.nome_espec as EspecNome " + "FROM profissional INNER JOIN espec "
							+ "ON profissional.espec_id = espec.id " + "ORDER BY id");

			rs = st.executeQuery();

			List<Profissional> list = new ArrayList<>();
			Map<Integer, Especializacao> map = new HashMap<>();

			while (rs.next()) {
				
				String dl = rs.getString("deletado");
				char cdl = dl.charAt(0);
				if(cdl == 'F') {
					Especializacao espec = map.get(rs.getInt("espec_id"));
	
					if (espec == null) {
						espec = instatiateEspecializacao(rs);
						map.put(rs.getInt("espec_id"), espec);
					}
	
					Profissional prof = instatiateProfissional(rs, espec);
					list.add(prof);
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

	@Override
	public List<Profissional> findByEspecializacao(Especializacao especializao) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT profissional.*, espec.nome_espec as EspecNome " + "FROM profissional INNER JOIN espec "
							+ "ON profissional.espec_id = espec.id " + "WHERE espec_id = ? " + "ORDER BY EspecNome");
			st.setInt(1, especializao.getIdEspeci());

			rs = st.executeQuery();

			List<Profissional> list = new ArrayList<>();
			Map<Integer, Especializacao> map = new HashMap<>();

			while (rs.next()) {

				Especializacao espec = map.get(rs.getInt("espec_id"));

				if (espec == null) {
					espec = instatiateEspecializacao(rs);
					map.put(rs.getInt("espec_id"), espec);
				}

				Profissional prof = instatiateProfissional(rs, espec);
				list.add(prof);
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
