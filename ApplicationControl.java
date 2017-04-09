/*
Bryan Dingman
Lab 8: Snake
The main body of the game. This file controls application start, main, and various object references
*/
package SnakeGame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ApplicationControl extends Application
{
    
    // MainPane (Used for building scene)
    Pane MainPane = new Pane();
    
    // Scoreboard allocation
    ScoreBoard ScoreBoard;
    
    // Snake Init
    Snake Snake = new Snake();
    
    // Menus allocation
    Menus Menus;
    
    // Some final variables for controlling window size.
    // Pulls from Config class
    final int WINDOW_X = Config.WINDOW_X;
    final int WINDOW_Y = Config.WINDOW_Y;
    
    // Initalize the MainScene
    Scene MainScene = new Scene(MainPane, WINDOW_X, WINDOW_Y);
    
    // Allocation for our main game thread and FX Task
    public static Task GameTask;
    public static Thread GameThread;
    
    /*==================================================
             CONSTRUCTOR FOR APPLICATION CONTROL
    ====================================================*/
    public ApplicationControl()
    {
        ScoreBoard = new ScoreBoard(MainPane);
        Menus = new Menus(MainPane,this,Snake,ScoreBoard);
    }
    
    /*==================================================
                JAVA FX APPLICATION START
    ====================================================*/
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) 
    {   
        /*==================================================
                          MAIN PANE SETUP
        ====================================================*/
        
        // Make the background black
        MainPane.setStyle("-fx-background-color: black;");
        
        // Set up the EH for our keybinds
        MainPane.setOnKeyPressed (e -> 
        {
            // Make sure the game has started
            if (Config.getDifficulty() != -1)
            {
                // Make sure the only key pressed is an arrow key
                if (e.getCode().isArrowKey()) 
                {
                    if (e.getCode() == KeyCode.UP && !Snake.getDirection().equals("UP"))
                    {
                        Snake.handleDirectionChange("UP");
                    }
                    else if (e.getCode() == KeyCode.DOWN && !Snake.getDirection().equals("DOWN"))
                    {
                        Snake.handleDirectionChange("DOWN");
                    }
                    else if (e.getCode() == KeyCode.LEFT && !Snake.getDirection().equals("LEFT"))
                    {
                        Snake.handleDirectionChange("LEFT");
                    }
                    else if (e.getCode() == KeyCode.RIGHT && !Snake.getDirection().equals("RIGHT"))
                    {
                        Snake.handleDirectionChange("RIGHT");
                    }
                }
                
                // Handle pausing the game
                if (e.getCode() == KeyCode.SPACE)
                {
                    if (Config.Pause)
                    {
                        Config.resume();
                    }
                    else
                    {
                        Config.pause();
                    }
                }
                
                // Allows for you to restart
                if (e.getCode() == KeyCode.ESCAPE && Snake.isAlive())
                {
                    Config.pause();
                    Snake.reset();
                    MainPane.getChildren().clear();
                    Config.setDifficulty(-1);
                    Menus.buildMain("LOAD");
                }
            }
            
        });
        
        // Since onKeyPressed fires twice, once for down, once for up
        // This is needed for the score screen
        MainPane.setOnKeyReleased(e -> 
        {
            if (Config.ScoreScreenOpen)
            {
                // Move up and down the letters/numbers for initials
                if (e.getCode().isArrowKey()) 
                {
                    if (null != e.getCode())   
                    {
                        switch (e.getCode())
                        {
                            case UP:
                                Menus.handleArrows("UP");
                                break;
                            case DOWN:
                                Menus.handleArrows("DOWN");
                                break;
                            case LEFT:
                                Menus.handleArrows("LEFT");
                                break;
                            case RIGHT:
                                Menus.handleArrows("RIGHT");
                                break;
                            default:
                                break;
                        }
                    }
                }
                
                // Shortcut to save!
                if (e.getCode() == KeyCode.ENTER)
                {
                    ScoreBoard.saveScore(Menus.n1.getText()+ Menus.n2.getText()+ Menus.n3.getText(),ScoreBoard.getScore());
                    Menus.buildScoreScreen("LOAD_SCORES");
                    Config.setDifficulty(-1);
                }
            }
        });
        
        MainPane.requestFocus();
        
        /*==================================================
                          MAIN MENU SETUP!
        ====================================================*/
        Menus.buildMain("LOAD");

        /*==================================================
                          VARIABLES SETUP!
        ====================================================*/

        // Keep our thread from running
        Config.setDifficulty(-1);
        
        // Reset the scoreboard
        ScoreBoard.reset();
        
        /*==================================================
                            GAME THREAD!
        ====================================================*/
        GameTask = new Task<Void>()
        {
            @Override
            public Void call()
            {
                // Allows us to not leave the task. This keeps the game
                // running so the player can play again
                while (Config.LoopForever)
                {
                    /*
                    A janky "WaitUntil"
                    The thread has to run before the menu is even loaded
                    This allows us to not have the snake moving around while
                        the player is still selecting the difficulty.
                    I know it's kinda hacky, but it works!
                    */
                    while (Config.getDifficulty() == -1)
                    {
                        try
                        {
                            Thread.sleep(500);
                        }
                        catch (InterruptedException ex)
                        {
                            System.out.println("Unable to sleep");
                        }
                    }

                    // Game loop!
                    while (Snake.isAlive())
                    {
                        if (!Config.Pause)
                        {
                            // Move the snake
                            Snake.moveSnake();

                            // Check to see if we have hit ourself
                            Snake.detect_hitSelf();

                            // Check to see if we hit a fud piece
                            Snake.detect_hitFud();

                            // Check to see if we hit the side window
                            Snake.detect_hitWindow();

                            // Spawn a new fud pellet if we need a new one
                            Snake.spawnFud();
                        }

                        //=================================
                        //          Sleep the Loop
                        //=================================
                        try
                        {
                            Thread.sleep(Config.Speed);
                        }
                        catch (InterruptedException ex)
                        {
                            System.out.println("Unable to sleep");
                        }
                    }
                    
                    // After game loop!
                    // This is to handle when the game is over, but the player wants to play again
                    if (Config.ScoreScreenOpen)
                    {                      
                        Platform.runLater(() -> 
                        {
                            MainPane.getChildren().clear();
                            Menus.buildScoreScreen("LOAD_SAVE");
                        });

                        while (Config.ScoreScreenOpen)
                        {
                            try
                            {
                                Thread.sleep(500);
                            }
                            catch (InterruptedException ex)
                            {
                                System.out.println("Unable to sleep");
                            }
                        }
                    }
                }
                return null;
            }
        };

        
        // Setting up game thread to run
        GameThread = new Thread(GameTask);
        GameThread.setDaemon(true);
        
        // RUN IT!
        GameThread.start();

        /*==================================================
                    DISPLAY THE STAGES
        ====================================================*/
        primaryStage.setTitle("Snake"); // Set the stage title
        primaryStage.setScene(MainScene); // Place the scene in the stage
        primaryStage.setResizable(false);
        primaryStage.show(); // Display the stage 
        
        // Force bind the keypresses to the main scene.
        MainScene.onKeyPressedProperty().bind(MainPane.onKeyPressedProperty());
    }
    
    // Required by IDEs
    public static void main(String[] args) 
    {
        launch(args);
    }
}
