/*
Bryan Dingman
Lab 8: Snake
Master Config file:
    Controls various variables across the game. Feel free to update above the "DO NOT EDIT BELOW"
*/

package SnakeGame;

/*
    Class: 
        Config
    Use:
        No Constructor
        Call via direct, like other methods
    Methods:
        getDifficulty()
        setDifficulty(int)
*/
public class Config 
{
    // Starting lengths for difficulties
    public final static int START_LENGTH_EASY       = 1;
    public final static int START_LENGTH_MEDIUM     = 5;
    public final static int START_LENGTH_HARD       = 10;
    
    // Time, in milliseconds, per movement 
    public final static int TIME_EASY       = 100;
    public final static int TIME_MEDIUM     = 80;
    public final static int TIME_HARD       = 60;
    
    // Window sizes (Smaller == harder)
    // Avoid using sizes below 400 x 400
    public final static int WINDOW_X        = 800;
    public final static int WINDOW_Y        = 700;
    
    // Amount of points to be rewarded per fud nommed
    public final static double POINT        = 20;
    
    /*==================================================
                  DO NOT EDIT BELOW!
    ====================================================
    ====================================================
    ====================================================
    ====================================================
    ====================================================
    ====================================================
    ====================================================
    ====================================================
    ====================================================
    ====================================================
    ====================================================*/
    
    // Difficulty stuff
    static int Difficulty;
    static int Speed;
    
    // Screen sizing
    public final static double CENTER_X = WINDOW_X / 2;
    public final static double CENTER_Y = WINDOW_Y / 2;
    
    // Pause 
    static boolean Pause = false;
    
    // Used for after the game stuff
    static boolean LoopForever = true;
    static boolean ScoreScreenOpen = false;
    
    /*==================================================
                    DIFFICULTY
    ====================================================*/
    public static int getDifficulty()
    {
        return Difficulty;
    }
    
    public static void setDifficulty(int state)
    {
        // 1 - easy, 2 - medium, 3 - hard
        Difficulty = state;
        
        switch (state)
        {
            case 1:
                Speed = TIME_EASY;
                break;
            case 2:
                Speed = TIME_MEDIUM;
                break;
            case 3:
                Speed = TIME_HARD;
                break;
        }
    }
    
    /*==================================================
                    PAUSE
    ====================================================*/
    
    public static void pause()
    {
        Pause = true;
    }
    
    public static void resume()
    {
        Pause = false;
    }
}
