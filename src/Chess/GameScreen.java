package Chess;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import Chess.ui.ResourceManager;
import Chess.ui.ThemeManager;
import Chess.ui.UIComponents;

/**
 * Main game screen with the chess board and game controls
 * Updated to show whose turn it is and format move history properly
 *
 * @author Group3
 * @version 2.1
 */
public class GameScreen extends JPanel {
    private final GameManager manager;
    private final ThemeManager theme;
    private GUIBoard chessBoard;
    private Board board;
    private int aiLevel;
    private JLabel turnLabel;
    private JLabel aiLevelLabel;
    private JLabel movesLabel;
    private JLabel halfMoveLabel;
    private JPanel whiteCapturesPanel;
    private JPanel blackCapturesPanel;
    private JPanel moveHistoryPanel;
    private ArrayList<String> moveHistory;
    private ArrayList<String> redoHistory = new ArrayList<>();
    private int moveCount = 0;
    private int moveNumber = 1; // Move number (1. e4 2. c5, etc.)
    private int halfMoveCount = 0;

    /**
     * Counter for the half-move clock used in fifty-move rule detection
     * Constructs a new {@code GameScreen} with the specified game manager/board
     * <p>
     * Initializes the UI components, sets up the layout with top bar, main game
     * area,
     * and information panels, and registers a theme change listener.
     * </p>
     *
     * @param manager the game manager controlling screen navigation; must not be
     *                null
     * @param board   the chess board to display; may be null (board will not be
     *                initialized)
     *                Constructor for GameScreen
     */

    public GameScreen(GameManager manager, Board board, int aiLev) {
        this.manager = manager;
        this.board = board;
        board.setGameScreen(this);
        this.aiLevel = aiLev;
        this.setLayout(new BorderLayout());
        this.theme = ThemeManager.getInstance();
        this.moveHistory = new ArrayList<>();

        setBackground(theme.getPrimaryBackground());
        theme.addListener(this::updateTheme);

        chessBoard = new GUIBoard(manager, board, this);

        manager.setGameScreen(this);
        manager.setGUIBoard(chessBoard);

        add(createTopBar(), BorderLayout.NORTH);
        add(createMainGameArea(), BorderLayout.CENTER);
    }

    /**
     * Creates the top navigation bar with back button and status indicators.
     * <p>
     * The top bar contains a "Main Menu" back button on the left and turn/AI level
     * indicators on the right. Clicking the back button shows a confirmation dialog
     * before returning to the main menu.
     * </p>
     *
     * @return a panel containing the top bar components
     */
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(20, 40, 10, 40));

        String arrowIcon = theme.isDarkMode() ? "whiteArrow" : "blackArrow"; // Arrow color based on theme
        JButton backBtn = new JButton("  Main Menu", ResourceManager.icon(arrowIcon, 18)); 
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        // backBtn.setForeground(new Color(90, 90, 90));
        backBtn.setFont(ResourceManager.uiFont(13));
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                backBtn.setForeground(theme.getPrimaryText()); // Main menu defalt text color
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                backBtn.setForeground(theme.getSecondaryText()); // Main menu hover text color
            }
        });

        backBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to leave the game?",
                    "Leave Game",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                manager.showScreen("menu");
            }
        });

        topBar.add(backBtn, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        turnLabel = createStyledButton("Turn: White", false);
        rightPanel.add(turnLabel);

        aiLevelLabel = createStyledButton("AI Level: " + this.aiLevel + "/10", false);
        rightPanel.add(aiLevelLabel);

        topBar.add(rightPanel, BorderLayout.EAST);

        return topBar;
    }

    /**
     * Creates a styled button-like label
     *
     * @param text   the text to display
     * @param filled whether the button is filled or outlined
     * @return a styled JLabel acting as a button
     */
    private JLabel createStyledButton(String text, boolean filled) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (filled) {
                    g2.setColor(Color.WHITE);
                } else {
                    g2.setColor(theme.getSecondaryBackground());
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                if (!filled) {
                    g2.setColor(theme.getBorder());
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setForeground(filled ? Color.BLACK : theme.getPrimaryText());
        label.setFont(theme.getBodyFont());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(new EmptyBorder(8, 20, 8, 20));
        label.setOpaque(false);
        return label;
    }

    /**
     * Creates the main game area containing the chess board and information panels
     * <p>
     * The main area uses a BorderLayout with the chess board and action buttons
     * centered, and game information cards positioned on the right
     * </p>
     *
     * @return a panel containing the main game components
     */

    private JPanel createMainGameArea() {
        JPanel mainArea = new JPanel(new BorderLayout(30, 0));
        mainArea.setOpaque(false);
        mainArea.setBorder(new EmptyBorder(20, 40, 40, 40));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        JPanel boardWrapper = new JPanel(new GridBagLayout());
        boardWrapper.setOpaque(false);

        // chessBoard = new GUIBoard(manager, board, this);
        chessBoard.setPreferredSize(new Dimension(520, 520));
        chessBoard.setMaximumSize(new Dimension(520, 520));

        boardWrapper.add(chessBoard);
        leftPanel.add(boardWrapper, BorderLayout.CENTER);
        leftPanel.add(createActionButtons(), BorderLayout.SOUTH);

        mainArea.add(leftPanel, BorderLayout.CENTER);
        mainArea.add(createRightPanel(), BorderLayout.EAST);

        return mainArea;
    }

    /**
     * Creates the action buttons panel below the chess board
     * 
     * @return a configured {@link JPanel} containing all action buttons
     */
    private JPanel createActionButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setOpaque(false);

        JButton undoBtn = createIconButton("Undo", "undo", 16);
        JButton redoBtn = createIconButton("Redo", "redo", 16);
        JButton saveBtn = createIconButton("Save", "save", 16);
        JButton hintBtn = createIconButton("Hint", "hint", 16);
        JButton resignBtn = createIconButton("Resign", "resign", 16);
        resignBtn.setForeground(new Color(244, 67, 54));

        undoBtn.addActionListener(e -> {
            if (board.undoLastMove()) {
                if (!moveHistory.isEmpty()) {
                    String undoMove = moveHistory.remove(moveHistory.size() - 1);
                    redoHistory.add(undoMove);
                    updateMoveHistory();
                }
                chessBoard.updateDisplay();
                chessBoard.revalidate();
                chessBoard.repaint();
                ModernDialog.showInfo(this, "Last move undone!", "Undo");
            } else {
                ModernDialog.showWarning(this, "No Moves to undo!", "Undo");
            }
        });

        redoBtn.addActionListener(e -> {
            if (board.redoLastMove()) {
                if (!redoHistory.isEmpty()) {
                    String redoMove = redoHistory.remove(redoHistory.size() - 1);
                    moveHistory.add(redoMove);
                    updateMoveHistory();
                }
                chessBoard.updateDisplay();
                chessBoard.revalidate();
                chessBoard.repaint();
                ModernDialog.showInfo(this, "Last move redone!", "Redo");
            } else {
                ModernDialog.showWarning(this, "No Moves to redo!", "Redo");
            }
        });

        saveBtn.addActionListener(e -> {
            try {
                manager.saveGameWithDialog();
                ModernDialog.showInfo(this, "Game saved successfully in PGN format!", "Save");
            } catch (Exception ex) {
                ModernDialog.showWarning(this, "Error saving game: " + ex.getMessage(), "Save Error");
                ex.printStackTrace();
            }
        });

        /*
         * hintBtn.addActionListener(e -> {
         * ModernDialog.showInfo(this, "Hint: Develop your pieces!", "Hint");
         * });
         */

        // ======Adding update to Hint ========

        hintBtn.addActionListener(e -> {
            try {
                GetHint getHint = new GetHint(board);
                ChessGame currentGame = manager.getCurrentGame();
                if (currentGame != null) {
                    String playerColor = currentGame.getCurrentPlayer().getColor();
                    String hint = getHint.generateHint(playerColor);
                    ModernDialog.showInfo(this, hint, "Hint for " + playerColor);
                } else {
                    ModernDialog.showInfo(this, "No active game to provide hints for.", "Hint");
                }
            } catch (Exception ex) {
                ModernDialog.showWarning(this, "Could not generate hint: " + ex.getMessage(), "Hint Error");
                ex.printStackTrace();
            }
        });

        resignBtn.addActionListener(e -> {
            if (ModernDialog.showConfirm(this, "Are you sure you want to resign?",
                    "Resign Game") == JOptionPane.YES_OPTION) {
                ModernDialog.showWarning(this, "You resigned. Game over!", "Game Over");
                manager.showScreen("menu");
            }
        });

        buttonPanel.add(undoBtn);
        buttonPanel.add(redoBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(hintBtn);
        buttonPanel.add(resignBtn);

        return buttonPanel;
    }

    /**
     * Creates an icon button with hover effects
     * 
     * @param text     button text
     * @param iconName name of the icon resource
     * @param iconSize size of the icon
     * @return configured JButton
     */
    private JButton createIconButton(String text, String iconName, int iconSize) {
        JButton btn = new JButton(text, ResourceManager.icon(iconName, iconSize));
        btn.setForeground(theme.getSecondaryText());
        btn.setFont(theme.getSmallFont());
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setIconTextGap(5);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setForeground(theme.getPrimaryText());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setForeground(theme.getSecondaryText());
            }
        });
        return btn;
    }

    /**
     * Creates the right-side panel containing captured pieces, move history, and
     * game info
     * 
     * @return the right panel with game information cards
     */
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(280, 0));

        rightPanel.add(createCapturedPiecesCard());
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(createMoveHistoryCard());
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(createGameInfoCard());
        rightPanel.add(Box.createVerticalGlue());

        return rightPanel;
    }

    /**
     * Creates the captured pieces card
     * 
     * @return the captured pieces panel
     */
    private JPanel createCapturedPiecesCard() {
        JPanel card = createCard();
        card.setMaximumSize(new Dimension(280, 200));

        JLabel title = UIComponents.createHeading("Captured Pieces");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(15));

        JLabel whiteCapturesLabel = new JLabel("White's Captures");
        whiteCapturesLabel.setForeground(theme.getSecondaryText());
        whiteCapturesLabel.setFont(theme.getSmallFont());
        whiteCapturesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(whiteCapturesLabel);
        card.add(Box.createVerticalStrut(5));

        whiteCapturesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        whiteCapturesPanel.setOpaque(false);
        whiteCapturesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        whiteCapturesPanel.setMaximumSize(new Dimension(240, Integer.MAX_VALUE));
        card.add(whiteCapturesPanel);
        card.add(Box.createVerticalStrut(15));

        JLabel blackCapturesLabel = new JLabel("Black's Captures");
        blackCapturesLabel.setForeground(theme.getSecondaryText());
        blackCapturesLabel.setFont(theme.getSmallFont());
        blackCapturesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(blackCapturesLabel);
        card.add(Box.createVerticalStrut(5));

        blackCapturesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        blackCapturesPanel.setOpaque(false);
        blackCapturesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        blackCapturesPanel.setMaximumSize(new Dimension(240, Integer.MAX_VALUE));
        card.add(blackCapturesPanel);

        return card;
    }

    /**
     * Creates the move history card
     * 
     * @return the move history panel
     */
    private JPanel createMoveHistoryCard() {
        JPanel card = createCard();
        card.setPreferredSize(new Dimension(280, 250));
        card.setMaximumSize(new Dimension(280, 250));

        JLabel title = UIComponents.createHeading("Move History");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(15));

        moveHistoryPanel = new JPanel();
        moveHistoryPanel.setLayout(new BoxLayout(moveHistoryPanel, BoxLayout.Y_AXIS));
        moveHistoryPanel.setOpaque(false);

        JLabel noMovesLabel = new JLabel("No moves yet");
        noMovesLabel.setForeground(theme.getSecondaryText());
        noMovesLabel.setFont(theme.getSmallFont());
        noMovesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        moveHistoryPanel.add(noMovesLabel);

        JScrollPane scrollPane = new JScrollPane(moveHistoryPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(240, 160));
        scrollPane.setPreferredSize(new Dimension(240, 160));

        card.add(scrollPane);

        return card;
    }

    /**
     * Creates the game info card
     * 
     * @return the game info panel
     */
    private JPanel createGameInfoCard() {
        JPanel card = createCard();
        card.setMaximumSize(new Dimension(280, 150));

        JLabel title = UIComponents.createHeading("Game Info");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(15));

        addInfoLine(card, "Mode: Human vs AI");

        movesLabel = new JLabel("Moves: 0");
        movesLabel.setForeground(theme.getSecondaryText());
        movesLabel.setFont(theme.getBodyFont());
        movesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(movesLabel);

        return card;
    }

    /**
     * Creates a styled card panel with rounded corners and border
     * 
     * @return the styled card panel
     */
    private JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.getCardBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), theme.getBorderRadius(), theme.getBorderRadius());
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 2, true),
                new EmptyBorder(20, 20, 20, 20)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        return card;
    }

    /**
     * Adds an information line to the game info card
     * 
     * @param panel the card panel to add the line to
     * @param text  the information text
     */
    private void addInfoLine(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(theme.getSecondaryText());
        label.setFont(theme.getBodyFont());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }

    /**
     * Adds a move with proper PGN-style formatting
     * Shows who made the move (White or Black)
     * 
     * Example output:
     * "1. e4" for White's first move
     * "1... c5" for Black's first move
     * "2. Nf3" for White's second move
     * 
     * @param move   the move in algebraic notation (e.g., "e4")
     * @param player the player who made the move ("White" or "Black")
     */
    public void addMove(String move, String player) {
        // Format in PGN style
        String formattedMove;
        if (player.equals("White")) {
            formattedMove = moveNumber + ". " + move;
        } else {
            formattedMove = moveNumber + "... " + move;
            // moveNumber++; // Increment only after Black moves
        }

        moveHistory.add(formattedMove);
        redoHistory.clear();
        updateMoveHistory();
        moveCount++;
        updateMoveCount();
    }

    /**
     * Legacy method for compatibility (assumes White)
     */

    /**
     * Adds a move to the history assuming the move was made by White.
     *
     * @param move the algebraic notation of the move to add
     */
    public void addMove(String move) {
        addMove(move, "White");
    }

    /**
     * Adds a captured piece icon to the appropriate capture panel.
     *
     * @param piece   the piece identifier
     * @param isWhite {@code true} if the captured piece belonged to White,
     *                {@code false} if it belonged to Black
     */
    public void addCapturedPiece(String piece, boolean isWhite) {
        JLabel pieceLabel = new JLabel();
        ImageIcon icon = ResourceManager.piece(piece, 18);
        pieceLabel.setIcon(icon);
        if (isWhite) {
            whiteCapturesPanel.add(pieceLabel);
        } else {
            blackCapturesPanel.add(pieceLabel);
        }

        whiteCapturesPanel.revalidate();
        whiteCapturesPanel.repaint();
        blackCapturesPanel.revalidate();
        blackCapturesPanel.repaint();
    }

    /**
     * Updates the move history display panel
     */
    private void updateMoveHistory() {
        moveHistoryPanel.removeAll();

        if (moveHistory.isEmpty()) {
            JLabel noMovesLabel = new JLabel("No moves yet");
            noMovesLabel.setForeground(theme.getSecondaryText());
            noMovesLabel.setFont(theme.getSmallFont());
            noMovesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            moveHistoryPanel.add(noMovesLabel);
        } else {
            // Display moves in pairs (White and Black on same line)
            for (int i = 0; i < moveHistory.size(); i += 2) {
                JPanel moveRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
                moveRow.setOpaque(false);
                moveRow.setAlignmentX(Component.LEFT_ALIGNMENT);
                moveRow.setMaximumSize(new Dimension(240, 20));

                // White move
                JLabel whiteMove = new JLabel(moveHistory.get(i));
                whiteMove.setForeground(theme.getSecondaryText());
                whiteMove.setFont(theme.getSmallFont());
                moveRow.add(whiteMove);

                // Black move (if exists)
                if (i + 1 < moveHistory.size()) {
                    JLabel blackMove = new JLabel(moveHistory.get(i + 1));
                    blackMove.setForeground(theme.getSecondaryText());
                    blackMove.setFont(theme.getSmallFont());
                    moveRow.add(blackMove);
                }

                moveHistoryPanel.add(moveRow);
            }
        }

        moveHistoryPanel.revalidate();
        moveHistoryPanel.repaint();
    }

    /**
     * Clears the move history and resets counters
     * Called when loading a new game
     */
    public void clearMoveHistory() {
        moveHistory.clear();
        redoHistory.clear();
        moveCount = 0;
        moveNumber = 1;
        halfMoveCount = 0;
        updateMoveHistory();
        updateMoveCount();
        resetHalfMove();
        System.out.println(" Move history cleared");
    }

    /**
     * Updates the board display
     * Call this after loading a game to show the final position
     */
    public void updateBoardDisplay() {
        if (chessBoard != null) {
            chessBoard.updateDisplay();
            chessBoard.revalidate();
            chessBoard.repaint();
            System.out.println(" Board display updated");
        }
    }

    private void updateMoveCount() {
        movesLabel.setText("Moves: " + moveCount);
    }

    /**
     * Updates the turn indicator label
     * Called from GUIBoard when a move is made
     * 
     * @param turn the player whose turn it is ("White" or "Black")
     */
    public void updateTurn(String turn) {
        turnLabel.setText("Turn: " + turn);
        turnLabel.repaint();
    }

    /**
     * Increments the half-move clock and updates the display
     */
    public void incrementHalfMove() {
        halfMoveCount++;
        halfMoveLabel.setText("Half-move Clock: " + halfMoveCount);
    }

    /**
     * Resets the half-move clock to zero and updates the display
     */
    public void resetHalfMove() {
        halfMoveCount = 0;
        halfMoveLabel.setText("Half-move Clock: 0");
    }

    /**
     * Updates the theme of the game screen components
     */
    private void updateTheme() {
        setBackground(theme.getPrimaryBackground());

        if (turnLabel != null) {
            turnLabel.repaint();
        }
        if (aiLevelLabel != null) {
            aiLevelLabel.repaint();
        }

        revalidate();
        repaint();
    }

    public GUIBoard getChessBoard() {
        return this.chessBoard; // needed the current instance of the board to correctly update the random/ai
                                // moves generated
    }
}