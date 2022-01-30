
import java.util.Comparator;

/** 
 * Container class that will be used for accessing and storing coordinates.
 * @author starp
 *
 */
public class Coordinate {
    /**
     * Stores the x and y coordinates 
     */
    private Integer x, y;
    
    /**
     * Constructor that will be called when instantiated
     * @param x  the x coordinate that it will store.
     * @param y  the y coordinate that it will store.
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;        
    }
    
    /**
     * Returns the x coordinate. 
     * @return {@link #x}
     */
    public int getX() {
        return x;
    }
    
    /**
     * Returns the y coordinate. 
     * @return {@link #y}
     */
    public int getY() {
        return y;
    }
    
    /**
     * Checks whether another set of coordinates are equal to this one.
     * @param other  the other coordinate to be compared to.
     */
    public boolean equals(Object other) {
        /** Checks that other isn't a null object. */
        if (other != null && (other instanceof Coordinate)) {
          return this.x == ((Coordinate)other).x 
                 && this.y == ((Coordinate)other).y;
        }
        
        return false;
    }
    
    /**
     * Overloads this and can be used statically.
     * @param coord1  to be compared to coordinate 2
     * @param coord1  to be compared to coordinate 1
     */
    public static boolean equals(Coordinate coord1, Coordinate coord2) {
        return coord1.x == coord2.x && coord1.y == coord2.y;
    }
    
    public int hashCode(){
        return x.hashCode() ^ y.hashCode();
    }

    /** 
     * Comparator for sorting a list of coordinates by their x values. 
     */
    public static class XComparator implements Comparator<Coordinate> {
        /**
         * Used to return whether an x value is higher or lower than another. 
         * @param coord1  will be compared to coord2.
         * @param coord2  will be compared to coord1.
         * @return 0 if their x values are equal, a positive number if the x
         *         value of coord1 is greater than coord2's, and a negative 
         *         number if otherwise.
         */
        @Override
        public int compare(Coordinate coord1, Coordinate coord2) {
          if (coord1.getX() == coord2.getX()) return 0;
          else return coord1.getX() - coord2.getX();       
        }        
    }
    
    /** 
     * Comparator for sorting a list of coordinates by their y values.
     */
    public static class YComparator implements Comparator<Coordinate> {
        /**
         * Used to return whether a y value is higher or lower than another.
         * @param coord1  will be compared to coord2.
         * @param coord2  will be compared to coord1.
         * @return 0 if their y values are equal, a positive number if the y 
         * value of coord1 is greater than coord2's, and a negative number 
         * if otherwise.
         */
        @Override
        public int compare(Coordinate coord1, Coordinate coord2) {
            if (coord1.getY() == coord2.getY()) return 0;
            else return coord1.getY() - coord2.getY();
        }

        
    }

}
