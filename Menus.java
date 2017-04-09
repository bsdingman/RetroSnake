/*
Bryan Dingman
Lab 8: Snake
Class for all the menus, Controls Main Menu, Difficulty Menu, and High score menu
*/

package SnakeGame;

import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/*
    Class: 
        Menus
    Use:
        Requires Construtor, staticially controlled
        Menus (Pane, ApplicationControl, Snake, ScoreBoard)
    Methods:
        buildMain(String state)
        buildDifficulty(String state)
        buildScoreScreen (String state)
        handleButtons(Button button)
        resetInitials()
        handleArrows(String dir)
        
*/
public class Menus 
{
    // Some initalization
    Pane MainPane;
    ApplicationControl AppControl;
    ScoreBoard ScoreBoard;
    Snake Snake;
    
    // Final variables
    final int WINDOW_X = Config.WINDOW_X;
    final int WINDOW_Y = Config.WINDOW_Y;
    //
    final double CENTER_X = Config.CENTER_X;
    final double CENTER_Y = Config.CENTER_Y;
    
    /*
     *  Main Menu
     */
    Button play = new Button("Play");
    Button exit = new Button("Quit");
    Label title_mainmenu = new Label("SNAKE");
    
    /*
     *  Difficulty Menu
     */
    Label title_difficulty = new Label("Chose your difficulty");
    Button easy = new Button("Easy");
    Button medium = new Button("Medium");
    Button hard = new Button("Hard");
    
    /*
     *  High Score Menu
     */
    Label title_highscore = new Label("Top 10 Highest Scores");
    Button playNew = new Button("Play Again");
    Button save = new Button("Save");
    Label enterName = new Label("Enter Your Initials");
    
    // Up and down arrows 
    Label upArrow = new Label("\u02C5");
    Label downArrow = new Label("\u02C4");
    
    // Initials labels 
    Label n1 = new Label("A");
    Label n2 = new Label("A");
    Label n3 = new Label("A");   
    
    // Storage containers 
    ArrayList<String> List = new ArrayList<>();
    ArrayList<Label> ScoreText = new ArrayList<>();
    
    // Remember where we are in the selector for initials 
    int index_0 = 0;
    int index_1 = 0;
    int index_2 = 0;
    int curSelected = 0;
    
    /*
     * Name:
     *  Menus
     *
     * Description:
     *  Menus class constructor
     *
     * Input:
     *  PANE - mainPane
     *  APPLICATIONCONTROL - appControl
     *  SNAKE - newSnake
     *  SCOREBOARD - score
     *
     * Output:
     *  None
     */
    public Menus(Pane mainPane, ApplicationControl appControl, Snake newSnake, ScoreBoard score)
    {
        // Save our params so we can use them later
        this.MainPane = mainPane;
        this.AppControl = appControl;
        this.Snake = newSnake;
        this.ScoreBoard = score;
        
        /*
         *  Main Menu
         */
        // Set up the play button
        play.setLayoutX(CENTER_X - 35);
        play.setLayoutY(CENTER_Y + 25);
        
        // Stylize it!
        handleButtons(play);
        
        // Set up the exit button
        exit.setLayoutX(CENTER_X - 31);
        exit.setLayoutY(CENTER_Y + 50);
        
        // Stylize it!
        handleButtons(exit);
        
        // Set up the Snake Title
        title_mainmenu.setLayoutX(CENTER_X - 85);
        title_mainmenu.setLayoutY(CENTER_Y - 35);
        title_mainmenu.setFont(Font.font("Courier New",FontWeight.BOLD,55));
        title_mainmenu.setTextFill(Color.LIMEGREEN);
        
        // Set up the EH's for play
        play.setOnMouseClicked(e -> 
        {
            // Closes the main menu and loads the difficulty menu
            buildMain("UNLOAD");
            buildDifficulty("LOAD");
        });
        
        // Set up the EH's for exit
        exit.setOnMouseClicked(e ->
        {
            // Closes the game
            Platform.exit();
        });
                
        /*
         *  Difficulty Menu
         */
        // Set up the difficulty title
        title_difficulty.setLayoutX(CENTER_X - 125);
        title_difficulty.setLayoutY(CENTER_Y - 25);
        title_difficulty.setFont(Font.font("Courier New",FontWeight.BOLD,20));
        title_difficulty.setTextFill(Color.LIMEGREEN);
        
        // Set up the easy button
        easy.setLayoutX(CENTER_X - 35);
        easy.setLayoutY(CENTER_Y);
        
        // Stylize it!
        handleButtons(easy);
        
        // Set up the medium button
        medium.setLayoutX(CENTER_X - 45);
        medium.setLayoutY(CENTER_Y + 30);
        
        // Stylize it!
        handleButtons(medium);
        
        // Set up the hard button
        hard.setLayoutX(CENTER_X - 35);
        hard.setLayoutY(CENTER_Y + 60);
        
        // Stylize it!
        handleButtons(hard);
        
        // Set up the EH's for difficulty
        easy.setOnMouseClicked(e -> 
        {
            // Close the difficulty menu
            buildDifficulty("UNLOAD");
            
            // set the difficulty 
            Config.setDifficulty(1);
            
            // Start the snake
            Snake.start(MainPane);
            
            // Build the snake
            Snake.buildSnake();
            
            // Build the scoreboard
            ScoreBoard.build();
            
            // Make sure the snake isn't paused
            Config.resume();
        });
        
        medium.setOnMouseClicked(e -> 
        {
            buildDifficulty("UNLOAD");

            Config.setDifficulty(2);
            
            Snake.start(MainPane);
            
            Snake.buildSnake();
            
            ScoreBoard.build();
            
            Config.resume();
        });
        
        hard.setOnMouseClicked(e -> 
        {
            buildDifficulty("UNLOAD");
            
            Config.setDifficulty(3);
            
            Snake.start(MainPane);
            
            Snake.buildSnake();
            
            ScoreBoard.build();
            
            Config.resume();
        });
        
        /*
         *  High Score Menu
         */
        // Set up the title
        title_highscore.setLayoutX(CENTER_X - 95);
        title_highscore.setLayoutY(CENTER_Y - 200);
        title_highscore.setFont(Font.font("Courier New",FontWeight.BOLD,15));
        title_highscore.setTextFill(Color.LIMEGREEN);
        
        // Set up the enter name title
        enterName.setLayoutX(CENTER_X - 85);
        enterName.setLayoutY(CENTER_Y - 30);
        enterName.setFont(Font.font("Courier New",FontWeight.BOLD,15));
        enterName.setTextFill(Color.LIMEGREEN);
        
        // Set up the First initial
        n1.setLayoutX(CENTER_X - 28);
        n1.setLayoutY(CENTER_Y);
        n1.setFont(Font.font("Courier New",25));
        n1.setTextFill(Color.LIMEGREEN);
        
        // Set up the up arrow at the first initial
        upArrow.setLayoutX(CENTER_X - 25);
        upArrow.setLayoutY(CENTER_Y - 10);
        upArrow.setFont(Font.font("Courier New",15));
        upArrow.setTextFill(Color.LIMEGREEN);
        
        // Set up the down arrow at the first initial
        downArrow.setLayoutX(CENTER_X - 25);
        downArrow.setLayoutY(CENTER_Y + 22);
        downArrow.setFont(Font.font("Courier New",15));
        downArrow.setTextFill(Color.LIMEGREEN);
        
        // Set up the second initial
        n2.setLayoutX(CENTER_X - 8);
        n2.setLayoutY(CENTER_Y);
        n2.setFont(Font.font("Courier New",25));
        n2.setTextFill(Color.LIMEGREEN);

        // Set up the third initial
        n3.setLayoutX(CENTER_X + 12);
        n3.setLayoutY(CENTER_Y);
        n3.setFont(Font.font("Courier New",25));
        n3.setTextFill(Color.LIMEGREEN);

        // Set up the save button
        save.setLayoutX(CENTER_X - 35);
        save.setLayoutY(CENTER_Y + 35);
        
        // Save button EH
        save.setOnMouseClicked(e -> 
        {
            ScoreBoard.saveScore(n1.getText()+n2.getText()+n3.getText(),ScoreBoard.getScore());
            buildScoreScreen("LOAD_SCORES");
            Config.setDifficulty(-1);
        });
        
        // Stylize it!
        handleButtons(save);

        // Set up the play new button
        playNew.setLayoutX(CENTER_X - 63);
        playNew.setLayoutY(CENTER_Y + 90);
        
        // Set up the play new EH
        playNew.setOnMouseClicked(e -> 
        {
            // Unload the score screne
            buildScoreScreen("UNLOAD");
            
            // Adjust it so our thread will reset
            Config.ScoreScreenOpen = false;
            
            // Build the difficulty menu
            buildDifficulty("LOAD");
            
            // Reset the snake and fuds
            Snake.reset();

            // Make sure we aren't paused
            Config.resume();
        });

        // Stylize it!
        handleButtons(playNew);
        
        //////////////////////////////////////////////
        //////////////////////////////////////////////
        //////////////////////////////////////////////
        // Add the initials so we can use them later
        List.add("A");List.add("B");List.add("C");List.add("D");List.add("E");List.add("F");List.add("G");List.add("H");
        List.add("I");List.add("J");List.add("K");List.add("L");List.add("M");List.add("N");List.add("O");List.add("P");
        List.add("Q");List.add("R");List.add("S");List.add("T");List.add("U");List.add("V");List.add("W");List.add("X");
        List.add("Y");List.add("Z");List.add("1");List.add("2");List.add("3");List.add("4");List.add("5");List.add("6");
        List.add("7");List.add("8");List.add("9");List.add("0");
    }
    
    /*
     * Name:
     *  buildMain
     *
     * Description:
     *  Builds the main menu. Sets moves the quit button and adds to the main pane
     *
     * Input:
     *  STRING - state
     *
     * Output:
     *  None
     */
    public void buildMain(String state)
    {
        switch (state)
        {
            case "LOAD":
            {
                exit.setLayoutX(CENTER_X - 33);
                exit.setLayoutY(CENTER_Y + 50);
                
                MainPane.getChildren().add(play);
                MainPane.getChildren().add(exit);
                MainPane.getChildren().add(title_mainmenu);
                
                break;
            }
            case "UNLOAD":
            {
                MainPane.getChildren().remove(play);
                MainPane.getChildren().remove(exit);
                MainPane.getChildren().remove(title_mainmenu);
                break;
            }
            default:
            {
                break;
            }
        }
    }
    
    /*
     * Name:
     *  buildDifficulty
     *
     * Description:
     *  Builds the difficulty menu. 
     *
     * Input:
     *  STRING - state
     *
     * Output:
     *  None
     */
    public void buildDifficulty(String state)
    {
        switch (state)
        {
            case "LOAD":
            {
                MainPane.getChildren().add(title_difficulty);
                MainPane.getChildren().add(easy);
                MainPane.getChildren().add(medium);
                MainPane.getChildren().add(hard);
                break;
            }
            case "UNLOAD":
            {
                MainPane.getChildren().remove(title_difficulty);
                MainPane.getChildren().remove(easy);
                MainPane.getChildren().remove(medium);
                MainPane.getChildren().remove(hard);
                break;
            }
            default:
            {
                break;
            }
        }
    }
    
    /*
     * Name:
     *  buildScoreScreen
     *
     * Description:
     *  Builds the scorescreen. Handles the initial screen, and the scores
     *
     * Input:
     *  STRING - state
     *
     * Output:
     *  None
     */
    public void buildScoreScreen (String state)
    {
        switch (state)
        {
            case "LOAD_SAVE":
            {
                //MainPane.getChildren().add(exit);
                MainPane.getChildren().add(enterName);
                MainPane.getChildren().add(save);
                MainPane.getChildren().add(upArrow);
                MainPane.getChildren().add(downArrow);
                
                MainPane.getChildren().add(n1);
                MainPane.getChildren().add(n2);
                MainPane.getChildren().add(n3);
                break;
            }
            case "LOAD_SCORES":
                
                MainPane.getChildren().remove(enterName);
                MainPane.getChildren().remove(save);
                MainPane.getChildren().remove(upArrow);
                MainPane.getChildren().remove(downArrow);
                
                MainPane.getChildren().remove(n1);
                MainPane.getChildren().remove(n2);
                MainPane.getChildren().remove(n3);
                
                exit.setLayoutX(CENTER_X - 35);
                exit.setLayoutY(CENTER_Y + 120);
                
                MainPane.getChildren().add(exit);
                MainPane.getChildren().add(title_highscore);
                MainPane.getChildren().add(playNew);
                
                // Based on our scores, dynamically create the texts
                int y = 200;
                for (int i = 0; i < ScoreBoard.FinalScores.size() && i < 10; i++)
                {
                    // Remove 20 pixels 
                    y -= 20;
                    
                    // Create the label 
                    Label text = new Label(ScoreBoard.FinalScores.get(i));
                    
                    // Assign the x and y
                    text.setLayoutX(CENTER_X - 76);
                    text.setLayoutY(CENTER_Y - y);
                    text.setFont(Font.font("Courier New",15));
                    text.setTextFill(Color.LIMEGREEN);
                    
                    // Add to the main pane
                    MainPane.getChildren().add(text);
                    
                    // Remember it so we can remove it later
                    ScoreText.add(text);
                }
                
                
                break;
            case "UNLOAD":
            {
                MainPane.getChildren().remove(title_highscore);
                MainPane.getChildren().remove(playNew);
                MainPane.getChildren().remove(exit);
                MainPane.getChildren().remove(enterName);
                MainPane.getChildren().remove(n1);
                MainPane.getChildren().remove(n2);
                MainPane.getChildren().remove(n3);

                // Remove all the scores
                for (Label x: ScoreText)
                {
                    MainPane.getChildren().remove(x);
                }
                
                ScoreText.clear();
                
                // Reset the initials so they start at AAA
                resetInitials();
                break;
            }
            default:
            {
                break;
            }
        }
    }
    
    /*
     * Name:
     *  handleButtons
     *
     * Description:
     *  Stylizes the button passed to it so it will change when hovered 
     *
     * Input:
     *  BUTTON - button
     *
     * Output:
     *  None
     */
    public final void handleButtons(Button button)
    {
        String defaultStyle = 
            "-fx-font: 18 'courier new';"
            + "-fx-background-color: transparent;"
            + "-fx-text-fill: limegreen;"
            + "-fx-font-weight: normal;";
        
        String hoverStyle = 
            "-fx-font: 18 'courier new';"
            + "-fx-background-color: transparent;"
            + "-fx-text-fill: limegreen;"
            + "-fx-font-weight: bold;";
        
        button.setStyle(defaultStyle);
        
        button.setOnMouseEntered(e ->
        {
           button.setStyle(hoverStyle);
        });
        
        button.setOnMouseExited(e ->
        {
           button.setStyle(defaultStyle);
        });    
    }
    
    /*
     * Name:
     *  resetInitials
     *
     * Description:
     *  Resets the initials to AAA and moves the arrows back to the first one
     *
     * Input:
     *  None
     *
     * Output:
     *  None
     */
    public void resetInitials()
    {
        // Reset vars
        index_0 = 0;
        index_1 = 0;
        index_2 = 0;
        curSelected = 0;
        
        // Set it back to the first thing (Which is A)
        n1.setText(List.get(index_0));
        n2.setText(List.get(index_1));
        n3.setText(List.get(index_2));
        
        // Move the arrows back
        upArrow.setLayoutX(CENTER_X - 25);
        upArrow.setLayoutY(CENTER_Y - 10);
        downArrow.setLayoutX(CENTER_X - 25);
        downArrow.setLayoutY(CENTER_Y + 22);
    }
    
    /*
     * Name:
     *  handleArrows
     *
     * Description:
     *  Move the arrows based on direction
     *
     * Input:
     *  STRING - dir
     *
     * Output:
     *  None
     */
    public void handleArrows(String dir)
    {
        // I like nested switches
        switch (dir)
        {
            // Handles moving through the letters/numbers
            case "UP":
                switch (curSelected) 
                {
                    case 0:
                        // Move our index back one
                        index_0--;
                        
                        // If it's less than zero, move index to the end of our array
                        if (index_0 < 0)
                        {
                            index_0 = List.size() - 1;
                        }
                        
                        // Set the text
                        n1.setText(List.get(index_0));
                        break;
                        
                    // Repeat for the same above
                    case 1:
                        index_1--;
                        if (index_1 < 0)
                        {
                            index_1 = List.size() - 1;
                        }
                        
                        n2.setText(List.get(index_1));
                        break;
                    case 2:
                        index_2--;
                        if (index_2 < 0)
                        {
                            index_2 = List.size() - 1;
                        }
                        
                        n3.setText(List.get(index_2));
                        break;
                    default:
                        break;
                }
                break;
            case "DOWN":
                // Handles the same way as UP but the opposite
                switch (curSelected) 
                {
                    case 0:
                        index_0++;
                        if (index_0 > List.size() - 1)
                        {
                            index_0 = 0;
                        }
                        
                        n1.setText(List.get(index_0));
                        break;
                    case 1:
                        index_1++;
                        if (index_1 > List.size() - 1)
                        {
                            index_1 = 0;
                        }
                        
                        n2.setText(List.get(index_1));
                        break;
                    case 2:
                        index_2++;
                        if (index_2 > List.size() - 1)
                        {
                            index_2 = 0;
                        }
                        
                        n3.setText(List.get(index_2));
                        break;
                    default:
                        break;
                }
                break;
            case "LEFT":
                // Handle moving between columns
                switch (curSelected) 
                {
                    case 0:
                        // Set the current column
                        curSelected = 2;
                        // Move the up arrow
                        upArrow.setLayoutX(CENTER_X + 15);
                        upArrow.setLayoutY(CENTER_Y - 10);
                        // Move the down arrow
                        downArrow.setLayoutX(CENTER_X + 15);
                        downArrow.setLayoutY(CENTER_Y + 22);
                        break;
                    case 1:
                        curSelected = 0;
                        upArrow.setLayoutX(CENTER_X - 25);
                        upArrow.setLayoutY(CENTER_Y - 10);
                        downArrow.setLayoutX(CENTER_X - 25);
                        downArrow.setLayoutY(CENTER_Y + 22);
                        break;
                    case 2:
                        curSelected = 1;
                        upArrow.setLayoutX(CENTER_X - 5);
                        upArrow.setLayoutY(CENTER_Y - 10);
                        downArrow.setLayoutX(CENTER_X - 5);
                        downArrow.setLayoutY(CENTER_Y + 22);
                        break;
                    default:
                        break;
                }
                break;
            // Repeat the same as for LEFT but other way
            case "RIGHT":
                switch (curSelected) 
                {
                    case 0:
                        curSelected = 1;
                        upArrow.setLayoutX(CENTER_X - 5);
                        upArrow.setLayoutY(CENTER_Y - 10);
                        downArrow.setLayoutX(CENTER_X - 5);
                        downArrow.setLayoutY(CENTER_Y + 22);
                        break;
                    case 1:
                        curSelected = 2;
                        upArrow.setLayoutX(CENTER_X + 15);
                        upArrow.setLayoutY(CENTER_Y - 10);
                        downArrow.setLayoutX(CENTER_X + 15);
                        downArrow.setLayoutY(CENTER_Y + 22);
                        break;
                    case 2:
                        curSelected = 0;
                        upArrow.setLayoutX(CENTER_X - 25);
                        upArrow.setLayoutY(CENTER_Y - 10);
                        downArrow.setLayoutX(CENTER_X - 25);
                        downArrow.setLayoutY(CENTER_Y + 22);
                        break;
                    default:
                        break;
                }
                break;
        }
    }
}
