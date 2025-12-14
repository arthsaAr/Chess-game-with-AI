package Chess;
import java.util.List;

/**
 * Contains the logic for validating moves and determining end-game conditions
 * Composed within {@link ChessGame}
 *
 * @author Group3
 * @version 1.3
 */
public class RuleEngine {
    private Board board;

    /** @param board board whose moves are being validated */
    public RuleEngine(Board board) {
        this.board = board;
    }

    /**
     * Checks if the king of the given color is currently in check
     * A king is in check if any opponent piece is attacking its position
     * 
     * @param kingColor The color of the king ("White" or "Black")
     * @return {@code true} if the king is in check, otherwise {@code false}
     */
    // Check if king is in check
    public boolean isKingInCheck(String kingColor) {
        // Assume Board has a method to find the King's position
        Coordinate kingPos = board.findKing(kingColor);
        if (kingPos == null) {
            return false; // Should not happen
        }

        String opponentColor = kingColor.equals("White") ? "Black" : "White";

        // Iterate through all opponent pieces
        for (Piece piece : board.getAllPieces(opponentColor)) {
            // Check if any opponent piece attacks the King's square
            if (piece.isAttacking(board, kingPos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the player of the given color has any legal moves available
     * 
     * @param playerColor The color of the player ("White" or "Black")
     * @return {@code true} if the player has at least one legal move, otherwise
     *         {@code false}
     */
    // Check if player has any legal moves
    private boolean hasAnyLegalMoves(String playerColor) {
        // Iterate over all pieces of the current player
        for (Piece piece : board.getAllPieces(playerColor)) {
            List<Coordinate> pseudoMoves = piece.getLegalMoves(board);

            // Test each pseudo-legal move
            for (Coordinate target : pseudoMoves) {
                // Determine the captured piece for the Move object
                Piece targetPiece = board.getPieceAt(target.getRow(), target.getCol());
                Move testMove = new Move(piece.getPosition(), target, piece, targetPiece);

                // Simulate the move on a temporary state/board (or use undo)
                board.simulateMove(testMove);
                boolean strillInCheck = isKingInCheck(playerColor);
                board.undoMove(testMove);

                // If the King is NOT in check after the move, it's a legal move.
                if (!strillInCheck) {
                    return true; // Found at least one legal move
                }

            }
        }
        return false; // No legal moves found for any piece
    }

    /**
     * Check if King can castle on either sides
     * 
     * @param king     the king attempting to catsle
     * @param kingSide true for king side castle, false for Queen side castle
     * @param board    The game board
     * @return true if castling is legal, false otherwise.
     */
    public boolean canCastle(Board board, King king, boolean kingSide) {
        int row = king.getPosition().getRow();
        int col = king.getPosition().getCol();

        /*
         * Rook Positions
         * King-side rook = column 7
         * Queen-side rook = column 0
         */
        int rook_col = kingSide ? 7 : 0;
        Piece rook = board.getPieceAt(row, rook_col); // rook is inline with king

        // Rook must exist, be the same color and be unmoved
        if (!(rook instanceof Rook) || rook.getColor() != king.getColor() || rook.hasMoved()) {
            return false;
        }

        // Path between king and rook is empty
        int start = Math.min(col, rook_col) + 1;
        int end = Math.max(col, rook_col) - 1;

        // Check all square between rook and king
        for (int c = start; c <= end; c++) {
            if (board.getPieceAt(row, c) != null) {
                return false;
            }
        }

        /*
         * King cannot castle out of, through, or into check
         * King side: f5, g6
         * Queen side: d3, c2
         */
        int[] kingPath = kingSide ? new int[] { 5, 6 }
                : new int[] { 3, 2 };

        // check king's current square
        if (isKingInCheck(king.getColor())) {
            return false;
        }

        // check squares king move through
        for (int targetCol : kingPath) {
            Move testMove = new Move(
                    king.getPosition(),
                    new Coordinate(row, targetCol),
                    king,
                    null);

            board.simulateMove(testMove);
            boolean isInCheck = isKingInCheck(king.getColor());
            board.undoMove(testMove);

            if (isInCheck) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the given player's king is currently in check
     *
     * @param player The player whose king to check.
     * @return {@code true} if the player's king is in check, otherwise
     *         {@code false}
     */
    // Checks wheather player king is in check
    public boolean isInCheck(Player player) {
        return isKingInCheck(player.getColor());
    }

    /**
     * Determine whether the given player is in check for checkmate
     * 
     * @return true if the player is in checkmate (stub implementation).
     * @param player the player in checkmate
     */
    public boolean isCheckmate(Player player) {
        String playerColor = player.getColor();
        // Checkmate, in check and NO legal moves
        return isKingInCheck(playerColor) && !hasAnyLegalMoves(playerColor);
    }

    /** @return true if stalemate condition exists (stub implementation). */
    public boolean isStalemate(Player player) {
        String playerColor = player.getColor();
        // Stalemate, NOT in check and NO legal MOVES
        return !isKingInCheck(playerColor) && !hasAnyLegalMoves(playerColor);
        // return false;
    }

    /**
     * Determines whether a given {@link Move} is legal according to chess rules
     *
     * @param move the {@link Move} to validate; must not be {@code null}
     * @return {@code true} if the move is legal, {@code false} otherwise
     */
    public boolean isMoveLegal(Move move) {
        // Basic check: is the destination in the piece's pseudo-legal list?
        if (!move.getMovedPiece().isMoveValid(board, move)) {
            return false;
        }

        // The critical check: does the move prevent the King from being in check?
        String playerColor = move.getMovedPiece().getColor();

        // 1. Temporarily execute the move
        board.simulateMove(move);

        // 2. Check if the King is still in check after the move
        boolean isSafe = !isKingInCheck(playerColor);

        // 3. Undo the move
        board.undoMove(move);

        return isSafe;
    }
}
