package Chess;

/**
 * Represents a single square on the chessboard.
 * Each square can hold a {@link Piece} and has a coordinate (row, column)
 * 
 * @author Group3
 * @version 1.0
 */
public class Square {

    private int row;
    private int col;
    private Piece piece;

    /**
     * Creates a new square.
     *
     * @param row row index (0–7)
     * @param col column index (0–7)
     */
    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        this.piece = null;
    }

    /** @return the piece on this square, or {@code null} if empty */
    public Piece getPiece() {
        return piece;
    }

    /** @param piece piece to place on this square */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /** @return this square's row index */
    public int getRow() {
        return row;
    }

    /** @return this square's column index */
    public int getCol() {
        return col;
    }
}
