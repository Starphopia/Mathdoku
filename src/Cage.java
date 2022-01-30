
import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

/**
 * Class for a cage. 
 */
public class Cage {   
  /** 
   * The cells contained within the cage. 
   */
  protected GCell[] cells;
  /**
   * The target number that must be reached. 
   */
  protected int target;
  /**
   * The shape of the cage.
   */
  protected Shape cage;
  /** 
   * The label of the cage. 
   */
  protected Label operatorLabel;
  /**
   * The maximum size of the label.
   */
  protected final int MAXFONTSIZE = 20;
  /**
   * The minimum size of the label.
   */
  protected final int MINFONTSIZE = 12;
  
  /**
   * Takes in an array of cells that will be within the cage and adds 
   * them to the local variable. Then it draws it out the lines on the 
   * pane. 
   * @param cellArray   contains cells that are encaged.
   * @param paneToDraw  the pane where the rectangle will be added to.
   */
  public Cage(GCell[] cellArray, Pane paneToDraw, int target) {
    ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
    cells = cellArray;
    this.target = target;
    
    /** Loops through the cell array and find the cage's vertices. */
    for (GCell cell : cellArray) {
      /** Cage vertices will be made up of cell vertices. */
      for (Coordinate cellCoordinate: cell.getCoordinate()) {
        /** Cage vertices will be shared by 0 or 3 cells. */
        if (coordinates.contains(cellCoordinate)) {
            /** Remove vertices that is found an even no. time. */
            coordinates.remove(cellCoordinate);
        }
        else {
            coordinates.add(cellCoordinate);
        }
      }
    }
      
    drawCage(paneToDraw);
    createTargetLabel(target, paneToDraw);
  }
      
  /**
   * Gets the shape of the cage.
   * @return {@link #cage}
   */
  public Shape getCage() {
    return cage;
  }
  
  /**
   * Increases or decreases the label text size.
   */
  public void changeLabel(boolean isIncreasing) {
    int sizeChange = isIncreasing ? 4 : -4;
    if ((operatorLabel.getFont().getSize() < MAXFONTSIZE  && isIncreasing)
        || (operatorLabel.getFont().getSize() > MINFONTSIZE && !isIncreasing)) {
      operatorLabel.setFont(new Font(operatorLabel.getFont().getSize() 
                                     + sizeChange));
    }
  }
  
  /**
   * Draws the grid by combining together shapes, and adding a border.
   * @param drawPane     the pane that the lines will be added to.
   */
  protected void drawCage(Pane drawPane) {
    cage = cells[0];
    /** Adds the cells of the cage to the composite shape. */
    for (GCell gCell : cells) {
      cage = Shape.union(cage, gCell);
    }
    cage.setFill(Color.TRANSPARENT);
    cage.setStroke(Color.BLACK);
    /** So that the cells underneath the cage can detect clicks. */
    cage.setMouseTransparent(true);
    cage.setStrokeWidth(6);
    drawPane.getChildren().add(cage);      
  }
  
  /**
   * Changes the border of the cage to indicate that it has been completed
   * incorrectly.
   * @param showMistakes    whether the user has chosen to indicate mistakes.
   */
  protected void highlightWrong(boolean showMistakes) {
    if (showMistakes) {
      cage.setFill(Color.rgb(200, 0, 0, 0.3));
    }
  }
  
  /**
   * Resets the border to normal.
   */
  protected void resetFill() {
    cage.setFill(Color.rgb(0, 2, 0, 0.1));
  }
  
  /** 
   * Adds the operation label. Will be called by the child classes'
   * createTargetLabel(String, Pane) method.
   * @param operation     the operation used to reach the target.
   * @param target        the target number to reach.
   * @param paneToDraw    where the label will be added to.
   */
  protected void createTargetLabel(String operation, int target, 
                                   Pane paneToDraw) {
    String labelText = Integer.toString(target);
    
    /** Adds the appropriate target and operator to the label. */
    labelText = Integer.toString(target) + operation;
    
    /** Creates the label for the operator. */
    operatorLabel = new Label(labelText);
    paneToDraw.getChildren().add(operatorLabel);
    operatorLabel.setMouseTransparent(true);
    
    /** Positions the label at one of the cell's corner. */
    Coordinate location = cells[0].getCoordinate()[0];
    operatorLabel.setTranslateX(location.getX() + 5);
    operatorLabel.setTranslateY(location.getY() + 5);
    operatorLabel.setFont(new Font(16));
  }      
  
  /** 
   * Will be called by outside classes.
   */
  protected boolean completedCorrectly(boolean showMistake) {
    /** Checks if the cells are full. */
    for (GCell gCell : cells) {
      if (gCell.getContent() == 0) {
        return false;
      }
    }
    
    if (completedCorrectly()) {
      resetFill();
      return true;
    }
    else {
      highlightWrong(showMistake);
      return false;
    }
  }
  
  /** 
   * @return whether the cells has been completed correctly to reach the 
   *         target.
   */
  protected boolean completedCorrectly() {
	  return cells[0].getContent() == target;
  }
  
  /**
   * Creates the target label and positions according.
   * @param target        the target number that must be reached.
   * @param paneToDraw    the pane that the label will be added to.
   */
  protected void createTargetLabel(int target, Pane paneToDraw) {
	  createTargetLabel("", target, paneToDraw);
  } 
}
