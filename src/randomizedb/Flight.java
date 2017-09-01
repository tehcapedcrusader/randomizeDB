package randomizedb;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Isuru Udukala
 */
public class Flight
{
    static final String TABLE_NAME = "flights";
    static final String DECLARATION = "create table " + TABLE_NAME + "(flightno char(4),origin char(3),destination char(3),departure datetime,landing datetime,primary key(flightno),foreign key(origin) references airports(iata),foreign key(destination) references airports(iata));";
    static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00.0");

    String fno, origin, destination;
    LocalDateTime departure, landing;

    Flight(String fno, String origin, String destination, LocalDateTime departure, LocalDateTime landing)
    {
        this.fno = fno;
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.landing = landing;
    }

    @Override
    public String toString()
    {
        StringBuilder strb = new StringBuilder();
        strb.append("Flight no\t: ").append(fno);
        strb.append("\nOrigin\t\t: ").append(origin);
        strb.append("\nDestination\t: ").append(destination);
        strb.append("\nDeparture\t: ").append(departure);
        strb.append("\nLanding\t\t: ").append(landing);
        return strb.toString();
    }

    void consolidate(Connection con)
    {
        try
        {
            PreparedStatement prp = con.prepareStatement("insert into flights values(?,?,?,?,?)");
            prp.setString(1, fno);
            prp.setString(2, origin);
            prp.setString(3, destination);
            prp.setString(4, departure.toString());
            prp.setString(5, landing.toString());
            prp.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("consolidating flights failed." + e);
            System.exit(-8);
        }
    }

}

class generateFlights
{
    static Flight[] parseObjects(Connection con, int flimit)
    {
        int counter = 0;
        String fno, origin, destination;
        LocalDateTime departure, landing;
        Flight[] flist = new Flight[flimit];
        String fnumbers[] = new String[flimit];
        Airport alist[] = null;
        try
        {
            PreparedStatement prp = con.prepareStatement("select * from airports");
            ResultSet rs = prp.executeQuery();
            alist = generateAirports.parseFromRS(rs);
        }
        catch (SQLException e)
        {
            System.out.println("flights failed");
        }
        int orgrand[] = randomize.array(alist.length, alist.length);
        int dstrand[] = randomize.array(alist.length, alist.length);
        boolean unique;
        for (int i = 0; i < flimit; i++)
        {
            unique = true;
            fno = "F" + randomize.inRange(333, 999);

            for (int j = 0; j < fnumbers.length; j++)
                if (fno.equals(fnumbers[j]))
                    unique = false;
            if (!unique)
            {
                i--;
                break;
            }
            fnumbers[i] = fno;

            origin = alist[orgrand[i]].iata;
            destination = alist[dstrand[i]].iata;

            departure = getRandomDateTime(0);
            landing = getRandomDateTime(1);

            flist[i] = new Flight(fno, origin, destination, departure, landing);
        }
        return flist;
    }

    static LocalDateTime getRandomDateTime(int offset)
    {
        //offset in days
        StringBuilder dep = new StringBuilder();
        dep.append(LocalDate.now().plusDays(offset)).append("T");
        int hour = randomize.inRange(1, 23);
        dep.append((hour < 10) ? "0" : "").append(hour).append(":");
        dep.append((randomize.inRange(0, 1) == 0) ? "00" : "30");
        return LocalDateTime.parse(dep);
    }

    static Flight[] parseFromRS(ResultSet rs)
    {
        int cols = 5;
        Flight[] ftemp = null;
        int count = 0;
        String rsdata[] = new String[cols];
        try
        {
            while (rs.next())
            { count++; }
            ftemp = new Flight[count];
            rs.beforeFirst();

            for (int i = 0; i < count; i++)
            {
                rs.next();
                for (int j = 0; j < cols; j++)
                    rsdata[j] = rs.getString(j + 1);

                String fno = rsdata[0];
                String origin = rsdata[1];
                String destination = rsdata[2];
                LocalDateTime departure = LocalDateTime.parse(rsdata[3], Flight.DATE_FORMAT);
                LocalDateTime landing = LocalDateTime.parse(rsdata[4], Flight.DATE_FORMAT);

                ftemp[i] = new Flight(fno, origin, destination, departure, landing);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Parsing from resultSet failed. exiting with error code -6\n" + e);
        }
        return ftemp;
    }
}