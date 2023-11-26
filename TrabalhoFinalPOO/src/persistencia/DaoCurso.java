package persistencia;

import java.sql.*;
import java.util.ArrayList;
import modelo.Curso;

public class DaoCurso extends Dao{
    ArrayList<Curso> cursos = new ArrayList<>();
    public ArrayList<Curso> carregarCursos(Curso cu) {
        try {
            String sql = "select * from curso";
            ResultSet rs = consultaSQL(sql);
            while (rs.next()) {
                cu.setIdCurso(rs.getInt("idCurso"));
                cu.setNome(rs.getString("nome"));
                cu.setCargaHoraria(rs.getString("cargaHoraria"));
                cu.setQtdSemestres(rs.getString("qtdSemestres"));

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
                cu.setCargaHoraria(rs.getString("cargaHoraria"));
                cu.setQtdSemestres(rs.getString("qtdSemestres"));
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
            ps.setString(3, cu.getCargaHoraria());
            ps.setString(4, cu.getQtdSemestres());

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
            ps.setString(2, cu.getCargaHoraria());
            ps.setString(3, cu.getQtdSemestres());

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