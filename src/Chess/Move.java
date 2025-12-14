package Chess;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single chess move with detailed logging information
 * <p>
 * Stores source and destination coordinates and the pieces involved.
 * Now also captures legal moves for detailed game analysis and PGN annotation.
 * </p>
 * 
 * @author Group3
 * @version 1.3
 */
public class Move {
    private Coordinate from;
    private Coordinate to;
    private Coordinate rookFrom; // Stores rook movement
    private Coordinate rookTo; // Stores rook movement

    private Piece movedPiece;
    private Piece capturedPiece;

    private List<Coordinate> legalMoves; // All legal moves from source
    private long timestamp; // When the move was made

    private String promotionPieces; // Which piece pawn promotes to
    
    /**
     * Creates a move record.
     * 
     * @param from          starting coordinate
     * @param to            destination coordinate
     * @param movedPiece    piece being moved
     * @param capturedPiece piece being captured, or {@code null}
     */
    public Move(Coordinate from, Coordinate to, Piece movedPiece, Piece capturedPiece) {
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.timestamp = System.currentTimeMillis();
        this.legalMoves = new ArrayList<>();
        this.promotionPieces = null;
    }

    /**
     * Enhanced constructor with legal moves for detailed logging
     */

    /**
     * Constructs a {@link Move} object with detailed information, including
     * legal moves and promotion options
     *
     * @param from            the starting {@link Coordinate} of the move
     * @param to              the ending {@link Coordinate} of the move
     * @param movedPiece      the {@link Piece} being moved
     * @param capturedPiece   the {@link Piece} being captured, or {@code null} if
     *                        none
     * @param legalMoves      a {@link List} of legal {@link Coordinate}
     *                        destinations for this piece; may be {@code null}
     * @param promotionPieces a {@link String} representing promotion options, if
     *                        applicable; may be {@code null}
     */
    public Move(Coordinate from, Coordinate to, Piece movedPiece, Piece capturedPiece, List<Coordinate> legalMoves,
            String promotionPieces) {
        this(from, to, movedPiece, capturedPiece);
        this.legalMoves = legalMoves != null ? new ArrayList<>(legalMoves) : new ArrayList<>();
        this.promotionPieces = promotionPieces;
    }

    /**
     * Sets the piece being moved. Used for pawn promotion to update the Piece
     * object
     * from the Pawn to the promoted piece (e.g., Queen).
     *
     * @param movedPiece the new piece object (e.g., Queen)
     */
    public void setMovedPiece(Piece movedPiece) {
        this.movedPiece = movedPiece;
    }

    /**
     * Creates a move from square objects
     *
     * @param selectedSquare the square containing the piece to move; must not be
     *                       null
     * @param clickedSquare  the destination square; must not be null
     */
    public Move(Square selectedSquare, Square clickedSquare) {
        this.from = new Coordinate(selectedSquare.getRow(), selectedSquare.getCol());
        this.to = new Coordinate(clickedSquare.getRow(), clickedSquare.getCol());
        this.movedPiece = selectedSquare.getPiece();
        this.capturedPiece = clickedSquare.getPiece(); // may be null if empty
        this.timestamp = System.currentTimeMillis();
        this.legalMoves = new ArrayList<>();
        this.promotionPieces = null;
    }

    public void setRookMovement(Coordinate rookFrom, Coordinate rookTo) {
        this.rookFrom = rookFrom;
        this.rookTo = rookTo;
    }

    public Coordinate getRookFrom() {
        return rookFrom;
    }

    public Coordinate getRookTo() {
        return rookTo;
    }

    public void setRookFrom(Coordinate from) {
        this.rookFrom = from;
    }

    public void setRookTo(Coordinate to) {
        this.rookTo = to;
    }


    /**
     * Returns the starting coordinate of the move
     *
     * @return the source coordinate
     */
    public Coordinate getFrom() {
        return from;
    }

    /**
     * Returns the destination coordinate of the move
     *
     * @return the destination coordinate
     */
    public Coordinate getTo() {
        return to;
    }

    /**
     * Returns the piece being moved
     *
     * @return the moved piece
     */
    public Piece getMovedPiece() {
        return movedPiece;
    }

    /**
     * Returns the piece being captured
     *
     * @return the captured piece, or null if no piece was captured
     */
    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    /**
     * Returns the legal moves available from the source square
     *
     * @return list of legal move coordinates
     */
    public List<Coordinate> getLegalMoves() {
        // System.out.println("Retrieving legal moves, count: " + legalMoves.size());
        return new ArrayList<>(legalMoves);
    }

    /**
     * Sets the legal moves for this move
     *
     * @param legalMoves list of legal move coordinates
     */
    public void setLegalMoves(List<Coordinate> legalMoves) {
        this.legalMoves = legalMoves != null ? new ArrayList<>(legalMoves) : new ArrayList<>();
    }

    /**
     * Returns the timestamp of when this move was made
     *
     * @return timestamp in milliseconds
     */
    public long getTimestamp() {
        return timestamp;
    }

    public void setPromotionPieces(String promotionPieces) {
        this.promotionPieces = promotionPieces;
    }

    public String getPromotionPiece() {
        return promotionPieces;
    }

    /**
     * Generates a detailed log of this move with all legal move information
     * 
     * @return formatted string with move details
     */
    public String generateDetailedLog() {
        StringBuilder sb = new StringBuilder();

        String pieceName = movedPiece.getClass().getSimpleName();
        String pieceColor = movedPiece.getColor();

        String fromSquare = coordinateToNotation(from);
        String toSquare = coordinateToNotation(to);

        sb.append(pieceName).append(" (").append(pieceColor).append(") ");
        sb.append("from ").append(fromSquare).append(" to ").append(toSquare);

        if (capturedPiece != null) {
            String capturedName = capturedPiece.getClass().getSimpleName();
            sb.append(" captures ").append(capturedName);
        }

        sb.append(" | Legal moves: ").append(legalMoves.size());

        return sb.toString();
    }

    /**
     * Generates a detailed move report with all legal moves listed
     * 
     * @return formatted string with complete move analysis
     */
    public String generateMoveReport() {
        StringBuilder sb = new StringBuilder();

        String pieceName = movedPiece.getClass().getSimpleName();
        int row = from.getRow();
        int col = from.getCol();

        sb.append(pieceName).append(" at (").append(row).append(",").append(col).append(")\n");
        sb.append("  legal moves: ").append(legalMoves.size()).append("\n");

        for (Coordinate move : legalMoves) {
            sb.append("    -> (").append(move.getRow()).append(",").append(move.getCol()).append(")\n");
        }

        sb.append("  Move executed: ").append(from.getRow()).append(",").append(from.getCol());
        sb.append(" to ").append(to.getRow()).append(",").append(to.getCol()).append("\n");

        if (capturedPiece != null) {
            sb.append("  Capture: ").append(capturedPiece.getClass().getSimpleName()).append("\n");
        }

        return sb.toString();
    }

    /**
     * Converts a coordinate to chess notation (e.g., "e4")
     */
    private String coordinateToNotation(Coordinate coord) {
        char file = (char) ('a' + coord.getCol());
        char rank = (char) ('1' + coord.getRow());
        return "" + file + rank;
    }

    /**
     * Returns a string representation of the move
     *
     * @return a human-readable string describing the move
     */
    @Override
    public String toString() {
        return movedPiece.getClass().getSimpleName() + " " + from + " → " + to;
    }

}