package Chess;
import java.util.List;

/**
 * It gives hints for the current player based on the board
 * <p>
 * The {@code GetHint} class will analyze the current chess board state and from
 * there it will generate hints for the player
 * </p>
 *
 * @author Group3
 * @version 1.0
 */
public class GetHint {
    /** The chess board to help with analysis */
    private Board board;

    /** The rule engine for validating moves and checking game state */
    private RuleEngine ruleEngine;

    public GetHint(Board board) {
        this.board = board;
        this.ruleEngine = new RuleEngine(board);
    }

    /**
     * Generates a helpful hint for the current player
     * 
     * @param playerColor the color of the current player ("White" or "Black")
     * @return a string containing a specific, actionable hint for the player
     */
    public String generateHint(String playerColor) {
        // Priority 1: Check if in check if so then do first
        if (ruleEngine.isKingInCheck(playerColor)) {
            return getCheckEscapeHint(playerColor);
        }

        // Priority 2: Check for captures available
        Move captureMove = findBestCapture(playerColor);
        if (captureMove != null) {
            return formatCaptureHint(captureMove);
        }

        // Priority 3: Check for threats to our pieces
        Move defensiveMove = findThreatenedPiece(playerColor);
        if (defensiveMove != null) {
            return formatDefensiveHint(defensiveMove);
        }

        // Priority 4: Check if we can give check
        Move checkMove = findCheckMove(playerColor);
        if (checkMove != null) {
            return formatCheckHint(checkMove);
        }

        // Priority 5: Strategic development hints
        return getStrategicHint(playerColor);
    }

    /**
     * Provides a hint when the player's king is in check
     * 
     * @param playerColor the color of the player in check
     * @return a hint describing how to escape check, or a general warning if no
     *         specific escape is found
     */
    private String getCheckEscapeHint(String playerColor) {
        Piece king = findKing(playerColor);
        if (king == null)
            return "Move your king to safety!";

        List<Coordinate> kingMoves = king.getLegalMoves(board);

        // Check if king can move to safety
        for (Coordinate move : kingMoves) {
            Move testMove = new Move(king.getPosition(), move, king,
                    board.getPieceAt(move.getRow(), move.getCol()));
            board.simulateMove(testMove);
            boolean stillInCheck = ruleEngine.isKingInCheck(playerColor);
            board.undoMove(testMove);

            if (!stillInCheck) {
                return "Your king is in check! Move it to " +
                        coordinateToNotation(move) + " to escape.";
            }
        }

        // Check if we can block the check
        // String opponentColor = playerColor.equals("White") ? "Black" : "White";
        List<Piece> myPieces = board.getAllPieces(playerColor);

        for (Piece piece : myPieces) {
            if (piece instanceof King)
                continue;

            List<Coordinate> moves = piece.getLegalMoves(board);
            for (Coordinate move : moves) {
                Move testMove = new Move(piece.getPosition(), move, piece,
                        board.getPieceAt(move.getRow(), move.getCol()));
                board.simulateMove(testMove);
                boolean stillInCheck = ruleEngine.isKingInCheck(playerColor);
                board.undoMove(testMove);

                if (!stillInCheck) {
                    return "Block the check! Move your " + piece.getClass().getSimpleName() +
                            " from " + coordinateToNotation(piece.getPosition()) +
                            " to " + coordinateToNotation(move);
                }
            }
        }

        return "Your king is in check! Find a way to escape.";
    }

    /**
     * Finds the best safe capture move available for the player
     *
     * @param playerColor the color of the player to find captures for
     * @return the highest-value safe capture move, or {@code null} if no safe
     *         captures are available
     */
    private Move findBestCapture(String playerColor) {
        List<Piece> myPieces = board.getAllPieces(playerColor);
        Move bestCapture = null;
        int bestValue = 0;

        for (Piece piece : myPieces) {
            List<Coordinate> moves = piece.getLegalMoves(board);

            for (Coordinate move : moves) {
                Piece target = board.getPieceAt(move.getRow(), move.getCol());

                if (target != null && !piece.isSameColor(target)) {
                    // Check if this capture is safe
                    Move captureMove = new Move(piece.getPosition(), move, piece, target);

                    if (isMoveSafe(captureMove, playerColor)) {
                        int captureValue = target.getValue();
                        if (captureValue > bestValue) {
                            bestValue = captureValue;
                            bestCapture = captureMove;
                        }
                    }
                }
            }
        }

        return bestCapture;
    }

    /**
     * Finds a piece that is currently under threat from an opponent
     *
     * @param playerColor the color of the player whose pieces to check
     * @return a move that saves a threatened piece, or {@code null} if no
     *         threatened pieces can be saved
     */
    private Move findThreatenedPiece(String playerColor) {
        String opponentColor = playerColor.equals("White") ? "Black" : "White";
        List<Piece> myPieces = board.getAllPieces(playerColor);

        for (Piece piece : myPieces) {
            if (piece instanceof King)
                continue; // King threats handled separately

            Coordinate pos = piece.getPosition();

            // Check if any opponent piece can capture this piece
            List<Piece> opponentPieces = board.getAllPieces(opponentColor);
            for (Piece opponent : opponentPieces) {
                if (opponent.getLegalMoves(board).contains(pos)) {
                    // This piece is threatened, try to find a safe move
                    List<Coordinate> safeMoves = piece.getLegalMoves(board);
                    for (Coordinate safeMove : safeMoves) {
                        Move escapeMove = new Move(pos, safeMove, piece,
                                board.getPieceAt(safeMove.getRow(), safeMove.getCol()));

                        if (isMoveSafe(escapeMove, playerColor) &&
                                !isPieceThreatenedAt(safeMove, playerColor)) {
                            return escapeMove;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Finds a move that gives check to the opponent's king
     *
     * @param playerColor the color of the player to find check-giving moves for
     * @return a move that gives check to the opponent, or {@code null} if no
     *         safe check-giving moves are available
     */
    private Move findCheckMove(String playerColor) {
        String opponentColor = playerColor.equals("White") ? "Black" : "White";
        List<Piece> myPieces = board.getAllPieces(playerColor);

        for (Piece piece : myPieces) {
            List<Coordinate> moves = piece.getLegalMoves(board);

            for (Coordinate move : moves) {
                Move testMove = new Move(piece.getPosition(), move, piece,
                        board.getPieceAt(move.getRow(), move.getCol()));

                board.simulateMove(testMove);
                boolean givesCheck = ruleEngine.isKingInCheck(opponentColor);
                boolean isSafe = !ruleEngine.isKingInCheck(playerColor);
                board.undoMove(testMove);

                if (givesCheck && isSafe) {
                    return testMove;
                }
            }
        }

        return null;
    }

    /**
     * Provides strategic development hints for board positioning
     *
     * @param playerColor the color of the player to generate strategic hints for
     * @return a strategic hint for improving board position
     */
    private String getStrategicHint(String playerColor) {
        List<Piece> myPieces = board.getAllPieces(playerColor);

        for (Piece piece : myPieces) {
            if ((piece instanceof Knight || piece instanceof Bishop) && !piece.hasMoved()) {
                List<Coordinate> moves = piece.getLegalMoves(board);
                if (!moves.isEmpty()) {
                    // Find a central move
                    for (Coordinate move : moves) {
                        if (isCentralSquare(move)) {
                            return "Develop your " + piece.getClass().getSimpleName() +
                                    " from " + coordinateToNotation(piece.getPosition()) +
                                    " to " + coordinateToNotation(move) + " to control the center.";
                        }
                    }

                    return "Develop your " + piece.getClass().getSimpleName() +
                            " from " + coordinateToNotation(piece.getPosition()) +
                            " to " + coordinateToNotation(moves.get(0));
                }
            }
        }
        for (Piece piece : myPieces) {
            if (piece instanceof Pawn && !piece.hasMoved()) {
                Coordinate pos = piece.getPosition();
                if (pos.getCol() == 3 || pos.getCol() == 4) { // d or e pawns
                    List<Coordinate> moves = piece.getLegalMoves(board);
                    if (!moves.isEmpty()) {
                        return "Advance your " + coordinateToNotation(pos) +
                                " pawn to control the center.";
                    }
                }
            }
        }

        Piece king = findKing(playerColor);
        if (king != null && !king.hasMoved()) {
            return "Consider castling to protect your king and activate your rook.";
        }

        return "Look for moves that improve your piece positions and control key squares.";
    }

    /**
     * Checks if a move is safe by verifying the king is not in check after the
     * move
     *
     * @param move        the move to validate
     * @param playerColor the color of the player making the move
     * @return {@code true} if the move doesn't leave the king in check,
     * 
     *         {@code false} otherwise
     */
    private boolean isMoveSafe(Move move, String playerColor) {
        board.simulateMove(move);
        boolean isSafe = !ruleEngine.isKingInCheck(playerColor);
        board.undoMove(move);
        return isSafe;
    }

    /**
     * Checks if a piece would be threatened by an opponent at a given position.
     *
     * @param pos         the coordinate to check for threats
     * @param playerColor the color of the player (to determine opponent pieces)
     * @return {@code true} if an opponent piece can attack the position,
     *         {@code false} otherwise
     */
    private boolean isPieceThreatenedAt(Coordinate pos, String playerColor) {
        String opponentColor = playerColor.equals("White") ? "Black" : "White";
        List<Piece> opponentPieces = board.getAllPieces(opponentColor);

        for (Piece opponent : opponentPieces) {
            if (opponent.getLegalMoves(board).contains(pos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the king piece of the specified color
     *
     * @param color the color of the king to find ("White" or "Black")
     * @return the {@link King} piece of the specified color, or {@code null} if
     *         not found
     */
    private Piece findKing(String color) {
        return board.getAllPieces(color).stream()
                .filter(p -> p instanceof King)
                .findFirst()
                .orElse(null);
    }

    /**
     * Checks if a coordinate is in the central four squares of the board
     * 
     * @param coord the coordinate to check
     * @return {@code true} if the coordinate is a central square, {@code false}
     *         otherwise
     */
    private boolean isCentralSquare(Coordinate coord) {
        int row = coord.getRow();
        int col = coord.getCol();
        return (row >= 3 && row <= 4) && (col >= 3 && col <= 4);
    }

    /**
     * Converts a coordinate to algebraic chess notation
     *
     * @param coord the coordinate to convert
     * @return the algebraic notation string
     */
    private String coordinateToNotation(Coordinate coord) {
        char file = (char) ('a' + coord.getCol());
        char rank = (char) ('1' + coord.getRow());
        return "" + file + rank;
    }

    /**
     * Formats a capture move into a human structured hint
     * 
     * @param move the capture move to format
     * @return a formatted string describing the capture opportunity
     */
    private String formatCaptureHint(Move move) {
        String pieceName = move.getMovedPiece().getClass().getSimpleName();
        String targetName = move.getCapturedPiece().getClass().getSimpleName();
        String from = coordinateToNotation(move.getFrom());
        String to = coordinateToNotation(move.getTo());

        return "Capture opportunity! Move your " + pieceName + " from " +
                from + " to " + to + " to capture the opponent's " + targetName + ".";
    }

    /**
     * Formats a defensive move into a human structured hint
     * 
     * @param move the defensive move to format
     * @return a formatted string describing the defensive action
     */
    private String formatDefensiveHint(Move move) {
        String pieceName = move.getMovedPiece().getClass().getSimpleName();
        String from = coordinateToNotation(move.getFrom());
        String to = coordinateToNotation(move.getTo());

        return "Your " + pieceName + " at " + from + " is under attack! " +
                "Move it to " + to + " for safety.";
    }

    /**
     * Formats a check-giving move into a human structured hint
     * 
     * @param move the check-giving move to format
     * @return a formatted string describing the offensive check opportunity
     */
    private String formatCheckHint(Move move) {
        String pieceName = move.getMovedPiece().getClass().getSimpleName();
        String from = coordinateToNotation(move.getFrom());
        String to = coordinateToNotation(move.getTo());

        return "Attack! Move your " + pieceName + " from " + from + " to " +
                to + " to give check to the opponent's king!";
    }
}