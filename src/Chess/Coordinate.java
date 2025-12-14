package Chess;

/**
 * Represents a row–column coordinate on the chessboard.
 * Immutable helper class used in {@link Move} and {@link Piece}.
 *
 * @author Group3
 * @version 1.0.0
 */
public class Coordinate {

    /** Row index on the board (0–7). */
    private final int row;

    /** Column index on the board (0–7). */
    private final int col;

    /**
     * Constructs a coordinate with given row and columm
     *
     * @param row row index (0–7)
     * @param col column index (0–7)
     */
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /** @return the row index */
    public int getRow() {
        return row;
    }

    /** @return the column index */
    public int getCol() {
        return col;
    }

    /**
     * Compares this coordinate with another object for equality
     * 
     * @param o the object to compare with this coordinate
     * @return {@code true} if the object is a Coordinate with the same row and
     *         column values, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Coordinate))
            return false;
        Coordinate c = (Coordinate) o;
        return row == c.row && col == c.col;
    }

    /**
     * Generates a hash code for this coordinate
     * 
     * @return the hash code value for this coordinate
     */
    @Override
    public int hashCode() {
        return row * 31 + col;
    }

    /**
     * Returns a string representation of this coordinate
     * 
     * @return a string representation of the coordinate in the format "(row,col)"
     */
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }

    /**
     * Creates a deep copy of this coordinate
     * 
     * @return a new {@link Coordinate} with identical row and column values
     */
    public Coordinate copy() {
        return new Coordinate(this.row, this.col);
    }
}
