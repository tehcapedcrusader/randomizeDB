package randomizedb;

import java.sql.*;

/**
 * @author Isuru Udukala
 */
public class Ticket
{
    static final String TABLE_NAME = "tickets";
    static final String DECLARATION = "create table " + TABLE_NAME + "(ticketno char(5),flightno char(4),seatno char(4),class varchar(20),passengerid char(7),primary key(ticketno),foreign key(flightno) references flights(flightno),foreign key(passengerid) references passengers(passengerid));";
    String ticketno, fno, seatno, fclass, passengerid;

    Ticket(String ticketno, String fno, String seatno, String fclass, String passengerid)
    {
        this.ticketno = ticketno;
        this.fno = fno;
        this.seatno = seatno;
        this.fclass = fclass;
        this.passengerid = passengerid;
    }

    @Override
    public String toString()
    {
        StringBuilder strb = new StringBuilder();
        strb.append("Ticket no\t: ").append(ticketno);
        strb.append("\nFlight no\t: ").append(fno);
        strb.append("\nSeat no\t\t: ").append(seatno);
        strb.append("\nClass\t\t: ").append(fclass);
        strb.append("\nPassenger ID\t: ").append(passengerid);
        return strb.toString();
    }

    void consolidate(Connection con)
    {
        try
        {
            PreparedStatement prp = con.prepareStatement("insert into tickets values(?,?,?,?,?);");
            prp.setString(1, ticketno);
            prp.setString(2, fno);
            prp.setString(3, seatno);
            prp.setString(4, fclass);
            prp.setString(5, passengerid);
            prp.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("error consolidating tickets\n" + e);
        }
    }
}

class generateTickets
{
    static Ticket[] parseObjects(Connection con)
    {
        String ticketno, fno, seatno, fclass, passengerid;

        Passenger[] plist = null;
        Flight[] flist = null;
        try
        {
            PreparedStatement pprp = con.prepareStatement("select * from passengers");
            ResultSet prs = pprp.executeQuery();

            PreparedStatement fprp = con.prepareStatement("select * from flights");
            ResultSet frs = fprp.executeQuery();

            plist = generatePassengers.parseFromRS(prs);
            flist = generateFlights.parseFromRS(frs);
        }
        catch (SQLException e)
        {
            System.out.println("fetching passengers from db failed.\n" + e);
            System.exit(-11);
        }
        int count = plist.length;
        boolean unique = true;

        Ticket[] tlist = new Ticket[count];

        int flightRand[] = randomize.array(count, count);
        int pasRand[] = randomize.array(count, count);
        //String ticketno,String fno,String seatno,String fclass,String passengerid)
        for (int i = 0; i < count; i++)
        {
            fclass = null;
            ticketno = "T" + randomize.inRange(3333, 9999);
            seatno = "S" + randomize.inRange(333, 999);
            int fclasstemp = randomize.inRange(1, 3);
            if (fclasstemp == 1)
                fclass = "First class";
            else if (fclasstemp == 2)
                fclass = "Business class";
            else if (fclasstemp == 3)
                fclass = "Economy class";

            fno = flist[flightRand[i]].fno;
            passengerid = plist[pasRand[i]].passengerid;

            tlist[i] = new Ticket(ticketno, fno, seatno, fclass, passengerid);
        }
        return tlist;
    }
}