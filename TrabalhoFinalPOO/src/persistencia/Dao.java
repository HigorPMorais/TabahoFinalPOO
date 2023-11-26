package persistencia;

import java.sql.*;

public abstract class Dao {
    private Connection conexao;
    
    public Dao() {
        conexao = DBConnection.getConnection();
    }
    
    
    public Connection getConexao() {
        return conexao;
    }
    
    public ResultSet consultaSQL(String comandoSql) throws SQLException {
        ResultSet rs = conexao.createStatement().executeQuery(comandoSql);
        return rs;
    }
    
    protected int gerarProximoId(String tabela, String coluna){
        try {
            ResultSet rs = consultaSQL("select max("+coluna+") from "+tabela);
            if(rs.next()){
                int valor = rs.getInt(1) + 1;
                return valor>0? valor: 1;
            }
        } catch (SQLException ex) {
            System.out.println("falha ao gerar próximo ID\n"+ ex.getMessage());
            return -1;
        }
        return 0;
        
    }
    
    public PreparedStatement criarPreparedStatement(String sql) throws SQLException{
        return conexao.prepareStatement(sql);
    }
    
    public void executeSql(String sql) throws SQLException{
        Statement st =  conexao.createStatement();
        st.executeUpdate(sql);
        st.close();
    }
}