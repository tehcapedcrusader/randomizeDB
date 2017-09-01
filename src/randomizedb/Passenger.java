package randomizedb;

import java.sql.*;

public class Passenger
{
    static final String TABLE_NAME = "passengers";
    static final String DECLARATION = "create table " + TABLE_NAME + "(name varchar(50),gender varchar(6),age int, address varchar(120),passport char(8),passengerid char(7) primary key,phone varchar(15));";
    //name, gender, age, address, passport, passengerid (PK), phone COLS=7
    int age;
    String name, gender, address, passport, passengerid, phone;

    Passenger(String name, String gender, int age, String address, String passport, String passengerid, String phone)
    {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.passport = passport;
        this.passengerid = passengerid;
        this.phone = phone;
    }

    @Override
    public String toString()
    {
        StringBuilder strb = new StringBuilder();
        strb.append("Name\t\t: ").append(name);
        strb.append("\nGender\t\t: ").append(gender);
        strb.append("\nAge\t\t: ").append(age);
        strb.append("\nPassport ID\t: ").append(passport);
        strb.append("\nPassenger ID\t: ").append(passengerid);
        strb.append("\nAddress\t\t: ").append(address);
        strb.append("\nPhone\t\t: ").append(phone);

        return strb.toString();
    }

    void consolidate(Connection con)
    {
        try
        {
            PreparedStatement prp = con.prepareStatement("insert into passengers values(?,?,?,?,?,?,?);");
            prp.setString(1, name);
            prp.setString(2, gender);
            prp.setInt(3, age);
            prp.setString(4, address);
            prp.setString(5, passport);
            prp.setString(6, passengerid);
            prp.setString(7, phone);
            prp.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("consolidating passenger object failed");
            System.exit(-6);
        }
    }
}

class generatePassengers
{
    static Passenger[] parseObjects()
    {
        boolean unique;
        extractData passdata = new extractData(fetchData.passengers(), 2);
        String passlist[][] = passdata.createarray();
        String pidlist[] = new String[passlist.length];

        extractData addrdata = new extractData(fetchData.addresses(), 1);
        String addrlist[][] = addrdata.createarray();

        int rnd[] = randomize.array(passlist.length, addrlist.length);

        Passenger plist[] = new Passenger[passlist.length];
        for (int i = 0; i < plist.length; i++)
        {
            unique = true;
            String name = passlist[i][0];
            String gender = passlist[i][1];
            String address = addrlist[rnd[i]][0];
            int age = randomize.inRange(25, 85);
            String passport = "M" + randomize.inRange(2333333, 8333333);

            StringBuilder phonenum = new StringBuilder();
            phonenum.append("+").append(randomize.inRange(65, 95));
            phonenum.append("-").append(randomize.inRange(123456789, 923456789));
            String phone = phonenum.toString();

            String passengerid = "P" + randomize.inRange(233333, 933333);
            for (int j = 0; j < pidlist.length; j++)
                if (passengerid.equals(pidlist[j]))
                    unique = false;
            if (!unique)
            {
                i--;
                break;
            }
            plist[i] = new Passenger(name, gender, age, address, passport, passengerid, phone);
        }
        return plist;
    }

    static Passenger[] parseFromRS(ResultSet rs)
    {
        int cols = 7;
        Passenger[] ptemp = null;
        int count = 0;
        String rsdata[] = new String[cols];
        try
        {
            while (rs.next())
            { count++; }
            ptemp = new Passenger[count];
            rs.beforeFirst();

            for (int i = 0; i < count; i++)
            {
                rs.next();
                for (int j = 0; j < cols; j++)
                    rsdata[j] = rs.getString(j + 1);

                String name = rsdata[0];
                String gender = rsdata[1];
                int age = Integer.parseInt(rsdata[2]);
                String address = rsdata[3];
                String passport = rsdata[4];
                String passengerid = rsdata[5];
                String phone = rsdata[6];

                ptemp[i] = new Passenger(name, gender, age, address, passport, passengerid, phone);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Parsing from resultSet failed. exiting with error code -6\n" + e);
        }
        return ptemp;
    }
}