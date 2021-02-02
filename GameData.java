import java.util.ArrayList;
import java.util.List;

/** 
   * Contains a summary on the information. Acts as a record. 
   * @author starp
   *
   */
  public class GameData {
    /**
     * Stores the size of a side of the new game grid.
     */
    private int size = 0;
    /**
     * Stores which cells belong to which cage. 
     */
    private List<ArrayList<Integer>> cages = new ArrayList<ArrayList<Integer>>();
    /**
     * Stores the operator of each cage.  
     */
    private List<Operations> operators;
    /**
     * Stores the target of each cage. 
     */
    private List<Integer> targets;
    
    /**
     * Accessor for the number of columns/rows.
     * @return {@link #size} 
     */
    public int getSize() {
      return size;
    }
    
    /** 
     * Accessor for the array list of integer arrays holding which cells
     * are part of which cages. 
     * @return {@link #cages} 
     */
    public List<ArrayList<Integer>> getCages() {
      return cages;
    }
    
    /** 
     * Accessor for the operator of each cage.
     * @return {@link #operators}
     */
    public List<Operations> getOperators() {
      return operators;
    }
    
    /** 
     * Accessor for the target of each cage. 
     * @return {@link #targets}
     */
    public List<Integer> getTargets() {
      return targets;
    }
    
    /**
     * Mutator method for size.
     * @param size    the number of columns/rows.
     */
    public void setSize(int size) {
      this.size = size;
    }
    
    /**
     * Mutator method for the array cages.
     * @param cageInfo    the new cages that will be added to the grid.
     */
    public void setCages(List<ArrayList<Integer>> cageInfo) {
      this.cages = cageInfo;
    }   
    
    /**
     * Mutator method for the targets.
     * @param target    the targets for each cage.
     */
    public void setTargets(List<Integer> target) {
      this.targets = target;
    }
    
    /**
     * Mutator method for the operators.
     * @param operators     the operation performed by each cage.
     */
    public void setOperators(List<Operations> operators) {
      this.operators = operators;
    }
  }