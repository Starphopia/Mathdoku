import java.util.ArrayList;

import java.util.List;

import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

/** 
   * Used for the detection of mistakes. 
   */
class MistakeChecker {
  /**
   * The size of the grid.
   */
  private int type;
  /**
   * The numbers which each row and column must contain.
   */
  private ArrayList<Integer> mustContain = new ArrayList<Integer>();
  /**
   * Will highlight incorrect rows and columns.
   */
  private Highlighter highlighter;
  /**
   * Whether mistakes should be shown.
   */
  private boolean showMistakes = false;
  /**
   * Pane where the grid will be contained.
   */
  Pane paneToAdd;
  
  /** 
   * Constructor will initialise some of the local variables.
   * @param paneToAdd     required for the highlighter to make changes to 
   *                      the grid.
   * @param type          the size of the grid.
   */
  public MistakeChecker(Pane paneToAdd, int type) {
    this.type = type;
    /** Creates list of numbers which each row/column should contain. */
    for (int i = 1; i <= type; i++) {
      mustContain.add(i);
    }  
    this.paneToAdd = paneToAdd;
    /** Initialises the highlighter. */
    highlighter = new Highlighter(paneToAdd);
  }
  
  /**
   * Accessor for the variable showMistake
   * @return whether the user have chosen to show mistakes.
   */
  public boolean getShowMistake() {
    return showMistakes;
  }
  
  /** 
   * Sets the size of the grid.
   * @param size    new size of the grid.
   */
  public void setType(int size) {
    type = size;
    mustContain.clear();
    for (int i = 1; i <= size; i++) {
      mustContain.add(i);
    }
    
    highlighter = new Highlighter(paneToAdd);
  }
  
  /**
   * Toggles whether mistakes should be shown.
   * @param cages   cages of the grid.
   * @param grid    the grid of cells.
   */
  public void toggleShowMistake(List<Cage> cages, GCell[][] grid) {
    showMistakes = !showMistakes;
    
    if (showMistakes) {
      checkMistakes(cages, grid);
    }
    else {
      highlighter.hideHighlights(cages);
    }
  }
  
  /** 
   * Will be used to check whether there are any mistakes.
   * @param cages   the cages on the grid currently.
   * @param grid    the grid of cells.
   */
  public void checkMistakes(List<Cage> cages, GCell[][] grid) {
    boolean correct = true;

    /** Checks if each cage has been completed correctly. */
    for (Cage cage : cages) {
      correct = correct && cage.completedCorrectly();
      /** Highlights cages if completed incorrectly. */
      if (!cage.completedCorrectly() && showMistakes) {
        cage.highlightWrong(showMistakes);
      }
      else {
        cage.resetFill();
      }
    }

    /** Checks both rows and columns. */
    correct &= checkColumnRow(true, grid);
    correct &= checkColumnRow(false, grid);
   
    /** If completed correctly it will create a win alert. */
    notifyWon(correct, grid);
  }
  
  /** 
   * Removes all highlights. 
   */
  public void resetMistakeChecker() {
    /** Removes every row and column highlight. */
    for (int i = 0; i < type * 2; i++) {
      highlighter.remove(i);
    }
  }
  
  /**
   * Checks whether the row or column is completed correctly.
   * @param isRow   whether a row is being checked.
   * @param grid    is the grid that stores the cells.
   * @return true if the column has been completed correctly.
   */
  private boolean checkColumnRow(boolean isRow, GCell[][] grid) {
    boolean correct = true;
    /** Checks whether each row has been completed correctly. */
    for (int variable = 0; variable < type; variable++) {
      /** Keeps track of the numbers in the row. */
      List<Integer> numbersContained = new ArrayList<Integer>();
      boolean shouldHighlight = false;
      for (int cellNo = 0; cellNo < type; cellNo++) {        
        int cellContent = isRow ? grid[variable][cellNo].getContent()
                                : grid[cellNo][variable].getContent();
        /** Sees if there are any duplicate numbers. */
        if (numbersContained.contains(cellContent) && cellContent != 0) {
          shouldHighlight = true;
        } else {
          /** Adds the number. */
          numbersContained.add(cellContent);
        }
      }        
      /** Highlights the row or column if there are duplicate numbers. */
      if (shouldHighlight && showMistakes) {
        highlighter.highlightColumnRow(variable, grid, isRow);        
      }
      else {
        highlighter.remove(isRow ? variable : variable + type);         
      }
    
      /** False if the row does not contain all the numbers required. */
      correct = correct && numbersContained.containsAll(mustContain); 
    }
    return correct;
  }
  
  /** 
   * If the user has completed the grid correctly, they are alerted.
   * @param correct   whether the grid has been completed correctly.
   * @param grid      where the cells can be animated.
   */
  private void notifyWon(boolean correct, GCell[][] grid) {
    if (correct) {
      Alert alert = new Alert(AlertType.NONE, 
                              "You have completed the grid correctly");
      alert.setTitle("Congratulations!");
      alert.getButtonTypes().add(ButtonType.OK);
      alert.show();    
      
      /** Animates each cell. */
      for (int i = 0; i < type; i++) {
        for (int j = 0; j < type; j++) {
          /** Animates the grid. */
          RotateTransition rotateCellTransition = new RotateTransition();
          rotateCellTransition.setDuration(Duration.seconds(10));
          rotateCellTransition.setByAngle(360);
          rotateCellTransition.setCycleCount(50);
          
          ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(2));
          scaleTransition.setToX(1.5f);
          scaleTransition.setToY(1.5f);
          scaleTransition.setCycleCount(250);
          scaleTransition.setAutoReverse(true);
          
          FillTransition fillTransition = new FillTransition(Duration.millis(3000), 
                                                             Color.rgb(0, 200, (j % 4) * 50, 0.5), 
                                                             Color.rgb(0, 50 * (i % 4), 200, 0.5));
          fillTransition.setCycleCount(600);
          
          ParallelTransition parallelTransition = new ParallelTransition();
          parallelTransition.setNode(grid[i][j]);
          parallelTransition.getChildren().addAll(rotateCellTransition, scaleTransition, 
                                                  fillTransition);
          parallelTransition.play();
        }
      }
    }
  }
  
  /**
   * Will be used to highlight incorrect rows and columns.
   * @author starp   
   */
  private class Highlighter {
    /**
     * Highlights for rows and columns (stored in the last half).
     */
    private Shape[] highlights;
    /**
     * The pane where the shapes will be added to.
     */
    private Pane paneToAdd;
    
    /** 
     * Will initialise the arrays.
     * @param paneToAdd   where the highlights will be created.
     */
    private Highlighter(Pane paneToAdd) {
      /** The shapes highlighted. */
      highlights = new Shape[type * 2];
      this.paneToAdd = paneToAdd;
    }
    
    /** 
     * Creates a bright transparent shape to cover a row/column to indicate it 
     * has been completed incorrectly.
     * @param index   which row/column is to be highlighted.
     * @param grid    grid of cells.
     * @param isRow   true if a row is to be highlighted, else a column.
     */
    private void highlightColumnRow(int index, GCell[][] grid, boolean isRow) {
      /** The space in the highlights array which will be checked. */
      int indexInArray = isRow ? index : index + type;
      /** If the row is not already highlighted. */
      if (highlights[indexInArray] == null) {
        /** Shape that will be used to highlight the row/column. */
        Shape shape = isRow ? grid[index][0] : grid[0][index];
        
        /** Adds the other cell in the row/column to the shape. */
        for (int i = 1; i < type; i++) {
          shape = Shape.union(shape, isRow ? grid[index][i] : grid[i][index]);
        }
        
        /** Keeps a record the new highlight shape. */
        highlights[indexInArray] = shape;
        setShapeProperties(shape);
        paneToAdd.getChildren().add(shape);
      }
    }

    
    /**
     * Removes a highlight shape from the pane. 
     * @param isRow     if true, a row is to be removed, else if false a 
     *                  column is removed. 
     * @param index     indicates which highlight shape is to be removed.
     */
    private void remove(int index) {
      /** 
       * Retrieves the shape to be removed. Index depends on whether a row or 
       * column is fetched.
       */
      Shape shapeToRemove = highlights[index];
            
      /** If there is a shape to remove, then remove. */
      if (shapeToRemove != null) {
        paneToAdd.getChildren().remove(shapeToRemove);
        highlights[index] = null;
      }     
    }
    
    /**
     * Method that will set the shape's colour and other properties. 
     * @param shape     the shape whose properties will be reset. 
     */
    private void setShapeProperties(Shape shape) {
      shape.setFill(Color.rgb(200, 0, 200, 0.4));
      shape.setMouseTransparent(true);
    }
    
    /** 
     * Clear all the row and column highlights.
     */
    private void hideHighlights(List<Cage> cages) {
      for (Cage cage : cages) {
        cage.resetFill();
      }

      for (int index = 0; index < highlights.length; index++) {
        remove(index);
      }
    }
  }
}
  