import java.util.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Generates a puzzle using a random number generator.
 * @author starp
 *
 */
public class PuzzleGenerator {
  /** 
   * Random number generator will determine how cells will be filled.
   */
  private Random random;
  /**
   * The seed that will be fed into the number generator.
   */
  private int seed;
    
    
  /**
   * Generates a random generator for the puzzle.
   */
  public PuzzleGenerator() {
    random = new Random();
  }

  /**
   * Generates a random generator using the seed given; will be used to 
   * create a puzzle.
   * @param seed        allows the same puzzle to be generated consistently 
   *                  from a seed given by the user.
   */
  public PuzzleGenerator(int seed) {
    random = new Random(seed);
    this.seed = seed;
  }
    
  /**
   * Creates a game data object representing a randomly generated puzzle.
   * @param sizeOfTheGrid       dimensions of the would be puzzle.
   * @param max                 maximum size of one cage.
   */
  public GameData createGameData(int sizeOfTheGrid, int max) {
    boolean valid = true;
    int[][] grid = new int[sizeOfTheGrid][sizeOfTheGrid];
        
    /** Stops looping when a valid seed is found. */
    do {
      valid = true;
      try {
        /** Generates a grid of numbers without column/row clashes. */
        GridNumberGenerator gridGen = new GridNumberGenerator(sizeOfTheGrid);
        grid = gridGen.getGridOfNumbers();
      }
      catch (Exception e) {
        /** If an error occurs, attempt again with a random seed. */
        if (seed != 0) {
          seed *= 7;
          random = new Random(seed);
        }
        else {
          random = new Random();
        }
          valid = false;
      }
    }  
    while (valid == false);
        
    CageGenerator cageGen = new CageGenerator(grid, max);
    /** Creates the game data to store information on the new game. */
    GameData gameData = new GameData();
    gameData.setSize(grid.length);
    gameData.setCages(cageGen.getCageCells());
    gameData.setOperators(cageGen.getOperators());
    gameData.setTargets(cageGen.getTargets());      
        
    return gameData;
  }
    
    
  /**
   * A class containing code needed to generate a grid of random numbers. 
   * @author starp
   *
   */
  private class GridNumberGenerator {
    /**
     * Represents the grid of numbers.
     */
    private int[][] grid;
    /**
     * Numbers that should be filled in each column and row. 
     */
    private ArrayList<Integer> numbersToFill;
    /**
     * Represents the numbers that have yet to be filled in each column.
     */
    private ArrayList<ArrayList<Integer>> colNumbers;
    /** 
     * Represents the numebrs that have yet to be filled in each row.
     */
    private ArrayList<ArrayList<Integer>> rowNumbers;
    
    /**
     * Creates a two dimensional array ready to contain numbers. Will 
     * represent what the grid should be filled with.
     * @param sizeOfTheGrid     how many columns/rows.
     */
    public GridNumberGenerator(int sizeOfTheGrid) {
      grid = new int[sizeOfTheGrid][sizeOfTheGrid];
      
      /** Initialises the numbers that should be filled in each column/row. */
      numbersToFill = new ArrayList<Integer>();
      for (int number = 1; number <= sizeOfTheGrid; number++) {
          numbersToFill.add(number);
      }
      
      colNumbers = new ArrayList<ArrayList<Integer>>();
      rowNumbers = new ArrayList<ArrayList<Integer>>();
      /** Initialises the two array lists. */
      for (int i = 0; i < sizeOfTheGrid; i++) {
          colNumbers.add(new ArrayList<Integer>(numbersToFill));
          rowNumbers.add(new ArrayList<Integer>(numbersToFill));
      }
      
      fillGrid();
    }
    
    /** 
     * Returns the grid. 
     */
    public int[][] getGridOfNumbers() {
      return grid;
    }
            
    
    private void swapColumn(int column, int row) {
      int columnToSwap = column;
      
      do {
          columnToSwap--;
      } 
      while ((compatibleNumbersLeft(columnToSwap, row).size() == 0
              || !colNumbers.get(column).contains(grid[row][columnToSwap])));
      /** Swapped cell is assigned a new value. */
      fillCell(row, column, clearCell(row, columnToSwap));
      fillCell(row, columnToSwap);
    }
    
    /** 
     * Fills the grid with numbers.
     */
    private void fillGrid() {
      /** Fills each row. */
      for (int row = 0; row < grid.length; row++) {
        /** Loops through each column. */
        for (int column = 0; column < grid.length; column++) {
          /** If there's no compatible columns left. */
          if (compatibleNumbersLeft(column, row).size() == 0) {
            swapColumn(column, row);
          }
          else {
            /** Randomly choose a compatible value for the current column. */
            fillCell(row, column);
          }
        }
      }
    }
    
    /**
     * Fills in the cell with a specified number. 
     * @param row           which row the cell is part of.
     * @param column        which column the cell is part of.
     * @param number        which number the cell is part of.
     */
    private void fillCell(int row, int column, Integer number) {
      /** If there's a number within the cell previously. */
      if (grid[row][column] != 0) {
        clearCell(row, column);
      }
      
      /** Fills in the cell with the new number. */
      grid[row][column] = number;
      colNumbers.get(column).indexOf(number);
      colNumbers.get(column).remove(colNumbers.get(column).indexOf(number));
      rowNumbers.get(row).remove(rowNumbers.get(row).indexOf(number));
    }
    
    /**
     * Finds a number suitable for the cell.
     * @param row           which row the cell is part of.
     * @param column        which column the cell is part of.
     */
    private void fillCell(int row, int column) {
      /** If there's a number within the cell previously. */
      if (grid[row][column] != 0) {
        clearCell(row, column);
      }
      ArrayList<Integer> options = compatibleNumbersLeft(column, row);
      
      /** If there are numbers available. */
      if (options.size() != 0) {
        fillCell(row, column, options.get(random.nextInt(options.size())));
      }
    }
    
    /**
     * Clears a cell.
     * @return the value that the cell previously contained. 
     */
    private int clearCell(int row, int column) {
      int previous = grid[row][column];
      colNumbers.get(column).add(previous);
      rowNumbers.get(row).add(previous);
      grid[row][column] = 0;
      return previous;
    }
    
    
    /** 
     * Returns numbers that can fill a particular cell.
     * @param column        which column.
     * @param row           which row.
     * @return all compatible numbers.
     */
    private ArrayList<Integer> compatibleNumbersLeft(int column, int row) {
      ArrayList<Integer> compatibleNumbers = new ArrayList<Integer>(rowNumbers.get(row));
      compatibleNumbers.retainAll(colNumbers.get(column));
      return compatibleNumbers;
    }
  }
    
  private class CageGenerator {
    /**
     * Holds the numbers in the grid.
     */
    private int[][] grid;
    /**
     * ArrayList that stores the cells that have been visited.
     */
    private HashSet<Coordinate> visited;
    /**
     * Cells that will be contained in each cage.
     */
    private List<ArrayList<Integer>> cageCells;
    /**
     * Stores the operator of each cage. 
     */
    private List<Operations> operators;
    /**
     * Stores the target to each cage.
     */
    private List<Integer> targets;
      
    /** 
     * Initialises the grid.
     * @param grid      containing the numbers stored inside the grid.
     */
    public CageGenerator(int[][] grid, int max) {
      this.grid = grid;
      visited = new HashSet<Coordinate>();
      cageCells = new ArrayList<ArrayList<Integer>>();
      operators = new ArrayList<Operations>();
      targets = new ArrayList<Integer>();
      
      generateCages(max);
      typeCages();
    }
    
    /**
     * Accessor for the array list of cages with cells.
     * @return {@link #cageCells}
     */
    public List<ArrayList<Integer>> getCageCells() {
      return cageCells;
    }
    
    /**
     * Accessor for the array list of operators in each cage.
     * @return {@link #operators} 
     */
    public List<Operations> getOperators() {
      return operators;
    }
    
    /**
     * Access for the array list of targets in each cage. 
     * @return {@link #operators}
     */
    public List<Integer> getTargets() {
      return targets;
    }
    
    /**
     * Generates the information for the cages.
     * @param max       maximum number of cells in a cage.
     */
    public void generateCages(int max) {
      /** Goes through each cell. */
      for (int i = 0; i < grid.length; i++) {
        for (int j = 0; j < grid.length; j++) {
          /** If the cell is unvisited, draw a cage including it. */
          if (!visited.contains(new Coordinate(i, j))) {
            ArrayList<Integer> cage = new ArrayList<Integer>();
            drawCage(i, j, 1, cage, max);
            cageCells.add(cage);
          }
        }
      }
    }
    
    /**
     * Creates a single cage based around a single cell.
     * @param i                 which row the cell is in. 
     * @param j                 which column the cell is in.
     * @param numberOfCells     number of cells already in the cage.
     * 
     */
    private boolean drawCage(int i, int j, int numberOfCells, 
                           ArrayList<Integer> cellsInCage,
                           int max) {
      if (numberOfCells == 1) {
          visited.add(new Coordinate(i, j));
          cellsInCage.add(i * grid.length + j + 1);
      }
      
      /** If the maximum number of cells included. */
      if (numberOfCells == max) {
          return false;
      }
      else {
        /** Stores which surrounding cells are available. */
        ArrayList<Coordinate> cells = new ArrayList<Coordinate>();
        
        /** Adds all unvisited neighbour cells. */
        if (i > 0 ? !visited.contains(new Coordinate(i - 1, j)) : false) {
          cells.add(new Coordinate(i - 1, j));
        }
        if (i < grid.length - 1 ? !visited.contains(new Coordinate(i + 1, j))
                                : false) {
          cells.add(new Coordinate (i + 1, j));
        }
        if (j > 0 ? !visited.contains(new Coordinate (i, j - 1)): false) {
          cells.add(new Coordinate(i, j - 1));
        }
        if (j < grid.length - 1 ? !visited.contains(new Coordinate(i, j + 1))
                                : false) {
          cells.add(new Coordinate(i, j + 1));
        }
        
        /** If there are no available neighbour cells. */
        if (cells.size() == 0) {
          return false;
        }
        
        /** Random number generator selects the next cell. */
        Coordinate chosen = cells.get(random.nextInt(cells.size()));
        cellsInCage.add(chosen.getX() * grid.length + chosen.getY() + 1);
        visited.add(chosen);
        
        /** Determines if there's a next cell in the cage. */
        if (random.nextDouble() < 0.4) {
          drawCage(chosen.getX(), chosen.getY(), numberOfCells + 1, 
                   cellsInCage, max); 
        }
        
        return true;
      }
    }
    
    /**
     * Determine the operator and target to each cage. 
     */
    private void typeCages() {
      for (ArrayList<Integer> cage : cageCells) {             
        /** Stores all the would be values in the cage. */
        ArrayList<Integer> values = new ArrayList<Integer>();
        
        /** Finds the value in each cell. */
        for (int cellIndex : cage) {
          values.add(grid[Math.floorDiv(cellIndex - 1, grid.length)]
                         [(cellIndex - 1) % grid.length]); 
        }
        
        /** Prioritise the division cage since valid ones are rare. */
        if (canDivide(values)) {
          operators.add(Operations.DIVIDE);
          targets.add(divide(values));
        }
        else {
          /** Randomly choose the operator. */
          List<Operations> possibleSigns = new ArrayList<Operations>();
          possibleSigns.add(Operations.TIMES);
          possibleSigns.add(Operations.ADD);
          
          /** If we can subtract the numbers, add it to the possible signs. */
          if (canSubtract(values)) {
            possibleSigns.add(Operations.MINUS);
          }
          
          switch (possibleSigns.get(random.nextInt(possibleSigns.size()))) {
            case TIMES:
              operators.add(Operations.TIMES);
              targets.add(product(values));
              break;
            case ADD: 
              operators.add(Operations.ADD);
              targets.add(sum(values));
              break;
            case MINUS:
              operators.add(Operations.MINUS);
              targets.add(subtract(values));
              break;
            default:
              break;                          
          }
        }
      }
    } 
    
    /**
     * Returns whether a cage can be given a subtraction operator.
     * @param values    the values stores in the cage.      
     * @return whether the maximum is bigger or equal to the sum of the 
     *         rest of the numbers.
     */
    private boolean canDivide(ArrayList<Integer> values) {
      int max = Collections.max(values);    
      /** Finds if the rest of the numbers are factors of the maximum. */
      for (Integer value : values) {
        if (max % value != 0) {
          return false;
        }
      }
      
      return true;
    }
    
    /**
     * Returns whether a cage can be given a subtraction operator.
     * @param values        within the cage.
     * @return whether the maximum is bigger than the sum of the rest of 
     *         values.
     */
    private boolean canSubtract(ArrayList<Integer> values) {
      return subtract(values) >= 0;
    }
    
    /**
     * Performs the subtract operator on the values of the cage.
     * @param values        within the cage.
     * @return the maximum subtracted by the sum of the rest of the values.
     */
    private Integer subtract(ArrayList<Integer> values) {
      return 2 * Collections.max(values) 
             - values.stream().mapToInt(Integer::intValue).sum();
    }
    
    /**
     * Performs the division operator on the values of the cage.
     * @param values        within the cage.
     * @return the maximum divided by the product of the rest of the 
     *         values.
     */
    private Integer divide(ArrayList<Integer> values) {
      /** The arraylist without the maximum. */
      ArrayList<Integer> rest = new ArrayList<Integer>(values);
      rest.remove(Collections.max(rest));
      int product = 1;
      
      for (int number : rest) {
        product *= number;
      }
      
      return Collections.max(values) / product;           
    }
    
    /**
     * Computes the target of a multiplication cage.
     * @param values        within the cage.
     * @return product of all the values.
     */
    private Integer product(ArrayList<Integer> values) {
      int product = 1;
      
      for (Integer value : values) {
        product *= value;
      }
      
      return product;
    }
    
    /**
     * Returns the target of an addition cage.
     * @param values        within the cage.
     * @return The sum of all of the values within the arraylist.
     */
    private Integer sum(ArrayList<Integer> values) {
      return values.stream().mapToInt(Integer::intValue).sum();
    }
  }
}
