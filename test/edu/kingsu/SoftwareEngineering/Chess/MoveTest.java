package edu.kingsu.SoftwareEngineering.Chess;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MoveTest {

    private Coordinate from;
    private Coordinate to;
    private Pawn pawn;
    private Queen queen;
    private Move move;

    @Before
    public void setup() {
        from = new Coordinate(1, 4);
        to = new Coordinate(3, 4);
        pawn = new Pawn("White", from);
        queen = new Queen("Black", to);
        move = new Move(from, to, pawn, null);
    }

    @Test
    public void testBasicConstructor() {
        assertEquals(from, move.getFrom());
        assertEquals(to, move.getTo());
        assertEquals(pawn, move.getMovedPiece());
        assertNull(move.getCapturedPiece());
        assertNotNull(move.getLegalMoves());
    }

    @Test
    public void testEnhancedConstructor() {
        List<Coordinate> legal = new ArrayList<>();
        legal.add(new Coordinate(2, 4));
        legal.add(new Coordinate(3, 4));
        Move m = new Move(from, to, pawn, queen, legal, "Q");
        assertEquals(2, m.getLegalMoves().size());
        assertEquals("Q", m.getPromotionPiece());
        assertEquals(queen, m.getCapturedPiece());
    }

    @Test
    public void testSquareConstructor() {
        Square s1 = new Square(1, 0);
        Square s2 = new Square(2, 0);
        Pawn p = new Pawn("White", new Coordinate(1, 0));
        s1.setPiece(p);
        Move m = new Move(s1, s2);
        assertEquals(1, m.getFrom().getRow());
        assertEquals(2, m.getTo().getRow());
        assertEquals(p, m.getMovedPiece());
    }

    @Test
    public void testSetMovedPiece() {
        move.setMovedPiece(queen);
        assertEquals(queen, move.getMovedPiece());
    }

    @Test
    public void testSetCapturedPiece() {
        move.setCapturedPiece(queen);
        assertEquals(queen, move.getCapturedPiece());
    }

    @Test
    public void testRookMovement() {
        Coordinate rFrom = new Coordinate(0, 0);
        Coordinate rTo = new Coordinate(0, 3);
        move.setRookMovement(rFrom, rTo);
        assertEquals(rFrom, move.getRookFrom());
        assertEquals(rTo, move.getRookTo());
    }

    @Test
    public void testPromotion() {
        move.setPromotionPieces("Q");
        assertEquals("Q", move.getPromotionPiece());
    }

    @Test
    public void testTimestamp() {
        assertTrue(move.getTimestamp() > 0);
    }

    @Test
    public void testLegalMovesDefensiveCopy() {
        List<Coordinate> legal = new ArrayList<>();
        legal.add(new Coordinate(2, 4));
        move.setLegalMoves(legal);
        List<Coordinate> returned = move.getLegalMoves();
        returned.clear();
        assertEquals(1, move.getLegalMoves().size());
    }

    @Test
    public void testGenerateDetailedLog_NoCapture() {
        String log = move.generateDetailedLog();
        assertTrue(log.contains("Pawn"));
        assertTrue(log.contains("White"));
        assertTrue(log.contains("e2"));
        assertTrue(log.contains("e4"));
    }

    @Test
    public void testGenerateDetailedLog_WithCapture() {
        move.setCapturedPiece(queen);
        String log = move.generateDetailedLog();
        assertTrue(log.contains("captures"));
    }

    @Test
    public void testGenerateMoveReport() {
        List<Coordinate> legal = new ArrayList<>();
        legal.add(new Coordinate(2, 4));
        legal.add(new Coordinate(3, 4));
        move.setLegalMoves(legal);
        String report = move.generateMoveReport();
        assertTrue(report.contains("Pawn"));
        assertTrue(report.contains("legal moves: 2"));
        assertTrue(report.contains("-> (2,4)"));
        assertTrue(report.contains("-> (3,4)"));
    }

    @Test
    public void testGenerateMoveReport_WithCapture() {
        move.setCapturedPiece(queen);
        String report = move.generateMoveReport();
        assertTrue(report.contains("Capture"));
        assertTrue(report.contains("Queen"));
    }

    @Test
    public void testToString() {
        String text = move.toString();
        assertTrue(text.contains("Pawn"));
        assertTrue(text.contains(from.toString()));
        assertTrue(text.contains(to.toString()));
    }
}
