package Chess;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Queen chess piece. The Queen combines the movement abilities of
 * both the Rook and Bishop,
 * allowing it to move any number of squares vertically, horizontally, or
 * diagonally until blocked by
 * another piece or the edge of the board
 *
 * @author Group3
 * @version 1.0
 */

/**
 * Represents a Queen chess piece. The Queen combines the movement abilities of
 * both the Rook and Bishop,
 * allowing it to move any number of squares vertically, horizontally, or
 * diagonally until blocked by
 * another piece or the edge of the board
 *
 * @author Group3
 * @version 1.0
 */

public class Queen extends Piece {

    /**
     * Constructs a Queen with the specified color and initial position
     *
     * @param color    the color of the Queen ("white" or "black")
     * @param position the starting position of the Queen on the board
     */
    public Queen(String color, Coordinate position) {
        super(color, position);
    }

    /**
     * Computes all legal moves for the Queen based on its current position and the
     * state of the board
     *
     * @param board the current game board
     * @return a list of {@link Coordinate} objects representing all legal moves for
     *         this Queen
     */
    @Override
    public List<Coordinate> getLegalMoves(Board board) {
        List<Coordinate> moves = new ArrayList<>();
        // Queen moves like rook + bishop
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, // Rook
                { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } }; // Bishop

        for (int[] d : dirs) {
            int r = position.getRow() + d[0];
            int c = position.getCol() + d[1];
            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Coordinate target = new Coordinate(r, c);
                Piece targetPiece = board.getPieceAt(r, c);

                if (targetPiece == null) {
                    // 1. Square is vacant: Add move and continue in this direction
                    moves.add(target);
                } else {
                    // 2. Square is occupied
                    // check collision
                    if (this.isSameColor(targetPiece)) {
                        // Friendly piece: Cannot move here or past it.
                        break;
                    } else {
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
     * Returns the symbol used to represent this piece on the board
     *
     * @return the string "Q" for Queen
     */
    @Override
    public String getSymbol() {
        return "Q";
    }

    /**
     * Returns the point value of this piece in standard chess evaluation
     *
     * @return the integer 9
     */
    @Override
    public int getValue() {
        return 9;
    }

    /**
     * Creates a deep copy of this Queen, preserving its color, position, and
     * movement state
     *
     * @return a new {@code Queen} instance identical to this one
     */
    @Override
    public Piece copy() {
        Queen copy = new Queen(this.color, new Coordinate(position.getRow(), position.getCol()));
        copy.setHasMoved(this.hasMoved);
        return copy;
    }

    // Is king Checked

    /**
     * Determines whether this King is attacking a specific square
     *
     * @param board  the current {@link Board} state; must not be {@code null}
     * @param target the {@link Coordinate} to check
     * @return {@code true} if the King can move to the target square (i.e., attacks
     *         it), {@code false} otherwise
     */
    @Override
    public boolean isAttacking(Board board, Coordinate target) {
        return getLegalMoves(board).contains(target);
    }
}