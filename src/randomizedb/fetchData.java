package randomizedb;
import java.io.FileReader;

public class fetchData
{
    static final String UBUNTUDIR = "/home/isuru/Documents/SLIIT/Y2 S1/ST2/Project/Database";
    static final String WINDIR = "E:\\Isuru Udukala\\Documents\\SLIIT\\Y2 S1\\ST2\\Project\\Database";
    static String location = UBUNTUDIR;
    
    static String validate(extractData edata)
    {
        StringBuilder strb = new StringBuilder();
        strb.append(edata.validate() ? "Valid. Parsed " : "Invalid");
        strb.append(edata.getEntryCount()).append(" entries");

        return strb.toString();
    }
    static String passengers()
    {
        String pstring = fetch(location + "/passenger list.txt");
        //String pstring=fetch("E:\\Isuru Udukala\\Documents\\SLIIT\\Y2 S1\\ST2\\Project\\Database\\passenger list.txt");
        return pstring;
    }
    static String addresses()
    {
        return fetch(location + "/address list.txt");
        //return fetch("E:\\Isuru Udukala\\Documents\\SLIIT\\Y2 S1\\ST2\\Project\\Database\\address list.txt");
    }
    static String airports()
    {
        return fetch(location + "/airports list.txt");
        //return fetch("E:\\Isuru Udukala\\Documents\\SLIIT\\Y2 S1\\ST2\\Project\\Database\\airports list.txt");
    }    
    private static String fetch(String path)
    {
        StringBuilder data = new StringBuilder();
        int temp = 0;
        FileReader fr = null;
        try
        {
            fr = new FileReader(path);
            while ((temp = fr.read()) != -1)
            {
                data.append(Character.toChars(temp));
            }
        }
        catch (Exception e)
        {
            System.out.println("Failed to read file. Exiting with error code -6\n" + e);
        }
        return data.toString();
    }
}