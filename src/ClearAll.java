
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * Clears the cells.
 * @author starp
 *
 */
public class ClearAll {
  /**
   * Clears a given grid if the confirmed by the player.
   * @param type    dimensions of the grid.
   * @param cages   of the grid.
   * @param grid    with the cells.
   */
  public static void clearAll(int type, GCell[][] grid, List<Cage> cages, 
                              MistakeChecker mistakeChecker) {
    Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure?");
    alert.setTitle("Confirm clearing");
    alert.showAndWait();
    if (alert.getResult() == ButtonType.OK) {
      for (int i = 0; i < type; i++) {
        for (int j = 0; j < type; j++) {
          grid[i][j].clear();
          mistakeChecker.checkMistakes(cages, grid);
        }
      }
    }
  }
}
