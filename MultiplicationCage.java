import javafx.scene.layout.Pane;

/** 
 * Cage which will perform the multiplication operator on the cells.
 */
class MultiplicationCage extends Cage {
  /** 
   * Will call the parent constructor.
   * @param cellArray     cells which the cage will contain.
   * @param paneToDraw    where the cage will be drawn.
   * @param target        the target number that the cells should multiply to.
   */
  public MultiplicationCage(GCell[] cellArray, Pane paneToDraw, int target) {
    super(cellArray, paneToDraw, target);      
  }
  
  /**
   * Creates the label.
   * @param target        the label will be "target×"
   * @param paneToDraw    the pane where the cage will be added.
   */
  @Override
  protected void createTargetLabel(int target, Pane paneToDraw) {
    super.createTargetLabel("×", target, paneToDraw);      
  }

  /**
   * @true if the cells multiply to the target.
   */
  @Override
  protected boolean completedCorrectly() {
    int multiplied = 1;
    
    /** Calculates the product of all of the cells. */
    for (GCell cell : cells) {
      multiplied *= cell.getContent(); 
    }
    return target == multiplied;
  }    
}
