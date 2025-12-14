package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;

import edu.kingsu.SoftwareEngineering.Chess.innerclasses.pieceBoard;

import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit test class for the abstract {@link Piece} class.
 * Uses a test subclass {@link PieceTester} to verify basic functionality:
 * - Legal moves
 * - Copying pieces
 * - Position and color
 * - Move validation
 * 
 * @Author Group3
 * Version: 1.0
 */
public class PieceTest {

    private Board board;
    private pieceBoard white;
    private pieceBoard black;

    @Before
    public void initialize() {
        board = new Board(null);
        white = new pieceBoard("White", new Coordinate(1, 1));
        black = new pieceBoard("Black", new Coordinate(2,2));
    }

    @After 
    public void destroy() {
        board = null;
        white = null;
        black = null;
    }

    @Test 
    public void testValidMove() {
        Board test = new Board(null);
        white.setPosition(new Coordinate(1, 1));
        test.setPieceAt(1, 1, white);
        Move aValidMove = new Move(new Coordinate(1, 1), new Coordinate(2, 1), white, null);
        Move aInvalidMove = new Move(new Coordinate(1, 1), new Coordinate(4, 1), white, null);
        assertTrue("Move should be valid", white.isMoveValid(test, aValidMove));
        assertFalse("Move should be invalid", white.isMoveValid(test, aInvalidMove));
    }

    @Test 
    public void testCopy() {
        Piece copied = white.copy();
        assertNotSame("Copied piece should be a different than the original piece", white, copied);
        assertEquals("Copied piece should have same color to the original", white.color, copied.color);
        assertEquals("Copied piece should have same position as the original piece", white.position, copied.position);
    }

     @Test
    public void colorTester() {
        assertEquals("White piece color", "White", white.getColor());
    }

     @Test
    public void positionTester() {
        assertEquals(1, white.getRow());
        assertEquals(1, white.getCol());
    }
    
     @Test
    public void sameColorTester() {
        assertTrue("Same color should return true", white.isSameColor(new pieceBoard("White", new Coordinate(3,3))));
        assertFalse("Different color should return false", white.isSameColor(black));
    }
}
