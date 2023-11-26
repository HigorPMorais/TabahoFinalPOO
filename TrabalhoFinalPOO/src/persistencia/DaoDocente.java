package persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import modelo.Docente;

/**
 *
 * @author higor
 */
public class DaoDocente extends Dao{
    private DaoEndereco daoEndereco;

    public DaoDocente() {
        daoEndereco = new DaoEndereco();
    }

    public ArrayList<Docente> carregarDocentes() {
        ArrayList<Docente> listaDocentes = new ArrayList<>();
        try {
            String sql = """
                         SELECT * FROM docente 
                         left join endereco as ed on idEndereco = ed.idEndereco""";
            ResultSet rs = consultaSQL(sql);
            while (rs.next()) {
                Docente p = new Docente();
                p.setIdFuncionario(rs.getInt("idDocente"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                p.setDataNascimento(rs.getLocalDate("dataNascimento"));
                p.setEmail(rs.getString("email"));
                p.setGenero(rs.getString("genero"));
                p.setCtps(rs.getString("ctps"));
                p.setSalario(rs.getDouble("salario"));

                if (rs.getObject("idEndereco", Integer.class) != null) {
                    p.getEndereco().setIdEndereco(rs.getInt("idEndereco"));
                    p.getEndereco().setCidade(rs.getString("cidade"));
                    p.getEndereco().setRua(rs.getString("rua"));
                    p.getEndereco().setNumero(rs.getString("numero"));
                }
                listaDocentes.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Falha ao carregar docentes!\n" + e.getMessage());
        }
        return listaDocentes;
    }

    public Docente carregarPorId(int idDocente) {
        Docente cl = null;
        try {
            String sql = "SELECT * FROM docente \n"
                    + "left join endereco as ed on idEndereco = ed.idEndereco"
                    + " where docente.idDocente = " + idDocente;
            ResultSet rs = consultaSQL(sql);
            if (rs.next()) {
                cl = new Docente();
                cl.setIdFuncionario(rs.getInt("idDocente"));
                cl.setNome(rs.getString("nome"));
                cl.setCpf(rs.getString("cpf"));
                cl.setDataNascimento(rs.getLocalDate("dataNascimento"));
                cl.setEmail(rs.getString("email"));
                cl.setGenero(rs.getString("genero"));
                cl.setCtps(rs.getString("ctps"));
                cl.setSalario(rs.getDouble("salario"));


                if (rs.getObject("idEndereco", Integer.class) != null) {
                    cl.getEndereco().setIdEndereco(rs.getInt("idEndereco"));
                    cl.getEndereco().setCidade(rs.getString("cidade"));
                    cl.getEndereco().setRua(rs.getString("rua"));
                    cl.getEndereco().setNumero(rs.getString("numero"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Falha ao carregar docente!\n"
                    + e.getMessage());
        }
        return cl;
    }

    public boolean salvar(Docente docente) {
        try {
            String sql = """
                         INSERT INTO docente(
                         idDocente, nome, ra, cpf, idEndereco)
                         VALUES (?, ?, ?, ?, ?)""";

            PreparedStatement ps = criarPreparedStatement(sql);
            docente.setIdFuncionario(gerarProximoId("docente","idDocente"));
            ps.setInt(1, docente.getIdFuncionario());
            ps.setString(2, docente.getNome());
            ps.setString(3, docente.getCpf());
            ps.setDate(4, docente.getDataNascimento());
            ps.setString(5, docente.getEmail());
            ps.setString(6, docente.getGenero());
            ps.setString(7, docente.getCtps());
            ps.setDouble(8, docente.getSalario());

            if (docente.getEndereco() != null) {
                if (docente.getEndereco().getIdEndereco() == null || docente.getEndereco().getIdEndereco() == 0) {
                    daoEndereco.salvar(docente.getEndereco());
                }
                ps.setInt(9, docente.getEndereco().getIdEndereco());
            } else {
                ps.setObject(9, null);
            }
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            try {
                getConexao().rollback();
            } catch (SQLException ex1) {
                System.out.println("Falhar ao realizar RollBack");
            }
            System.out.println("Falha ao salvar Docente\n" + ex.getMessage());
            return false;
        }
    }

    public boolean atualizar(Docente docente) {
        try {
            String sql = """
                         UPDATE docente
                         SET nome=?,ra= ?, cpf=?, tel=?, idEndereco=? 
                         WHERE idDocente= """ + docente.getIdFuncionario();

            PreparedStatement ps = criarPreparedStatement(sql);
            ps.setString(1, docente.getNome());
            ps.setString(2, docente.getCpf());
            ps.setDate(3, docente.getDataNascimento());
            ps.setString(4, docente.getEmail());
            ps.setString(5, docente.getGenero());
            ps.setString(6, docente.getCtps());
            ps.setDouble(7, docente.getSalario());
            
            if (docente.getEndereco() != null) {
                if (docente.getEndereco().getIdEndereco() == null) {
                    daoEndereco.salvar(docente.getEndereco());
                } else {
                    daoEndereco.atualizar(docente.getEndereco());
                }
                ps.setInt(8, docente.getEndereco().getIdEndereco());
            } else {
                ps.setObject(8, null);
            }

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Falha ao editar docente!\n" + e.getMessage());
            return false;
        }
    }

    public boolean remover(Docente docente) {
        try {
            String sql = "DELETE FROM docente\n"
                    + " WHERE idDocente =" + docente.getIdFuncionario()
                    + "; " + daoEndereco.comandoSqlRemover(docente.getEndereco());

            executeSql(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("Falha ao remover docente!\n" + e.getMessage());
            return false;
        }
    }
}
