package Chess;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading chess games in enhanced PGN format with game mode metadata.
 * 
 * <p>
 * This class extends the standard PGN format to include additional metadata about:
 * <ul>
 *   <li>Game mode (Human vs Human, Human vs AI, AI vs AI)</li>
 *   <li>AI difficulty level (1-10)</li>
 *   <li>Player color assignments</li>
 * </ul>
 * </p>
 * 
 * @author Group3
 * @version 2.0
 */

/**
 *
 * @author Group3
 * @version 1.0
 */
public class FileHandler {

    /**
     * Saves a game to a PGN file with complete game state including metadata.
     * 
     * <p>
     * The saved file includes standard PGN tags plus custom tags for game mode,
     * AI difficulty level, and player color assignments.
     * </p>
     *
     * @param moveHistory the list of moves made in the game
     * @param filename the path where the game should be saved
     * @param gameMode the game mode ("Human vs Human", "Human vs AI", or "AI vs AI")
     * @param aiLevel the AI difficulty level (1-10)
     * @param playerWhite true if the human player is playing as white, false otherwise
     */
public void saveGame(List<Move> moveHistory, String filename, String gameMode, int aiLevel, boolean playerWhite) {
    // System.out.println("\nFileHandler.saveGame() called");
    // System.out.println("Filename: " + filename);
    // System.out.println("Game Mode: " + gameMode);
    // System.out.println("AI Level: " + aiLevel);
    // System.out.println("Player White: " + playerWhite);
    // System.out.println("Moves to save: " + (moveHistory != null ? moveHistory.size() : "NULL!"));
    
    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
        // Standard PGN headers
        writer.println("[Event \"Chess Game\"]");
        writer.println("[Site \"?\"]");
        writer.println("[Date \"" + new java.util.Date() + "\"]");
        writer.println("[White \"Player 1\"]");
        writer.println("[Black \"Player 2\"]");
        
        // Custom metadata for game restoration
        writer.println("[GameMode \"" + gameMode + "\"]");
        writer.println("[AILevel \"" + aiLevel + "\"]");
        writer.println("[PlayerWhite \"" + playerWhite + "\"]");
        writer.println();
        
        // Write moves in standard PGN format
        if (moveHistory != null && !moveHistory.isEmpty()) {
            System.out.println("Writing moves to PGN file:");
            
            int moveNumber = 1;
            for (int i = 0; i < moveHistory.size(); i++) {
                if (i % 2 == 0) {
                    writer.print(moveNumber + ". ");
                }
                
                Move move = moveHistory.get(i);
                
                String notation = convertToAlgebraicNotation(move);
                
                
                writer.print(notation + " ");
                
                if (i % 2 == 1) {
                    writer.println();
                    moveNumber++;
                }
            }
            
            // Add newline if last move was by white
            if (moveHistory.size() % 2 == 1) {
                writer.println();
            }
            
            System.out.println("Successfully wrote " + moveHistory.size() + " moves to file");
        } else {
            System.out.println("WARNING: No moves to save! moveHistory is " + 
                (moveHistory == null ? "null" : "empty"));
        }
        
        writer.println("*");
  
    } catch (IOException e) {
        System.err.println("ERROR: Error saving game: " + e.getMessage());
        e.printStackTrace();
    }
}
    
    /**
     * Loads a game from a PGN file and reconstructs the complete game state.
     * 
     * <p>
     * This method:
     * <ol>
     *   <li>Parses the PGN file to extract metadata and moves</li>
     *   <li>Creates appropriate Player objects based on game mode</li>
     *   <li>Applies all moves to recreate the board position</li>
     *   <li>Returns a GameState object containing all necessary information</li>
     * </ol>
     * </p>
     *
     * @param filename the path of the PGN file to load
     * @return a {@link GameState} object containing the loaded game, or null if loading fails
     */
    public GameState loadGame(String filename) {
        
        Board board = new Board(null);
        board.initializeBoard();
        
        // Default values if metadata is missing
        String gameMode = "Human vs Human";
        int aiLevel = 5;
        boolean playerWhite = true;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            StringBuilder pgnContent = new StringBuilder();
            
            // Read entire file and extract metadata
            while ((line = reader.readLine()) != null) {
                // Parse custom metadata tags
                if (line.startsWith("[GameMode")) {
                    gameMode = extractTagValue(line);
                } else if (line.startsWith("[AILevel")) {
                    try {
                        aiLevel = Integer.parseInt(extractTagValue(line));
                    } catch (NumberFormatException e) {
                        aiLevel = 5;
                    }
                } else if (line.startsWith("[PlayerWhite")) {
                    playerWhite = Boolean.parseBoolean(extractTagValue(line));
                }
                pgnContent.append(line).append("\n");
            }
            reader.close();

            String pgn = pgnContent.toString();
            
            // Create players based on saved game mode
            Player whitePlayer = null;
            Player blackPlayer = null;
            
            switch(gameMode) {
                case "Human vs Human":
                    whitePlayer = new HumanPlayer("White", "White");
                    blackPlayer = new HumanPlayer("Black", "Black");
                    break;
                case "Human vs AI":
                    if (playerWhite) {
                        whitePlayer = new HumanPlayer("White", "White");
                        blackPlayer = new AIPlayer("Computer", "Black", aiLevel);
                    } else {
                        whitePlayer = new AIPlayer("Computer", "White", aiLevel);
                        blackPlayer = new HumanPlayer("Black", "Black");
                    }
                    break;
                case "AI vs AI":
                    whitePlayer = new AIPlayer("White", "White", aiLevel);
                    blackPlayer = new AIPlayer("Black", "Black", aiLevel);
                    break;
                default:
                    whitePlayer = new HumanPlayer("White", "White");
                    blackPlayer = new HumanPlayer("Black", "Black");
                    break;
            }
            
            List<Player> players = new ArrayList<>();
            players.add(whitePlayer);
            players.add(blackPlayer);
            
            ChessGame game = new ChessGame(players);
            game.setBoard(board);
            
            // Parse and apply all moves from PGN
            List<String> moveStrings = extractMoves(pgn);

        
            
            for (int i = 0; i < moveStrings.size(); i++) {
        String moveStr = moveStrings.get(i);
        String playerColor = (i % 2 == 0) ? "White" : "Black";
        
        Move move = convertAlgebraicToMove(moveStr, board, playerColor);
        
        if (move != null) {
            Piece piece = board.getPieceAt(
                move.getFrom().getRow(), 
                move.getFrom().getCol()
            );
            
            if (piece != null) {
                board.setPieceAt(move.getFrom().getRow(), move.getFrom().getCol(), null);
                board.setPieceAt(move.getTo().getRow(), move.getTo().getCol(), piece);
                piece.setPosition(move.getTo());
    
                game.addMove(move);
            
            }
        }
    }
            // Return complete game state for restoration
            return new GameState(game, gameMode, aiLevel, playerWhite);
            
        } catch (IOException e) {
            System.err.println("Error reading PGN file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
        
    /**
     * Extracts the value from a PGN tag.
     * 
     * <p>
     * Example: {@code [GameMode "Human vs AI"]} returns {@code "Human vs AI"}
     * </p>
     *
     * @param line the PGN tag line to parse
     * @return the extracted value, or an empty string if parsing fails
     */
    private String extractTagValue(String line) {
        int start = line.indexOf('"');
        int end = line.lastIndexOf('"');
        if (start != -1 && end != -1 && start < end) {
            return line.substring(start + 1, end);
        }
        return "";
    }

    /**
     * Extracts move strings from PGN content by removing metadata and move numbers.
     *
     * @param pgn the complete PGN content as a string
     * @return a list of move strings in algebraic notation
     */

    /**
     * Extracts chess moves from PGN file content
     * 
     * @param pgn the complete PGN file content as a string
     * @return a list of move strings in algebraic notation
     */
    private List<String> extractMoves(String pgn) {
        List<String> moves = new ArrayList<>();

        String[] lines = pgn.split("\n");
        StringBuilder moveSection = new StringBuilder();
        
        // Extract only the move section (not metadata tags)
        for (String line : lines) {
            line = line.trim();
            if (!line.startsWith("[") && !line.isEmpty()) {
                moveSection.append(line).append(" ");
            }
        }

        String moves_part = moveSection.toString();
        
        // Remove move numbers and game result markers
        moves_part = moves_part.replaceAll("\\d+\\.", " ");
        moves_part = moves_part.replaceAll("\\d+\\.\\.\\.\\s*", " ");
        moves_part = moves_part.replaceAll("\\s*\\*\\s*", " ");
        moves_part = moves_part.replaceAll("\\s*1-0\\s*", " ");
        moves_part = moves_part.replaceAll("\\s*0-1\\s*", " ");
        moves_part = moves_part.replaceAll("\\s*1/2-1/2\\s*", " ");
        
        // Split by whitespace to get individual moves
        String[] tokens = moves_part.split("\\s+");

        for (String token : tokens) {
            token = token.trim();
            if (!token.isEmpty()) {
                moves.add(token);
            }
        }

        return moves;
    }

    /**
     * Converts algebraic notation to a Move object.
     * 
     * <p>
     * Handles various notation formats:
     * <ul>
     *   <li>Pawn moves: "e4", "exd5"</li>
     *   <li>Piece moves: "Nf3", "Bxc4"</li>
     *   <li>Castling: "O-O", "O-O-O"</li>
     * </ul>
     * </p>
     *
     * @param notation the move in algebraic notation
     * @param board the current board state
     * @param playerColor the color of the player making the move
     * @return a {@link Move} object, or null if the notation cannot be parsed
     */
    private Move convertAlgebraicToMove(String notation, Board board, String playerColor) {
        String move = notation.trim();

        if (move.isEmpty())
            return null;

        // Handle castling
        if (move.equals("O-O") || move.equals("0-0")) {
            return handleCastling(board, playerColor, true);
        }

        if (move.equals("O-O-O") || move.equals("0-0-0")) {
            return handleCastling(board, playerColor, false);
        }

        // Get destination square (last 2 characters)
        if (move.length() < 2) {
            System.out.println("    Move too short: " + move);
            return null;
        }
        
        char destFile = move.charAt(move.length() - 2);
        char destRank = move.charAt(move.length() - 1);
        
        System.out.println("    Dest file: " + destFile + ", dest rank: " + destRank);
        
        if (!Character.isLetter(destFile) || !Character.isDigit(destRank)) {
            System.out.println("    Invalid destination format");
            return null;
        }
        
        int destCol = destFile - 'a';      // a-h → 0-7
        int destRow = destRank - '1';      // 1-8 → 0-7
        
     
        Coordinate to = new Coordinate(destRow, destCol);

        // Determine piece type from first character
        char firstChar = move.charAt(0);

        
        if (Character.isLowerCase(firstChar)) {
            // Pawn move
           
            Move pawnMove = handlePawnMove(board, move, destCol, destRow, playerColor);
            if (pawnMove != null) {
                System.out.println("    Pawn move found!");
            } else {
                System.out.println("    Pawn move NOT found");
            }
            return pawnMove;
        } else {
 
            return handlePieceMove(board, firstChar, to, playerColor);
        }
    }

    /**
     * Handles pawn move notation parsing.
     * 
     * <p>
     * Examples: "e4" (simple move), "exd5" (capture)
     * </p>
     *
     * @param board the current board state
     * @param move the move notation
     * @param destCol the destination column (0-7)
     * @param destRow the destination row (0-7)
     * @param playerColor the color of the player making the move
     * @return a {@link Move} object representing the pawn move, or null if not found
     */

    private Move handlePawnMove(Board board, String move, int destCol, int destRow, String playerColor) {
        System.out.println("      handlePawnMove: move=" + move + ", destCol=" + destCol + ", destRow=" + destRow + ", color=" + playerColor);
        
        Coordinate to = new Coordinate(destRow, destCol);
        int direction = playerColor.equals("White") ? 1 : -1;
        
        // Check for capture (contains 'x')
        if (move.contains("x")) {
            // Capture move like "exd5"
            int sourceCol = move.charAt(0) - 'a';
            int sourceRow = destRow - direction;
            
            if (sourceRow < 0 || sourceRow >= 8) {
                return null;
            }
            
            Piece pawn = board.getPieceAt(sourceRow, sourceCol);
            if (pawn instanceof Pawn && pawn.getColor().equals(playerColor)) {
                Piece captured = board.getPieceAt(destRow, destCol);
                return new Move(new Coordinate(sourceRow, sourceCol), to, pawn, captured);
            }
            
            return null;
        }
    

        int oneSquareBack = destRow - direction;
        if (oneSquareBack >= 0 && oneSquareBack < 8) {
            Piece piece = board.getPieceAt(oneSquareBack, destCol);
            
            if (piece instanceof Pawn && piece.getColor().equals(playerColor)) {
                // Check if destination is empty
                if (board.getPieceAt(destRow, destCol) == null) {
                    System.out.println("      Found pawn at row " + oneSquareBack + " (one square back)");
                    return new Move(new Coordinate(oneSquareBack, destCol), to, piece, null);
                }
            }
        }
        
        // Try two squares back (for initial double move)
        int twoSquaresBack = destRow - (direction * 2);
        if (twoSquaresBack >= 0 && twoSquaresBack < 8) {
            Piece piece = board.getPieceAt(twoSquaresBack, destCol);
            
            if (piece instanceof Pawn && piece.getColor().equals(playerColor)) {
                // Check both destination and path are empty
                boolean pathClear = (board.getPieceAt(oneSquareBack, destCol) == null) &&
                                (board.getPieceAt(destRow, destCol) == null);
                
                if (pathClear) {
                    // Verify this is from initial position
                    int initialRow = playerColor.equals("White") ? 1 : 6;
                    if (twoSquaresBack == initialRow) {
                        System.out.println("      Found pawn at row " + twoSquaresBack + " (two squares back - initial move)");
                        return new Move(new Coordinate(twoSquaresBack, destCol), to, piece, null);
                    }
                }
            }
        }
        
    
        for (int r = 0; r < 8; r++) {
            Piece p = board.getPieceAt(r, destCol);
            if (p != null) {
                System.out.println("        Row " + r + ": " + p.getClass().getSimpleName() + " (" + p.getColor() + ")");
            }
        }

        return null;
    }  

    /**
     * Handles piece move notation parsing (Knight, Bishop, Rook, Queen, King).
     * 
     * <p>
     * Examples: "Nf3", "Bxc4", "Qd8"
     * </p>
     *
     * @param board the current board state
     * @param pieceSymbol the piece symbol ('N', 'B', 'R', 'Q', 'K')
     * @param to the destination coordinate
     * @param playerColor the color of the player making the move
     * @return a {@link Move} object representing the piece move, or null if not found
     */
    /**
     * Handles pawn moves in algebraic notation
     * 
     * @param board       the current board state
     * @param move        the pawn move notation
     * @param destCol     the destination column
     * @param destRow     the destination row
     * @param playerColor the color of the pawn
     * @return a {@link Move} object for the pawn move, or {@code null} if
     *         no valid pawn is found
     */
    private Move handlePieceMove(Board board, char pieceSymbol, Coordinate to, String playerColor) {
        // Find piece of this type that can move to destination
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(row, col);

                if (piece != null && piece.getColor().equals(playerColor)) {
                    // Check if this piece matches the symbol
                    if (matchesPieceSymbol(piece, pieceSymbol)) {
                        // Check if this piece can legally move to destination
                        List<Coordinate> legalMoves = piece.getLegalMoves(board);
                        if (legalMoves.contains(to)) {
                            return new Move(new Coordinate(row, col), to, piece, null);
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Checks if a piece matches the given PGN symbol.
     *
     * @param piece the piece to check
     * @param symbol the PGN symbol ('N', 'B', 'R', 'Q', 'K')
     * @return true if the piece matches the symbol, false otherwise
     */

    /**
     * Checks if a piece matches the given algebraic notation symbol
     *
     * @param piece  the piece to check
     * @param symbol the PGN symbol character
     * @return {@code true} if the piece matches the symbol, {@code false} otherwise
     */
    private boolean matchesPieceSymbol(Piece piece, char symbol) {
        switch (symbol) {
            case 'N':
                return piece instanceof Knight;
            case 'B':
                return piece instanceof Bishop;
            case 'R':
                return piece instanceof Rook;
            case 'Q':
                return piece instanceof Queen;
            case 'K':
                return piece instanceof King;
            default:
                return false;
        }
    }

    /**
     * Handles castling move notation parsing.
     * 
     * <p>
     * "O-O" or "0-0" represents king-side castling.
     * "O-O-O" or "0-0-0" represents queen-side castling.
     * </p>
     *
     * @param board the current board state
     * @param playerColor the color of the player castling
     * @param kingSide true for king-side castling, false for queen-side
     * @return a {@link Move} object representing the king's castling move, or null if king not found
     */
    private Move handleCastling(Board board, String playerColor, boolean kingSide) {
        // Find the king
        King king = null;
        int kingCol = -1;

        for (int col = 0; col < 8; col++) {
            Piece p = board.getPieceAt(playerColor.equals("White") ? 0 : 7, col);
            if (p instanceof King && p.getColor().equals(playerColor)) {
                king = (King) p;
                kingCol = col;
                break;
            }
        }

        if (king == null)
            return null;

        int row = playerColor.equals("White") ? 0 : 7;
        Coordinate kingFrom = new Coordinate(row, kingCol);
        Coordinate kingTo;

        if (kingSide) {
            kingTo = new Coordinate(row, 6); // g-file
        } else {
            kingTo = new Coordinate(row, 2); // c-file
        }

        return new Move(kingFrom, kingTo, king, null);
    }

    /**
     * Converts a Move object to algebraic notation.
     *
     * @param move the move to convert
     * @return the algebraic notation string (e.g., "e4", "Nf3", "Qxd8")
     */
    private String convertToAlgebraicNotation(Move move) {
        Coordinate to = move.getTo();
        char file = (char) ('a' + to.getCol());
        char rank = (char) ('1' + to.getRow());

        Piece piece = move.getMovedPiece();
        String symbol = getPieceSymbol(piece);

        return symbol + file + rank;
    }

    /**
     * Gets the PGN symbol for a piece.
     *
     * @param piece the piece to get the symbol for
     * @return the PGN symbol ('K', 'Q', 'R', 'B', 'N', or empty string for pawns)
     */
    private String getPieceSymbol(Piece piece) {
        if (piece instanceof King)
            return "K";
        if (piece instanceof Queen)
            return "Q";
        if (piece instanceof Rook)
            return "R";
        if (piece instanceof Bishop)
            return "B";
        if (piece instanceof Knight)
            return "N";
        if (piece instanceof Pawn)
            return "";
        return "";
    }
    
    /**
     * Represents the complete state of a loaded chess game.
     * 
     * <p>
     * This class encapsulates all information needed to restore a game
     * to its exact state at the time of saving, including player types,
     * AI difficulty, and color assignments.
     * </p>
     */
    public static class GameState {
        /** The loaded chess game with board state and move history */
        public ChessGame game;
        
        /** The game mode (e.g., "Human vs Human", "Human vs AI", "AI vs AI") */
        public String gameMode;
        
        /** The AI difficulty level (1-10) */
        public int aiLevel;
        
        /** True if the human player is playing as white, false otherwise */
        public boolean playerWhite;
        
        /**
         * Constructs a new GameState with all necessary restoration information.
         *
         * @param game the loaded chess game
         * @param gameMode the game mode
         * @param aiLevel the AI difficulty level
         * @param playerWhite true if human plays as white
         */
        public GameState(ChessGame game, String gameMode, int aiLevel, boolean playerWhite) {
            this.game = game;
            this.gameMode = gameMode;
            this.aiLevel = aiLevel;
            this.playerWhite = playerWhite;
        }
    }
}