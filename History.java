import java.util.List;
import java.util.Stack;

import javafx.scene.control.Button;

/**
 * Will keep a record of previous actions taken. Used for redoing and 
 * undoing.
 * @author starp
 *
 */
public class History {
  /**
   * State of the grid beforehand.
   */
  private Stack<String> previousStack = new Stack<String>();
  /** 
   * State of the grid after.
   */
  private Stack<String> nextStack = new Stack<String>();
  /**
   * The grid will have "TYPE" columns and "TYPE" rows. 
   */
  private int type;
  /**
   * The undo button will be disabled if not valid.
   */
  private Button undoButton;
  /**
   * The redo button will be disabled if not valid.
   */
  private Button redoButton;
  
  /**
   * Creates the object and passes in the would be size of the grid.
   * @param type
   */
  public History(int type) {
    this.type = type;
  }
  
  /**
   * Sets the undo and redo buttons.
   * @param undoButton    used to undo actions.
   * @param redoButton    used to redo actions.
   */
  public void setButtons(Button undoButton, Button redoButton) {
    this.undoButton = undoButton;
    undoButton.setDisable(true);
    this.redoButton = redoButton;
    redoButton.setDisable(true);
  }
  
  /**
   * Updates the size/type.
   * @param size    the new size of the table.
   */
  public void setSize(int size) {
    type = size;
  } 
  
  /**
   * Adds the state of the newly changed grid to the stack. 
   * @param grid      state of the grid.
   */
  public void addHistory(GCell[][] grid) {
    String state = convertGridToString(grid);
    
    /** Checks if the stack is empty. */
    if (!previousStack.isEmpty()) {
      /** If something has changed to the grid, then the new state is added. */
      if (!state.equals(previousStack.peek())) {
        previousStack.push(state);
        nextStack.clear();
      }
    }
    else {
      previousStack.push(state);
      nextStack.clear();
    }
    
    /** Enables the undo button. */
    undoButton.setDisable(false);
    /** Disables the redo button. */
    redoButton.setDisable(true);
  }
  
  /**
   * Undoes the action done, and make changes to the stacks.
   * @param grid              the grid of cells.
   */
  public void undo(GCell[][] grid, MistakeChecker mistakeChecker, 
                   List<Cage> cages) {
    
    /** Prevents popping from an empty stack. */
    if (!previousStack.empty()) {
      String state = previousStack.pop();
      nextStack.push(convertGridToString(grid));
      redoButton.setDisable(false);
      intepretStateString(state, grid, cages, mistakeChecker);
    }   
    
    /** If there are no previous actions, disable the undo button. */
    if (previousStack.empty()) {
      undoButton.setDisable(true);
    }
  }
  
  /** 
   * Redos the action done, and make changes to the stacks.
   * @param grid              the grid of cells.
   * @param mistakeChecker    checks if there are any mistakes.
   * @param cages             the cages of the grid.
   */
  public void redo(GCell[][] grid, MistakeChecker mistakeChecker,
                   List<Cage> cages) {
    /** Prevents popping from an empty stack. */
    if (!nextStack.empty()) {
      String state = nextStack.pop();
      previousStack.push(convertGridToString(grid));
      undoButton.setDisable(false);
      intepretStateString(state, grid, cages, mistakeChecker);
    }
    
    /** If there are no actions to redo, disable the redo button. */
    if (nextStack.empty()) {
      redoButton.setDisable(true);
    }
  }
  
  /**
   * Resets history by removing everything from the two stacks.
   */
  public void clear() {
    previousStack.clear();
    nextStack.clear();
  }
  
  /**
   * Convert grid state into string.
   * @param grid    the grid of cells.    
   * @return a string containing the contents of all cells.
   */
  private String convertGridToString(GCell[][] grid) {
    /** Stores the content of each cell in the grid. */
    String state = "";
    for (int row = 0; row < type; row++) {
      for (int column = 0; column < type; column++) {
        state += grid[row][column].getContent();
      }
    }
    
    return state;
  }

  /**
   * Changes the state of the grid to the given string.
   * @param state             a string holding the content of all the cells.
   * @param grid              grid of cells.
   * @param cages             the cages within the grid.
   * @param mistakeChecker    checks the mistakes within the grid.
   */
  private void intepretStateString(String state, GCell[][] grid, List<Cage> cages,
                                   MistakeChecker mistakeChecker) {
    /** Keeps track at what point we are in intepreting the state string. */
    int index = 0;
    /** Loops through the cells in the grid. */
    for (int row = 0; row < type; row++) {
      for (int column = 0; column < type; column++) {
        /** If needed, clear cell, else it's assigned the stored value. */
        if (state.charAt(index) == '0') {
          grid[row][column].clear();
        }
        else {
          /** Enters the number of the cell. */
          int number = Integer.parseInt(String.valueOf(state.charAt(index)));
          grid[row][column].enterNumber(number, type);          
        }       

        index++;
      }
    }
    
    /** Redo error checking. */
    mistakeChecker.checkMistakes(cages, grid);
  }
               

}
