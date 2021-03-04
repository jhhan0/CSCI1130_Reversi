package reversi;

import java.awt.Color;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * ReversiOnFile is a subclass of Reversi, adding File I/O capabilities
 * for loading and saving game.
 *
 * I declare that the work here submitted is original
 * except for source material explicitly acknowledged,
 * and that the same or closely related material has not been
 * previously submitted for another course.
 * I also acknowledge that I am aware of University policy and
 * regulations on honesty in academic work, and of the disciplinary
 * guidelines and procedures applicable to breaches of such
 * policy and regulations, as contained in the website.
 *
 * University Guideline on Academic Honesty:
 *   http://www.cuhk.edu.hk/policy/academichonesty
 * Faculty of Engineering Guidelines to Academic Honesty:
 *   https://www.erg.cuhk.edu.hk/erg/AcademicHonesty
 *
 * Student Name: HAN, Jihun
 * Student ID  : 1155128719
 * Date        : 4/12/2020
 * 
 */
public class ReversiOnFile extends Reversi {
    
    public static final char UNICODE_BLACK_CIRCLE = '\u25CF';
    public static final char UNICODE_WHITE_CIRCLE = '\u25CB';
    public static final char UNICODE_WHITE_SMALL_SQUARE = '\u25AB';
    
    // constructor to give a new look to new subclass game
    public ReversiOnFile()
    {
        super();
        window.setTitle("ReversiOnFile");
        gameBoard.setBoardColor(Color.BLUE);
    }


    // STUDENTS' WORK HERE    
    public void loadBoard(String filename) throws Exception
    {
        // 1) prepare an empty board
        for (int row = 1; row <= 8; row++)
        {
            for (int col = 1; col <= 8; col++)
            {
                pieces[row][col] = EMPTY;
            }
        }
        // 2) load board and current player from file in UTF-8 Charset encoding
        try 
        {
            Scanner scanFile = new Scanner(new File(filename), "UTF-8");
            char[] loadPieces = new char[10];
            int line = 0, index;
            while (scanFile.hasNextLine())
            {
                String str = scanFile.nextLine();
                int length = str.length();
                for (index = 0; index < length; index++)
                {
                    loadPieces[index] = str.charAt(index);
                    
                    switch (loadPieces[index]) 
                    {
                        case UNICODE_WHITE_SMALL_SQUARE:
                            pieces[line + 1][index + 1] = EMPTY;
                            break;
                        case UNICODE_BLACK_CIRCLE:
                            pieces[line + 1][index + 1] = BLACK;
                            break;
                        case UNICODE_WHITE_CIRCLE:
                            pieces[line + 1][index + 1] = WHITE;
                            break;
                        default:
                            break;
                    }
                }
                
                line++;
                if (line == 8)
                {
                    break;
                }
            }
            if (scanFile.hasNextLine())
            {
                String strPlayer = scanFile.nextLine();
                char player = strPlayer.charAt(0);
                
                switch (player) 
                {
                    case UNICODE_BLACK_CIRCLE:
                        currentPlayer = BLACK;
                        break;
                    case UNICODE_WHITE_CIRCLE:
                        currentPlayer = WHITE;
                        break;
                }
            }
        // 3) display successful messages and update game status on screen
            gameBoard.addText("Loaded board from " + filename);
            System.out.println("Loaded board from " + filename);
            gameBoard.updateStatus(pieces, currentPlayer);
            
            // check whether next player is forced pass or not
            boolean notForcedPass = forcedPassCheck();
                
            if (notForcedPass == false)
            {
                gameBoard.addText("Forced Pass");
                currentPlayer = FLIP * currentPlayer;
                gameBoard.updateStatus(pieces, currentPlayer);
                
                // check whether next player is double forced pass or not
                boolean notDoubleForcedPass = forcedPassCheck();

                if (notDoubleForcedPass == false)
                {
                    gameBoard.addText("Double Forced Pass");
                    gameBoard.addText("End game!");
                }
            }
        }
        catch (Exception e)
        {
        // 4) in case of any Exception:
        //default game setting with BLACK first
            pieces[4][4] = WHITE;
            pieces[4][5] = BLACK;
            pieces[5][4] = BLACK;
            pieces[5][5] = WHITE;
            currentPlayer = BLACK;
            
            gameBoard.addText("Cannot load board from " + filename);
            System.out.println("Cannot load board from " + filename);
            gameBoard.updateStatus(pieces, currentPlayer);
        }   
        // you may implement a method to setupBoardDefaultGame();
    }

    

    // STUDENTS' WORK HERE    
    public void saveBoard(String filename) throws Exception
    {
        try{
        // 1) open/overwrite a file for writing text in UTF-8 Charset encoding
            PrintStream saveFile = new PrintStream(new File(filename), "UTF-8");
            
        // 2) save board to the file on 8 lines of 8 Unicode char on each line
            int row, col;
            int[][] numArray = new int [10][10];
            for (row = 1; row <= 8; row++)
            {
                for (col = 1; col <= 8; col++)
                {
                    numArray [row - 1][col - 1] = pieces[row][col];
                    
                    switch (numArray [row - 1][col - 1])
                    {
                        case 0:
                            saveFile.print(UNICODE_WHITE_SMALL_SQUARE);
                            break;
                        case 1:
                            saveFile.print(UNICODE_WHITE_CIRCLE);
                            break;
                        case -1: 
                            saveFile.print(UNICODE_BLACK_CIRCLE);
                            break;
                    }
                }
                saveFile.println("");
            }
        // 3) save current player on line 9 and display successful messages
            switch (currentPlayer)
            {
                case 1:
                    saveFile.print(UNICODE_WHITE_CIRCLE);
                    break;
                case -1: 
                    saveFile.print(UNICODE_BLACK_CIRCLE);
                    break;
            }
            gameBoard.addText("Saved board to " + filename);
            System.out.println("Saved board to " + filename);
        }  
        catch (Exception e)
        {    
        // 4) in case of any Exception:
            gameBoard.addText("Cannot save board to " + filename);
            System.out.println("Cannot save board to " + filename);
        }            
    }
    

    
    // STUDENTS' WORK HERE    
    /**
     * Override sayGoodbye method of super class, to save board
     */
    // ...
    // {
        // as usual, sayGoodbye...
        
        // ask for save filename
        // String filename = JOptionPane.showInputDialog("Save board filename");
        
        // save board to file
        // ...
    // }
    
    
    
    @Override
    protected void sayGoodbye(){
        String filename = JOptionPane.showInputDialog("Save board filename");
        try
        {
            System.out.println("Goodbye!");
            saveBoard(filename);
        }
        catch (Exception e)
        {
        //to handle Exception
        }
    }
     
    
    
    // STUDENTS' WORK HERE    
    // main() method, starting point of subclass ReversiOnFile
    public static void main(String[] args) {
        ReversiOnFile game = new ReversiOnFile();
        
        
        // comment or remove the following statements for real game play
//        game.setupDebugBoardEndGame();
//        game.saveBoard("game4.txt");
//        game.setupDebugBoardMidGame();
//        game.saveBoard("game8.txt");
        // end of sample/ debugging code
        
        
        
        // ask for load filename
        String filename = JOptionPane.showInputDialog("Load board filename");
        // load board from file
        try
        {
            game.loadBoard(filename);
        }
        catch (Exception e)
        {
        //to handle Exception
        }
        // ...
    }

}
    