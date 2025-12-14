package Chess.ui;
import Chess.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * The TutorialScreen shows a step-by-step guided tutorial
 * that teaches players the basic chess rules and piece movements
 *
 * @author Group 3
 */

public class TutorialScreen extends JPanel {
    private final GameManager manager;
    private final ThemeManager theme;
    private JTextArea lessonContent;
    private JLabel lessonCounter;
    private JLabel lessonTitle;
    private JLabel title;
    private int currentLesson = 0;
    private final int totalLessons = 8;
    private JPanel dotsPanel;
    private JButton prevBtn, nextBtn, finishBtn;
    private JLabel pieceIcon;
    private GUIBoard chessBoard;
    private Board board;
    private JPanel lessonCard;

    // Lesson data
    private static final String[] LESSON_TITLES = {
            "Welcome to Chess",
            "The Pawn",
            "The Rook",
            "The Knight",
            "The Bishop",
            "The Queen",
            "The King",
            "Special Moves: Castling"
    };

    private static final String[] LESSON_CONTENTS = {
            "Chess is a strategic board game played between two players. The objective is to checkmate your opponent's king, which means the king is under attack and cannot escape capture.",
            "Pawns move forward one square, but capture diagonally. On their first move, they can move two squares forward. Pawns are the most numerous pieces but also the least powerful.",
            "Rooks move horizontally or vertically any number of squares. They are powerful pieces for controlling files and ranks.",
            "Knights move in an \"L\" shape: two squares in one direction and one square perpendicular. They are the only pieces that can jump over other pieces.",
            "Bishops move diagonally any number of squares. Each player starts with two bishops: one on light squares and one on dark squares.",
            "The queen is the most powerful piece. She can move any number of squares horizontally, vertically, or diagonally.",
            "The king moves one square in any direction. Protecting your king is the most important objective. If your king is in checkmate, you lose the game!",
            "Castling is a special move involving the king and rook. The king moves two squares toward a rook, and the rook jumps over to the other side. This can only be done if neither piece has moved and there are no pieces between them."
    };

    private static final String[] PIECE_IMAGES = {
            null, "pawn_W", "rook_W", "knight_W", "bishop_W", "queen_W", "king_W", null
    };

    /**
     * Creates a new tutorial screen
     * 
     * @param manager the main manager that handles screen switching
     */
    public TutorialScreen(GameManager manager) {
        this.manager = manager;
        this.theme = ThemeManager.getInstance();

        setLayout(new BorderLayout());
        setBackground(theme.getPrimaryBackground());

        // Initialize board
        board = new Board(null);

        // Listen for theme changes
        theme.addListener(this::updateTheme);

        // === Top Bar with Back Button ===
        add(UIComponents.createTopBar("  Back to Menu", () -> manager.showScreen("menu")), BorderLayout.NORTH);

        // === Main Content Area ===
        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setOpaque(false);
        mainContent.setBorder( 
                new EmptyBorder(theme.getSpacingM(), theme.getSpacingXL(), theme.getSpacingXL(), theme.getSpacingXL()));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, theme.getSpacingL());

        // === Left Side: Title + Chess Board ===
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);

        // Title and Lesson Counter
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, theme.getSpacingM(), 0));

        title = UIComponents.createTitle("Chess Tutorial");
        headerPanel.add(title, BorderLayout.WEST);

        lessonCounter = new JLabel("Lesson 1 of 8");
        lessonCounter.setForeground(theme.getPrimaryText()); // mini box backgrounc color
        lessonCounter.setFont(theme.getBodyFont());
        lessonCounter.setBackground(theme.getSecondaryBackground());
        lessonCounter.setOpaque(true);
        lessonCounter.setBorder(new EmptyBorder(8, 16, 8, 16));
        headerPanel.add(lessonCounter, BorderLayout.EAST);

        leftPanel.add(headerPanel, BorderLayout.NORTH);

        // Chess Board with pieces
        JPanel boardWrapper = new JPanel(new GridBagLayout());
        boardWrapper.setOpaque(false);

        chessBoard = new GUIBoard(manager, board, false);
        chessBoard.setPreferredSize(new Dimension(520, 520));
        chessBoard.setMaximumSize(new Dimension(520, 520));

        boardWrapper.add(chessBoard);
        leftPanel.add(boardWrapper, BorderLayout.CENTER);

        mainContent.add(leftPanel, gbc);

        // === Right Side: Lesson Card ===
        gbc.gridx++;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainContent.add(createLessonCard(), gbc);

        add(mainContent, BorderLayout.CENTER);

        // Initialize first lesson
        updateLesson();
    }

    /**
     * Builds the right-hand lesson card panel containing text, images, and
     * navigation
     * 
     * @return a styled panel containing the tutorial lesson UI
     */
    private JPanel createLessonCard() {
        lessonCard = new JPanel() {
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
        lessonCard.setOpaque(false);
        lessonCard.setLayout(new BorderLayout());
        lessonCard.setBorder(new CompoundBorder(
                new LineBorder(theme.getBorder(), 2, true),
                new EmptyBorder(40, 40, 30, 40)));
        lessonCard.setPreferredSize(new Dimension(500, 0));
        lessonCard.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        // Center: Lesson content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);

        // Icon, title, and piece
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        // Left: Icon and Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titlePanel.setOpaque(false);

        // icon switch between light and dark based on theme
        JLabel iconLabel = new JLabel(ResourceManager.icon("book", 28)); 
        titlePanel.add(iconLabel);

        lessonTitle = UIComponents.createHeading("Welcome to Chess");
        titlePanel.add(lessonTitle);

        headerPanel.add(titlePanel, BorderLayout.WEST);

        // Right: Piece icon (large)
        pieceIcon = new JLabel();
        pieceIcon.setBorder(new EmptyBorder(0, 0, 0, 0));
        headerPanel.add(pieceIcon, BorderLayout.EAST);

        contentPanel.add(headerPanel, BorderLayout.NORTH);

        // Lesson text
        lessonContent = new JTextArea();
        lessonContent.setEditable(false);
        lessonContent.setLineWrap(true);
        lessonContent.setWrapStyleWord(true);
        lessonContent.setFont(theme.getBodyFont());
        lessonContent.setForeground(theme.getSecondaryText());
        lessonContent.setBackground(theme.getCardBackground());
        lessonContent.setBorder(new EmptyBorder(10, 0, 20, 0));

        JScrollPane scrollPane = new JScrollPane(lessonContent);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        contentPanel.add(scrollPane, BorderLayout.CENTER);

        lessonCard.add(contentPanel, BorderLayout.CENTER);

        // Bottom: Navigation
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(theme.getSpacingM(), 0, 0, 0));

        // Navigation buttons
        JPanel navPanel = new JPanel(new GridLayout(1, 2, theme.getSpacingS(), 0));
        navPanel.setOpaque(false);

        // Previous button
        prevBtn = UIComponents.createSecondaryButton("Previous", ResourceManager.icon("arrow", 16));
        prevBtn.setEnabled(false);
        prevBtn.addActionListener(e -> previousLesson());

        // Next button
        nextBtn = UIComponents.createPrimaryButton("Next", ResourceManager.icon("arrow", 16));
        nextBtn.addActionListener(e -> nextLesson());

        navPanel.add(prevBtn);
        navPanel.add(nextBtn);

        bottomPanel.add(navPanel, BorderLayout.NORTH);

        // Finish Tutorial button (initially hidden)
        finishBtn = UIComponents.createSecondaryButton("Finish Tutorial");
        finishBtn.setVisible(false);
        finishBtn.addActionListener(e -> manager.showScreen("menu"));
        bottomPanel.add(finishBtn, BorderLayout.SOUTH);

        // Progress dots
        dotsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 15));
        dotsPanel.setOpaque(false);
        updateDots();
        bottomPanel.add(dotsPanel, BorderLayout.CENTER);

        lessonCard.add(bottomPanel, BorderLayout.SOUTH);

        return lessonCard;
    }

    private void updateDots() {
        dotsPanel.removeAll();
        for (int i = 0; i < totalLessons; i++) {
            final int index = i;
            JPanel dot = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(index == currentLesson ? theme.getPrimaryButton() : theme.getAccent());
                    g2.fillOval(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            };
            dot.setOpaque(false);
            dot.setPreferredSize(new Dimension(10, 10));
            dotsPanel.add(dot);
        }
        dotsPanel.revalidate();
        dotsPanel.repaint();
    }

    private void previousLesson() {
        if (currentLesson > 0) {
            currentLesson--;
            updateLesson();
        }
    }

    private void nextLesson() {
        if (currentLesson < totalLessons - 1) {
            currentLesson++;
            updateLesson();
        }
    }

    private void updateLesson() {
        lessonCounter.setText("Lesson " + (currentLesson + 1) + " of " + totalLessons);
        lessonTitle.setText(LESSON_TITLES[currentLesson]);
        lessonContent.setText(LESSON_CONTENTS[currentLesson]);

        // Update piece icon on the right
        if (PIECE_IMAGES[currentLesson] != null) {
            pieceIcon.setIcon(ResourceManager.piece(PIECE_IMAGES[currentLesson], 80));
        } else {
            pieceIcon.setIcon(null);
        }

        // Update the chess board based on lesson
        updateBoardForLesson(currentLesson);

        // Update button states
        prevBtn.setEnabled(currentLesson > 0);

        // Show/hide next and finish buttons
        boolean isLastLesson = currentLesson == totalLessons - 1;
        nextBtn.setVisible(!isLastLesson);
        finishBtn.setVisible(isLastLesson);

        // Update dots
        updateDots();
    }

    /**
     * Changes the chessboard pieces to match the lesson topic
     *
     * @param lesson the index of the current lesson
     */
    private void updateBoardForLesson(int lesson) {
        board.clear();
        board.setTutorialMode(true);
        chessBoard.setTutorialMode(true);

        Piece pieceToHighlight =  null;
        switch (lesson) {
            case 0:
                board.initializeBoard();
                break;
            case 1:
                board.setPieceAt(1, 4, new Pawn("White", new Coordinate(1, 4)));
                board.setPieceAt(3, 3, new Pawn("Black", new Coordinate(3, 3)));
                board.setPieceAt(3, 5, new Pawn("Black", new Coordinate(3, 5)));
                pieceToHighlight = board.getPieceAt(1, 4);
                break;
            case 2:
                board.setPieceAt(4, 4, new Rook("White", new Coordinate(4, 4)));
                pieceToHighlight = board.getPieceAt(4, 4);
                break;
            case 3:
                board.setPieceAt(4, 4, new Knight("White", new Coordinate(4, 4)));
                pieceToHighlight = board.getPieceAt(4, 4);
                break;
            case 4:
                board.setPieceAt(4, 4, new Bishop("White", new Coordinate(4, 4)));
                pieceToHighlight = board.getPieceAt(4, 4);
                break;
            case 5:
                board.setPieceAt(4, 4, new Queen("White", new Coordinate(4, 4)));
                pieceToHighlight = board.getPieceAt(4, 4);
                break;
            case 6:
                board.setPieceAt(4, 4, new King("White", new Coordinate(4, 4)));
                pieceToHighlight = board.getPieceAt(4, 4);
                break;
            case 7:
                board.setPieceAt(0, 4, new King("White", new Coordinate(0, 4)));
                board.setPieceAt(0, 7, new Rook("White", new Coordinate(0, 7)));
                break;
        }

        chessBoard.updateDisplay();
        if(pieceToHighlight != null){
            chessBoard.showPossibleMoves(board.getSquare(pieceToHighlight.getPosition().getCol(), pieceToHighlight.getPosition().getRow()));
        }
        //hardcoded highlight specifically for casteling
        if(lesson == 7){
            chessBoard.getSquareLabel(5, 0).setBackground(chessBoard.getTheme().getHighlightSquare());
            chessBoard.getSquareLabel(6, 0).setBackground(chessBoard.getTheme().getHighlightSquare());
        }
    }

    private void updateTheme() 
    {
        setBackground(theme.getPrimaryBackground());
        lessonCounter.setForeground(theme.getPrimaryText());
        lessonCounter.setFont(theme.getBodyFont());
        lessonCounter.setBackground(theme.getSecondaryBackground());
        lessonContent.setFont(theme.getBodyFont());
        lessonContent.setForeground(theme.getSecondaryText());
        lessonContent.setBackground(theme.getCardBackground());
        lessonCard.repaint();
        updateDots();
        revalidate();
        repaint();
    }
}