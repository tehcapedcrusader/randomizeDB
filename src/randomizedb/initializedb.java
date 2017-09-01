package randomizedb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class initializedb
{
    final static String MYSQL_URL = "jdbc:mysql://localhost:3306";
    final static String ATRS_URL = "jdbc:mysql://localhost:3306/atrsdb";
    
    static Connection connect(String url)
    {
        Connection con = null;
        try
        {
            con = DriverManager.getConnection(url, "root", "alpine");
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
        return con;
    }
    static Connection createDatabase()
    {
        Connection con = null;
        try
        {
            con = connect(MYSQL_URL);
            Statement stmt = con.createStatement();
            stmt.executeUpdate("drop database if exists atrsdb");
            stmt.executeUpdate("create database atrsdb");

            con = connect(ATRS_URL);
            stmt = con.createStatement();
            stmt.executeUpdate(Airport.DECLARATION);
            stmt.executeUpdate(Passenger.DECLARATION);
            stmt.executeUpdate(Account.DECLARATION);
            stmt.executeUpdate(Flight.DECLARATION);
            stmt.executeUpdate(Ticket.DECLARATION);
        }
        catch (SQLException e)
        {
            System.exit(-2);
        }
        return con;
    }
}