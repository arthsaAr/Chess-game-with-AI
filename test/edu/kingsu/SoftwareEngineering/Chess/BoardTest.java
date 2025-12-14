package edu.kingsu.SoftwareEngineering.Chess;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.beans.Transient;
import edu.kingsu.SoftwareEngineering.Chess.Main;

/**
 * JUnit test class for the {@link Board} class.
 * Tests initialization, piece placement, movement, board clearing,copying,
 * and string representation of the chess board(More to be added)
 * Covers valid/invalid moves and board boundaries.
 * 
 * Author: Group3
 * Version: 1.0
 */
public class BoardTest  {
  /** The board instance used for testing. */
  private Board board;  //for testing board class

  @Before
  public void setupChess() {
    board = new Board(null);
  }

  @After
  public void tearDownChess() {
    board = null;
  }

  /**
   * Tests if board initializes correctly with non-null squares
   * and proper dimensions.
   */
  @Test 
  public void testBoardInitializationisNotNull() {
    assertNotNull("Board squares should be initializes", board.getSquares());
    assertEquals("Board should have 8 rows", 8, board.getSquares().length);
    assertEquals("Board should have 8 columns", 8, board.getSquares()[0].length);
  }

  /**
   * Tests if pieces are correctly placed at the start of the game.
   */
  @Test
  public void testPiecePlacementAtStart() {
    Piece whitePawn = board.getPieceAt(1, 0);
    Piece blackRook = board.getPieceAt(7,0);

    assertNotNull("White parn should be in row 1 and column 0", whitePawn);
    assertEquals("White", whitePawn.getColor());

    assertNotNull("Black rook should be in row 7 and column 0", blackRook);
    assertEquals("Black", blackRook.getColor());
  }

  @Test 
  public void testValidPosition() {
    assertTrue("Position (0,0) should be valid", board.isValidPosition(0,0));
    assertFalse("Position (8,8) should be invalid", board.isValidPosition(8,8));
    assertTrue("Position (4,5) should be valid", board.isValidPosition(4,5));
    assertFalse("Position (-1,0) should be invalid", board.isValidPosition(-1,-1));
  }

  @Test 
  public void testPieceMovement() {
    Piece piece = board.getPieceAt(1, 0);
    boolean isMoved = board.movePiece(1, 0, 3, 0, null, true);
    assertTrue("Move should be true for valid positions", isMoved);
    assertNull("Original position should have nothing after move", board.getPieceAt(1, 0));
    assertEquals("Piece should be at new position after moved", piece, board.getPieceAt(3, 0));
  }

  @Test 
  public void testInvalidPieceMovement() {
    boolean moved = board.movePiece(-1, 0, 2, 0, null, true);
    assertFalse("Move should not be valid for invalid source", moved);

    moved= board.movePiece(1,0,8,0, null, true);
    assertFalse("Move should not be valid for invalid place to move", moved);
  }

  @Test 
  public void testBoardClearing() {
    board.clear();
    for(int i=0; i<Board.ROWS; i++) {
      for(int j=0; j<Board.COLUMNS; j++) {
        assertNull("All positions should be null after clearing the board", board.getPieceAt(i,j));
      }
    }
  }

   /**
   * Tests if copying board creates a new board instance with the same pieces in corresponding positions.
   */
  @Test 
  public void testCopyingBoard() {
    Board copied = board.copy();
    assertNotNull(copied);
    assertNotSame("Copy should not be a same object", board, copied);
    assertNotNull("Piece at a e2 position should also exist in the copied board", copied.getPieceAt(1, 4));
  }

  /**
   * Tests if string representation of the board includes  rank numbers, file letters, and piece symbols.
   */
  @Test 
  public void testToStringhasRanksAndFiles() {
    String textIs = board.toString();
    assertTrue("Board string should have rank number 1", textIs.contains("1"));
    assertTrue("Board string should have letters from a to h", textIs.contains("a"));
    assertTrue("Board string should contain symbols for piece", textIs.contains("W"));
  }

  //tests for Board were successfull with total of 8 tests and no failure/errors

}
