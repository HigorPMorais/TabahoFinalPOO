package persistencia;

import java.sql.*;
import java.util.ArrayList;
import modelo.Funcionario;

public class DaoFuncionario extends Dao{
    private DaoEndereco daoEndereco;

    public DaoFuncionario() {
        daoEndereco = new DaoEndereco();
    }

    public ArrayList<Funcionario> carregarFuncionarios() {
        ArrayList<Funcionario> listaFuncionarios = new ArrayList<>();
        try {
            String sql = """
                         SELECT * FROM funcionario 
                         left join endereco as ed on idEndereco = ed.idEndereco""";
            ResultSet rs = consultaSQL(sql);
            while (rs.next()) {
                Funcionario p = new Funcionario();
                p.setIdFuncionario(rs.getInt("idFuncionario"));
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
                listaFuncionarios.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Falha ao carregar funcionarios!\n" + e.getMessage());
        }
        return listaFuncionarios;
    }

    public Funcionario carregarPorId(int idFuncionario) {
        Funcionario cl = null;
        try {
            String sql = "SELECT * FROM funcionario \n"
                    + "left join endereco as ed on idEndereco = ed.idEndereco"
                    + " where funcionario.idFuncionario = " + idFuncionario;
            ResultSet rs = consultaSQL(sql);
            if (rs.next()) {
                cl = new Funcionario();
                cl.setIdFuncionario(rs.getInt("idFuncionario"));
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
            System.out.println("Falha ao carregar funcionario!\n"
                    + e.getMessage());
        }
        return cl;
    }

    public boolean salvar(Funcionario funcionario) {
        try {
            String sql = """
                         INSERT INTO funcionario(
                         idFuncionario, nome, ra, cpf, idEndereco)
                         VALUES (?, ?, ?, ?, ?)""";

            PreparedStatement ps = criarPreparedStatement(sql);
            funcionario.setIdFuncionario(gerarProximoId("funcionario","idFuncionario"));
            ps.setInt(1, funcionario.getIdFuncionario());
            ps.setString(2, funcionario.getNome());
            ps.setString(3, funcionario.getCpf());
            ps.setDate(4, funcionario.getDataNascimento());
            ps.setString(5, funcionario.getEmail());
            ps.setString(6, funcionario.getGenero());
            ps.setString(7, funcionario.getCtps());
            ps.setDouble(8, funcionario.getSalario());

            if (funcionario.getEndereco() != null) {
                if (funcionario.getEndereco().getIdEndereco() == null || funcionario.getEndereco().getIdEndereco() == 0) {
                    daoEndereco.salvar(funcionario.getEndereco());
                }
                ps.setInt(9, funcionario.getEndereco().getIdEndereco());
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
            System.out.println("Falha ao salvar Funcionario\n" + ex.getMessage());
            return false;
        }
    }

    public boolean atualizar(Funcionario funcionario) {
        try {
            String sql = """
                         UPDATE funcionario
                         SET nome=?,ra= ?, cpf=?, tel=?, idEndereco=? 
                         WHERE idFuncionario= """ + funcionario.getIdFuncionario();

            PreparedStatement ps = criarPreparedStatement(sql);
            ps.setString(1, funcionario.getNome());
            ps.setString(2, funcionario.getCpf());
            ps.setDate(3, funcionario.getDataNascimento());
            ps.setString(4, funcionario.getEmail());
            ps.setString(5, funcionario.getGenero());
            ps.setString(6, funcionario.getCtps());
            ps.setDouble(7, funcionario.getSalario());
            
            if (funcionario.getEndereco() != null) {
                if (funcionario.getEndereco().getIdEndereco() == null) {
                    daoEndereco.salvar(funcionario.getEndereco());
                } else {
                    daoEndereco.atualizar(funcionario.getEndereco());
                }
                ps.setInt(8, funcionario.getEndereco().getIdEndereco());
            } else {
                ps.setObject(8, null);
            }

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Falha ao editar funcionario!\n" + e.getMessage());
            return false;
        }
    }

    public boolean remover(Funcionario funcionario) {
        try {
            String sql = "DELETE FROM funcionario\n"
                    + " WHERE idFuncionario =" + funcionario.getIdFuncionario()
                    + "; " + daoEndereco.comandoSqlRemover(funcionario.getEndereco());

            executeSql(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("Falha ao remover funcionario!\n" + e.getMessage());
            return false;
        }
    }
}
