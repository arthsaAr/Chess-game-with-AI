package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Before;
import org.junit.Test;

import edu.kingsu.SoftwareEngineering.Chess.innerclasses.HelperGameManager;

import static org.junit.Assert.*;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.CardLayout;
import java.awt.HeadlessException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GameManagerTest {

    private HelperGameManager manager;

    private void initializeManagerUI() throws Exception {
       try {
            manager = new HelperGameManager();
            Field mainFrameField = GameManager.class.getDeclaredField("mainFrame");
            mainFrameField.setAccessible(true);
            
            JFrame frame = new JFrame();
            frame.setUndecorated(true); // Helps with headless mode
            mainFrameField.set(manager, frame);

            Field mainPanelField = GameManager.class.getDeclaredField("mainPanel");
            mainPanelField.setAccessible(true);
            JPanel panel = new JPanel(new CardLayout());
            mainPanelField.set(manager, panel);

            Field cardLayoutField = GameManager.class.getDeclaredField("cardLayout");
            cardLayoutField.setAccessible(true);
            cardLayoutField.set(manager, panel.getLayout());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to initialize UI: " + e.getClass().getName() + " - " + e.getMessage(), e);
        }
    }

    @Before
    public void setUp() {
        try {
            System.setProperty("java.awt.headless", "true");
            manager = new HelperGameManager();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Setup failed: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }

    @Test
    public void testConstructorInitializesFileHandler() {
        assertNotNull(manager.getFileHandler());
    }

    @Test
    public void testStartGameHumanVsHuman() {
        try {
            initializeManagerUI();
            manager.startGame(3, "Human vs Human", true);
            
            assertNotNull(manager.getCurrentGame());
            assertNotNull(manager.getGameScreen());
            
            List<Player> players = manager.getCurrentGame().getPlayers();
            assertEquals(2, players.size());
            assertTrue(players.get(0) instanceof HumanPlayer);
            assertTrue(players.get(1) instanceof HumanPlayer);
            assertEquals("White", players.get(0).getColor());
            assertEquals("Black", players.get(1).getColor());
        } catch (Exception e) {
            e.printStackTrace(); // ADD THIS
            fail("Test failed: " + e.getClass().getName() + " - " + e.getMessage());
        }
    }

    @Test
    public void testStartGameHumanVsAI() {
        try {
            initializeManagerUI();
            manager.startGame(5, "Human vs AI", true);
            
            List<Player> players = manager.getCurrentGame().getPlayers();
            assertTrue(players.get(0) instanceof HumanPlayer);
            assertTrue(players.get(1) instanceof AIPlayer);
            assertEquals(5, ((AIPlayer)players.get(1)).getDifficultyLevel());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testStartGameAIVsAI() {
        try {
            initializeManagerUI();
            manager.startGame(4, "AI vs AI", true);
            
            List<Player> players = manager.getCurrentGame().getPlayers();
            assertTrue(players.get(0) instanceof AIPlayer);
            assertTrue(players.get(1) instanceof AIPlayer);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testStartGameCreatesInitializedBoard() {
        try {
            initializeManagerUI();
            manager.startGame(3, "Human vs Human", true);
            
            Board board = manager.getCurrentGame().getBoard();
            assertNotNull(board);
            
            // Verify key pieces are in starting positions
            assertNotNull(board.getPieceAt(0, 0)); // White rook
            assertNotNull(board.getPieceAt(0, 4)); // White king
            assertNotNull(board.getPieceAt(7, 4)); // Black king
            assertTrue(board.getPieceAt(1, 0) instanceof Pawn); // White pawn
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testMakeMoveUpdatesGameState() {
        try {
            initializeManagerUI();
            manager.startGame(3, "Human vs Human", true);

            ChessGame game = manager.getCurrentGame();
            Board board = game.getBoard();
            Piece pawn = board.getPieceAt(1, 0);
            
            Move move = new Move(
                pawn.getPosition(),
                new Coordinate(2, 0),
                pawn,
                null
            );

            int initialHistorySize = game.getMoveHistory().size();
            manager.makeMove(move);
            
            // Verify move was recorded
            assertTrue(game.getMoveHistory().size() > initialHistorySize);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testProcessAIMovesWithHumanPlayerDoesNothing() {
        try {
            initializeManagerUI();
            manager.startGame(3, "Human vs Human", true);
            
            ChessGame game = manager.getCurrentGame();
            int initialMoves = game.getMoveHistory().size();
            
            manager.processAIMoves();
            
            // No AI players, so no moves should be made
            assertEquals(initialMoves, game.getMoveHistory().size());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testSaveGameWithValidGame() {
        try {
            initializeManagerUI();
            manager.startGame(3, "Human vs Human", true);
            
            ChessGame game = manager.getCurrentGame();
            Board board = game.getBoard();
            Piece pawn = board.getPieceAt(1, 0);
            Move move = new Move(pawn.getPosition(), new Coordinate(2, 0), pawn, null);
            game.addMove(move);
            
            // Should not throw exception
            manager.saveGame("test_game");
            assertTrue(true);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testLoadGameInvalidFileReturnsFalse() {
        boolean loaded = manager.loadGame("nonexistent_file.pgn");
        assertFalse(loaded);
    }

    @Test
    public void testCheckGameOverWithNoGame() {
        assertFalse(manager.checkAndHandleGameOver());
    }

    @Test
    public void testCheckGameOverWithActiveGame() {
        try {
            initializeManagerUI();
            manager.startGame(3, "Human vs Human", true);
            
            // Game just started, should not be over
            assertFalse(manager.checkAndHandleGameOver());
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testCheckGameOverWithCheckmate() {
        try {
            initializeManagerUI();
            manager.startGame(3, "Human vs Human", true);

            ChessGame game = manager.getCurrentGame();
            RuleEngine engine = game.getRuleEngine();

            // Force checkmate using reflection
            Field f = RuleEngine.class.getDeclaredField("isCheckmate");
            f.setAccessible(true);
            f.setBoolean(engine, true);

            boolean gameOver = manager.checkAndHandleGameOver();
            assertTrue(gameOver);
        } catch (HeadlessException e) {
            // Expected in headless mode - dialog can't show
            assertTrue(true);
        } catch (Exception e) {
            fail("Test failed: " + e.getMessage());
        }
    }

    @Test
    public void testGettersBeforeInitialization() {
        assertNull(manager.getCurrentGame());
        assertNull(manager.getGameScreen());
        assertNull(manager.getGuiBoard());
        assertNull(manager.getMainFrame());
        assertNull(manager.getMainPanel());
        assertNull(manager.getCardLayout());
    }

}