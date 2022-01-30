import javafx.scene.layout.Pane;

/**
 * Cage which will perform the addition operation on the cells.
 */
public class AdditionCage extends Cage {
  /**
   * Constructor that will call its parent's constructor.
   * @param cells         inside the cage.
   * @param paneToDraw    The pane where the cage will be drawn.
   * @param target        the number that must be reached by adding all the 
   *                      cells' numbers together.
   */
  public AdditionCage(GCell[] cells, Pane paneToDraw, int target) {
    super(cells, paneToDraw, target);
  }
  
  /**
   * Creates and positions the label with the + sign.
   * @param target        the number that will be written by using the 
   *                      target+ syntax.
   * @param paneToDraw    the pane where the label will be added to.
   */
  @Override
  protected void createTargetLabel(int target, Pane paneToDraw) {
    super.createTargetLabel("+", target, paneToDraw);
  }
  
  /** 
   * Returns true if the cells have been filled in correctly. 
   */
  @Override
  protected boolean completedCorrectly() {
    int sum = 0;
    /** Sums together the contents of the cells. */
    for (GCell cell : cells) {
      sum += cell.getContent();
    }
    
    return sum == target;
  }
}
