/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class database_connector
{
 
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String ip = "//3.227.166.251/U05Wal";

    private static final String url = "jdbc:mysql://3.227.166.251/U05Wal";

    private static final String driver = "com.mysql.jdbc.Driver";

    private static final String username = "U05Wal";
    private static final String password = "53688623841";

    private static Connection conn = null;
    
    public static Connection connection_open()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connection to SQL database made");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(SQLException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        return conn;
    }
    public static void connection_close()
    {
        try
        {
            conn.close();
            System.out.println("\"Connection to SQL database closed");
        }
        catch(SQLException e)
        {

        }
    } 
    
    public static Connection getConnection () {
        return conn;
    }
}
