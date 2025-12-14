package Chess;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Pawn piece in chess
 *
 * @author Group3
 * @version 1.2
 */
public class Pawn extends Piece {

    /**
     * Constructs a new {@code Pawn} with the specified color and position
     *
     * @param color    the color of the Pawn, either "White" or "Black"; must not be
     *                 null
     * @param position the starting position of the Pawn on the board; must not be
     *                 null
     */
    public Pawn(String color, Coordinate position) {
        super(color, position);
    }

    /**
     * Returns a list of legal move coordinates for the Pawn
     *
     * @param board the current chess board state; must not be null
     * @return a list of coordinates representing possible Pawn moves; never null
     *         but may be empty
     */
    @Override
    public List<Coordinate> getLegalMoves(Board board) {
        List<Coordinate> moves = new ArrayList<>();
        int r = position.getRow();
        int c = position.getCol();
        int direction;
        if (isWhite()) {
            direction = 1;
        } else {
            direction = -1;
        }

       // System.out.println("Pawn at (" + r + "," + c + "), direction=" + direction);

        // Forward 1
        int forwardRow = r + direction;
        if (forwardRow >= 0 && forwardRow < 8 && board.getPieceAt(forwardRow, c) == null) {
            moves.add(new Coordinate(forwardRow, c));

            // Forward 2 on first move
            if (!hasMoved) {
                int forwardTwo = r + 2 * direction;
                if (forwardTwo >= 0 && forwardTwo < 8 && board.getPieceAt(forwardTwo, c) == null) {
                    moves.add(new Coordinate(forwardTwo, c));
                }
            }
        }

        // Diagonal captures
        int captureRow = r + direction; // one row forwards
        if(captureRow >= 0 && captureRow < 8)
        {
            int[] captureCols = { c - 1, c + 1 };

            for (int col : captureCols) 
            {
                if (col >= 0 && col < 8) 
                {
                    Piece target = board.getPieceAt(captureRow, col);

                    // Onlt add if capturing an piece
                    if (target != null && canCapture(target)) 
                    {
                        moves.add(new Coordinate(captureRow, col));
                    }
                }
            }
        }
        
        // Special Rules: En Passant 
        Move lastMove = board.getLastMove();

        // Check for en passant: oppenents last move was a double step pawn advance
        if(lastMove != null && lastMove.getMovedPiece() instanceof Pawn)
        {
            // Find the pawn the oppenent just move
            Piece lastPieceMoved = lastMove.getMovedPiece();

            // Coordinates for oppenent's last move
            int fromRow = lastMove.getFrom().getRow();
            int toRow = lastMove.getTo().getRow();
            int toCol = lastMove.getTo().getCol();

            // Black/White pawn move 2 squares last turn
            if(Math.abs(toRow - fromRow) == 2)
            {
                // Is Pawn adjacent to opponents Pawn
                if(toCol == c - 1 || toCol == c + 1)
                {
                    Piece adjacent = board.getPieceAt(r, toCol); 

                    // Check if this piece is the opponent's pawn
                    if(adjacent instanceof Pawn && !adjacent.isSameColor(this))
                    {
                        // En passant on the square
                        moves.add(new Coordinate(captureRow, toCol));
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Returns the symbol representing the Pawn.
     *
     * @return "P" for Pawn
     */
    @Override
    public String getSymbol() {
        return "P";
    }

    /**
     * Returns the value of the Pawn piece
     *
     * @return 1, the point value of a Pawn
     */
    @Override
    public int getValue() {
        return 1;
    }

    /**
     * Creates a deep copy of this Pawn piece
     *
     * @return a new Pawn instance with the same properties as this Pawn
     */
    @Override
    public Piece copy() {
        Pawn copy = new Pawn(this.color, new Coordinate(position.getRow(), position.getCol()));
        copy.setHasMoved(this.hasMoved);
        return copy;
    }

    /**
     * Checks if this pawn is attacking the given target square
     * 
     * @param board  The current chess board state (included for consistency with
     *               other pieces)
     * @param target The square to check if it’s being attacked by this pawn
     * @return {@code true} if the target square is diagonally forward from this
     *         pawn, otherwise {@code false}
     */
    // Attacks diagonal only
    @Override
    public boolean isAttacking(Board board, Coordinate target) {
        int r = position.getRow();
        int c = position.getCol();
        int direction = isWhite() ? 1 : -1;

        // Diagonal attack squares only
        int[] diagCols = { c - 1, c + 1 };
        for (int col : diagCols) {
            int row = r + direction;
            if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                if (target.equals(new Coordinate(row, col))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *  Check if pawn is currently on promotion rank
     * White Pawn promotes on row 7
     * Black pawn promotes on row 0
     * @param targetRow Where the row move
     * @return {@code true} if pawn is on promotion rank, otherwise {@code false}
     */
    public boolean promotionRank(int targetRow)
    {
        if(isWhite())
        {
            return targetRow == 7; // White promtes on rank 8 (row 7)
        }
        else
        {
            return targetRow == 0; // Black promotes on rank 1 (row 0)
        }
    }

}