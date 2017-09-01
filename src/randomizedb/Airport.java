package randomizedb;

import java.sql.*;

public class Airport
{
    final static String TABLE_NAME = "Airports";
    final static String DECLARATION = "create table " + TABLE_NAME + "(Name varchar(50), IATA char(3) primary key, Country varchar(50), City varchar(50));";

    String name, iata, country, city;

    Airport(String name, String iata, String country, String city)
    {
        this.name = name;
        this.iata = iata;
        this.country = country;
        this.city = city;
    }

    @Override
    public String toString()
    {
        StringBuilder string = new StringBuilder();
        string.append("\nName\t :  ").append(name);
        string.append("\nIATA\t :  ").append(iata);
        string.append("\nCountry\t :  ").append(country);
        string.append("\nCity\t :  ").append(city);

        return string.toString();
    }

    void consolidate(Connection con)
    {
        try
        {
            PreparedStatement prp = con.prepareStatement("insert into airports values(?,?,?,?);");
            prp.setString(1, name);
            prp.setString(2, iata);
            prp.setString(3, country);
            prp.setString(4, city);
            prp.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Writing to airport table failed.exiting with error code -5");
            System.out.println(this);
            System.exit(-5);
        }
    }
}

class generateAirports
{
    static Airport[] parseObjects(extractData airdata)
    {
        String name, iata, country, city;
        Airport alist[] = new Airport[airdata.getrows()];
        String templist[][] = airdata.createarray();
        for (int i = 0; i < alist.length; i++)
        {
            name = templist[i][3];
            iata = templist[i][2];
            country = templist[i][0];
            city = templist[i][1];
            alist[i] = new Airport(name, iata, country, city);
        }

        return alist;
    }

    static Airport[] parseFromRS(ResultSet rs)
    {
        String name, iata, country, city;
        int counter = 0;
        Airport[] alist = null;
        try
        {
            while (rs.next())
            { counter++; }
            rs.beforeFirst();
            alist = new Airport[counter];
            for (int i = 0; i < counter; i++)
            {
                rs.next();
                name = rs.getString(1);
                iata = rs.getString(2);
                country = rs.getString(3);
                city = rs.getString(4);
                alist[i] = new Airport(name, iata, country, city);
            }
        }
        catch (SQLException e)
        {
            System.out.println("parsing from RS failed on airports");
            System.exit(-9);
        }
        return alist;
    }
}