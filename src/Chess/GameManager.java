package Chess;
import Chess.ui.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Coordinates the overall chess game flow and manages all GUI screens
 *
 * @author Group3
 * @version 1.0
 */
public class GameManager {

    private ChessGame currentGame;
    private GameScreen gameScreen;
    private GUIBoard guiBoard;
    private FileHandler fileHandler;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    /**
     * Constructs a new GameManager and initializes the FileHandler.
     */
    public GameManager() {
        fileHandler = new FileHandler();
    }

    /**
     * Launches the main GUI application window and registers all screen panels.
     */
    public void launchUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        if (GraphicsEnvironment.isHeadless()) {
                System.out.println("[TEST MODE] UI launch skipped.");
                return;
            }


        mainFrame = new JFrame("Chess Game Management System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1400, 900);

        MainMenu.GradientHost root = new MainMenu.GradientHost();
        root.setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);

        MainMenu mainMenu = new MainMenu(new MainMenu.Callbacks() {
            @Override
            public void onNewGame() {
                showScreen("newGame");
            }

            @Override
            public void onLoadGame() {
                showScreen("loadGame");
            }

            @Override
            public void onTutorial() {
                showScreen("tutorial");
            }

            @Override
            public void onSettings() {
                showScreen("settings");
            }

            @Override
            public void onHelp() {
                showScreen("help");
            }
        });

        mainPanel.add(mainMenu, "menu");
        mainPanel.add(new NewGameSetup(this), "newGame");
        mainPanel.add(new LoadGameScreen(this), "loadGame");
        mainPanel.add(new TutorialScreen(this), "tutorial");
        mainPanel.add(new SettingsScreen(this), "settings");
        mainPanel.add(new HelpScreen(this), "help");

        root.add(mainPanel, BorderLayout.CENTER);
        mainFrame.setContentPane(root);

        mainFrame.setMinimumSize(new Dimension(1024, 720));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        showScreen("menu");
    }

    /**
     * Switches the currently visible screen by name using CardLayout.
     *
     * @param screenName the name of the screen to display
     */
    public void showScreen(String screenName) {
        try {
            cardLayout.show(mainPanel, screenName);
        } catch (IllegalArgumentException e) {
            System.err.println("ERROR: Screen not found: " + screenName);
            e.printStackTrace();
        }
    }

    /**
     * Starts the game and displays loaded moves and board.
     * Called after setCurrentGame() when loading a game.
     * Uses the stored gameScreen reference.
     */
public void startGame() {
    if (currentGame == null) {
        System.err.println("ERROR: No current game set!");
        return;
    }
    
    if (gameScreen == null) {
        System.err.println("ERROR: GameScreen is not set!");
        return;
    }

    Board board = currentGame.getBoard();
    gameScreen.getChessBoard().setBoard(board);
    
    // DON'T clear move history here - it has the loaded moves!
    // gameScreen.clearMoveHistory();  <- REMOVE THIS LINE IF IT'S THERE
    
    List<Move> moveHistory = currentGame.getMoveHistory();
    
    System.out.println("DEBUG: startGame() called with " + 
        moveHistory.size() + " moves in history");  // ADD THIS
    
    if (moveHistory != null && moveHistory.size() > 0) {
        // Display loaded moves
        gameScreen.clearMoveHistory();  // Clear DISPLAY only
        
        for (int i = 0; i < moveHistory.size(); i++) {
            Move move = moveHistory.get(i);
            String player = (i % 2 == 0) ? "White" : "Black";
            String notation = convertMoveToNotation(move);
            gameScreen.addMove(notation, player);
        }
        
        String currentTurn = (moveHistory.size() % 2 == 0) ? "White" : "Black";
        gameScreen.updateTurn(currentTurn);
    }
    
    gameScreen.getChessBoard().updateDisplay();
    gameScreen.updateBoardDisplay();
}
    /**
     * Converts a Move object to algebraic notation (e.g., "e4", "Nf3", "exd5").
     *
     * @param move the {@link Move} to convert
     * @return the algebraic notation string representing the move
     */
    private String convertMoveToNotation(Move move) {
        Coordinate to = move.getTo();
        Piece piece = move.getMovedPiece();

        char file = (char) ('a' + to.getCol());
        char rank = (char) ('1' + to.getRow());
        String destination = "" + file + rank;

        String symbol = getPieceSymbol(piece);
        String capture = (move.getCapturedPiece() != null) ? "x" : "";

        if (piece instanceof Pawn) {
            if (!capture.isEmpty()) {
                char fromFile = (char) ('a' + move.getFrom().getCol());
                return fromFile + "x" + destination;
            }
            return destination;
        }

        return symbol + capture + destination;
    }

    /**
     * Gets the PGN symbol for a piece.
     *
     * @param piece the {@link Piece} to get the symbol for
     * @return the character symbol for the piece (K, Q, R, B, N, or empty string
     *         for pawns)
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
     * Starts a new standard chess game session.
     *
     * @param aiLevel     the AI difficulty level (1-10)
     * @param gameMode    the type of game mode (e.g., "Human vs Human", "Human vs
     *                    AI", "AI vs AI")
     * @param playerWhite true if the player is playing as white, false if playing
     *                    as black
     */
    public void startGame(int aiLevel, String gameMode, boolean playerWhite) {
        Player whitePlayer = null;
        Player blackPlayer = null;

        switch (gameMode) {
            case "Human vs Human":
                whitePlayer = new HumanPlayer("White", "White");
                blackPlayer = new HumanPlayer("Black", "Black");
                break;
            case "Human vs AI":
                if (playerWhite) {
                    whitePlayer = new HumanPlayer("White", "White");
                    blackPlayer = new AIPlayer("Computer", "Black", aiLevel);
                } else {
                    whitePlayer = new AIPlayer("White", "White", aiLevel);
                    blackPlayer = new HumanPlayer("Black", "Black");
                }
                break;
            case "AI vs AI":
                whitePlayer = new AIPlayer("White", "White", aiLevel);
                blackPlayer = new AIPlayer("Black", "Black", aiLevel);
                break;
        }

        Board board = new Board(gameScreen);
        board.initializeBoard();

        List<Player> players = new ArrayList<>();
        players.add(whitePlayer);
        players.add(blackPlayer);
        this.currentGame = new ChessGame(players);
        this.currentGame.setBoard(board);

        this.gameScreen = new GameScreen(this, board, aiLevel);
        this.setGameScreen(this.gameScreen);

        mainPanel.add(gameScreen, "game");
        showScreen("game");

        if (gameMode.equals("AI vs AI")) {
            javax.swing.Timer aiStartTimer = new javax.swing.Timer(2000, e -> {
                ((javax.swing.Timer) e.getSource()).stop();
                processAIMoves(); // begin the first AI move
            });
            aiStartTimer.setRepeats(false);
            aiStartTimer.start();
        }

        if (gameMode.equals("Human vs AI") && currentGame.getCurrentPlayer() instanceof AIPlayer) {
            javax.swing.Timer aiStartTimer = new javax.swing.Timer(2000, e -> {
                ((javax.swing.Timer) e.getSource()).stop();
                processAIMoves(); // ai makes it's move
            });
            aiStartTimer.setRepeats(false);
            aiStartTimer.start();
        }
    }

    /**
     * Handles a move event coming from the GUI.
     * <p>
     * Delegates legality checking to {@link ChessGame#makeMove(Move)}.
     * If successful, updates the board view.
     * </p>
     *
     * @param move the {@link Move} being attempted by the player
     */
    public void makeMove(Move move) {
        if (currentGame != null && currentGame.makeMove(move)) {
            if (this.guiBoard != null) {
                this.guiBoard.updateDisplay();
            }
        }
    }

    // -------------------------------------------------------------------------
    // File Handling (Save / Load) - PGN Format Support
    // -------------------------------------------------------------------------

    /**
     * Saves the current game to a PGN file with complete game state.
     * 
     * <p>
     * Automatically appends .pgn extension if not present. Saves game mode,
     * AI difficulty level, player colors, and all moves made.
     * </p>
     *
     * @param filename the path where the game should be saved
     */
    public void saveGame(String filename) {
        if (!filename.toLowerCase().endsWith(".pgn")) {
            filename = filename + ".pgn";
        }
        
    
        
        List<Move> history = currentGame.getMoveHistory();
        
        
        // Show all moves
        for (int i = 0; i < history.size(); i++) {
            Move m = history.get(i);
            String player = (i % 2 == 0) ? "White" : "Black";
           
        }
//System.out.println("==================================\n");
        
        String gameMode = determineGameMode();
        int aiLevel = getAILevel();
        boolean playerWhite = isPlayerWhite();
        
        fileHandler.saveGame(history, filename, gameMode, aiLevel, playerWhite);
        System.out.println("Saved to: " + filename);
    }


    /**
     * Determines the game mode based on the types of current players.
     *
     * @return the game mode string ("Human vs Human", "Human vs AI", or "AI vs AI")
     */
    private String determineGameMode() {
        if (currentGame == null) return "Human vs Human";
        
        List<Player> players = currentGame.getPlayers();
        if (players == null || players.size() < 2) return "Human vs Human";
        
        Player white = players.get(0);
        Player black = players.get(1);
        
        boolean whiteIsAI = white instanceof AIPlayer;
        boolean blackIsAI = black instanceof AIPlayer;
        
        if (!whiteIsAI && !blackIsAI) {
            return "Human vs Human";
        } else if (whiteIsAI && blackIsAI) {
            return "AI vs AI";
        } else {
            return "Human vs AI";
        }
    }

    /**
     * Gets the AI difficulty level from the current game.
     * 
     * <p>
     * Searches through players to find an AI player and returns its difficulty level.
     * Returns 5 (medium difficulty) as default if no AI player is found.
     * </p>
     *
     * @return the AI difficulty level (1-10), or 5 if no AI player exists
     */
    private int getAILevel() {
        if (currentGame == null) return 5;
        
        List<Player> players = currentGame.getPlayers();
        if (players == null) return 5;
        
        for (Player p : players) {
            if (p instanceof AIPlayer) {
                return ((AIPlayer) p).getDifficultyLevel();
            }
        }
        return 5;
    }

    /**
     * Checks if the human player is playing as white in Human vs AI mode.
     * 
     * <p>
     * For Human vs Human and AI vs AI modes, this always returns true.
     * </p>
     *
     * @return true if the white player is human, false otherwise
     */
    private boolean isPlayerWhite() {
        if (currentGame == null) return true;
        
        List<Player> players = currentGame.getPlayers();
        if (players == null || players.isEmpty()) return true;
        
        Player white = players.get(0);
        
        return white instanceof HumanPlayer;
    }
  

    /**
     * Loads a previously saved game from a PGN file with complete state restoration.
     * 
     * <p>
     * This method:
     * <ol>
     *   <li>Loads the game file and extracts all metadata</li>
     *   <li>Recreates the board position by applying all moves</li>
     *   <li>Restores player types (Human/AI) and AI difficulty</li>
     *   <li>Creates the game screen and displays the loaded state</li>
     *   <li>If it's AI's turn, automatically triggers the AI to move</li>
     * </ol>
     * </p>
     *
     * @param filename the path of the file to load (e.g., "saved_game.pgn")
     * @return true if the game was loaded successfully, false otherwise
     */
    public boolean loadGame(String filename) {
        FileHandler.GameState gameState = fileHandler.loadGame(filename);
        
        if (gameState == null) {
            System.err.println("Failed to load game from: " + filename);
            return false;
        }
        
        this.currentGame = gameState.game;
        
        // Recreate GameScreen with loaded state
        this.gameScreen = new GameScreen(this, gameState.game.getBoard(), gameState.aiLevel);
        this.setGameScreen(this.gameScreen);
        
        // CRITICAL: Set the GUIBoard reference
        this.setGUIBoard(this.gameScreen.getChessBoard());
        
        mainPanel.add(gameScreen, "game");
        
      
        
        // Start the game with loaded state (displays moves and board)
        startGame();
        
        showScreen("game");
        
        // If it's AI's turn after loading, trigger AI move after a delay
        if (currentGame.getCurrentPlayer() instanceof AIPlayer) {
            javax.swing.Timer aiStartTimer = new javax.swing.Timer(2000, e -> {
                ((javax.swing.Timer)e.getSource()).stop();
                processAIMoves();
            });
            aiStartTimer.setRepeats(false);
            aiStartTimer.start();
        }
        
        return true;
    }
    /**
     * Alternative load method that shows a file chooser dialog.
     * Allows the user to browse and select a PGN file to load.
     */
    public void loadGameWithDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PGN Files (*.pgn)", "pgn"));

        int result = fileChooser.showOpenDialog(mainFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            loadGame(filePath);
        }
    }

    /**
     * Alternative save method that shows a file chooser dialog.
     */
    public void saveGameWithDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PGN Files (*.pgn)", "pgn"));

        int result = fileChooser.showSaveDialog(mainFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            saveGame(filePath);
        }
    }


    /**
     * Processes AI moves with proper null checking.
     * Handles continuous AI play for AI vs AI mode.
     */
    public void processAIMoves() {
    if(currentGame == null || gameScreen == null){
        return;
    }
    if(currentGame.getCurrentPlayer() instanceof AIPlayer){
        AIPlayer ai = (AIPlayer) currentGame.getCurrentPlayer();
        
       
        Move aiMove = ai.makeMove(currentGame.getBoard());
        if(aiMove != null){
           
            // CRITICAL: Execute the move on the board FIRST
            Board board = currentGame.getBoard();
            boolean moved = board.movePiece(
                aiMove.getFrom().getRow(),
                aiMove.getFrom().getCol(),
                aiMove.getTo().getRow(),
                aiMove.getTo().getCol(),
                null, true
            );
            
            if (moved) {
                
                
                // THEN add to history
                currentGame.addMove(aiMove);
                
                
                
                // Change turn
                currentGame.changeTurn();
                
                // Update GUI display
                if(guiBoard != null){
                    guiBoard.updateDisplay();
                } else if(gameScreen != null && gameScreen.getChessBoard() != null) {
                    gameScreen.getChessBoard().updateDisplay();
                }
                
                // Get move notation
                String moveAlgebricNotation;
                if(guiBoard != null) {
                    moveAlgebricNotation = guiBoard.convertToAlgebraicNotation(
                        aiMove.getFrom(), aiMove.getTo(), aiMove.getMovedPiece());
                } else {
                    moveAlgebricNotation = gameScreen.getChessBoard().convertToAlgebraicNotation(
                        aiMove.getFrom(), aiMove.getTo(), aiMove.getMovedPiece());
                }
                
                // Show move in UI
                gameScreen.addMove(moveAlgebricNotation, ai.getColor());
                
                // Update turn display
                gameScreen.updateTurn(currentGame.getCurrentPlayer().getColor());
                
                // Check if game is over
                if (checkAndHandleGameOver()) {
                    return;
                }
            } else {
                System.out.println("ERROR: AI move failed to execute on board");
            }
        }
    
        // Check if next player is also AI
        if (currentGame.getCurrentPlayer() instanceof AIPlayer) {
            new javax.swing.Timer(2000, e -> {
                ((javax.swing.Timer)e.getSource()).stop();
                processAIMoves();
            }).start();
        }
    }
}
    // Game  Overwriter Here 

    /**
     * Checks if the game is over and displays the result
     * @return true if game is over, false otherwise
     */
    public boolean checkAndHandleGameOver() {
        if (currentGame == null) {
            return false;
        }
        
        Player currentPlayer = currentGame.getCurrentPlayer();
        RuleEngine ruleEngine = currentGame.getRuleEngine();
        
        // Check for checkmate
        if (ruleEngine.isCheckmate(currentPlayer)) {
            String winner = currentPlayer.getColor().equals("White") ? "Black" : "White";
            showGameOverDialog("Checkmate!", winner + " wins!");
            return true;
        }
        
        // Check for stalemate
        if (ruleEngine.isStalemate(currentPlayer)) {
            showGameOverDialog("Stalemate!", "The game is a draw!");
            return true;
        }
        
        return false;
    }

    /**
     * Displays game over dialog and offers options
     */
    private void showGameOverDialog(String title, String message) {
        int choice = JOptionPane.showOptionDialog(
            mainFrame,
            message + "\n\nWhat would you like to do?",
            title,
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new Object[]{"New Game", "Main Menu", "Exit"},
            "New Game"
        );
        
        switch (choice) {
            case 0: // New Game
                showScreen("newGame");
                break;
            case 1: // Main Menu
                showScreen("menu");
                break;
            case 2: // Exit
                System.exit(0);
                break;
        }
    }


    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    /**
     * Sets the current game (used when loading from PGN).
     *
     * @param game the {@link ChessGame} to set as current
     */
    public void setCurrentGame(ChessGame game) {
        this.currentGame = game;
    }

    /**
     * Sets the game screen reference. Called from LoadGameScreen.
     *
     * @param gameScreen the {@link GameScreen} to set
     */
    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Sets the GUI board reference.
     *
     * @param guiBoard the {@link GUIBoard} to set
     */
    public void setGUIBoard(GUIBoard guiBoard) {
        this.guiBoard = guiBoard;
    }

    /**
     * Gets the GUI board used to display the game.
     *
     * @return the {@link GUIBoard} used to display the game
     */
    public GUIBoard getGuiBoard() {
        return this.guiBoard;
    }

    /**
     * Gets the chess game currently being played.
     *
     * @return the {@link ChessGame} currently being played
     */
    public ChessGame getCurrentGame() {
        return this.currentGame;
    }

    /**
     * Gets the game screen.
     *
     * @return the {@link GameScreen} used to display the game
     */
    public GameScreen getGameScreen() {
        return this.gameScreen;
    }

    /**
     * Gets the main application frame.
     *
     * @return the main {@link JFrame}
     */
    public JFrame getMainFrame() {
        return this.mainFrame;
    }

    /**
     * Gets the main panel that holds all screens.
     *
     * @return the {@link JPanel} that holds all screens
     */
    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    /**
     * Gets the CardLayout managing screen navigation.
     *
     * @return the {@link CardLayout} managing screen navigation
     */
    public CardLayout getCardLayout() {
        return this.cardLayout;
    }

    /**
     * Gets the file handler used to manage game files.
     *
     * @return the {@link FileHandler} used to manage game files
     */
    public FileHandler getFileHandler() {
        return this.fileHandler;
    }
}