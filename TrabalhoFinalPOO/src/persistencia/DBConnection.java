
package persistencia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBConnection {
    private static Connection con = null;
  
    private static void criarConexao()
    {
        String url = "jdbc:mysql://localhost:3306/aipapai";
        String user = "root";
        String pass = "univel";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
        }
        catch (ClassNotFoundException | SQLException e) {
            System.out.println("Falha na Matrix da conexão do BDConec1\n" + e.getMessage());
        }
    }

    public static Connection getConnection()
    {
        try {
            if(con==null || con.isClosed()){
              criarConexao();
            }
            return con; 
        } catch (SQLException ex) {
            System.out.println("Falha na Matrix BDConec2\n" + ex.getMessage());
            return null;
        }
    }
}