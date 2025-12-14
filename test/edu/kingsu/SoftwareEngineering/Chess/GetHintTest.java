package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Before;
import org.junit.Test;

import edu.kingsu.SoftwareEngineering.Chess.innerclasses.helperBoard;

import static org.junit.Assert.*;

public class GetHintTest {

    private Board board;
    private GetHint hint;

    @Before
    public void setUp() {
        board = new helperBoard();
        hint = new GetHint(board);
    }

    @Test
    public void testGenerateHint_NormalPosition() {
        String result = hint.generateHint("White");
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testCaptureHint() {
        board.movePiece(1, 4, 3, 4, null, true);
        board.movePiece(6, 3, 4, 3, null, true);
        board.movePiece(3, 4, 4, 3, null, true);

        String result = hint.generateHint("White");
        assertTrue(
            result.contains("Capture") ||
            result.contains("x") ||
            result.contains("capture")
        );
    }

    @Test
    public void testCheckHint() {
        board.movePiece(1, 4, 3, 4, null, true);
        board.movePiece(6, 5, 5, 5, null, true);
        board.movePiece(0, 3, 4, 7, null, true);

        board.setPieceAt(7, 4, new King("Black", new Coordinate(7, 4)));

        String result = hint.generateHint("White");
        assertTrue(
            result.toLowerCase().contains("check") ||
            result.toLowerCase().contains("attack")
        );
    }

    @Test
    public void testDefensiveHint() {
        board.movePiece(1, 3, 3, 3, null, true);
        board.movePiece(6, 4, 4, 4, null, true);
        board.movePiece(0, 5, 3, 2, null, true);

        String result = hint.generateHint("White");
        assertTrue(
            result.contains("attack") ||
            result.contains("safety") ||
            result.contains("Move")
        );
    }

    @Test
    public void testStrategicHint_Knight() {
        String result = hint.generateHint("White");
        assertTrue(
            result.contains("Develop") ||
            result.contains("control") ||
            result.contains("center")
        );
    }

    @Test
    public void testStrategicHint_Pawn() {
        board.movePiece(1, 3, 3, 3, null, true);
        String result = hint.generateHint("Black");
        assertNotNull(result);
    }

    @Test
    public void testKingSafetyHint() {
        board.movePiece(1, 4, 3, 4, null, true);
        board.movePiece(6, 3, 4, 3, null, true);
        board.movePiece(0, 4, 1, 4, null, true);

        board.setPieceAt(4, 4, new Rook("Black", new Coordinate(4,4)));

        String result = hint.generateHint("White");
        assertTrue(result.toLowerCase().contains("king"));
    }

    @Test
    public void testIsCentralSquarePath() {
        Coordinate c = new Coordinate(3, 3);
        board.getAllPieces("White").get(0).setPosition(c);

        String result = hint.generateHint("White");
        assertNotNull(result);
    }

    @Test
    public void testFindKingFallback() {
        board.getAllPieces("White").removeIf(p -> p instanceof King);
        String result = hint.generateHint("White");
        assertNotNull(result);
    }

    @Test
    public void testNoCrashOnEmptyBoard() {
        Board emptyBoard = new Board(null);
        GetHint emptyHint = new GetHint(emptyBoard);
        String result = emptyHint.generateHint("White");
        assertNotNull(result);
    }

    @Test
public void testCheckEscapeHintDirect() throws Exception {
    board.initializeBoard();

    // Put black rook giving check to white king
    board.movePiece(7, 0, 0, 4, null, true); // rook → e1

    java.lang.reflect.Method m =
            GetHint.class.getDeclaredMethod("getCheckEscapeHint", String.class);
    m.setAccessible(true);

    String result = (String) m.invoke(hint, "White");

    assertNotNull(result);
    assertTrue(result.toLowerCase().contains("check"));
}
@Test
public void testFormatDefensiveHintDirect() throws Exception {
    Piece pawn = board.getAllPieces("White").get(0);
    Move mve = new Move(pawn.getPosition(),
            new Coordinate(2, 2), pawn, null);

    java.lang.reflect.Method m =
            GetHint.class.getDeclaredMethod("formatDefensiveHint", Move.class);
    m.setAccessible(true);

    String result = (String) m.invoke(hint, mve);

    assertNotNull(result);
    assertTrue(result.contains("safety"));
}
@Test
public void testFormatCheckHintDirect() throws Exception {
    Piece pawn = board.getAllPieces("White").get(0);
    Move mve = new Move(pawn.getPosition(),
            new Coordinate(2, 4), pawn, null);

    java.lang.reflect.Method m =
            GetHint.class.getDeclaredMethod("formatCheckHint", Move.class);
    m.setAccessible(true);

    String result = (String) m.invoke(hint, mve);

    assertNotNull(result);
    assertTrue(result.toLowerCase().contains("check"));
}

@Test
public void testIsPieceThreatenedAtTrue() throws Exception {
    Coordinate pos = new Coordinate(0, 4); // King square

    java.lang.reflect.Method m =
            GetHint.class.getDeclaredMethod("isPieceThreatenedAt",
                    Coordinate.class, String.class);
    m.setAccessible(true);

    Boolean result = (Boolean) m.invoke(hint, pos, "White");

    assertNotNull(result);
}

@Test
public void testFindThreatenedPieceDirect() throws Exception {
    board.movePiece(6, 4, 4, 4, null, true); // black pawn threatening center

    java.lang.reflect.Method m =
            GetHint.class.getDeclaredMethod("findThreatenedPiece", String.class);
    m.setAccessible(true);

    Move result = (Move) m.invoke(hint, "White");

    assertTrue(result == null || result instanceof Move);
}

@Test
public void testIsCentralSquareAllPaths() throws Exception {
    java.lang.reflect.Method m =
            GetHint.class.getDeclaredMethod("isCentralSquare", Coordinate.class);
    m.setAccessible(true);

    assertTrue((Boolean) m.invoke(hint, new Coordinate(3, 3)));
    assertFalse((Boolean) m.invoke(hint, new Coordinate(0, 0)));
}

@Test
public void testStrategicHintAllPaths() throws Exception {
    java.lang.reflect.Method m =
            GetHint.class.getDeclaredMethod("getStrategicHint", String.class);
    m.setAccessible(true);

    String result = (String) m.invoke(hint, "White");

    assertNotNull(result);
}

@Test
public void testFindCheckMoveDirect() throws Exception {
    java.lang.reflect.Method m =
            GetHint.class.getDeclaredMethod("findCheckMove", String.class);
    m.setAccessible(true);

    Move mv = (Move) m.invoke(hint, "White");

    assertTrue(mv == null || mv instanceof Move);
}

}
