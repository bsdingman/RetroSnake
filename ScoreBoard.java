/*
Bryan Dingman
Lab 8: Snake
Scoreboard class. Handles loading, saving, display the scoreboard
*/
package SnakeGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/*
    Class: 
        ScoreBoard
    Use:
        Requires Construtor, staticially controlled
        ScoreBoard(Pane)
    Methods:
        addPoint()
        reset()
        getScore()
        build()
        saveScore(String init, String score)
        loadScores()
        sort (ArrayList<String> init, ArrayList<String> score, ArrayList<String> dif)
        swap(int i, int x, ArrayList<String> init, ArrayList<String> score, ArrayList<String> dif)   
*/
public class ScoreBoard 
{
    // Remember our MainPane
    Pane MainPane;
    
    // Our current score
    static Double CurrentHighScore = 0.0;
    
    // The amount of points to add
    static double pointsToAdd = Config.POINT;
    
    // Window sizes
    final static int WINDOW_X = Config.WINDOW_X;
    final static int WINDOW_Y = Config.WINDOW_Y;
    
    // In game display labels
    static Label text = new Label("Score: ");
    static Label score = new Label();
    
    // List of final scores
    // This list is compiled, AAA   | E |   1255
    ArrayList<String> FinalScores = new ArrayList<>();
    
    // List of scores split up via column
    ArrayList<String> Initials = new ArrayList<>();
    ArrayList<String> Scores = new ArrayList<>();
    ArrayList<String> Difficulty = new ArrayList<>();
    
    // The score files
    File file = new File("scores.txt");
    
    /*
     * Name:
     *  ScoreBoard
     *
     * Description:
     *  ScoreBoard class constructor
     *
     * Input:
     *  PANE - pane
     *
     * Output:
     *  None
     */
    public ScoreBoard(Pane pane)
    {
        MainPane = pane;
        
        // Load the scores
        loadScores();
    }
    
    /*
     * Name:
     *  addPoint
     *
     * Description:
     *  Add points to the current score and format it to be displayed on screen
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public static void addPoint()
    {
        // Save the points
        CurrentHighScore += pointsToAdd;
        
        // Format the score to remove the .0
        DecimalFormat format = new DecimalFormat("0.#");
        
        // Display it!
        score.setText(format.format(CurrentHighScore));
    }
    
    /*
     * Name:
     *  reset
     *
     * Description:
     *  Reset the current score
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public static void reset()
    {
        CurrentHighScore = 0.0;
    }
    
    /*
     * Name:
     *  getScore
     *
     * Description:
     *  Get the current score
     *
     * Input:
     *  None
     *
     * Output:
     *  STRING - CurrentHighScore
     */
    public static String getScore()
    {
        DecimalFormat format = new DecimalFormat("0.#");
        return format.format(CurrentHighScore);
    }
    
    /*
     * Name:
     *  build
     *
     * Description:
     *  Builds the scoreboard in game
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public void build()
    {
        // Set the layout for the score title
        text.setLayoutX(WINDOW_X - 150);
        text.setLayoutY(15);
        
        // Set the style
        text.setFont(Font.font("Courier New",15));
        text.setTextFill(Color.LIMEGREEN);
        
        // Set the layout for the score 
        score.setLayoutX(WINDOW_X - 95);
        score.setLayoutY(15);
        
        // Set the style
        score.setFont(Font.font("Courier New",15));
        score.setTextFill(Color.LIMEGREEN);
        
        // Add it!
        MainPane.getChildren().addAll(text,score);
        
        // Display the current score
        DecimalFormat format = new DecimalFormat("0.#");
        score.setText(format.format(CurrentHighScore));
    }
    
    /*
     * Name:
     *  saveScore
     *
     * Description:
     *  Takes the initials and score passed to it, gets the current difficulty, encrypts it and saves
     *
     * Input:
     *  STRING - init
     *  STRING - score
     *
     * Output:
     *  None
     */
    public void saveScore(String init, String score)
    {
        // Temp scores storage array
        ArrayList<String> tempScores = new ArrayList<>();
        
        // Clear the final scores list
        FinalScores.clear();
        
        // Add the initials 
        Initials.add(init);
        
        // Add the score
        Scores.add(score);
        
        // Find our current difficulty and assign it
        String dif = "";
        switch (Config.getDifficulty())
        {
            case 1:
                dif = "E";
                break;
            case 2:
                dif = "M";
                break;
            case 3:
                dif = "H";
                break;
        }
        
        // push it back!
        Difficulty.add(dif);
        
        // Using bubble sort, sort our arrays so the highest score is first
        sort(Initials,Scores,Difficulty);
        
        // Write formatted output to the file
        for (int i = Initials.size() - 1; i >= 0; i--) 
        {
            // Format our string, AAA   | E |   1234
            String finalString = Initials.get(i) + "   | " + Difficulty.get(i) + " |   " + Scores.get(i);
            
            // Save the unecrypted string so we can display it
            FinalScores.add(finalString);
            
            // Get the bytes 
            byte[] array = finalString.getBytes();

            // encode data using BASE64
            String encoded = Base64.getEncoder().encodeToString(array);
            
            // Save the encoded score
            tempScores.add(encoded);
        }

        try 
        {
            // Write to the score file
            java.io.PrintWriter fileOutput = new java.io.PrintWriter(file);

            // Save all the scores
            for (String x : tempScores)
            {
                fileOutput.println(x);
            }

            // Close the file
            fileOutput.close();
        }
        // Catch file not found exceptions 
        catch (FileNotFoundException ex)
        {
            System.out.println("scores.txt does not exist!");
        }
    }
    
    /*
     * Name:
     *  loadScores
     *
     * Description:
     *  Loads the encypted scores from the score file, decrypts them and add to an array
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public void loadScores()
    {
        // Initalize our input
        Scanner input;
        try 
        {
            // Attempt to read in the file and the values 
            input = new Scanner(new File("Scores.txt"));

            // While we have some values in the file, display them on screen 
            while(input.hasNext())                   
            {
                // Decode data
                byte[] valueDecoded = Base64.getDecoder().decode(input.nextLine()); 
                
                // Convert from byte array to string
                String score = new String(valueDecoded);
                
                // Pushback to our final scores
                FinalScores.add(score);
                
                // Split the string from | 
                String[] split = score.split("\\|");
                
                // Pushback the individual scores
                Initials.add(split[0].trim());
                Scores.add(split[2].trim());
                Difficulty.add(split[1].trim());
            }
        }
        // Catch file not found exceptions 
        catch (FileNotFoundException ex)
        {
            System.out.println(ex);
        }
    }

    /*
     * Name:
     *  sort
     *
     * Description:
     *  Sort our initials, score, and difficulty arrays using a bubble sort. 
     *
     * Input:
     *  ARRAYLIST<String> - init
     *  ARRAYLIST<String> - score
     *  ARRAYLIST<String> - dif
     *
     * Output:
     *  None
     */
    public static void sort (ArrayList<String> init, ArrayList<String> score, ArrayList<String> dif)
    {
        // Get the size 
        int length = score.size();
        int y;
        
        // Inital loop 
        for (int i = 0; i < length; i++)
        {
            // Start from the beginning
            for (int x = 0; x < length - 1; x++) 
            {
                // Get the number next to the first
                y = x + 1;
                Double first = Double.parseDouble(score.get(x));
                Double second = Double.parseDouble(score.get(y));
                
                // If it's greater than, swap them
                if (first > second) 
                {
                    // Call the swap method
                    swap(x,y,init,score,dif);
                }
            }
        }
    }

    /*
     * Name:
     *  Snake
     *
     * Description:
     *  Swap function for switching places in an array. This will switch the three arrays around based on index
     *
     * Input:
     *  INT - i
     *  INT - x
     *  ARRAYLIST<String> - init
     *  ARRAYLIST<String> - score
     *  ARRAYLIST<String> - dif
     *
     * Output:
     *  None
     */
    public static void swap(int i, int x, ArrayList<String> init, ArrayList<String> score, ArrayList<String> dif)
    {
        // Make our temp variables
        String temp;
        String tempInit;
        String tempDif;
        
        // Swap our score array
        temp = score.get(i);
        score.set(i, score.get(x));
        score.set(x,temp);
        
        // Swap init array
        tempInit = init.get(i);
        init.set(i, init.get(x));
        init.set(x,tempInit);
        
        // Swap def array
        tempDif = dif.get(i);
        dif.set(i, dif.get(x));
        dif.set(x,tempDif);
    }
}
