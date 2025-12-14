package Chess;
import Chess.ui.ResourceManager;
import Chess.ui.ThemeManager;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Modern visual chess board that displays pieces using images
 * Properly integrated with Board move tracking
 *
 * @author Group3
 * @version 2.2
 */
public class GUIBoard extends JPanel {
    private final GameManager manager;
    private Board board;
    private JPanel boardPanel;
    private JPanel wrapper;
    private JLabel[][] squareLabels;
    private Square selectedSquare = null;
    private ThemeManager theme;
    // private String currentTurn = "White";
    private GameScreen gameScreen; // Reference to display moves
    private boolean previewMode = false;
    private boolean tutorialMode = false;

    /**
     * Constructor with GameScreen reference for move display
     */

    /**
     *
     * @param manager    the parent {@code GameManager} controlling game flow
     * @param board      the underlying {@code Board} model representing game state
     * @param gameScreen the screen used for displaying moves and turn changes,
     *                   or {@code null} if not required
     */
    public GUIBoard(GameManager manager, Board board, GameScreen gameScreen) {
        this.manager = manager;
        this.board = board;
        this.theme = ThemeManager.getInstance();
        this.gameScreen = gameScreen;
        //board.setGameScreen(gameScreen);
        squareLabels = new JLabel[8][8];

        theme.addListener(() -> {
            // System.out.println("GUIBoard: Theme changed, updating colors...");
            updateBackground();
            updateSquareColors();
            revalidate();
            repaint();
        });

        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(theme.getPrimaryBackground());

        createBoard();
        updateDisplay();
    }

    /**
     * Backward compatible constructor
     */

    /**
     * Creates a {@code GUIBoard} without a {@link GameScreen}
     *
     * @param manager the parent {@code GameManager}
     * @param board   the underlying {@code Board} model
     */
    public GUIBoard(GameManager manager, Board board, boolean preview) {
        this(manager, board, null);
        this.previewMode = preview;
        updateDisplay();
    }

    private void updateBackground() {
        setBackground(theme.getPrimaryBackground());
        if (wrapper != null) {
            wrapper.repaint();
        }
    }

    private void updateSquareColors() {
        if (squareLabels == null) {
            // System.out.println("squareLabels is null!");
            return;
        }

        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (squareLabels[file][rank] != null) {
                    boolean isLight = (file + rank) % 2 != 0;
                    Color newColor = isLight ? theme.getLightSquare() : theme.getDarkSquare();
                    squareLabels[file][rank].setBackground(newColor);
                }
            }
        }

        // System.out.println("Square colors updated!");
    }

    private void createBoard() {
        wrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(40, 40, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(new CompoundBorder(
                new LineBorder(new Color(60, 60, 60), 3, true),
                new EmptyBorder(15, 15, 15, 15)));

        boardPanel = new JPanel(new GridLayout(8, 8, 0, 0));
        boardPanel.setOpaque(false);
        boardPanel.setBorder(new LineBorder(new Color(50, 50, 50), 2));

        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                JLabel square = createSquare(file, rank);
                squareLabels[file][rank] = square;
                boardPanel.add(square);
            }
        }

        wrapper.add(boardPanel, BorderLayout.CENTER);

        JPanel fileLabels = new JPanel(new GridLayout(1, 8));
        fileLabels.setOpaque(false);
        fileLabels.setBorder(new EmptyBorder(5, 15, 0, 15));
        for (char c = 'a'; c <= 'h'; c++) {
            JLabel label = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            label.setForeground(new Color(150, 150, 150));
            label.setFont(ResourceManager.uiFont(14));
            fileLabels.add(label);
        }
        wrapper.add(fileLabels, BorderLayout.SOUTH);

        JPanel rankLabels = new JPanel(new GridLayout(8, 1));
        rankLabels.setOpaque(false);
        rankLabels.setBorder(new EmptyBorder(15, 0, 15, 5));
        for (int i = 8; i >= 1; i--) {
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            label.setForeground(new Color(150, 150, 150));
            label.setFont(ResourceManager.uiFont(14));
            rankLabels.add(label);
        }
        wrapper.add(rankLabels, BorderLayout.WEST);

        add(wrapper, BorderLayout.CENTER);
    }

    /**
     * Creates and configures a single square on the chessboard
     *
     * @param file the column index of the square (0–7)
     * @param rank the row index of the square (0–7)
     * @return a configured {@link JLabel} representing the board square
     */
    private JLabel createSquare(int file, int rank) {
        JLabel square = new JLabel();
        square.setOpaque(true);
        square.setHorizontalAlignment(SwingConstants.CENTER);
        square.setVerticalAlignment(SwingConstants.CENTER);
        square.setPreferredSize(new Dimension(60, 60));
        square.setBorder(BorderFactory.createEmptyBorder());

        boolean isLight = (file + rank) % 2 != 0;
        square.setBackground(isLight ? theme.getLightSquare() : theme.getDarkSquare());

        square.putClientProperty("file", file);
        square.putClientProperty("rank", rank);

        square.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!tutorialMode){
                    handleSquareClick(file, rank);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedSquare == null) {
                    square.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                square.setCursor(Cursor.getDefaultCursor());
            }
        });

        return square;
    }

    public JLabel getSquareLabel(int col, int row){
        return squareLabels[col][row];
    }
    /**
     * Handles square clicks - now properly integrates with Board's move tracking
     */

    /**
     * Handles user interaction when a square on the board is clicked
     *
     * @param file the file (column index 0–7) of the clicked square
     * @param rank the rank (row index 0–7) of the clicked square
     */
    private void handleSquareClick(int file, int rank) {
    ChessGame currentGame = manager.getCurrentGame();

    if (currentGame != null) {
        Player currentPlayer = currentGame.getCurrentPlayer();
        if (!(currentPlayer instanceof HumanPlayer)) {
            return; // it's AI's turn, ignore clicks
        }
    }

    Square clickedSquare = board.getSquare(file, rank);

    if (selectedSquare == null) {
        // First click - select piece
        if (clickedSquare != null && clickedSquare.getPiece() != null) {
            Piece piece = clickedSquare.getPiece();

            Player curPlayer = currentGame.getCurrentPlayer();
            if (!piece.getColor().equals(curPlayer.getColor())) {
                return;
            }

            selectedSquare = clickedSquare;
            clearHighlights();
            showPossibleMoves(clickedSquare);
        }
    } else {
        // Second click - move piece
        Piece piece = selectedSquare.getPiece();
        String choosePromotionPiece = null;
        if (piece != null) {
            // Promotion check and Prompt
            if (piece instanceof Pawn) {
                Pawn pawn = (Pawn) piece;
                if (pawn.promotionRank(rank)) {
                    choosePromotionPiece = promotionPiecePrompt();
                    if (choosePromotionPiece == null) {
                        selectedSquare = null;
                        clearHighlights();
                        updateDisplay();
                        return;
                    }
                }
            }

            // ============================================================
            // FIXED: Only call board.movePiece() to execute the move
            // Then add to game history WITHOUT re-executing
            // ============================================================
            boolean moved = board.movePiece(
                    selectedSquare.getRow(),
                    selectedSquare.getCol(),
                    clickedSquare.getRow(),
                    clickedSquare.getCol(),
                    choosePromotionPiece, true
            );

            if (moved) {
                System.out.println("SUCCESS: Human move executed on board");
                
                // Get the move that was just made
                Move performedMove = board.getLastMove();
                
                if (performedMove != null) {
                    // CRITICAL: Add to game history WITHOUT re-executing
                    // Use addMove() instead of makeMove()
                    currentGame.addMove(performedMove);
                    
                    System.out.println("Move added to game history. Total moves: " + 
                        currentGame.getMoveHistory().size());
                    
                    // Determine the piece used for notation
                    Piece notationPiece = performedMove.getMovedPiece();
                    
                    // Get coordinates for notation
                    Coordinate fromCoord = performedMove.getFrom();
                    Coordinate toCoord = performedMove.getTo();
                    
                    // Display move in algebraic notation
                    String moveNotation = convertToAlgebraicNotation(fromCoord, toCoord, notationPiece);
                    
                    // Promotion Suffix
                    if (choosePromotionPiece != null) {
                        moveNotation += "=" + getPieceSymbol(notationPiece);
                    }
                    
                    // Get current player BEFORE changing turn
                    String currentPlayerColor = currentGame.getCurrentPlayer().getColor();
                    
                    // Add move to display
                    gameScreen.addMove(moveNotation, currentPlayerColor);
                    
                    // Change turn
                    currentGame.changeTurn();
                    
                    // Update turn display
                    gameScreen.updateTurn(currentGame.getCurrentPlayer().getColor());
                    
                    // Check if game is over after the move
                    if (manager.checkAndHandleGameOver()) {
                        selectedSquare = null;
                        clearHighlights();
                        updateDisplay();
                        return;
                    }
                } else {
                    System.err.println("WARNING: board.getLastMove() returned null!");
                }
            }
        }
        
        selectedSquare = null;
        clearHighlights();
        updateDisplay();
        
        // Trigger AI move if it's AI's turn
        if (currentGame.getCurrentPlayer() instanceof AIPlayer) {
            new javax.swing.Timer(2000, e -> {
                ((javax.swing.Timer) e.getSource()).stop();
                manager.processAIMoves();
            }).start();
        }
    }
}
    /**
     * Prompt user to select a piece for promotion
     * 
     * @return string name of selected piece or null if cancelled
     */
    private String promotionPiecePrompt() {
        // Promotion options
        Object[] pieceOptions = { "Queen", "Rook", "Bishop", "Knight" };

        // Asker users using JOptionPane
        String selectPiece = (String) JOptionPane.showInputDialog(
                this,
                "Pawn has reached the End! Choose your new piece:",
                "Pawn Promotion",
                JOptionPane.QUESTION_MESSAGE,
                null,
                pieceOptions,
                pieceOptions[0] // Queen default
        );
        return selectPiece;
    }

    /**
     * Converts a move to algebraic notation
     */

    /**
     * Converts a move from coordinates to standard algebraic notation
     *
     * @param from  the starting {@link Coordinate} of the move
     * @param to    the ending {@link Coordinate} of the move
     * @param piece the {@link Piece} being moved
     * @return a {@link String} representing the move in algebraic notation
     */
    public String convertToAlgebraicNotation(Coordinate from, Coordinate to, Piece piece) {
        String toSquare = coordinateToNotation(to);
        String pieceName = getPieceSymbol(piece);

        // Check if there's a piece at destination (capture)
        Piece targetPiece = board.getPieceAt(to.getRow(), to.getCol());
        String capture = (targetPiece != null && !piece.isSameColor(targetPiece)) ? "x" : "";

        // Pawn moves don't include piece symbol
        if (piece instanceof Pawn) {
            if (!capture.isEmpty()) {
                String fromSquare = coordinateToNotation(from);
                return fromSquare.charAt(0) + "x" + toSquare;
            }
            return toSquare;
        }
        // Other pieces include their symbol
        return pieceName + capture + toSquare;
    }

    /**
     * Converts a coordinate to algebraic notation (e.g., "e4")
     */
    /**
     * Converts a board coordinate to algebraic notation
     *
     * @param coord the {@link Coordinate} to convert
     * @return a {@link String} representing the coordinate in algebraic notation
     */
    private String coordinateToNotation(Coordinate coord) {
        char file = (char) ('a' + coord.getCol());
        char rank = (char) ('1' + coord.getRow());
        return "" + file + rank;
    }

    /**
     * Gets the PGN symbol for a piece
     */
    /**
     * Returns the PGN symbol for a given chess piece
     *
     * @param piece the {@link Piece} to get the symbol for
     * @return a {@link String} representing the PGN symbol for the piece
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

    public ThemeManager getTheme(){
        return this.theme;
    }
    /**
     * Highlights all legal moves for a given piece on the board
     *
     * @param from the {@link Square} containing the piece to move
     */
    public void showPossibleMoves(Square from) {
        Piece piece = from.getPiece();
        if (piece == null) {
            return;
        }

        List<Coordinate> allowedMoves = piece.getLegalMoves(board);
        for (int i = 0; i < allowedMoves.size(); i++) {
            Coordinate coord = allowedMoves.get(i);
            int highlightRow = coord.getRow();
            int highlightCol = coord.getCol();

            Color highlightColor = theme.getHighlightSquare();
            squareLabels[highlightCol][highlightRow].setBackground(highlightColor);
        }
    }

    private void clearHighlights() {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                boolean isLight = (file + rank) % 2 != 0;
                squareLabels[file][rank].setBackground(isLight ? theme.getLightSquare() : theme.getDarkSquare());
            }
        }
    }

    public void updateDisplay() {
        updateSquareColors();

        // Clear all squares first
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (squareLabels[file][rank] != null) {
                    squareLabels[file][rank].setIcon(null);
                }
            }
        }

        // Display all pieces from current board state
        if (board != null) {

            for (int rank = 0; rank < 8; rank++) {
                for (int file = 0; file < 8; file++) {
                    Square square = board.getSquare(file, rank);

                    if (square != null && square.getPiece() != null && squareLabels[file][rank] != null) {
                        Piece piece = square.getPiece();

                        String pieceName = getPieceImageName(piece);
                        if (pieceName != null) {
                            try {
                                int size;
                                if(previewMode){
                                    size = 25;
                                }else{
                                    size=55;
                                }
                                ImageIcon icon = ResourceManager.piece(pieceName, size);
                                if (icon != null) {
                                    squareLabels[file][rank].setIcon(icon);

                                }
                            } catch (Exception e) {
                                System.err.println("Error loading piece image: " + pieceName);
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        } else {
            System.err.println("ERROR: Board is null!");
        }

        revalidate();
        repaint();
    }

    /**
     * Returns the file name for the image representing a given piece
     *
     * @param piece the {@link Piece} to get the image name for
     * @return the image file name as a {@link String}, or {@code null} if the piece
     *         is null or unknown
     */
    private String getPieceImageName(Piece piece) {
        if (piece == null)
            return null;

        String color = piece.getColor().equals("White") ? "W" : "B";
        String type = piece.getClass().getSimpleName().toLowerCase();

        switch (type) {
            case "pawn":
                return "pawn_" + color;
            case "rook":
                return "rook_" + color;
            case "knight":
                return "knight_" + color;
            case "bishop":
                return "bishop_" + color;
            case "queen":
                return "queen_" + color;
            case "king":
                return "king_" + color;
            default:
                return null;
        }
    }

    /**
     * Sets a new board and updates the display
     *
     * @param newBoard the {@link Board} to display
     */
    public void showBoard(Board newBoard) {
        this.board = newBoard;
        updateDisplay();
    }

    /**
     * Displays an informational message in a dialog box
     *
     * @param message the message text to display
     */
    public void showMessage(String message) {
        ModernDialog.showInfo(this, message, "Chess");
    }

    /**
     * Returns the current {@link Board} displayed by this GUI
     *
     * @return the current {@link Board} object
     */
    public Board getBoard() {
        return board;
    }

    public void setTutorialMode(boolean en){
        this.tutorialMode = en;
    }

    /**
     * Sets the board to a new {@link Board} instance
     *
     * @param board the {@link Board} to set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

}