package Chess;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Knight piece in chess.
 * <p>
 * The Knight is a minor piece that moves in an L-shape pattern: two squares in
 * one
 * direction (horizontal or vertical) and one square perpendicular to that
 * direction.
 * The Knight is unique among chess pieces because it can "jump over" other
 * pieces,
 * making it the only piece whose movement is not blocked by intervening pieces.
 *
 * @author Group3
 * @version 1.0
 */

public class Knight extends Piece {

    /**
     * Constructs a new {@code Knight} with the specified color and position
     *
     * @param color    the color of the Knight, either "White" or "Black"; must not
     *                 be null
     * @param position the starting position of the Knight on the board; must not be
     *                 null
     */
    public Knight(String color, Coordinate position) {
        super(color, position);
    }

    /**
     * Returns a list of legal move coordinates for the Knight
     * <p>
     * The Knight moves in an L-shape: two squares in one direction (horizontal or
     * vertical) and then one square perpendicular to that. This creates up to eight
     * possible destination squares. Unlike other pieces, the Knight can jump over
     * intervening pieces, so this method only needs to check if the destination
     * square is within the board boundaries
     * </p>
     *
     * @param board the current chess board state; must not be null
     * @return a list of coordinates representing possible Knight moves; never null
     *         but may be empty
     */
    @Override
    public List<Coordinate> getLegalMoves(Board board) {
        List<Coordinate> moves = new ArrayList<>();
        int[][] jumps = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 }, { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 } };

        for (int[] j : jumps) {
            int r = position.getRow() + j[0];
            int c = position.getCol() + j[1];
            if (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Coordinate target = new Coordinate(r, c);
                Piece targetPiece = board.getPieceAt(r, c);

                // 2. Check for Friendly Piece Collision
                // Move is legal if the square is empty (null) OR if the piece is an enemy
                if (targetPiece == null || !this.isSameColor(targetPiece)) {
                    moves.add(target);
                }
            }
        }
        return moves;
    }

    /**
     * Returns the symbol representing the Knight
     * <p>
     * Uses "N" rather than "K" to avoid confusion with the King.
     * </p>
     *
     * @return "N" for Knight
     */
    @Override
    public String getSymbol() {
        return "N";
    }

    /**
     * Returns the value of the Knight piece
     *
     * @return 3, the point value of a Knight
     */
    @Override
    public int getValue() {
        return 3;
    }

    /**
     * Creates a deep copy of this Knight piece
     * <p>
     * The copied Knight has the same color, position, and movement history
     * as the original but is a completely independent object
     * </p>
     *
     * @return a new Knight instance with the same properties as this Knight
     */
    @Override
    public Piece copy() {
        Knight copy = new Knight(this.color, new Coordinate(position.getRow(), position.getCol()));
        copy.setHasMoved(this.hasMoved);
        return copy;
    }

    // Is king Checked

    /**
     * Determines whether the King is attacking a specific square
     * 
     * @param board  the current {@link Board} state; must not be {@code null}
     * @param target the {@link Coordinate} to check for attack
     * @return {@code true} if the King can legally move to the target square,
     *         {@code false} otherwise
     */
    @Override
    public boolean isAttacking(Board board, Coordinate target) {
        return getLegalMoves(board).contains(target);
    }
}
