package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit test class for the {@link Rook} chess piece.
 * Test initialization, symbol, value, copying, and legal move behavior, including movement blocked by same-color pieces.
 * 
 * Author: Group3
 * Version: 1.0
 */
public class RookTest {
    private Board board;
    private Rook white;
    private Rook black;

    @Before
    public void setup() {
        board = new Board(null);
        board.clear();
        white = new Rook("White", new Coordinate(0, 0));
        black = new Rook("Black", new Coordinate(7, 7));

        board.setPieceAt(0, 0, white);
        board.setPieceAt(7, 7, black);
    }

    @After
    public void destroy() {
        board = null;
        black = null;
        white = null;
    }

    @Test
    public void testInitializationofRook() {
        assertEquals("White Rook row is", 0, white.getRow());
        assertEquals("White Rook col is ", 0, white.getCol());
        assertEquals("White Rook color", "White", white.getColor());

        assertEquals("Black Rook row is ", 7, black.getRow());
        assertEquals("Black Rook col is", 7, black.getCol());
        assertEquals("Black Rook color", "Black", black.getColor());
    }

    @Test
    public void testSymbolGetter() {
        assertEquals("White Rook symbol", "R", white.getSymbol());
        assertEquals("Black Rook symbol", "R", black.getSymbol());
    }

    @Test
    public void testGetter() {
        assertEquals(5, white.getValue());
        assertEquals(5, black.getValue());
    }

    @Test
    public void testCopying() {
        Rook copied = (Rook) white.copy();
        assertNotSame("Copy should be a different object when compared", white, copied);
        assertEquals("Copy should have the same color", white.getColor(), copied.getColor());
        assertEquals("Copied rook should be in same position", white.getPosition(), copied.getPosition());
    }

    //need to work on legalMoves as the tests are not yet working for it
    @Test
    public void testBlockedbySameColor() {
        Rook friendRook = new Rook("White", new Coordinate(0, 3));
        board.setPieceAt(0, 3, friendRook);

        List<Coordinate> moves = white.getLegalMoves(board);
        assertFalse(moves.contains(new Coordinate(0, 3)));
        assertTrue(moves.contains(new Coordinate(0, 2)));
    }

    @Test
    public void testLegalMoves() {
        List<Coordinate> moves = white.getLegalMoves(board);
        assertTrue(moves.contains(new Coordinate(1, 0)));
        assertTrue(moves.contains(new Coordinate(0, 1)));
    }
}
