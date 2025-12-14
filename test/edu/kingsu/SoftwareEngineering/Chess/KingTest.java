package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JUnit test class for the {@link King} chess piece.
 * Tests the king's legal moves, attacking capability, copying, and basic properties like symbol and value.
 * Includes tests for corner and center positions.
 * 
 * Author: Group3
 * Version: 1.0
 */

public class KingTest {
    
    private kingBoard board;
    private King whiteKing;
    private King blackKing;

    @Before
    public void setup() {
        board = new kingBoard();
        whiteKing = new King("White", new Coordinate(4, 4));
        blackKing = new King("Black", new Coordinate(0, 0));
        board.setPieceAt(4, 4, whiteKing);
        board.setPieceAt(0, 0, blackKing);
    }

    @After 
    public void destroy() {
        whiteKing = null;
        blackKing = null;
        board = null;
    }

    @Test
    public void testLegalMovesWhenInCenter() {
        List<Coordinate> moves = whiteKing.getLegalMoves(board);
        assertEquals("King in center should have total of 8 moves", 8, moves.size());
    }

    @Test
    public void testLegalMovesWhenInCorner() {
        List<Coordinate> moves = blackKing.getLegalMoves(board);
        assertEquals("King in corner should have only 3 moves", 3, moves.size());
    }

    @Test
    public void testKingIsAttacking() {
        Coordinate inFront = new Coordinate(5, 4);
        Coordinate faraway = new Coordinate(6, 4);
        assertTrue(whiteKing.isAttacking(board, inFront));  //if a piece is in front it should attack
        assertFalse(whiteKing.isAttacking(board, faraway)); //if piece is far no use for king to attack/needs multiple steps
    }

    @Test
    public void testKingCopy() {
        King copy = (King) whiteKing.copy();
        assertNotSame("Copy should be different object when compared ", whiteKing, copy);
        assertEquals("Copied king should have same position row", whiteKing.getRow(), copy.getRow());
        assertEquals("Copied king should have same position col", whiteKing.getCol(), copy.getCol());
        assertEquals("Copied kign should have same color", whiteKing.getColor(), copy.getColor());
    }

    @Test
    public void TestKingSymbol() {
        assertEquals("Symbol should be K for king", "K", whiteKing.getSymbol());
        assertEquals("King value should be 1000", 1000, whiteKing.getValue());
    }
}
