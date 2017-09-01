package randomizedb;

public class extractData
{
    String list;
    int cols = -1;

    extractData(String strlist, int columns)
    {
        list = strlist;
        cols = columns;
    }

    int getEntryCount()
    {
        int linecount = 1;
        for (int i = 0; i < list.length(); i++)
            if (list.charAt(i) == '\n')
                linecount++;
        return linecount;
    }

    boolean validate()
    {
        int tabcount = 0, linecount = 1;
        for (int i = 0; i < list.length(); i++)
            if (list.charAt(i) == '\t')
                tabcount++;
            else if (list.charAt(i) == '\n')
                linecount++;
        //System.out.println(tabcount + " - " + linecount);
        return linecount * (cols - 1) == tabcount;
    }

    void print()
    {
        for (int i = 0; i < list.length(); i++)
        {
            if (!Character.isLetter(list.charAt(i)))
                System.out.println(i + " - " + (int) list.charAt(i));
        }
    }

    int getrows()
    {
        int counter = 1;
        for (int i = 0; i < list.length(); i++)
            if (list.charAt(i) == '\n')
                counter++;
        return counter;
    }

    String[][] createarray()
    {
        if (!validate())
        {
            System.out.println("Invalid formatting on string list");
            System.exit(-1);
        }
        int count = getrows();
        String array[][] = new String[count][];

        //allocating for each String[]
        for (int i = 0; i < count; i++)
            array[i] = new String[cols];

        //splitting and storing in airports[]
        for (int i = 0; i < count; i++)
            for (int j = 0; j < cols; j++)
                array[i][j] = list.split("\n")[i].split("\t")[j];

        return array;
    }
}

class randomize
{
    static int[] array(int count, int limit)
    {
        boolean match = false;
        int tempint = -5;
        int temparray[] = new int[count];
        for (int i = 0; i < count; )
        {
            tempint = (int) (Math.random() * limit);
            for (int j = 0; j < i; j++)
            {
                match = false;
                if (temparray[j] == tempint)
                {
                    match = true;
                    break;
                }
            }
            if (!match)
                temparray[i++] = tempint;
        }
        return temparray;
    }

    static int inRange(int min, int max)
    {
        int random = min + (int) (Math.random() * ((max - min) + 1));
        return random;
    }
}