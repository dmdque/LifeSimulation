import java.awt.*;
import hsa.Console;

public class Methods
{
    public boolean[] [] grid = new boolean [100] [100];
    public Methods
    {
    }


    public live (int row, int column)
    {
	if (grid [row] [column]) // The cell exists
	    if (1 > surroundingChecker (row, column) || surroundingChecker (row, column) > 6)
		grid [row] [column] = false; // The cell dies

	else // No cell exists
	    if (1 < surroundingChecker (row, column) && surroundingChecker (row, column) < 4)
		grid [row] [column] = true; // A cell is created
    }


    public void add (int x, int y, int size)
    {
	if (grid [row] [column])
	    if ((Math.random () * 10) < 8)
		grid [row] [column] = false;
    }


    public void delete (int x, int y, int size)
    {
    }


    public int surroundingChecker (int row, int column)
    { // CHECK FOR BOUNDARIES
	int counter = 0;
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
	return counter;
    }
}
