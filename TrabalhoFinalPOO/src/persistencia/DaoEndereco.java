package persistencia;
import java.sql.*;
import java.util.ArrayList;
import modelo.Endereco;

public class DaoEndereco extends Dao{
    ArrayList<Endereco> enderecos = new ArrayList<>();
    
    public ArrayList<Endereco> carregarEnderecos(){
        try {
            String sql = "select * from endereco";
            ResultSet rs = consultaSQL(sql);
            while(rs.next()){
                Endereco en = new Endereco();
                en.setIdEndereco(rs.getInt("idEndereco"));
                en.setCidade(rs.getString("cidade"));
                en.setRua(rs.getString("rua"));
                en.setNumero(rs.getString("numero"));
                
                enderecos.add(en);
            }
        } catch (SQLException ex) {
            System.out.println("Falha ao carregar endereços!\n"+ ex.getMessage());
        }
        return enderecos;
    }
    
    public Endereco carregarEnderecoPorId(int idEndereco){
        Endereco en = null;
        try {
            String sql = "select * from endereco where idEndereco = "+idEndereco;
            ResultSet rs = consultaSQL(sql);
            if(rs.next()){
                en = new Endereco();
                en.setIdEndereco(rs.getInt("idEndereco"));
                en.setCidade(rs.getString("cidade"));
                en.setRua(rs.getString("rua"));
                en.setNumero(rs.getString("numero"));
            }
        } catch (SQLException ex) {
            System.out.println("Falha ao carregar enereço!\n"+ ex.getMessage());
        }
        return en;
    }
    
    public boolean salvar(Endereco en){
        try {
            String sql = """
                         INSERT INTO endereco(
                          idEndereco, cidade, rua, numero )
                          VALUES (?, ?, ?, ?);""";
            
            PreparedStatement ps = criarPreparedStatement(sql);
            en.setIdEndereco(gerarProximoId("endereco","idEndereco"));
            ps.setInt(1, en.getIdEndereco());
            ps.setString(2, en.getCidade());
            ps.setString(3, en.getRua());
            ps.setString(4, en.getNumero());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Falha ao salvar endereço na matrix\n" + ex.getMessage());
            return false;
        }
    }
    
    public boolean atualizar(Endereco en){
        try {
            String sql = """
                         UPDATE endereco
                          SET cidade=?, rua=?, numero=?
                          WHERE idEndereco ="""+en.getIdEndereco();
            
            PreparedStatement ps = criarPreparedStatement(sql);
            ps.setString(1, en.getCidade());
            ps.setString(2, en.getRua());
            ps.setString(3, en.getNumero());
            
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println("Falha ao editar enereço da Matrix\n" + ex.getMessage());
            return false;
        }
    }
    
    public String comandoSqlRemover(Endereco en){
        return "DELETE FROM enereco WHERE idEndereco = "+en.getIdEndereco();
    }
    
    public boolean remover(Endereco en){
        try {
            executeSql(comandoSqlRemover(en));
            return true;
        } catch (SQLException e) {
            System.out.println("Falha ao remover endereço\n"+e.getMessage());
            return false;
        }
    }
    
   public void listar(Endereco en){
       for (Endereco endereco : enderecos) {
           System.out.println("ID: "+endereco.getIdEndereco());
           System.out.println("Cidade: " + endereco.getCidade());
           System.out.println("Rua: " + endereco.getRua());
           System.out.println("Numero: " + endereco.getNumero());
       }
   }
}