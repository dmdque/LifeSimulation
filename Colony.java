// Variable grid size
import java.awt.*;

public class Colony
{
    protected boolean[] [] grid, tempGrid;
    protected int _generations;

    public Colony (double density)
    {
	_generations = 0;
	grid = new boolean [50] [50];
	tempGrid = new boolean [50] [50];
	for (int row = 0 ; row < grid.length ; row++)
	    for (int column = 0 ; column < grid [0].length ; column++)
		grid [row] [column] = Math.random () < density; // Math.random () < density returns a boolean value
    }


    public void show (Graphics g)
    {
	g.setColor (Color.gray);
	g.fillRect (0, 0, 500, 500); // Clear display

	g.setColor (Color.yellow);
	for (int row = 0 ; row < grid.length ; row++)
	    for (int column = 0 ; column < grid [0].length ; column++)
		if (grid [row] [column]) // If it is true
		    g.fillRect (column * 10, row * 10, 10, 10); // Draw block

	// g.setColor (Color.black);
	// for (int row = 0 ; row < grid.length ; row++)
	//     for (int column = 0 ; column < grid [0].length ; column++)
	//         g.drawRect (column * 10, row * 10, 8, 8); // 8, 8 gives the grid a different feel
	g.setColor (Color.black);
	for (int i = 0 ; i <= 500 ; i += 10)
	{
	    g.drawLine (0, i, 500, i);
	    g.drawLine (i, 0, i, 500);
	}
    }


    public void advance ()
    {
	_generations++;
	for (int row = 0 ; row < grid.length ; row++)
	{
	    for (int column = 0 ; column < grid [0].length ; column++)
	    {
		if (grid [row] [column])
		    tempGrid [row] [column] = true;
		else if (!grid [row] [column])
		    tempGrid [row] [column] = false;
	    }
	}
	for (int row = 0 ; row < grid.length ; row++)
	    for (int column = 0 ; column < grid [0].length ; column++)
		live (row, column);
    }


    public void live (int row, int column)
    {
	if (tempGrid [row] [column]) // The cell exists
	{
	    if (2 > surroundingChecker (tempGrid, row, column) || surroundingChecker (tempGrid, row, column) > 3)
		grid [row] [column] = false;  // The cell dies
	}
	else if (tempGrid [row] [column] == false) // No cell exists
	    if (2 < surroundingChecker (tempGrid, row, column) && surroundingChecker (tempGrid, row, column) < 4)
		grid [row] [column] = true; // A cell is created
    }


    public int getGenerations ()
    {
	return _generations;
    }


    public boolean[] [] getGrid ()
    {
	return grid;
    }


    public void setGrid (boolean[] [] grid)
    {
	this.grid = grid;
    }


    public void setGrid (int row, int column, boolean live)
    {
	grid [row] [column] = live;
    }


    public boolean isLive (int row, int column)
    {
	return grid [row] [column];
    }


    // public void open (String fileName)
    // {
    //     int size;
    //
    //     TextInputFile filein = new TextInputFile (finename);
    //     for (size = 0 ; !filein.eof () ; size++) // Finds out the number of lines in the file
    //         filein.readLine ();
    //     filein.close ();
    //
    //     String temp[] = new String [size]; // Creates an array of size equal to the length of the file
    //     filein = new TextInputFile (fname);
    //     for (int i = 0 ; !filein.eof () ; i++)
    //         temp [i] = filein.readLine (); // Reads file
    //     filein.close (); // Close file
    //
    //     return temp;
    //
    // }
    //
    //
    // public static char[] [] loadGrid (String fileName)
    // {
    //     boolean[] [] grid;
    //     int row, column;
    //     try
    //     {
    //         BufferedReader inputFile = new BufferedReader (new FileReader (fileName));
    //         String text = inputFile.readLine ();
    //         for (row = 0 ; text != null ; row++)
    //             text = inputFile.readLine ();
    //         inputFile.close ();
    //
    //         input = new BufferedReader (new FileReader (fileName));
    //         text = input.readLine ();
    //         map = new boolean [row] [text.length ()]; //actual array
    //
    //         int temp = row;
    //         for (row = 0 ; row < temp ; row++)
    //         {
    //             for (int col = 0 ; col < text.length () ; col++)
    //             {
    //                 map [row] [col] = text.charAt (col);  //add it into the array
    //             }
    //             text = input.readLine ();
    //         }
    //     }
    //
    //
    //     catch (IOException e)  //if file is not found
    //     {
    //         c.println ("Unable to read from file");
    //         c.getChar ();
    //         System.exit (-1);
    //     }
    //
    //
    //     return map;
    // }


    public void save ()
    {
    }


    public void add (int y, int x, int size)  // Row, column, size
    {
	// size -= 2;
	for (int row = y - (size / 2) ; row <= y + (size / 2) && row < grid.length ; row++)
	{
	    for (int column = x - (size / 2) ; column <= x + (size / 2) && column < grid [0].length ; column++)
	    {
		try
		{
		    if (!grid [row] [column])
			grid [row] [column] = Math.random () < 0.8;
		}
		catch (ArrayIndexOutOfBoundsException aioobe)
		{
		}
	    }
	}
    }


    public void delete (int y, int x, int size)
    {
	// size -= 2;
	for (int row = y - (size / 2) ; row <= y + (size / 2) && row < grid.length ; row++)
	{
	    for (int column = x - (size / 2) ; column <= x + (size / 2) && column < grid [0].length ; column++)
	    {
		try
		{
		    if (grid [row] [column])
			grid [row] [column] = !(Math.random () < 0.8);
		}
		catch (ArrayIndexOutOfBoundsException aioobe)
		{
		}
	    }
	}
    }


    public int surroundingChecker (boolean[] [] grid, int row, int column)
    {
	int counter = 0;
	if (row == 0 || row == grid.length - 1 || column == 0 || column == grid [0].length - 1)
	{
	    try
	    {
		if (grid [row - 1] [column - 1])
		    counter++;
	    }
	    catch (ArrayIndexOutOfBoundsException e)
	    {
	    }
	    try
	    {
		if (grid [row - 1] [column])
		    counter++;
	    }
	    catch (ArrayIndexOutOfBoundsException e)
	    {
	    }
	    try
	    {
		if (grid [row - 1] [column + 1])
		    counter++;
	    }
	    catch (ArrayIndexOutOfBoundsException e)
	    {
	    }
	    try
	    {
		if (grid [row] [column - 1])
		    counter++;
	    }
	    catch (ArrayIndexOutOfBoundsException e)
	    {
	    }
	    try
	    {
		if (grid [row] [column + 1])
		    counter++;
	    }
	    catch (ArrayIndexOutOfBoundsException e)
	    {
	    }
	    try
	    {
		if (grid [row + 1] [column - 1])
		    counter++;
	    }
	    catch (ArrayIndexOutOfBoundsException e)
	    {
	    }
	    try
	    {
		if (grid [row + 1] [column])
		    counter++;
	    }
	    catch (ArrayIndexOutOfBoundsException e)
	    {
	    }
	    try
	    {
		if (grid [row + 1] [column + 1])
		    counter++;
	    }
	    catch (ArrayIndexOutOfBoundsException e)
	    {
	    }
	}
	else
	{
	    if (grid [row - 1] [column - 1])
		counter++;
	    if (grid [row - 1] [column])
		counter++;
	    if (grid [row - 1] [column + 1])
		counter++;
	    if (grid [row] [column - 1])
		counter++;
	    if (grid [row] [column + 1])
		counter++;
	    if (grid [row + 1] [column - 1])
		counter++;
	    if (grid [row + 1] [column])
		counter++;
	    if (grid [row + 1] [column + 1])
		counter++;
	}
	return counter;
    }


    // static public String[] FillRunner ()  // Runs Fill
    // {
    //     c.clear ();
    //
    //     String fname;
    //     c.print ("Enter the file name to retrieve names from: ");
    //     fname = c.readLine (); // The name of the file
    //     String list[] = Fill (fname); // Store names in array
    //     Print (list); // Calls Print method to display the array
    //
    //     c.println ();
    //     c.println ("<Press any key to return to the main menu>");
    //     c.getChar ();
    //
    //     return list;
    // }
    //
    //
    // static public String[] Fill (String fname)
    // {
    //     int size;
    //
    //     TextInputFile filein = new TextInputFile (fname);
    //     for (size = 0 ; !filein.eof () ; size++) // Finds out the number of lines in the file
    //         filein.readLine ();
    //     filein.close ();
    //
    //     String temp[] = new String [size]; // Creates an array of size equal to the length of the file
    //     filein = new TextInputFile (fname);
    //     for (int i = 0 ; !filein.eof () ; i++)
    //         temp [i] = filein.readLine (); // Reads file
    //     filein.close (); // Close file
    //
    //     return temp;
    // }
}


