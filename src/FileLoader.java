import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;

/**
 * Class that interprets a text file loaded by the user containing a game.
 * @author starp
 *
 */
public class FileLoader {
  /**
   * Will be used to store the cells that are part of the cages.
   */
  private HashSet<Integer> cells = new HashSet<Integer>();
  /**
   * Contains a list of arrays containing cells that are part of the cage. 
   */
  private List<ArrayList<Integer>> cageInfo = new ArrayList<ArrayList<Integer>>();
  /**
   * Creates a list of targets.
   */
  private List<Integer> targets = new ArrayList<Integer>();
  /**
   * Creates a list for the operators. 
   */
  private List<Operations> operators = new ArrayList<Operations>();
  
  /**
   * The game data object created.
   */
  private GameData gameData;

  
  public FileLoader(TextArea textArea) {
    try {
      for (String line : textArea.getText().split("\n")) {
        interpretLine(line);
      }
      
      /** Creates the accessible game data object. */
      if (!createGameData()) {
        gameData = null;
      }
      
      if (gameData == null) {
        Alert alert = new Alert(AlertType.WARNING, "Game cannot be created.");
        alert.show();
      }
    }
    catch (NumberFormatException e) {
      Alert alert = new Alert(AlertType.WARNING, "Only whole numbers accepted!");
      alert.show();
    }
    catch (ArrayIndexOutOfBoundsException e) {
      Alert alert = new Alert(AlertType.WARNING, "Incorrect format used.");
      alert.show();
    }
  }
  
  /**
   * Attempts to load the file.
   * @param path    to the file.
   * @param pane    where the cages will be drawn.
   */
  public FileLoader(String path) {
    /** Object representing the file. */
    File file = new File(path);
    /** Checks that the file path is correct and is not a directory. */
    if (file.exists() && file.isFile()) {
      try {
        /** Readers for reading the file. */
        FileReader reader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(reader);
        
        /** While there is a next line to read. */
        while (bufferedReader.ready()) {
          interpretLine(bufferedReader.readLine());
        }
        bufferedReader.close(); 
        
        /** Creates the accessible game data object. */
        if (!createGameData()) {
          gameData = null;
        }
        
        if (gameData == null) {
          Alert alert = new Alert(AlertType.WARNING, "File cannot be loaded.");
          alert.show();
        }
      } 
      catch (FileNotFoundException e) {
        Alert alert = new Alert(AlertType.WARNING, "File cannot be found.");
        alert.show();
      } 
      catch (IOException e) {
        Alert alert = new Alert(AlertType.WARNING, "An error has occured with "
                                                   + "processing the file."); 
        alert.show();
      }
      catch (NumberFormatException e) {
        Alert alert = new Alert(AlertType.WARNING, "Only whole numbers accepted!");
        e.printStackTrace();
        alert.show();
      }
      catch (ArrayIndexOutOfBoundsException e) {
        Alert alert = new Alert(AlertType.WARNING, "Ensure the format is correct");
        alert.show();
      }
      
    }   
  }
  
  /**
   * Returns the game data object.
   * @return {@link #gameData}
   */
  public GameData getGameData() {
    return gameData;
  }
  
  /**
   * Splits the line into the relevant parts. 
   * @param line    of code.
   */
  private void interpretLine(String line) throws NumberFormatException, ArrayIndexOutOfBoundsException {
    /** Contains the target, operator, and cell numbers. */
    String[] data = line.split(" ");
    
    /** Array storing information on what cells are in the cage. */
    ArrayList<Integer> cageData = new ArrayList<Integer>();
    for (String cellNumber : data[1].split(",")) {
      cells.add(Integer.parseInt(cellNumber));
      cageData.add(Integer.parseInt(cellNumber));
    }
    Collections.sort(cageData);
    
    /** Adds the array of integers to the master list. */
    cageInfo.add(cageData);
    
    /** Adds the target and operator of the cage or just the target. */
    if (data[0].charAt(data[0].length() - 1) == '÷') {
    	targets.add(Integer.parseInt(data[0].substring(0, data[0].length() - 2)));
    }
    else if (data[0].charAt(data[0].length() - 1) == '+' 
    	|| data[0].charAt(data[0].length() - 1) == '-'
    	|| data[0].charAt(data[0].length() - 1) == 'x'
    	|| data[0].charAt(data[0].length() - 1) == '÷') {
      targets.add(Integer.parseInt(data[0].substring(0, data[0].length() - 1)));
    }
    else {
      targets.add(Integer.parseInt(data[0].substring(0, data[0].length())));
    }
    switch (data[0].charAt(data[0].length() - 1)) {
      case '+':
        operators.add(Operations.ADD);
        break;
      case '-':
        operators.add(Operations.MINUS);
        break;
      case 'x':
        operators.add(Operations.TIMES);
        break;
      case '÷':
        operators.add(Operations.DIVIDE);
        break;
      default:
    	operators.add(Operations.NONE);
    }
  }
  
  /** 
   * Creates a game data object that will store data on the new game.
   */
  private boolean createGameData() {
    gameData = new GameData();
    
    /** The dimension of the grid should be over the maximum. */    
    int size = ((int) Math.ceil(Math.sqrt(Collections.max(cells))));
    /** In case the size is 0. */
    size = size != 0 ? size : 1;
    /** If there's overlapping cages, then not all the cells will be covered. */
    if (cells.size() < size * size || size > 9) {
      return false;
    }
    gameData.setSize(size);
    
    /** Checks if the cells are adjacent. */
    for (ArrayList<Integer> cageInfoCells : cageInfo) {
      /** If there is only one cell in the cage, then it is valid. */
      if (cageInfoCells.size() != 1) {
        /** Checks that the cell is connected to one other cell in the cage. */
        for (int cellNumber : cageInfoCells) {
          /** If any one cell is not connected, the new game can't be loaded. */
          if (!hasAdjacentCell(cellNumber, cageInfoCells, size)) {
            return false;           
          } 
        }
      }
    }
    
    
    /** Adds information on the cages. */
    gameData.setCages(cageInfo);
    gameData.setOperators(operators);
    gameData.setTargets(targets);
    return true;
  }
  
  /**
   * Checks whether a cell is connected to at least one other cell in the cage.
   * @param cageInfoCells   the cells in the cage.
   * @param cell          the cell we are checking this property for.
   * @param size          the number of columns/rows in the grid.
   * @return true if the cell is adjacent to one another in the array.
   */
  private boolean hasAdjacentCell(Integer cell, ArrayList<Integer> cageInfoCells, int size) {
    /** Checks the other cells. */
    for (int otherCell : cageInfoCells) {
      /** Checks if that other cell is above/below/on the right/left. */
      if (otherCell == cell - size || otherCell == cell + size 
          || otherCell == cell - 1 || otherCell == cell + 1) {
        return true;
      }
    }
    
    /** Else return false. */
    return false;
  }
  
}
