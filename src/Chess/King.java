package Chess;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a King piece in chess.
 * <p>
 * The King is the most important piece in chess. It can move one square in any
 * direction: horizontally, vertically, or diagonally. The King has special
 * movement
 * rules including castling (not implemented in basic move generation) and
 * cannot
 * move into check.
 * </p>
 *
 * @author Group3
 * @version 1.0
 */

public class King extends Piece {

    /**
     * Constructs a new {@code King} with the specified color and position
     *
     * @param color    the color of the King, either "White" or "Black"; must not be
     *                 null
     * @param position the starting position of the King on the board; must not be
     *                 null
     */

    public King(String color, Coordinate position) {
        super(color, position);
    }

    /**
     * Returns a list of legal move coordinates for the King
     *
     * @param board the current chess board state; must not be null
     * @return a list of coordinates representing possible King moves; never null
     *         but may be empty
     */
    @Override
    public List<Coordinate> getLegalMoves(Board board) {
        List<Coordinate> moves = new ArrayList<>();
        // square
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

        List<Piece> opponentPieces = board.getAllPieces(switchColor(this.getColor()));
        // Calculate all possible moves
        for (int[] d : dirs) {
            int r = position.getRow() + d[0];
            int c = position.getCol() + d[1];
            // check board boundaries
            if (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Coordinate target = new Coordinate(r, c);
                Piece targetPiece = board.getPieceAt(r, c);

                // Check for collision/capture
                if (targetPiece == null || !this.isSameColor(targetPiece)) {
                    // adding external method to exclude king's move which is not valid/because it
                    // is attacked by other piece
                    boolean isAttacked = false;
                    for (int i = 0; i < opponentPieces.size(); i++) {
                        Piece opponentPiece = opponentPieces.get(i);

                        if (opponentPiece instanceof King) {
                            int adjR = Math.abs(r - opponentPiece.getRow());
                            int adjC = Math.abs(c - opponentPiece.getCol());
                            if (adjR <= 1 && adjC <= 1) {
                                isAttacked = true;
                                break;
                            }
                        } else {
                            List<Coordinate> opponentMoves = opponentPieces.get(i).getLegalMoves(board);
                            for (int j = 0; j < opponentMoves.size(); j++) {
                                if (opponentMoves.get(j).equals(target)) {
                                    isAttacked = true;
                                    break;
                                }
                            }
                            if (isAttacked) {
                                break;
                            }
                        }
                    }
                    if (!isAttacked) {
                        moves.add(target);
                    }
                }
            }
        }

        // Casting Logic

        if (!this.hasMoved) // king doesn't move
        {
            // King's current row (0 = white, 7 = black)
            int row = this.position.getRow();

            RuleEngine engine = new RuleEngine(board);

            // King Side Castle: are conditions satisfied
            if (engine.canCastle(board, this, true)) {
                // King moves from column 4 --> 6
                moves.add(new Coordinate(row, 6));
            }

            // Queen Side Castle: are conditions satisfied
            if (engine.canCastle(board, this, false)) {
                // King moves from column 4 --> 2
                moves.add(new Coordinate(row, 2));
            }
        }

        return moves;
    }

    /**
     * Returns the symbol representing the King
     * 
     * @return "K" for King
     */
    @Override
    public String getSymbol() {
        return "K";
    }

    /**
     * Returns the value of the King piece
     *
     * @return 1000, representing the invaluable nature of the King
     */
    @Override
    public int getValue() {
        return 1000; // King is invaluable
    }

    /**
     * Creates a deep copy of this King piece
     *
     * @return a new King instance with the same properties as this King
     */
    @Override
    public Piece copy() {
        King copy = new King(this.color, new Coordinate(position.getRow(), position.getCol()));
        copy.setHasMoved(this.hasMoved);
        return copy;
    }

    /**
     * Checks if this king is attacking the given target square
     *
     * @param board  The current chess board state (included for consistency with
     *               other pieces)
     * @param target The square to check if it’s being attacked by this king
     * @return {@code true} if the target square is adjacent to the king, otherwise
     *         {@code false}
     */
    // Is king Checked
    @Override
    public boolean isAttacking(Board board, Coordinate target) {
        // Only adjacent squares matter for attacking
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };
        for (int[] d : dirs) {
            int r = position.getRow() + d[0];
            int c = position.getCol() + d[1];
            if (r >= 0 && r < 8 && c >= 0 && c < 8 && target.equals(new Coordinate(r, c))) {
                return true;
            }
        }
        return false;
    }

    // helper function to switch color, mainly used to analyze the captures from
    // opponent's perspective

    /**
     * Returns the opposite color of the given player
     *
     * @param color the current color ("White" or "Black"); case-sensitive
     * @return "Black" if {@code color} is "White", otherwise "White"
     */
    private String switchColor(String color) {
        String changed;
        if (color.equals("White")) {
            changed = "Black";
        } else {
            changed = "White";
        }
        return changed;
    }

}
