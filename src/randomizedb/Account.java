package randomizedb;
import java.sql.*;

public class Account
{
    static final String DEFAULT_PASS = "pass";
    static final String TABLE_NAME = "accounts";
    static final String DECLARATION = "create table " + TABLE_NAME + "(username varchar(30),password int,actype varchar(5),passengerid char(7),constraint pk primary key(username),constraint fk foreign key(passengerid) references passengers(passengerid));";
    static final String ADMINS[] = {"Isuru Udukala", "Gimhani Paranawithana", "Janitha Thennakoon", "Hasini Subasinghe"};
    String usern, passid, actype;
    int passw;

    //username, password int, actype, passengerid
    Account(String username, int password, String actp, String passengerid)
    {
        usern = username;
        passw = password;
        passid = passengerid;
        actype = actp;
    }
    @Override
    public String toString()
    {
        StringBuilder strb = new StringBuilder();
        strb.append("Username\t:").append(usern);
        strb.append("\nPassword\t:").append(passw);
        strb.append("\nPassengerID\t:").append(passid);
        strb.append("\nType\t\t:").append(actype);

        return strb.toString();
    }
    void consolidate(Connection con)
    {
        try
        {
            PreparedStatement prp = con.prepareStatement("insert into accounts values(?,?,?,?);");
            prp.setString(1, usern);
            prp.setInt(2, passw);
            prp.setString(3, actype);
            prp.setString(4, passid);
            prp.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("Consolidating accounts failed. exiting with error code -8");
            System.exit(-8);
        }
    }
}
class generateAccounts
{
    static Passenger[] fetchPassengers(Connection con)
    {
        Passenger paslist[] = null;
        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from passengers");
            paslist = generatePassengers.parseFromRS(rs);
        }
        catch (SQLException e)
        {
            System.out.println("parsing passenger objects failed. exiting with error code -7\n" + e);
            System.exit(-7);
        }
        return paslist;
    }
    static String[][] generateUsernames(Passenger[] paslist)
    {
        boolean admin;
        String usernames[][] = new String[paslist.length][3];
        for (int i = 0; i < paslist.length; i++)
        {
            admin = false;
            StringBuilder gen = new StringBuilder();
            String firstname = paslist[i].name.toLowerCase().split(" ")[0];
            gen.append(firstname).append(randomize.inRange(100, 999));
            usernames[i][0] = gen.toString();
            usernames[i][1] = paslist[i].passengerid;
            for (int j = 0; j < Account.ADMINS.length; j++)
                if (paslist[i].name.equalsIgnoreCase(Account.ADMINS[j]))
                    admin = true;
            if (admin)
                usernames[i][2] = "Admin";
            else
                usernames[i][2] = "User";
        }
        return usernames;
    }
    static Account[] generateCredentials(String[][] credentials)
    {
        Account[] aclist = new Account[credentials.length];
        for (int i = 0; i < aclist.length; i++)
        {
            String usern = credentials[i][0], passid = credentials[i][1], pass = Account.DEFAULT_PASS, actype = credentials[i][2];

            aclist[i] = new Account(usern, pass.hashCode(), actype, passid);
        }
        return aclist;
    }
}
