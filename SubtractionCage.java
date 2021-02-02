import javafx.scene.layout.Pane;

/**
 * Cage which will perform the subtraction operation on the cells.
 */
class SubtractionCage extends Cage {
  
  /**
   * Will call the parent constructor.
   * @param cellArray     the cells can be subtracted to make the target.
   * @param paneToDraw    the pane where the cage will be draw in.
   * @param target        the target number 
   */
  public SubtractionCage(GCell[] cellArray, Pane paneToDraw, int target) {
    super(cellArray, paneToDraw, target);
  }

  /** 
   * Will be used to create the target label 
   * @param target        will be used in the label text: "target-"
   * @param paneToDraw    pane where the label will be added to.
   */
  @Override
  protected void createTargetLabel(int target, Pane paneToDraw) {
    super.createTargetLabel("-", target, paneToDraw);      
  }

  /** 
   * Returns whether the user has entered the numbers in correctly.
   */
  @Override
  protected boolean completedCorrectly() {
    /** Sum of the numbers. */
    int sum = 0;
    /** Max value where the other numbers will be subtracted from. */
    int max = 0;
    for (GCell cell : cells) {
      int currentNumber = cell.getContent();
      sum += currentNumber;
      max = max < currentNumber ? currentNumber : max;
    }
    
    return target == (2 * max) - sum;
  }    
}
