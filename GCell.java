import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Class for each of the cells which will be drawn using rectangles.
 * @author starp
 *
 */
public class GCell extends Rectangle {
  /** 
   * Which row it is in the grid.
   */
  private final int ROW;
  /**
   * Which column it is in grid.
   */
  private final int COLUMN;
  /**
   * Coordinates of the vertices.
   */
  private Coordinate[] vertices;
  /**
   * The size of the numbers in each cell.
   */
  private FontSize fontSize;
  /**
   * The number that it contains. 0 means it contains nothing. 
   */
  private int content = 0;
   
  /**
  * Draws the rectangle.
  * @param x  coordinate of where the cell will be positioned.
  * @param y  coordinate of the where the cell will be positioned.
  * @param cellWidth  width of the cell.
  * @param gridPane   where the cells will be drawn on.   
  * @param selected   the cell currently selected.
  */
  public GCell(int x, int y, int cellWidth, Pane gridPane, GCell selected) {
    /** Generates the square. */
    super(x * cellWidth, y * cellWidth, cellWidth, cellWidth);
    /** Formats the rectangle. */
    clear();
    unselect();
    fontSize = FontSize.MEDIUM;
    
    /** Stores the location of the cell inside the member variables. */
    ROW = x;
    COLUMN = y;
    
    /** Computes the coordinates. */
    vertices = new Coordinate[4];
    vertices[0] = new Coordinate(x * cellWidth, y * cellWidth);
    vertices[1] = new Coordinate(x * cellWidth, (y + 1) * cellWidth);
    vertices[2] = new Coordinate((x + 1) * cellWidth, y * cellWidth);
    vertices[3] = new Coordinate((x + 1) * cellWidth, 
                                 (y + 1) * cellWidth); 
  } 
  
  /** 
   * Accessor for which row the cell is in.
   * @return {@link GCell#ROW}
   */
  public int getRow() {
    return ROW;
  }
    
  /**
   * Accessor for which column the cell is in.
   * @return {link Cell#COLUMN}
   */
  public int getColumn() {
    return COLUMN;
  }
    
  /**
   * This will return the coordinates of the cell's vertices.
   * @return
   */
  public Coordinate[] getCoordinate() {
    return vertices;
  }         
    
  /**
   * Returns the number contained in the cell. 
   * @return the number that is written to the cell.
   */
  public int getContent() {
    return content;
  }
  
  /**
   * Shows that the cell is unselected; reverts the border colour to
   * black. 
   */
  public void unselect() {
    setStrokeType(StrokeType.INSIDE);
    this.setStrokeWidth(1);
    setStroke(Color.BLACK);
  }
  
  /** 
   * Allows a number to be keyed in and the content to be updated.
   * @param     the size of the grid.
   */
  public void enterNumber(int input, int type) {
    /** First checks that the input is valid. */
    if (input <= type && input > 0) {
      /** Stores the path of the number image. */
      String imagePath = "";
      switch (fontSize) {
        case MEDIUM:
          imagePath = input == 1 ? "one.png" 
                                  : input == 2 ? "two.png"
                                  : input == 3 ? "three.png"
                                  : input == 4 ? "four.png"
                                  : input == 5 ? "five.png"
                                  : input == 6 ? "six.png"
                                  : input == 7 ? "seven.png"
                                  : input == 8 ? "eight.png"
                                  : "nine.png";
          break;
        case BIG:
          imagePath = input == 1? "oneBig.png"
                                : input == 2 ? "twoBig.png"
                                : input == 3 ? "threeBig.png"
                                : input == 4 ? "fourBig.png"
                                : input == 5 ? "fiveBig.png"
                                : input == 6 ? "sixBig.png"
                                : input == 7 ? "sevenBig.png"
                                : input == 8 ? "eightBig.png"
                                : "nineBig.png";
          break;
        case SMALL:
          imagePath = input == 1? "oneSmall.png"
                                : input == 2 ? "twoSmall.png"
                                : input == 3 ? "threeSmall.png"
                                : input == 4 ? "fourSmall.png"
                                : input == 5 ? "fiveSmall.png"
                                : input == 6 ? "sixSmall.png"
                                : input == 7 ? "sevenSmall.png"
                                : input == 8 ? "eightSmall.png"
                                : "nineSmall.png";
          break;
          
      }
      
      /** Finds the image containing the number. */
      Image image = new Image(imagePath);
      setFill(new ImagePattern(image));   
      content = input;
    }
  }
    
  /**
   * Clears the cell of its content (by clearing the image of the number).
   */
  public void clear() {
    setFill(Color.WHITE);
    content = 0;
  }     
    
  /** 
   * When the cell is selected. Will be accessed when clicked; this will
   * make the border bolder and a different colour.
   * The cell currently selected.
   */
  public void select(GCell selected) {        
    /** Ensures the last cell to be selected is unselected. */
    if (selected != null) { 
      selected.unselect();
    }
    selected = this;
    setStrokeWidth(10);
    setStroke(Color.PLUM);
  }
  
  /**
   * Increases or decreases the number size in the cells.
   * @param isIncreasing    true if we are increasing the font size
   * @param type            number of columns/rows in the grid.
   */
  public void changeFontSize(boolean isIncreasing, int type) {
    /** Checks whether we are increasing or decreasing the font size. */
    if (isIncreasing) {
      /** Changes the font size. */
      if (fontSize == FontSize.SMALL) {
        fontSize = FontSize.MEDIUM;
      }
      else if (fontSize == FontSize.MEDIUM) {
        fontSize = FontSize.BIG;
      }
    }
    else {
      /** Changes the font size property. */
      if (fontSize == FontSize.BIG) {
        fontSize = FontSize.MEDIUM;
      }
      else if (fontSize == FontSize.MEDIUM) {
        fontSize = FontSize.SMALL;
      }
    }
    
    /** Updates the image. */
    this.enterNumber(content, type);
  }
}
