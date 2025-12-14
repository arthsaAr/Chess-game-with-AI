package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * JUnit test class for the Bishop chess piece.
 * Tests the five methods implemented in Bishop class.
 *
 * @author Group3
 * @version 1.0
 */
public class BishopTest {

    private Bishop whiteBishop;
    private Bishop cornerBishop;
    private Board board;

    @Before
    public void setUp() {
        board = new Board(null);
        whiteBishop = new Bishop("White", new Coordinate(4, 4));
        cornerBishop = new Bishop("White", new Coordinate(0, 0));
    }


    @Test
    public void testConstructor() {
        Bishop bishop = new Bishop("White", new Coordinate(3, 5));
        assertEquals("White", bishop.getColor());
        assertEquals(3, bishop.getPosition().getRow());
        assertEquals(5, bishop.getPosition().getCol());
    }


    @Test
    public void testGetSymbol() {
        assertEquals("B", whiteBishop.getSymbol());
        assertEquals("B", cornerBishop.getSymbol());
    }


    @Test
    public void testGetValue() {
        assertEquals(3, whiteBishop.getValue());
        assertEquals(3, cornerBishop.getValue());
    }

    // getLegalMoves() Tests

    //need to work on legalMoves as the tests are not yet working for it
    @Test
    public void testLegalMovesEmptyBoardCenter() {
        board = new Board(null);
        board.setPieceAt(4, 4, whiteBishop);

        List<Coordinate> moves = whiteBishop.getLegalMoves(board);
        assertEquals(8, moves.size());
    }

    @Test
    public void testLegalMovesCorner() {
        board= new Board(null);
        board.clear();
        board.setPieceAt(0, 0, cornerBishop);

        List<Coordinate> moves = cornerBishop.getLegalMoves(board);
        assertEquals(7, moves.size());
    }

    @Test
    public void testLegalMovesStopsAtPiece() {
        board.setPieceAt(4, 4, whiteBishop);
        board.setPieceAt(6, 6, new Pawn("Black", new Coordinate(6, 6)));
        
        List<Coordinate> moves = whiteBishop.getLegalMoves(board);
        
        //Includes (6,6) not (7,7)
        boolean has66 = false;
        boolean has77 = false;
        for (Coordinate c : moves) {
            if (c.getRow() == 6 && c.getCol() == 6) has66 = true;
            if (c.getRow() == 7 && c.getCol() == 7) has77 = true;
        }
        assertTrue(has66);
        assertFalse(has77);
    }

     /**
     * Tests that all legal moves of a bishop are diagonal
     */
    @Test
    public void testAllMovesAreDiagonal() {
        List<Coordinate> moves = whiteBishop.getLegalMoves(board);
        
        int startRow = whiteBishop.getPosition().getRow();
        int startCol = whiteBishop.getPosition().getCol();
        
        for (Coordinate move : moves) {
            int rowDiff = Math.abs(move.getRow() - startRow);
            int colDiff = Math.abs(move.getCol() - startCol);
            assertEquals(rowDiff, colDiff);
        }
    }

    /**
     * Tests that all legal moves are within the board boundaries.
     */
    @Test
    public void testMovesWithinBounds() {
        List<Coordinate> moves = whiteBishop.getLegalMoves(board);
        
        for (Coordinate move : moves) {
            assertTrue(move.getRow() >= 0 && move.getRow() < 8);
            assertTrue(move.getCol() >= 0 && move.getCol() < 8);
        }
    }

    /**
     * Testings if bishop can move in all four directions
     */
    @Test
    public void testAllFourDiagonalDirections() {
        List<Coordinate> moves = whiteBishop.getLegalMoves(board);
        
        // From (4,4) check all four diagonal directions
        boolean hasNE = false;
        boolean hasNW = false;
        boolean hasSE = false;
        boolean hasSW = false;
        
        for (Coordinate c : moves) {
            if (c.getRow() < 4 && c.getCol() > 4) hasNE = true;
            if (c.getRow() < 4 && c.getCol() < 4) hasNW = true;
            if (c.getRow() > 4 && c.getCol() > 4) hasSE = true;
            if (c.getRow() > 4 && c.getCol() < 4) hasSW = true;
        }
        
        assertTrue(hasNE);
        assertTrue(hasNW);
        assertTrue(hasSE);
        assertTrue(hasSW);
    }

    @Test
    public void testGetLegalMovesNotNull() {
        assertNotNull(whiteBishop.getLegalMoves(board));
    }

    /**
     * Testings legal moves when bishop is blocked by pieces
     */
    @Test
    public void testLegalMovesBlockedImmediately() {
        board.setPieceAt(4, 4, whiteBishop);
        board.setPieceAt(3, 3, new Pawn("Black", new Coordinate(3, 3)));
        board.setPieceAt(3, 5, new Pawn("Black", new Coordinate(3, 5)));
        board.setPieceAt(5, 3, new Pawn("Black", new Coordinate(5, 3)));
        board.setPieceAt(5, 5, new Pawn("Black", new Coordinate(5, 5)));
        
        List<Coordinate> moves = whiteBishop.getLegalMoves(board);
        assertEquals(4, moves.size());
    }

    // ==================== copy() Tests ====================

    @Test
    public void testCopy() {
        whiteBishop.setHasMoved(true);
        Bishop copy = (Bishop) whiteBishop.copy();
        
        assertNotSame(whiteBishop, copy);
        assertEquals(whiteBishop.getColor(), copy.getColor());
        assertEquals(whiteBishop.getPosition().getRow(), copy.getPosition().getRow());
        assertEquals(whiteBishop.getPosition().getCol(), copy.getPosition().getCol());
        assertEquals(whiteBishop.hasMoved(), copy.hasMoved());
    }

    /**
     * Testing if copy() creates a deep copy of the bishop position.
     */
    @Test
    public void testCopyDeepCopiesPosition() {
        Bishop copy = (Bishop) whiteBishop.copy();
        assertNotSame(whiteBishop.getPosition(), copy.getPosition());
    }

    /**
     * testing if copy() preserves the hasMoved property.
     */
    @Test
    public void testCopyPreservesHasMoved() {
        whiteBishop.setHasMoved(true);
        Bishop copy = (Bishop) whiteBishop.copy();
        assertTrue(copy.hasMoved());
        
        cornerBishop.setHasMoved(false);
        Bishop copy2 = (Bishop) cornerBishop.copy();
        assertFalse(copy2.hasMoved());
    }
}

