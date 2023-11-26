package persistencia;

import java.sql.*;
import modelo.Aluno;
import java.util.*;

public class DaoAluno extends Dao {

    private DaoEndereco daoEndereco;

    public DaoAluno() {
        daoEndereco = new DaoEndereco();
    }

    public ArrayList<Aluno> carregarAlunos() {
        ArrayList<Aluno> listaAlunos = new ArrayList<>();
        try {
            String sql = """
                         SELECT * FROM aluno 
                         left join endereco as ed on idEndereco = ed.idEndereco""";
            ResultSet rs = consultaSQL(sql);
            while (rs.next()) {
                Aluno p = new Aluno();
                p.setIdAluno(rs.getInt("idAluno"));
                p.setNome(rs.getString("nome"));
                p.setRa(rs.getString("ra"));
                p.setCpf(rs.getString("cpf"));

                if (rs.getObject("idEndereco", Integer.class) != null) {
                    p.getEndereco().setIdEndereco(rs.getInt("idEndereco"));
                    p.getEndereco().setCidade(rs.getString("cidade"));
                    p.getEndereco().setRua(rs.getString("rua"));
                    p.getEndereco().setNumero(rs.getString("numero"));
                }
                listaAlunos.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Falha ao carregar alunos!\n" + e.getMessage());
        }
        return listaAlunos;
    }

    public Aluno carregarPorId(int idAluno) {
        Aluno cl = null;
        try {
            String sql = "SELECT * FROM aluno \n"
                    + "left join endereco as ed on idEndereco = ed.idEndereco"
                    + " where aluno.idAluno = " + idAluno;
            ResultSet rs = consultaSQL(sql);
            if (rs.next()) {
                cl = new Aluno();
                cl.setIdAluno(rs.getInt("idAluno"));
                cl.setNome(rs.getString("nome"));
                cl.setRa(rs.getString("ra"));
                cl.setCpf(rs.getString("cpf"));

                if (rs.getObject("idEndereco", Integer.class) != null) {
                    cl.getEndereco().setIdEndereco(rs.getInt("idEndereco"));
                    cl.getEndereco().setCidade(rs.getString("cidade"));
                    cl.getEndereco().setRua(rs.getString("rua"));
                    cl.getEndereco().setNumero(rs.getString("numero"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Falha ao carregar aluno!\n"
                    + e.getMessage());
        }
        return cl;
    }

    public boolean salvar(Aluno aluno) {
        try {
            String sql = """
                         INSERT INTO aluno(
                         idAluno, nome, ra, cpf, idEndereco)
                         VALUES (?, ?, ?, ?, ?)""";

            PreparedStatement ps = criarPreparedStatement(sql);
            aluno.setIdAluno(gerarProximoId("aluno","idAluno"));
            ps.setInt(1, aluno.getIdAluno());
            ps.setString(2, aluno.getNome());
            ps.setString(3, aluno.getRa());
            ps.setString(4, aluno.getCpf());

            if (aluno.getEndereco() != null) {
                if (aluno.getEndereco().getIdEndereco() == null || aluno.getEndereco().getIdEndereco() == 0) {
                    daoEndereco.salvar(aluno.getEndereco());
                }
                ps.setInt(5, aluno.getEndereco().getIdEndereco());
            } else {
                ps.setObject(5, null);
            }
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            try {
                getConexao().rollback();
            } catch (SQLException ex1) {
                System.out.println("Falhar ao realizar RollBack");
            }
            System.out.println("Falha ao salvar Aluno\n" + ex.getMessage());
            return false;
        }
    }

    public boolean atualizar(Aluno aluno) {
        try {
            String sql = """
                         UPDATE aluno
                         SET nome=?,ra= ?, cpf=?, tel=?, idEndereco=? 
                         WHERE idAluno= """ + aluno.getIdAluno();

            PreparedStatement ps = criarPreparedStatement(sql);
            ps.setString(1, aluno.getNome());
            ps.setString(2, aluno.getRa());
            ps.setString(3, aluno.getCpf());
            
            if (aluno.getEndereco() != null) {
                if (aluno.getEndereco().getIdEndereco() == null) {
                    daoEndereco.salvar(aluno.getEndereco());
                } else {
                    daoEndereco.atualizar(aluno.getEndereco());
                }
                ps.setInt(4, aluno.getEndereco().getIdEndereco());
            } else {
                ps.setObject(4, null);
            }

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Falha ao editar aluno!\n" + e.getMessage());
            return false;
        }
    }

    public boolean remover(Aluno aluno) {
        try {
            String sql = "DELETE FROM aluno\n"
                    + " WHERE idAluno =" + aluno.getIdAluno()
                    + "; " + daoEndereco.comandoSqlRemover(aluno.getEndereco());

            executeSql(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("Falha ao remover aluno!\n" + e.getMessage());
            return false;
        }
    }
}