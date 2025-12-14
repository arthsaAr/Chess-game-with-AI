package Chess;
import java.util.*;

/**
 * The {@code Bishop} piece.
 * <p>
 * Moves diagonally any number of squares until blocked by another piece
 * </p>
 * 
 * @author Group3
 * @version 1.0
 *
 */
public class Bishop extends Piece {

    /**
     * Makes a new {@code Bishop} with the color and position
     *
     * @param color    the color of the piece ("White" or "Black")
     * @param position the initial {@link Coordinate} of the Bishop on the board
     */
    public Bishop(String color, Coordinate position) {
        super(color, position);
    }

    /**
     * Gives list of legal moves for this Bishop on the board
     * <p>
     * {@inheritDoc} Moves diagonally in all four directions until its blocked
     * </p>
     *
     * @param board the current state of the chessboard
     * @return a {@link List} of {@link Coordinate} objects representing legal moves
     */
    @Override
    public List<Coordinate> getLegalMoves(Board board) {
        List<Coordinate> moves = new ArrayList<>();
        int[][] dirs = { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

        for (int[] d : dirs) {
            int r = position.getRow() + d[0];
            int c = position.getCol() + d[1];
            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Coordinate target = new Coordinate(r, c);
                Piece targetPiece = board.getPieceAt(r, c);

                if (targetPiece == null) 
                {
                    // 1. Square is vacant: Add move and continue in this direction
                    moves.add(target);
                } else 
                {
                    // 2. Square is occupied, check collision
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
     * Returns the symbol representing the Bishop piece ("B")
     *
     * @return "B" as the symbol for Bishop
     */
    @Override
    public String getSymbol() {
        return "B";
    }

    /**
     * Returns the value of the Bishop piece
     *
     * @return 3, the value of a Bishop
     */
    @Override
    public int getValue() {
        return 3; // Bishop is worth 3 points
    }

    /**
     * Creates a deep copy of this Bishop.
     *
     * @return a new {@link Bishop} object with the same color, position, and move
     *         status
     */
    @Override
    public Piece copy() {
        Bishop copy = new Bishop(this.color, new Coordinate(position.getRow(), position.getCol()));
        copy.setHasMoved(this.hasMoved);
        return copy;
    }

    //Is king Checked
    @Override
    public boolean isAttacking(Board board, Coordinate target) 
    {
        return getLegalMoves(board).contains(target);
    }
}