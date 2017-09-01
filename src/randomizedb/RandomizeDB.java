package randomizedb;

import java.sql.*;

public class RandomizeDB
{

    public static void main(String[] args) throws Exception
    {
        //        System.out.println("change");
        int ecount;

        System.out.println("fetching data from files..");
        extractData eairdata = new extractData(fetchData.airports(), 4);
        System.out.println("Airports\t: " + fetchData.validate(eairdata));

        extractData epdata = new extractData(fetchData.passengers(), 2);
        System.out.println("Passengers\t: " + fetchData.validate(epdata));

        extractData eadrdata = new extractData(fetchData.addresses(), 1);
        System.out.println("Addresses\t: " + fetchData.validate(eadrdata));

        System.out.print("\ncreating database...");
        Connection con = initializedb.createDatabase();
        System.out.println("\rcreated new database: atrsdb");


        System.out.print("consolidating airports: processing..");
        Airport airlist[] = generateAirports.parseObjects(eairdata);
        ecount = airlist.length;
        for (int i = 0; i < airlist.length; i++)
            airlist[i].consolidate(con);
        System.out.println("\rconsolidating airports: done. " + ecount + " entries written");


        System.out.print("consolidating passengers: processing..");
        Passenger plist[] = generatePassengers.parseObjects();
        ecount = plist.length;
        for (int i = 0; i < plist.length; i++)
            plist[i].consolidate(con);
        System.out.println("\rconsolidating passengers: done. " + ecount + " entries written");


        System.out.print("consolidating accounts: processing..");
        Passenger[] allpsgs = generateAccounts.fetchPassengers(con);
        String[][] credentials = generateAccounts.generateUsernames(allpsgs);
        Account[] aclist = generateAccounts.generateCredentials(credentials);
        ecount = aclist.length;
        for (int i = 0; i < aclist.length; i++)
            aclist[i].consolidate(con);
        System.out.println("\rconsolidating accounts: done. " + ecount + " entries written");


        int flightCount = 15;
        System.out.print("consolidating flights: processing..");
        Flight[] ftemp = generateFlights.parseObjects(con, flightCount);
        ecount = ftemp.length;
        for (int i = 0; i < flightCount; i++)
            ftemp[i].consolidate(con);
        System.out.println("\rconsolidating flights: done. " + ecount + " entries written");


        System.out.print("consolidating tickets: processing..");
        Ticket[] tlist = generateTickets.parseObjects(con);
        ecount = tlist.length;
        for (int i = 0; i < tlist.length; i++)
            tlist[i].consolidate(con);
        con.close();
        System.out.println("\rconsolidating tickets: done. " + ecount + " entries written");
    }
}