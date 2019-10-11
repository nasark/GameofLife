//Nasar Khan - Conway's Game of Life 
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  // Needed for ActionListener
import javax.swing.event.*;  // Needed for ActionListener
import javax.imageio.*;
import java.io.*;

class GameOfLifeDemo extends JFrame
{
    static Colony colony = new Colony (0.5);
    static Timer t;
    private String locations [] = {"Middle", "Top Left Corner", "Top Right Corner", "Bottom Left Corner", "Bottom Right Corner"}; //locations for combo box
    private String sizes [] = {"10x10","15x15","20x20","25x25","30x30"}; //size available in combo box for users to specify
    public JComboBox <String> locationBox = new JComboBox<>(locations); //public for easier access throughout program
    public JComboBox <String> sizeBox = new JComboBox<>(sizes);
    JTextField fileName = new JTextField ("Enter file name here", 12); //text field to retrieve names of files to save and load
    //======================================================== constructor
    public GameOfLifeDemo ()
    {
        // 1... Create/initialize components
        BtnListener btnListener = new BtnListener (); // listener for all buttons

        JButton simulateBtn = new JButton ("Simulate"); //creation of buttons for various methods
        simulateBtn.addActionListener (btnListener);
        JButton eradicateBtn = new JButton ("Eradicate");
        eradicateBtn.addActionListener (btnListener);
        JButton populateBtn = new JButton ("Populate");
        populateBtn.addActionListener (btnListener);
        JButton saveBtn = new JButton ("Save");
        saveBtn.addActionListener (btnListener);
        JButton loadBtn = new JButton ("Load");
        loadBtn.addActionListener (btnListener);
        

        // 2... Create content pane, set layout
        JPanel content = new JPanel ();        // Create a content pane
        content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
        JPanel north = new JPanel ();
        north.setLayout (new FlowLayout ()); // Use FlowLayout for input area

        DrawArea board = new DrawArea (500, 500);

        // 3... Add the components to the input area.

        north.add (simulateBtn);
        north.add (eradicateBtn);
        north.add (populateBtn);
        north.add (locationBox);
        north.add (sizeBox);
        north.add (saveBtn);
        north.add (loadBtn);
        north.add (fileName);

        content.add (north, "North"); // Input area
        content.add (board, "South"); // Output area

        // 4... Set this window's attributes.
        setContentPane (content);
        pack ();
        setTitle ("Life Simulation");
        setSize (850, 570);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo (null);           // Center window.
    }

    class BtnListener implements ActionListener 
    {
        public void actionPerformed (ActionEvent e)
        {
            String setLocation = (String)locationBox.getSelectedItem(); //retrieve location and size information from combo boxes and store into string variables
            String sizeInfo = (String)sizeBox.getSelectedItem();
            String file = fileName.getText(); //retrieve name of file from text field and store into string variable
            int setSize=0;

            if (sizeInfo.equals("10x10")) //set size values according to information selected by user - dimensions of square were used to give users a general idea of the size
                setSize = 10;
            else if (sizeInfo.equals("15x15"))
                setSize = 15;
            else if (sizeInfo.equals("20x20"))
                setSize = 20;
            else if (sizeInfo.equals("25x25"))
                setSize = 25;
            else if (sizeInfo.equals("30x30"))
                setSize = 30;

            if (e.getActionCommand ().equals ("Simulate"))
            {
                Movement moveColony = new Movement (); // ActionListener for timer
                t = new Timer (200, moveColony); // set up Movement to run every 200 milliseconds
                t.start (); // start simulation
            }
            else if (e.getActionCommand ().equals ("Eradicate"))
            {
                colony.eradicate(setLocation, setSize); //eradicate according to location and size selected
            }
            else if (e.getActionCommand ().equals ("Populate"))
            {
                colony.populate(setLocation, setSize); //populate according to location and size selected
            }
            else if (e.getActionCommand ().equals ("Save"))
            {
                colony.save (file); //save pattern with name used in textfield
            }
            else if (e.getActionCommand ().equals ("Load"))
            {
                colony.load (file); //load file according name used in textfield
            }

            repaint ();            // refresh display of colony
        }
    }

    class DrawArea extends JPanel
    {
        public DrawArea (int width, int height)
        {
            this.setPreferredSize (new Dimension (width, height)); // size
        }

        public void paintComponent (Graphics g)
        {
            colony.show (g); // display current state of colony
        }
    }

    class Movement implements ActionListener
    {
        public void actionPerformed (ActionEvent event)
        {
            colony.advance (); // advance to the next time step
            repaint (); // refresh 
        }
    }

    //======================================================== method main
    public static void main (String[] args)
    {
        GameOfLifeDemo window = new GameOfLifeDemo ();
        window.setVisible (true);
    }
}

class Colony
{
    private boolean grid[] [];

    public Colony (double density)
    {
        grid = new boolean [100] [100];
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
                grid [row] [col] = Math.random () < density;
    }

    public void show (Graphics g)
    {
        for (int row = 0 ; row < grid.length ; row++)
            for (int col = 0 ; col < grid [0].length ; col++)
            {
                if (grid [row] [col]) // life
                    g.setColor (Color.black);
                else
                    g.setColor (Color.white);
                g.fillRect (col * 5 + 2, row * 5 + 2, 5, 5); // draw life form
            }
    }

    public boolean live (int row, int col) //checks current state of life form and determines its fate
    {
        int count = 0;
        //checks each unit around life form to determine near population
        if (row > 0 && grid [row - 1] [col]) // one unit above and row is not the first row to prevent out of bounds error 
            count++;
            
        if (row < grid.length - 1 && grid [row + 1] [col]) // one unit below
            count++;
            
        if (col > 0 && grid [row] [col - 1]) // one unit left
            count++;
            
        if (col < grid [0].length - 1 && grid [row] [col + 1]) // one unit right
            count++;
            
        if (row > 0 && col > 0 && grid [row - 1] [col - 1]) // one unit diagonally to the left and up 
            count++;
            
        if (row < grid.length - 1 && col > 0 && grid [row + 1] [col - 1]) //  one unit diagonally to the left and down 
            count++;
            
        if (row > 0 && col < grid [0].length - 1 && grid [row - 1] [col + 1]) // one unit diagonally to the right and up 
            count++;
            
        if (row < grid.length - 1 && col < grid [0].length - 1 && grid [row + 1] [col + 1]) // one unit diagonally to the right and down 
            count++;

        //fate of life form is determined according to surrounding population
        if (grid [row] [col] && count < 2) // dies
            return false;
            
        if (grid [row] [col] && count >= 4) // dies
            return false;
            
        if (grid [row] [col] && (count == 2 || count == 3)) // lives
            return true;
            
        if (!grid [row] [col] && count == 3) // reproduction
            return true;

        return grid [row] [col]; // new state of life form is returned
    }

    public void advance () //advances the colony during each interval of timer
    {
        boolean temp [][] = new boolean [100] [100]; //creat temporary boolean 2d array

        for (int x = 0; x<grid.length; x++) //go through rows and coloumns
        {
            
            for (int y = 0; y<grid[x].length; y++)
            {
                temp[x][y] = live (x,y);  //determines the fate of each life form in original array and updates new states into temp array        
            }
            
        }
        
        grid=temp; //temporary is reassigned to original
    }

    public void eradicate (String location, int size) //accepts general location and size of eradication
    {
        int row, col;
        if (location.equals ("Middle")) //if option selected in combo box is middle location
        {
            row = 50;
            col = 50; //represents middle of 2D array
            
            for (int x = 0; x<size; x++) // <size to facilitate for sizes such as "10x10" etc
            {
                for (int y = 0; y<size; y++) //loops will add counter up to size and add that to row and col to eradicate in the shape of a square
                {
                    if(Math.random()>0.1) //facilitate for 90% eradication - 90% of size is eradicated
                    {
                        grid [row][col] = false;   //destroy life
                        grid[row+x][col] = false;
                        grid[row+x][col-y] = false;
                        grid [row+x][col+y]= false;
                        grid [row][col-y] = false;
                        grid [row] [col+y] = false;
                        grid [row-x] [col] = false;
                        grid [row-x] [col+y] = false;
                        grid [row-x] [col-y] = false;          
                    }
                }
            }

        }
        else //other locations need size*2 because they are on the corners of the 2D array and as a result eradication size would be halved
        {    
            for (int x = 0; x<size*2; x++)
            {
                for (int y = 0; y<size*2; y++)
                {
                    if (location.equals ("Top Left Corner")) 
                    {
                        row = 0; //location of eradication
                        col = 0;

                        if(Math.random()>0.1) //90% of size is eradicated
                        {
                            grid [row][col] = false;  //units are only right, down, and diagonal right-down, otherwise it would be out of bounds
                            grid[row+x][col] = false;
                            grid [row+x][col+y]= false;
                            grid [row] [col+y] = false;  
                        }

                    }
                    else if (location.equals ("Top Right Corner"))
                    {
                        row = 0; //location of eradication
                        col = 99;

                        if(Math.random()>0.1) //90% eradication
                        {
                            grid [row][col] = false; //units are only left, down, and diagonal left-down, to facilitate out of bounds
                            grid[row+x][col] = false;
                            grid [row+x][col-y]= false;
                            grid [row] [col-y] = false;   
                        }

                    }
                    else if (location.equals ("Bottom Left Corner"))
                    {
                        row = 99; //location of eradication
                        col = 0;

                        if(Math.random()>0.1) //90% eradication
                        {
                            grid [row][col] = false;   //units are only up, right, and diagonal up-right, to facilitate out of bounds
                            grid [row] [col+y] = false;
                            grid [row-x] [col] = false;
                            grid [row-x] [col+y] = false;
                        }

                    }
                    else if (location.equals ("Bottom Right Corner"))
                    {
                        row = 99; //location of eradication
                        col = 99;

                        if(Math.random()>0.1) //90% eradication
                        {
                            grid [row][col] = false; //units are only up, left, and diagonal up-left, to facilitate out of bounds
                            grid [row][col-y] = false;
                            grid [row-x] [col] = false;
                            grid [row-x] [col-y] = false;  
                        }

                    }

                }
            }
        }

    }

    public void populate (String location, int size) //accepts general location and size of life
    {
        int row, col;
        if (location.equals ("Middle")) //same process as eradicate except 80% populating according to size and boolean array is being set to true
        {
            row = 50;
            col = 50;
           
            for (int x = 0; x<size; x++)
            {
                for (int y = 0; y<size; y++)
                {
                    if(Math.random()>0.2) //80% of size is populated
                    {
                        grid [row][col] = true;
                        grid[row+x][col] = true;
                        grid[row+x][col-y] = true;
                        grid [row+x][col+y]= true;
                        grid [row][col-y] = true;
                        grid [row] [col+y] = true;
                        grid [row-x] [col] = true;
                        grid [row-x] [col+y] = true;
                        grid [row-x] [col-y] = true;    
                    }
                }
            }
        }

        else //same as eradicate
        {
            for (int x = 0; x<size*2; x++)
            {
                for (int y = 0; y<size*2; y++)
                {
                    if (location.equals ("Top Left Corner"))
                    {
                        row = 0; //location of top left
                        col = 0;

                        if (Math.random()>0.2) //80% of size is populated
                        {
                            grid [row][col] = true; //true = life
                            grid[row+x][col] = true;
                            grid [row+x][col+y]= true;
                            grid [row] [col+y] = true;
                        }

                    }
                    else if (location.equals ("Top Right Corner"))
                    {
                        row = 0; //location of top right
                        col = 99;

                        if (Math.random()>0.2) //80% of size is populated
                        {
                            grid [row][col] = true;
                            grid[row+x][col] = true;
                            grid [row+x][col-y]= true;
                            grid [row] [col-y] = true;  
                        }

                    }
                    else if (location.equals ("Bottom Left Corner"))
                    {
                        row = 99; //location of bottom left
                        col = 0;

                        if (Math.random()>0.2) //80% of size is populated
                        {
                            grid [row][col] = true;
                            grid [row] [col+y] = true;
                            grid [row-x] [col] = true;
                            grid [row-x] [col+y] = true;
                        }

                    }
                    else if (location.equals ("Bottom Right Corner"))
                    {
                        row = 99; //location of bottom right
                        col = 99;

                        if (Math.random()>0.2) //80% of size is populated
                        {
                            grid [row][col] = true;
                            grid [row][col-y] = true;
                            grid [row-x] [col] = true;
                            grid [row-x] [col-y] = true; 
                        }

                    }

                }
            }
        }

    }

    public void save(String fname) //accepts name of file to create from textfield
    {
        

        try //to check validity of pattern and file name, and prevent program from crashing
        {
            String str = ""; //stores life form states of each row in array 
            FileWriter fw = new FileWriter (fname);
            PrintWriter fileout = new PrintWriter (fw);
            for (int x = 0; x<grid.length; x++) //go through array
            {
                for (int y = 0; y<grid[x].length; y++)  //go through row
                {
                    if (grid[x][y]) //if location in array is true - life
                    {
                        str += "O"; //"O" character represents an alive life form
                    }
                    else
                    {
                        str += "#"; //"#" character represent no living life form
                    }
                }
                fileout.println (str); //row is stored in file
                str = "";  //string is emptied in preparation for the next row
            }
            fileout.close (); // close file

        }
        catch (IOException e) //if file does not exist
        {
            System.out.print ("File not found");
        }

        

    }

    public void load (String fname) //accepts name of file to be loaded
    {
        
        try //to check validity of pattern and file name, and prevent program from crashing
        {
            String str = "";
            FileReader fr = new FileReader (fname);
            BufferedReader filein = new BufferedReader (fr);
            
            for (int x = 0; x<grid.length; x++)
            {
                str = filein.readLine(); //first row in text file is stored in string variable
                
                for (int y = 0; y<grid[x].length; y++)
                {
                    if (str.charAt(y) == 'O') //if character in coloumn of text file is the character that represents life
                    {
                        grid[x][y] = true; //set corresponding location in array to true
                    }
                    else //otherwise if character is '#'
                    {
                        grid[x][y] = false; //set corresponding location in array to true
                    }
                }
                //once all coloumns in row have been analyzed, next row is stored into string variable
            }
            filein.close(); //close file
        }
        catch (IOException e) //if file specified does not exist
        {
            System.out.print ("File not found");
        }
        
    }
}
