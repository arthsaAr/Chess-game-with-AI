package Chess;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents the standard 8×8 chessboard
 * <p>
 * Composed of {@link Square} objects that hold {@link Piece}
 * Responsible for initializing the starting position of all pieces
 * </p>
 *
 * @author Group3
 * @version 1.1.0
 */
public class Board {

    /** Number of rows on the chessboard. */
    public static final int ROWS = 8;

    /** Number of columns on the chessboard. */
    public static final int COLUMNS = 8;

    /** Two-dimensional grid of squares. */
    private Square[][] squares;

    // Undo Functionalities
    private Stack<Move> undoStack = new Stack<>();
    // Redo Functionalities
    private Stack<Move> redoStack = new Stack<>();

    private GameScreen gameScreen;

    private int moveCounter = 0;
    private int whiteCounter = -1;
    private int blackCounter = -1;
    private boolean tutorialMode = false;

    /**
     * Constructs a new, empty chessboard and populates it with
     * standard starting pieces.
     */
    public Board(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        squares = new Square[ROWS][COLUMNS];
        initializeBoard();
    }

    /**
     * Initializes each square and sets up pieces in standard chess layout.
     */
    public void initializeBoard() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                squares[r][c] = new Square(r, c);
            }
        }
        // Place Black pieces (top side - rank 8 and 7)
        placeMajorPieces("Black", 7); // Rank 8 (row index 7)
        placePawns("Black", 6); // Rank 7 (row index 6)

        // Place White pieces (bottom side - rank 1 and 2)
        placePawns("White", 1); // Rank 2 (row index 1)
        placeMajorPieces("White", 0); // Rank 1 (row index 0)
    }

    /**
     * Pawns for the given color
     *
     * @param color the color of the pawns
     * @param row   the row where pawns should be placed
     */
    private void placePawns(String color, int row) {
        for (int c = 0; c < COLUMNS; c++) {
            squares[row][c].setPiece(new Pawn(color, new Coordinate(row, c)));
        }
    }

    /**
     * Helper method to place major pieces (R, N, B, Q, K, B, N, R)
     *
     * @param color the color of the pieces
     * @param row   the row where the pieces should be placed
     */
    private void placeMajorPieces(String color, int row) {
        squares[row][0].setPiece(new Rook(color, new Coordinate(row, 0)));
        squares[row][1].setPiece(new Knight(color, new Coordinate(row, 1)));
        squares[row][2].setPiece(new Bishop(color, new Coordinate(row, 2)));
        squares[row][3].setPiece(new Queen(color, new Coordinate(row, 3)));
        squares[row][4].setPiece(new King(color, new Coordinate(row, 4)));
        squares[row][5].setPiece(new Bishop(color, new Coordinate(row, 5)));
        squares[row][6].setPiece(new Knight(color, new Coordinate(row, 6)));
        squares[row][7].setPiece(new Rook(color, new Coordinate(row, 7)));
    }

    /**
     * Retrieves the piece at a specific board location using row/column indices.
     *
     * @param row board row index (0–7)
     * @param col board column index (0–7)
     * @return the {@link Piece} if present; otherwise {@code null}
     */
    public Piece getPieceAt(int row, int col) {
        if (isValidPosition(row, col)) {
            return squares[row][col].getPiece();
        }
        return null;
    }

    /**
     * Places or removes a piece at the specified location.
     *
     * @param row   target row
     * @param col   target column
     * @param piece the piece to place (or {@code null} to clear)
     */
    public void setPieceAt(int row, int col, Piece piece) {
        if (isValidPosition(row, col)) {
            squares[row][col].setPiece(piece);
        }
    }

    /**
     * Retrieves a square using chess notation (file, rank).
     * File 0 = 'a', File 7 = 'h'
     * Rank 0 = 1, Rank 7 = 8
     *
     * @param file the file (column) index 0-7 (a-h)
     * @param rank the rank (row) index 0-7 (1-8)
     * @return the Square at that position, or null if invalid
     */
    public Square getSquare(int file, int rank) {
        if (isValidPosition(rank, file)) {
            return squares[rank][file];
        }
        return null;
    }

    /**
     * Retrieves a square using algebraic notation (e.g., "e4").
     *
     * @param notation algebraic notation (e.g., "e4", "a1", "h8")
     * @return the Square at that position, or null if invalid
     */
    public Square getSquare(String notation) {
        if (notation == null || notation.length() != 2) {
            return null;
        }

        char fileChar = notation.charAt(0);
        char rankChar = notation.charAt(1);

        if (fileChar < 'a' || fileChar > 'h' || rankChar < '1' || rankChar > '8') {
            return null;
        }

        int file = fileChar - 'a'; // Convert 'a'-'h' to 0-7
        int rank = rankChar - '1'; // Convert '1'-'8' to 0-7

        return getSquare(file, rank);
    }

    /**
     * Checks if a position is valid on the board.
     *
     * @param row the row index
     * @param col the column index
     * @return true if position is within bounds
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < ROWS && col >= 0 && col < COLUMNS;
    }

    /**
     * Moves a piece from one square to another.
     *
     * @param fromRow              source row
     * @param fromCol              source column
     * @param toRow                destination row
     * @param toCol                destination column
     * @param choosePromotionPiece the users choosen piece type for promotion
     * @return {@code true} if the move was executed
     */
    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol, String choosePromotionPiece, boolean isRealMove) {
        moveCounter++;
        if (!isValidPosition(fromRow, fromCol) || !isValidPosition(toRow, toCol)) {
            return false;
        }
        
        Piece piece = getPieceAt(fromRow, fromCol);
        if (piece == null) {
            return false;
        }

        if(isRealMove && checkKingCheck(this, piece.getColor())){
            Board testBoard = this.copy();
            Piece testPiece = testBoard.getPieceAt(fromRow, fromCol);
            testBoard.setPieceAt(toRow, toCol, testPiece);
            testBoard.setPieceAt(fromRow, fromCol, null);

            testPiece.moveTo(new Coordinate(toRow, toCol));

            if(checkKingCheck(testBoard, piece.getColor())){
                return false;
            }
        }

        List<Coordinate> legalMoves = piece.getLegalMoves(this);

        Piece targetPiece = getPieceAt(toRow, toCol);
        if (targetPiece != null && piece.isSameColor(targetPiece)) {
            return false; // Cannot capture own piece
        }

        // Check for promotion
        String promotion = null;
        // String choosePromotionPiece = null;
        if (piece instanceof Pawn && choosePromotionPiece != null) {
            Pawn pawn = (Pawn) piece;
            if (pawn.promotionRank(toRow)) {
                // promotion = "Queen"; // hardcore Queen, QUIboard will have choices
                promotion = choosePromotionPiece; // GUI choice
            }
        }

        // UPDATED: Pass legalMoves to Move constructor
        Move move = new Move(piece.getPosition(), new Coordinate(toRow, toCol), piece, targetPiece, legalMoves,
                promotion);

        if (!piece.isMoveValid(this, move)) {
            return false;
        }

        // En Passant checks
        if (piece instanceof Pawn && targetPiece == null && fromCol != toCol) {
            // Pawn moved Diagonally
            if (fromCol != toCol) {
                int direction = piece.isWhite() ? 1 : -1; // Determine direction
                int capturedPawnRow = toRow - direction; // Calculate row for captured pawns

                Piece en_passantPawn = getPieceAt(capturedPawnRow, toCol); // get the captured pawn

                if (en_passantPawn instanceof Pawn && !en_passantPawn.isSameColor(piece)) {
                    // Remove captured Pawn
                    setPieceAt(capturedPawnRow, toCol, null);

                    // Record in Move object
                    move.setCapturedPiece(en_passantPawn);
                }
            }
        }

        // Move the piece
        setPieceAt(toRow, toCol, piece);
        setPieceAt(fromRow, fromCol, null);

        // Update piece position
        piece.moveTo(new Coordinate(toRow, toCol));

        //handling proper castling
        if(piece instanceof King){
            int colDiff = toCol-fromCol;
            if(colDiff ==2){
                Coordinate rookFrom = new Coordinate(fromRow, 7);
                Coordinate rookTo = new Coordinate(fromRow, 5);
            
                Piece rook = getPieceAt(rookFrom.getRow(), rookFrom.getCol());

                if(rook != null){
                    setPieceAt(rookTo.getRow(), rookTo.getCol(), rook);
                    setPieceAt(rookFrom.getRow(), rookFrom.getCol(), null);
                    rook.moveTo(rookTo);

                    move.setRookFrom(rookFrom);
                    move.setRookTo(rookTo);
                }
            } else if(colDiff == -2){
                Coordinate rookFrom = new Coordinate(fromRow, 0);
                Coordinate rookTo = new Coordinate(fromRow, 3);
            
                Piece rook = getPieceAt(rookFrom.getRow(), rookFrom.getCol());

                if(rook != null){
                    setPieceAt(rookTo.getRow(), rookTo.getCol(), rook);
                    setPieceAt(rookFrom.getRow(), rookFrom.getCol(), null);
                    rook.moveTo(rookTo);

                    move.setRookFrom(rookFrom);
                    move.setRookTo(rookTo);
                }
            }
        }

        // Promotion after piece is moved
        if (promotion != null) {
            pawnPromotion(toRow, toCol, move);
        }

        if(isRealMove){
            //for updating the captured piece panel
            Piece captured = move.getCapturedPiece();
            if(captured != null){
                boolean isWhiteCaptured = captured.isWhite();

                String color;
                if(isWhiteCaptured){
                    if(whiteCounter != moveCounter){
                        color = "W";
                        String type = captured.getClass().getSimpleName().toLowerCase();
                        gameScreen.addCapturedPiece(type+"_"+color, true);
                        whiteCounter = moveCounter;
                    }
                }else {
                    if(blackCounter != moveCounter){
                        color = "B";
                        String type = captured.getClass().getSimpleName().toLowerCase();
                        gameScreen.addCapturedPiece(type+"_"+color, false);
                        blackCounter = moveCounter;
                    }
                }
            }
        }
        

        undoStack.push(move); // Record move for undo
        redoStack.clear(); // Clear redo stack when new moves are made
        return true;
    }

    /** 
     * @param board the board state to check
     * @param color the color of the king to check ("White" or "Black")
     * @return {@code true} if the king is in check, {@code false} otherwise
     */
    private boolean checkKingCheck(Board board, String color) {
        Piece king = board.getAllPieces(color).stream().filter(p -> p instanceof King).findFirst().orElse(null);

        if (king == null) {
            return false;
        }

        Coordinate kingPosition = king.getPosition();
        String opponentColor;
        if(color == "White"){
            opponentColor = "Black";
        }else {
            opponentColor = "White";
        }


        for (int i = 0; i < board.getAllPieces(opponentColor).size(); i++) {
            Piece curPiece = board.getAllPieces(opponentColor).get(i);
            List<Coordinate> curMove = curPiece.getLegalMoves(board);
            for (int j = 0; j < curMove.size(); j++) {
                if (curMove.get(j).equals(kingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Creates a deep copy of the board.
     *
     * @return a new Board with the same piece positions
     */
    public Board copy() {
        Board newBoard = new Board(gameScreen);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                Piece piece = this.getPieceAt(r, c);
                if (piece != null) {
                    // You'll need to implement piece cloning
                    newBoard.setPieceAt(r, c, piece.copy());
                }
            }
        }
        return newBoard;
    }

    /**
     * Clears all pieces from the board.
     */
    public void clear() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                squares[r][c].setPiece(null);
            }
        }
    }

    // === Helper for RuleEngine ===
    /**
     * Finds the position of the king with the given color
     * 
     * @param color The color of the king ("White" or "Black")
     * @return The {@link Coordinate} of the king, or {@code null} if not found
     */
    // Finds King's Position and Color
    public Coordinate findKing(String color) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                Piece piece = getPieceAt(r, c);
                if (piece instanceof King && piece.getColor().equals(color)) {
                    return new Coordinate(r, c);
                }
            }
        }
        return null; // King not found
    }

    /**
     * Returns a list of all pieces belonging to the specific color
     * 
     * @param color The color of the pieces to find ("White" or "Black")
     * @return A list of {@link Piece} objects for that color
     */
    // Retrieves a list of all pieces belonging to the specified color
    public List<Piece> getAllPieces(String color) {
        List<Piece> pieces = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                Piece piece = getPieceAt(r, c);
                if (piece != null && piece.getColor().equals(color)) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    /**
     * Temporarily performs a move on the board
     * 
     * @param move The Move that shows which piece is moving and where it goes
     */
    // Test the move and is reversable (undoMove)
    public void simulateMove(Move move) {
        Coordinate from = move.getFrom();
        Coordinate to = move.getTo();
        Piece movedPiece = move.getMovedPiece();
        // --- Move Piece ---
        // 1. Clear the source square
        setPieceAt(from.getRow(), from.getCol(), null);

        // 2. Place the moved piece on the destination square
        setPieceAt(to.getRow(), to.getCol(), movedPiece);

        // 3. Update the piece's internal position (crucial for getLegalMoves)
        movedPiece.setPosition(to);
    }

    /**
     * Undoes the last move performed on the board
     * 
     * @return {@code true} if a move was undone, otherwise {@code false}
     */
    // Undoes the last move
    public boolean undoLastMove() {
        if (undoStack.isEmpty()) {
            return false;
        }
        Move lastMove = undoStack.pop();
        undoMove(lastMove);
        redoStack.push(lastMove);
        return true;
    }

    /**
     * Redoes the last undone move from the redo stack
     * This re-applies the move and adds it back to the undo history
     * 
     * @return {@code true} if a move was redone, otherwise {@code false}
     */
    public boolean redoLastMove() {
        if (redoStack.isEmpty()) {
            return false;
        }
        Move move = redoStack.pop();

        Coordinate from = move.getFrom();
        Coordinate to = move.getTo();
        Piece movedPiece = move.getMovedPiece();

        // === Normal ===
        setPieceAt(from.getRow(), from.getCol(), null);
        setPieceAt(to.getRow(), to.getCol(), movedPiece);
        movedPiece.setPosition(to);

        // === Promotion ===
        if (move.getPromotionPiece() != null) {
            // Promotion logics
            pawnPromotion(to.getRow(), to.getCol(), move);
        }

        // === Castling ====
        if (movedPiece instanceof King && move.getRookFrom() != null) {
            Coordinate rook_from = move.getRookFrom();
            Coordinate rook_to = move.getRookTo();

            Piece rook = getPieceAt(rook_from.getRow(), rook_from.getCol());

            // If rook isn't there, skip to avoid null
            if (rook != null) {
                setPieceAt(rook_to.getRow(), rook_to.getCol(), rook);
                setPieceAt(rook_from.getRow(), rook_from.getCol(), null);
                rook.moveTo(rook_to);
            }
        }
        // Puck back to undo stack
        undoStack.push(move);
        return true;
    }

    /**
     * @return the most recent {@link Move} from the undo stack, or {@code null}
     *         if no moves have been made
     */
    public Move getLastMove() {
        if (undoStack.isEmpty()) {
            return null;
        }
        return undoStack.peek();
    }

    // Clear all move history (game reset)

    public void clearHistory() {
        undoStack.clear();
        redoStack.clear();
    }

    /**
     * The piece types that pawn can promote to
     * 
     * @param type  Type of piece promotion
     * @param color Which color is the piece promoted? White or Black
     * @param pos   The position where the promotion happens
     */
    private Piece promotePieces(String type, String color, Coordinate pos) {
        // Types of piece promotions
        switch (type) {
            case "Queen":
                return new Queen(color, pos);

            case "Rook":
                return new Rook(color, pos);

            case "Bishop":
                return new Bishop(color, pos);

            case "Knight":
                return new Knight(color, pos);

            default:
                throw new IllegalArgumentException("Invalid promotion piece type: " + type);
        }
    }

    /**
     * Promote pawn to a new piece type at a given location
     *
     * @param row  the row of the promoted piece
     * @param col  the column of the promoted piece
     * @param move the move object that triggered the promotion
     */
    public void pawnPromotion(int row, int col, Move move) /// not done
    {
        String color = move.getMovedPiece().getColor();
        String type = move.getPromotionPiece();
        Coordinate pos = new Coordinate(row, col);

        // Create new piece
        Piece newPiece = promotePieces(type, color, pos);

        // Replace piece on the board
        setPieceAt(row, col, newPiece);

        // Update Move object to new piece
        move.setMovedPiece(newPiece);

    }

    /**
     * undos a move by moving the piece back to its original square and restoring
     * any captured piece
     *
     * @param move The Move that shows which piece is moving and where it goes
     */
    public void undoMove(Move move) {

        Coordinate from = move.getFrom();
        Coordinate to = move.getTo();
        Piece movedPiece = move.getMovedPiece();
        Piece capturedPiece = move.getCapturedPiece();

        // Check if promotion is valid
        if (move.getPromotionPiece() != null) {
            Piece originalPawn = new Pawn(movedPiece.getColor(), from);
            originalPawn.setHasMoved(true); // Must move to be promoted

            // 1. Put the original Pawn back to its source square
            setPieceAt(from.getRow(), from.getCol(), originalPawn);

            // 2. Put the captured piece back (if one existed)
            if (capturedPiece != null) {
                capturedPiece.setPosition(to);
            }
            setPieceAt(to.getRow(), to.getCol(), capturedPiece);

            // reset move object to pawn for redo
            move.setMovedPiece(originalPawn);
        } else {
            // Standard undo
            // 1. Put the moved piece back to its source square
            setPieceAt(from.getRow(), from.getCol(), movedPiece);

            // 2. Reset the piece's internal position
            movedPiece.setPosition(from);

            // 3. Put the captured piece back (if one existed)
            if (capturedPiece != null) {
                capturedPiece.setPosition(to);
            }
            setPieceAt(to.getRow(), to.getCol(), capturedPiece);

        }

        // ========= Castling undo ==========
        if (move.getMovedPiece() instanceof King && move.getRookFrom() != null) {
            Coordinate rook_from = move.getRookFrom();
            Coordinate rook_to = move.getRookTo();

            Piece rook = getPieceAt(rook_to.getRow(), rook_to.getCol());
            if(rook != null){
                setPieceAt(rook_from.getRow(), rook_from.getCol(), rook); // Move rook back
                setPieceAt(rook_to.getRow(), rook_to.getCol(), null);

                rook.moveTo(rook_from);
            }
        }

        // Note: If you needed to track hasMoved in simulate, you'd reset it here.
    }

    // === Debug and Utility ===

    /**
     * Returns a string representation of the board (for debugging).
     *
     * @return a visual representation of the board
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  a b c d e f g h\n");

        for (int r = ROWS - 1; r >= 0; r--) {
            sb.append(r + 1).append(" ");
            for (int c = 0; c < COLUMNS; c++) {
                Piece p = getPieceAt(r, c);
                if (p == null) {
                    sb.append(". ");
                } else {
                    String color = p.getColor().equals("White") ? "W" : "B";
                    String type = p.getClass().getSimpleName().substring(0, 1);
                    sb.append(type).append(color.charAt(0)).append(" ");
                }
            }
            sb.append(r + 1).append("\n");
        }

        sb.append("  a b c d e f g h\n");
        return sb.toString();
    }

    /**
     * Retrive the full grid os squares
     * 
     * @return all board squares.
     */
    public Square[][] getSquares() {
        return squares;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void setTutorialMode(boolean enable){
        this.tutorialMode = enable;
    }

}