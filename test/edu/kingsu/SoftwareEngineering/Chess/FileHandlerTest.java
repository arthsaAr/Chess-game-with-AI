package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileHandlerTest {

    private FileHandler fileHandler;
    private String testFile;

    @Before
    public void setUp() {
        fileHandler = new FileHandler();
        testFile = "test_game.pgn";
    }

    @After
    public void tearDown() {
        File f = new File(testFile);
        if (f.exists()) {
            f.delete();
        }
        fileHandler = null;
    }

    // ================= SAVE GAME =================

    @Test
    public void testSaveGameCreatesFile() {
        List<Move> moves = createDummyMoves();

        fileHandler.saveGame(
                moves,
                testFile,
                "Human vs AI",
                6,
                true
        );

        File f = new File(testFile);
        assertTrue("PGN file should be created", f.exists());
        assertTrue("PGN file should not be empty", f.length() > 0);
    }

    // ================= LOAD GAME BASIC =================

    @Test
    public void testLoadGameReturnsGameState() {
        List<Move> moves = createDummyMoves();

        fileHandler.saveGame(
                moves,
                testFile,
                "Human vs Human",
                3,
                true
        );

        FileHandler.GameState state = fileHandler.loadGame(testFile);

        assertNotNull(state);
        assertNotNull(state.game);
        assertEquals("Human vs Human", state.gameMode);
        assertEquals(3, state.aiLevel);
        assertTrue(state.playerWhite);
    }

    // ================= LOAD GAME WITH AI =================

    @Test
    public void testLoadGameHumanVsAI() {
        List<Move> moves = createDummyMoves();

        fileHandler.saveGame(
                moves,
                testFile,
                "Human vs AI",
                7,
                false
        );

        FileHandler.GameState state = fileHandler.loadGame(testFile);

        assertNotNull(state);
        assertEquals("Human vs AI", state.gameMode);
        assertEquals(7, state.aiLevel);
        assertFalse(state.playerWhite);

        assertNotNull(state.game.getPlayers().get(0));
        assertNotNull(state.game.getPlayers().get(1));
    }

    // ================= AI VS AI MODE =================

    @Test
    public void testLoadGameAIvsAI() {
        List<Move> moves = createDummyMoves();

        fileHandler.saveGame(
                moves,
                testFile,
                "AI vs AI",
                9,
                true
        );

        FileHandler.GameState state = fileHandler.loadGame(testFile);

        assertEquals("AI vs AI", state.gameMode);
        assertEquals(9, state.aiLevel);
        assertTrue(state.game.getPlayers().get(0) instanceof AIPlayer);
        assertTrue(state.game.getPlayers().get(1) instanceof AIPlayer);
    }

    // ================= EMPTY MOVE SAVE =================

    @Test
    public void testSaveGameWithNoMovesDoesNotCrash() {
        fileHandler.saveGame(
                new ArrayList<Move>(),
                testFile,
                "Human vs Human",
                5,
                true
        );

        File f = new File(testFile);
        assertTrue(f.exists());
    }

    // ================= LOAD INVALID FILE =================

    @Test
    public void testLoadInvalidFileReturnsNull() {
        FileHandler.GameState state = fileHandler.loadGame("file_does_not_exist.pgn");
        assertNull(state);
    }

    // ================= MOVE ROUND TRIP =================

    @Test
    public void testSaveAndReloadKeepsMoves() {
        List<Move> moves = createDummyMoves();

        fileHandler.saveGame(
                moves,
                testFile,
                "Human vs Human",
                4,
                true
        );

        FileHandler.GameState state = fileHandler.loadGame(testFile);

        assertNotNull(state);
        assertEquals(moves.size(), state.game.getMoveHistory().size());
    }

    // ================= HELPERS =================

    private List<Move> createDummyMoves() {
        List<Move> moves = new ArrayList<Move>();

        Board board = new Board(null);
        board.initializeBoard();

        Piece pawn = board.getPieceAt(1, 4); // White pawn e2
        Coordinate to = new Coordinate(3, 4); // e4

        Move m1 = new Move(
                pawn.getPosition(),
                to,
                pawn,
                null
        );

        board.setPieceAt(1, 4, null);
        board.setPieceAt(3, 4, pawn);
        pawn.setPosition(to);

        Piece pawn2 = board.getPieceAt(6, 3); // Black pawn d7
        Coordinate to2 = new Coordinate(4, 3); // d5

        Move m2 = new Move(
                pawn2.getPosition(),
                to2,
                pawn2,
                null
        );

        moves.add(m1);
        moves.add(m2);

        return moves;
    }
}
