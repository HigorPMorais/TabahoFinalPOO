package persistencia;

import java.sql.*;
import java.util.ArrayList;
import modelo.Curso;

public class DaoCurso extends Dao{
    private DaoEndereco daoEndereco;
    private DaoDocente daoDocente;
    
    public DaoCurso() {
        daoDocente = new DaoDocente();
        
    }
    
    ArrayList<Curso> cursos = new ArrayList<>();
    public ArrayList<Curso> carregarCursos(Curso cu) {
        try {
            String sql = "select * from curso";
            ResultSet rs = consultaSQL(sql);
            while (rs.next()) {
                cu.setIdCurso(rs.getInt("idCurso"));
                cu.setNome(rs.getString("nome"));
                cu.setCargaHoraria(rs.getInt("cargaHoraria"));
                cu.setQtdSemestres(rs.getInt("qtdSemestres"));
                
                if (rs.getObject("idFuncionario", Integer.class) != null) {
                   cu.getCoordenador().setIdFuncionario(rs.getInt("idFuncionario"));
                   cu.getCoordenador().setNome(rs.getString("nome"));
                   cu.getCoordenador().setFormacao(rs.getString("formacao"));
                   cu.getCoordenador().setCtps(rs.getString("ctps"));
                   cu.getCoordenador().setCpf(rs.getString("cpf"));
                   cu.getCoordenador().setDataNascimento(rs.getLocalDate("dataNascimento"));
                   cu.getCoordenador().setEmail(rs.getString("email"));
                   cu.getCoordenador().setGenero(rs.getString("genero"));
                   cu.getCoordenador().setSalario(rs.getDouble("salario"));
                   cu.getCoordenador().getEndereco().setIdEndereco(rs.getInt("idEndereco"));
                   cu.getCoordenador().getEndereco().setCidade(rs.getString("cidade"));
                   cu.getCoordenador().getEndereco().setRua(rs.getString("rua"));
                   cu.getCoordenador().getEndereco().setNumero(rs.getString("numero"));
                }
                cursos.add(cu);
            }
        } catch (SQLException ex) {
            System.out.println("Falha ao carregar a matrix do CUrso!\n" + ex.getMessage());
        }
        return cursos;
    }

    public Curso carregarCursoPorId(int idCurso) {
        Curso cu = null;
        try {
            String sql = "select * from curso where idCurso = " + idCurso;
            ResultSet rs = consultaSQL(sql);
            if (rs.next()) {
                cu = new Curso();
                cu.setIdCurso(rs.getInt("idCurso"));
                cu.setNome(rs.getString("nome"));
                cu.setCargaHoraria(rs.getInt("cargaHoraria"));
                cu.setQtdSemestres(rs.getInt("qtdSemestres"));
                
                if (rs.getObject("idFuncionario", Integer.class) != null) {
                   cu.getCoordenador().setIdFuncionario(rs.getInt("idFuncionario"));
                   cu.getCoordenador().setNome(rs.getString("nome"));
                   cu.getCoordenador().setFormacao(rs.getString("formacao"));
                   cu.getCoordenador().setCtps(rs.getString("ctps"));
                   cu.getCoordenador().setCpf(rs.getString("cpf"));
                   cu.getCoordenador().setDataNascimento(rs.getLocalDate("dataNascimento"));
                   cu.getCoordenador().setEmail(rs.getString("email"));
                   cu.getCoordenador().setGenero(rs.getString("genero"));
                   cu.getCoordenador().setSalario(rs.getDouble("salario"));
                   cu.getCoordenador().getEndereco().setIdEndereco(rs.getInt("idEndereco"));
                   cu.getCoordenador().getEndereco().setCidade(rs.getString("cidade"));
                   cu.getCoordenador().getEndereco().setRua(rs.getString("rua"));
                   cu.getCoordenador().getEndereco().setNumero(rs.getString("numero"));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Falha ao carregar cuereço!\n" + ex.getMessage());
        }
        return cu;
    }

    public boolean salvar(Curso cu) {
        try {
            String sql = """
                         INSERT INTO curso(
                          idCurso, nome, cargaHoraria, qtdSemestres )
                          VALUES (?, ?, ?, ?);""";

            PreparedStatement ps = criarPreparedStatement(sql);
            cu.setIdCurso(gerarProximoId("curso", "idCurso"));
            ps.setInt(1, cu.getIdCurso());
            ps.setString(2, cu.getNome());
            ps.setInt(3, cu.getCargaHoraria());
            ps.setInt(4, cu.getQtdSemestres());
            
            if (cu.getCoordenador().getEndereco() != null) {
                if (cu.getCoordenador().getEndereco().getIdEndereco() == null || cu.getCoordenador().getEndereco().getIdEndereco() == 0) {
                    daoEndereco.salvar(cu.getCoordenador().getEndereco());
                }
                ps.setInt(5, cu.getCoordenador().getEndereco().getIdEndereco());
            } else {
                ps.setObject(5, null);
            }

            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Falha ao salvar curso na matrix\n" + ex.getMessage());
            return false;
        }
    }

    public boolean atualizar(Curso cu) {
        try {
            String sql = """
                         UPDATE curso
                          SET nome=?, cargaHoraria=?, qtdSemestres=?
                          WHERE idCurso =""" + cu.getIdCurso();

            PreparedStatement ps = criarPreparedStatement(sql);
            ps.setString(1, cu.getNome());
            ps.setInt(2, cu.getCargaHoraria());
            ps.setInt(3, cu.getQtdSemestres());

            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Falha ao editar curso da Matrix\n" + ex.getMessage());
            return false;
        }
    }

    public String comandoSqlRemover(Curso cu) {
        return "DELETE FROM curso WHERE idCurso = " + cu.getIdCurso();
    }

    public boolean remover(Curso cu) {
        try {
            executeSql(comandoSqlRemover(cu));
            return true;
        } catch (SQLException e) {
            System.out.println("Falha ao remover curso da matrix\n" + e.getMessage());
            return false;
        }
    }
    
    public void listar(){
        for (Curso curso : cursos) {
            System.out.println("ID: " +curso.getIdCurso());
            System.out.println("Nome: "+curso.getNome());
            System.out.println("Carga Horaria: " + curso.getCargaHoraria());
            System.out.println("Quantidade de Semestres: " +curso.getQtdSemestres());
        }
    }
}