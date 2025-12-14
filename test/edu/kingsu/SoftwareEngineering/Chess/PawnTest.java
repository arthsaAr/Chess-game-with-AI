package edu.kingsu.SoftwareEngineering.Chess;

import java.util.List;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.beans.Transient;
import edu.kingsu.SoftwareEngineering.Chess.Main;

/**
 * JUnit test class for the {@link Pawn} chess piece.
 * Tests pawn movement, double movement on first move, blocking, and capturing behavior.
 * 
 * Author: Group3
 * Version: 1.0
 */
public class PawnTest {
    private Board board;
    private Pawn white;
    private Pawn black;

    @Before
    public void initialize() {
        board = new Board(null);
        white = new Pawn("White", new Coordinate(1, 5));
        black = new Pawn("Black", new Coordinate(6, 3));

        board.setPieceAt(1, 5, white);
        board.setPieceAt(6, 3, black);
    }

    @After 
    public void destroy() {
        board = null;
        white = null;
        black= null;
    }

    @Test 
    public void testPawnMovemenet() {
        List<Coordinate> moves = white.getLegalMoves(board);
        assertTrue("White pawn should be able to move forward ", moves.contains(new Coordinate(2,5)));
    }

    @Test 
    public void testPawnDoubleMovement() {
        List<Coordinate> moves = white.getLegalMoves(board);
        assertTrue("Pawn can also have 2 moves available when it's their first move", moves.contains(new Coordinate(3,5)));
    }

    @Test 
    public void testPawnBlocked() {
        Pawn blocker = new Pawn("White", new Coordinate(2, 5));
        board.setPieceAt(2, 5, blocker);

        List<Coordinate> moves = white.getLegalMoves(board);
        assertFalse("Pawn should not move forward if its blocked", moves.contains(new Coordinate(2, 5)));
    }

    @Test
    public void testPawnCaptureSameColor() {
        Pawn sameColor = new Pawn("White", new Coordinate(2, 4));
        board.setPieceAt(2, 4, sameColor);

        List<Coordinate> moves = sameColor.getLegalMoves(board);

        assertFalse("Pawn should not capture friendly piece", moves.contains(new Coordinate(2, 4)));
    }
}
