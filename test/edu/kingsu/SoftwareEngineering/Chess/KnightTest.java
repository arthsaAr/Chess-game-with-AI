package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;

/**
 * JUnit test class for the {@link Knight} chess piece.
 * Tests copying, legal moves (including blocked moves), symbol, and attacking behavior.
 * 
 * Author: Group3
 * Version: 1.0
 */
public class KnightTest {
    private Board board;
    private Knight whiteKnight;
    private Knight blackKnight;

    @Before 
    public void setup() {
        board = new Board(null);

        whiteKnight = new Knight("White", new Coordinate(4, 4));
        board.setPieceAt(4, 4, whiteKnight);
        blackKnight = new Knight("Black", new Coordinate(2, 2));
        board.setPieceAt(2, 2, blackKnight);
    }

    @After 
    public void destroy() {
        board = null;
        whiteKnight = null;
        blackKnight = null;
    }

    @Test
    public void testCopy() {
        Piece copied = whiteKnight.copy();
        assertNotSame("Copied Knight should be different as a object", whiteKnight, copied);
        assertEquals("Both knights should have same color", whiteKnight.getColor(), copied.getColor());
        assertEquals("Both knights should have same position", whiteKnight.getRow(), copied.getRow());
        assertEquals("Both knights should have same position", whiteKnight.getCol(), copied.getCol());
    }

    @Test
    public void testMovesWhenBlocked() {
        // Put a friendly piece at one of the Knight's target squares
        Rook sameColor = new Rook("White", new Coordinate(6, 5));
        board.setPieceAt(6, 5, sameColor);

        List<Coordinate> moves = whiteKnight.getLegalMoves(board);

        // Knight should NOT be able to move to (6,5)
        assertFalse("Knight cannot move to square with same color", moves.contains(new Coordinate(6,5)));
    }

    @Test
    public void testSymbolGetter() {
        assertEquals("N", whiteKnight.getSymbol());
        assertEquals("N", blackKnight.getSymbol());
    }

    @Test
    public void testAttack() {
        Coordinate toEat = new Coordinate(6,5);
        Rook newK = new Rook("Black", toEat);
        board.setPieceAt(toEat.getRow(), toEat.getCol(), newK);

        assertTrue("Knight should be attacking enemy piece at (6,5)", whiteKnight.isAttacking(board, toEat));

        Coordinate emptySquare = new Coordinate(0,0);
        assertFalse("Knight should not be attacking empty square (0,0)", whiteKnight.isAttacking(board, emptySquare));
    }
}
