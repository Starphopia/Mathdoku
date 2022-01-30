import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.layout.Pane;

/**
 * Cage where the division operation will be applied to the numbers.
 * @author starp
 *
 */
class DivisionCage extends Cage {
  /** 
   * Calls the parent constructor.
   * @param cellArray     The cells that make up the cage.
   * @param target        the target number that should be made by dividing
   *                      the numbers in the cells.
   * @param paneToDraw    The pane where the division cage will be added.
   */
  public DivisionCage(GCell[] cellArray, Pane paneToDraw, int target) {
    super(cellArray, paneToDraw, target);
  }

  /**
   * Creates the label for the target and the operations. 
   * @param target        the label will take the form "target÷"
   * @param paneToDraw    where the label will be added to.
   */
  @Override
  protected void createTargetLabel(int target, Pane paneToDraw) {
    super.createTargetLabel("÷", target, paneToDraw);      
  }

  /** 
   * @return true if the cells can be arranged and divided to make the 
   *         target.
   */
  @Override
  protected boolean completedCorrectly() {
    /** Gets the numbers from the cells. */
    ArrayList<Integer> numbers = new ArrayList<Integer>();
    
    for (GCell cell : cells) {
      numbers.add(cell.getContent());
    }
    
    /** Firstly checks whether there is a zero error. */
    if (numbers.contains(0)) {
      return false;
    }
    
    /** 
     * Because the target must be an integer, the other numbers must be
     * factors of the largest number. 
     */
    Integer maxNumber = Collections.max(numbers);
    numbers.remove(maxNumber);
    int numberAfterDivision = maxNumber.intValue();
    for (Integer number : numbers) {
      if (maxNumber % number != 0) {
        return false;
      }
      
      /** Otherwise divide by that number. */
      numberAfterDivision /= number;
    }
    
    /** Returns whether the divided number is equal to the target. */
    return target == numberAfterDivision;    
  }
  
}
