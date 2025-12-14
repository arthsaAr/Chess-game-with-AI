package Chess;
import java.util.*;

/**
 * Represents a Rook chess piece. The Rook can move any number of squares
 * horizontally or vertically
 * until it is blocked by another piece or the edge of the board.
 *
 *
 * @author Group3
 * @version 1.0
 */
public class Rook extends Piece {

    /**
     * Constructs a Rook with the specified color and position
     *
     * @param color    the color of the Rook ("white" or "black")
     * @param position the starting position of the Rook on the board
     */
    public Rook(String color, Coordinate position) {
        super(color, position);
    }

    /**
     * Computes all legal moves for the Rook based on its current position and the
     * state of the board
     *
     * @param board the current game board
     * @return a list of {@link Coordinate} objects representing all legal moves for
     *         this Rook
     */
    @Override
    public List<Coordinate> getLegalMoves(Board board) {
        List<Coordinate> moves = new ArrayList<>();
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        for (int[] d : dirs) {
            int r = position.getRow() + d[0];
            int c = position.getCol() + d[1];
            // Loop continues until we hit the edge of the board
            while (r >= 0 && r < 8 && c >= 0 && c < 8) 
            {
                Coordinate target = new Coordinate(r, c);
                Piece targetPiece = board.getPieceAt(r, c);

                if (targetPiece == null) {
                    // 1. Square is vacant: Add move and continue in this direction
                    moves.add(target);
                } else {
                    // 2. Square is occupied
                    if (this.isSameColor(targetPiece)) {
                        // Friendly piece: Cannot move here or past it.
                        break; 
                    } else { // Implicitly, it's an enemy piece
                        // Enemy piece: Legal capture, but cannot move past it.
                        moves.add(target);
                        break; 
                    }
                }

                // Move to the next square 
                r += d[0];
                c += d[1];
                
            }
        }
        return moves;
    }

    /**
     * Returns the symbol representing this piece on the board
     *
     * @return the string "R" for Rook
     */
    @Override
    public String getSymbol() {
        return "R";
    }

    /**
     * Returns the standard point value of this piece in chess evaluation
     *
     * @return the integer 5
     */
    @Override
    public int getValue() {
        return 5;
    }

    /**
     * Creates a deep copy of this Rook, preserving its color, position, and
     * movement state
     *
     * @return a new {@code Rook} instance identical to this one
     */
    @Override
    public Piece copy() {
        Rook copy = new Rook(this.color, new Coordinate(position.getRow(), position.getCol()));
        copy.setHasMoved(this.hasMoved);
        return copy;
    }

    //Is king Checked
    @Override
    public boolean isAttacking(Board board, Coordinate target) 
    {
        return getLegalMoves(board).contains(target);
    }

    public void setPosition(Coordinate newC){
        this.position = newC;
    }
}