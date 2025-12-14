package Chess;

import java.util.List;

/**
 * Abstract superclass for all chess pieces
 *
 * @author Group3
 * @version 1.0
 */
public abstract class Piece {
    /** Piece color ("White" or "Black"). */
    protected String color;

    /** Current position on the board. */
    protected Coordinate position;

    /** Indicates whether the piece has moved (important for castling, pawn moves). */
    protected boolean hasMoved;

    /**
     * Creates a new piece.
     *
     * @param color piece color ("White" or "Black")
     * @param position starting coordinate
     */
    public Piece(String color, Coordinate position) {
        this.color = color;
        this.position = position;
        this.hasMoved = false;
    }

    /**
     * Computes all legal moves for this piece on the given board.
     *
     * @param board current game board
     * @return list of legal destination coordinates
     */
    public abstract List<Coordinate> getLegalMoves(Board board);

    /**
     * Checks if a proposed move is valid.
     *
     * @param board game board to test on
     * @param move move candidate
     * @return {@code true} if the move is allowed; {@code false} otherwise
     */
    public boolean isMoveValid(Board board, Move move) {
        Piece targetPiece = board.getPieceAt(move.getTo().getRow(), move.getTo().getCol());
        if(targetPiece != null && this.isSameColor(targetPiece)){
            return false;
        }
        return getLegalMoves(board).contains(move.getTo());
    }

    /**
     * Moves this piece to a new position.
     *
     * @param newPosition the destination coordinate
     */
    public void moveTo(Coordinate newPosition) {
        this.position = newPosition;
        this.hasMoved = true;
    }

    /**
     * Sets the position without marking as moved (used for board initialization).
     *
     * @param position the new position
     */
    public void setPosition(Coordinate position) {
        this.position = position;
    }

    /**
     * Checks if this piece can capture the target piece.
     *
     * @param target the piece to potentially capture
     * @return true if capture is allowed (opposite colors)
     */
    public boolean canCapture(Piece target) {
        return target != null && !target.getColor().equals(this.color);
    }

    /**
     * Checks if this piece is the same color as another piece.
     *
     * @param other the other piece
     * @return true if both pieces are the same color
     */
    public boolean isSameColor(Piece other) {
        return other != null && other.getColor().equals(this.color);
    }

    /**
     * Checks if this piece is white.
     *
     * @return true if the piece is white
     */
    public boolean isWhite() {
        return "White".equalsIgnoreCase(color);
    }

    /**
     * Checks if this piece is black.
     *
     * @return true if the piece is black
     */
    public boolean isBlack() {
        return "Black".equalsIgnoreCase(color);
    }

    /**
     * Gets the piece value for evaluation purposes.
     * Override in subclasses for specific values.
     *
     * @return piece value (e.g., Pawn=1, Knight=3, etc.)
     */
    public int getValue() {
        return 0; // Override in subclasses
    }

    /**
     * Gets the algebraic notation symbol for this piece.
     * 
     * @return single character representing the piece (K, Q, R, B, N, P)
     */
    public abstract String getSymbol();

    /**
     * Creates a copy of this piece (for board evaluation).
     * Override in subclasses if needed.
     *
     * @return a new piece with the same properties
     */
    public abstract Piece copy();

    // Checks if this piece, based on its movement pattern (pseudo-legally),could attack the given target coordinate.
    public abstract boolean isAttacking(Board board, Coordinate target);

    // ========== Getters and Setters ==========

    /** @return this piece's color */
    public String getColor() { 
        return color; 
    }

    /** @return current coordinate */
    public Coordinate getPosition() { 
        return position; 
    }

    /** @return true if this piece has moved */
    public boolean hasMoved() { 
        return hasMoved; 
    }

    /** @param hasMoved sets whether the piece has moved */
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /** @return the row (rank) of this piece's position */
    public int getRow() {
        return position.getRow();
    }

    /** @return the column (file) of this piece's position */
    public int getCol() {
        return position.getCol();
    }
}