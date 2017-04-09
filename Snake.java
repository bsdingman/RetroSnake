/*
Bryan Dingman
Lab 8: Snake
The snake class. This class controls building, movement, hit detection, fud spawn for our protagonist. 
Or antagionist, if you are pro fud...
*/

package SnakeGame;

import java.util.ArrayList;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/*
    Class: 
        Snake
    Use:
        Requires Construtor, staticially controlled
        Snake(Pane)
    Methods:
        isAlive()
        killSolidSnake()
        handleDirectionChange()
        getDirection()
        buildSnake()
        moveSnake()
        spawnFud()
        detect_hitFud()
        detect_hitSelf()
        detect_hitWindow()
        
*/
public class Snake 
{
    // Snake Pane, we add to this vs MainPane. Cuz reasons
    Pane SnakePane = new Pane();
    
    /*
     *  Snake Properties 
     */
    // Initial length of snake
    static int length;
    // Is the snake the alive?
    static boolean Alive = true;
    // Initial direction
    static String Direction = "RIGHT";
    // Is the food spawned?
    static boolean fudSpawned = false;
    
    // Piece sizing, used for fud and snake bits
    final static int PIECE_H = 20;
    final static int PIECE_W = 20;
    
    // Colors!
    final static Color HEAD_COLOR = Color.GREEN;
    final static Color BODY_COLOR = Color.LIMEGREEN;
    
    // Snake bits, great in soup!
    Rectangle head = new Rectangle(PIECE_H,PIECE_W);

    // Fud properties
    Rectangle fud = new Rectangle(PIECE_H - 2,PIECE_W - 2);
    final static Color FUD_COLOR = Color.RED;
    
    // Game properties
    static boolean WindowEdgeDeath;
    
    // Storage containers
    static ArrayList<Rectangle> SnakePieces = new ArrayList<>();
    
    /////////////////////////////////////////////
    // Some final variables
    //
    // Difficulty vars (Starting length)
    final static int EASY = 1;
    final static int MEDIUM = 5;
    final static int HARD = 10;
    //
    // Window sizes (Pulled from config)
    final static int WINDOW_X = Config.WINDOW_X;
    final static int WINDOW_Y = Config.WINDOW_Y;
    //
    final double CENTER_X = Config.CENTER_X;
    final double CENTER_Y = Config.CENTER_Y;
    //
    // Movement distance
    final static int STEP = PIECE_W + 1;
    /////////////////////////////////////////////
    
    /*
     * Name:
     *  Snake
     *
     * Description:
     *  Snake class constructor
     *
     * Input:
     *  PANE - pane
     *
     * Output:
     *  None
     */
    public Snake()
    {
    }
    
    public void start(Pane pane)
    {
        pane.getChildren().add(SnakePane);
        
        // Set the color of the head
        head.setFill(HEAD_COLOR);
        
        // Set the side of the pieces
        head.setX(CENTER_X);
        head.setY(CENTER_Y);
        
        // Add it to the pieces
        SnakePieces.add(head);
        
        // Color the food
        fud.setFill(FUD_COLOR);
    }
    
    public void reset()
    {    
        SnakePieces = new ArrayList<>();
        fudSpawned = false;
        Direction = "RIGHT";
        head = new Rectangle(PIECE_H,PIECE_W);
        fud = new Rectangle(PIECE_H - 2,PIECE_W - 2);
        
        Platform.runLater(() -> 
        {
            SnakePane.getChildren().clear();
        });
        
        Alive = true;
        
        ScoreBoard.reset();
    }

    /*
     * Name:
     *  isAlive()
     *
     * Description:
     *  Checks to see if the snake is alive
     *
     * Input:
     *  None
     *
     * Output:
     *  STRING - Alive
     */
    public static boolean isAlive()
    {
        return Alive;
    }
    
    /*
     * Name:
     *  killSolidSnake
     *
     * Description:
     *  Kills the snake if called
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public void killSolidSnake()
    {
        Alive = false;
        Config.ScoreScreenOpen = true;
    }
    
    /*
     * Name:
     *  handleDirectionChange
     *
     * Description:
     *  Updates the direction
     *
     * Input:
     *  direction - STRING
     *
     * Output:
     *  None
     */
    public void handleDirectionChange(String direction)
    {        
        Direction = direction;
    }
    
    /*
     * Name:
     *  getDirection
     *
     * Description:
     *  Returns the direction the snake is to move
     *
     * Input:
     *  None
     *
     * Output:
     *  Direction - STRING
     */
    public static String getDirection()
    {
        return Direction;
    }

    /*
     * Name:
     *  buildSnake
     *
     * Description:
     *  Builds the snake based on difficulty (changes the start length)
     *  Creates the first piece of fud on the board
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public void buildSnake()
    {
        // Set up the difficulty based on option chose
        switch (Config.getDifficulty())
        {
            // Easy difficulty
            case 1:
                length = EASY;
                WindowEdgeDeath = true;
                break;
            // Medium
            case 2:
                length = MEDIUM;
                WindowEdgeDeath = true;
                break;
            // Hard
            case 3:
                length = HARD;
                WindowEdgeDeath = true;
                break;
            // I messed up!
            default: 
                length = EASY;
                WindowEdgeDeath = true;
                break;
        }
        
        // Create the body pieces
        for (int i = 0; i < length; i++)
        {
            Rectangle bodyPiece = new Rectangle(PIECE_W,PIECE_H);
            
            bodyPiece.setFill(BODY_COLOR);
            //bodyPiece.setStroke(Color.BLACK);
            
            SnakePieces.add(bodyPiece);
            
        }
        
        // Add the snake to the screen
        int interation = 0;
        for (Rectangle x : SnakePieces)
        {
            // This offsets the squares so the are farther back from the head
            x.setX(head.getX() - (STEP * interation));
            x.setY(head.getY());
                       
            SnakePane.getChildren().add(x);
            interation++;
        }
        
        // Find the x coord for where the food needs to spawn based on window size
        int x = new Random().nextInt(WINDOW_X / PIECE_W) * PIECE_W;

        // "Margin" around the window so it doesn't spawn on the edge
        if (x > WINDOW_X - (PIECE_W * 3)) 
        {
            x = WINDOW_X - (PIECE_W * 3);
        }
        
        // "Margin"
        if (x < 0 + (PIECE_W * 3)) 
        {
            x = 0 + (PIECE_W * 3);
        }

        // Find the Y coord for where the food needs to spawn based on window size
        int y = new Random().nextInt(WINDOW_Y / PIECE_W) * PIECE_W;

        // "Margin" around the window so it doesn't spawn on the edge
        if (y > WINDOW_Y - (PIECE_W * 3)) 
        {
            y = WINDOW_Y - (PIECE_W * 3);
        }
        
        // "Margin"
        if (y < 0 + (PIECE_W * 3)) 
        {
            y = 0 + (PIECE_W * 3);
        }

        // Set the POS
        fud.setX(x);
        fud.setY(y);
        
        // Add it to the snake pane
        SnakePane.getChildren().add(fud);
        
        // Remember that we have a fud spawned!
        fudSpawned = true;
    }

    /*
     * Name:
     *  moveSnake
     *
     * Description:
     *  Takes the old head
     *  Changes old head into body piece
     *  Creates a new head based on direction and step distance
     *  Add its to the SnakePieces list
     *  Removes the trailing bit 
     *  Sets the new head
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public void moveSnake()
    {
        // Save our old head
        Rectangle oldHead = head;
        
        // Change color of head to body as it's a body piece now
        oldHead.setFill(BODY_COLOR);

        // Create new head
        Rectangle newHead = new Rectangle(PIECE_H,PIECE_W);

        
        // Set the POS based on direction
        switch (Direction)
        {
            case "UP":
                newHead.setY(oldHead.getY() - STEP);
                newHead.setX(oldHead.getX());
                break;
            case "DOWN":
                newHead.setY(oldHead.getY() + STEP);
                newHead.setX(oldHead.getX());
                break;
            case "LEFT":
                newHead.setX(oldHead.getX() - STEP);
                newHead.setY(oldHead.getY());
                break;
            case "RIGHT":
                newHead.setX(oldHead.getX() + STEP);
                newHead.setY(oldHead.getY());
                break;
        }
        
        // Set fill for head
        newHead.setFill(HEAD_COLOR);

        // Add it to the list
        SnakePieces.add(0, newHead);
        
        // Get the butt!
        Rectangle snakeButt = SnakePieces.get(SnakePieces.size() - 1);
        
        Platform.runLater(() -> 
        {
            // Add it to the pane
            SnakePane.getChildren().add(newHead);
            
            // Remove it from the pane
            SnakePane.getChildren().remove(snakeButt);
        });
        
        // Remove it from the arraylist
        SnakePieces.remove(snakeButt);
        
        // remember!
        head = newHead;
    }
    
    /*
     * Name:
     *  spawnFud
     *
     * Description:
     *  Spawns the fud if there are no fuds spawned
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public void spawnFud()
    {
        if (!fudSpawned)
        {
            // Find the x coord for where the food needs to spawn based on window size
            int x = new Random().nextInt(WINDOW_X / PIECE_W) * PIECE_W;

            // "Margin" around the window so it doesn't spawn on the edge
            if (x > WINDOW_X - (PIECE_W * 3)) 
            {
                x = WINDOW_X - (PIECE_W * 3);
            }

            if (x < 0 + (PIECE_W * 3)) 
            {
                x = 0 + (PIECE_W * 3);
            }

            // Find the Y coord for where the food needs to spawn based on window size
            int y = new Random().nextInt(WINDOW_Y / PIECE_W) * PIECE_W;

            // "Margin" around the window so it doesn't spawn on the edge
            if (y > WINDOW_Y - (PIECE_W * 3)) 
            {
                y = WINDOW_Y - (PIECE_W * 3);
            }

            if (y < 0 + (PIECE_W * 3)) 
            {
                y = 0 + (PIECE_W * 3);
            }
        
            Rectangle newFood = new Rectangle(PIECE_H,PIECE_W);
            
            newFood.setFill(FUD_COLOR);
            
            newFood.setX(x);
            newFood.setY(y);
            
            Platform.runLater(() -> 
            {
                // Add it to the pane
                SnakePane.getChildren().add(newFood);
            });

            fud = newFood;
            
            fudSpawned = true;
        } 
    }
    
    /*
     * Name:
     *  detect_hitFood
     *
     * Description:
     *  Checks to see if the snake head has intersected with a fud piece
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public void detect_hitFud()
    {
        if (fud.intersects(head.getX(),head.getY(),head.getWidth(),head.getHeight()))
        {
            // Remember our old fud
            Rectangle oldFud = fud;
            
            // Leave the game thread to update the GUI pane
            Platform.runLater(() -> 
            {
                // Remove it from the pane
                SnakePane.getChildren().remove(oldFud);
            });
         
            // Create the new Fud piece
            Rectangle bodyPiece = new Rectangle(PIECE_W,PIECE_H);
            
            // Set fud attributes
            bodyPiece.setFill(BODY_COLOR);
            bodyPiece.setStroke(Color.BLACK);
            
            // Add a piece to the Snake so it increases in length
            SnakePieces.add(bodyPiece);

            // We need moar fud!
            fudSpawned = false;
            
            // Request a new fud to be spawned
            spawnFud();
           
            // Leave the game thread to add points to the scoreboard
            Platform.runLater(() -> 
            {
                // Add the points
                ScoreBoard.addPoint();
            });
        }
    }
    
    /*
     * Name:
     *  detect_hitSelf
     *
     * Description:
     *  Checks to see if the snake head has hit the body
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public void detect_hitSelf()
    {
        // Remember which interation we are on
        int interation = 0;
        
        // Loop through all the body pieces
        for (Rectangle x : SnakePieces)
        {
            // Skip over the head. 
            if (interation != 0)
            {
                // See if it intersects with a body piece
                if (head.intersects(x.getX(), x.getY(), x.getWidth(), x.getHeight())) 
                {
                    // RIP
                    killSolidSnake();
                }
            }
            // Increase interation
            interation++;
        }
    }
    
    /*
     * Name:
     *  detect_hitWindow
     *
     * Description:
     *  Checks to see if the snake has left the bounding box of the screen. 
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public void detect_hitWindow()
    {
        // Are we playing with Boundries?
        if (WindowEdgeDeath)
        {
            // Top part of screen
            if (head.intersects(0, -10, WINDOW_X, 10)) 
            {
                // RIP
                killSolidSnake();
            }
            
            // Bottom part of screen
            if (head.intersects(0, WINDOW_Y, WINDOW_X, 10)) 
            {
                // RIP
                killSolidSnake();
            }
            
            // left part of screen
            if (head.intersects(-10, 0, 10, WINDOW_Y)) 
            {
                // RIP
                killSolidSnake();
            }
            
            // Right part of screen
            if (head.intersects(WINDOW_X, 0, 10, WINDOW_Y)) 
            {
                // RIP
                killSolidSnake();
            }
        }
    }
}
