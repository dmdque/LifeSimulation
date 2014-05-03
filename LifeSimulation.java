/* ReadMe
 * Left or right click to add or remove a single point, depending on the cell's current life
 * Drag to add or remove single points at each cell the mouse is over
 * Hold SHIFT and left-click or left-click and drag to activate the add method
 * Hold SHIFT and right-click or right-click and drag to activate the remove method
 * Change the value in the JSpinner to adjust the radius of add or remove functions
*/
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;

public class LifeSimulation extends JFrame
{
    protected Colony colony;
    protected Timer timer;
    protected Display display; // Where the simulation will be displayed
    protected JButton _commandButton;
    protected ImageIcon _playIcon, _pauseIcon;
    protected JTextField _fileNameJTextField, _sizeJTextField;
    protected JLabel _fileIO, _generations;
    protected boolean live; // Determines whether the clicked cell is alive or dead
    // protected JFileChooser fileChooser;

    public LifeSimulation ()
    {
	colony = new Colony (0.2);

	JPanel content = new JPanel (new BorderLayout (0, 0)); // Main panel for everything
	JPanel commands = new JPanel (new FlowLayout (FlowLayout.LEFT)); // South panel, for all the buttons
	JPanel toolBar = new JPanel (new FlowLayout (FlowLayout.LEFT)); // North panel, for opening and saving functions

	// Display
	display = new Display (501, 501);
	display.addMouseListener (new displayMouseListener ());
	display.addMouseMotionListener (new displayMouseMotionListener ());

	// Stop/Play button icons
	_playIcon = new ImageIcon (loadImage ("PlayIcon.png"));
	_pauseIcon = new ImageIcon (loadImage ("PauseIcon.png"));

	// JButtons
	_commandButton = new JButton (_playIcon);
	JButton nextButton = new JButton (new ImageIcon (loadImage ("NextIcon.png")));
	JButton openButton = new JButton (new ImageIcon (loadImage ("OpenIcon.png")));
	JButton saveButton = new JButton (new ImageIcon (loadImage ("SaveIcon.png")));
	JButton fastButton = new JButton (new ImageIcon (loadImage ("FastForwardIcon.png")));
	JButton slowButton = new JButton (new ImageIcon (loadImage ("RewindIcon.png")));
	_commandButton.addActionListener (new CommandButtonListener ());
	nextButton.addActionListener (new NextButtonListener ());
	openButton.addActionListener (new OpenButtonListener ());
	saveButton.addActionListener (new SaveButtonListener ());
	fastButton.addActionListener (new FastForwardButtonListener ());
	slowButton.addActionListener (new RewindButtonListener ());

	_fileNameJTextField = new JTextField (15);
	_fileIO = new JLabel ();
	_generations = new JLabel ("" + colony.getGenerations ());
	_generations.setHorizontalAlignment (_generations.RIGHT);

	// JSpinner
	SpinnerNumberModel numberModel = new SpinnerNumberModel (10, 0, 10, 1);
	numberModel.setMaximum (null); // Sets maximum to have to upper bound
	JSpinner _spinner = new JSpinner (numberModel);
	(((JSpinner.DefaultEditor) _spinner.getEditor ()).getTextField ()).setColumns (2); // Sets spinner's columns to 2
	_sizeJTextField = ((JSpinner.DefaultEditor) _spinner.getEditor ()).getTextField ();

	toolBar.add (openButton);
	toolBar.add (saveButton);
	toolBar.add (new JLabel ("Enter a file name: "));
	toolBar.add (_fileNameJTextField);
	toolBar.add (_fileIO);

	commands.add (slowButton);
	commands.add (_commandButton);
	commands.add (fastButton);
	commands.add (nextButton);
	commands.add (new JLabel ("Enter size: "));
	commands.add (_spinner);
	commands.add (new JLabel ("Generations: "));
	commands.add (_generations);

	content.add (toolBar, BorderLayout.NORTH);
	content.add (display, BorderLayout.CENTER);
	content.add (commands, BorderLayout.SOUTH);

	Advance advance = new Advance ();
	timer = new Timer (100, advance);

	setContentPane (content);
	pack ();
	setTitle ("Life Simulation");
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo (null);
    }


    class CommandButtonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    if (!timer.isRunning ())
	    {
		timer.start ();
		_commandButton.setIcon (_pauseIcon);
	    }
	    else if (timer.isRunning ())
	    {
		timer.stop ();
		_commandButton.setIcon (_playIcon);
	    }
	}
    }


    class NextButtonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    colony.advance (); // Advances 1 generation
	    display.repaint (); // Repaints the display with updated generation
	    _generations.setText ("" + colony.getGenerations ());
	}
    }


    class FastForwardButtonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    if (timer.getDelay () == 0)
		timer.setDelay (10);
	    else
		timer.setDelay ((int) (timer.getDelay () / 1.5));
	}
    }


    class RewindButtonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    if (timer.getDelay () == 0)
		timer.setDelay (10);
	    else
		timer.setDelay ((int) (timer.getDelay () * 1.5));
	}
    }



    class OpenButtonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    try
	    {
		String fileName = ("" + _fileNameJTextField.getText ());
		try
		{
		    BufferedReader reader = new BufferedReader (new FileReader (fileName + ".txt"));
		    try
		    {
			boolean[] [] grid = new boolean [50] [50];
			for (int row = 0 ; row < grid.length ; row++, reader.readLine ())
			    for (int column = 0 ; column < grid [0].length ; column++)
				grid [row] [column] = reader.read () == '*';
			reader.close ();
			colony.setGrid (grid);
			display.repaint ();
			_fileIO.setText ("Open Successful");
		    }
		    catch (IOException ioe)
		    {
			_fileIO.setText ("File not found");
		    }
		}
		catch (FileNotFoundException fnfe)
		{
		    _fileIO.setText ("File not found");
		}
	    }
	    catch (NullPointerException npe)
	    {
		_fileIO.setText ("Invalid file name");
	    }
	}
    }


    class SaveButtonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    try
	    {
		String fileName = ("" + _fileNameJTextField.getText ());
		try
		{
		    BufferedWriter writer = new BufferedWriter (new FileWriter (fileName + ".txt"));
		    boolean[] [] grid = colony.getGrid ();
		    for (int row = 0 ; row < grid.length ; row++, writer.newLine ())
			for (int column = 0 ; column < grid [0].length ; column++)
			    if (grid [row] [column])
				writer.write ('*');
			    else
				writer.write (' ');
		    writer.close ();
		    _fileIO.setText ("Save Successful");
		}
		catch (IOException ioe)
		{
		    _fileIO.setText ("Invalid file name");
		}
	    }
	    catch (NullPointerException npe)
	    {
		_fileIO.setText ("Invalid file name");
	    }
	}
    }


    class displayMouseListener implements MouseListener
    {
	public void mouseClicked (MouseEvent e)  // Method required
	{
	}
	public void mousePressed (MouseEvent e)
	{
	    if (e.isShiftDown ())
	    {
		if (e.getButton () == e.BUTTON1)
		{
		    try
		    {
			try
			{
			    colony.add (e.getY () / 10, e.getX () / 10, Integer.parseInt (_sizeJTextField.getText ()));
			    display.repaint ();
			}
			catch (NumberFormatException nfe)
			{
			}
		    }
		    catch (ArrayIndexOutOfBoundsException aioobe)
		    {
		    }
		}
		else if (e.getButton () == e.BUTTON3)
		{
		    try
		    {
			try
			{
			    colony.delete (e.getY () / 10, e.getX () / 10, Integer.parseInt (_sizeJTextField.getText ()));
			    display.repaint ();
			}
			catch (NumberFormatException nfe)
			{
			}
		    }
		    catch (ArrayIndexOutOfBoundsException aioobe)
		    {
		    }
		}
	    }
	    else
	    {
		try
		{
		    live = colony.isLive (e.getY () / 10, e.getX () / 10);
		    colony.setGrid (e.getY () / 10, e.getX () / 10, !live);
		    display.repaint ();
		}
		catch (ArrayIndexOutOfBoundsException aioobe)
		{
		}
	    }
	}
	public void mouseReleased (MouseEvent e)
	{
	}
	public void mouseEntered (MouseEvent e)
	{
	}
	public void mouseExited (MouseEvent e)
	{
	}
    }


    class displayMouseMotionListener implements MouseMotionListener
    {
	public void mouseDragged (MouseEvent e)
	{
	    int add = e.BUTTON1_DOWN_MASK | e.SHIFT_DOWN_MASK;
	    int delete = e.BUTTON3_DOWN_MASK | e.SHIFT_DOWN_MASK;
	    if (e.getModifiersEx () == add) // if (e.isShiftDown ())
	    {
		try
		{
		    try
		    {
			colony.add (e.getY () / 10, e.getX () / 10, Integer.parseInt (_sizeJTextField.getText ()));
			display.repaint ();
		    }
		    catch (NumberFormatException nfe)
		    {
		    }
		}
		catch (ArrayIndexOutOfBoundsException aioobe)
		{
		}
	    }
	    else if (e.getModifiersEx () == delete) // if (e.isShiftDown () && e.getButton () == e.BUTTON3)
	    {
		try
		{
		    try
		    {
			colony.delete (e.getY () / 10, e.getX () / 10, Integer.parseInt (_sizeJTextField.getText ()));
			display.repaint ();
		    }
		    catch (NumberFormatException nfe)
		    {
		    }
		}
		catch (ArrayIndexOutOfBoundsException aioobe)
		{
		}
	    }
	    else
	    {
		try
		{
		    colony.setGrid (e.getY () / 10, e.getX () / 10, !live /*e.getModifiersEx () == e.BUTTON1_DOWN_MASK*/);
		    display.repaint ();
		}
		catch (ArrayIndexOutOfBoundsException aioobe)
		{
		}
	    }
	    // // if (e.getButton () == e.BUTTON1)
	    // //     live = true;
	    // //
	    // // else if (e.getButton () == e.BUTTON2)
	    // //     live = false;
	    // if (e.getButton () != e.NOBUTTON)
	    // {
	    //     if (e.getButton () == e.BUTTON1)
	    //     {
	    //         try
	    //         {
	    //             colony.setGrid (e.getY () / 10, e.getX () / 10, true);
	    //             display.repaint ();
	    //         }
	    //         catch (ArrayIndexOutOfBoundsException aioobe)
	    //         {
	    //         }
	    //     }
	    //     if (e.getButton () == e.BUTTON3)
	    //     {
	    //         try
	    //         {
	    //             colony.setGrid (e.getY () / 10, e.getX () / 10, false);
	    //             display.repaint ();
	    //         }
	    //         catch (ArrayIndexOutOfBoundsException aioobe)
	    //         {
	    //         }
	    //     }
	}


	public void mouseMoved (MouseEvent e)
	{
	}
    }


    class Display extends JPanel // Where the simulation will be displayed
    {
	public Display (int width, int height)
	{
	    this.setPreferredSize (new Dimension (width, height));
	    this.setBackground (Color.white); // Sets the background to white so that when super.paintComponent is called, a white background is shown instead of the default grey
	}
	public void paintComponent (Graphics g)
	{
	    super.paintComponent (g); // Calls super.paintComponent to clear the JPanel before updating the image
	    colony.show (g);
	}
    }


    class Advance implements ActionListener
    {
	// protected Colony colony;
	public Advance ()
	{
	    // this.colony = colony;
	}

	public void actionPerformed (ActionEvent e)
	{
	    colony.advance (); // Advances 1 generation
	    display.repaint (); // Repaints the display with updated generation
	    _generations.setText ("" + colony.getGenerations ());
	}
    }


    public Image loadImage (String fileName)  // Loads an image
    {
	Image image = null;
	try
	{
	    image = ImageIO.read (new File (fileName));
	}
	catch (IOException e)
	{
	}
	return image;
    }


    public static void main (String[] args)
    {
	LifeSimulation window = new LifeSimulation ();
	window.setVisible (true);
    }
}


